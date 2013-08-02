package com.digitald4.iis.model;
import com.digitald4.iis.dao.AssessmentEntryDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(schema="iis",name="assessment_entry")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM AssessmentEntry o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM AssessmentEntry o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM AssessmentEntry o"),//AUTO-GENERATED
	@NamedQuery(name = "findByAppointment", query="SELECT o FROM AssessmentEntry o WHERE o.APPOINTMENT_ID=?1"),//AUTO-GENERATED
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "refresh", query="SELECT o.* FROM assessment_entry o WHERE o.ID=?"),//AUTO-GENERATED
})
public class AssessmentEntry extends AssessmentEntryDAO{
	public AssessmentEntry(){
	}
	public AssessmentEntry(Integer id){
		super(id);
	}
	public AssessmentEntry(AssessmentEntry orig){
		super(orig);
	}
	public int compareTo(Object o) {
		if (o instanceof AssessmentEntry) {
			AssessmentEntry ae = (AssessmentEntry)o;
			if (getAssessmentId() < ae.getAssessmentId()) {
				return -1;
			}
			if (getAssessmentId() > ae.getAssessmentId()) {
				return 1;
			}
		}
		return super.compareTo(o);
	}
	
	public Object getPrevValue() throws Exception {
		Appointment prevApp = getAppointment().getPrevAppointment();
		if (prevApp == null) {
			return null;
		}
		return prevApp.getAssessmentValue(getAssessmentId());
	}
	
	public Object getValue() throws Exception {
		Object value = getValueGD();
		if (value == null) {
			value = getValueStr();
		}
		if (value == null) {
			value = getPrevValue();
		}
		return value;
	}
	public AssessmentEntry setValue(String value) throws Exception {
		if (getAssessment().getGeneralDatas().size() > 0) {
			setValueId(Integer.parseInt(value));
		} else {
			setValueStr(value);
		}
		return this;
	}
}
