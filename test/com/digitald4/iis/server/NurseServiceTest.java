package com.digitald4.iis.server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4UIProtos.GetRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.UpdateRequest;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.digitald4.iis.server.NurseService;
import com.digitald4.iis.test.TestCase;

public class NurseServiceTest extends TestCase {

	@Test
	public void testList() throws DD4StorageException {
		NurseService service = new NurseService(
				new GenericDAOStore<>(new DAOProtoSQLImpl<>(Nurse.class, dbConnector)));
		
		List<NurseUI> nurses = service.list(ListRequest.getDefaultInstance());
		assertTrue(nurses.size() > 0);
	}

	@Test
	public void testGet() throws DD4StorageException {
		NurseService service = new NurseService(
				new GenericDAOStore<>(new DAOProtoSQLImpl<>(Nurse.class, dbConnector)));

		NurseUI nurse = service.get(GetRequest.newBuilder()
				.setId(74)
				.build());
		assertTrue(nurse.hasAddress());
	}

	@Test
	public void testUpdate() throws DD4StorageException {
		NurseService service = new NurseService(
				new GenericDAOStore<>(new DAOProtoSQLImpl<>(Nurse.class, dbConnector)));

		NurseUI nurse = service.update(UpdateRequest.newBuilder()
				.setId(74)
				.setProto("{\"address\": {\"address\":\"212 W Mission Ct, Corona, CA 92882, USA\",\"latitude\":33.860343,\"longitude\":-117.57081299999999}}")
				.build());
		assertTrue(nurse.hasAddress());
	}
}
