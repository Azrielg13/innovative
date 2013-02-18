package com.digitald4.iis.model;
import java.util.Collection;
import java.util.Date;

import com.digitald4.common.component.CalEvent;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.dao.AppointmentDAO;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;
@Entity
@Table(schema="iis",name="appointment")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM Appointment o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM Appointment o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM Appointment o WHERE o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByPatient", query="SELECT o FROM Appointment o WHERE o.PATIENT_ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findByNurse", query="SELECT o FROM Appointment o WHERE o.NURSE_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM appointment o WHERE o.ID=?"),//AUTO-GENERATED
})
public class Appointment extends AppointmentDAO implements CalEvent {
	
	public Appointment(){
	}
	
	public Appointment(Integer id){
		super(id);
	}
	
	public Appointment(Appointment orig){
		super(orig);
	}
	
	public String getLink() {
		return "<a href=\"assessment?id="+getId()+"\">"+getPatient()+"</a>";
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
	
	public static Collection<Appointment> getPending() {
		return getCollection(new String[]{""+PROPERTY.CANCELLED,""+PROPERTY.ASSESSMENT_COMPLETE}, false, false);
	}

	@Override
	public DateTime getEndTime() {
		return getStartTime().plusMinutes(getDuration());
	}

	@Override
	public boolean isActiveOnDay(Date date) {
		return isActiveBetween(new DateTime(date), new DateTime(date).plusDays(1));
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isActiveBetween(DateTime start, DateTime end) {
		DateTime st = getStartTime();
		DateTime et = getEndTime();
		// Did this event start any time between these periods or did these period start any time during this event
		return (start.isBefore(st) && end.isAfter(st) || st.isBefore(start) && et.isAfter(start));
	}
}
