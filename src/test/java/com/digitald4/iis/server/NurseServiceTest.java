package com.digitald4.iis.server;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4UIProtos.GetRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.UpdateRequest;
import com.digitald4.common.storage.DAOConnectorImpl;
import com.digitald4.common.storage.DataConnector;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.storage.ListResponse;
import com.digitald4.common.util.Provider;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.digitald4.iis.proto.IISUIProtos.GPSAddress;
import com.digitald4.iis.test.TestCase;
import com.google.protobuf.Any;
import com.google.protobuf.FieldMask;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.junit.Test;
import org.mockito.Mock;

public class NurseServiceTest extends TestCase {
	@Mock
	private DataConnector dataConnector = mock(DataConnector.class);
	private Provider<DataConnector> dataConnectorProvider = () -> dataConnector;

	private static final Nurse nurse1 = Nurse.newBuilder()
			.setFirstName("Shalonda")
			.setLastName("Mayfield")
			.setFullName("Shalonda Mayfield")
			.setAddress(GPSAddress.newBuilder()
					.setAddress("123 Fake St, Fake CA 98765, USA")
					.setLatitude(10.2)
					.setLongitude(10.4)
					.build())
			.build();

	private static final Nurse nurse2 = Nurse.newBuilder()
			.setFirstName("Nurse")
			.setLastName("Betty")
			.setFullName("Nurse Betty")
			.setAddress(GPSAddress.newBuilder()
					.setAddress("123 Fake St, Fake CA 98765, USA")
					.setLatitude(10.2)
					.setLongitude(10.4)
					.build())
			.build();

	@Test
	public void testList() throws DD4StorageException {
		when(dataConnector.list(eq(Nurse.class), any(ListRequest.class)))
				.thenReturn(ListResponse.<Nurse>newBuilder()
						.addResult(nurse1)
						.addResult(nurse2)
						.setTotalSize(2)
						.build());
		NurseService service = new NurseService(
				new GenericStore<>(new DAOConnectorImpl<>(Nurse.class, dataConnectorProvider)));
		
		List<NurseUI> nurses = service.list(ListRequest.getDefaultInstance())
				.getResultList()
				.stream()
				.map(any -> any.unpack(NurseUI.class))
				.collect(Collectors.toList());
		assertTrue(nurses.size() > 0);
	}

	@Test
	public void testGet() throws DD4StorageException {
		when(dataConnector.get(Nurse.class, 74L)).thenReturn(nurse1);
		NurseService service = new NurseService(
				new GenericStore<>(new DAOConnectorImpl<>(Nurse.class, dataConnectorProvider)));

		NurseUI nurse = service.get(GetRequest.newBuilder()
				.setId(74L)
				.build());
		assertEquals("123 Fake St, Fake CA 98765, USA", nurse.getAddress().getAddress());
		assertEquals(10.2, nurse.getAddress().getLatitude(), .000001);
		assertEquals(10.4, nurse.getAddress().getLongitude(), .0000001);
	}

	@Test
	public void testUpdate() throws DD4StorageException {
		when(dataConnector.update(eq(Nurse.class), eq(74L), any(UnaryOperator.class)))
				.then((i) -> i.getArgumentAt(2, UnaryOperator.class).apply(nurse1));
		NurseService service = new NurseService(
				new GenericStore<>(new DAOConnectorImpl<>(Nurse.class, dataConnectorProvider)));

		NurseUI nurse = service.update(UpdateRequest.newBuilder()
				.setId(74L)
				.setProto(Any.pack(NurseUI.newBuilder()
						.setAddress(GPSAddress.newBuilder()
								.setAddress("212 W Mission Ct, Corona, CA 92882, USA")
								.setLatitude(33.860343)
								.setLongitude(-117.57081299999999))
						.build()))
				.setUpdateMask(FieldMask.newBuilder().addPaths("address"))
				.build());
		assertEquals("212 W Mission Ct, Corona, CA 92882, USA", nurse.getAddress().getAddress());
		assertEquals(33.860343, nurse.getAddress().getLatitude(), .000001);
		assertEquals(-117.57081299999999, nurse.getAddress().getLongitude(), .0000001);
	}
}
