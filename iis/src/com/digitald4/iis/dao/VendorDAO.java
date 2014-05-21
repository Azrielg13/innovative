package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
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
public abstract class VendorDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,NAME,ADDRESS,ADDR_UNIT,LATITUDE,LONGITUDE,PHONE_NUMBER,FAX_NUMBER,CONTACT_NAME,CONTACT_NUMBER,CONTACT_EMAIL,ACTIVE,BILLING_RATE,BILLING_RATE_2HR_SOC,BILLING_RATE_2HR_ROC,BILLING_FLAT,BILLING_FLAT_2HR_SOC,BILLING_FLAT_2HR_ROC,MILEAGE_RATE,NOTES};
	private Integer id;
	private String name;
	private String address;
	private String addrUnit;
	private double latitude;
	private double longitude;
	private String phoneNumber;
	private String faxNumber;
	private String contactName;
	private String contactNumber;
	private String contactEmail;
	private boolean active = true;
	private double billingRate;
	private double billingRate2HrSoc;
	private double billingRate2HrRoc;
	private double billingFlat;
	private double billingFlat2HrSoc;
	private double billingFlat2HrRoc;
	private double mileageRate;
	private String notes;
	private List<Invoice> invoices;
	private List<Patient> patients;
	public static Vendor getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Vendor getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Vendor o = null;
		if(fetch || cache != null && cache.contains(Vendor.class, pk))
			o = em.find(Vendor.class, pk);
		return o;
	}
	public static List<Vendor> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Vendor> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Vendor> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Vendor o";
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
	public synchronized static List<Vendor> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Vendor> tq = em.createQuery(jpql,Vendor.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Vendor> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Vendor> tq = em.createNamedQuery(name,Vendor.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public VendorDAO(){}
	public VendorDAO(Integer id){
		this.id=id;
	}
	public VendorDAO(VendorDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(VendorDAO orig){
		this.name=orig.getName();
		this.address=orig.getAddress();
		this.addrUnit=orig.getAddrUnit();
		this.latitude=orig.getLatitude();
		this.longitude=orig.getLongitude();
		this.phoneNumber=orig.getPhoneNumber();
		this.faxNumber=orig.getFaxNumber();
		this.contactName=orig.getContactName();
		this.contactNumber=orig.getContactNumber();
		this.contactEmail=orig.getContactEmail();
		this.active=orig.isActive();
		this.billingRate=orig.getBillingRate();
		this.billingRate2HrSoc=orig.getBillingRate2HrSoc();
		this.billingRate2HrRoc=orig.getBillingRate2HrRoc();
		this.billingFlat=orig.getBillingFlat();
		this.billingFlat2HrSoc=orig.getBillingFlat2HrSoc();
		this.billingFlat2HrRoc=orig.getBillingFlat2HrRoc();
		this.mileageRate=orig.getMileageRate();
		this.notes=orig.getNotes();
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
	public Vendor setId(Integer id) throws Exception  {
		Object oldValue = null;
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public Vendor setName(String name) throws Exception  {
		Object oldValue = null;
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="ADDRESS",nullable=true,length=100)
	public String getAddress(){
		return address;
	}
	public Vendor setAddress(String address) throws Exception  {
		Object oldValue = null;
		if (!isSame(address, oldValue)) {
			this.address = address;
			setProperty("ADDRESS", address, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="ADDR_UNIT",nullable=true,length=20)
	public String getAddrUnit(){
		return addrUnit;
	}
	public Vendor setAddrUnit(String addrUnit) throws Exception  {
		Object oldValue = null;
		if (!isSame(addrUnit, oldValue)) {
			this.addrUnit = addrUnit;
			setProperty("ADDR_UNIT", addrUnit, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="LATITUDE",nullable=true)
	public double getLatitude(){
		return latitude;
	}
	public Vendor setLatitude(double latitude) throws Exception  {
		Object oldValue = null;
		if (!isSame(latitude, oldValue)) {
			this.latitude = latitude;
			setProperty("LATITUDE", latitude, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="LONGITUDE",nullable=true)
	public double getLongitude(){
		return longitude;
	}
	public Vendor setLongitude(double longitude) throws Exception  {
		Object oldValue = null;
		if (!isSame(longitude, oldValue)) {
			this.longitude = longitude;
			setProperty("LONGITUDE", longitude, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="PHONE_NUMBER",nullable=true,length=20)
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public Vendor setPhoneNumber(String phoneNumber) throws Exception  {
		Object oldValue = null;
		if (!isSame(phoneNumber, oldValue)) {
			this.phoneNumber = phoneNumber;
			setProperty("PHONE_NUMBER", phoneNumber, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="FAX_NUMBER",nullable=true,length=20)
	public String getFaxNumber(){
		return faxNumber;
	}
	public Vendor setFaxNumber(String faxNumber) throws Exception  {
		Object oldValue = null;
		if (!isSame(faxNumber, oldValue)) {
			this.faxNumber = faxNumber;
			setProperty("FAX_NUMBER", faxNumber, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="CONTACT_NAME",nullable=true,length=64)
	public String getContactName(){
		return contactName;
	}
	public Vendor setContactName(String contactName) throws Exception  {
		Object oldValue = null;
		if (!isSame(contactName, oldValue)) {
			this.contactName = contactName;
			setProperty("CONTACT_NAME", contactName, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="CONTACT_NUMBER",nullable=true,length=20)
	public String getContactNumber(){
		return contactNumber;
	}
	public Vendor setContactNumber(String contactNumber) throws Exception  {
		Object oldValue = null;
		if (!isSame(contactNumber, oldValue)) {
			this.contactNumber = contactNumber;
			setProperty("CONTACT_NUMBER", contactNumber, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="CONTACT_EMAIL",nullable=true,length=32)
	public String getContactEmail(){
		return contactEmail;
	}
	public Vendor setContactEmail(String contactEmail) throws Exception  {
		Object oldValue = null;
		if (!isSame(contactEmail, oldValue)) {
			this.contactEmail = contactEmail;
			setProperty("CONTACT_EMAIL", contactEmail, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public Vendor setActive(boolean active) throws Exception  {
		Object oldValue = null;
		if (!isSame(active, oldValue)) {
			this.active = active;
			setProperty("ACTIVE", active, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="BILLING_RATE",nullable=true)
	public double getBillingRate(){
		return billingRate;
	}
	public Vendor setBillingRate(double billingRate) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingRate, oldValue)) {
			this.billingRate = billingRate;
			setProperty("BILLING_RATE", billingRate, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="BILLING_RATE_2HR_SOC",nullable=true)
	public double getBillingRate2HrSoc(){
		return billingRate2HrSoc;
	}
	public Vendor setBillingRate2HrSoc(double billingRate2HrSoc) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingRate2HrSoc, oldValue)) {
			this.billingRate2HrSoc = billingRate2HrSoc;
			setProperty("BILLING_RATE_2HR_SOC", billingRate2HrSoc, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="BILLING_RATE_2HR_ROC",nullable=true)
	public double getBillingRate2HrRoc(){
		return billingRate2HrRoc;
	}
	public Vendor setBillingRate2HrRoc(double billingRate2HrRoc) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingRate2HrRoc, oldValue)) {
			this.billingRate2HrRoc = billingRate2HrRoc;
			setProperty("BILLING_RATE_2HR_ROC", billingRate2HrRoc, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="BILLING_FLAT",nullable=true)
	public double getBillingFlat(){
		return billingFlat;
	}
	public Vendor setBillingFlat(double billingFlat) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingFlat, oldValue)) {
			this.billingFlat = billingFlat;
			setProperty("BILLING_FLAT", billingFlat, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="BILLING_FLAT_2HR_SOC",nullable=true)
	public double getBillingFlat2HrSoc(){
		return billingFlat2HrSoc;
	}
	public Vendor setBillingFlat2HrSoc(double billingFlat2HrSoc) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingFlat2HrSoc, oldValue)) {
			this.billingFlat2HrSoc = billingFlat2HrSoc;
			setProperty("BILLING_FLAT_2HR_SOC", billingFlat2HrSoc, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="BILLING_FLAT_2HR_ROC",nullable=true)
	public double getBillingFlat2HrRoc(){
		return billingFlat2HrRoc;
	}
	public Vendor setBillingFlat2HrRoc(double billingFlat2HrRoc) throws Exception  {
		Object oldValue = null;
		if (!isSame(billingFlat2HrRoc, oldValue)) {
			this.billingFlat2HrRoc = billingFlat2HrRoc;
			setProperty("BILLING_FLAT_2HR_ROC", billingFlat2HrRoc, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="MILEAGE_RATE",nullable=true)
	public double getMileageRate(){
		return mileageRate;
	}
	public Vendor setMileageRate(double mileageRate) throws Exception  {
		Object oldValue = null;
		if (!isSame(mileageRate, oldValue)) {
			this.mileageRate = mileageRate;
			setProperty("MILEAGE_RATE", mileageRate, oldValue);
		}
		return (Vendor)this;
	}
	@Column(name="NOTES",nullable=true,length=256)
	public String getNotes(){
		return notes;
	}
	public Vendor setNotes(String notes) throws Exception  {
		Object oldValue = null;
		if (!isSame(notes, oldValue)) {
			this.notes = notes;
			setProperty("NOTES", notes, oldValue);
		}
		return (Vendor)this;
	}
	public List<Invoice> getInvoices(){
		if(isNewInstance() || invoices != null){
			if(invoices == null)
				invoices = new SortedList<Invoice>();
			return invoices;
		}
		return Invoice.getNamedCollection("findByVendor",getId());
	}
	public Vendor addInvoice(Invoice invoice) throws Exception {
		invoice.setVendor((Vendor)this);
		if(isNewInstance() || invoices != null)
			getInvoices().add(invoice);
		else
			invoice.insert();
		return (Vendor)this;
	}
	public Vendor removeInvoice(Invoice invoice) throws Exception {
		if(isNewInstance() || invoices != null)
			getInvoices().remove(invoice);
		else
			invoice.delete();
		return (Vendor)this;
	}
	public List<Patient> getPatients(){
		if(isNewInstance() || patients != null){
			if(patients == null)
				patients = new SortedList<Patient>();
			return patients;
		}
		return Patient.getNamedCollection("findByVendor",getId());
	}
	public Vendor addPatient(Patient patient) throws Exception {
		patient.setVendor((Vendor)this);
		if(isNewInstance() || patients != null)
			getPatients().add(patient);
		else
			patient.insert();
		return (Vendor)this;
	}
	public Vendor removePatient(Patient patient) throws Exception {
		if(isNewInstance() || patients != null)
			getPatients().remove(patient);
		else
			patient.delete();
		return (Vendor)this;
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
			case NAME: return getName();
			case ADDRESS: return getAddress();
			case ADDR_UNIT: return getAddrUnit();
			case LATITUDE: return getLatitude();
			case LONGITUDE: return getLongitude();
			case PHONE_NUMBER: return getPhoneNumber();
			case FAX_NUMBER: return getFaxNumber();
			case CONTACT_NAME: return getContactName();
			case CONTACT_NUMBER: return getContactNumber();
			case CONTACT_EMAIL: return getContactEmail();
			case ACTIVE: return isActive();
			case BILLING_RATE: return getBillingRate();
			case BILLING_RATE_2HR_SOC: return getBillingRate2HrSoc();
			case BILLING_RATE_2HR_ROC: return getBillingRate2HrRoc();
			case BILLING_FLAT: return getBillingFlat();
			case BILLING_FLAT_2HR_SOC: return getBillingFlat2HrSoc();
			case BILLING_FLAT_2HR_ROC: return getBillingFlat2HrRoc();
			case MILEAGE_RATE: return getMileageRate();
			case NOTES: return getNotes();
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
			case NAME:setName(String.valueOf(value)); break;
			case ADDRESS:setAddress(String.valueOf(value)); break;
			case ADDR_UNIT:setAddrUnit(String.valueOf(value)); break;
			case LATITUDE:setLatitude(Double.valueOf(value)); break;
			case LONGITUDE:setLongitude(Double.valueOf(value)); break;
			case PHONE_NUMBER:setPhoneNumber(String.valueOf(value)); break;
			case FAX_NUMBER:setFaxNumber(String.valueOf(value)); break;
			case CONTACT_NAME:setContactName(String.valueOf(value)); break;
			case CONTACT_NUMBER:setContactNumber(String.valueOf(value)); break;
			case CONTACT_EMAIL:setContactEmail(String.valueOf(value)); break;
			case ACTIVE:setActive(Boolean.valueOf(value)); break;
			case BILLING_RATE:setBillingRate(Double.valueOf(value)); break;
			case BILLING_RATE_2HR_SOC:setBillingRate2HrSoc(Double.valueOf(value)); break;
			case BILLING_RATE_2HR_ROC:setBillingRate2HrRoc(Double.valueOf(value)); break;
			case BILLING_FLAT:setBillingFlat(Double.valueOf(value)); break;
			case BILLING_FLAT_2HR_SOC:setBillingFlat2HrSoc(Double.valueOf(value)); break;
			case BILLING_FLAT_2HR_ROC:setBillingFlat2HrRoc(Double.valueOf(value)); break;
			case MILEAGE_RATE:setMileageRate(Double.valueOf(value)); break;
			case NOTES:setNotes(String.valueOf(value)); break;
		}
	}
	public Vendor copy() throws Exception {
		Vendor cp = new Vendor((Vendor)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(VendorDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(Invoice child:getInvoices())
			cp.addInvoice(child.copy());
		for(Patient child:getPatients())
			cp.addPatient(child.copy());
	}
	public Vector<String> getDifference(VendorDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getAddress(),o.getAddress())) diffs.add("ADDRESS");
		if(!isSame(getAddrUnit(),o.getAddrUnit())) diffs.add("ADDR_UNIT");
		if(!isSame(getLatitude(),o.getLatitude())) diffs.add("LATITUDE");
		if(!isSame(getLongitude(),o.getLongitude())) diffs.add("LONGITUDE");
		if(!isSame(getPhoneNumber(),o.getPhoneNumber())) diffs.add("PHONE_NUMBER");
		if(!isSame(getFaxNumber(),o.getFaxNumber())) diffs.add("FAX_NUMBER");
		if(!isSame(getContactName(),o.getContactName())) diffs.add("CONTACT_NAME");
		if(!isSame(getContactNumber(),o.getContactNumber())) diffs.add("CONTACT_NUMBER");
		if(!isSame(getContactEmail(),o.getContactEmail())) diffs.add("CONTACT_EMAIL");
		if(!isSame(isActive(),o.isActive())) diffs.add("ACTIVE");
		if(!isSame(getBillingRate(),o.getBillingRate())) diffs.add("BILLING_RATE");
		if(!isSame(getBillingRate2HrSoc(),o.getBillingRate2HrSoc())) diffs.add("BILLING_RATE_2HR_SOC");
		if(!isSame(getBillingRate2HrRoc(),o.getBillingRate2HrRoc())) diffs.add("BILLING_RATE_2HR_ROC");
		if(!isSame(getBillingFlat(),o.getBillingFlat())) diffs.add("BILLING_FLAT");
		if(!isSame(getBillingFlat2HrSoc(),o.getBillingFlat2HrSoc())) diffs.add("BILLING_FLAT_2HR_SOC");
		if(!isSame(getBillingFlat2HrRoc(),o.getBillingFlat2HrRoc())) diffs.add("BILLING_FLAT_2HR_ROC");
		if(!isSame(getMileageRate(),o.getMileageRate())) diffs.add("MILEAGE_RATE");
		if(!isSame(getNotes(),o.getNotes())) diffs.add("NOTES");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getName()))
			 throw new Exception("NAME is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (invoices != null) {
			for (Invoice invoice : getInvoices()) {
				invoice.setVendor((Vendor)this);
			}
		}
		if (patients != null) {
			for (Patient patient : getPatients()) {
				patient.setVendor((Vendor)this);
			}
		}
		if (invoices != null) {
			for (Invoice invoice : getInvoices()) {
				invoice.insert();
			}
			invoices = null;
		}
		if (patients != null) {
			for (Patient patient : getPatients()) {
				patient.insert();
			}
			patients = null;
		}
	}
}
