package com.digitald4.iis.model;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.*;

import com.digitald4.common.model.GeneralData;
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
	public void testReadAppointments() {
		Nurse nurse = Nurse.getInstance(2);
		assertTrue(nurse.getAppointments().size() > 2);
	}
	
	@Test
	public void getAllLicenses() throws Exception {
		for(GeneralData type : GenData.LICENSE.get().getGeneralDatas()) {
			License license = nurse.getLicense(type);
			System.out.println(license + " " + license.getNumber() + " " + license.getValidDate() + " " + license.getExpirationDate());
		}
	}
	
	@Test
	public void getPendAsses() {
		Nurse nurse = Nurse.getInstance(2);
		assertTrue(nurse.getPendAsses().size() > 0);
		System.out.println("Pending assessments: " + nurse.getPendAsses().size());
		Appointment app = nurse.getPendAsses().iterator().next();
		assertNull(app.getTimeIn());
	}
	
	@Test
	public void toJSON() throws Exception {
		JSONObject json = nurse.toJSON();
		System.out.println("json: " + json);
		assertNotNull(json.toString());
		assertEquals(nurse.getAddress(), json.get("address"));
		json = new JSONObject().put("word", "hello");
		System.out.println("json: " + json);
	}
	
	@Test
	public void updateFromJSON() throws Exception {
		License license = nurse.getAllLicenses().iterator().next();
		JSONObject json = license.toJSON();
		System.out.println("License json: " + json);
		json.put("number", "123456");
		license.update(json);
		assertEquals("123456", license.getNumber());
	}
	
	@Test
	public void testDelete() {
		if (!nurse.isNewInstance()) {
			nurse.getUser().delete();
			nurse.delete();
		}
	}
}
