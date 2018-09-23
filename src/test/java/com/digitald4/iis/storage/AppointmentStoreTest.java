package com.digitald4.iis.storage;

import static org.mockito.Mockito.mock;

import com.digitald4.common.proto.DD4Protos.Query;
import com.digitald4.common.proto.DD4Protos.Query.Filter;
import com.digitald4.common.storage.DAO;
import com.digitald4.iis.test.TestCase;
import javax.inject.Provider;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentStoreTest extends TestCase {
	@Mock
	private DAO dao = mock(DAO.class);
	private Provider<DAO> daoProvider = () -> dao;

	@Test
	public void testGetBillable() {
		AppointmentStore store = new AppointmentStore(daoProvider, null, null);
		store.list(Query.newBuilder()
				.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperator("=").setValue("7"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperator(">=").setValue("6"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperator("<=").setValue("7"))
				.build());
	}
}
