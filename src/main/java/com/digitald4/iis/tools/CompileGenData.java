package com.digitald4.iis.tools;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.tools.GenDataCompiler;

public class CompileGenData {
	public static void main(String[] args) throws Exception {
		DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1"));
		new GenDataCompiler(new GeneralDataStore(() -> dao), "iis",
				"src/main/java/com/digitald4/iis/storage/GenData.java", "src/main/js/GenData.js").compile();
	}
}
