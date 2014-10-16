package com.digitald4.budget.model;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class BillTest {

	@Test
	public void test() throws Exception {
		Account sce = new Account().setName("Sce");
		Account chase = new Account().setName("Eddie's Checking");
		Bill bill = new Bill().setAmount(201.53).setDueDate(DateTime.parse("2014-10-24").toDate());
		sce.addBill(bill);
		assertEquals(201.53, bill.getAmount(), .0001);
		Transaction trans = new Transaction().setDebitAccount(chase).setAmount(150);
		bill.addTransaction(trans);
		assertEquals(150, bill.getPaid(), .0001);
		assertEquals(51.53, bill.getRemainingDue(), .0001);
		assertEquals(bill.getDueDate(), trans.getPaymentDate());
		assertTrue(bill.isActiveOnDay(DateTime.parse("2014-10-24").toDate()));
	}
}
