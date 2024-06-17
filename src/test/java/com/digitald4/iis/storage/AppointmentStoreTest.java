package com.digitald4.iis.storage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.test.TestCase;
import com.google.common.collect.ImmutableList;
import javax.inject.Provider;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentStoreTest extends TestCase {
	@Mock
	private final DAO dao = mock(DAO.class);
	private final Provider<DAO> daoProvider = () -> dao;

	@Test
	public void testGetBillable() {
		when(dao.list(eq(Appointment.class), any())).thenReturn(QueryResult.of(ImmutableList.of(), 0, null));
		AppointmentStore store = new AppointmentStore(daoProvider, null, null);
		store.list(
				Query.forList().setFilters(
						Filter.of("vendor_id", "=", 7),
						Filter.of("state", ">=", 6),
						Filter.of("state", "<=", 7)));
	}
}
