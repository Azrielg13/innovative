package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.AssessmentEntry;
import com.digitald4.common.model.GeneralData;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
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
	public static AssessmentEntry getInstance(Integer id){
		return getInstance(id, true);
	}
	public static AssessmentEntry getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		AssessmentEntry o = null;
		if(fetch || cache != null && cache.contains(AssessmentEntry.class, pk))
			o = em.find(AssessmentEntry.class, pk);
		return o;
	}
	public static Collection<AssessmentEntry> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<AssessmentEntry> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<AssessmentEntry> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM AssessmentEntry o";
		if(props != null && props.length > 0){
			qlString += " WHERE";
			int p=0;
			for(String prop:props){
				if(p > 0)
					qlString +=" AND";
				if(values[p]==null)
					qlString += " o."+prop+" IS NULL";
				else
					qlString += " o."+prop+" = ?"+(p+1);
				p++;
			}
		}
		return getCollection(qlString,values);
	}
	public synchronized static Collection<AssessmentEntry> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<AssessmentEntry> tq = em.createQuery(jpql,AssessmentEntry.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static Collection<AssessmentEntry> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<AssessmentEntry> tq = em.createNamedQuery(name,AssessmentEntry.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public AssessmentEntryDAO(){}
	public AssessmentEntryDAO(Integer id){
		this.id=id;
	}
	public AssessmentEntryDAO(AssessmentEntryDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(AssessmentEntryDAO orig){
		this.appointmentId=orig.getAppointmentId();
		this.assessmentId=orig.getAssessmentId();
		this.valueId=orig.getValueId();
		this.valueStr=orig.getValueStr();
		this.ack=orig.isAck();
	}
	public String getHashKey(){
		return getHashKey(getKeyValues());
	}
	public Object[] getKeyValues(){
		return new Object[]{id};
	}
	@Override
	public int hashCode(){
		return PrimaryKey.hashCode(getKeyValues());
	}
	@Id
	@GeneratedValue
	@Column(name="ID",nullable=false)
	public Integer getId(){
		return id;
	}
	public AssessmentEntry setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
		}
		return (AssessmentEntry)this;
	}
	@Column(name="APPOINTMENT_ID",nullable=false)
	public Integer getAppointmentId(){
		return appointmentId;
	}
	public AssessmentEntry setAppointmentId(Integer appointmentId)throws Exception{
		if(!isSame(appointmentId, getAppointmentId())){
			Integer oldValue = getAppointmentId();
			this.appointmentId=appointmentId;
			setProperty("APPOINTMENT_ID", appointmentId, oldValue);
			appointment=null;
		}
		return (AssessmentEntry)this;
	}
	@Column(name="ASSESSMENT_ID",nullable=false)
	public Integer getAssessmentId(){
		return assessmentId;
	}
	public AssessmentEntry setAssessmentId(Integer assessmentId)throws Exception{
		if(!isSame(assessmentId, getAssessmentId())){
			Integer oldValue = getAssessmentId();
			this.assessmentId=assessmentId;
			setProperty("ASSESSMENT_ID", assessmentId, oldValue);
			assessment=null;
		}
		return (AssessmentEntry)this;
	}
	@Column(name="VALUE_ID",nullable=true)
	public Integer getValueId(){
		return valueId;
	}
	public AssessmentEntry setValueId(Integer valueId)throws Exception{
		if(!isSame(valueId, getValueId())){
			Integer oldValue = getValueId();
			this.valueId=valueId;
			setProperty("VALUE_ID", valueId, oldValue);
			valueGD=null;
		}
		return (AssessmentEntry)this;
	}
	@Column(name="VALUE_STR",nullable=true,length=64)
	public String getValueStr(){
		return valueStr;
	}
	public AssessmentEntry setValueStr(String valueStr)throws Exception{
		if(!isSame(valueStr, getValueStr())){
			String oldValue = getValueStr();
			this.valueStr=valueStr;
			setProperty("VALUE_STR", valueStr, oldValue);
		}
		return (AssessmentEntry)this;
	}
	@Column(name="ACK",nullable=true)
	public boolean isAck(){
		return ack;
	}
	public AssessmentEntry setAck(boolean ack)throws Exception{
		if(!isSame(ack, isAck())){
			boolean oldValue = isAck();
			this.ack=ack;
			setProperty("ACK", ack, oldValue);
		}
		return (AssessmentEntry)this;
	}
	public Appointment getAppointment(){
		if(appointment==null)
			appointment=Appointment.getInstance(getAppointmentId());
		return appointment;
	}
	public AssessmentEntry setAppointment(Appointment appointment)throws Exception{
		setAppointmentId(appointment==null?null:appointment.getId());
		this.appointment=appointment;
		return (AssessmentEntry)this;
	}
	public GeneralData getAssessment(){
		if(assessment==null)
			assessment=GeneralData.getInstance(getAssessmentId());
		return assessment;
	}
	public AssessmentEntry setAssessment(GeneralData assessment)throws Exception{
		setAssessmentId(assessment==null?null:assessment.getId());
		this.assessment=assessment;
		return (AssessmentEntry)this;
	}
	public GeneralData getValueGD(){
		if(valueGD==null)
			valueGD=GeneralData.getInstance(getValueId());
		return valueGD;
	}
	public AssessmentEntry setValueGD(GeneralData valueGD)throws Exception{
		setValueId(valueGD==null?null:valueGD.getId());
		this.valueGD=valueGD;
		return (AssessmentEntry)this;
	}
	public Map<String,Object> getPropertyValues(){
		Hashtable<String,Object> values = new Hashtable<String,Object>();
		for(PROPERTY prop:PROPERTY.values()){
			Object value = getPropertyValue(prop);
			if(value!=null)
				values.put(""+prop,value);
		}
		return values;
	}
	public void setPropertyValues(Map<String,Object> data)throws Exception{
		for(String key:data.keySet())
			setPropertyValue(key,data.get(key).toString());
	}
	public Object getPropertyValue(String property){
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property){
		switch(property){
			case ID: return getId();
			case APPOINTMENT_ID: return getAppointmentId();
			case ASSESSMENT_ID: return getAssessmentId();
			case VALUE_ID: return getValueId();
			case VALUE_STR: return getValueStr();
			case ACK: return isAck();
		}
		return null;
	}
	public void setPropertyValue(String property, String value)throws Exception{
		if(property==null)return;
		setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}
	public void setPropertyValue(PROPERTY property, String value)throws Exception{
		switch(property){
			case ID:setId(Integer.valueOf(value)); break;
			case APPOINTMENT_ID:setAppointmentId(Integer.valueOf(value)); break;
			case ASSESSMENT_ID:setAssessmentId(Integer.valueOf(value)); break;
			case VALUE_ID:setValueId(Integer.valueOf(value)); break;
			case VALUE_STR:setValueStr(String.valueOf(value)); break;
			case ACK:setAck(Boolean.valueOf(value)); break;
		}
	}
	public AssessmentEntry copy()throws Exception{
		AssessmentEntry cp = new AssessmentEntry((AssessmentEntry)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(AssessmentEntryDAO cp)throws Exception{
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
	public void insertParents()throws Exception{
		if(appointment != null && appointment.isNewInstance())
				appointment.insert();
	}
	public void insertPreCheck()throws Exception{
		if (isNull(appointmentId))
			 throw new Exception("APPOINTMENT_ID is required.");
		if (isNull(assessmentId))
			 throw new Exception("ASSESSMENT_ID is required.");
	}
	public void insertChildren()throws Exception{
	}
}
