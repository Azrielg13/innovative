package com.digitald4.iis.tools;

import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.common.proto.DD4Protos.GeneralData;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.tools.GenDataCompiler;

public class CompileGenData {
	public static void main(String[] args) throws Exception {
		DBConnector dbConnector = new DBConnectorThreadPoolImpl(
				"org.gjt.mm.mysql.Driver",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"dd4_user", "getSchooled85");
		DAO<GeneralData> dao = new DAOProtoSQLImpl<>(GeneralData.class, dbConnector);
		new GenDataCompiler("iis", dao, "src/main/java/com/digitald4/iis/storage/GenData.java",
				"src/main/js/gendata.js").compile();
	}
}
