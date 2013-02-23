package com.digitald4.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;

public class UserTest extends DD4TestCase {

	@Test
	public void createNew() throws Exception {
		User user = User.getInstance("eddiemay", "testpass");
		if (user == null) {
			user = new User()
				.setType(GenData.UserType_Standard.getInstance())
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
	public void findByEmailPassword() {
		User user = User.getInstance("eddiemay@gmail.com", "testpass");
		assertNotNull(user);
		user = User.getInstance("eddiemay@gmail.com", "hjlf");
		assertNull(user);
	}
}
