package com.digitald4.iis.tools;

import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.common.storage.DAOSQLImpl;
import com.digitald4.common.tools.GenDataCompiler;

public class CompileGenData {
	public static void main(String[] args) throws Exception {
		new GenDataCompiler("iis",
				new DAOSQLImpl(new DBConnectorThreadPoolImpl(
						"org.gjt.mm.mysql.Driver",
						"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
						"dd4_user", "getSchooled85")),
				"src/main/java/com/digitald4/iis/storage/GenData.java",
				"src/main/js/gendata.js").compile();
	}
}
