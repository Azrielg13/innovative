package com.digitald4.iis.model;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.dao.AppointmentDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="appointment")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Appointment o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Appointment o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Appointment o WHERE o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByPatient", query="SELECT o FROM Appointment o WHERE o.PATIENT_ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM Appointment o WHERE o.NURSE_ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM appointment o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Appointment extends AppointmentDAO{
	public Appointment(){
	}
	public Appointment(Integer id){
		super(id);
	}
	public Appointment(Appointment orig){
		super(orig);
	}
	public Object getPropertyValue(String property) {
		if (Character.isDigit(property.charAt(0))) {
			return null;
		}
		return super.getPropertyValue(property);
	}
	public void setPropertyValue(String property, String value) throws Exception {
		if (Character.isDigit(property.charAt(0))) {
			setAssessmentEntry(GeneralData.getInstance(Integer.parseInt(property)), value);
		} else {
			super.setPropertyValue(property, value);
		}
	}
	public Appointment setAssessmentEntry(GeneralData assessment, String value) throws Exception {
		for (AssessmentEntry ae : getAssessmentEntrys()) {
			if (ae.getAssessment() == assessment) {
				ae.setValueStr(value);
				return this;
			}
		}
		return addAssessmentEntry(new AssessmentEntry().setAssessment(assessment).setValueStr(value));
	}
}
