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
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://192.168.1.103/iis?autoReconnect=true", "iis", "webpass");
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void createNew() throws Exception {
		User user = User.getInstance("eddiemay", "testpass");
		if (user == null) {
			user = new User()
				.setType(GenData.UserType_Standard.getInstance())
				.setUsername("eddiemay")
				.setEmail("eddiemay@gmail.com")
				.setFirstName("Eddie")
				.setLastName("Mayfield")
				.setPassword("testpass");
			assertEquals("Eddie",user.getFirstName());
			user.insert();
		}
		assertNotNull(user);
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
