package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;
import com.digitald4.iis.tld.AssTabs;

public class AssessmentTest extends DD4TestCase {
	
	@Test
	public void testAssTabs() throws Exception {
		AssTabs at = new AssTabs();
		at.setTitle("Assessment");
		at.setAppointment(new Appointment());
		System.out.println(at.getOutputIndented());
	}

	@Test
	public void testSetProperty() throws Exception {
		Appointment app = new Appointment();
		assertEquals(0, app.getAssessmentEntrys().size());
		app.setPropertyValue("55", "comment");
		assertEquals(1, app.getAssessmentEntrys().size());
		assertEquals("comment", app.getAssessmentEntrys().iterator().next().getValueStr());
		app.setPropertyValue("145", "location");
		assertEquals(2, app.getAssessmentEntrys().size());
		app.setPropertyValue("55", "new comment");
		assertEquals(2, app.getAssessmentEntrys().size());
		assertEquals("new comment", app.getAssessmentEntrys().iterator().next().getValueStr());
	}
}
