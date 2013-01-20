package com.digitald4.common.test;

import org.junit.BeforeClass;

import com.digitald4.common.jpa.EntityManagerHelper;


public class DD4TestCase{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://192.168.1.103/iis?autoReconnect=true", "iis", "webpass");
	}
}
