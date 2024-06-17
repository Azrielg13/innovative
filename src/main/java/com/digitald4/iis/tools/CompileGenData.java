package com.digitald4.iis.tools;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.storage.GenData;

public class CompileGenData {
	public static void main(String[] args) throws Exception {
		Long groupId = GenData.IV_ACCESS;
		DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
		GeneralDataStore generalDataStore = new GeneralDataStore(() -> dao);
		/* new GenDataCompiler(generalDataStore, "iis",
				"src/main/java/com/digitald4/iis/storage/GenData.java", "src/main/js/GenData.js").compile(); */
		var gdList = generalDataStore.listByGroupId(groupId).getItems().stream()
				.filter(gd -> !gd.getName().isEmpty()).collect(toImmutableList());
		gdList.stream().map(JSONUtil::toJSON).forEach(System.out::println);
		DAO daoTest =
				new DAOApiImpl(new APIConnector("https://test-dot-ip360-179401.uc.r.appspot.com/_api", "v1").loadIdToken());
		daoTest.create(generalDataStore.get(groupId));
		gdList.forEach(daoTest::create);
	}
}
