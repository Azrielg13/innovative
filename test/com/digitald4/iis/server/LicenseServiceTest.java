package com.digitald4.iis.server;

import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.JSONService;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.proto.IISProtos.License;
import com.digitald4.iis.proto.IISUIProtos.LicenseUI;
import com.digitald4.iis.test.TestCase;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LicenseServiceTest extends TestCase {

	@Test
	public void testList() throws Exception {
		
		JSONService service = new DualProtoService<>(LicenseUI.class,
				new GenericStore<>(new DAOProtoSQLImpl<>(License.class, dbConnector, "V_LICENSE")));
		
		Object licenses = service.performAction("list",
				new JSONObject("{\"query_param\":[{\"column\":\"expiration_date\",\"operan\":\"<\",\"value\":\"1487693850006\"}]}"));
		assertTrue(licenses.toString().length() > 10);
		System.out.println(licenses);
	}
}
