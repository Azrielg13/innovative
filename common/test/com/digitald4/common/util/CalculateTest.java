package com.digitald4.common.util;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class CalculateTest {

	@Test
	public void testGetMonthRange() {
		Pair<DateTime, DateTime> range = Calculate.getMonthRange(2014, 10);
		DateTime start = range.getLeft();
		DateTime end = range.getRight();
		assertEquals(9, start.getMonthOfYear());
		assertEquals(28, start.getDayOfMonth());
		assertEquals(11, end.getMonthOfYear());
		assertEquals(1, end.getDayOfMonth());
		
		range = Calculate.getMonthRange(2014, 11);
		start = range.getLeft();
		end = range.getRight();
		assertEquals(10, start.getMonthOfYear());
		assertEquals(26, start.getDayOfMonth());
		assertEquals(12, end.getMonthOfYear());
		assertEquals(6, end.getDayOfMonth());
		
		range = Calculate.getMonthRange(2014, 6);
		start = range.getLeft();
		end = range.getRight();
		assertEquals(6, start.getMonthOfYear());
		assertEquals(1, start.getDayOfMonth());
		assertEquals(7, end.getMonthOfYear());
		assertEquals(5, end.getDayOfMonth());
		
		range = Calculate.getMonthRange(2014, 5);
		start = range.getLeft();
		end = range.getRight();
		assertEquals(4, start.getMonthOfYear());
		assertEquals(27, start.getDayOfMonth());
		assertEquals(5, end.getMonthOfYear());
		assertEquals(31, end.getDayOfMonth());
	}
}
