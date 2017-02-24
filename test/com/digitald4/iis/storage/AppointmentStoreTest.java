package com.digitald4.iis.storage;


import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.QueryParam;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.test.TestCase;
import org.junit.Test;

public class AppointmentStoreTest extends TestCase {

	@Test
	public void testGetBillable() throws Exception {
		AppointmentStore store = new AppointmentStore(
				new DAOProtoSQLImpl<>(Appointment.class, dbConnector));
		store.get(QueryParam.newBuilder().setColumn("billing_vendor_id").setOperan("=").setValue("7").build(),
				QueryParam.newBuilder().setColumn("state").setOperan(">=").setValue("6").build(),
				QueryParam.newBuilder().setColumn("state").setOperan("<=").setValue("7").build());
	}
}
