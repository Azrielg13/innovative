package com.digitald4.common.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;

public class UserTest extends DD4TestCase {

	@Test
	public void setEmail() throws Exception {
		User user = new User();
		user.setEmail("eddiemay@gmail.com");
		assertEquals("eddiemay", user.getUserName());
	}
	
	@Test
	public void createNew() throws Exception {
		User user = User.get("eddiemay@gmail.com", "vxae11");
		if (user == null) {
			user = new User()
				.setType(GenData.UserType_Standard.get())
				.setEmail("eddiemay@gmail.com")
				.setFirstName("Eddie")
				.setLastName("Mayfield")
				.setPasswordRaw("vxae11");
			assertEquals("Eddie", user.getFirstName());
			assertNotSame("testpass", user.getPassword());
			user.insert();
		}
		assertNotNull(user);
	}
	
	@Test
	public void findByEmailPassword() throws Exception {
		User user = User.get("eddiemay@gmail.com", "vxae11");
		assertNotNull(user);
		user = User.get("eddiemay@gmail.com", "hjlf");
		assertNull(user);
	}
	
	@Test
	public void findByUserNamePassword() throws Exception {
		User user = User.get("eddiemay", "vxae11");
		assertNotNull(user);
		user = User.get("eddiemay", "hjlf");
		assertNull(user);
	}
}
