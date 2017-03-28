package com.digitald4.iis.storage;

import static org.junit.Assert.*;

import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.iis.proto.IISProtos.Paystub;
import com.digitald4.iis.test.TestCase;
import org.junit.Test;

public class PaystubTest extends TestCase {

	@Test
	public void testGetBillable() throws Exception {
		PaystubStore store = new PaystubStore(
				new DAOProtoSQLImpl<>(Paystub.class, dbConnector),
				null, null, null);
		Paystub paystub = store.get(33);
		assertTrue(paystub.getAppointmentIdCount() > 0);
	}
}
