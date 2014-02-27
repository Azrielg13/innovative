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
	public enum PROPERTY{ID,PATIENT_ID,NURSE_ID,START,END,CANCELLED,TIME_IN,TIME_OUT,PAY_RATE,MILEAGE,PAYSTUB_ID,MILEAGE_RATE,BILLING_RATE,VENDOR_MILEAGE_RATE,INVOICE_ID,ASSESSMENT_COMPLETE,ASSESSMENT_APPROVED,APPROVED_DATE,APPROVER_ID};
	private Integer id;
	private Integer patientId;
	private Integer nurseId;
	private DateTime start;
	private DateTime end;
	private boolean cancelled;
	private DateTime timeIn;
	private DateTime timeOut;
	private double payRate;
	private short mileage;
	private Integer paystubId;
	private double mileageRate;
	private double billingRate;
	private double vendorMileageRate;
	private Integer invoiceId;
	private boolean assessmentComplete;
	private boolean assessmentApproved;
	private Date approvedDate;
	private Integer approverId;
	private List<AssessmentEntry> assessmentEntrys;
	private Invoice invoice;
	private Nurse nurse;
	private Patient patient;
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
		this.timeIn=orig.getTimeIn();
		this.timeOut=orig.getTimeOut();
		this.payRate=orig.getPayRate();
		this.mileage=orig.getMileage();
		this.paystubId=orig.getPaystubId();
		this.mileageRate=orig.getMileageRate();
		this.billingRate=orig.getBillingRate();
		this.vendorMileageRate=orig.getVendorMileageRate();
		this.invoiceId=orig.getInvoiceId();
		this.assessmentComplete=orig.isAssessmentComplete();
		this.assessmentApproved=orig.isAssessmentApproved();
		this.approvedDate=orig.getApprovedDate();
		this.approverId=orig.getApproverId();
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
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
		if (!isSame(cancelled, oldValue)) {
			this.cancelled = cancelled;
			setProperty("CANCELLED", cancelled, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="TIME_IN",nullable=true)
	public DateTime getTimeIn(){
		return timeIn;
	}
	public Appointment setTimeIn(DateTime timeIn) throws Exception  {
		Object oldValue = null;
		if (!isSame(timeIn, oldValue)) {
			this.timeIn = timeIn;
			setProperty("TIME_IN", timeIn, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="TIME_OUT",nullable=true)
	public DateTime getTimeOut(){
		return timeOut;
	}
	public Appointment setTimeOut(DateTime timeOut) throws Exception  {
		Object oldValue = null;
		if (!isSame(timeOut, oldValue)) {
			this.timeOut = timeOut;
			setProperty("TIME_OUT", timeOut, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAY_RATE",nullable=true)
	public double getPayRate(){
		return payRate;
	}
	public Appointment setPayRate(double payRate) throws Exception  {
		Object oldValue = null;
		if (!isSame(payRate, oldValue)) {
			this.payRate = payRate;
			setProperty("PAY_RATE", payRate, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="MILEAGE",nullable=true)
	public short getMileage(){
		return mileage;
	}
	public Appointment setMileage(short mileage) throws Exception  {
		Object oldValue = null;
		if (!isSame(mileage, oldValue)) {
			this.mileage = mileage;
			setProperty("MILEAGE", mileage, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="PAYSTUB_ID",nullable=true)
	public Integer getPaystubId(){
		return paystubId;
	}
	public Appointment setPaystubId(Integer paystubId) throws Exception  {
		Object oldValue = null;
		if (!isSame(paystubId, oldValue)) {
			this.paystubId = paystubId;
			setProperty("PAYSTUB_ID", paystubId, oldValue);
			paystub=null;
		}
		return (Appointment)this;
	}
	@Column(name="MILEAGE_RATE",nullable=true)
	public double getMileageRate(){
		return mileageRate;
	}
	public Appointment setMileageRate(double mileageRate) throws Exception  {
		Object oldValue = null;
		if (!isSame(mileageRate, oldValue)) {
			this.mileageRate = mileageRate;
			setProperty("MILEAGE_RATE", mileageRate, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="BILLING_RATE",nullable=true)
	public double getBillingRate(){
		return billingRate;
	}
	public Appointment setBillingRate(double billingRate) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingRate, oldValue)) {
			this.billingRate = billingRate;
			setProperty("BILLING_RATE", billingRate, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="VENDOR_MILEAGE_RATE",nullable=true)
	public double getVendorMileageRate(){
		return vendorMileageRate;
	}
	public Appointment setVendorMileageRate(double vendorMileageRate) throws Exception  {
		Object oldValue = null;
		if (!isSame(vendorMileageRate, oldValue)) {
			this.vendorMileageRate = vendorMileageRate;
			setProperty("VENDOR_MILEAGE_RATE", vendorMileageRate, oldValue);
		}
		return (Appointment)this;
	}
	@Column(name="INVOICE_ID",nullable=true)
	public Integer getInvoiceId(){
		return invoiceId;
	}
	public Appointment setInvoiceId(Integer invoiceId) throws Exception  {
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
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
		Object oldValue = null;
		if (!isSame(approverId, oldValue)) {
			this.approverId = approverId;
			setProperty("APPROVER_ID", approverId, oldValue);
			user=null;
		}
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
			case TIME_IN: return getTimeIn();
			case TIME_OUT: return getTimeOut();
			case PAY_RATE: return getPayRate();
			case MILEAGE: return getMileage();
			case PAYSTUB_ID: return getPaystubId();
			case MILEAGE_RATE: return getMileageRate();
			case BILLING_RATE: return getBillingRate();
			case VENDOR_MILEAGE_RATE: return getVendorMileageRate();
			case INVOICE_ID: return getInvoiceId();
			case ASSESSMENT_COMPLETE: return isAssessmentComplete();
			case ASSESSMENT_APPROVED: return isAssessmentApproved();
			case APPROVED_DATE: return getApprovedDate();
			case APPROVER_ID: return getApproverId();
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
			case TIME_IN:setTimeIn(new DateTime(value)); break;
			case TIME_OUT:setTimeOut(new DateTime(value)); break;
			case PAY_RATE:setPayRate(Double.valueOf(value)); break;
			case MILEAGE:setMileage(Short.valueOf(value)); break;
			case PAYSTUB_ID:setPaystubId(Integer.valueOf(value)); break;
			case MILEAGE_RATE:setMileageRate(Double.valueOf(value)); break;
			case BILLING_RATE:setBillingRate(Double.valueOf(value)); break;
			case VENDOR_MILEAGE_RATE:setVendorMileageRate(Double.valueOf(value)); break;
			case INVOICE_ID:setInvoiceId(Integer.valueOf(value)); break;
			case ASSESSMENT_COMPLETE:setAssessmentComplete(Boolean.valueOf(value)); break;
			case ASSESSMENT_APPROVED:setAssessmentApproved(Boolean.valueOf(value)); break;
			case APPROVED_DATE:setApprovedDate(FormatText.parseDate(value)); break;
			case APPROVER_ID:setApproverId(Integer.valueOf(value)); break;
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
		if(!isSame(getTimeIn(),o.getTimeIn())) diffs.add("TIME_IN");
		if(!isSame(getTimeOut(),o.getTimeOut())) diffs.add("TIME_OUT");
		if(!isSame(getPayRate(),o.getPayRate())) diffs.add("PAY_RATE");
		if(!isSame(getMileage(),o.getMileage())) diffs.add("MILEAGE");
		if(!isSame(getPaystubId(),o.getPaystubId())) diffs.add("PAYSTUB_ID");
		if(!isSame(getMileageRate(),o.getMileageRate())) diffs.add("MILEAGE_RATE");
		if(!isSame(getBillingRate(),o.getBillingRate())) diffs.add("BILLING_RATE");
		if(!isSame(getVendorMileageRate(),o.getVendorMileageRate())) diffs.add("VENDOR_MILEAGE_RATE");
		if(!isSame(getInvoiceId(),o.getInvoiceId())) diffs.add("INVOICE_ID");
		if(!isSame(isAssessmentComplete(),o.isAssessmentComplete())) diffs.add("ASSESSMENT_COMPLETE");
		if(!isSame(isAssessmentApproved(),o.isAssessmentApproved())) diffs.add("ASSESSMENT_APPROVED");
		if(!isSame(getApprovedDate(),o.getApprovedDate())) diffs.add("APPROVED_DATE");
		if(!isSame(getApproverId(),o.getApproverId())) diffs.add("APPROVER_ID");
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
		if (isNull(patientId))
			 throw new Exception("PATIENT_ID is required.");
		if (isNull(start))
			 throw new Exception("START is required.");
		if (isNull(end))
			 throw new Exception("END is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if(assessmentEntrys != null){
			for(AssessmentEntry assessmentEntry:getAssessmentEntrys())
				assessmentEntry.setAppointment((Appointment)this);
		}
		if(assessmentEntrys != null){
			for(AssessmentEntry assessmentEntry:getAssessmentEntrys())
				if(assessmentEntry.isNewInstance())
					assessmentEntry.insert();
			assessmentEntrys = null;
		}
	}
}
