package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.junit.*;

import com.digitald4.common.test.DD4TestCase;

public class InvoiceTest extends DD4TestCase {
	
	@Test
	public void testReadAll() throws Exception {
		for (Invoice invoice : Invoice.getAll()) {
			assertNotNull(invoice.getData());
			System.out.println(invoice.getId() + " " + invoice.getName() + " " + invoice.getVendor() + " " + invoice.isPaid() + " " + invoice.getData().length + "byres");
		}
	}
}
