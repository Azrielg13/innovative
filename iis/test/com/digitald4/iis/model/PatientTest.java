package com.digitald4.iis.model;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import com.digitald4.common.test.DD4TestCase;


public class PatientTest extends DD4TestCase {
	private static Patient patient = new Patient();
	
	@Test
	public void testSetPropertyValue() throws Exception {
		patient.setPropertyValue("dianosis", "Stupid");
		assertEquals("Stupid", patient.getDianosis());
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
	public void testInsertPreCheck() throws Exception {
		patient.setReferralSource("Lady Gaga");
		patient.setMrNum("T548-7369-1981");
		patient.setDianosis("Too smart for his own good");
		patient.setBilling("Medical Center");
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
		patient.setReferralSource("Lady Gaga");
		patient.setName("Eddie Cane");
		patient.setMrNum("T548-7369-1981");
		patient.setDianosis("Too smart for his own good");
		patient.setBilling("Medical Center");
		patient.setReferralDate(Calendar.getInstance().getTime());
		
		patient.insert();
	}
	
	@Test
	public void testDelete() {
		if (!patient.isNewInstance()) {
			assertNotNull(Patient.getInstance(patient.getId()));
			patient.delete();
			assertNull(Patient.getInstance(patient.getId()));
		}
	}
}
