package com.digitald4.iis.tools;

import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.common.proto.DD4Protos.GeneralData;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.tools.GeneralDataJSCompiler;

/**
 * Created by eddiemay on 8/21/16.
 */
public class CompileGeneralDataJS {
	public static void main(String[] args) throws Exception {
		DBConnector dbConnector = new DBConnectorThreadPoolImpl(
				"org.gjt.mm.mysql.Driver",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"dd4_user", "getSchooled85");
		DAO<GeneralData> dao = new DAOProtoSQLImpl<>(GeneralData.class, dbConnector, null, "general_data");
		new GeneralDataJSCompiler("iis", dao, "src-gen/js/generaldata.js").compile();
	}
}
