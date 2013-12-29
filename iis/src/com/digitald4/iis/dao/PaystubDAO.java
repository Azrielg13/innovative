package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Paystub;
import java.sql.Blob;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import org.joda.time.DateTime;
public abstract class PaystubDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,NURSE_ID,NAME,GENERATION_TIME,DATA};
	private Integer id;
	private Integer nurseId;
	private String name;
	private DateTime generationTime;
	private Blob data;
	private List<Appointment> appointments;
	private Nurse nurse;
	public static Paystub getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Paystub getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Paystub o = null;
		if(fetch || cache != null && cache.contains(Paystub.class, pk))
			o = em.find(Paystub.class, pk);
		return o;
	}
	public static List<Paystub> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Paystub> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Paystub> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Paystub o";
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
	public synchronized static List<Paystub> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Paystub> tq = em.createQuery(jpql,Paystub.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Paystub> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Paystub> tq = em.createNamedQuery(name,Paystub.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public PaystubDAO(){}
	public PaystubDAO(Integer id){
		this.id=id;
	}
	public PaystubDAO(PaystubDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(PaystubDAO orig){
		this.nurseId=orig.getNurseId();
		this.name=orig.getName();
		this.generationTime=orig.getGenerationTime();
		this.data=orig.getData();
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
	public Paystub setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="NURSE_ID",nullable=false)
	public Integer getNurseId(){
		return nurseId;
	}
	public Paystub setNurseId(Integer nurseId)throws Exception{
		if(!isSame(nurseId, getNurseId())){
			Integer oldValue = getNurseId();
			this.nurseId=nurseId;
			setProperty("NURSE_ID", nurseId, oldValue);
			nurse=null;
		}
		return (Paystub)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public Paystub setName(String name)throws Exception{
		if(!isSame(name, getName())){
			String oldValue = getName();
			this.name=name;
			setProperty("NAME", name, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="GENERATION_TIME",nullable=true)
	public DateTime getGenerationTime(){
		return generationTime;
	}
	public Paystub setGenerationTime(DateTime generationTime)throws Exception{
		if(!isSame(generationTime, getGenerationTime())){
			DateTime oldValue = getGenerationTime();
			this.generationTime=generationTime;
			setProperty("GENERATION_TIME", generationTime, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="DATA",nullable=true)
	public Blob getData(){
		return data;
	}
	public Paystub setData(Blob data)throws Exception{
		if(!isSame(data, getData())){
			Blob oldValue = getData();
			this.data=data;
			setProperty("DATA", data, oldValue);
		}
		return (Paystub)this;
	}
	public Nurse getNurse(){
		if(nurse==null)
			nurse=Nurse.getInstance(getNurseId());
		return nurse;
	}
	public Paystub setNurse(Nurse nurse)throws Exception{
		setNurseId(nurse==null?null:nurse.getId());
		this.nurse=nurse;
		return (Paystub)this;
	}
	public List<Appointment> getAppointments(){
		if(isNewInstance() || appointments != null){
			if(appointments == null)
				appointments = new SortedList<Appointment>();
			return appointments;
		}
		return Appointment.getNamedCollection("findByPaystub",getId());
	}
	public Paystub addAppointment(Appointment appointment)throws Exception{
		appointment.setPaystub((Paystub)this);
		if(isNewInstance() || appointments != null)
			getAppointments().add(appointment);
		else
			appointment.insert();
		return (Paystub)this;
	}
	public Paystub removeAppointment(Appointment appointment)throws Exception{
		if(isNewInstance() || appointments != null)
			getAppointments().remove(appointment);
		else
			appointment.delete();
		return (Paystub)this;
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
			case NURSE_ID: return getNurseId();
			case NAME: return getName();
			case GENERATION_TIME: return getGenerationTime();
			case DATA: return getData();
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
			case NURSE_ID:setNurseId(Integer.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case GENERATION_TIME:setGenerationTime(new DateTime(value)); break;
			case DATA: // setData(Blob.valueOf(value)); break;
		}
	}
	public Paystub copy()throws Exception{
		Paystub cp = new Paystub((Paystub)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(PaystubDAO cp)throws Exception{
		super.copyChildrenTo(cp);
		for(Appointment child:getAppointments())
			cp.addAppointment(child.copy());
	}
	public Vector<String> getDifference(PaystubDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getNurseId(),o.getNurseId())) diffs.add("NURSE_ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getGenerationTime(),o.getGenerationTime())) diffs.add("GENERATION_TIME");
		if(!isSame(getData(),o.getData())) diffs.add("DATA");
		return diffs;
	}
	public void insertParents()throws Exception{
		if(nurse != null && nurse.isNewInstance())
				nurse.insert();
	}
	public void insertPreCheck()throws Exception{
		if (isNull(nurseId))
			 throw new Exception("NURSE_ID is required.");
		if (isNull(name))
			 throw new Exception("NAME is required.");
	}
	public void insertChildren()throws Exception{
		if(appointments != null){
			for(Appointment appointment:getAppointments())
				appointment.setPaystub((Paystub)this);
		}
		if(appointments != null){
			for(Appointment appointment:getAppointments())
				if(appointment.isNewInstance())
					appointment.insert();
			appointments = null;
		}
	}
}
