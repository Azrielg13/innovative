package com.digitald4.common.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.digitald4.common.jpa.EntityManagerHelper;

public class GeneralDataTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://192.168.1.19/iis?autoReconnect=true", "iis", "webpass");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void insertDefaults() {
		GeneralData userType = new GeneralData();
		userType.setName("User Type");
		userType.setDescription("Types of users");
		userType.setInGroupId(1);
		GeneralData admin = new GeneralData();
		admin.setName("Admin");
		admin.setDescription("Admin User");
		admin.setInGroupId(1);
		admin.setRank(0);
		userType.addGeneralData(admin);
		GeneralData standard = new GeneralData();
		standard.setName("Standard");
		standard.setDescription("Standard User");
		standard.setInGroupId(2);
		standard.setRank(1);
		userType.addGeneralData(standard);
		assertEquals(2,userType.getGeneralDatas().size());
		System.out.println(userType.getGeneralDatas());
		userType.insert();
	}

}
