package com.digitald4.iis.model;
import com.digitald4.iis.dao.AssessmentEntryDAO;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="assessment_entry")
@NamedQueries({
	@NamedQuery(name = "findByID", query="SELECT o FROM AssessmentEntry o WHERE o.ID=?1"),//AUTO-GENERATED
	@NamedQuery(name = "findAll", query="SELECT o FROM AssessmentEntry o"),//AUTO-GENERATED
	@NamedQuery(name = "findAllActive", query="SELECT o FROM AssessmentEntry o WHERE o.DELETED_TS IS NULL"),//AUTO-GENERATED
	@NamedQuery(name = "findByAppointment", query="SELECT o FROM AssessmentEntry o WHERE o.APPOINTMENT_ID=?1 AND o.DELETED_TS IS NULL"),//AUTO-GENERATED
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
}