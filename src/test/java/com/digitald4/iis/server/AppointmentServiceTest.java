package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.SequenceStore;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.storage.Store;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.Appointment.Assessment;
import com.digitald4.iis.model.Appointment.Repeat;
import com.digitald4.iis.model.Appointment.Repeat.Type;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.NurseStore;
import com.digitald4.iis.storage.PatientStore;
import com.digitald4.iis.test.TestCase;
import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableSet;
import java.time.Clock;
import java.time.Instant;
import java.util.function.UnaryOperator;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentServiceTest extends TestCase {
	private static final Instant START = Instant.ofEpochMilli(DateTime.parse("2024-06-28").getMillis());
	private static final Appointment APPOINTMENT = new Appointment().setDate(START).setPatientId(123L).setNurseId(234L);
	@Mock private final DAO dao = mock(DAO.class);
	@Mock private final SessionStore<User> sessionStore = mock(SessionStore.class);
	@Mock private final PatientStore patientStore = mock(PatientStore.class);
	@Mock private final NurseStore nurseStore = mock(NurseStore.class);
	@Mock private final Store<Vendor, Long> vendorStore = mock(Store.class);
	@Mock private final Clock clock = mock(Clock.class);
	@Mock private final SequenceStore sequenceStore = mock(SequenceStore.class);
	private AppointmentService service;

	@Before
	public void setup() {
		when(sequenceStore.getAndIncrement(Repeat.class)).thenReturn(100L);
		service = new AppointmentService(new AppointmentStore(() -> dao, null, clock), sessionStore, sequenceStore);
	}

	@Test
	public void testMapToJSON() {
		Appointment appointment = new Appointment().setAssessments(
				ImmutableList.of(new Assessment(927L, "102"), new Assessment(292L, "Hello there")));
		String output = new JSONObject(appointment).toString();
		assertThat(output).contains("\"assessments\":[{\"typeId\":927,\"value\":\"102\"},{\"typeId\":292,\"value\":\"Hello there\"}]");
	}

	@Test
	public void testMapMerge() {
		Appointment appointment = JSONUtil.toObject(
				Appointment.class,
				new JSONObject("{\"assessments\":[{\"typeId\":927,\"value\":\"102\"},{\"typeId\":292,\"value\":\"Hello there\"}]}"));
		System.out.println(new JSONObject(appointment));
		assertThat(appointment.getAssessment(927L).getValue()).isEqualTo("102");
		assertThat(appointment.getAssessment(292L).getValue()).isEqualTo("Hello there");
	}

	@Test @Ignore
	public void testUpdateAssessment() throws Exception {
		Appointment appointment = new Appointment()
				.setStart(Instant.ofEpochMilli(1000))
				.setPatientId(45L)
				.setNurseId(23L)
				.setAssessments(
						ImmutableList.of(
								new Assessment(995L, "Good"),
								new Assessment(927L, "98.6F"),
								new Assessment(845L, "Hello")));
		when(dao.get(Patient.class, 45L)).thenReturn(new Patient().setId(45L).setName("George Man"));
		when(dao.get(Nurse.class, 23L)).thenReturn(new Nurse().setId(72L).setFirstName("Karen").setLastName("Lee"));
		when(dao.get(Appointment.class, 72L)).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(72L), any(UnaryOperator.class)))
				.then((i) -> i.getArgument(2, UnaryOperator.class).apply(appointment));

		Appointment result = service.update(
				72L,
				new Appointment().setAssessments(
						ImmutableList.of(
								new Assessment(995L, "Good"),
								new Assessment(927L, "102"),
								new Assessment(845L, "Hello"))),
				"assessments",
				null);

		assertThat(result.getAssessment(927L).getValue()).isEqualTo("102");
		assertThat(result.getPatientName()).isEqualTo("George Man");
		assertThat(result.getNurseName()).isEqualTo("Karen Lee");
		assertThat(result.getAssessment(995L).getValue()).isEqualTo("Good");
		assertThat(result.getAssessment(845L).getValue()).isEqualTo("Hello");
	}

	@Test
	public void expand_noRepeat() {
		Appointment appointment = createAppointment(null);
		assertThat(service.expand(appointment)).containsExactly(appointment);
	}

	@Test
	public void expand_doesNotRepeat() {
		Appointment appointment = createAppointment(new Repeat().setType(Type.Does_not_repeat));
		assertThat(service.expand(appointment)).containsExactly(appointment);
	}

	@Test
	public void expand_daily() {
		Repeat repeat = new Repeat().setType(Type.Daily).setVisits(7);
		assertThat(service.expand(createAppointment(repeat))).containsExactly(
				createAppointment(DateTime.parse("2024-06-28"), repeat),
				createAppointment(DateTime.parse("2024-06-29"), repeat),
				createAppointment(DateTime.parse("2024-06-30"), repeat),
				createAppointment(DateTime.parse("2024-07-01"), repeat),
				createAppointment(DateTime.parse("2024-07-02"), repeat),
				createAppointment(DateTime.parse("2024-07-03"), repeat),
				createAppointment(DateTime.parse("2024-07-04"), repeat));
	}

	@Test
	public void expand_dailyUntil() {
		Repeat repeat = new Repeat().setType(Type.Daily).setUntil(DateTime.parse("2024-07-03").getMillis());
		assertThat(service.expand(createAppointment(repeat))).containsExactly(
				createAppointment(DateTime.parse("2024-06-28"), repeat),
				createAppointment(DateTime.parse("2024-06-29"), repeat),
				createAppointment(DateTime.parse("2024-06-30"), repeat),
				createAppointment(DateTime.parse("2024-07-01"), repeat),
				createAppointment(DateTime.parse("2024-07-02"), repeat),
				createAppointment(DateTime.parse("2024-07-03"), repeat));
	}

	@Test
	public void expand_everyWeekDay() {
		Repeat repeat = new Repeat().setType(Type.Every_weekday).setVisits(7);
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-01"),
						DateTime.parse("2024-07-02"),
						DateTime.parse("2024-07-03"),
						DateTime.parse("2024-07-04"),
						DateTime.parse("2024-07-05"),
						DateTime.parse("2024-07-08"));
	}

	@Test
	public void expand_everyWeekDayUntil() {
		Repeat repeat = new Repeat().setType(Type.Every_weekday).setUntil(DateTime.parse("2024-07-08").getMillis());
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-01"),
						DateTime.parse("2024-07-02"),
						DateTime.parse("2024-07-03"),
						DateTime.parse("2024-07-04"),
						DateTime.parse("2024-07-05"),
						DateTime.parse("2024-07-08"));
	}

	@Test
	public void expand_weekly() {
		Repeat repeat = new Repeat().setType(Type.Weekly_on_same_day).setVisits(7);
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-05"),
						DateTime.parse("2024-07-12"),
						DateTime.parse("2024-07-19"),
						DateTime.parse("2024-07-26"),
						DateTime.parse("2024-08-02"),
						DateTime.parse("2024-08-09"));
	}

	@Test
	public void expand_weeklyUntil() {
		Repeat repeat = new Repeat().setType(Type.Weekly_on_same_day).setUntil(DateTime.parse("2024-08-09").getMillis());
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-05"),
						DateTime.parse("2024-07-12"),
						DateTime.parse("2024-07-19"),
						DateTime.parse("2024-07-26"),
						DateTime.parse("2024-08-02"),
						DateTime.parse("2024-08-09"));
	}

	@Test
	public void expand_monthly() {
		Repeat repeat = new Repeat().setType(Type.Monthly_on_same_day).setVisits(7);
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-28"),
						DateTime.parse("2024-08-28"),
						DateTime.parse("2024-09-28"),
						DateTime.parse("2024-10-28"),
						DateTime.parse("2024-11-28"),
						DateTime.parse("2024-12-28"));
	}

	@Test
	public void expand_monthlyUntil() {
		Repeat repeat = new Repeat().setType(Type.Monthly_on_same_day).setUntil(DateTime.parse("2024-12-28").getMillis());
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-28"),
						DateTime.parse("2024-08-28"),
						DateTime.parse("2024-09-28"),
						DateTime.parse("2024-10-28"),
						DateTime.parse("2024-11-28"),
						DateTime.parse("2024-12-28"));
	}

	@Test
	public void expand_nDays() {
		Repeat repeat = new Repeat().setType(Type.Every_N_days).setNumDays(28).setVisits(7);
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-26"),
						DateTime.parse("2024-08-23"),
						DateTime.parse("2024-09-20"),
						DateTime.parse("2024-10-18"),
						DateTime.parse("2024-11-15"),
						DateTime.parse("2024-12-13"));
	}

	@Test
	public void expand_nDaysUntil() {
		Repeat repeat =
				new Repeat().setType(Type.Every_N_days).setNumDays(28).setUntil(DateTime.parse("2024-12-13").getMillis());
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-26"),
						DateTime.parse("2024-08-23"),
						DateTime.parse("2024-09-20"),
						DateTime.parse("2024-10-18"),
						DateTime.parse("2024-11-15"),
						DateTime.parse("2024-12-13"));
	}

	@Test
	public void expand_weeklyOnDays() {
		// Every Monday, Wednesday and Friday for 7 visits.
		Repeat repeat = new Repeat().setType(Type.Weekly_on_days).setDays(ImmutableSet.of(2, 4, 6)).setVisits(7);
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(
						DateTime.parse("2024-06-28"),
						DateTime.parse("2024-07-01"),
						DateTime.parse("2024-07-03"),
						DateTime.parse("2024-07-05"),
						DateTime.parse("2024-07-08"),
						DateTime.parse("2024-07-10"),
						DateTime.parse("2024-07-12"));
	}

	@Test
	public void expand_weeklyOnDaysUntil() {
		Repeat repeat = new Repeat().setType(Type.Weekly_on_days)
				.setDays(ImmutableSet.of(2, 4, 6)).setUntil(DateTime.parse("2024-07-12").getMillis());
		assertThat(service.expand(createAppointment(repeat)).stream().map(Appointment::date).map(DateTime::new)
				.collect(toImmutableList()))
				.containsExactly(DateTime.parse("2024-06-28"), DateTime.parse("2024-07-01"), DateTime.parse("2024-07-03"),
						DateTime.parse("2024-07-05"), DateTime.parse("2024-07-08"), DateTime.parse("2024-07-10"), DateTime.parse("2024-07-12"));
	}

	private static Appointment createAppointment(Repeat repeat) {
		return APPOINTMENT.copy().setDate(START).setRepeat(repeat);
	}

	private static Appointment createAppointment(DateTime date, Repeat repeat) {
		return APPOINTMENT.copy().setDate(Instant.ofEpochMilli(date.getMillis())).setRepeat(repeat).setSeriesId(100L);
	}
}
