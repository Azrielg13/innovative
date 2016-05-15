package com.digitald4.iis.model;

import static org.junit.Assert.*;

import java.sql.Time;
import java.text.ParseException;

import org.joda.time.DateTime;
import org.junit.*;

import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;

public class AppointmentTest extends DD4TestCase {

	@Test
	public void testDateTimeMethods() throws ParseException, Exception {
		Appointment app = new Appointment(null);
		assertNull(app.getStart());
		assertNull(app.getEnd());
		app.setStartDate(FormatText.USER_DATE.parse("02/18/2013"));
		assertEquals(2, app.getStart().getMonthOfYear());
		assertEquals(18, app.getStart().getDayOfMonth());
		assertEquals(2013, app.getStart().getYear());
		assertEquals(0, app.getStart().getMillisOfDay());
		app.setStartTime(new Time(FormatText.TIME.parse("08:48:48").getTime()));
		assertEquals(2, app.getStart().getMonthOfYear());
		assertEquals(18, app.getStart().getDayOfMonth());
		assertEquals(2013, app.getStart().getYear());
		assertEquals(8, app.getStart().getHourOfDay());
		assertEquals(48, app.getStart().getMinuteOfHour());
		assertEquals(48, app.getStart().getSecondOfMinute());
		
		app.setPropertyValue("END_TIME", "10:48:48");
		assertEquals(2, app.getEnd().getMonthOfYear());
		assertEquals(18, app.getEnd().getDayOfMonth());
		assertEquals(2013, app.getEnd().getYear());
		assertEquals(10, app.getEnd().getHourOfDay());
		assertEquals(48, app.getEnd().getMinuteOfHour());
		assertEquals(0, app.getEnd().getSecondOfMinute());
		
		// End time set before start time. End date +1.
		app.setPropertyValue("END_TIME", "07:12");
		assertEquals(2, app.getEnd().getMonthOfYear());
		assertEquals(19, app.getEnd().getDayOfMonth());
		assertEquals(2013, app.getEnd().getYear());
		assertEquals(7, app.getEnd().getHourOfDay());
		assertEquals(12, app.getEnd().getMinuteOfHour());
		assertEquals(0, app.getEnd().getSecondOfMinute());
		
		// End time adjusted to after start time. End date -1.
		app.setPropertyValue("END_TIME", "12:12");
		assertEquals(2, app.getEnd().getMonthOfYear());
		assertEquals(18, app.getEnd().getDayOfMonth());
		assertEquals(2013, app.getEnd().getYear());
		assertEquals(12, app.getEnd().getHourOfDay());
		assertEquals(12, app.getEnd().getMinuteOfHour());
		assertEquals(0, app.getEnd().getSecondOfMinute());
		
		// Start time jumps past end time. End date +1.
		app.setPropertyValue("START_TIME", "13:13");
		assertEquals(2, app.getEnd().getMonthOfYear());
		assertEquals(19, app.getEnd().getDayOfMonth());
		assertEquals(2013, app.getEnd().getYear());
		assertEquals(12, app.getEnd().getHourOfDay());
		assertEquals(12, app.getEnd().getMinuteOfHour());
		assertEquals(0, app.getEnd().getSecondOfMinute());
		
		// End time set while start still null.
		app = new Appointment(null);
		assertNull(app.getStart());
		assertNull(app.getEnd());
		app.setPropertyValue("END_TIME", "12:12");
		assertEquals(1, app.getEnd().getMonthOfYear());
		assertEquals(1, app.getEnd().getDayOfMonth());
		assertEquals(1970, app.getEnd().getYear());
		assertEquals(12, app.getEnd().getHourOfDay());
		assertEquals(12, app.getEnd().getMinuteOfHour());
		assertEquals(0, app.getEnd().getSecondOfMinute());
		
		// Start Date set. End should follow.
		app.setStartDate(FormatText.USER_DATE.parse("02/18/2013"));
		assertEquals(2, app.getEnd().getMonthOfYear());
		assertEquals(18, app.getEnd().getDayOfMonth());
		assertEquals(2013, app.getEnd().getYear());
		assertEquals(12, app.getEnd().getHourOfDay());
		assertEquals(12, app.getEnd().getMinuteOfHour());
		assertEquals(0, app.getEnd().getSecondOfMinute());
		
		app.setPropertyValue("TIME_IN", "10:52");
		assertEquals(10, app.getTimeIn().getHourOfDay());
		assertEquals(52, app.getTimeIn().getMinuteOfHour());
	}
	
	@Test
	public void testSetPropertyD() throws Exception {
		Appointment app = new Appointment(null);
		app.setPropertyValue("MILEAGE", "500");
		assertEquals(500, app.getMileageD(), .0001);
		app.setPropertyValue("MILEAGE_D", "501");
		assertEquals(501, app.getMileage(), .0001);
		app.setPropertyValue("MILEAGE", "50000");
		assertEquals(50000, app.getMileage(), .0001);
		app.setPropertyValue("MILEAGE_D", "50000");
		assertEquals(50000, app.getMileage(), .0001);
	}
	
	@Test
	public void testMileage() throws Exception {
		Appointment app = new Appointment(null)
				.setStart(DateTime.now())
				.setNurse(new Nurse(null).setMileageRate(.5))
				.setPatient(new Patient(null).setVendor(new Vendor(null).setMileageRate(.4)));
		
		// If we set no mileage there should be no mileage.
		assertEquals(0, app.getMileage(), .0001);
		assertEquals(0, app.getPayMileage(), .0001);
		assertEquals(0, app.getBillingMileage(), .0001);
		assertEquals(.5, app.getPayMileageRate(), .0001);
		assertEquals(.4, app.getBillingMileageRate(), .0001);
		
		// Setting the Driven Miles less than 20 will result in the pay/billable being zero
		app.setMileageD(10);
		assertEquals(10, app.getMileage(), .0001);
		assertEquals(0, app.getPayMileage(), .0001);
		assertEquals(0, app.getBillingMileage(), .0001);
		assertEquals(.5, app.getPayMileageRate(), .0001);
		assertEquals(.4, app.getBillingMileageRate(), .0001);
		
		// Setting the Driven Miles greater than 20 will result in the pay/billable being (driven - 20)
		app.setMileageD(25);
		assertEquals(25, app.getMileage(), .0001);
		assertEquals(5, app.getPayMileage(), .0001);
		assertEquals(5, app.getBillingMileage(), .0001);
		assertEquals(.5, app.getPayMileageRate(), .0001);
		assertEquals(.4, app.getBillingMileageRate(), .0001);
		
		// Setting the billable miles will override the payable miles if not set.
		app.setBillingMileageD(20);
		assertEquals(25, app.getMileage(), .0001);
		assertEquals(20, app.getBillingMileage(), .0001);
		assertEquals(20, app.getPayMileage(), .0001);
		assertEquals(.5, app.getPayMileageRate(), .0001);
		assertEquals(.4, app.getBillingMileageRate(), .0001);
		
		// Setting the payable miles will override the billable miles if not set.
		app.setBillingMileageD(-1);
		app.setPayMileageD(15);
		assertEquals(25, app.getMileage(), .0001);
		assertEquals(15, app.getBillingMileage(), .0001);
		assertEquals(15, app.getPayMileage(), .0001);
		assertEquals(.5, app.getPayMileageRate(), .0001);
		assertEquals(.4, app.getBillingMileageRate(), .0001);
		
		// You can zero out pay and billable if you like.
		app.setBillingMileageD(0);
		app.setPayMileageD(0);
		assertEquals(25, app.getMileage(), .0001);
		assertEquals(0, app.getBillingMileage(), .0001);
		assertEquals(0, app.getPayMileage(), .0001);
		assertEquals(.5, app.getPayMileageRate(), .0001);
		assertEquals(.4, app.getBillingMileageRate(), .0001);
	}
	
	@Test
	public void testMileageMultiAppointment() throws Exception {
		Nurse nurse = new Nurse(null).setMileageRate(.5);
		Appointment app1 = new Appointment(null)
				.setStart(DateTime.parse("2014-08-09T07:00:00"))
				.setNurse(nurse)
				.setPatient(new Patient(null).setVendor(new Vendor(null).setMileageRate(.4)));
		Appointment app2 = new Appointment(null)
				.setStart(DateTime.parse("2014-08-10T10:15:00"))
				.setNurse(nurse)
				.setPatient(new Patient(null).setVendor(new Vendor(null).setMileageRate(.4)));
		Appointment app3 = new Appointment(null)
				.setStart(DateTime.parse("2014-08-09T10:15:00"))
				.setNurse(nurse)
				.setPatient(new Patient(null).setVendor(new Vendor(null).setMileageRate(.4)));
		nurse.addAppointment(app1).addAppointment(app2).addAppointment(app3);
		
		assertNull(app1.getPrevSameNurseDayApp());
		assertNull(app2.getPrevSameNurseDayApp());
		assertEquals(app1, app3.getPrevSameNurseDayApp());
		
		// If we set no mileage there should be no mileage.
		assertEquals(0, app1.getMileage(), .0001);
		assertEquals(0, app1.getPayMileage(), .0001);
		assertEquals(20, app1.getSelfPaidMileage(), .0001);
		assertEquals(0, app2.getMileage(), .0001);
		assertEquals(0, app2.getPayMileage(), .0001);
		assertEquals(20, app2.getSelfPaidMileage(), .0001);
		assertEquals(0, app3.getMileage(), .0001);
		assertEquals(0, app3.getPayMileage(), .0001);
		assertEquals(20, app3.getSelfPaidMileage(), .0001);
		
		app1.setMileageD(10);
		app3.setMileageD(5);
		assertEquals(10, app1.getMileage(), .0001);
		assertEquals(0, app1.getPayMileage(), .0001);
		assertEquals(20, app1.getSelfPaidMileage(), .0001);
		// Setting mileage on app1 should have no effect on app2
		assertEquals(0, app2.getMileage(), .0001);
		assertEquals(0, app2.getPayMileage(), .0001);
		assertEquals(20, app2.getSelfPaidMileage(), .0001);
		// The self paid mileage of app3 should be lowered app1 mileage
		assertEquals(5, app3.getMileage(), .0001);
		assertEquals(0, app3.getPayMileage(), .0001);
		assertEquals(10, app3.getSelfPaidMileage(), .0001);
		
		app1.setMileageD(30);
		assertEquals(30, app1.getMileage(), .0001);
		assertEquals(10, app1.getPayMileage(), .0001);
		assertEquals(20, app1.getSelfPaidMileage(), .0001);
		// Setting mileage on app1 should have no effect on app2
		assertEquals(0, app2.getMileage(), .0001);
		assertEquals(0, app2.getPayMileage(), .0001);
		assertEquals(20, app2.getSelfPaidMileage(), .0001);
		// The self paid mileage of app3 should be lowered app1 mileage
		assertEquals(5, app3.getMileage(), .0001);
		assertEquals(5, app3.getPayMileage(), .0001);
		assertEquals(0, app3.getSelfPaidMileage(), .0001);
		
		app1.setMileageD(18);
		assertEquals(18, app1.getMileage(), .0001);
		assertEquals(0, app1.getPayMileage(), .0001);
		assertEquals(20, app1.getSelfPaidMileage(), .0001);
		// Setting mileage on app1 should have no effect on app2
		assertEquals(0, app2.getMileage(), .0001);
		assertEquals(0, app2.getPayMileage(), .0001);
		assertEquals(20, app2.getSelfPaidMileage(), .0001);
		// The self paid mileage of app3 should be lowered app1 mileage
		assertEquals(5, app3.getMileage(), .0001);
		assertEquals(3, app3.getPayMileage(), .0001);
		assertEquals(2, app3.getSelfPaidMileage(), .0001);
	}

	@Test
	public void testActiveOn() throws Exception {
		Appointment app = new Appointment(null).setStart(new DateTime(Calculate.getCal(2013, 02, 18, 8, 48, 48)))
				.setEnd(new DateTime(FormatText.USER_DATETIME.parse("02/18/2013 10:48:48")));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 18).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 19).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 17).getTime()));
		app.setEnd(new DateTime(FormatText.USER_DATETIME.parse("02/19/2013 08:48:48")));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 18).getTime()));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 19).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 17).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 20).getTime()));
	}
	
	@Test
	public void testBilling() throws Exception {
		Vendor vendor = new Vendor(null).setBillingFlat(200).setBillingRate(50)
				.setBillingFlat2HrSoc(220).setBillingRate2HrSoc(70)
				.setBillingFlat2HrRoc(210).setBillingRate2HrRoc(60);
		Patient patient = new Patient(null).setVendor(vendor);
		Nurse nurse = new Nurse(null);
		DateTime timeIn = DateTime.now().minusHours(2);
		DateTime timeOut = DateTime.now();
		Appointment appointment = new Appointment(null).setPatient(patient).setNurse(nurse)
				.setStart(DateTime.now()).setTimeInD(timeIn).setTimeOutD(timeOut);
		assertEquals(GenData.ACCOUNTING_TYPE_SOC_2HR.get(entityManager), appointment.getBillingType());
		assertEquals(2.0, appointment.getLoggedHours(), 0.0);
		assertEquals(220.0, appointment.getBillingFlat(), 0.0);
		assertEquals(0.0, appointment.getBilledHours(), 0.0);
		assertEquals(70.0, appointment.getBillingRate(), 0.0);
		assertEquals(220.0, appointment.getBillingTotal(), 0.0);
		
		appointment.setTimeInD(timeIn.minusHours(1));
		assertEquals(GenData.ACCOUNTING_TYPE_HOURLY.get(entityManager), appointment.getBillingType());
		assertEquals(3.0, appointment.getLoggedHours(), 0.0);
		assertEquals(0.0, appointment.getBillingFlat(), 0.0);
		assertEquals(50.0, appointment.getBillingRate(), 0.0);
		assertEquals(150.0, appointment.getBillingTotal(), 0.0);

		appointment.setBillingType(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager), appointment.getBillingType());
		assertEquals(3.0, appointment.getLoggedHours(), 0.0);
		assertEquals(200.0, appointment.getBillingFlat(), 0.0);
		assertEquals(0.0, appointment.getBilledHours(), 0.0);
		assertEquals(0.0, appointment.getBillingRate(), 0.0);
		assertEquals(200.0, appointment.getBillingTotal(), 0.0);

		appointment.setTimeInD(timeIn.plusMinutes(105));
		appointment.setBillingType(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager), appointment.getBillingType());
		assertEquals(.25, appointment.getLoggedHours(), 0.0);
		assertEquals(200.0, appointment.getBillingFlat(), 0.0);
		assertEquals(0.0, appointment.getBilledHours(), 0.0);
		assertEquals(0.0, appointment.getBillingRate(), 0.0);
		assertEquals(200.0, appointment.getBillingTotal(), 0.0);

		appointment.setTimeInD(timeIn.minusHours(1));
		appointment.setBillingType(GenData.ACCOUNTING_TYPE_SOC_2HR.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_SOC_2HR.get(entityManager), appointment.getBillingType());
		assertEquals(3.0, appointment.getLoggedHours(), 0.0);
		assertEquals(220.0, appointment.getBillingFlat(), 0.0);
		assertEquals(1.0, appointment.getBilledHours(), 0.0);
		assertEquals(70.0, appointment.getBillingRate(), 0.0);
		assertEquals(290.0, appointment.getBillingTotal(), 0.0);

		appointment.setTimeInD(timeIn.minusMinutes(30));
		appointment.setBillingType(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager), appointment.getBillingType());
		assertEquals(2.5, appointment.getLoggedHours(), 0.0);
		assertEquals(210.0, appointment.getBillingFlat(), 0.0);
		assertEquals(.5, appointment.getBilledHours(), 0.0);
		assertEquals(60.0, appointment.getBillingRate(), 0.0);
		assertEquals(240.0, appointment.getBillingTotal(), 0.0);
		
		appointment.setTimeInD(timeIn.plusMinutes(30));
		appointment.setBillingType(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager), appointment.getBillingType());
		assertEquals(1.5, appointment.getLoggedHours(), 0.0);
		assertEquals(210.0, appointment.getBillingFlat(), 0.0);
		assertEquals(0.0, appointment.getBilledHours(), 0.0);
		assertEquals(60.0, appointment.getBillingRate(), 0.0);
		assertEquals(210.0, appointment.getBillingTotal(), 0.0);
		
		appointment.setPropertyValue("TIME_IN", "20:10");
		appointment.setPropertyValue("TIME_OUT", "03:10");
		appointment.setBillingType(GenData.ACCOUNTING_TYPE_HOURLY.get(entityManager));
		assertEquals(7, appointment.getLoggedHours(), 0.0);
		assertEquals(0.0, appointment.getBillingFlat(), 0.0);
		assertEquals(7, appointment.getBilledHours(), 0.0);
		assertEquals(50.0, appointment.getBillingRate(), 0.0);
		assertEquals(350.0, appointment.getBillingTotal(), 0.0);
	}
	
	@Test
	public void testPayroll() throws Exception {
		Vendor vendor = new Vendor(entityManager);
		Patient patient = new Patient(entityManager).setVendor(vendor);
		Nurse nurse = new Nurse(entityManager).setPayFlat(100).setPayRate(40)
				.setPayFlat2HrSoc(95).setPayRate2HrSoc(60)
				.setPayFlat2HrRoc(90).setPayRate2HrRoc(50);
		DateTime timeIn = DateTime.now().minusHours(2);
		DateTime timeOut = DateTime.now();
		Appointment appointment = new Appointment(entityManager).setPatient(patient).setNurse(nurse)
				.setStart(DateTime.now()).setTimeInD(timeIn).setTimeOutD(timeOut);
		assertEquals(2.0, appointment.getLoggedHours(), 0.0);
		assertEquals(GenData.ACCOUNTING_TYPE_SOC_2HR.get(entityManager), appointment.getPayingType());
		assertEquals(95.0, appointment.getPayFlat(), 0.0);
		assertEquals(0.0, appointment.getPayHours(), 0.0);
		assertEquals(60.0, appointment.getPayRate(), 0.0);
		assertEquals(95.0, appointment.getPaymentTotal(), 0.0);
		
		appointment.setTimeInD(timeIn.minusHours(1));
		assertEquals(3.0, appointment.getLoggedHours(), 0.0);
		assertEquals(GenData.ACCOUNTING_TYPE_HOURLY.get(entityManager), appointment.getPayingType());
		assertEquals(0.0, appointment.getPayFlat(), 0.0);
		assertEquals(40.0, appointment.getPayRate(), 0.0);
		assertEquals(3.0, appointment.getPayHours(), 0.0);
		assertEquals(120.0, appointment.getPaymentTotal(), 0.0);

		appointment.setPayingType(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager), appointment.getPayingType());
		assertEquals(3.0, appointment.getLoggedHours(), 0.0);
		assertEquals(100.0, appointment.getPayFlat(), 0.0);
		assertEquals(0.0, appointment.getPayHours(), 0.0);
		assertEquals(0.0, appointment.getPayRate(), 0.0);
		assertEquals(100.0, appointment.getPaymentTotal(), 0.0);

		appointment.setTimeInD(timeIn.plusMinutes(105));
		appointment.setPayingType(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_FIXED.get(entityManager), appointment.getPayingType());
		assertEquals(.25, appointment.getLoggedHours(), 0.0);
		assertEquals(100.0, appointment.getPayFlat(), 0.0);
		assertEquals(0.0, appointment.getPayHours(), 0.0);
		assertEquals(0.0, appointment.getPayRate(), 0.0);
		assertEquals(100.0, appointment.getPaymentTotal(), 0.0);

		appointment.setTimeInD(timeIn.minusHours(1));
		appointment.setPayingType(GenData.ACCOUNTING_TYPE_SOC_2HR.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_SOC_2HR.get(entityManager), appointment.getPayingType());
		assertEquals(3.0, appointment.getLoggedHours(), 0.0);
		assertEquals(95.0, appointment.getPayFlat(), 0.0);
		assertEquals(1.0, appointment.getPayHours(), 0.0);
		assertEquals(60.0, appointment.getPayRate(), 0.0);
		assertEquals(155.0, appointment.getPaymentTotal(), 0.0);

		appointment.setTimeInD(timeIn.minusMinutes(30));
		appointment.setPayingType(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager), appointment.getPayingType());
		assertEquals(2.5, appointment.getLoggedHours(), 0.0);
		assertEquals(90.0, appointment.getPayFlat(), 0.0);
		assertEquals(.5, appointment.getPayHours(), 0.0);
		assertEquals(50.0, appointment.getPayRate(), 0.0);
		assertEquals(115.0, appointment.getPaymentTotal(), 0.0);
		
		appointment.setTimeInD(timeIn.plusMinutes(30));
		appointment.setPayingType(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager));
		assertEquals(GenData.ACCOUNTING_TYPE_ROC_2HR.get(entityManager), appointment.getPayingType());
		assertEquals(1.5, appointment.getLoggedHours(), 0.0);
		assertEquals(90.0, appointment.getPayFlat(), 0.0);
		assertEquals(0.0, appointment.getPayHours(), 0.0);
		assertEquals(50.0, appointment.getPayRate(), 0.0);
		assertEquals(90.0, appointment.getPaymentTotal(), 0.0);
	}
	
	@Test
	public void testAutoFill() throws Exception {
		// Does not throw error if patient is null
		new Appointment(entityManager).autoFill();
		
		Patient patient = new Patient(entityManager);
		Appointment app = new Appointment(entityManager).setPatient(patient).autoFill();
		assertNull(app.getNurseId());
		assertNull(app.getStart());
		assertNull(app.getEnd());
		
		Appointment last = new Appointment(entityManager).setNurseId(42)
				.setStart(DateTime.parse("2014-12-23T07:43:00")).setEnd(DateTime.parse("2014-12-23T08:30:00"));
		patient.addAppointment(last);
		app.autoFill();
		assertEquals(42, app.getNurseId().intValue());
		assertEquals("" + last.getStartTime(), "" + app.getStartTime());
		assertEquals("" + last.getEndTime(), "" + app.getEndTime());
		
		app = new Appointment(entityManager).setPatient(patient).setStart(DateTime.parse("2014-12-29"));
		app.autoFill();
		assertEquals(42, app.getNurseId().intValue());
		assertEquals(12, app.getStart().getMonthOfYear());
		assertEquals(29, app.getStart().getDayOfMonth());
		assertEquals(2014, app.getStart().getYear());
		assertEquals("" + last.getStartTime(), "" + app.getStartTime());
		assertEquals("" +last.getEndTime(), "" + app.getEndTime());
	}
	
	private static Appointment appointment;
	@Test @Ignore
	public void testInsert() throws Exception {
		Patient patient = entityManager.find(Patient.class, 3);
		appointment = new Appointment(entityManager).setStart(new DateTime(Calculate.getCal(2013, 02, 18, 8, 48, 48)))
				.setEnd(new DateTime(FormatText.USER_DATETIME.parse("02/18/2013 10:48:48")));
		patient.addAppointment(appointment);
	}
	
	@Test @Ignore
	public void testDelete() throws Exception {
		if (appointment != null && !appointment.isNewInstance()) {
			appointment.delete();
		}
	}
	
	@Test @Ignore
	public void testRead() {
		Patient patient = entityManager.find(Patient.class, 3);
		assertTrue(patient.getAppointments().size() > 0);
		DateTime st = patient.getAppointments().iterator().next().getStart();
		assertEquals(2013, st.getYear());
		assertEquals(2, st.getMonthOfYear());
		assertEquals(18, st.getDayOfMonth());
		assertEquals(8, st.getHourOfDay());
	}
}
