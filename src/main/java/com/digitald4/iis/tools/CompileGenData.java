package com.digitald4.iis.tools;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.tools.GenDataCompiler;

import java.time.Clock;

public class CompileGenData {
	public static void main(String[] args) throws Exception {
		APIConnector apiConnector = new APIConnector("https://ip360-179401.appspot.com/_ah/api", "v1");
		DAO dao = new DAOApiImpl(apiConnector, Clock.systemUTC());
		new GenDataCompiler(
				"iis", dao, "src/main/java/com/digitald4/iis/storage/GenData.java", "src/main/js/gendata.js")
				.compile();
	}
}
