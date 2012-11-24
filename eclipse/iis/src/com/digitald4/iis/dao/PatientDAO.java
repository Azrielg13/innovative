package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import java.sql.Date;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
public abstract class PatientDAO extends DataAccessObject{
	public static enum KEY_PROPERTY{ID};
	public static enum PROPERTY{ID,REFERRAL_DATE,REFERRAL_SOURCE,FIRST_NAME,LAST_NAME,NURSE_ID,MR_NUM,DIANOSIS,THERAPY_TYPE,IV_ACCESS,START_OF_CARE,SSTART_OF_CARE_DATE,SERVICE_ADDRESS,BILLING,RX,EST_LAST_DAY_OF_SERVICE,LABS,LABS_FREQUENCY,FIRST_RECERT_DUE,D_C_DATE,INFO_IN_S_O_S,SCHEDULING_PREFERENCE,REFERRAL_NOTE,REFERRAL_RESOLUTION_ID,REFERRAL_RESOLUTION_DATE,REFERRAL_RESOLUTION_NOTE,VENDOR_CONFIRMATION_DATE,NURSE_CONFIRMATION_DATE,PATIENT_CONFIRMATION_DATE,MEDS_DELIVERY_DATE,MEDS_CONFIRMATION_DATE,ACTIVE,DESCRIPTION};
	private Integer id;
	private Date referralDate;
	private String referralSource;
	private String firstName;
	private String lastName;
	private Integer nurseId;
	private String mrNum;
	private String dianosis;
	private String therapyType;
	private String ivAccess;
	private boolean startOfCare;
	private Date sstartOfCareDate;
	private String serviceAddress;
	private String billing;
	private String rx;
	private Date estLastDayOfService;
	private boolean labs;
	private String labsFrequency;
	private Date firstRecertDue;
	private String dCDate;
	private String infoInSOS;
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
	private String description;
	private Nurse nurse;
	public static Patient getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Patient getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Patient o = null;
		if(cache != null && cache.contains(Patient.class, pk))
			o = em.find(Patient.class, pk);
		if(o==null && fetch)
			o = em.find(Patient.class, pk);
		return o;
	}
	public static Collection<Patient> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<Patient> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<Patient> getCollection(String[] props, Object... values){
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
	public synchronized static Collection<Patient> getCollection(String jpql, Object... values){
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
	public synchronized static Collection<Patient> getNamedCollection(String name, Object... values){
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
		this.referralSource=orig.getReferralSource();
		this.firstName=orig.getFirstName();
		this.lastName=orig.getLastName();
		this.nurseId=orig.getNurseId();
		this.mrNum=orig.getMrNum();
		this.dianosis=orig.getDianosis();
		this.therapyType=orig.getTherapyType();
		this.ivAccess=orig.getIvAccess();
		this.startOfCare=orig.isStartOfCare();
		this.sstartOfCareDate=orig.getSstartOfCareDate();
		this.serviceAddress=orig.getServiceAddress();
		this.billing=orig.getBilling();
		this.rx=orig.getRx();
		this.estLastDayOfService=orig.getEstLastDayOfService();
		this.labs=orig.isLabs();
		this.labsFrequency=orig.getLabsFrequency();
		this.firstRecertDue=orig.getFirstRecertDue();
		this.dCDate=orig.getDCDate();
		this.infoInSOS=orig.getInfoInSOS();
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
		this.description=orig.getDescription();
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
	@SequenceGenerator(name="SEQ",sequenceName="SEQ")
	@Column(name="ID",nullable=false)
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		if(isSame(id, getId()))return;
		Integer oldValue = getId();
		this.id=id;
		setProperty("ID", id, oldValue);
	}
	@Column(name="REFERRAL_DATE",nullable=true)
	public Date getReferralDate(){
		return referralDate;
	}
	public void setReferralDate(Date referralDate){
		if(isSame(referralDate, getReferralDate()))return;
		Date oldValue = getReferralDate();
		this.referralDate=referralDate;
		setProperty("REFERRAL_DATE", referralDate, oldValue);
	}
	@Column(name="REFERRAL_SOURCE",nullable=false,length=64)
	public String getReferralSource(){
		return referralSource;
	}
	public void setReferralSource(String referralSource){
		if(isSame(referralSource, getReferralSource()))return;
		String oldValue = getReferralSource();
		this.referralSource=referralSource;
		setProperty("REFERRAL_SOURCE", referralSource, oldValue);
	}
	@Column(name="FIRST_NAME",nullable=false,length=32)
	public String getFirstName(){
		return firstName;
	}
	public void setFirstName(String firstName){
		if(isSame(firstName, getFirstName()))return;
		String oldValue = getFirstName();
		this.firstName=firstName;
		setProperty("FIRST_NAME", firstName, oldValue);
	}
	@Column(name="LAST_NAME",nullable=false,length=32)
	public String getLastName(){
		return lastName;
	}
	public void setLastName(String lastName){
		if(isSame(lastName, getLastName()))return;
		String oldValue = getLastName();
		this.lastName=lastName;
		setProperty("LAST_NAME", lastName, oldValue);
	}
	@Column(name="NURSE_ID",nullable=true)
	public Integer getNurseId(){
		return nurseId;
	}
	public void setNurseId(Integer nurseId){
		if(isSame(nurseId, getNurseId()))return;
		Integer oldValue = getNurseId();
		this.nurseId=nurseId;
		setProperty("NURSE_ID", nurseId, oldValue);
		nurse=null;
	}
	@Column(name="MR_NUM",nullable=true,length=16)
	public String getMrNum(){
		return mrNum;
	}
	public void setMrNum(String mrNum){
		if(isSame(mrNum, getMrNum()))return;
		String oldValue = getMrNum();
		this.mrNum=mrNum;
		setProperty("MR_NUM", mrNum, oldValue);
	}
	@Column(name="DIANOSIS",nullable=true,length=64)
	public String getDianosis(){
		return dianosis;
	}
	public void setDianosis(String dianosis){
		if(isSame(dianosis, getDianosis()))return;
		String oldValue = getDianosis();
		this.dianosis=dianosis;
		setProperty("DIANOSIS", dianosis, oldValue);
	}
	@Column(name="THERAPY_TYPE",nullable=true,length=64)
	public String getTherapyType(){
		return therapyType;
	}
	public void setTherapyType(String therapyType){
		if(isSame(therapyType, getTherapyType()))return;
		String oldValue = getTherapyType();
		this.therapyType=therapyType;
		setProperty("THERAPY_TYPE", therapyType, oldValue);
	}
	@Column(name="IV_ACCESS",nullable=true,length=64)
	public String getIvAccess(){
		return ivAccess;
	}
	public void setIvAccess(String ivAccess){
		if(isSame(ivAccess, getIvAccess()))return;
		String oldValue = getIvAccess();
		this.ivAccess=ivAccess;
		setProperty("IV_ACCESS", ivAccess, oldValue);
	}
	@Column(name="START_OF_CARE",nullable=true)
	public boolean isStartOfCare(){
		return startOfCare;
	}
	public void setStartOfCare(boolean startOfCare){
		if(isSame(startOfCare, isStartOfCare()))return;
		boolean oldValue = isStartOfCare();
		this.startOfCare=startOfCare;
		setProperty("START_OF_CARE", startOfCare, oldValue);
	}
	@Column(name="SSTART_OF_CARE_DATE",nullable=true)
	public Date getSstartOfCareDate(){
		return sstartOfCareDate;
	}
	public void setSstartOfCareDate(Date sstartOfCareDate){
		if(isSame(sstartOfCareDate, getSstartOfCareDate()))return;
		Date oldValue = getSstartOfCareDate();
		this.sstartOfCareDate=sstartOfCareDate;
		setProperty("SSTART_OF_CARE_DATE", sstartOfCareDate, oldValue);
	}
	@Column(name="SERVICE_ADDRESS",nullable=true,length=50)
	public String getServiceAddress(){
		return serviceAddress;
	}
	public void setServiceAddress(String serviceAddress){
		if(isSame(serviceAddress, getServiceAddress()))return;
		String oldValue = getServiceAddress();
		this.serviceAddress=serviceAddress;
		setProperty("SERVICE_ADDRESS", serviceAddress, oldValue);
	}
	@Column(name="BILLING",nullable=true,length=64)
	public String getBilling(){
		return billing;
	}
	public void setBilling(String billing){
		if(isSame(billing, getBilling()))return;
		String oldValue = getBilling();
		this.billing=billing;
		setProperty("BILLING", billing, oldValue);
	}
	@Column(name="RX",nullable=true,length=64)
	public String getRx(){
		return rx;
	}
	public void setRx(String rx){
		if(isSame(rx, getRx()))return;
		String oldValue = getRx();
		this.rx=rx;
		setProperty("RX", rx, oldValue);
	}
	@Column(name="EST_LAST_DAY_OF_SERVICE",nullable=true)
	public Date getEstLastDayOfService(){
		return estLastDayOfService;
	}
	public void setEstLastDayOfService(Date estLastDayOfService){
		if(isSame(estLastDayOfService, getEstLastDayOfService()))return;
		Date oldValue = getEstLastDayOfService();
		this.estLastDayOfService=estLastDayOfService;
		setProperty("EST_LAST_DAY_OF_SERVICE", estLastDayOfService, oldValue);
	}
	@Column(name="LABS",nullable=true)
	public boolean isLabs(){
		return labs;
	}
	public void setLabs(boolean labs){
		if(isSame(labs, isLabs()))return;
		boolean oldValue = isLabs();
		this.labs=labs;
		setProperty("LABS", labs, oldValue);
	}
	@Column(name="LABS_FREQUENCY",nullable=true,length=64)
	public String getLabsFrequency(){
		return labsFrequency;
	}
	public void setLabsFrequency(String labsFrequency){
		if(isSame(labsFrequency, getLabsFrequency()))return;
		String oldValue = getLabsFrequency();
		this.labsFrequency=labsFrequency;
		setProperty("LABS_FREQUENCY", labsFrequency, oldValue);
	}
	@Column(name="FIRST_RECERT_DUE",nullable=true)
	public Date getFirstRecertDue(){
		return firstRecertDue;
	}
	public void setFirstRecertDue(Date firstRecertDue){
		if(isSame(firstRecertDue, getFirstRecertDue()))return;
		Date oldValue = getFirstRecertDue();
		this.firstRecertDue=firstRecertDue;
		setProperty("FIRST_RECERT_DUE", firstRecertDue, oldValue);
	}
	@Column(name="D_C_DATE",nullable=true,length=64)
	public String getDCDate(){
		return dCDate;
	}
	public void setDCDate(String dCDate){
		if(isSame(dCDate, getDCDate()))return;
		String oldValue = getDCDate();
		this.dCDate=dCDate;
		setProperty("D_C_DATE", dCDate, oldValue);
	}
	@Column(name="INFO_IN_S_O_S",nullable=true,length=64)
	public String getInfoInSOS(){
		return infoInSOS;
	}
	public void setInfoInSOS(String infoInSOS){
		if(isSame(infoInSOS, getInfoInSOS()))return;
		String oldValue = getInfoInSOS();
		this.infoInSOS=infoInSOS;
		setProperty("INFO_IN_S_O_S", infoInSOS, oldValue);
	}
	@Column(name="SCHEDULING_PREFERENCE",nullable=true,length=64)
	public String getSchedulingPreference(){
		return schedulingPreference;
	}
	public void setSchedulingPreference(String schedulingPreference){
		if(isSame(schedulingPreference, getSchedulingPreference()))return;
		String oldValue = getSchedulingPreference();
		this.schedulingPreference=schedulingPreference;
		setProperty("SCHEDULING_PREFERENCE", schedulingPreference, oldValue);
	}
	@Column(name="REFERRAL_NOTE",nullable=true,length=1024)
	public String getReferralNote(){
		return referralNote;
	}
	public void setReferralNote(String referralNote){
		if(isSame(referralNote, getReferralNote()))return;
		String oldValue = getReferralNote();
		this.referralNote=referralNote;
		setProperty("REFERRAL_NOTE", referralNote, oldValue);
	}
	@Column(name="REFERRAL_RESOLUTION_ID",nullable=true)
	public Integer getReferralResolutionId(){
		return referralResolutionId;
	}
	public void setReferralResolutionId(Integer referralResolutionId){
		if(isSame(referralResolutionId, getReferralResolutionId()))return;
		Integer oldValue = getReferralResolutionId();
		this.referralResolutionId=referralResolutionId;
		setProperty("REFERRAL_RESOLUTION_ID", referralResolutionId, oldValue);
	}
	@Column(name="REFERRAL_RESOLUTION_DATE",nullable=true)
	public Date getReferralResolutionDate(){
		return referralResolutionDate;
	}
	public void setReferralResolutionDate(Date referralResolutionDate){
		if(isSame(referralResolutionDate, getReferralResolutionDate()))return;
		Date oldValue = getReferralResolutionDate();
		this.referralResolutionDate=referralResolutionDate;
		setProperty("REFERRAL_RESOLUTION_DATE", referralResolutionDate, oldValue);
	}
	@Column(name="REFERRAL_RESOLUTION_NOTE",nullable=true,length=512)
	public String getReferralResolutionNote(){
		return referralResolutionNote;
	}
	public void setReferralResolutionNote(String referralResolutionNote){
		if(isSame(referralResolutionNote, getReferralResolutionNote()))return;
		String oldValue = getReferralResolutionNote();
		this.referralResolutionNote=referralResolutionNote;
		setProperty("REFERRAL_RESOLUTION_NOTE", referralResolutionNote, oldValue);
	}
	@Column(name="VENDOR_CONFIRMATION_DATE",nullable=true)
	public Date getVendorConfirmationDate(){
		return vendorConfirmationDate;
	}
	public void setVendorConfirmationDate(Date vendorConfirmationDate){
		if(isSame(vendorConfirmationDate, getVendorConfirmationDate()))return;
		Date oldValue = getVendorConfirmationDate();
		this.vendorConfirmationDate=vendorConfirmationDate;
		setProperty("VENDOR_CONFIRMATION_DATE", vendorConfirmationDate, oldValue);
	}
	@Column(name="NURSE_CONFIRMATION_DATE",nullable=true)
	public Date getNurseConfirmationDate(){
		return nurseConfirmationDate;
	}
	public void setNurseConfirmationDate(Date nurseConfirmationDate){
		if(isSame(nurseConfirmationDate, getNurseConfirmationDate()))return;
		Date oldValue = getNurseConfirmationDate();
		this.nurseConfirmationDate=nurseConfirmationDate;
		setProperty("NURSE_CONFIRMATION_DATE", nurseConfirmationDate, oldValue);
	}
	@Column(name="PATIENT_CONFIRMATION_DATE",nullable=true)
	public Date getPatientConfirmationDate(){
		return patientConfirmationDate;
	}
	public void setPatientConfirmationDate(Date patientConfirmationDate){
		if(isSame(patientConfirmationDate, getPatientConfirmationDate()))return;
		Date oldValue = getPatientConfirmationDate();
		this.patientConfirmationDate=patientConfirmationDate;
		setProperty("PATIENT_CONFIRMATION_DATE", patientConfirmationDate, oldValue);
	}
	@Column(name="MEDS_DELIVERY_DATE",nullable=true)
	public Date getMedsDeliveryDate(){
		return medsDeliveryDate;
	}
	public void setMedsDeliveryDate(Date medsDeliveryDate){
		if(isSame(medsDeliveryDate, getMedsDeliveryDate()))return;
		Date oldValue = getMedsDeliveryDate();
		this.medsDeliveryDate=medsDeliveryDate;
		setProperty("MEDS_DELIVERY_DATE", medsDeliveryDate, oldValue);
	}
	@Column(name="MEDS_CONFIRMATION_DATE",nullable=true)
	public Date getMedsConfirmationDate(){
		return medsConfirmationDate;
	}
	public void setMedsConfirmationDate(Date medsConfirmationDate){
		if(isSame(medsConfirmationDate, getMedsConfirmationDate()))return;
		Date oldValue = getMedsConfirmationDate();
		this.medsConfirmationDate=medsConfirmationDate;
		setProperty("MEDS_CONFIRMATION_DATE", medsConfirmationDate, oldValue);
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public void setActive(boolean active){
		if(isSame(active, isActive()))return;
		boolean oldValue = isActive();
		this.active=active;
		setProperty("ACTIVE", active, oldValue);
	}
	@Column(name="DESCRIPTION",nullable=true,length=256)
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		if(isSame(description, getDescription()))return;
		String oldValue = getDescription();
		this.description=description;
		setProperty("DESCRIPTION", description, oldValue);
	}
	public Nurse getNurse(){
		if(nurse==null)
			nurse=Nurse.getInstance(getNurseId());
		return nurse;
	}
	public void setNurse(Nurse nurse){
		setNurseId(nurse==null?0:nurse.getId());
		this.nurse=nurse;
	}
	public Patient copy(){
		Patient cp = new Patient((Patient)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(PatientDAO cp){
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(PatientDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getReferralDate(),o.getReferralDate())) diffs.add("REFERRAL_DATE");
		if(!isSame(getReferralSource(),o.getReferralSource())) diffs.add("REFERRAL_SOURCE");
		if(!isSame(getFirstName(),o.getFirstName())) diffs.add("FIRST_NAME");
		if(!isSame(getLastName(),o.getLastName())) diffs.add("LAST_NAME");
		if(!isSame(getNurseId(),o.getNurseId())) diffs.add("NURSE_ID");
		if(!isSame(getMrNum(),o.getMrNum())) diffs.add("MR_NUM");
		if(!isSame(getDianosis(),o.getDianosis())) diffs.add("DIANOSIS");
		if(!isSame(getTherapyType(),o.getTherapyType())) diffs.add("THERAPY_TYPE");
		if(!isSame(getIvAccess(),o.getIvAccess())) diffs.add("IV_ACCESS");
		if(!isSame(isStartOfCare(),o.isStartOfCare())) diffs.add("START_OF_CARE");
		if(!isSame(getSstartOfCareDate(),o.getSstartOfCareDate())) diffs.add("SSTART_OF_CARE_DATE");
		if(!isSame(getServiceAddress(),o.getServiceAddress())) diffs.add("SERVICE_ADDRESS");
		if(!isSame(getBilling(),o.getBilling())) diffs.add("BILLING");
		if(!isSame(getRx(),o.getRx())) diffs.add("RX");
		if(!isSame(getEstLastDayOfService(),o.getEstLastDayOfService())) diffs.add("EST_LAST_DAY_OF_SERVICE");
		if(!isSame(isLabs(),o.isLabs())) diffs.add("LABS");
		if(!isSame(getLabsFrequency(),o.getLabsFrequency())) diffs.add("LABS_FREQUENCY");
		if(!isSame(getFirstRecertDue(),o.getFirstRecertDue())) diffs.add("FIRST_RECERT_DUE");
		if(!isSame(getDCDate(),o.getDCDate())) diffs.add("D_C_DATE");
		if(!isSame(getInfoInSOS(),o.getInfoInSOS())) diffs.add("INFO_IN_S_O_S");
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
		if(!isSame(getDescription(),o.getDescription())) diffs.add("DESCRIPTION");
		return diffs;
	}
	public void insertParents(){
		if(nurse != null && nurse.isNewInstance())
				nurse.insert();
	}
	public void insertChildren(){
	}
}
