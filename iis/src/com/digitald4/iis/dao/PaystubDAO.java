package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Deduction;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Paystub;
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
public abstract class PaystubDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,NURSE_ID,STATUS_ID,NAME,PAY_DATE,GENERATION_TIME,LOGGED_HOURS,MILEAGE,PAY_MILEAGE,LOGGED_HOURS_Y_T_D,MILEAGE_Y_T_D,PAY_MILEAGE_Y_T_D,GROSS_PAY,PRE_TAX_DEDUCTION,TAXABLE,TAX_TOTAL,POST_TAX_DEDUCTION,NON_TAX_WAGES,NET_PAY,GROSS_PAY_Y_T_D,PRE_TAX_DEDUCTION_Y_T_D,TAXABLE_Y_T_D,TAX_TOTAL_Y_T_D,POST_TAX_DEDUCTION_Y_T_D,NON_TAX_WAGES_Y_T_D,NET_PAY_Y_T_D,COMMENT,DATA};
	private Integer id;
	private Integer nurseId;
	private Integer statusId;
	private String name;
	private Date payDate;
	private DateTime generationTime;
	private double loggedHours;
	private int mileage;
	private double payMileage;
	private double loggedHoursYTD;
	private int mileageYTD;
	private double payMileageYTD;
	private double grossPay;
	private double preTaxDeduction;
	private double taxable;
	private double taxTotal;
	private double postTaxDeduction;
	private double nonTaxWages;
	private double netPay;
	private double grossPayYTD;
	private double preTaxDeductionYTD;
	private double taxableYTD;
	private double taxTotalYTD;
	private double postTaxDeductionYTD;
	private double nonTaxWagesYTD;
	private double netPayYTD;
	private String comment;
	private byte[] data;
	private List<Appointment> appointments;
	private List<Deduction> deductions;
	private Nurse nurse;
	private GeneralData status;
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
		this.statusId=orig.getStatusId();
		this.name=orig.getName();
		this.payDate=orig.getPayDate();
		this.generationTime=orig.getGenerationTime();
		this.loggedHours=orig.getLoggedHours();
		this.mileage=orig.getMileage();
		this.payMileage=orig.getPayMileage();
		this.loggedHoursYTD=orig.getLoggedHoursYTD();
		this.mileageYTD=orig.getMileageYTD();
		this.payMileageYTD=orig.getPayMileageYTD();
		this.grossPay=orig.getGrossPay();
		this.preTaxDeduction=orig.getPreTaxDeduction();
		this.taxable=orig.getTaxable();
		this.taxTotal=orig.getTaxTotal();
		this.postTaxDeduction=orig.getPostTaxDeduction();
		this.nonTaxWages=orig.getNonTaxWages();
		this.netPay=orig.getNetPay();
		this.grossPayYTD=orig.getGrossPayYTD();
		this.preTaxDeductionYTD=orig.getPreTaxDeductionYTD();
		this.taxableYTD=orig.getTaxableYTD();
		this.taxTotalYTD=orig.getTaxTotalYTD();
		this.postTaxDeductionYTD=orig.getPostTaxDeductionYTD();
		this.nonTaxWagesYTD=orig.getNonTaxWagesYTD();
		this.netPayYTD=orig.getNetPayYTD();
		this.comment=orig.getComment();
		this.data=orig.getData();
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
	public Paystub setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="NURSE_ID",nullable=false)
	public Integer getNurseId(){
		return nurseId;
	}
	public Paystub setNurseId(Integer nurseId) throws Exception  {
		Integer oldValue = getNurseId();
		if (!isSame(nurseId, oldValue)) {
			this.nurseId = nurseId;
			setProperty("NURSE_ID", nurseId, oldValue);
			nurse=null;
		}
		return (Paystub)this;
	}
	@Column(name="STATUS_ID",nullable=false)
	public Integer getStatusId(){
		return statusId;
	}
	public Paystub setStatusId(Integer statusId) throws Exception  {
		Integer oldValue = getStatusId();
		if (!isSame(statusId, oldValue)) {
			this.statusId = statusId;
			setProperty("STATUS_ID", statusId, oldValue);
			status=null;
		}
		return (Paystub)this;
	}
	@Column(name="NAME",nullable=true,length=64)
	public String getName(){
		return name;
	}
	public Paystub setName(String name) throws Exception  {
		String oldValue = getName();
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="PAY_DATE",nullable=false)
	public Date getPayDate(){
		return payDate;
	}
	public Paystub setPayDate(Date payDate) throws Exception  {
		Date oldValue = getPayDate();
		if (!isSame(payDate, oldValue)) {
			this.payDate = payDate;
			setProperty("PAY_DATE", payDate, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="GENERATION_TIME",nullable=true)
	public DateTime getGenerationTime(){
		return generationTime;
	}
	public Paystub setGenerationTime(DateTime generationTime) throws Exception  {
		DateTime oldValue = getGenerationTime();
		if (!isSame(generationTime, oldValue)) {
			this.generationTime = generationTime;
			setProperty("GENERATION_TIME", generationTime, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="LOGGED_HOURS",nullable=true)
	public double getLoggedHours(){
		return loggedHours;
	}
	public Paystub setLoggedHours(double loggedHours) throws Exception  {
		double oldValue = getLoggedHours();
		if (!isSame(loggedHours, oldValue)) {
			this.loggedHours = loggedHours;
			setProperty("LOGGED_HOURS", loggedHours, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="MILEAGE",nullable=true)
	public int getMileage(){
		return mileage;
	}
	public Paystub setMileage(int mileage) throws Exception  {
		int oldValue = getMileage();
		if (!isSame(mileage, oldValue)) {
			this.mileage = mileage;
			setProperty("MILEAGE", mileage, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="PAY_MILEAGE",nullable=true)
	public double getPayMileage(){
		return payMileage;
	}
	public Paystub setPayMileage(double payMileage) throws Exception  {
		double oldValue = getPayMileage();
		if (!isSame(payMileage, oldValue)) {
			this.payMileage = payMileage;
			setProperty("PAY_MILEAGE", payMileage, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="LOGGED_HOURS_Y_T_D",nullable=true)
	public double getLoggedHoursYTD(){
		return loggedHoursYTD;
	}
	public Paystub setLoggedHoursYTD(double loggedHoursYTD) throws Exception  {
		double oldValue = getLoggedHoursYTD();
		if (!isSame(loggedHoursYTD, oldValue)) {
			this.loggedHoursYTD = loggedHoursYTD;
			setProperty("LOGGED_HOURS_Y_T_D", loggedHoursYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="MILEAGE_Y_T_D",nullable=true)
	public int getMileageYTD(){
		return mileageYTD;
	}
	public Paystub setMileageYTD(int mileageYTD) throws Exception  {
		int oldValue = getMileageYTD();
		if (!isSame(mileageYTD, oldValue)) {
			this.mileageYTD = mileageYTD;
			setProperty("MILEAGE_Y_T_D", mileageYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="PAY_MILEAGE_Y_T_D",nullable=true)
	public double getPayMileageYTD(){
		return payMileageYTD;
	}
	public Paystub setPayMileageYTD(double payMileageYTD) throws Exception  {
		double oldValue = getPayMileageYTD();
		if (!isSame(payMileageYTD, oldValue)) {
			this.payMileageYTD = payMileageYTD;
			setProperty("PAY_MILEAGE_Y_T_D", payMileageYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="GROSS_PAY",nullable=false)
	public double getGrossPay(){
		return grossPay;
	}
	public Paystub setGrossPay(double grossPay) throws Exception  {
		double oldValue = getGrossPay();
		if (!isSame(grossPay, oldValue)) {
			this.grossPay = grossPay;
			setProperty("GROSS_PAY", grossPay, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="PRE_TAX_DEDUCTION",nullable=true)
	public double getPreTaxDeduction(){
		return preTaxDeduction;
	}
	public Paystub setPreTaxDeduction(double preTaxDeduction) throws Exception  {
		double oldValue = getPreTaxDeduction();
		if (!isSame(preTaxDeduction, oldValue)) {
			this.preTaxDeduction = preTaxDeduction;
			setProperty("PRE_TAX_DEDUCTION", preTaxDeduction, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="TAXABLE",nullable=true)
	public double getTaxable(){
		return taxable;
	}
	public Paystub setTaxable(double taxable) throws Exception  {
		double oldValue = getTaxable();
		if (!isSame(taxable, oldValue)) {
			this.taxable = taxable;
			setProperty("TAXABLE", taxable, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="TAX_TOTAL",nullable=true)
	public double getTaxTotal(){
		return taxTotal;
	}
	public Paystub setTaxTotal(double taxTotal) throws Exception  {
		double oldValue = getTaxTotal();
		if (!isSame(taxTotal, oldValue)) {
			this.taxTotal = taxTotal;
			setProperty("TAX_TOTAL", taxTotal, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="POST_TAX_DEDUCTION",nullable=true)
	public double getPostTaxDeduction(){
		return postTaxDeduction;
	}
	public Paystub setPostTaxDeduction(double postTaxDeduction) throws Exception  {
		double oldValue = getPostTaxDeduction();
		if (!isSame(postTaxDeduction, oldValue)) {
			this.postTaxDeduction = postTaxDeduction;
			setProperty("POST_TAX_DEDUCTION", postTaxDeduction, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="NON_TAX_WAGES",nullable=true)
	public double getNonTaxWages(){
		return nonTaxWages;
	}
	public Paystub setNonTaxWages(double nonTaxWages) throws Exception  {
		double oldValue = getNonTaxWages();
		if (!isSame(nonTaxWages, oldValue)) {
			this.nonTaxWages = nonTaxWages;
			setProperty("NON_TAX_WAGES", nonTaxWages, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="NET_PAY",nullable=true)
	public double getNetPay(){
		return netPay;
	}
	public Paystub setNetPay(double netPay) throws Exception  {
		double oldValue = getNetPay();
		if (!isSame(netPay, oldValue)) {
			this.netPay = netPay;
			setProperty("NET_PAY", netPay, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="GROSS_PAY_Y_T_D",nullable=true)
	public double getGrossPayYTD(){
		return grossPayYTD;
	}
	public Paystub setGrossPayYTD(double grossPayYTD) throws Exception  {
		double oldValue = getGrossPayYTD();
		if (!isSame(grossPayYTD, oldValue)) {
			this.grossPayYTD = grossPayYTD;
			setProperty("GROSS_PAY_Y_T_D", grossPayYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="PRE_TAX_DEDUCTION_Y_T_D",nullable=true)
	public double getPreTaxDeductionYTD(){
		return preTaxDeductionYTD;
	}
	public Paystub setPreTaxDeductionYTD(double preTaxDeductionYTD) throws Exception  {
		double oldValue = getPreTaxDeductionYTD();
		if (!isSame(preTaxDeductionYTD, oldValue)) {
			this.preTaxDeductionYTD = preTaxDeductionYTD;
			setProperty("PRE_TAX_DEDUCTION_Y_T_D", preTaxDeductionYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="TAXABLE_Y_T_D",nullable=true)
	public double getTaxableYTD(){
		return taxableYTD;
	}
	public Paystub setTaxableYTD(double taxableYTD) throws Exception  {
		double oldValue = getTaxableYTD();
		if (!isSame(taxableYTD, oldValue)) {
			this.taxableYTD = taxableYTD;
			setProperty("TAXABLE_Y_T_D", taxableYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="TAX_TOTAL_Y_T_D",nullable=true)
	public double getTaxTotalYTD(){
		return taxTotalYTD;
	}
	public Paystub setTaxTotalYTD(double taxTotalYTD) throws Exception  {
		double oldValue = getTaxTotalYTD();
		if (!isSame(taxTotalYTD, oldValue)) {
			this.taxTotalYTD = taxTotalYTD;
			setProperty("TAX_TOTAL_Y_T_D", taxTotalYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="POST_TAX_DEDUCTION_Y_T_D",nullable=true)
	public double getPostTaxDeductionYTD(){
		return postTaxDeductionYTD;
	}
	public Paystub setPostTaxDeductionYTD(double postTaxDeductionYTD) throws Exception  {
		double oldValue = getPostTaxDeductionYTD();
		if (!isSame(postTaxDeductionYTD, oldValue)) {
			this.postTaxDeductionYTD = postTaxDeductionYTD;
			setProperty("POST_TAX_DEDUCTION_Y_T_D", postTaxDeductionYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="NON_TAX_WAGES_Y_T_D",nullable=true)
	public double getNonTaxWagesYTD(){
		return nonTaxWagesYTD;
	}
	public Paystub setNonTaxWagesYTD(double nonTaxWagesYTD) throws Exception  {
		double oldValue = getNonTaxWagesYTD();
		if (!isSame(nonTaxWagesYTD, oldValue)) {
			this.nonTaxWagesYTD = nonTaxWagesYTD;
			setProperty("NON_TAX_WAGES_Y_T_D", nonTaxWagesYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="NET_PAY_Y_T_D",nullable=true)
	public double getNetPayYTD(){
		return netPayYTD;
	}
	public Paystub setNetPayYTD(double netPayYTD) throws Exception  {
		double oldValue = getNetPayYTD();
		if (!isSame(netPayYTD, oldValue)) {
			this.netPayYTD = netPayYTD;
			setProperty("NET_PAY_Y_T_D", netPayYTD, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="COMMENT",nullable=true,length=1024)
	public String getComment(){
		return comment;
	}
	public Paystub setComment(String comment) throws Exception  {
		String oldValue = getComment();
		if (!isSame(comment, oldValue)) {
			this.comment = comment;
			setProperty("COMMENT", comment, oldValue);
		}
		return (Paystub)this;
	}
	@Column(name="DATA",nullable=true)
	public byte[] getData(){
		return data;
	}
	public Paystub setData(byte[] data) throws Exception  {
		byte[] oldValue = getData();
		if (!isSame(data, oldValue)) {
			this.data = data;
			setProperty("DATA", data, oldValue);
		}
		return (Paystub)this;
	}
	public Nurse getNurse(){
		if(nurse==null)
			nurse=Nurse.getInstance(getNurseId());
		return nurse;
	}
	public Paystub setNurse(Nurse nurse) throws Exception {
		setNurseId(nurse==null?null:nurse.getId());
		this.nurse=nurse;
		return (Paystub)this;
	}
	public GeneralData getStatus(){
		if(status==null)
			status=GeneralData.getInstance(getStatusId());
		return status;
	}
	public Paystub setStatus(GeneralData status) throws Exception {
		setStatusId(status==null?null:status.getId());
		this.status=status;
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
	public Paystub addAppointment(Appointment appointment) throws Exception {
		appointment.setPaystub((Paystub)this);
		if(isNewInstance() || appointments != null)
			getAppointments().add(appointment);
		else
			appointment.insert();
		return (Paystub)this;
	}
	public Paystub removeAppointment(Appointment appointment) throws Exception {
		if(isNewInstance() || appointments != null)
			getAppointments().remove(appointment);
		else
			appointment.delete();
		return (Paystub)this;
	}
	public List<Deduction> getDeductions(){
		if(isNewInstance() || deductions != null){
			if(deductions == null)
				deductions = new SortedList<Deduction>();
			return deductions;
		}
		return Deduction.getNamedCollection("findByPaystub",getId());
	}
	public Paystub addDeduction(Deduction deduction) throws Exception {
		deduction.setPaystub((Paystub)this);
		if(isNewInstance() || deductions != null)
			getDeductions().add(deduction);
		else
			deduction.insert();
		return (Paystub)this;
	}
	public Paystub removeDeduction(Deduction deduction) throws Exception {
		if(isNewInstance() || deductions != null)
			getDeductions().remove(deduction);
		else
			deduction.delete();
		return (Paystub)this;
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
			case NURSE_ID: return getNurseId();
			case STATUS_ID: return getStatusId();
			case NAME: return getName();
			case PAY_DATE: return getPayDate();
			case GENERATION_TIME: return getGenerationTime();
			case LOGGED_HOURS: return getLoggedHours();
			case MILEAGE: return getMileage();
			case PAY_MILEAGE: return getPayMileage();
			case LOGGED_HOURS_Y_T_D: return getLoggedHoursYTD();
			case MILEAGE_Y_T_D: return getMileageYTD();
			case PAY_MILEAGE_Y_T_D: return getPayMileageYTD();
			case GROSS_PAY: return getGrossPay();
			case PRE_TAX_DEDUCTION: return getPreTaxDeduction();
			case TAXABLE: return getTaxable();
			case TAX_TOTAL: return getTaxTotal();
			case POST_TAX_DEDUCTION: return getPostTaxDeduction();
			case NON_TAX_WAGES: return getNonTaxWages();
			case NET_PAY: return getNetPay();
			case GROSS_PAY_Y_T_D: return getGrossPayYTD();
			case PRE_TAX_DEDUCTION_Y_T_D: return getPreTaxDeductionYTD();
			case TAXABLE_Y_T_D: return getTaxableYTD();
			case TAX_TOTAL_Y_T_D: return getTaxTotalYTD();
			case POST_TAX_DEDUCTION_Y_T_D: return getPostTaxDeductionYTD();
			case NON_TAX_WAGES_Y_T_D: return getNonTaxWagesYTD();
			case NET_PAY_Y_T_D: return getNetPayYTD();
			case COMMENT: return getComment();
			case DATA: return getData();
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
			case NURSE_ID:setNurseId(Integer.valueOf(value)); break;
			case STATUS_ID:setStatusId(Integer.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case PAY_DATE:setPayDate(FormatText.parseDate(value)); break;
			case GENERATION_TIME:setGenerationTime(new DateTime(value)); break;
			case LOGGED_HOURS:setLoggedHours(Double.valueOf(value)); break;
			case MILEAGE:setMileage(Integer.valueOf(value)); break;
			case PAY_MILEAGE:setPayMileage(Double.valueOf(value)); break;
			case LOGGED_HOURS_Y_T_D:setLoggedHoursYTD(Double.valueOf(value)); break;
			case MILEAGE_Y_T_D:setMileageYTD(Integer.valueOf(value)); break;
			case PAY_MILEAGE_Y_T_D:setPayMileageYTD(Double.valueOf(value)); break;
			case GROSS_PAY:setGrossPay(Double.valueOf(value)); break;
			case PRE_TAX_DEDUCTION:setPreTaxDeduction(Double.valueOf(value)); break;
			case TAXABLE:setTaxable(Double.valueOf(value)); break;
			case TAX_TOTAL:setTaxTotal(Double.valueOf(value)); break;
			case POST_TAX_DEDUCTION:setPostTaxDeduction(Double.valueOf(value)); break;
			case NON_TAX_WAGES:setNonTaxWages(Double.valueOf(value)); break;
			case NET_PAY:setNetPay(Double.valueOf(value)); break;
			case GROSS_PAY_Y_T_D:setGrossPayYTD(Double.valueOf(value)); break;
			case PRE_TAX_DEDUCTION_Y_T_D:setPreTaxDeductionYTD(Double.valueOf(value)); break;
			case TAXABLE_Y_T_D:setTaxableYTD(Double.valueOf(value)); break;
			case TAX_TOTAL_Y_T_D:setTaxTotalYTD(Double.valueOf(value)); break;
			case POST_TAX_DEDUCTION_Y_T_D:setPostTaxDeductionYTD(Double.valueOf(value)); break;
			case NON_TAX_WAGES_Y_T_D:setNonTaxWagesYTD(Double.valueOf(value)); break;
			case NET_PAY_Y_T_D:setNetPayYTD(Double.valueOf(value)); break;
			case COMMENT:setComment(String.valueOf(value)); break;
		}
	}
	public Paystub copy() throws Exception {
		Paystub cp = new Paystub((Paystub)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(PaystubDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(Appointment child:getAppointments())
			cp.addAppointment(child.copy());
		for(Deduction child:getDeductions())
			cp.addDeduction(child.copy());
	}
	public Vector<String> getDifference(PaystubDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getNurseId(),o.getNurseId())) diffs.add("NURSE_ID");
		if(!isSame(getStatusId(),o.getStatusId())) diffs.add("STATUS_ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getPayDate(),o.getPayDate())) diffs.add("PAY_DATE");
		if(!isSame(getGenerationTime(),o.getGenerationTime())) diffs.add("GENERATION_TIME");
		if(!isSame(getLoggedHours(),o.getLoggedHours())) diffs.add("LOGGED_HOURS");
		if(!isSame(getMileage(),o.getMileage())) diffs.add("MILEAGE");
		if(!isSame(getPayMileage(),o.getPayMileage())) diffs.add("PAY_MILEAGE");
		if(!isSame(getLoggedHoursYTD(),o.getLoggedHoursYTD())) diffs.add("LOGGED_HOURS_Y_T_D");
		if(!isSame(getMileageYTD(),o.getMileageYTD())) diffs.add("MILEAGE_Y_T_D");
		if(!isSame(getPayMileageYTD(),o.getPayMileageYTD())) diffs.add("PAY_MILEAGE_Y_T_D");
		if(!isSame(getGrossPay(),o.getGrossPay())) diffs.add("GROSS_PAY");
		if(!isSame(getPreTaxDeduction(),o.getPreTaxDeduction())) diffs.add("PRE_TAX_DEDUCTION");
		if(!isSame(getTaxable(),o.getTaxable())) diffs.add("TAXABLE");
		if(!isSame(getTaxTotal(),o.getTaxTotal())) diffs.add("TAX_TOTAL");
		if(!isSame(getPostTaxDeduction(),o.getPostTaxDeduction())) diffs.add("POST_TAX_DEDUCTION");
		if(!isSame(getNonTaxWages(),o.getNonTaxWages())) diffs.add("NON_TAX_WAGES");
		if(!isSame(getNetPay(),o.getNetPay())) diffs.add("NET_PAY");
		if(!isSame(getGrossPayYTD(),o.getGrossPayYTD())) diffs.add("GROSS_PAY_Y_T_D");
		if(!isSame(getPreTaxDeductionYTD(),o.getPreTaxDeductionYTD())) diffs.add("PRE_TAX_DEDUCTION_Y_T_D");
		if(!isSame(getTaxableYTD(),o.getTaxableYTD())) diffs.add("TAXABLE_Y_T_D");
		if(!isSame(getTaxTotalYTD(),o.getTaxTotalYTD())) diffs.add("TAX_TOTAL_Y_T_D");
		if(!isSame(getPostTaxDeductionYTD(),o.getPostTaxDeductionYTD())) diffs.add("POST_TAX_DEDUCTION_Y_T_D");
		if(!isSame(getNonTaxWagesYTD(),o.getNonTaxWagesYTD())) diffs.add("NON_TAX_WAGES_Y_T_D");
		if(!isSame(getNetPayYTD(),o.getNetPayYTD())) diffs.add("NET_PAY_Y_T_D");
		if(!isSame(getComment(),o.getComment())) diffs.add("COMMENT");
		if(!isSame(getData(),o.getData())) diffs.add("DATA");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(nurse != null && nurse.isNewInstance())
				nurse.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getNurseId()))
			 throw new Exception("NURSE_ID is required.");
		if (isNull(getStatusId()))
			 throw new Exception("STATUS_ID is required.");
		if (isNull(getPayDate()))
			 throw new Exception("PAY_DATE is required.");
		if (isNull(getGrossPay()))
			 throw new Exception("GROSS_PAY is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.setPaystub((Paystub)this);
			}
		}
		if (deductions != null) {
			for (Deduction deduction : getDeductions()) {
				deduction.setPaystub((Paystub)this);
			}
		}
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.insert();
			}
			appointments = null;
		}
		if (deductions != null) {
			for (Deduction deduction : getDeductions()) {
				deduction.insert();
			}
			deductions = null;
		}
	}
}
