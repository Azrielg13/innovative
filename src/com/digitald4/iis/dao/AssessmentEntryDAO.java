package com.digitald4.iis.dao;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.AssessmentEntry;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** TODO Copy Right*/
/**Description of class, (we need to get this from somewhere, database? xml?)*/
public abstract class AssessmentEntryDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,APPOINTMENT_ID,ASSESSMENT_ID,VALUE_ID,VALUE_STR,ACK};
	private Integer id;
	private Integer appointmentId;
	private Integer assessmentId;
	private Integer valueId;
	private String valueStr;
	private boolean ack;
	private Appointment appointment;
	private GeneralData assessment;
	private GeneralData valueGD;
	public AssessmentEntryDAO(EntityManager entityManager) {
		super(entityManager);
	}
	public AssessmentEntryDAO(EntityManager entityManager, Integer id) {
		super(entityManager);
		this.id=id;
	}
	public AssessmentEntryDAO(EntityManager entityManager, AssessmentEntryDAO orig) {
		super(entityManager, orig);
		copyFrom(orig);
	}
	public void copyFrom(AssessmentEntryDAO orig){
		this.appointmentId = orig.getAppointmentId();
		this.assessmentId = orig.getAssessmentId();
		this.valueId = orig.getValueId();
		this.valueStr = orig.getValueStr();
		this.ack = orig.isAck();
	}
	@Override
	public String getHashKey() {
		return getHashKey(getKeyValues());
	}
	public Object[] getKeyValues() {
		return new Object[]{id};
	}
	@Override
	public int hashCode() {
		return PrimaryKey.hashCode(getKeyValues());
	}
	@Id
	@GeneratedValue
	@Column(name="ID",nullable=false)
	public Integer getId(){
		return id;
	}
	public AssessmentEntry setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (AssessmentEntry)this;
	}
	@Column(name="APPOINTMENT_ID",nullable=false)
	public Integer getAppointmentId(){
		return appointmentId;
	}
	public AssessmentEntry setAppointmentId(Integer appointmentId) throws Exception  {
		Integer oldValue = getAppointmentId();
		if (!isSame(appointmentId, oldValue)) {
			this.appointmentId = appointmentId;
			setProperty("APPOINTMENT_ID", appointmentId, oldValue);
			appointment=null;
		}
		return (AssessmentEntry)this;
	}
	@Column(name="ASSESSMENT_ID",nullable=false)
	public Integer getAssessmentId(){
		return assessmentId;
	}
	public AssessmentEntry setAssessmentId(Integer assessmentId) throws Exception  {
		Integer oldValue = getAssessmentId();
		if (!isSame(assessmentId, oldValue)) {
			this.assessmentId = assessmentId;
			setProperty("ASSESSMENT_ID", assessmentId, oldValue);
			assessment=null;
		}
		return (AssessmentEntry)this;
	}
	@Column(name="VALUE_ID",nullable=true)
	public Integer getValueId(){
		return valueId;
	}
	public AssessmentEntry setValueId(Integer valueId) throws Exception  {
		Integer oldValue = getValueId();
		if (!isSame(valueId, oldValue)) {
			this.valueId = valueId;
			setProperty("VALUE_ID", valueId, oldValue);
			valueGD=null;
		}
		return (AssessmentEntry)this;
	}
	@Column(name="VALUE_STR",nullable=true,length=64)
	public String getValueStr(){
		return valueStr;
	}
	public AssessmentEntry setValueStr(String valueStr) throws Exception  {
		String oldValue = getValueStr();
		if (!isSame(valueStr, oldValue)) {
			this.valueStr = valueStr;
			setProperty("VALUE_STR", valueStr, oldValue);
		}
		return (AssessmentEntry)this;
	}
	@Column(name="ACK",nullable=true)
	public boolean isAck(){
		return ack;
	}
	public AssessmentEntry setAck(boolean ack) throws Exception  {
		boolean oldValue = isAck();
		if (!isSame(ack, oldValue)) {
			this.ack = ack;
			setProperty("ACK", ack, oldValue);
		}
		return (AssessmentEntry)this;
	}
	public Appointment getAppointment() {
		if (appointment == null) {
			return getEntityManager().find(Appointment.class, getAppointmentId());
		}
		return appointment;
	}
	public AssessmentEntry setAppointment(Appointment appointment) throws Exception {
		setAppointmentId(appointment==null?null:appointment.getId());
		this.appointment=appointment;
		return (AssessmentEntry)this;
	}
	public GeneralData getAssessment() {
		if (assessment == null) {
			return getEntityManager().find(GeneralData.class, getAssessmentId());
		}
		return assessment;
	}
	public AssessmentEntry setAssessment(GeneralData assessment) throws Exception {
		setAssessmentId(assessment==null?null:assessment.getId());
		this.assessment=assessment;
		return (AssessmentEntry)this;
	}
	public GeneralData getValueGD() {
		if (valueGD == null) {
			return getEntityManager().find(GeneralData.class, getValueId());
		}
		return valueGD;
	}
	public AssessmentEntry setValueGD(GeneralData valueGD) throws Exception {
		setValueId(valueGD==null?null:valueGD.getId());
		this.valueGD=valueGD;
		return (AssessmentEntry)this;
	}
	public Map<String,Object> getPropertyValues() {
		Hashtable<String,Object> values = new Hashtable<String,Object>();
		for(PROPERTY prop:PROPERTY.values()) {
			Object value = getPropertyValue(prop);
			if(value!=null)
				values.put(""+prop,value);
		}
		return values;
	}

	public AssessmentEntry setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key, data.get(key).toString());
		return (AssessmentEntry)this;
	}

	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case APPOINTMENT_ID: return getAppointmentId();
			case ASSESSMENT_ID: return getAssessmentId();
			case VALUE_ID: return getValueId();
			case VALUE_STR: return getValueStr();
			case ACK: return isAck();
		}
		return null;
	}

	@Override
	public AssessmentEntry setPropertyValue(String property, String value) throws Exception  {
		if(property == null) return (AssessmentEntry)this;
		return setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}

	public AssessmentEntry setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case APPOINTMENT_ID:setAppointmentId(Integer.valueOf(value)); break;
			case ASSESSMENT_ID:setAssessmentId(Integer.valueOf(value)); break;
			case VALUE_ID:setValueId(Integer.valueOf(value)); break;
			case VALUE_STR:setValueStr(String.valueOf(value)); break;
			case ACK:setAck(Boolean.valueOf(value)); break;
		}
		return (AssessmentEntry)this;
	}

	public AssessmentEntry copy() throws Exception {
		AssessmentEntry cp = new AssessmentEntry(getEntityManager(), (AssessmentEntry)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(AssessmentEntryDAO cp) throws Exception {
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(AssessmentEntryDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getAppointmentId(),o.getAppointmentId())) diffs.add("APPOINTMENT_ID");
		if(!isSame(getAssessmentId(),o.getAssessmentId())) diffs.add("ASSESSMENT_ID");
		if(!isSame(getValueId(),o.getValueId())) diffs.add("VALUE_ID");
		if(!isSame(getValueStr(),o.getValueStr())) diffs.add("VALUE_STR");
		if(!isSame(isAck(),o.isAck())) diffs.add("ACK");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(appointment != null && appointment.isNewInstance())
				appointment.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getAppointmentId()))
			 throw new Exception("APPOINTMENT_ID is required.");
		if (isNull(getAssessmentId()))
			 throw new Exception("ASSESSMENT_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
	}
}
