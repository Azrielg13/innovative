package com.digitald4.iis.model;

import static org.junit.Assert.*;

import java.sql.Time;
import java.text.ParseException;

import org.joda.time.DateTime;
import org.junit.*;

import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;

public class AppointmentTest extends DD4TestCase{

	@Test
	public void testDateTimeMethods() throws ParseException, Exception {
		Appointment app = new Appointment();
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
		app = new Appointment();
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
	public void testActiveOn() throws Exception {
		Appointment app = new Appointment().setStart(new DateTime(Calculate.getCal(2013, 02, 18, 8, 48, 48)))
				.setEnd(new DateTime(FormatText.USER_DATETIME.parse("02/18/2013 10:48:48")));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 18).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 19).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 17).getTime()));
		app.setEnd(new DateTime(FormatText.USER_DATETIME.parse("02/20/2013 08:48:48")));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 18).getTime()));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 19).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 17).getTime()));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 20).getTime()));
	}
	
	private static Appointment appointment;
	@Test
	public void testInsert() throws Exception {
		Patient patient = Patient.getInstance(3);
		appointment = new Appointment().setStart(new DateTime(Calculate.getCal(2013, 02, 18, 8, 48, 48)))
				.setEnd(new DateTime(FormatText.USER_DATETIME.parse("02/18/2013 10:48:48")));
		patient.addAppointment(appointment);
	}
	
	@Test
	public void testDelete() {
		if (appointment != null && !appointment.isNewInstance()) {
			appointment.delete();
		}
	}
	
	@Test
	public void testRead() {
		Patient patient = Patient.getInstance(3);
		assertTrue(patient.getAppointments().size() > 0);
		DateTime st = patient.getAppointments().iterator().next().getStart();
		assertEquals(2013, st.getYear());
		assertEquals(2, st.getMonthOfYear());
		assertEquals(18, st.getDayOfMonth());
		assertEquals(8, st.getHourOfDay());
	}
}
