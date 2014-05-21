package com.digitald4.iis.model;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.*;

import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.util.Pair;


public class PatientTest extends DD4TestCase {
	private static Patient patient = new Patient();
	
	@Test
	public void testSetPropertyValue() throws Exception {
		patient.setPropertyValue("name", "Larry");
		assertEquals("Larry", patient.getName());
		patient.setPropertyValue("referral_resolution_id", ""+5);
		assertSame(5, patient.getReferralResolutionId());
		patient.setPropertyValue("referral_resolution_id", "6");
		assertSame(6, patient.getReferralResolutionId());
		patient.setPropertyValue("labs", "true");
		assertSame(true, patient.isLabs());
		patient.setPropertyValue("first_recert_due", "02/08/2013");
		patient.setPropertyValue("referral_date", "");
		assertNull(patient.getReferralDate());
	}
	
	@Test
	public void testGetAppointments() {
		for (Patient patient : Patient.getAllActive()) {
			assertTrue(patient.getAppointments() != null);
		}
	}
	
	@Test
	public void testInsertPreCheck() throws Exception {
		patient.setReferralSource(Vendor.getAllActive().get(0));
		patient.setMrNum("T548-7369-1981");
		patient.setDianosis(GenData.DIANOSIS.get().getGeneralDatas().iterator().next());
		patient.setVendor(Vendor.getAllActive().get(0));
		patient.setReferralDate(Calendar.getInstance().getTime());
		try {
			patient.insertPreCheck();
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("NAME is required."));
		}
		
		patient.setName("Eddie Cane");
		patient.insertPreCheck();
	}
	
	@Test
	public void testInsert() throws Exception {
		patient.setReferralSource(Vendor.getAllActive().get(0));
		patient.setName("Eddie Cane");
		patient.setMrNum("T548-7369-1981");
		patient.setDianosis(GenData.DIANOSIS.get().getGeneralDatas().iterator().next());
		patient.setVendor(Vendor.getAllActive().get(0));
		patient.setReferralDate(Calendar.getInstance().getTime());
		patient.setServiceAddress("212 W. Mission Ct, Corona, CA 92882, USA")
				.setLatitude(33.860343)
				.setLongitude(-117.570813);
		patient.insert();
	}
	
	@Test
	public void testGetNursesByDistance() throws Exception {
		patient.setName("Eddie Cane");
		patient.setServiceAddress("212 W. Mission Ct, Corona, CA 92882, USA")
				.setLatitude(33.860343)
				.setLongitude(-117.570813);
		for (Pair<Nurse,Double> pair : patient.getNursesByDistance()) {
			System.out.println(pair.getLeft() + " - " + pair.getRight() + " miles away");
		}
	}
	
	@Test
	public void testDelete() throws Exception {
		if (!patient.isNewInstance()) {
			assertNotNull(Patient.getInstance(patient.getId()));
			patient.delete();
			assertNull(Patient.getInstance(patient.getId()));
		}
	}
}
