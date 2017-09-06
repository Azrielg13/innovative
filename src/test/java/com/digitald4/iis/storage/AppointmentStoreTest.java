package com.digitald4.iis.storage;

import static org.mockito.Mockito.mock;

import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.storage.DAOConnectorImpl;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.Filter;
import com.digitald4.common.storage.DataConnector;
import com.digitald4.common.util.Provider;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.test.TestCase;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentStoreTest extends TestCase {
	@Mock
	private DataConnector dataConnector = mock(DataConnector.class);
	private Provider<DataConnector> dataConnectorProvider = () -> dataConnector;

	@Test
	public void testGetBillable() throws Exception {
		AppointmentStore store = new AppointmentStore(
				new DAOConnectorImpl<>(Appointment.class, dataConnectorProvider),
				null, null);
		store.list(ListRequest.newBuilder()
				.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperan("=").setValue("7"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperan(">=").setValue("6"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperan("<=").setValue("7"))
				.build());
	}
}
