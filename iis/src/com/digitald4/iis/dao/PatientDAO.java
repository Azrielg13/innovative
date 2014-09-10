package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Appointment;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import java.util.Date;
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
public abstract class PatientDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,REFERRAL_DATE,REFERRAL_SOURCE_ID,NAME,MR_NUM,D_O_B,DIANOSIS_ID,THERAPY_TYPE_ID,I_V_ACCESS_ID,PATIENT_STATUS_ID,START_OF_CARE_DATE,SERVICE_ADDRESS,SERVICE_ADDR_UNIT,PHONE_NUMBER,PRIMARY_PHONE_TYPE_ID,ALT_CONTACT_NUMBER,ALT_PHONE_TYPE_ID,EMERGENCY_CONTACT,EMERGENCY_CONTACT_PHONE,EMERGENCY_CONTACT_PHONE_TYPE_ID,LATITUDE,LONGITUDE,BILLING_ID,RX,EST_LAST_DAY_OF_SERVICE,LABS,LABS_FREQUENCY,FIRST_RECERT_DUE,D_C_DATE,INFO_IN_S_O_S,SCHEDULING_PREFERENCE,REFERRAL_NOTE,REFERRAL_RESOLUTION_ID,REFERRAL_RESOLUTION_DATE,REFERRAL_RESOLUTION_NOTE,VENDOR_CONFIRMATION_DATE,NURSE_CONFIRMATION_DATE,PATIENT_CONFIRMATION_DATE,MEDS_DELIVERY_DATE,MEDS_CONFIRMATION_DATE,ACTIVE,BILLING_RATE,BILLING_RATE_2HR_SOC,BILLING_RATE_2HR_ROC,BILLING_FLAT,BILLING_FLAT_2HR_SOC,BILLING_FLAT_2HR_ROC,MILEAGE_RATE,DESCRIPTION};
	private Integer id;
	private Date referralDate;
	private Integer referralSourceId;
	private String name;
	private String mrNum;
	private Date dOB;
	private Integer dianosisId;
	private Integer therapyTypeId;
	private Integer iVAccessId;
	private Integer patientStatusId;
	private Date startOfCareDate;
	private String serviceAddress;
	private String serviceAddrUnit;
	private String phoneNumber;
	private Integer primaryPhoneTypeId;
	private String altContactNumber;
	private Integer altPhoneTypeId;
	private String emergencyContact;
	private String emergencyContactPhone;
	private Integer emergencyContactPhoneTypeId;
	private double latitude;
	private double longitude;
	private Integer billingId;
	private String rx;
	private Date estLastDayOfService;
	private boolean labs;
	private String labsFrequency;
	private Date firstRecertDue;
	private Date dCDate;
	private boolean infoInSOS;
	private String schedulingPreference;
	private String referralNote;
	private Integer referralResolutionId;
	private Date referralResolutionDate;
	private String referralResolutionNote;
	private Date vendorConfirmationDate;
	private Date nurseConfirmationDate;
	private Date patientConfirmationDate;
	private Date medsDeliveryDate;
	private Date medsConfirmationDate;
	private boolean active = true;
	private double billingRate;
	private double billingRate2HrSoc;
	private double billingRate2HrRoc;
	private double billingFlat;
	private double billingFlat2HrSoc;
	private double billingFlat2HrRoc;
	private double mileageRate;
	private String description;
	private List<Appointment> appointments;
	private GeneralData dianosis;
	private GeneralData iVAccess;
	private GeneralData patientState;
	private GeneralData referralResolution;
	private Vendor referralSource;
	private GeneralData therapyType;
	private Vendor vendor;
	public static Patient getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Patient getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Patient o = null;
		if(fetch || cache != null && cache.contains(Patient.class, pk))
			o = em.find(Patient.class, pk);
		return o;
	}
	public static List<Patient> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Patient> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Patient> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Patient o";
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
	public synchronized static List<Patient> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Patient> tq = em.createQuery(jpql,Patient.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Patient> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Patient> tq = em.createNamedQuery(name,Patient.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public PatientDAO(){}
	public PatientDAO(Integer id){
		this.id=id;
	}
	public PatientDAO(PatientDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(PatientDAO orig){
		this.referralDate=orig.getReferralDate();
		this.referralSourceId=orig.getReferralSourceId();
		this.name=orig.getName();
		this.mrNum=orig.getMrNum();
		this.dOB=orig.getDOB();
		this.dianosisId=orig.getDianosisId();
		this.therapyTypeId=orig.getTherapyTypeId();
		this.iVAccessId=orig.getIVAccessId();
		this.patientStatusId=orig.getPatientStatusId();
		this.startOfCareDate=orig.getStartOfCareDate();
		this.serviceAddress=orig.getServiceAddress();
		this.serviceAddrUnit=orig.getServiceAddrUnit();
		this.phoneNumber=orig.getPhoneNumber();
		this.primaryPhoneTypeId=orig.getPrimaryPhoneTypeId();
		this.altContactNumber=orig.getAltContactNumber();
		this.altPhoneTypeId=orig.getAltPhoneTypeId();
		this.emergencyContact=orig.getEmergencyContact();
		this.emergencyContactPhone=orig.getEmergencyContactPhone();
		this.emergencyContactPhoneTypeId=orig.getEmergencyContactPhoneTypeId();
		this.latitude=orig.getLatitude();
		this.longitude=orig.getLongitude();
		this.billingId=orig.getBillingId();
		this.rx=orig.getRx();
		this.estLastDayOfService=orig.getEstLastDayOfService();
		this.labs=orig.isLabs();
		this.labsFrequency=orig.getLabsFrequency();
		this.firstRecertDue=orig.getFirstRecertDue();
		this.dCDate=orig.getDCDate();
		this.infoInSOS=orig.isInfoInSOS();
		this.schedulingPreference=orig.getSchedulingPreference();
		this.referralNote=orig.getReferralNote();
		this.referralResolutionId=orig.getReferralResolutionId();
		this.referralResolutionDate=orig.getReferralResolutionDate();
		this.referralResolutionNote=orig.getReferralResolutionNote();
		this.vendorConfirmationDate=orig.getVendorConfirmationDate();
		this.nurseConfirmationDate=orig.getNurseConfirmationDate();
		this.patientConfirmationDate=orig.getPatientConfirmationDate();
		this.medsDeliveryDate=orig.getMedsDeliveryDate();
		this.medsConfirmationDate=orig.getMedsConfirmationDate();
		this.active=orig.isActive();
		this.billingRate=orig.getBillingRate();
		this.billingRate2HrSoc=orig.getBillingRate2HrSoc();
		this.billingRate2HrRoc=orig.getBillingRate2HrRoc();
		this.billingFlat=orig.getBillingFlat();
		this.billingFlat2HrSoc=orig.getBillingFlat2HrSoc();
		this.billingFlat2HrRoc=orig.getBillingFlat2HrRoc();
		this.mileageRate=orig.getMileageRate();
		this.description=orig.getDescription();
	}
	@Override
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
	public Patient setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_DATE",nullable=true)
	public Date getReferralDate(){
		return referralDate;
	}
	public Patient setReferralDate(Date referralDate) throws Exception  {
		Date oldValue = getReferralDate();
		if (!isSame(referralDate, oldValue)) {
			this.referralDate = referralDate;
			setProperty("REFERRAL_DATE", referralDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_SOURCE_ID",nullable=false)
	public Integer getReferralSourceId(){
		return referralSourceId;
	}
	public Patient setReferralSourceId(Integer referralSourceId) throws Exception  {
		Integer oldValue = getReferralSourceId();
		if (!isSame(referralSourceId, oldValue)) {
			this.referralSourceId = referralSourceId;
			setProperty("REFERRAL_SOURCE_ID", referralSourceId, oldValue);
			referralSource=null;
		}
		return (Patient)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public Patient setName(String name) throws Exception  {
		String oldValue = getName();
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MR_NUM",nullable=true,length=16)
	public String getMrNum(){
		return mrNum;
	}
	public Patient setMrNum(String mrNum) throws Exception  {
		String oldValue = getMrNum();
		if (!isSame(mrNum, oldValue)) {
			this.mrNum = mrNum;
			setProperty("MR_NUM", mrNum, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="D_O_B",nullable=true)
	public Date getDOB(){
		return dOB;
	}
	public Patient setDOB(Date dOB) throws Exception  {
		Date oldValue = getDOB();
		if (!isSame(dOB, oldValue)) {
			this.dOB = dOB;
			setProperty("D_O_B", dOB, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="DIANOSIS_ID",nullable=true)
	public Integer getDianosisId(){
		return dianosisId;
	}
	public Patient setDianosisId(Integer dianosisId) throws Exception  {
		Integer oldValue = getDianosisId();
		if (!isSame(dianosisId, oldValue)) {
			this.dianosisId = dianosisId;
			setProperty("DIANOSIS_ID", dianosisId, oldValue);
			dianosis=null;
		}
		return (Patient)this;
	}
	@Column(name="THERAPY_TYPE_ID",nullable=true)
	public Integer getTherapyTypeId(){
		return therapyTypeId;
	}
	public Patient setTherapyTypeId(Integer therapyTypeId) throws Exception  {
		Integer oldValue = getTherapyTypeId();
		if (!isSame(therapyTypeId, oldValue)) {
			this.therapyTypeId = therapyTypeId;
			setProperty("THERAPY_TYPE_ID", therapyTypeId, oldValue);
			therapyType=null;
		}
		return (Patient)this;
	}
	@Column(name="I_V_ACCESS_ID",nullable=true)
	public Integer getIVAccessId(){
		return iVAccessId;
	}
	public Patient setIVAccessId(Integer iVAccessId) throws Exception  {
		Integer oldValue = getIVAccessId();
		if (!isSame(iVAccessId, oldValue)) {
			this.iVAccessId = iVAccessId;
			setProperty("I_V_ACCESS_ID", iVAccessId, oldValue);
			iVAccess=null;
		}
		return (Patient)this;
	}
	@Column(name="PATIENT_STATUS_ID",nullable=true)
	public Integer getPatientStatusId(){
		return patientStatusId;
	}
	public Patient setPatientStatusId(Integer patientStatusId) throws Exception  {
		Integer oldValue = getPatientStatusId();
		if (!isSame(patientStatusId, oldValue)) {
			this.patientStatusId = patientStatusId;
			setProperty("PATIENT_STATUS_ID", patientStatusId, oldValue);
			patientState=null;
		}
		return (Patient)this;
	}
	@Column(name="START_OF_CARE_DATE",nullable=true)
	public Date getStartOfCareDate(){
		return startOfCareDate;
	}
	public Patient setStartOfCareDate(Date startOfCareDate) throws Exception  {
		Date oldValue = getStartOfCareDate();
		if (!isSame(startOfCareDate, oldValue)) {
			this.startOfCareDate = startOfCareDate;
			setProperty("START_OF_CARE_DATE", startOfCareDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="SERVICE_ADDRESS",nullable=true,length=100)
	public String getServiceAddress(){
		return serviceAddress;
	}
	public Patient setServiceAddress(String serviceAddress) throws Exception  {
		String oldValue = getServiceAddress();
		if (!isSame(serviceAddress, oldValue)) {
			this.serviceAddress = serviceAddress;
			setProperty("SERVICE_ADDRESS", serviceAddress, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="SERVICE_ADDR_UNIT",nullable=true,length=20)
	public String getServiceAddrUnit(){
		return serviceAddrUnit;
	}
	public Patient setServiceAddrUnit(String serviceAddrUnit) throws Exception  {
		String oldValue = getServiceAddrUnit();
		if (!isSame(serviceAddrUnit, oldValue)) {
			this.serviceAddrUnit = serviceAddrUnit;
			setProperty("SERVICE_ADDR_UNIT", serviceAddrUnit, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="PHONE_NUMBER",nullable=true,length=20)
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public Patient setPhoneNumber(String phoneNumber) throws Exception  {
		String oldValue = getPhoneNumber();
		if (!isSame(phoneNumber, oldValue)) {
			this.phoneNumber = phoneNumber;
			setProperty("PHONE_NUMBER", phoneNumber, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="PRIMARY_PHONE_TYPE_ID",nullable=true)
	public Integer getPrimaryPhoneTypeId(){
		return primaryPhoneTypeId;
	}
	public Patient setPrimaryPhoneTypeId(Integer primaryPhoneTypeId) throws Exception  {
		Integer oldValue = getPrimaryPhoneTypeId();
		if (!isSame(primaryPhoneTypeId, oldValue)) {
			this.primaryPhoneTypeId = primaryPhoneTypeId;
			setProperty("PRIMARY_PHONE_TYPE_ID", primaryPhoneTypeId, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="ALT_CONTACT_NUMBER",nullable=true,length=20)
	public String getAltContactNumber(){
		return altContactNumber;
	}
	public Patient setAltContactNumber(String altContactNumber) throws Exception  {
		String oldValue = getAltContactNumber();
		if (!isSame(altContactNumber, oldValue)) {
			this.altContactNumber = altContactNumber;
			setProperty("ALT_CONTACT_NUMBER", altContactNumber, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="ALT_PHONE_TYPE_ID",nullable=true)
	public Integer getAltPhoneTypeId(){
		return altPhoneTypeId;
	}
	public Patient setAltPhoneTypeId(Integer altPhoneTypeId) throws Exception  {
		Integer oldValue = getAltPhoneTypeId();
		if (!isSame(altPhoneTypeId, oldValue)) {
			this.altPhoneTypeId = altPhoneTypeId;
			setProperty("ALT_PHONE_TYPE_ID", altPhoneTypeId, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="EMERGENCY_CONTACT",nullable=true,length=40)
	public String getEmergencyContact(){
		return emergencyContact;
	}
	public Patient setEmergencyContact(String emergencyContact) throws Exception  {
		String oldValue = getEmergencyContact();
		if (!isSame(emergencyContact, oldValue)) {
			this.emergencyContact = emergencyContact;
			setProperty("EMERGENCY_CONTACT", emergencyContact, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="EMERGENCY_CONTACT_PHONE",nullable=true,length=20)
	public String getEmergencyContactPhone(){
		return emergencyContactPhone;
	}
	public Patient setEmergencyContactPhone(String emergencyContactPhone) throws Exception  {
		String oldValue = getEmergencyContactPhone();
		if (!isSame(emergencyContactPhone, oldValue)) {
			this.emergencyContactPhone = emergencyContactPhone;
			setProperty("EMERGENCY_CONTACT_PHONE", emergencyContactPhone, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="EMERGENCY_CONTACT_PHONE_TYPE_ID",nullable=true)
	public Integer getEmergencyContactPhoneTypeId(){
		return emergencyContactPhoneTypeId;
	}
	public Patient setEmergencyContactPhoneTypeId(Integer emergencyContactPhoneTypeId) throws Exception  {
		Integer oldValue = getEmergencyContactPhoneTypeId();
		if (!isSame(emergencyContactPhoneTypeId, oldValue)) {
			this.emergencyContactPhoneTypeId = emergencyContactPhoneTypeId;
			setProperty("EMERGENCY_CONTACT_PHONE_TYPE_ID", emergencyContactPhoneTypeId, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="LATITUDE",nullable=true)
	public double getLatitude(){
		return latitude;
	}
	public Patient setLatitude(double latitude) throws Exception  {
		double oldValue = getLatitude();
		if (!isSame(latitude, oldValue)) {
			this.latitude = latitude;
			setProperty("LATITUDE", latitude, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="LONGITUDE",nullable=true)
	public double getLongitude(){
		return longitude;
	}
	public Patient setLongitude(double longitude) throws Exception  {
		double oldValue = getLongitude();
		if (!isSame(longitude, oldValue)) {
			this.longitude = longitude;
			setProperty("LONGITUDE", longitude, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_ID",nullable=false)
	public Integer getBillingId(){
		return billingId;
	}
	public Patient setBillingId(Integer billingId) throws Exception  {
		Integer oldValue = getBillingId();
		if (!isSame(billingId, oldValue)) {
			this.billingId = billingId;
			setProperty("BILLING_ID", billingId, oldValue);
			vendor=null;
		}
		return (Patient)this;
	}
	@Column(name="RX",nullable=true,length=128)
	public String getRx(){
		return rx;
	}
	public Patient setRx(String rx) throws Exception  {
		String oldValue = getRx();
		if (!isSame(rx, oldValue)) {
			this.rx = rx;
			setProperty("RX", rx, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="EST_LAST_DAY_OF_SERVICE",nullable=true)
	public Date getEstLastDayOfService(){
		return estLastDayOfService;
	}
	public Patient setEstLastDayOfService(Date estLastDayOfService) throws Exception  {
		Date oldValue = getEstLastDayOfService();
		if (!isSame(estLastDayOfService, oldValue)) {
			this.estLastDayOfService = estLastDayOfService;
			setProperty("EST_LAST_DAY_OF_SERVICE", estLastDayOfService, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="LABS",nullable=true)
	public boolean isLabs(){
		return labs;
	}
	public Patient setLabs(boolean labs) throws Exception  {
		boolean oldValue = isLabs();
		if (!isSame(labs, oldValue)) {
			this.labs = labs;
			setProperty("LABS", labs, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="LABS_FREQUENCY",nullable=true,length=64)
	public String getLabsFrequency(){
		return labsFrequency;
	}
	public Patient setLabsFrequency(String labsFrequency) throws Exception  {
		String oldValue = getLabsFrequency();
		if (!isSame(labsFrequency, oldValue)) {
			this.labsFrequency = labsFrequency;
			setProperty("LABS_FREQUENCY", labsFrequency, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="FIRST_RECERT_DUE",nullable=true)
	public Date getFirstRecertDue(){
		return firstRecertDue;
	}
	public Patient setFirstRecertDue(Date firstRecertDue) throws Exception  {
		Date oldValue = getFirstRecertDue();
		if (!isSame(firstRecertDue, oldValue)) {
			this.firstRecertDue = firstRecertDue;
			setProperty("FIRST_RECERT_DUE", firstRecertDue, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="D_C_DATE",nullable=true)
	public Date getDCDate(){
		return dCDate;
	}
	public Patient setDCDate(Date dCDate) throws Exception  {
		Date oldValue = getDCDate();
		if (!isSame(dCDate, oldValue)) {
			this.dCDate = dCDate;
			setProperty("D_C_DATE", dCDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="INFO_IN_S_O_S",nullable=true)
	public boolean isInfoInSOS(){
		return infoInSOS;
	}
	public Patient setInfoInSOS(boolean infoInSOS) throws Exception  {
		boolean oldValue = isInfoInSOS();
		if (!isSame(infoInSOS, oldValue)) {
			this.infoInSOS = infoInSOS;
			setProperty("INFO_IN_S_O_S", infoInSOS, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="SCHEDULING_PREFERENCE",nullable=true,length=64)
	public String getSchedulingPreference(){
		return schedulingPreference;
	}
	public Patient setSchedulingPreference(String schedulingPreference) throws Exception  {
		String oldValue = getSchedulingPreference();
		if (!isSame(schedulingPreference, oldValue)) {
			this.schedulingPreference = schedulingPreference;
			setProperty("SCHEDULING_PREFERENCE", schedulingPreference, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_NOTE",nullable=true,length=1024)
	public String getReferralNote(){
		return referralNote;
	}
	public Patient setReferralNote(String referralNote) throws Exception  {
		String oldValue = getReferralNote();
		if (!isSame(referralNote, oldValue)) {
			this.referralNote = referralNote;
			setProperty("REFERRAL_NOTE", referralNote, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_RESOLUTION_ID",nullable=true)
	public Integer getReferralResolutionId(){
		return referralResolutionId;
	}
	public Patient setReferralResolutionId(Integer referralResolutionId) throws Exception  {
		Integer oldValue = getReferralResolutionId();
		if (!isSame(referralResolutionId, oldValue)) {
			this.referralResolutionId = referralResolutionId;
			setProperty("REFERRAL_RESOLUTION_ID", referralResolutionId, oldValue);
			referralResolution=null;
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_RESOLUTION_DATE",nullable=true)
	public Date getReferralResolutionDate(){
		return referralResolutionDate;
	}
	public Patient setReferralResolutionDate(Date referralResolutionDate) throws Exception  {
		Date oldValue = getReferralResolutionDate();
		if (!isSame(referralResolutionDate, oldValue)) {
			this.referralResolutionDate = referralResolutionDate;
			setProperty("REFERRAL_RESOLUTION_DATE", referralResolutionDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_RESOLUTION_NOTE",nullable=true,length=512)
	public String getReferralResolutionNote(){
		return referralResolutionNote;
	}
	public Patient setReferralResolutionNote(String referralResolutionNote) throws Exception  {
		String oldValue = getReferralResolutionNote();
		if (!isSame(referralResolutionNote, oldValue)) {
			this.referralResolutionNote = referralResolutionNote;
			setProperty("REFERRAL_RESOLUTION_NOTE", referralResolutionNote, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="VENDOR_CONFIRMATION_DATE",nullable=true)
	public Date getVendorConfirmationDate(){
		return vendorConfirmationDate;
	}
	public Patient setVendorConfirmationDate(Date vendorConfirmationDate) throws Exception  {
		Date oldValue = getVendorConfirmationDate();
		if (!isSame(vendorConfirmationDate, oldValue)) {
			this.vendorConfirmationDate = vendorConfirmationDate;
			setProperty("VENDOR_CONFIRMATION_DATE", vendorConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="NURSE_CONFIRMATION_DATE",nullable=true)
	public Date getNurseConfirmationDate(){
		return nurseConfirmationDate;
	}
	public Patient setNurseConfirmationDate(Date nurseConfirmationDate) throws Exception  {
		Date oldValue = getNurseConfirmationDate();
		if (!isSame(nurseConfirmationDate, oldValue)) {
			this.nurseConfirmationDate = nurseConfirmationDate;
			setProperty("NURSE_CONFIRMATION_DATE", nurseConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="PATIENT_CONFIRMATION_DATE",nullable=true)
	public Date getPatientConfirmationDate(){
		return patientConfirmationDate;
	}
	public Patient setPatientConfirmationDate(Date patientConfirmationDate) throws Exception  {
		Date oldValue = getPatientConfirmationDate();
		if (!isSame(patientConfirmationDate, oldValue)) {
			this.patientConfirmationDate = patientConfirmationDate;
			setProperty("PATIENT_CONFIRMATION_DATE", patientConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MEDS_DELIVERY_DATE",nullable=true)
	public Date getMedsDeliveryDate(){
		return medsDeliveryDate;
	}
	public Patient setMedsDeliveryDate(Date medsDeliveryDate) throws Exception  {
		Date oldValue = getMedsDeliveryDate();
		if (!isSame(medsDeliveryDate, oldValue)) {
			this.medsDeliveryDate = medsDeliveryDate;
			setProperty("MEDS_DELIVERY_DATE", medsDeliveryDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MEDS_CONFIRMATION_DATE",nullable=true)
	public Date getMedsConfirmationDate(){
		return medsConfirmationDate;
	}
	public Patient setMedsConfirmationDate(Date medsConfirmationDate) throws Exception  {
		Date oldValue = getMedsConfirmationDate();
		if (!isSame(medsConfirmationDate, oldValue)) {
			this.medsConfirmationDate = medsConfirmationDate;
			setProperty("MEDS_CONFIRMATION_DATE", medsConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public Patient setActive(boolean active) throws Exception  {
		boolean oldValue = isActive();
		if (!isSame(active, oldValue)) {
			this.active = active;
			setProperty("ACTIVE", active, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_RATE",nullable=true)
	public double getBillingRate(){
		return billingRate;
	}
	public Patient setBillingRate(double billingRate) throws Exception  {
		double oldValue = getBillingRate();
		if (!isSame(billingRate, oldValue)) {
			this.billingRate = billingRate;
			setProperty("BILLING_RATE", billingRate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_RATE_2HR_SOC",nullable=true)
	public double getBillingRate2HrSoc(){
		return billingRate2HrSoc;
	}
	public Patient setBillingRate2HrSoc(double billingRate2HrSoc) throws Exception  {
		double oldValue = getBillingRate2HrSoc();
		if (!isSame(billingRate2HrSoc, oldValue)) {
			this.billingRate2HrSoc = billingRate2HrSoc;
			setProperty("BILLING_RATE_2HR_SOC", billingRate2HrSoc, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_RATE_2HR_ROC",nullable=true)
	public double getBillingRate2HrRoc(){
		return billingRate2HrRoc;
	}
	public Patient setBillingRate2HrRoc(double billingRate2HrRoc) throws Exception  {
		double oldValue = getBillingRate2HrRoc();
		if (!isSame(billingRate2HrRoc, oldValue)) {
			this.billingRate2HrRoc = billingRate2HrRoc;
			setProperty("BILLING_RATE_2HR_ROC", billingRate2HrRoc, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_FLAT",nullable=true)
	public double getBillingFlat(){
		return billingFlat;
	}
	public Patient setBillingFlat(double billingFlat) throws Exception  {
		double oldValue = getBillingFlat();
		if (!isSame(billingFlat, oldValue)) {
			this.billingFlat = billingFlat;
			setProperty("BILLING_FLAT", billingFlat, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_FLAT_2HR_SOC",nullable=true)
	public double getBillingFlat2HrSoc(){
		return billingFlat2HrSoc;
	}
	public Patient setBillingFlat2HrSoc(double billingFlat2HrSoc) throws Exception  {
		double oldValue = getBillingFlat2HrSoc();
		if (!isSame(billingFlat2HrSoc, oldValue)) {
			this.billingFlat2HrSoc = billingFlat2HrSoc;
			setProperty("BILLING_FLAT_2HR_SOC", billingFlat2HrSoc, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING_FLAT_2HR_ROC",nullable=true)
	public double getBillingFlat2HrRoc(){
		return billingFlat2HrRoc;
	}
	public Patient setBillingFlat2HrRoc(double billingFlat2HrRoc) throws Exception  {
		double oldValue = getBillingFlat2HrRoc();
		if (!isSame(billingFlat2HrRoc, oldValue)) {
			this.billingFlat2HrRoc = billingFlat2HrRoc;
			setProperty("BILLING_FLAT_2HR_ROC", billingFlat2HrRoc, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MILEAGE_RATE",nullable=true)
	public double getMileageRate(){
		return mileageRate;
	}
	public Patient setMileageRate(double mileageRate) throws Exception  {
		double oldValue = getMileageRate();
		if (!isSame(mileageRate, oldValue)) {
			this.mileageRate = mileageRate;
			setProperty("MILEAGE_RATE", mileageRate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="DESCRIPTION",nullable=true,length=256)
	public String getDescription(){
		return description;
	}
	public Patient setDescription(String description) throws Exception  {
		String oldValue = getDescription();
		if (!isSame(description, oldValue)) {
			this.description = description;
			setProperty("DESCRIPTION", description, oldValue);
		}
		return (Patient)this;
	}
	public GeneralData getDianosis(){
		if(dianosis==null)
			dianosis=GeneralData.getInstance(getDianosisId());
		return dianosis;
	}
	public Patient setDianosis(GeneralData dianosis) throws Exception {
		setDianosisId(dianosis==null?null:dianosis.getId());
		this.dianosis=dianosis;
		return (Patient)this;
	}
	public GeneralData getIVAccess(){
		if(iVAccess==null)
			iVAccess=GeneralData.getInstance(getIVAccessId());
		return iVAccess;
	}
	public Patient setIVAccess(GeneralData iVAccess) throws Exception {
		setIVAccessId(iVAccess==null?null:iVAccess.getId());
		this.iVAccess=iVAccess;
		return (Patient)this;
	}
	public GeneralData getPatientState(){
		if(patientState==null)
			patientState=GeneralData.getInstance(getPatientStatusId());
		return patientState;
	}
	public Patient setPatientState(GeneralData patientState) throws Exception {
		setPatientStatusId(patientState==null?null:patientState.getId());
		this.patientState=patientState;
		return (Patient)this;
	}
	public GeneralData getReferralResolution(){
		if(referralResolution==null)
			referralResolution=GeneralData.getInstance(getReferralResolutionId());
		return referralResolution;
	}
	public Patient setReferralResolution(GeneralData referralResolution) throws Exception {
		setReferralResolutionId(referralResolution==null?null:referralResolution.getId());
		this.referralResolution=referralResolution;
		return (Patient)this;
	}
	public Vendor getReferralSource(){
		if(referralSource==null)
			referralSource=Vendor.getInstance(getReferralSourceId());
		return referralSource;
	}
	public Patient setReferralSource(Vendor referralSource) throws Exception {
		setReferralSourceId(referralSource==null?null:referralSource.getId());
		this.referralSource=referralSource;
		return (Patient)this;
	}
	public GeneralData getTherapyType(){
		if(therapyType==null)
			therapyType=GeneralData.getInstance(getTherapyTypeId());
		return therapyType;
	}
	public Patient setTherapyType(GeneralData therapyType) throws Exception {
		setTherapyTypeId(therapyType==null?null:therapyType.getId());
		this.therapyType=therapyType;
		return (Patient)this;
	}
	public Vendor getVendor(){
		if(vendor==null)
			vendor=Vendor.getInstance(getBillingId());
		return vendor;
	}
	public Patient setVendor(Vendor vendor) throws Exception {
		setBillingId(vendor==null?null:vendor.getId());
		this.vendor=vendor;
		return (Patient)this;
	}
	public List<Appointment> getAppointments(){
		if(isNewInstance() || appointments != null){
			if(appointments == null)
				appointments = new SortedList<Appointment>();
			return appointments;
		}
		return Appointment.getNamedCollection("findByPatient",getId());
	}
	public Patient addAppointment(Appointment appointment) throws Exception {
		appointment.setPatient((Patient)this);
		if(isNewInstance() || appointments != null)
			getAppointments().add(appointment);
		else
			appointment.insert();
		return (Patient)this;
	}
	public Patient removeAppointment(Appointment appointment) throws Exception {
		if(isNewInstance() || appointments != null)
			getAppointments().remove(appointment);
		else
			appointment.delete();
		return (Patient)this;
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
	public void setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key,data.get(key).toString());
	}
	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case REFERRAL_DATE: return getReferralDate();
			case REFERRAL_SOURCE_ID: return getReferralSourceId();
			case NAME: return getName();
			case MR_NUM: return getMrNum();
			case D_O_B: return getDOB();
			case DIANOSIS_ID: return getDianosisId();
			case THERAPY_TYPE_ID: return getTherapyTypeId();
			case I_V_ACCESS_ID: return getIVAccessId();
			case PATIENT_STATUS_ID: return getPatientStatusId();
			case START_OF_CARE_DATE: return getStartOfCareDate();
			case SERVICE_ADDRESS: return getServiceAddress();
			case SERVICE_ADDR_UNIT: return getServiceAddrUnit();
			case PHONE_NUMBER: return getPhoneNumber();
			case PRIMARY_PHONE_TYPE_ID: return getPrimaryPhoneTypeId();
			case ALT_CONTACT_NUMBER: return getAltContactNumber();
			case ALT_PHONE_TYPE_ID: return getAltPhoneTypeId();
			case EMERGENCY_CONTACT: return getEmergencyContact();
			case EMERGENCY_CONTACT_PHONE: return getEmergencyContactPhone();
			case EMERGENCY_CONTACT_PHONE_TYPE_ID: return getEmergencyContactPhoneTypeId();
			case LATITUDE: return getLatitude();
			case LONGITUDE: return getLongitude();
			case BILLING_ID: return getBillingId();
			case RX: return getRx();
			case EST_LAST_DAY_OF_SERVICE: return getEstLastDayOfService();
			case LABS: return isLabs();
			case LABS_FREQUENCY: return getLabsFrequency();
			case FIRST_RECERT_DUE: return getFirstRecertDue();
			case D_C_DATE: return getDCDate();
			case INFO_IN_S_O_S: return isInfoInSOS();
			case SCHEDULING_PREFERENCE: return getSchedulingPreference();
			case REFERRAL_NOTE: return getReferralNote();
			case REFERRAL_RESOLUTION_ID: return getReferralResolutionId();
			case REFERRAL_RESOLUTION_DATE: return getReferralResolutionDate();
			case REFERRAL_RESOLUTION_NOTE: return getReferralResolutionNote();
			case VENDOR_CONFIRMATION_DATE: return getVendorConfirmationDate();
			case NURSE_CONFIRMATION_DATE: return getNurseConfirmationDate();
			case PATIENT_CONFIRMATION_DATE: return getPatientConfirmationDate();
			case MEDS_DELIVERY_DATE: return getMedsDeliveryDate();
			case MEDS_CONFIRMATION_DATE: return getMedsConfirmationDate();
			case ACTIVE: return isActive();
			case BILLING_RATE: return getBillingRate();
			case BILLING_RATE_2HR_SOC: return getBillingRate2HrSoc();
			case BILLING_RATE_2HR_ROC: return getBillingRate2HrRoc();
			case BILLING_FLAT: return getBillingFlat();
			case BILLING_FLAT_2HR_SOC: return getBillingFlat2HrSoc();
			case BILLING_FLAT_2HR_ROC: return getBillingFlat2HrRoc();
			case MILEAGE_RATE: return getMileageRate();
			case DESCRIPTION: return getDescription();
		}
		return null;
	}
	@Override
	public void setPropertyValue(String property, String value) throws Exception  {
		if(property==null)return;
		setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}
	public void setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case REFERRAL_DATE:setReferralDate(FormatText.parseDate(value)); break;
			case REFERRAL_SOURCE_ID:setReferralSourceId(Integer.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case MR_NUM:setMrNum(String.valueOf(value)); break;
			case D_O_B:setDOB(FormatText.parseDate(value)); break;
			case DIANOSIS_ID:setDianosisId(Integer.valueOf(value)); break;
			case THERAPY_TYPE_ID:setTherapyTypeId(Integer.valueOf(value)); break;
			case I_V_ACCESS_ID:setIVAccessId(Integer.valueOf(value)); break;
			case PATIENT_STATUS_ID:setPatientStatusId(Integer.valueOf(value)); break;
			case START_OF_CARE_DATE:setStartOfCareDate(FormatText.parseDate(value)); break;
			case SERVICE_ADDRESS:setServiceAddress(String.valueOf(value)); break;
			case SERVICE_ADDR_UNIT:setServiceAddrUnit(String.valueOf(value)); break;
			case PHONE_NUMBER:setPhoneNumber(String.valueOf(value)); break;
			case PRIMARY_PHONE_TYPE_ID:setPrimaryPhoneTypeId(Integer.valueOf(value)); break;
			case ALT_CONTACT_NUMBER:setAltContactNumber(String.valueOf(value)); break;
			case ALT_PHONE_TYPE_ID:setAltPhoneTypeId(Integer.valueOf(value)); break;
			case EMERGENCY_CONTACT:setEmergencyContact(String.valueOf(value)); break;
			case EMERGENCY_CONTACT_PHONE:setEmergencyContactPhone(String.valueOf(value)); break;
			case EMERGENCY_CONTACT_PHONE_TYPE_ID:setEmergencyContactPhoneTypeId(Integer.valueOf(value)); break;
			case LATITUDE:setLatitude(Double.valueOf(value)); break;
			case LONGITUDE:setLongitude(Double.valueOf(value)); break;
			case BILLING_ID:setBillingId(Integer.valueOf(value)); break;
			case RX:setRx(String.valueOf(value)); break;
			case EST_LAST_DAY_OF_SERVICE:setEstLastDayOfService(FormatText.parseDate(value)); break;
			case LABS:setLabs(Boolean.valueOf(value)); break;
			case LABS_FREQUENCY:setLabsFrequency(String.valueOf(value)); break;
			case FIRST_RECERT_DUE:setFirstRecertDue(FormatText.parseDate(value)); break;
			case D_C_DATE:setDCDate(FormatText.parseDate(value)); break;
			case INFO_IN_S_O_S:setInfoInSOS(Boolean.valueOf(value)); break;
			case SCHEDULING_PREFERENCE:setSchedulingPreference(String.valueOf(value)); break;
			case REFERRAL_NOTE:setReferralNote(String.valueOf(value)); break;
			case REFERRAL_RESOLUTION_ID:setReferralResolutionId(Integer.valueOf(value)); break;
			case REFERRAL_RESOLUTION_DATE:setReferralResolutionDate(FormatText.parseDate(value)); break;
			case REFERRAL_RESOLUTION_NOTE:setReferralResolutionNote(String.valueOf(value)); break;
			case VENDOR_CONFIRMATION_DATE:setVendorConfirmationDate(FormatText.parseDate(value)); break;
			case NURSE_CONFIRMATION_DATE:setNurseConfirmationDate(FormatText.parseDate(value)); break;
			case PATIENT_CONFIRMATION_DATE:setPatientConfirmationDate(FormatText.parseDate(value)); break;
			case MEDS_DELIVERY_DATE:setMedsDeliveryDate(FormatText.parseDate(value)); break;
			case MEDS_CONFIRMATION_DATE:setMedsConfirmationDate(FormatText.parseDate(value)); break;
			case ACTIVE:setActive(Boolean.valueOf(value)); break;
			case BILLING_RATE:setBillingRate(Double.valueOf(value)); break;
			case BILLING_RATE_2HR_SOC:setBillingRate2HrSoc(Double.valueOf(value)); break;
			case BILLING_RATE_2HR_ROC:setBillingRate2HrRoc(Double.valueOf(value)); break;
			case BILLING_FLAT:setBillingFlat(Double.valueOf(value)); break;
			case BILLING_FLAT_2HR_SOC:setBillingFlat2HrSoc(Double.valueOf(value)); break;
			case BILLING_FLAT_2HR_ROC:setBillingFlat2HrRoc(Double.valueOf(value)); break;
			case MILEAGE_RATE:setMileageRate(Double.valueOf(value)); break;
			case DESCRIPTION:setDescription(String.valueOf(value)); break;
		}
	}
	public Patient copy() throws Exception {
		Patient cp = new Patient((Patient)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(PatientDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(Appointment child:getAppointments())
			cp.addAppointment(child.copy());
	}
	public Vector<String> getDifference(PatientDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getReferralDate(),o.getReferralDate())) diffs.add("REFERRAL_DATE");
		if(!isSame(getReferralSourceId(),o.getReferralSourceId())) diffs.add("REFERRAL_SOURCE_ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getMrNum(),o.getMrNum())) diffs.add("MR_NUM");
		if(!isSame(getDOB(),o.getDOB())) diffs.add("D_O_B");
		if(!isSame(getDianosisId(),o.getDianosisId())) diffs.add("DIANOSIS_ID");
		if(!isSame(getTherapyTypeId(),o.getTherapyTypeId())) diffs.add("THERAPY_TYPE_ID");
		if(!isSame(getIVAccessId(),o.getIVAccessId())) diffs.add("I_V_ACCESS_ID");
		if(!isSame(getPatientStatusId(),o.getPatientStatusId())) diffs.add("PATIENT_STATUS_ID");
		if(!isSame(getStartOfCareDate(),o.getStartOfCareDate())) diffs.add("START_OF_CARE_DATE");
		if(!isSame(getServiceAddress(),o.getServiceAddress())) diffs.add("SERVICE_ADDRESS");
		if(!isSame(getServiceAddrUnit(),o.getServiceAddrUnit())) diffs.add("SERVICE_ADDR_UNIT");
		if(!isSame(getPhoneNumber(),o.getPhoneNumber())) diffs.add("PHONE_NUMBER");
		if(!isSame(getPrimaryPhoneTypeId(),o.getPrimaryPhoneTypeId())) diffs.add("PRIMARY_PHONE_TYPE_ID");
		if(!isSame(getAltContactNumber(),o.getAltContactNumber())) diffs.add("ALT_CONTACT_NUMBER");
		if(!isSame(getAltPhoneTypeId(),o.getAltPhoneTypeId())) diffs.add("ALT_PHONE_TYPE_ID");
		if(!isSame(getEmergencyContact(),o.getEmergencyContact())) diffs.add("EMERGENCY_CONTACT");
		if(!isSame(getEmergencyContactPhone(),o.getEmergencyContactPhone())) diffs.add("EMERGENCY_CONTACT_PHONE");
		if(!isSame(getEmergencyContactPhoneTypeId(),o.getEmergencyContactPhoneTypeId())) diffs.add("EMERGENCY_CONTACT_PHONE_TYPE_ID");
		if(!isSame(getLatitude(),o.getLatitude())) diffs.add("LATITUDE");
		if(!isSame(getLongitude(),o.getLongitude())) diffs.add("LONGITUDE");
		if(!isSame(getBillingId(),o.getBillingId())) diffs.add("BILLING_ID");
		if(!isSame(getRx(),o.getRx())) diffs.add("RX");
		if(!isSame(getEstLastDayOfService(),o.getEstLastDayOfService())) diffs.add("EST_LAST_DAY_OF_SERVICE");
		if(!isSame(isLabs(),o.isLabs())) diffs.add("LABS");
		if(!isSame(getLabsFrequency(),o.getLabsFrequency())) diffs.add("LABS_FREQUENCY");
		if(!isSame(getFirstRecertDue(),o.getFirstRecertDue())) diffs.add("FIRST_RECERT_DUE");
		if(!isSame(getDCDate(),o.getDCDate())) diffs.add("D_C_DATE");
		if(!isSame(isInfoInSOS(),o.isInfoInSOS())) diffs.add("INFO_IN_S_O_S");
		if(!isSame(getSchedulingPreference(),o.getSchedulingPreference())) diffs.add("SCHEDULING_PREFERENCE");
		if(!isSame(getReferralNote(),o.getReferralNote())) diffs.add("REFERRAL_NOTE");
		if(!isSame(getReferralResolutionId(),o.getReferralResolutionId())) diffs.add("REFERRAL_RESOLUTION_ID");
		if(!isSame(getReferralResolutionDate(),o.getReferralResolutionDate())) diffs.add("REFERRAL_RESOLUTION_DATE");
		if(!isSame(getReferralResolutionNote(),o.getReferralResolutionNote())) diffs.add("REFERRAL_RESOLUTION_NOTE");
		if(!isSame(getVendorConfirmationDate(),o.getVendorConfirmationDate())) diffs.add("VENDOR_CONFIRMATION_DATE");
		if(!isSame(getNurseConfirmationDate(),o.getNurseConfirmationDate())) diffs.add("NURSE_CONFIRMATION_DATE");
		if(!isSame(getPatientConfirmationDate(),o.getPatientConfirmationDate())) diffs.add("PATIENT_CONFIRMATION_DATE");
		if(!isSame(getMedsDeliveryDate(),o.getMedsDeliveryDate())) diffs.add("MEDS_DELIVERY_DATE");
		if(!isSame(getMedsConfirmationDate(),o.getMedsConfirmationDate())) diffs.add("MEDS_CONFIRMATION_DATE");
		if(!isSame(isActive(),o.isActive())) diffs.add("ACTIVE");
		if(!isSame(getBillingRate(),o.getBillingRate())) diffs.add("BILLING_RATE");
		if(!isSame(getBillingRate2HrSoc(),o.getBillingRate2HrSoc())) diffs.add("BILLING_RATE_2HR_SOC");
		if(!isSame(getBillingRate2HrRoc(),o.getBillingRate2HrRoc())) diffs.add("BILLING_RATE_2HR_ROC");
		if(!isSame(getBillingFlat(),o.getBillingFlat())) diffs.add("BILLING_FLAT");
		if(!isSame(getBillingFlat2HrSoc(),o.getBillingFlat2HrSoc())) diffs.add("BILLING_FLAT_2HR_SOC");
		if(!isSame(getBillingFlat2HrRoc(),o.getBillingFlat2HrRoc())) diffs.add("BILLING_FLAT_2HR_ROC");
		if(!isSame(getMileageRate(),o.getMileageRate())) diffs.add("MILEAGE_RATE");
		if(!isSame(getDescription(),o.getDescription())) diffs.add("DESCRIPTION");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(vendor != null && vendor.isNewInstance())
				vendor.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getReferralSourceId()))
			 throw new Exception("REFERRAL_SOURCE_ID is required.");
		if (isNull(getName()))
			 throw new Exception("NAME is required.");
		if (isNull(getBillingId()))
			 throw new Exception("BILLING_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.setPatient((Patient)this);
			}
		}
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.insert();
			}
			appointments = null;
		}
	}
}
