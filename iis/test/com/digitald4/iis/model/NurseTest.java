package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.junit.*;

import com.digitald4.common.model.User;
import com.digitald4.common.test.DD4TestCase;
import com.digitald4.iis.model.Nurse;

public class NurseTest extends DD4TestCase{
	private static User user;
	private static Nurse nurse;
	
	@Test
	public void testCreate() throws Exception {
		nurse = new Nurse().setUser(new User()
				.setFirstName("Nurse").setLastName("Betty").setEmail("betty@example.com")
				.setType(com.digitald4.common.model.GenData.UserType_Standard.get())
				).setAddress("123 Boardway St, New York, NY").setPayRate(40);
		user = nurse.getUser();
		assertEquals("Betty", nurse.getUser().getLastName());
	}
	
	@Test
	public void testInsert() throws Exception {
		nurse.insert();
		assertFalse(nurse.isNewInstance());
		assertEquals(user.getId(), nurse.getId());
	}
	
	@Test
	public void testDelete() {
		if (!nurse.isNewInstance()) {
			nurse.getUser().delete();
			nurse.delete();
		}
	}
}
