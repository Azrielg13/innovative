package com.digitald4.iis.tools;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.*;
import com.digitald4.common.tools.GenDataCompiler;
import javax.inject.Provider;

public class CompileGenData {
	public static void main(String[] args) throws Exception {
		APIConnector apiConnector = new APIConnector("https://ip360-179401.appspot.com/_ah/api", "v1");
		DAOApiProtoImpl messageDAO = new DAOApiProtoImpl(apiConnector);
		DAORouterImpl dao = new DAORouterImpl(messageDAO, new HasProtoDAO(messageDAO), new DAOApiImpl(apiConnector));
		Provider<DAO> daoProvider = () -> dao;
		new GenDataCompiler(
				"iis", dao, "src/main/java/com/digitald4/iis/storage/GenData.java", "src/main/js/gendata.js")
				.compile();
	}
}
