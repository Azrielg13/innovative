package com.digitald4.iis.storage;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.model.ServiceCode.Type;
import com.digitald4.iis.model.ServiceCode.Unit;
import com.digitald4.iis.test.TestCase;
import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Instant;
import java.util.function.UnaryOperator;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentStoreTest extends TestCase {
	private static final ServiceCode NURSE_PAY_50 =
			new ServiceCode().setCode("Nurse Pay 50").setType(Type.Pay).setUnit(Unit.Hour).setUnitPrice(50.0);
	private static final ServiceCode NURSE_VISIT_99 =
			new ServiceCode().setCode("Nurse Visit 99").setType(Type.Pay).setUnit(Unit.Visit).setUnitPrice(99.0);
	private static final ServiceCode BILL_75 =
			new ServiceCode().setCode("Bill 75").setType(Type.Bill).setUnit(Unit.Hour).setUnitPrice(75.0);
	private static final ServiceCode BILL_149 =
			new ServiceCode().setCode("Bill 149").setType(Type.Bill).setUnit(Unit.Visit).setUnitPrice(149.0);
	private static final Long VENDOR_ID = 60L;
	private static final Patient PATIENT = new Patient().setId(40L)
			.setFirstName("Patient").setLastName("Zero").setBillingVendorId(VENDOR_ID).setBillingVendorName("Vendor");

	@Mock
	private final DAO dao = mock(DAO.class);
	private final Provider<DAO> daoProvider = () -> dao;
	private final ServiceCodeStore serviceCodeStore = mock(ServiceCodeStore.class);
	private final Clock clock = mock(Clock.class);
	private AppointmentStore appointmentStore;

	@Before
	public void setUp() {
		appointmentStore = new AppointmentStore(daoProvider, serviceCodeStore, clock);
		when(dao.get(eq(Patient.class), eq(PATIENT.getId()))).thenReturn(PATIENT);
		when(serviceCodeStore.get(eq("Nurse Pay 50"))).thenReturn(NURSE_PAY_50);
		when(serviceCodeStore.get(eq("Nurse Visit 99"))).thenReturn(NURSE_VISIT_99);
		when(serviceCodeStore.get(eq("Bill 75"))).thenReturn(BILL_75);
		when(serviceCodeStore.get(eq("Bill 149"))).thenReturn(BILL_149);
	}

	@Test
	public void testGetBillable() {
		when(dao.list(eq(Appointment.class), any())).thenReturn(QueryResult.of(Appointment.class, ImmutableList.of(), 0, null));
		AppointmentStore store = new AppointmentStore(daoProvider, null, null);
		store.list(
				Query.forList().setFilters(
						Filter.of("vendor_id", "=", 7),
						Filter.of("state", ">=", 6),
						Filter.of("state", "<=", 7)));
	}

	@Test
	public void setsBillAndPayCodes_hourlyPay() {
		when(serviceCodeStore.getForVendor(eq(VENDOR_ID))).thenReturn(ImmutableList.of(BILL_75, BILL_149));
		when(serviceCodeStore.getForNurse(any(), eq(Unit.Hour))).thenReturn(ImmutableList.of(NURSE_PAY_50));
		Appointment appointment = new Appointment()
				.setId(1L).setPatientId(PATIENT.getId()).setNurseId(50L).setVendorId(VENDOR_ID).setAssessmentComplete(true);
		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(appointment.copy()));

		var updated = appointmentStore.update(1L, app -> app.setTimeIn(8 * 60 * 60 * 1000));
		assertThat(updated.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(updated.getTimeOut()).isNull();
		assertThat(updated.getLoggedHours()).isEqualTo(0);
		assertThat(updated.getBillingInfo()).isNull();
		assertThat(updated.getPaymentInfo()).isNull();

		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(updated);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(updated.copy()));

		var update2 = appointmentStore.update(1L, app -> app.setTimeOut(11 * 60 * 60 * 1000));
		assertThat(update2.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(update2.getTimeOut()).isEqualTo(Instant.parse("1970-01-01T11:00:00Z"));
		assertThat(update2.getLoggedHours()).isEqualTo(3);
		assertThat(update2.getPaymentInfo().getServiceCode()).isEqualTo("Nurse Pay 50");
		assertThat(update2.getPaymentInfo().getUnit()).isEqualTo(Unit.Hour);
		assertThat(update2.getPaymentInfo().getUnitCount()).isEqualTo(3);
		assertThat(update2.getPaymentInfo().getUnitRate()).isEqualTo(50);
		assertThat(update2.getPaymentInfo().subTotal()).isEqualTo(150);
		assertThat(update2.getBillingInfo().getServiceCode()).isEqualTo("Bill 75");
		assertThat(update2.getBillingInfo().getUnit()).isEqualTo(Unit.Hour);
		assertThat(update2.getBillingInfo().getUnitCount()).isEqualTo(3);
		assertThat(update2.getBillingInfo().getUnitRate()).isEqualTo(75);
		assertThat(update2.getBillingInfo().subTotal()).isEqualTo(225);
	}

	@Test
	public void setsBillAndPayCodes_visitPay() {
		when(serviceCodeStore.getForVendor(eq(VENDOR_ID))).thenReturn(ImmutableList.of(BILL_75, BILL_149));
		when(serviceCodeStore.getForNurse(any(), eq(Unit.Visit))).thenReturn(ImmutableList.of(NURSE_VISIT_99));
		Appointment appointment = new Appointment()
				.setId(1L).setPatientId(PATIENT.getId()).setNurseId(50L).setVendorId(VENDOR_ID).setAssessmentComplete(true);
		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(appointment.copy()));

		var updated = appointmentStore.update(1L, app -> app.setTimeIn(8 * 60 * 60 * 1000));
		assertThat(updated.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(updated.getTimeOut()).isNull();
		assertThat(updated.getLoggedHours()).isEqualTo(0);
		assertThat(updated.getBillingInfo()).isNull();
		assertThat(updated.getPaymentInfo()).isNull();

		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(updated);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(updated.copy()));

		var update2 = appointmentStore.update(1L, app -> app.setTimeOut(Instant.parse("1970-01-01T09:30:00Z")));
		assertThat(update2.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(update2.getTimeOut()).isEqualTo(Instant.parse("1970-01-01T09:30:00Z"));
		assertThat(update2.getLoggedHours()).isEqualTo(1.5);
		assertThat(update2.getPaymentInfo().getServiceCode()).isEqualTo("Nurse Visit 99");
		assertThat(update2.getPaymentInfo().getUnit()).isEqualTo(Unit.Visit);
		assertThat(update2.getPaymentInfo().getUnitCount()).isEqualTo(1);
		assertThat(update2.getPaymentInfo().getUnitRate()).isEqualTo(99);
		assertThat(update2.getPaymentInfo().subTotal()).isEqualTo(99);
		assertThat(update2.getBillingInfo().getServiceCode()).isEqualTo("Bill 149");
		assertThat(update2.getBillingInfo().getUnit()).isEqualTo(Unit.Visit);
		assertThat(update2.getBillingInfo().getUnitCount()).isEqualTo(1);
		assertThat(update2.getBillingInfo().getUnitRate()).isEqualTo(149);
		assertThat(update2.getBillingInfo().subTotal()).isEqualTo(149);
	}

	@Test
	public void setsBillAndPayCodes_hourlyFallBack() {
		when(serviceCodeStore.getForVendor(eq(VENDOR_ID))).thenReturn(ImmutableList.of(BILL_75));
		when(serviceCodeStore.getForNurse(any(), eq(Unit.Hour))).thenReturn(ImmutableList.of(NURSE_PAY_50));
		when(serviceCodeStore.getForNurse(any(), eq(Unit.Visit))).thenReturn(ImmutableList.of());
		Appointment appointment = new Appointment().setId(1L).setPatientId(PATIENT.getId())
				.setNurseId(50L).setVendorId(VENDOR_ID).setAssessmentComplete(true);
		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(appointment.copy()));

		var updated = appointmentStore.update(1L, app -> app.setTimeIn(8 * 60 * 60 * 1000));
		assertThat(updated.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(updated.getTimeOut()).isNull();
		assertThat(updated.getLoggedHours()).isEqualTo(0);
		assertThat(updated.getBillingInfo()).isNull();
		assertThat(updated.getPaymentInfo()).isNull();

		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(updated);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(updated.copy()));

		var update2 = appointmentStore.update(1L, app -> app.setTimeOut(Instant.parse("1970-01-01T09:30:00Z")));
		assertThat(update2.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(update2.getTimeOut()).isEqualTo(Instant.parse("1970-01-01T09:30:00Z"));
		assertThat(update2.getLoggedHours()).isEqualTo(1.5);
		assertThat(update2.getPaymentInfo().getServiceCode()).isEqualTo("Nurse Pay 50");
		assertThat(update2.getPaymentInfo().getUnit()).isEqualTo(Unit.Hour);
		assertThat(update2.getPaymentInfo().getUnitCount()).isEqualTo(2);
		assertThat(update2.getPaymentInfo().getUnitRate()).isEqualTo(50);
		assertThat(update2.getPaymentInfo().subTotal()).isEqualTo(100);
		assertThat(update2.getBillingInfo().getServiceCode()).isEqualTo("Bill 75");
		assertThat(update2.getBillingInfo().getUnit()).isEqualTo(Unit.Hour);
		assertThat(update2.getBillingInfo().getUnitCount()).isEqualTo(2);
		assertThat(update2.getBillingInfo().getUnitRate()).isEqualTo(75);
		assertThat(update2.getBillingInfo().subTotal()).isEqualTo(150);
	}

	@Test
	public void setsBillAndPayCodes_visitFallBack() {
		when(serviceCodeStore.getForVendor(eq(VENDOR_ID))).thenReturn(ImmutableList.of(BILL_149));
		when(serviceCodeStore.getForNurse(any(), eq(Unit.Hour))).thenReturn(ImmutableList.of());
		when(serviceCodeStore.getForNurse(any(), eq(Unit.Visit))).thenReturn(ImmutableList.of(NURSE_VISIT_99));
		Appointment appointment = new Appointment()
				.setId(1L).setPatientId(PATIENT.getId()).setNurseId(50L).setVendorId(VENDOR_ID).setAssessmentComplete(true);
		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(appointment.copy()));

		var updated = appointmentStore.update(1L, app -> app.setTimeIn(8 * 60 * 60 * 1000));
		assertThat(updated.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(updated.getTimeOut()).isNull();
		assertThat(updated.getLoggedHours()).isEqualTo(0);
		assertThat(updated.getBillingInfo()).isNull();
		assertThat(updated.getPaymentInfo()).isNull();

		when(dao.get(eq(Appointment.class), eq(1L))).thenReturn(updated);
		when(dao.update(eq(Appointment.class), eq(1L), any()))
				.thenAnswer(i -> ((UnaryOperator<Appointment>) i.getArgument(2)).apply(updated.copy()));

		var update2 = appointmentStore.update(1L, app -> app.setTimeOut(11 * 60 * 60 * 1000));
		assertThat(update2.getTimeIn()).isEqualTo(Instant.parse("1970-01-01T08:00:00Z"));
		assertThat(update2.getTimeOut()).isEqualTo(Instant.parse("1970-01-01T11:00:00Z"));
		assertThat(update2.getLoggedHours()).isEqualTo(3);
		assertThat(update2.getPaymentInfo().getServiceCode()).isEqualTo("Nurse Visit 99");
		assertThat(update2.getPaymentInfo().getUnit()).isEqualTo(Unit.Visit);
		assertThat(update2.getPaymentInfo().getUnitCount()).isEqualTo(1.5);
		assertThat(update2.getPaymentInfo().getUnitRate()).isEqualTo(99);
		assertThat(update2.getPaymentInfo().subTotal()).isEqualTo(148.5);
		assertThat(update2.getBillingInfo().getServiceCode()).isEqualTo("Bill 149");
		assertThat(update2.getBillingInfo().getUnit()).isEqualTo(Unit.Visit);
		assertThat(update2.getBillingInfo().getUnitCount()).isEqualTo(1.5);
		assertThat(update2.getBillingInfo().getUnitRate()).isEqualTo(149);
		assertThat(update2.getBillingInfo().subTotal()).isEqualTo(223.5);
	}
}
