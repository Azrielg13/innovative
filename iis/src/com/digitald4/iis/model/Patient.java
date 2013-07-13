package com.digitald4.iis.model;
import java.util.Collection;

import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.dao.PatientDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="patient")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Patient o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Patient o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Patient o"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM patient o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Patient extends PatientDAO{
	
	public Patient() {
		try {
			setReferralResolution(GenData.PATIENT_PENDING.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Patient(Integer id) {
		super(id);
	}
	
	public Patient(Patient orig) {
		super(orig);
	}
	
	public String getLink() {
		return "<a href=\"patient?id="+getId()+"\">"+this+"</a>";
	}
	
	public String toString() {
		return getName();
	}
	
	public static Collection<Patient> getPatientsByState(GeneralData state) {
		return getCollection(new String[]{""+PROPERTY.REFERRAL_RESOLUTION_ID}, state.getId());
	}
}
