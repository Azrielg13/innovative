package com.digitald4.common.test;

import org.junit.BeforeClass;

import com.digitald4.common.jpa.EntityManagerHelper;


public class DD4TestCase{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
	}
}
