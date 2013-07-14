package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;
import com.digitald4.iis.tld.AssTabs;

public class AssessmentTest extends DD4TestCase {
	
	@Test
	public void testAssTabs() throws Exception {
		AssTabs at = new AssTabs();
		at.setTitle("Assessment");
		at.setAppointment(new Appointment().setPatient(new Patient()));
		System.out.println(at.getOutputIndented());
	}

	@Test
	public void testSetProperty() throws Exception {
		Appointment app = new Appointment();
		assertEquals(0, app.getAssessmentEntrys().size());
		app.setPropertyValue("55", "comment");
		assertEquals(1, app.getAssessmentEntrys().size());
		assertEquals("comment", app.getAssessmentEntrys().get(0).getValue());
		app.setPropertyValue("145", "location");
		assertEquals(2, app.getAssessmentEntrys().size());
		app.setPropertyValue("55", "new comment");
		assertEquals(2, app.getAssessmentEntrys().size());
		assertEquals("new comment", app.getAssessmentEntrys().get(0).getValue());
	}
	
	@Test
	public void testGetValue() throws Exception {
		Patient patient = new Patient();
		Appointment app = new Appointment().setStart(DateTime.now().minusDays(1));
		patient.addAppointment(app);
		app.setPropertyValue("55", "comment");
		assertEquals("comment", app.getAssessmentValue(55));
		Appointment app2 = new Appointment().setStart(DateTime.now());
		patient.addAppointment(app2);
		assertEquals("comment", app2.getAssessmentValue(55));
	}
}
