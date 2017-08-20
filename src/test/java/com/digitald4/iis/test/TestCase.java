package com.digitald4.iis.test;

import org.junit.BeforeClass;

import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;

public class TestCase {
	
	protected static DBConnector dbConnector = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConnector = new DBConnectorThreadPoolImpl(
				"org.gjt.mm.mysql.Driver",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"iisosnet_user", "getSchooled85");
	}
}
