package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Patient;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
public abstract class PatientDAO extends DataAccessObject{
	public static enum KEY_PROPERTY{ID};
	public static enum PROPERTY{ID,REFERRAL_DATE,REFERRAL_SOURCE,NAME,MR_NUM,DIANOSIS,THERAPY_TYPE,IV_ACCESS,START_OF_CARE,START_OF_CARE_DATE,SERVICE_ADDRESS,BILLING,RX,EST_LAST_DAY_OF_SERVICE,LABS,LABS_FREQUENCY,FIRST_RECERT_DUE,D_C_DATE,INFO_IN_S_O_S,SCHEDULING_PREFERENCE,REFERRAL_NOTE,REFERRAL_RESOLUTION_ID,REFERRAL_RESOLUTION_DATE,REFERRAL_RESOLUTION_NOTE,VENDOR_CONFIRMATION_DATE,NURSE_CONFIRMATION_DATE,PATIENT_CONFIRMATION_DATE,MEDS_DELIVERY_DATE,MEDS_CONFIRMATION_DATE,ACTIVE,DESCRIPTION};
	private Integer id;
	private Date referralDate;
	private String referralSource;
	private String name;
	private String mrNum;
	private String dianosis;
	private String therapyType;
	private String ivAccess;
	private boolean startOfCare;
	private Date startOfCareDate;
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
	private GeneralData referralResolution;
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
		this.name=orig.getName();
		this.mrNum=orig.getMrNum();
		this.dianosis=orig.getDianosis();
		this.therapyType=orig.getTherapyType();
		this.ivAccess=orig.getIvAccess();
		this.startOfCare=orig.isStartOfCare();
		this.startOfCareDate=orig.getStartOfCareDate();
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
	@GeneratedValue
	@Column(name="ID",nullable=false)
	public Integer getId(){
		return id;
	}
	public Patient setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_DATE",nullable=true)
	public Date getReferralDate(){
		return referralDate;
	}
	public Patient setReferralDate(Date referralDate)throws Exception{
		if(!isSame(referralDate, getReferralDate())){
			Date oldValue = getReferralDate();
			this.referralDate=referralDate;
			setProperty("REFERRAL_DATE", referralDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_SOURCE",nullable=false,length=64)
	public String getReferralSource(){
		return referralSource;
	}
	public Patient setReferralSource(String referralSource)throws Exception{
		if(!isSame(referralSource, getReferralSource())){
			String oldValue = getReferralSource();
			this.referralSource=referralSource;
			setProperty("REFERRAL_SOURCE", referralSource, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public Patient setName(String name)throws Exception{
		if(!isSame(name, getName())){
			String oldValue = getName();
			this.name=name;
			setProperty("NAME", name, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MR_NUM",nullable=true,length=16)
	public String getMrNum(){
		return mrNum;
	}
	public Patient setMrNum(String mrNum)throws Exception{
		if(!isSame(mrNum, getMrNum())){
			String oldValue = getMrNum();
			this.mrNum=mrNum;
			setProperty("MR_NUM", mrNum, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="DIANOSIS",nullable=true,length=64)
	public String getDianosis(){
		return dianosis;
	}
	public Patient setDianosis(String dianosis)throws Exception{
		if(!isSame(dianosis, getDianosis())){
			String oldValue = getDianosis();
			this.dianosis=dianosis;
			setProperty("DIANOSIS", dianosis, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="THERAPY_TYPE",nullable=true,length=64)
	public String getTherapyType(){
		return therapyType;
	}
	public Patient setTherapyType(String therapyType)throws Exception{
		if(!isSame(therapyType, getTherapyType())){
			String oldValue = getTherapyType();
			this.therapyType=therapyType;
			setProperty("THERAPY_TYPE", therapyType, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="IV_ACCESS",nullable=true,length=64)
	public String getIvAccess(){
		return ivAccess;
	}
	public Patient setIvAccess(String ivAccess)throws Exception{
		if(!isSame(ivAccess, getIvAccess())){
			String oldValue = getIvAccess();
			this.ivAccess=ivAccess;
			setProperty("IV_ACCESS", ivAccess, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="START_OF_CARE",nullable=true)
	public boolean isStartOfCare(){
		return startOfCare;
	}
	public Patient setStartOfCare(boolean startOfCare)throws Exception{
		if(!isSame(startOfCare, isStartOfCare())){
			boolean oldValue = isStartOfCare();
			this.startOfCare=startOfCare;
			setProperty("START_OF_CARE", startOfCare, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="START_OF_CARE_DATE",nullable=true)
	public Date getStartOfCareDate(){
		return startOfCareDate;
	}
	public Patient setStartOfCareDate(Date startOfCareDate)throws Exception{
		if(!isSame(startOfCareDate, getStartOfCareDate())){
			Date oldValue = getStartOfCareDate();
			this.startOfCareDate=startOfCareDate;
			setProperty("START_OF_CARE_DATE", startOfCareDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="SERVICE_ADDRESS",nullable=true,length=50)
	public String getServiceAddress(){
		return serviceAddress;
	}
	public Patient setServiceAddress(String serviceAddress)throws Exception{
		if(!isSame(serviceAddress, getServiceAddress())){
			String oldValue = getServiceAddress();
			this.serviceAddress=serviceAddress;
			setProperty("SERVICE_ADDRESS", serviceAddress, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="BILLING",nullable=true,length=64)
	public String getBilling(){
		return billing;
	}
	public Patient setBilling(String billing)throws Exception{
		if(!isSame(billing, getBilling())){
			String oldValue = getBilling();
			this.billing=billing;
			setProperty("BILLING", billing, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="RX",nullable=true,length=64)
	public String getRx(){
		return rx;
	}
	public Patient setRx(String rx)throws Exception{
		if(!isSame(rx, getRx())){
			String oldValue = getRx();
			this.rx=rx;
			setProperty("RX", rx, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="EST_LAST_DAY_OF_SERVICE",nullable=true)
	public Date getEstLastDayOfService(){
		return estLastDayOfService;
	}
	public Patient setEstLastDayOfService(Date estLastDayOfService)throws Exception{
		if(!isSame(estLastDayOfService, getEstLastDayOfService())){
			Date oldValue = getEstLastDayOfService();
			this.estLastDayOfService=estLastDayOfService;
			setProperty("EST_LAST_DAY_OF_SERVICE", estLastDayOfService, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="LABS",nullable=true)
	public boolean isLabs(){
		return labs;
	}
	public Patient setLabs(boolean labs)throws Exception{
		if(!isSame(labs, isLabs())){
			boolean oldValue = isLabs();
			this.labs=labs;
			setProperty("LABS", labs, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="LABS_FREQUENCY",nullable=true,length=64)
	public String getLabsFrequency(){
		return labsFrequency;
	}
	public Patient setLabsFrequency(String labsFrequency)throws Exception{
		if(!isSame(labsFrequency, getLabsFrequency())){
			String oldValue = getLabsFrequency();
			this.labsFrequency=labsFrequency;
			setProperty("LABS_FREQUENCY", labsFrequency, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="FIRST_RECERT_DUE",nullable=true)
	public Date getFirstRecertDue(){
		return firstRecertDue;
	}
	public Patient setFirstRecertDue(Date firstRecertDue)throws Exception{
		if(!isSame(firstRecertDue, getFirstRecertDue())){
			Date oldValue = getFirstRecertDue();
			this.firstRecertDue=firstRecertDue;
			setProperty("FIRST_RECERT_DUE", firstRecertDue, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="D_C_DATE",nullable=true,length=64)
	public String getDCDate(){
		return dCDate;
	}
	public Patient setDCDate(String dCDate)throws Exception{
		if(!isSame(dCDate, getDCDate())){
			String oldValue = getDCDate();
			this.dCDate=dCDate;
			setProperty("D_C_DATE", dCDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="INFO_IN_S_O_S",nullable=true,length=64)
	public String getInfoInSOS(){
		return infoInSOS;
	}
	public Patient setInfoInSOS(String infoInSOS)throws Exception{
		if(!isSame(infoInSOS, getInfoInSOS())){
			String oldValue = getInfoInSOS();
			this.infoInSOS=infoInSOS;
			setProperty("INFO_IN_S_O_S", infoInSOS, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="SCHEDULING_PREFERENCE",nullable=true,length=64)
	public String getSchedulingPreference(){
		return schedulingPreference;
	}
	public Patient setSchedulingPreference(String schedulingPreference)throws Exception{
		if(!isSame(schedulingPreference, getSchedulingPreference())){
			String oldValue = getSchedulingPreference();
			this.schedulingPreference=schedulingPreference;
			setProperty("SCHEDULING_PREFERENCE", schedulingPreference, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_NOTE",nullable=true,length=1024)
	public String getReferralNote(){
		return referralNote;
	}
	public Patient setReferralNote(String referralNote)throws Exception{
		if(!isSame(referralNote, getReferralNote())){
			String oldValue = getReferralNote();
			this.referralNote=referralNote;
			setProperty("REFERRAL_NOTE", referralNote, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_RESOLUTION_ID",nullable=true)
	public Integer getReferralResolutionId(){
		return referralResolutionId;
	}
	public Patient setReferralResolutionId(Integer referralResolutionId)throws Exception{
		if(!isSame(referralResolutionId, getReferralResolutionId())){
			Integer oldValue = getReferralResolutionId();
			this.referralResolutionId=referralResolutionId;
			setProperty("REFERRAL_RESOLUTION_ID", referralResolutionId, oldValue);
			referralResolution=null;
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_RESOLUTION_DATE",nullable=true)
	public Date getReferralResolutionDate(){
		return referralResolutionDate;
	}
	public Patient setReferralResolutionDate(Date referralResolutionDate)throws Exception{
		if(!isSame(referralResolutionDate, getReferralResolutionDate())){
			Date oldValue = getReferralResolutionDate();
			this.referralResolutionDate=referralResolutionDate;
			setProperty("REFERRAL_RESOLUTION_DATE", referralResolutionDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="REFERRAL_RESOLUTION_NOTE",nullable=true,length=512)
	public String getReferralResolutionNote(){
		return referralResolutionNote;
	}
	public Patient setReferralResolutionNote(String referralResolutionNote)throws Exception{
		if(!isSame(referralResolutionNote, getReferralResolutionNote())){
			String oldValue = getReferralResolutionNote();
			this.referralResolutionNote=referralResolutionNote;
			setProperty("REFERRAL_RESOLUTION_NOTE", referralResolutionNote, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="VENDOR_CONFIRMATION_DATE",nullable=true)
	public Date getVendorConfirmationDate(){
		return vendorConfirmationDate;
	}
	public Patient setVendorConfirmationDate(Date vendorConfirmationDate)throws Exception{
		if(!isSame(vendorConfirmationDate, getVendorConfirmationDate())){
			Date oldValue = getVendorConfirmationDate();
			this.vendorConfirmationDate=vendorConfirmationDate;
			setProperty("VENDOR_CONFIRMATION_DATE", vendorConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="NURSE_CONFIRMATION_DATE",nullable=true)
	public Date getNurseConfirmationDate(){
		return nurseConfirmationDate;
	}
	public Patient setNurseConfirmationDate(Date nurseConfirmationDate)throws Exception{
		if(!isSame(nurseConfirmationDate, getNurseConfirmationDate())){
			Date oldValue = getNurseConfirmationDate();
			this.nurseConfirmationDate=nurseConfirmationDate;
			setProperty("NURSE_CONFIRMATION_DATE", nurseConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="PATIENT_CONFIRMATION_DATE",nullable=true)
	public Date getPatientConfirmationDate(){
		return patientConfirmationDate;
	}
	public Patient setPatientConfirmationDate(Date patientConfirmationDate)throws Exception{
		if(!isSame(patientConfirmationDate, getPatientConfirmationDate())){
			Date oldValue = getPatientConfirmationDate();
			this.patientConfirmationDate=patientConfirmationDate;
			setProperty("PATIENT_CONFIRMATION_DATE", patientConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MEDS_DELIVERY_DATE",nullable=true)
	public Date getMedsDeliveryDate(){
		return medsDeliveryDate;
	}
	public Patient setMedsDeliveryDate(Date medsDeliveryDate)throws Exception{
		if(!isSame(medsDeliveryDate, getMedsDeliveryDate())){
			Date oldValue = getMedsDeliveryDate();
			this.medsDeliveryDate=medsDeliveryDate;
			setProperty("MEDS_DELIVERY_DATE", medsDeliveryDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="MEDS_CONFIRMATION_DATE",nullable=true)
	public Date getMedsConfirmationDate(){
		return medsConfirmationDate;
	}
	public Patient setMedsConfirmationDate(Date medsConfirmationDate)throws Exception{
		if(!isSame(medsConfirmationDate, getMedsConfirmationDate())){
			Date oldValue = getMedsConfirmationDate();
			this.medsConfirmationDate=medsConfirmationDate;
			setProperty("MEDS_CONFIRMATION_DATE", medsConfirmationDate, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public Patient setActive(boolean active)throws Exception{
		if(!isSame(active, isActive())){
			boolean oldValue = isActive();
			this.active=active;
			setProperty("ACTIVE", active, oldValue);
		}
		return (Patient)this;
	}
	@Column(name="DESCRIPTION",nullable=true,length=256)
	public String getDescription(){
		return description;
	}
	public Patient setDescription(String description)throws Exception{
		if(!isSame(description, getDescription())){
			String oldValue = getDescription();
			this.description=description;
			setProperty("DESCRIPTION", description, oldValue);
		}
		return (Patient)this;
	}
	public GeneralData getReferralResolution(){
		if(referralResolution==null)
			referralResolution=GeneralData.getInstance(getReferralResolutionId());
		return referralResolution;
	}
	public Patient setReferralResolution(GeneralData referralResolution)throws Exception{
		setReferralResolutionId(referralResolution==null?null:referralResolution.getId());
		this.referralResolution=referralResolution;
		return (Patient)this;
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
		return getPropertyValue(PROPERTY.valueOf(property));
	}
	public Object getPropertyValue(PROPERTY property){
		switch(property){
			case ID: return getId();
			case REFERRAL_DATE: return getReferralDate();
			case REFERRAL_SOURCE: return getReferralSource();
			case NAME: return getName();
			case MR_NUM: return getMrNum();
			case DIANOSIS: return getDianosis();
			case THERAPY_TYPE: return getTherapyType();
			case IV_ACCESS: return getIvAccess();
			case START_OF_CARE: return isStartOfCare();
			case START_OF_CARE_DATE: return getStartOfCareDate();
			case SERVICE_ADDRESS: return getServiceAddress();
			case BILLING: return getBilling();
			case RX: return getRx();
			case EST_LAST_DAY_OF_SERVICE: return getEstLastDayOfService();
			case LABS: return isLabs();
			case LABS_FREQUENCY: return getLabsFrequency();
			case FIRST_RECERT_DUE: return getFirstRecertDue();
			case D_C_DATE: return getDCDate();
			case INFO_IN_S_O_S: return getInfoInSOS();
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
			case DESCRIPTION: return getDescription();
		}
		return null;
	}
	public void setPropertyValue(String property, String value)throws Exception{
		if(property==null)return;
		setPropertyValue(PROPERTY.valueOf(property.toUpperCase()),value);
	}
	public void setPropertyValue(PROPERTY property, String value)throws Exception{
		switch(property){
			case ID:setId(Integer.valueOf(value)); break;
			case REFERRAL_DATE:setReferralDate(FormatText.USER_DATE.parse(value)); break;
			case REFERRAL_SOURCE:setReferralSource(String.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case MR_NUM:setMrNum(String.valueOf(value)); break;
			case DIANOSIS:setDianosis(String.valueOf(value)); break;
			case THERAPY_TYPE:setTherapyType(String.valueOf(value)); break;
			case IV_ACCESS:setIvAccess(String.valueOf(value)); break;
			case START_OF_CARE:setStartOfCare(Boolean.valueOf(value)); break;
			case START_OF_CARE_DATE:setStartOfCareDate(FormatText.USER_DATE.parse(value)); break;
			case SERVICE_ADDRESS:setServiceAddress(String.valueOf(value)); break;
			case BILLING:setBilling(String.valueOf(value)); break;
			case RX:setRx(String.valueOf(value)); break;
			case EST_LAST_DAY_OF_SERVICE:setEstLastDayOfService(FormatText.USER_DATE.parse(value)); break;
			case LABS:setLabs(Boolean.valueOf(value)); break;
			case LABS_FREQUENCY:setLabsFrequency(String.valueOf(value)); break;
			case FIRST_RECERT_DUE:setFirstRecertDue(FormatText.USER_DATE.parse(value)); break;
			case D_C_DATE:setDCDate(String.valueOf(value)); break;
			case INFO_IN_S_O_S:setInfoInSOS(String.valueOf(value)); break;
			case SCHEDULING_PREFERENCE:setSchedulingPreference(String.valueOf(value)); break;
			case REFERRAL_NOTE:setReferralNote(String.valueOf(value)); break;
			case REFERRAL_RESOLUTION_ID:setReferralResolutionId(Integer.valueOf(value)); break;
			case REFERRAL_RESOLUTION_DATE:setReferralResolutionDate(FormatText.USER_DATE.parse(value)); break;
			case REFERRAL_RESOLUTION_NOTE:setReferralResolutionNote(String.valueOf(value)); break;
			case VENDOR_CONFIRMATION_DATE:setVendorConfirmationDate(FormatText.USER_DATE.parse(value)); break;
			case NURSE_CONFIRMATION_DATE:setNurseConfirmationDate(FormatText.USER_DATE.parse(value)); break;
			case PATIENT_CONFIRMATION_DATE:setPatientConfirmationDate(FormatText.USER_DATE.parse(value)); break;
			case MEDS_DELIVERY_DATE:setMedsDeliveryDate(FormatText.USER_DATE.parse(value)); break;
			case MEDS_CONFIRMATION_DATE:setMedsConfirmationDate(FormatText.USER_DATE.parse(value)); break;
			case ACTIVE:setActive(Boolean.valueOf(value)); break;
			case DESCRIPTION:setDescription(String.valueOf(value)); break;
		}
	}
	public Patient copy()throws Exception{
		Patient cp = new Patient((Patient)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(PatientDAO cp)throws Exception{
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(PatientDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getReferralDate(),o.getReferralDate())) diffs.add("REFERRAL_DATE");
		if(!isSame(getReferralSource(),o.getReferralSource())) diffs.add("REFERRAL_SOURCE");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getMrNum(),o.getMrNum())) diffs.add("MR_NUM");
		if(!isSame(getDianosis(),o.getDianosis())) diffs.add("DIANOSIS");
		if(!isSame(getTherapyType(),o.getTherapyType())) diffs.add("THERAPY_TYPE");
		if(!isSame(getIvAccess(),o.getIvAccess())) diffs.add("IV_ACCESS");
		if(!isSame(isStartOfCare(),o.isStartOfCare())) diffs.add("START_OF_CARE");
		if(!isSame(getStartOfCareDate(),o.getStartOfCareDate())) diffs.add("START_OF_CARE_DATE");
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
	public void insertParents()throws Exception{
	}
	public void insertPreCheck()throws Exception{
		if (isNull(referralSource))
			 throw new Exception("REFERRAL_SOURCE is required.");
		if (isNull(name))
			 throw new Exception("NAME is required.");
	}
	public void insertChildren()throws Exception{
	}
}
