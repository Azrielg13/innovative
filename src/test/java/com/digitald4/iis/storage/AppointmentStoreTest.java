package com.digitald4.iis.storage;

import static org.mockito.Mockito.mock;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.iis.test.TestCase;
import javax.inject.Provider;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentStoreTest extends TestCase {
	@Mock
	private final DAO dao = mock(DAO.class);
	private final Provider<DAO> daoProvider = () -> dao;

	@Test
	public void testGetBillable() {
		AppointmentStore store = new AppointmentStore(daoProvider, null, null, null, null);
		store.list(
				new Query()
						.setFilters(
								new Filter().setColumn("vendor_id").setOperator("=").setValue("7"),
								new Filter().setColumn("state").setOperator(">=").setValue("6"),
								new Filter().setColumn("state").setOperator("<=").setValue("7")));
	}
}
