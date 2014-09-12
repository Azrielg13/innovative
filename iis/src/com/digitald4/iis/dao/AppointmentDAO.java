package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.AssessmentEntry;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Paystub;
import com.digitald4.common.model.User;
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
import org.joda.time.DateTime;
public abstract class AppointmentDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,PATIENT_ID,NURSE_ID,START,END,CANCELLED,TIME_IN_D,TIME_OUT_D,MILEAGE_D,PAY_FLAT_D,PAY_RATE_D,PAY_HOURS_D,PAY_MILEAGE_D,PAY_MILEAGE_RATE_D,PAYING_TYPE_ID_D,PAYSTUB_ID,BILLING_FLAT_D,BILLING_RATE_D,BILLED_HOURS_D,BILLING_MILEAGE_D,BILLING_MILEAGE_RATE_D,BILLING_TYPE_ID_D,INVOICE_ID,ASSESSMENT_COMPLETE,ASSESSMENT_APPROVED,APPROVED_DATE,APPROVER_ID,DATA_FILE_ID};
	private Integer id;
	private Integer patientId;
	private Integer nurseId;
	private DateTime start;
	private DateTime end;
	private boolean cancelled;
	private DateTime timeInD;
	private DateTime timeOutD;
	private short mileageD;
	private double payFlatD;
	private double payRateD;
	private double payHoursD;
	private short payMileageD;
	private double payMileageRateD;
	private Integer payingTypeIdD;
	private Integer paystubId;
	private double billingFlatD;
	private double billingRateD;
	private double billedHoursD;
	private short billingMileageD;
	private double billingMileageRateD;
	private Integer billingTypeIdD;
	private Integer invoiceId;
	private boolean assessmentComplete;
	private boolean assessmentApproved;
	private Date approvedDate;
	private Integer approverId;
	private Integer dataFileId;
	private List<AssessmentEntry> assessmentEntrys;
	private GeneralData billingTypeD;
	private DataFile dataFile;
	private Invoice invoice;
	private Nurse nurse;
	private Patient patient;
	private GeneralData payingTypeD;
	private Paystub paystub;
	private User user;
	public static Appointment getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Appointment getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Appointment o = null;
		if(fetch || cache != null && cache.contains(Appointment.class, pk))
			o = em.find(Appointment.class, pk);
		return o;
	}
	public static List<Appointment> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Appointment> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Appointment> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Appointment o";
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
	public synchronized static List<Appointment> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Appointment> tq = em.createQuery(jpql,Appointment.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Appointment> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Appointment> tq = em.createNamedQuery(name,Appointment.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public AppointmentDAO(){}
	public AppointmentDAO(Integer id){
		this.id=id;
	}
	public AppointmentDAO(AppointmentDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(AppointmentDAO orig){
		this.patientId=orig.getPatientId();
		this.nurseId=orig.getNurseId();
		this.start=orig.getStart();
		this.end=orig.getEnd();
		this.cancelled=orig.isCancelled();
		this.timeInD=orig.getTimeInD();
		this.timeOutD=orig.getTimeOutD();
		this.mileageD=orig.getMileageD();
		this.payFlatD=orig.getPayFlatD();
		this.payRateD=orig.getPayRateD();
		this.payHoursD=orig.getPayHoursD();
		this.payMileageD=orig.getPayMileageD();
		this.payMileageRateD=orig.getPayMileageRateD();
		this.payingTypeIdD=orig.getPayingTypeIdD();
		this.paystubId=orig.getPaystubId();
		this.billingFlatD=orig.getBillingFlatD();
		this.billingRateD=orig.getBillingRateD();
		this.billedHoursD=orig.getBilledHoursD();
		this.billingMileageD=orig.getBillingMileageD();
		this.billingMileageRateD=orig.getBillingMileageRateD();
		this.billingTypeIdD=orig.getBillingTypeIdD();
		this.invoiceId=orig.getInvoiceId();
		this.assessmentComplete=orig.isAssessmentComplete();
		this.assessmentApproved=orig.isAssessmentApproved();
		this.approvedDate=orig.getApprovedDate();
		this.approverId=orig.getApproverId();
		this.dataFileId=orig.getDataFileId();
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
	public Appointment setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PATIENT_ID",nullable=false)
	public Integer getPatientId(){
		return patientId;
	}
	public Appointment setPatientId(Integer patientId) throws Exception  {
		Integer oldValue = getPatientId();
		if (!isSame(patientId, oldValue)) {
			this.patientId = patientId;
			setProperty("PATIENT_ID", patientId, oldValue);
			patient=null;
		}
		return (Appointment)this;
	}
	@Column(name="NURSE_ID",nullable=true)
	public Integer getNurseId(){
		return nurseId;
	}
	public Appointment setNurseId(Integer nurseId) throws Exception  {
		Integer oldValue = getNurseId();
		if (!isSame(nurseId, oldValue)) {
			this.nurseId = nurseId;
			setProperty("NURSE_ID", nurseId, oldValue);
			nurse=null;
		}
		return (Appointment)this;
	}
	@Column(name="START",nullable=false)
	public DateTime getStart(){
		return start;
	}
	public Appointment setStart(DateTime start) throws Exception  {
		DateTime oldValue = getStart();
		if (!isSame(start, oldValue)) {
			this.start = start;
			setProperty("START", start, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="END",nullable=false)
	public DateTime getEnd(){
		return end;
	}
	public Appointment setEnd(DateTime end) throws Exception  {
		DateTime oldValue = getEnd();
		if (!isSame(end, oldValue)) {
			this.end = end;
			setProperty("END", end, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="CANCELLED",nullable=true)
	public boolean isCancelled(){
		return cancelled;
	}
	public Appointment setCancelled(boolean cancelled) throws Exception  {
		boolean oldValue = isCancelled();
		if (!isSame(cancelled, oldValue)) {
			this.cancelled = cancelled;
			setProperty("CANCELLED", cancelled, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="TIME_IN_D",nullable=true)
	public DateTime getTimeInD(){
		return timeInD;
	}
	public Appointment setTimeInD(DateTime timeInD) throws Exception  {
		DateTime oldValue = getTimeInD();
		if (!isSame(timeInD, oldValue)) {
			this.timeInD = timeInD;
			setProperty("TIME_IN_D", timeInD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="TIME_OUT_D",nullable=true)
	public DateTime getTimeOutD(){
		return timeOutD;
	}
	public Appointment setTimeOutD(DateTime timeOutD) throws Exception  {
		DateTime oldValue = getTimeOutD();
		if (!isSame(timeOutD, oldValue)) {
			this.timeOutD = timeOutD;
			setProperty("TIME_OUT_D", timeOutD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="MILEAGE_D",nullable=true)
	public short getMileageD(){
		return mileageD;
	}
	public Appointment setMileageD(short mileageD) throws Exception  {
		short oldValue = getMileageD();
		if (!isSame(mileageD, oldValue)) {
			this.mileageD = mileageD;
			setProperty("MILEAGE_D", mileageD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAY_FLAT_D",nullable=true)
	public double getPayFlatD(){
		return payFlatD;
	}
	public Appointment setPayFlatD(double payFlatD) throws Exception  {
		double oldValue = getPayFlatD();
		if (!isSame(payFlatD, oldValue)) {
			this.payFlatD = payFlatD;
			setProperty("PAY_FLAT_D", payFlatD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAY_RATE_D",nullable=true)
	public double getPayRateD(){
		return payRateD;
	}
	public Appointment setPayRateD(double payRateD) throws Exception  {
		double oldValue = getPayRateD();
		if (!isSame(payRateD, oldValue)) {
			this.payRateD = payRateD;
			setProperty("PAY_RATE_D", payRateD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAY_HOURS_D",nullable=true)
	public double getPayHoursD(){
		return payHoursD;
	}
	public Appointment setPayHoursD(double payHoursD) throws Exception  {
		double oldValue = getPayHoursD();
		if (!isSame(payHoursD, oldValue)) {
			this.payHoursD = payHoursD;
			setProperty("PAY_HOURS_D", payHoursD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAY_MILEAGE_D",nullable=true)
	public short getPayMileageD(){
		return payMileageD;
	}
	public Appointment setPayMileageD(short payMileageD) throws Exception  {
		short oldValue = getPayMileageD();
		if (!isSame(payMileageD, oldValue)) {
			this.payMileageD = payMileageD;
			setProperty("PAY_MILEAGE_D", payMileageD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAY_MILEAGE_RATE_D",nullable=true)
	public double getPayMileageRateD(){
		return payMileageRateD;
	}
	public Appointment setPayMileageRateD(double payMileageRateD) throws Exception  {
		double oldValue = getPayMileageRateD();
		if (!isSame(payMileageRateD, oldValue)) {
			this.payMileageRateD = payMileageRateD;
			setProperty("PAY_MILEAGE_RATE_D", payMileageRateD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAYING_TYPE_ID_D",nullable=true)
	public Integer getPayingTypeIdD(){
		return payingTypeIdD;
	}
	public Appointment setPayingTypeIdD(Integer payingTypeIdD) throws Exception  {
		Integer oldValue = getPayingTypeIdD();
		if (!isSame(payingTypeIdD, oldValue)) {
			this.payingTypeIdD = payingTypeIdD;
			setProperty("PAYING_TYPE_ID_D", payingTypeIdD, oldValue);
			payingTypeD=null;
		}
		return (Appointment)this;
	}
	@Column(name="PAYSTUB_ID",nullable=true)
	public Integer getPaystubId(){
		return paystubId;
	}
	public Appointment setPaystubId(Integer paystubId) throws Exception  {
		Integer oldValue = getPaystubId();
		if (!isSame(paystubId, oldValue)) {
			this.paystubId = paystubId;
			setProperty("PAYSTUB_ID", paystubId, oldValue);
			paystub=null;
		}
		return (Appointment)this;
	}
	@Column(name="BILLING_FLAT_D",nullable=true)
	public double getBillingFlatD(){
		return billingFlatD;
	}
	public Appointment setBillingFlatD(double billingFlatD) throws Exception  {
		double oldValue = getBillingFlatD();
		if (!isSame(billingFlatD, oldValue)) {
			this.billingFlatD = billingFlatD;
			setProperty("BILLING_FLAT_D", billingFlatD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="BILLING_RATE_D",nullable=true)
	public double getBillingRateD(){
		return billingRateD;
	}
	public Appointment setBillingRateD(double billingRateD) throws Exception  {
		double oldValue = getBillingRateD();
		if (!isSame(billingRateD, oldValue)) {
			this.billingRateD = billingRateD;
			setProperty("BILLING_RATE_D", billingRateD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="BILLED_HOURS_D",nullable=true)
	public double getBilledHoursD(){
		return billedHoursD;
	}
	public Appointment setBilledHoursD(double billedHoursD) throws Exception  {
		double oldValue = getBilledHoursD();
		if (!isSame(billedHoursD, oldValue)) {
			this.billedHoursD = billedHoursD;
			setProperty("BILLED_HOURS_D", billedHoursD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="BILLING_MILEAGE_D",nullable=true)
	public short getBillingMileageD(){
		return billingMileageD;
	}
	public Appointment setBillingMileageD(short billingMileageD) throws Exception  {
		short oldValue = getBillingMileageD();
		if (!isSame(billingMileageD, oldValue)) {
			this.billingMileageD = billingMileageD;
			setProperty("BILLING_MILEAGE_D", billingMileageD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="BILLING_MILEAGE_RATE_D",nullable=true)
	public double getBillingMileageRateD(){
		return billingMileageRateD;
	}
	public Appointment setBillingMileageRateD(double billingMileageRateD) throws Exception  {
		double oldValue = getBillingMileageRateD();
		if (!isSame(billingMileageRateD, oldValue)) {
			this.billingMileageRateD = billingMileageRateD;
			setProperty("BILLING_MILEAGE_RATE_D", billingMileageRateD, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="BILLING_TYPE_ID_D",nullable=true)
	public Integer getBillingTypeIdD(){
		return billingTypeIdD;
	}
	public Appointment setBillingTypeIdD(Integer billingTypeIdD) throws Exception  {
		Integer oldValue = getBillingTypeIdD();
		if (!isSame(billingTypeIdD, oldValue)) {
			this.billingTypeIdD = billingTypeIdD;
			setProperty("BILLING_TYPE_ID_D", billingTypeIdD, oldValue);
			billingTypeD=null;
		}
		return (Appointment)this;
	}
	@Column(name="INVOICE_ID",nullable=true)
	public Integer getInvoiceId(){
		return invoiceId;
	}
	public Appointment setInvoiceId(Integer invoiceId) throws Exception  {
		Integer oldValue = getInvoiceId();
		if (!isSame(invoiceId, oldValue)) {
			this.invoiceId = invoiceId;
			setProperty("INVOICE_ID", invoiceId, oldValue);
			invoice=null;
		}
		return (Appointment)this;
	}
	@Column(name="ASSESSMENT_COMPLETE",nullable=true)
	public boolean isAssessmentComplete(){
		return assessmentComplete;
	}
	public Appointment setAssessmentComplete(boolean assessmentComplete) throws Exception  {
		boolean oldValue = isAssessmentComplete();
		if (!isSame(assessmentComplete, oldValue)) {
			this.assessmentComplete = assessmentComplete;
			setProperty("ASSESSMENT_COMPLETE", assessmentComplete, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="ASSESSMENT_APPROVED",nullable=true)
	public boolean isAssessmentApproved(){
		return assessmentApproved;
	}
	public Appointment setAssessmentApproved(boolean assessmentApproved) throws Exception  {
		boolean oldValue = isAssessmentApproved();
		if (!isSame(assessmentApproved, oldValue)) {
			this.assessmentApproved = assessmentApproved;
			setProperty("ASSESSMENT_APPROVED", assessmentApproved, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="APPROVED_DATE",nullable=true)
	public Date getApprovedDate(){
		return approvedDate;
	}
	public Appointment setApprovedDate(Date approvedDate) throws Exception  {
		Date oldValue = getApprovedDate();
		if (!isSame(approvedDate, oldValue)) {
			this.approvedDate = approvedDate;
			setProperty("APPROVED_DATE", approvedDate, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="APPROVER_ID",nullable=true)
	public Integer getApproverId(){
		return approverId;
	}
	public Appointment setApproverId(Integer approverId) throws Exception  {
		Integer oldValue = getApproverId();
		if (!isSame(approverId, oldValue)) {
			this.approverId = approverId;
			setProperty("APPROVER_ID", approverId, oldValue);
			user=null;
		}
		return (Appointment)this;
	}
	@Column(name="DATA_FILE_ID",nullable=true)
	public Integer getDataFileId(){
		return dataFileId;
	}
	public Appointment setDataFileId(Integer dataFileId) throws Exception  {
		Integer oldValue = getDataFileId();
		if (!isSame(dataFileId, oldValue)) {
			this.dataFileId = dataFileId;
			setProperty("DATA_FILE_ID", dataFileId, oldValue);
			dataFile=null;
		}
		return (Appointment)this;
	}
	public GeneralData getBillingTypeD(){
		if(billingTypeD==null)
			billingTypeD=GeneralData.getInstance(getBillingTypeIdD());
		return billingTypeD;
	}
	public Appointment setBillingTypeD(GeneralData billingTypeD) throws Exception {
		setBillingTypeIdD(billingTypeD==null?null:billingTypeD.getId());
		this.billingTypeD=billingTypeD;
		return (Appointment)this;
	}
	public DataFile getDataFile(){
		if(dataFile==null)
			dataFile=DataFile.getInstance(getDataFileId());
		return dataFile;
	}
	public Appointment setDataFile(DataFile dataFile) throws Exception {
		setDataFileId(dataFile==null?null:dataFile.getId());
		this.dataFile=dataFile;
		return (Appointment)this;
	}
	public Invoice getInvoice(){
		if(invoice==null)
			invoice=Invoice.getInstance(getInvoiceId());
		return invoice;
	}
	public Appointment setInvoice(Invoice invoice) throws Exception {
		setInvoiceId(invoice==null?null:invoice.getId());
		this.invoice=invoice;
		return (Appointment)this;
	}
	public Nurse getNurse(){
		if(nurse==null)
			nurse=Nurse.getInstance(getNurseId());
		return nurse;
	}
	public Appointment setNurse(Nurse nurse) throws Exception {
		setNurseId(nurse==null?null:nurse.getId());
		this.nurse=nurse;
		return (Appointment)this;
	}
	public Patient getPatient(){
		if(patient==null)
			patient=Patient.getInstance(getPatientId());
		return patient;
	}
	public Appointment setPatient(Patient patient) throws Exception {
		setPatientId(patient==null?null:patient.getId());
		this.patient=patient;
		return (Appointment)this;
	}
	public GeneralData getPayingTypeD(){
		if(payingTypeD==null)
			payingTypeD=GeneralData.getInstance(getPayingTypeIdD());
		return payingTypeD;
	}
	public Appointment setPayingTypeD(GeneralData payingTypeD) throws Exception {
		setPayingTypeIdD(payingTypeD==null?null:payingTypeD.getId());
		this.payingTypeD=payingTypeD;
		return (Appointment)this;
	}
	public Paystub getPaystub(){
		if(paystub==null)
			paystub=Paystub.getInstance(getPaystubId());
		return paystub;
	}
	public Appointment setPaystub(Paystub paystub) throws Exception {
		setPaystubId(paystub==null?null:paystub.getId());
		this.paystub=paystub;
		return (Appointment)this;
	}
	public User getUser(){
		if(user==null)
			user=User.getInstance(getApproverId());
		return user;
	}
	public Appointment setUser(User user) throws Exception {
		setApproverId(user==null?null:user.getId());
		this.user=user;
		return (Appointment)this;
	}
	public List<AssessmentEntry> getAssessmentEntrys(){
		if(isNewInstance() || assessmentEntrys != null){
			if(assessmentEntrys == null)
				assessmentEntrys = new SortedList<AssessmentEntry>();
			return assessmentEntrys;
		}
		return AssessmentEntry.getNamedCollection("findByAppointment",getId());
	}
	public Appointment addAssessmentEntry(AssessmentEntry assessmentEntry) throws Exception {
		assessmentEntry.setAppointment((Appointment)this);
		if(isNewInstance() || assessmentEntrys != null)
			getAssessmentEntrys().add(assessmentEntry);
		else
			assessmentEntry.insert();
		return (Appointment)this;
	}
	public Appointment removeAssessmentEntry(AssessmentEntry assessmentEntry) throws Exception {
		if(isNewInstance() || assessmentEntrys != null)
			getAssessmentEntrys().remove(assessmentEntry);
		else
			assessmentEntry.delete();
		return (Appointment)this;
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
			case PATIENT_ID: return getPatientId();
			case NURSE_ID: return getNurseId();
			case START: return getStart();
			case END: return getEnd();
			case CANCELLED: return isCancelled();
			case TIME_IN_D: return getTimeInD();
			case TIME_OUT_D: return getTimeOutD();
			case MILEAGE_D: return getMileageD();
			case PAY_FLAT_D: return getPayFlatD();
			case PAY_RATE_D: return getPayRateD();
			case PAY_HOURS_D: return getPayHoursD();
			case PAY_MILEAGE_D: return getPayMileageD();
			case PAY_MILEAGE_RATE_D: return getPayMileageRateD();
			case PAYING_TYPE_ID_D: return getPayingTypeIdD();
			case PAYSTUB_ID: return getPaystubId();
			case BILLING_FLAT_D: return getBillingFlatD();
			case BILLING_RATE_D: return getBillingRateD();
			case BILLED_HOURS_D: return getBilledHoursD();
			case BILLING_MILEAGE_D: return getBillingMileageD();
			case BILLING_MILEAGE_RATE_D: return getBillingMileageRateD();
			case BILLING_TYPE_ID_D: return getBillingTypeIdD();
			case INVOICE_ID: return getInvoiceId();
			case ASSESSMENT_COMPLETE: return isAssessmentComplete();
			case ASSESSMENT_APPROVED: return isAssessmentApproved();
			case APPROVED_DATE: return getApprovedDate();
			case APPROVER_ID: return getApproverId();
			case DATA_FILE_ID: return getDataFileId();
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
			case PATIENT_ID:setPatientId(Integer.valueOf(value)); break;
			case NURSE_ID:setNurseId(Integer.valueOf(value)); break;
			case START:setStart(new DateTime(value)); break;
			case END:setEnd(new DateTime(value)); break;
			case CANCELLED:setCancelled(Boolean.valueOf(value)); break;
			case TIME_IN_D:setTimeInD(new DateTime(value)); break;
			case TIME_OUT_D:setTimeOutD(new DateTime(value)); break;
			case MILEAGE_D:setMileageD(Short.valueOf(value)); break;
			case PAY_FLAT_D:setPayFlatD(Double.valueOf(value)); break;
			case PAY_RATE_D:setPayRateD(Double.valueOf(value)); break;
			case PAY_HOURS_D:setPayHoursD(Double.valueOf(value)); break;
			case PAY_MILEAGE_D:setPayMileageD(Short.valueOf(value)); break;
			case PAY_MILEAGE_RATE_D:setPayMileageRateD(Double.valueOf(value)); break;
			case PAYING_TYPE_ID_D:setPayingTypeIdD(Integer.valueOf(value)); break;
			case PAYSTUB_ID:setPaystubId(Integer.valueOf(value)); break;
			case BILLING_FLAT_D:setBillingFlatD(Double.valueOf(value)); break;
			case BILLING_RATE_D:setBillingRateD(Double.valueOf(value)); break;
			case BILLED_HOURS_D:setBilledHoursD(Double.valueOf(value)); break;
			case BILLING_MILEAGE_D:setBillingMileageD(Short.valueOf(value)); break;
			case BILLING_MILEAGE_RATE_D:setBillingMileageRateD(Double.valueOf(value)); break;
			case BILLING_TYPE_ID_D:setBillingTypeIdD(Integer.valueOf(value)); break;
			case INVOICE_ID:setInvoiceId(Integer.valueOf(value)); break;
			case ASSESSMENT_COMPLETE:setAssessmentComplete(Boolean.valueOf(value)); break;
			case ASSESSMENT_APPROVED:setAssessmentApproved(Boolean.valueOf(value)); break;
			case APPROVED_DATE:setApprovedDate(FormatText.parseDate(value)); break;
			case APPROVER_ID:setApproverId(Integer.valueOf(value)); break;
			case DATA_FILE_ID:setDataFileId(Integer.valueOf(value)); break;
		}
	}
	public Appointment copy() throws Exception {
		Appointment cp = new Appointment((Appointment)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(AppointmentDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(AssessmentEntry child:getAssessmentEntrys())
			cp.addAssessmentEntry(child.copy());
	}
	public Vector<String> getDifference(AppointmentDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getPatientId(),o.getPatientId())) diffs.add("PATIENT_ID");
		if(!isSame(getNurseId(),o.getNurseId())) diffs.add("NURSE_ID");
		if(!isSame(getStart(),o.getStart())) diffs.add("START");
		if(!isSame(getEnd(),o.getEnd())) diffs.add("END");
		if(!isSame(isCancelled(),o.isCancelled())) diffs.add("CANCELLED");
		if(!isSame(getTimeInD(),o.getTimeInD())) diffs.add("TIME_IN_D");
		if(!isSame(getTimeOutD(),o.getTimeOutD())) diffs.add("TIME_OUT_D");
		if(!isSame(getMileageD(),o.getMileageD())) diffs.add("MILEAGE_D");
		if(!isSame(getPayFlatD(),o.getPayFlatD())) diffs.add("PAY_FLAT_D");
		if(!isSame(getPayRateD(),o.getPayRateD())) diffs.add("PAY_RATE_D");
		if(!isSame(getPayHoursD(),o.getPayHoursD())) diffs.add("PAY_HOURS_D");
		if(!isSame(getPayMileageD(),o.getPayMileageD())) diffs.add("PAY_MILEAGE_D");
		if(!isSame(getPayMileageRateD(),o.getPayMileageRateD())) diffs.add("PAY_MILEAGE_RATE_D");
		if(!isSame(getPayingTypeIdD(),o.getPayingTypeIdD())) diffs.add("PAYING_TYPE_ID_D");
		if(!isSame(getPaystubId(),o.getPaystubId())) diffs.add("PAYSTUB_ID");
		if(!isSame(getBillingFlatD(),o.getBillingFlatD())) diffs.add("BILLING_FLAT_D");
		if(!isSame(getBillingRateD(),o.getBillingRateD())) diffs.add("BILLING_RATE_D");
		if(!isSame(getBilledHoursD(),o.getBilledHoursD())) diffs.add("BILLED_HOURS_D");
		if(!isSame(getBillingMileageD(),o.getBillingMileageD())) diffs.add("BILLING_MILEAGE_D");
		if(!isSame(getBillingMileageRateD(),o.getBillingMileageRateD())) diffs.add("BILLING_MILEAGE_RATE_D");
		if(!isSame(getBillingTypeIdD(),o.getBillingTypeIdD())) diffs.add("BILLING_TYPE_ID_D");
		if(!isSame(getInvoiceId(),o.getInvoiceId())) diffs.add("INVOICE_ID");
		if(!isSame(isAssessmentComplete(),o.isAssessmentComplete())) diffs.add("ASSESSMENT_COMPLETE");
		if(!isSame(isAssessmentApproved(),o.isAssessmentApproved())) diffs.add("ASSESSMENT_APPROVED");
		if(!isSame(getApprovedDate(),o.getApprovedDate())) diffs.add("APPROVED_DATE");
		if(!isSame(getApproverId(),o.getApproverId())) diffs.add("APPROVER_ID");
		if(!isSame(getDataFileId(),o.getDataFileId())) diffs.add("DATA_FILE_ID");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(invoice != null && invoice.isNewInstance())
				invoice.insert();
		if(nurse != null && nurse.isNewInstance())
				nurse.insert();
		if(patient != null && patient.isNewInstance())
				patient.insert();
		if(paystub != null && paystub.isNewInstance())
				paystub.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getPatientId()))
			 throw new Exception("PATIENT_ID is required.");
		if (isNull(getStart()))
			 throw new Exception("START is required.");
		if (isNull(getEnd()))
			 throw new Exception("END is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (assessmentEntrys != null) {
			for (AssessmentEntry assessmentEntry : getAssessmentEntrys()) {
				assessmentEntry.setAppointment((Appointment)this);
			}
		}
		if (assessmentEntrys != null) {
			for (AssessmentEntry assessmentEntry : getAssessmentEntrys()) {
				assessmentEntry.insert();
			}
			assessmentEntrys = null;
		}
	}
}
