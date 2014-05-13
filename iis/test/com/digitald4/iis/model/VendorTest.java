package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;

public class VendorTest extends DD4TestCase {

	@Test
	public void testSorting() {
		Vendor last = null;
		for (Vendor vendor : Vendor.getAll()) {
			if (last != null) {
				assertTrue(last.getName().compareTo(vendor.getName()) != 1);
			}
			System.out.println(vendor);
		}
	}
}
