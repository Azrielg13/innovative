package com.digitald4.iis.report;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

public class PayrollReportTest {

	@Test
	public void testCreateWeekly() {
		DateTime testDate = DateTime.now();
		PayrollReport report = new PayrollReport(testDate);
		System.out.println(report.getEnd());
		System.out.println(report.getStart());
		assertEquals(DateTimeConstants.SATURDAY, report.getEnd().getDayOfWeek());
		assertEquals(DateTimeConstants.SUNDAY, report.getStart().getDayOfWeek());
		assertTrue(report.getStart().compareTo(report.getEnd()) < 0);
		assertTrue(report.getStart().compareTo(testDate) < 0);
		assertTrue(report.getEnd().compareTo(testDate) > 0);
		
		testDate = DateTime.parse("2014-09-28T11:40:00");
		report = new PayrollReport(testDate);
		System.out.println(report.getEnd());
		System.out.println(report.getStart());
		assertEquals(DateTimeConstants.SATURDAY, report.getEnd().getDayOfWeek());
		assertEquals(DateTimeConstants.SUNDAY, report.getStart().getDayOfWeek());
		assertTrue(report.getStart().compareTo(report.getEnd()) < 0);
		assertTrue(report.getStart().compareTo(testDate) < 0);
		assertTrue(report.getEnd().compareTo(testDate) > 0);
	}
}
