package com.digitald4.common.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.digitald4.common.jpa.EntityManagerHelper;

public class UserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://192.168.1.19/iis?autoReconnect=true", "iis", "webpass");
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void createNew() {
		User user = new User();
		user.setType(GenData.Standard.getInstance());
		user.setUsername("eddiemay");
		user.setEmail("eddiemay@gmail.com");
		user.setFirstName("Eddie");
		user.setLastName("Mayfield");
		user.setPassword("testpass");
		assertEquals("Eddie",user.getFirstName());
		user.insert();
	}
	
	@Test
	public void findByUsername() {
		User user = User.getInstance("eddiemay", "testpass");
		assertNotNull(user);
		user = User.getInstance("eddiemay", "hjlf");
		assertNull(user);
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}
