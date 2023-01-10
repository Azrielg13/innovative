package com.digitald4.iis.server;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.storage.Store;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.Appointment.Assessment;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.NurseStore;
import com.digitald4.iis.test.TestCase;
import com.google.common.collect.ImmutableList;

import java.time.Clock;
import java.util.function.UnaryOperator;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentServiceTest extends TestCase {
	@Mock private final DAO dao = mock(DAO.class);
	@Mock private final SessionStore<User> sessionStore = mock(SessionStore.class);
	@Mock private final Store<Patient, Long> patientStore = mock(Store.class);
	@Mock private final NurseStore nurseStore = mock(NurseStore.class);
	@Mock private final Store<Vendor, Long> vendorStore = mock(Store.class);
	@Mock private final Clock clock = mock(Clock.class);

	@Test
	public void testMapToJSON() {
		Appointment appointment = new Appointment().setAssessments(
				ImmutableList.of(new Assessment(927L, "102"), new Assessment(292L, "Hello there")));
		String output = new JSONObject(appointment).toString();
		System.out.println(output);
		assertTrue(output.contains("\"assessments\":[{\"typeId\":927,\"value\":\"102\"},{\"typeId\":292,\"value\":\"Hello there\"}]"));
	}

	@Test
	public void testMapMerge() {
		Appointment appointment = JSONUtil.toObject(
				Appointment.class,
				new JSONObject("{\"assessments\":[{\"typeId\":927,\"value\":\"102\"},{\"typeId\":292,\"value\":\"Hello there\"}]}"));
		System.out.println(new JSONObject(appointment));
		assertEquals("102", appointment.getAssessment(927L).getValue());
		assertEquals("Hello there", appointment.getAssessment(292L).getValue());
	}

	@Test
	public void testUpdateAssessment() throws Exception {
		Appointment appointment = new Appointment()
				.setId(72L)
				.setStart(new DateTime(1000))
				.setPatientId(45L)
				.setNurseId(23L)
				.setAssessments(
						ImmutableList.of(
								new Assessment(995L, "Good"),
								new Assessment(927L, "98.6F"),
								new Assessment(845L, "Hello")));
		when(patientStore.get(45L)).thenReturn(new Patient().setId(45L).setName("George Man"));
		when(nurseStore.get(23L)).thenReturn(new Nurse().setId(72L).setFirstName("Karen").setLastName("Lee"));
		when(dao.get(Appointment.class, 72L)).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(72L), any(UnaryOperator.class)))
				.then((i) -> i.getArgumentAt(2, UnaryOperator.class).apply(appointment));

		AppointmentService service = new AppointmentService(
				new AppointmentStore(() -> dao, patientStore, nurseStore, vendorStore, clock), sessionStore);

		Appointment result = service.update(
				72L,
				new Appointment().setAssessments(
						ImmutableList.of(
								new Assessment(995L, "Good"),
								new Assessment(927L, "102"),
								new Assessment(845L, "Hello"))),
				"assessments",
				null);

		assertEquals("102", result.getAssessment(927L).getValue());
		assertEquals("George Man", result.getPatientName());
		assertEquals("Karen Lee", result.getNurseName());
		assertEquals("Good", result.getAssessment(995L).getValue());
		assertEquals("Hello", result.getAssessment(845L).getValue());
	}
}
