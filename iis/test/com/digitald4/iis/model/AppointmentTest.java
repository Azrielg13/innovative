package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.util.Calculate;

public class AppointmentTest extends DD4TestCase{

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testActiveOn() throws Exception {
		Appointment app = new Appointment().setStartTime(new DateTime(Calculate.getCal(2013, 02, 18, 8, 48, 48))).setDuration(120);
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 18).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 19).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 17).getTime()));
		app.setDuration(60*24*2);
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 18).getTime()));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 19).getTime()));
		assertFalse(app.isActiveOnDay(Calculate.getCal(2013, 02, 17).getTime()));
		assertTrue(app.isActiveOnDay(Calculate.getCal(2013, 02, 20).getTime()));
	}
	
	@Test
	@Ignore
	public void testInsert() throws Exception {
		Patient patient = Patient.getInstance(3);
		patient.addAppointment(new Appointment().setStartTime(new DateTime(Calculate.getCal(2013, 02, 18, 8, 48, 48))).setDuration(120));
	}
	
	@Test
	public void testRead() {
		Patient patient = Patient.getInstance(3);
		assertTrue(patient.getAppointments().size() > 0);
		DateTime st = patient.getAppointments().iterator().next().getStartTime();
		assertEquals(2013, st.getYear());
		assertEquals(2, st.getMonthOfYear());
		assertEquals(18, st.getDayOfMonth());
		assertEquals(8, st.getHourOfDay());
		assertEquals(48, st.getMinuteOfHour());
		assertEquals(48, st.getSecondOfMinute());
	}
}
