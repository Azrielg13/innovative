package com.digitald4.iis.model;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.*;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.common.test.DD4TestCase;
import com.digitald4.iis.model.Nurse;

public class NurseTest extends DD4TestCase {
	private static User user;
	private static Nurse nurse;
	
	@Before
	public void testCreate() throws Exception {
		nurse = new Nurse().setUser(new User()
				.setFirstName("Nurse").setLastName("Betty").setEmail("betty@example.com")
				.setType(com.digitald4.common.model.GenData.UserType_Standard.get())
				).setAddress("123 Boardway St, New York, NY").setPayRate(40);
		user = nurse.getUser();
		assertEquals("Betty", nurse.getUser().getLastName());
		assertTrue(40 == nurse.getPayRate());
	}
	
	@Test
	public void testReadAll() {
		List<Nurse> nurses = Nurse.getAll();
		assertTrue(nurses.size() > 0);
		for (Nurse nurse : nurses) {
			System.out.println(nurse);
			nurse.getPendAssesCount();
			nurse.getPendAsses();
			nurse.getStatus();
		}
		Vendor.getAll();
	}
	
	@Test
	public void testInsert() throws Exception {
		assertFalse(nurse.getPayRate() == 0);
		nurse.insert();
		assertFalse(nurse.isNewInstance());
		assertEquals(user.getId(), nurse.getId());
	}
	
	@Test
	public void testReadAppointments() {
		Nurse nurse = Nurse.getInstance(6);
		System.out.println(nurse);
		assertTrue(nurse.getAppointments().size() > 2);
		assertTrue(nurse.getPayRate() > 0);
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
		Nurse nurse = Nurse.getInstance(5);
		assertTrue(nurse.getPendAsses().size() > 0);
		System.out.println("Pending assessments: " + nurse.getPendAsses().size());
		Appointment app = nurse.getPendAsses().iterator().next();
		assertNull(app.getTimeIn());
	}
	
	@Test
	public void toJSON() throws Exception {
		JSONObject json = nurse.toJSON();
		System.out.println(nurse.getUser().toJSON());
		System.out.println("json: " + json);
		System.out.println("dao json: " + DataAccessObject.toJSON(nurse));
		assertNotNull(json);
		assertNotNull(json.toString());
		assertEquals(nurse.getAddress(), json.get("address"));
		json = new JSONObject().put("word", "hello");
		System.out.println("json: " + json);
	}
	
	@Test
	public void updateFromJSON() throws Exception {
		License license = nurse.getAllLicenses().get(0).getRight().get(0);
		JSONObject json = license.toJSON();
		System.out.println("License json: " + json);
		json.put("number", "123456");
		license.update(json);
		assertEquals("123456", license.getNumber());
	}
	
	@After
	public void testDelete() throws Exception {
		if (!nurse.isNewInstance()) {
			nurse.getUser().delete();
			nurse.delete();
		}
	}
}
