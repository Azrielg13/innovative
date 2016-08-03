package com.digitald4.iis.server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.digitald4.common.dao.sql.DAOProtoSQLImpl;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.ProtoService;
import com.digitald4.common.store.impl.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.digitald4.iis.test.TestCase;

public class NurseServiceTest extends TestCase {

	@Test
	public void testList() throws DD4StorageException {
		
		ProtoService<NurseUI> service = new DualProtoService<>(NurseUI.class,
				new GenericDAOStore<>(new DAOProtoSQLImpl<>(Nurse.class, dbConnector)));
		
		List<NurseUI> nurses = service.list(ListRequest.getDefaultInstance());
		assertTrue(nurses.size() > 0);
	}
}
