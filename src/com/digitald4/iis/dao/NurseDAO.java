package com.digitald4.iis.dao;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Paystub;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** TODO Copy Right*/
/**Description of class, (we need to get this from somewhere, database? xml?)*/
public abstract class NurseDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,REG_DATE,STATUS_ID,ADDRESS,ADDR_UNIT,LATITUDE,LONGITUDE,PHONE_NUMBER,REFERRAL_SOURCE,PAY_FLAT,PAY_RATE,PAY_FLAT_2HR_SOC,PAY_RATE_2HR_SOC,PAY_FLAT_2HR_ROC,PAY_RATE_2HR_ROC,MILEAGE_RATE};
	private Integer id;
	private Date regDate;
	private Integer statusId;
	private String address;
	private String addrUnit;
	private double latitude;
	private double longitude;
	private String phoneNumber;
	private String referralSource;
	private double payFlat;
	private double payRate;
	private double payFlat2HrSoc;
	private double payRate2HrSoc;
	private double payFlat2HrRoc;
	private double payRate2HrRoc;
	private double mileageRate = .5;
	private List<Appointment> appointments;
	private List<License> licenses;
	private List<Paystub> paystubs;
	private GeneralData status;
	private User user;
	public NurseDAO(EntityManager entityManager) {
		super(entityManager);
	}
	public NurseDAO(EntityManager entityManager, Integer id) {
		super(entityManager);
		this.id=id;
	}
	public NurseDAO(EntityManager entityManager, NurseDAO orig) {
		super(entityManager, orig);
		copyFrom(orig);
	}
	public void copyFrom(NurseDAO orig){
		this.regDate = orig.getRegDate();
		this.statusId = orig.getStatusId();
		this.address = orig.getAddress();
		this.addrUnit = orig.getAddrUnit();
		this.latitude = orig.getLatitude();
		this.longitude = orig.getLongitude();
		this.phoneNumber = orig.getPhoneNumber();
		this.referralSource = orig.getReferralSource();
		this.payFlat = orig.getPayFlat();
		this.payRate = orig.getPayRate();
		this.payFlat2HrSoc = orig.getPayFlat2HrSoc();
		this.payRate2HrSoc = orig.getPayRate2HrSoc();
		this.payFlat2HrRoc = orig.getPayFlat2HrRoc();
		this.payRate2HrRoc = orig.getPayRate2HrRoc();
		this.mileageRate = orig.getMileageRate();
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
	public Nurse setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
			user=null;
		}
		return (Nurse)this;
	}
	@Column(name="REG_DATE",nullable=true)
	public Date getRegDate(){
		return regDate;
	}
	public Nurse setRegDate(Date regDate) throws Exception  {
		Date oldValue = getRegDate();
		if (!isSame(regDate, oldValue)) {
			this.regDate = regDate;
			setProperty("REG_DATE", regDate, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="STATUS_ID",nullable=true)
	public Integer getStatusId(){
		return statusId;
	}
	public Nurse setStatusId(Integer statusId) throws Exception  {
		Integer oldValue = getStatusId();
		if (!isSame(statusId, oldValue)) {
			this.statusId = statusId;
			setProperty("STATUS_ID", statusId, oldValue);
			status=null;
		}
		return (Nurse)this;
	}
	@Column(name="ADDRESS",nullable=false,length=100)
	public String getAddress(){
		return address;
	}
	public Nurse setAddress(String address) throws Exception  {
		String oldValue = getAddress();
		if (!isSame(address, oldValue)) {
			this.address = address;
			setProperty("ADDRESS", address, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="ADDR_UNIT",nullable=true,length=20)
	public String getAddrUnit(){
		return addrUnit;
	}
	public Nurse setAddrUnit(String addrUnit) throws Exception  {
		String oldValue = getAddrUnit();
		if (!isSame(addrUnit, oldValue)) {
			this.addrUnit = addrUnit;
			setProperty("ADDR_UNIT", addrUnit, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="LATITUDE",nullable=true)
	public double getLatitude(){
		return latitude;
	}
	public Nurse setLatitude(double latitude) throws Exception  {
		double oldValue = getLatitude();
		if (!isSame(latitude, oldValue)) {
			this.latitude = latitude;
			setProperty("LATITUDE", latitude, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="LONGITUDE",nullable=true)
	public double getLongitude(){
		return longitude;
	}
	public Nurse setLongitude(double longitude) throws Exception  {
		double oldValue = getLongitude();
		if (!isSame(longitude, oldValue)) {
			this.longitude = longitude;
			setProperty("LONGITUDE", longitude, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PHONE_NUMBER",nullable=true,length=20)
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public Nurse setPhoneNumber(String phoneNumber) throws Exception  {
		String oldValue = getPhoneNumber();
		if (!isSame(phoneNumber, oldValue)) {
			this.phoneNumber = phoneNumber;
			setProperty("PHONE_NUMBER", phoneNumber, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="REFERRAL_SOURCE",nullable=true,length=100)
	public String getReferralSource(){
		return referralSource;
	}
	public Nurse setReferralSource(String referralSource) throws Exception  {
		String oldValue = getReferralSource();
		if (!isSame(referralSource, oldValue)) {
			this.referralSource = referralSource;
			setProperty("REFERRAL_SOURCE", referralSource, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_FLAT",nullable=true)
	public double getPayFlat(){
		return payFlat;
	}
	public Nurse setPayFlat(double payFlat) throws Exception  {
		double oldValue = getPayFlat();
		if (!isSame(payFlat, oldValue)) {
			this.payFlat = payFlat;
			setProperty("PAY_FLAT", payFlat, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_RATE",nullable=true)
	public double getPayRate(){
		return payRate;
	}
	public Nurse setPayRate(double payRate) throws Exception  {
		double oldValue = getPayRate();
		if (!isSame(payRate, oldValue)) {
			this.payRate = payRate;
			setProperty("PAY_RATE", payRate, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_FLAT_2HR_SOC",nullable=true)
	public double getPayFlat2HrSoc(){
		return payFlat2HrSoc;
	}
	public Nurse setPayFlat2HrSoc(double payFlat2HrSoc) throws Exception  {
		double oldValue = getPayFlat2HrSoc();
		if (!isSame(payFlat2HrSoc, oldValue)) {
			this.payFlat2HrSoc = payFlat2HrSoc;
			setProperty("PAY_FLAT_2HR_SOC", payFlat2HrSoc, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_RATE_2HR_SOC",nullable=true)
	public double getPayRate2HrSoc(){
		return payRate2HrSoc;
	}
	public Nurse setPayRate2HrSoc(double payRate2HrSoc) throws Exception  {
		double oldValue = getPayRate2HrSoc();
		if (!isSame(payRate2HrSoc, oldValue)) {
			this.payRate2HrSoc = payRate2HrSoc;
			setProperty("PAY_RATE_2HR_SOC", payRate2HrSoc, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_FLAT_2HR_ROC",nullable=true)
	public double getPayFlat2HrRoc(){
		return payFlat2HrRoc;
	}
	public Nurse setPayFlat2HrRoc(double payFlat2HrRoc) throws Exception  {
		double oldValue = getPayFlat2HrRoc();
		if (!isSame(payFlat2HrRoc, oldValue)) {
			this.payFlat2HrRoc = payFlat2HrRoc;
			setProperty("PAY_FLAT_2HR_ROC", payFlat2HrRoc, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="PAY_RATE_2HR_ROC",nullable=true)
	public double getPayRate2HrRoc(){
		return payRate2HrRoc;
	}
	public Nurse setPayRate2HrRoc(double payRate2HrRoc) throws Exception  {
		double oldValue = getPayRate2HrRoc();
		if (!isSame(payRate2HrRoc, oldValue)) {
			this.payRate2HrRoc = payRate2HrRoc;
			setProperty("PAY_RATE_2HR_ROC", payRate2HrRoc, oldValue);
		}
		return (Nurse)this;
	}
	@Column(name="MILEAGE_RATE",nullable=true)
	public double getMileageRate(){
		return mileageRate;
	}
	public Nurse setMileageRate(double mileageRate) throws Exception  {
		double oldValue = getMileageRate();
		if (!isSame(mileageRate, oldValue)) {
			this.mileageRate = mileageRate;
			setProperty("MILEAGE_RATE", mileageRate, oldValue);
		}
		return (Nurse)this;
	}
	public GeneralData getStatus() {
		if (status == null) {
			return getEntityManager().find(GeneralData.class, getStatusId());
		}
		return status;
	}
	public Nurse setStatus(GeneralData status) throws Exception {
		setStatusId(status==null?null:status.getId());
		this.status=status;
		return (Nurse)this;
	}
	public User getUser() {
		if (user == null) {
			return getEntityManager().find(User.class, getId());
		}
		return user;
	}
	public Nurse setUser(User user) throws Exception {
		setId(user==null?null:user.getId());
		this.user=user;
		return (Nurse)this;
	}
	public List<Appointment> getAppointments() {
		if (isNewInstance() || appointments != null) {
			if (appointments == null) {
				appointments = new SortedList<Appointment>();
			}
			return appointments;
		}
		return getNamedCollection(Appointment.class, "findByNurse", getId());
	}
	public Nurse addAppointment(Appointment appointment) throws Exception {
		appointment.setNurse((Nurse)this);
		if(isNewInstance() || appointments != null)
			getAppointments().add(appointment);
		else
			appointment.insert();
		return (Nurse)this;
	}
	public Nurse removeAppointment(Appointment appointment) throws Exception {
		if(isNewInstance() || appointments != null)
			getAppointments().remove(appointment);
		else
			appointment.delete();
		return (Nurse)this;
	}
	public List<License> getLicenses() {
		if (isNewInstance() || licenses != null) {
			if (licenses == null) {
				licenses = new SortedList<License>();
			}
			return licenses;
		}
		return getNamedCollection(License.class, "findByNurse", getId());
	}
	public Nurse addLicense(License license) throws Exception {
		license.setNurse((Nurse)this);
		if(isNewInstance() || licenses != null)
			getLicenses().add(license);
		else
			license.insert();
		return (Nurse)this;
	}
	public Nurse removeLicense(License license) throws Exception {
		if(isNewInstance() || licenses != null)
			getLicenses().remove(license);
		else
			license.delete();
		return (Nurse)this;
	}
	public List<Paystub> getPaystubs() {
		if (isNewInstance() || paystubs != null) {
			if (paystubs == null) {
				paystubs = new SortedList<Paystub>();
			}
			return paystubs;
		}
		return getNamedCollection(Paystub.class, "findByNurse", getId());
	}
	public Nurse addPaystub(Paystub paystub) throws Exception {
		paystub.setNurse((Nurse)this);
		if(isNewInstance() || paystubs != null)
			getPaystubs().add(paystub);
		else
			paystub.insert();
		return (Nurse)this;
	}
	public Nurse removePaystub(Paystub paystub) throws Exception {
		if(isNewInstance() || paystubs != null)
			getPaystubs().remove(paystub);
		else
			paystub.delete();
		return (Nurse)this;
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

	public Nurse setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key, data.get(key).toString());
		return (Nurse)this;
	}

	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case REG_DATE: return getRegDate();
			case STATUS_ID: return getStatusId();
			case ADDRESS: return getAddress();
			case ADDR_UNIT: return getAddrUnit();
			case LATITUDE: return getLatitude();
			case LONGITUDE: return getLongitude();
			case PHONE_NUMBER: return getPhoneNumber();
			case REFERRAL_SOURCE: return getReferralSource();
			case PAY_FLAT: return getPayFlat();
			case PAY_RATE: return getPayRate();
			case PAY_FLAT_2HR_SOC: return getPayFlat2HrSoc();
			case PAY_RATE_2HR_SOC: return getPayRate2HrSoc();
			case PAY_FLAT_2HR_ROC: return getPayFlat2HrRoc();
			case PAY_RATE_2HR_ROC: return getPayRate2HrRoc();
			case MILEAGE_RATE: return getMileageRate();
		}
		return null;
	}

	@Override
	public Nurse setPropertyValue(String property, String value) throws Exception  {
		if(property == null) return (Nurse)this;
		return setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}

	public Nurse setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case REG_DATE:setRegDate(FormatText.parseDate(value)); break;
			case STATUS_ID:setStatusId(Integer.valueOf(value)); break;
			case ADDRESS:setAddress(String.valueOf(value)); break;
			case ADDR_UNIT:setAddrUnit(String.valueOf(value)); break;
			case LATITUDE:setLatitude(Double.valueOf(value)); break;
			case LONGITUDE:setLongitude(Double.valueOf(value)); break;
			case PHONE_NUMBER:setPhoneNumber(String.valueOf(value)); break;
			case REFERRAL_SOURCE:setReferralSource(String.valueOf(value)); break;
			case PAY_FLAT:setPayFlat(Double.valueOf(value)); break;
			case PAY_RATE:setPayRate(Double.valueOf(value)); break;
			case PAY_FLAT_2HR_SOC:setPayFlat2HrSoc(Double.valueOf(value)); break;
			case PAY_RATE_2HR_SOC:setPayRate2HrSoc(Double.valueOf(value)); break;
			case PAY_FLAT_2HR_ROC:setPayFlat2HrRoc(Double.valueOf(value)); break;
			case PAY_RATE_2HR_ROC:setPayRate2HrRoc(Double.valueOf(value)); break;
			case MILEAGE_RATE:setMileageRate(Double.valueOf(value)); break;
		}
		return (Nurse)this;
	}

	public Nurse copy() throws Exception {
		Nurse cp = new Nurse(getEntityManager(), (Nurse)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(NurseDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(Appointment child:getAppointments())
			cp.addAppointment(child.copy());
		for(License child:getLicenses())
			cp.addLicense(child.copy());
		for(Paystub child:getPaystubs())
			cp.addPaystub(child.copy());
	}
	public Vector<String> getDifference(NurseDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getRegDate(),o.getRegDate())) diffs.add("REG_DATE");
		if(!isSame(getStatusId(),o.getStatusId())) diffs.add("STATUS_ID");
		if(!isSame(getAddress(),o.getAddress())) diffs.add("ADDRESS");
		if(!isSame(getAddrUnit(),o.getAddrUnit())) diffs.add("ADDR_UNIT");
		if(!isSame(getLatitude(),o.getLatitude())) diffs.add("LATITUDE");
		if(!isSame(getLongitude(),o.getLongitude())) diffs.add("LONGITUDE");
		if(!isSame(getPhoneNumber(),o.getPhoneNumber())) diffs.add("PHONE_NUMBER");
		if(!isSame(getReferralSource(),o.getReferralSource())) diffs.add("REFERRAL_SOURCE");
		if(!isSame(getPayFlat(),o.getPayFlat())) diffs.add("PAY_FLAT");
		if(!isSame(getPayRate(),o.getPayRate())) diffs.add("PAY_RATE");
		if(!isSame(getPayFlat2HrSoc(),o.getPayFlat2HrSoc())) diffs.add("PAY_FLAT_2HR_SOC");
		if(!isSame(getPayRate2HrSoc(),o.getPayRate2HrSoc())) diffs.add("PAY_RATE_2HR_SOC");
		if(!isSame(getPayFlat2HrRoc(),o.getPayFlat2HrRoc())) diffs.add("PAY_FLAT_2HR_ROC");
		if(!isSame(getPayRate2HrRoc(),o.getPayRate2HrRoc())) diffs.add("PAY_RATE_2HR_ROC");
		if(!isSame(getMileageRate(),o.getMileageRate())) diffs.add("MILEAGE_RATE");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(user != null && user.isNewInstance())
				user.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getAddress()))
			 throw new Exception("ADDRESS is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.setNurse((Nurse)this);
			}
		}
		if (licenses != null) {
			for (License license : getLicenses()) {
				license.setNurse((Nurse)this);
			}
		}
		if (paystubs != null) {
			for (Paystub paystub : getPaystubs()) {
				paystub.setNurse((Nurse)this);
			}
		}
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.insert();
			}
			appointments = null;
		}
		if (licenses != null) {
			for (License license : getLicenses()) {
				license.insert();
			}
			licenses = null;
		}
		if (paystubs != null) {
			for (Paystub paystub : getPaystubs()) {
				paystub.insert();
			}
			paystubs = null;
		}
	}
}