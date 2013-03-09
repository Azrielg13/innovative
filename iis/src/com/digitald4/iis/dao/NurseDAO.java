package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import com.digitald4.common.model.User;
import com.digitald4.common.util.FormatText;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
public abstract class NurseDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,REG_DATE,ACTIVE,ADDRESS,PHONE_NUMBER,REFERRAL_SOURCE,PAY_RATE,PAY_RATE_2HR_OR_LESS,MILEAGE_RATE};
	private Integer id;
	private Date regDate;
	private boolean active = false;
	private String address;
	private String phoneNumber;
	private String referralSource;
	private double payRate;
	private double payRate2HrOrLess;
	private double mileageRate;
	private Collection<Appointment> appointments;
	private Collection<License> licenses;
	private User user;
	public static Nurse getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Nurse getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Nurse o = null;
		if(fetch || cache != null && cache.contains(Nurse.class, pk))
			o = em.find(Nurse.class, pk);
		return o;
	}
	public static Collection<Nurse> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<Nurse> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<Nurse> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Nurse o";
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
	public synchronized static Collection<Nurse> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Nurse> tq = em.createQuery(jpql,Nurse.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static Collection<Nurse> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Nurse> tq = em.createNamedQuery(name,Nurse.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public NurseDAO(){}
	public NurseDAO(Integer id){
		this.id=id;
	}
	public NurseDAO(NurseDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(NurseDAO orig){
		this.regDate=orig.getRegDate();
		this.active=orig.isActive();
		this.address=orig.getAddress();
		this.phoneNumber=orig.getPhoneNumber();
		this.referralSource=orig.getReferralSource();
		this.payRate=orig.getPayRate();
		this.payRate2HrOrLess=orig.getPayRate2HrOrLess();
		this.mileageRate=orig.getMileageRate();
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
	public Nurse setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
			user=null;
		}
		return (Nurse)this;
	}
	@Column(name="REG_DATE",nullable=true)
	public Date getRegDate(){
		return regDate;
	}
	public Nurse setRegDate(Date regDate)throws Exception{
		if(!isSame(regDate, getRegDate())){
			Date oldValue = getRegDate();
			this.regDate=regDate;
			setProperty("REG_DATE", regDate, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public Nurse setActive(boolean active)throws Exception{
		if(!isSame(active, isActive())){
			boolean oldValue = isActive();
			this.active=active;
			setProperty("ACTIVE", active, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="ADDRESS",nullable=false,length=50)
	public String getAddress(){
		return address;
	}
	public Nurse setAddress(String address)throws Exception{
		if(!isSame(address, getAddress())){
			String oldValue = getAddress();
			this.address=address;
			setProperty("ADDRESS", address, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PHONE_NUMBER",nullable=true,length=20)
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public Nurse setPhoneNumber(String phoneNumber)throws Exception{
		if(!isSame(phoneNumber, getPhoneNumber())){
			String oldValue = getPhoneNumber();
			this.phoneNumber=phoneNumber;
			setProperty("PHONE_NUMBER", phoneNumber, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="REFERRAL_SOURCE",nullable=true,length=100)
	public String getReferralSource(){
		return referralSource;
	}
	public Nurse setReferralSource(String referralSource)throws Exception{
		if(!isSame(referralSource, getReferralSource())){
			String oldValue = getReferralSource();
			this.referralSource=referralSource;
			setProperty("REFERRAL_SOURCE", referralSource, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_RATE",nullable=false)
	public double getPayRate(){
		return payRate;
	}
	public Nurse setPayRate(double payRate)throws Exception{
		if(!isSame(payRate, getPayRate())){
			double oldValue = getPayRate();
			this.payRate=payRate;
			setProperty("PAY_RATE", payRate, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_RATE_2HR_OR_LESS",nullable=true)
	public double getPayRate2HrOrLess(){
		return payRate2HrOrLess;
	}
	public Nurse setPayRate2HrOrLess(double payRate2HrOrLess)throws Exception{
		if(!isSame(payRate2HrOrLess, getPayRate2HrOrLess())){
			double oldValue = getPayRate2HrOrLess();
			this.payRate2HrOrLess=payRate2HrOrLess;
			setProperty("PAY_RATE_2HR_OR_LESS", payRate2HrOrLess, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="MILEAGE_RATE",nullable=true)
	public double getMileageRate(){
		return mileageRate;
	}
	public Nurse setMileageRate(double mileageRate)throws Exception{
		if(!isSame(mileageRate, getMileageRate())){
			double oldValue = getMileageRate();
			this.mileageRate=mileageRate;
			setProperty("MILEAGE_RATE", mileageRate, oldValue);
		}
		return (Nurse)this;
	}
	public User getUser(){
		if(user==null)
			user=User.getInstance(getId());
		return user;
	}
	public Nurse setUser(User user)throws Exception{
		setId(user==null?null:user.getId());
		this.user=user;
		return (Nurse)this;
	}
	public Collection<Appointment> getAppointments(){
		if(isNewInstance() || appointments != null){
			if(appointments == null)
				appointments = new TreeSet<Appointment>();
			return appointments;
		}
		return Appointment.getNamedCollection("findByNurse",getId());
	}
	public Nurse addAppointment(Appointment appointment)throws Exception{
		appointment.setNurse((Nurse)this);
		if(isNewInstance() || appointments != null)
			getAppointments().add(appointment);
		else
			appointment.insert();
		return (Nurse)this;
	}
	public Nurse removeAppointment(Appointment appointment)throws Exception{
		if(isNewInstance() || appointments != null)
			getAppointments().remove(appointment);
		else
			appointment.delete();
		return (Nurse)this;
	}
	public Collection<License> getLicenses(){
		if(isNewInstance() || licenses != null){
			if(licenses == null)
				licenses = new TreeSet<License>();
			return licenses;
		}
		return License.getNamedCollection("findByNurse",getId());
	}
	public Nurse addLicense(License license)throws Exception{
		license.setNurse((Nurse)this);
		if(isNewInstance() || licenses != null)
			getLicenses().add(license);
		else
			license.insert();
		return (Nurse)this;
	}
	public Nurse removeLicense(License license)throws Exception{
		if(isNewInstance() || licenses != null)
			getLicenses().remove(license);
		else
			license.delete();
		return (Nurse)this;
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
			case REG_DATE: return getRegDate();
			case ACTIVE: return isActive();
			case ADDRESS: return getAddress();
			case PHONE_NUMBER: return getPhoneNumber();
			case REFERRAL_SOURCE: return getReferralSource();
			case PAY_RATE: return getPayRate();
			case PAY_RATE_2HR_OR_LESS: return getPayRate2HrOrLess();
			case MILEAGE_RATE: return getMileageRate();
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
			case REG_DATE:setRegDate(FormatText.parseDate(value)); break;
			case ACTIVE:setActive(Boolean.valueOf(value)); break;
			case ADDRESS:setAddress(String.valueOf(value)); break;
			case PHONE_NUMBER:setPhoneNumber(String.valueOf(value)); break;
			case REFERRAL_SOURCE:setReferralSource(String.valueOf(value)); break;
			case PAY_RATE:setPayRate(Double.valueOf(value)); break;
			case PAY_RATE_2HR_OR_LESS:setPayRate2HrOrLess(Double.valueOf(value)); break;
			case MILEAGE_RATE:setMileageRate(Double.valueOf(value)); break;
		}
	}
	public Nurse copy()throws Exception{
		Nurse cp = new Nurse((Nurse)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(NurseDAO cp)throws Exception{
		super.copyChildrenTo(cp);
		for(Appointment child:getAppointments())
			cp.addAppointment(child.copy());
		for(License child:getLicenses())
			cp.addLicense(child.copy());
	}
	public Vector<String> getDifference(NurseDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getRegDate(),o.getRegDate())) diffs.add("REG_DATE");
		if(!isSame(isActive(),o.isActive())) diffs.add("ACTIVE");
		if(!isSame(getAddress(),o.getAddress())) diffs.add("ADDRESS");
		if(!isSame(getPhoneNumber(),o.getPhoneNumber())) diffs.add("PHONE_NUMBER");
		if(!isSame(getReferralSource(),o.getReferralSource())) diffs.add("REFERRAL_SOURCE");
		if(!isSame(getPayRate(),o.getPayRate())) diffs.add("PAY_RATE");
		if(!isSame(getPayRate2HrOrLess(),o.getPayRate2HrOrLess())) diffs.add("PAY_RATE_2HR_OR_LESS");
		if(!isSame(getMileageRate(),o.getMileageRate())) diffs.add("MILEAGE_RATE");
		return diffs;
	}
	public void insertParents()throws Exception{
		if(user != null && user.isNewInstance())
				user.insert();
	}
	public void insertPreCheck()throws Exception{
		if (isNull(address))
			 throw new Exception("ADDRESS is required.");
		if (isNull(payRate))
			 throw new Exception("PAY_RATE is required.");
	}
	public void insertChildren()throws Exception{
		if(appointments != null){
			for(Appointment appointment:getAppointments())
				appointment.setNurse((Nurse)this);
		}
		if(licenses != null){
			for(License license:getLicenses())
				license.setNurse((Nurse)this);
		}
		if(appointments != null){
			for(Appointment appointment:getAppointments())
				if(appointment.isNewInstance())
					appointment.insert();
			appointments = null;
		}
		if(licenses != null){
			for(License license:getLicenses())
				if(license.isNewInstance())
					license.insert();
			licenses = null;
		}
	}
}
