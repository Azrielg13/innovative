package com.digitald4.iis.server;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.model.Address;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.server.service.BulkGetable;
import com.digitald4.common.storage.*;
import com.digitald4.iis.model.*;
import com.digitald4.iis.storage.LicenseStore;
import com.digitald4.iis.storage.NurseStore;
import com.digitald4.iis.test.TestCase;
import com.google.common.collect.ImmutableList;
import java.util.function.UnaryOperator;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class NurseServiceTest extends TestCase {
	@Mock private DAO dao = mock(DAO.class);
	@Mock private SessionStore<User> sessionStore = mock(SessionStore.class);
	private Provider<DAO> daoProvider = () -> dao;

	private static final Nurse nurse1 = new Nurse()
			.setFirstName("Shalonda")
			.setLastName("Mayfield")
			.setAddress(new Address()
					.setAddress("123 Fake St, Fake CA 98765, USA")
					.setLatitude(10.2)
					.setLongitude(10.4));

	private static final Nurse nurse2 = new Nurse()
			.setFirstName("Nurse")
			.setLastName("Betty")
			.setAddress(new Address()
					.setAddress("123 Fake St, Fake CA 98765, USA")
					.setLatitude(10.2)
					.setLongitude(10.4));

	private NurseService service;

	@Before
	public void setup() {
		service = new NurseService(new NurseStore(daoProvider, new LicenseStore(daoProvider)), sessionStore);
		when(dao.list(eq(License.class), any())).thenReturn(QueryResult.of(License.class, ImmutableList.of(), 0, null));
		when(dao.get(eq(License.class), anyIterable())).thenReturn(
				BulkGetable.MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
		when(dao.get(eq(GeneralData.class), anyIterable())).thenReturn(
				BulkGetable.MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
		when(dao.get(eq(Nurse.class), anyIterable())).thenReturn(
				BulkGetable.MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
		when(dao.get(eq(Patient.class), anyIterable())).thenReturn(
				BulkGetable.MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
		when(dao.get(eq(Vendor.class), anyIterable())).thenReturn(
				BulkGetable.MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
		when(dao.get(eq(User.class), anyIterable())).thenReturn(
				BulkGetable.MultiListResult.of(ImmutableList.of(), ImmutableList.of()));
		when(dao.create(anyIterable())).thenReturn(ImmutableList.of());
	}

	@Test
	public void testList() throws Exception {
		when(dao.list(eq(Nurse.class), any(Query.List.class)))
				.thenReturn(QueryResult.of(Nurse.class, ImmutableList.of(nurse1, nurse2), 2, Query.forList()));

		ImmutableList<Nurse> nurses = service.list(null, null, 0, 0, null).getItems();
		assertTrue(nurses.size() > 0);
	}

	@Test
	public void testGet() throws Exception {
		when(dao.get(Nurse.class, 74L)).thenReturn(nurse1);

		Nurse nurse = service.get(74L, null);
		assertEquals("123 Fake St, Fake CA 98765, USA", nurse.getAddress().getAddress());
		assertEquals(10.2, nurse.getAddress().getLatitude(), .000001);
		assertEquals(10.4, nurse.getAddress().getLongitude(), .0000001);
	}

	@Test
	public void testUpdate() throws Exception {
		when(dao.update(eq(Nurse.class), eq(74L), any(UnaryOperator.class)))
				.then((i) -> i.getArgument(2, UnaryOperator.class).apply(nurse1));

		Nurse nurse = service.update(
				74L,
				new Nurse()
						.setAddress(new Address()
								.setAddress("212 W Mission Ct, Corona, CA 92882, USA")
								.setLatitude(33.860343)
								.setLongitude(-117.57081299999999)),
				"address",
				null);
		assertEquals("212 W Mission Ct, Corona, CA 92882, USA", nurse.getAddress().getAddress());
		assertEquals(33.860343, nurse.getAddress().getLatitude(), .000001);
		assertEquals(-117.57081299999999, nurse.getAddress().getLongitude(), .0000001);
	}
}
