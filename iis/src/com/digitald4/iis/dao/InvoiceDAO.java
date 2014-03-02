package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.SortedList;
import com.digitald4.iis.model.Appointment;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Invoice;
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
import org.joda.time.DateTime;
public abstract class InvoiceDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,VENDOR_ID,STATUS_ID,NAME,GENERATION_TIME,TOTAL_DUE,TOTAL_PAID,COMMENT,DATA};
	private Integer id;
	private Integer vendorId;
	private Integer statusId;
	private String name;
	private DateTime generationTime;
	private double totalDue;
	private double totalPaid;
	private String comment;
	private byte[] data;
	private List<Appointment> appointments;
	private GeneralData status;
	private Vendor vendor;
	public static Invoice getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Invoice getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Invoice o = null;
		if(fetch || cache != null && cache.contains(Invoice.class, pk))
			o = em.find(Invoice.class, pk);
		return o;
	}
	public static List<Invoice> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Invoice> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Invoice> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Invoice o";
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
	public synchronized static List<Invoice> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Invoice> tq = em.createQuery(jpql,Invoice.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Invoice> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Invoice> tq = em.createNamedQuery(name,Invoice.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public InvoiceDAO(){}
	public InvoiceDAO(Integer id){
		this.id=id;
	}
	public InvoiceDAO(InvoiceDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(InvoiceDAO orig){
		this.vendorId=orig.getVendorId();
		this.statusId=orig.getStatusId();
		this.name=orig.getName();
		this.generationTime=orig.getGenerationTime();
		this.totalDue=orig.getTotalDue();
		this.totalPaid=orig.getTotalPaid();
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
	public Invoice setId(Integer id) throws Exception  {
		Object oldValue = null;
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Invoice)this;
	}
	@Column(name="VENDOR_ID",nullable=false)
	public Integer getVendorId(){
		return vendorId;
	}
	public Invoice setVendorId(Integer vendorId) throws Exception  {
		Object oldValue = null;
		if (!isSame(vendorId, oldValue)) {
			this.vendorId = vendorId;
			setProperty("VENDOR_ID", vendorId, oldValue);
			vendor=null;
		}
		return (Invoice)this;
	}
	@Column(name="STATUS_ID",nullable=false)
	public Integer getStatusId(){
		return statusId;
	}
	public Invoice setStatusId(Integer statusId) throws Exception  {
		Object oldValue = null;
		if (!isSame(statusId, oldValue)) {
			this.statusId = statusId;
			setProperty("STATUS_ID", statusId, oldValue);
			status=null;
		}
		return (Invoice)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public Invoice setName(String name) throws Exception  {
		Object oldValue = null;
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (Invoice)this;
	}
	@Column(name="GENERATION_TIME",nullable=true)
	public DateTime getGenerationTime(){
		return generationTime;
	}
	public Invoice setGenerationTime(DateTime generationTime) throws Exception  {
		Object oldValue = null;
		if (!isSame(generationTime, oldValue)) {
			this.generationTime = generationTime;
			setProperty("GENERATION_TIME", generationTime, oldValue);
		}
		return (Invoice)this;
	}
	@Column(name="TOTAL_DUE",nullable=false)
	public double getTotalDue(){
		return totalDue;
	}
	public Invoice setTotalDue(double totalDue) throws Exception  {
		Object oldValue = null;
		if (!isSame(totalDue, oldValue)) {
			this.totalDue = totalDue;
			setProperty("TOTAL_DUE", totalDue, oldValue);
		}
		return (Invoice)this;
	}
	@Column(name="TOTAL_PAID",nullable=true)
	public double getTotalPaid(){
		return totalPaid;
	}
	public Invoice setTotalPaid(double totalPaid) throws Exception  {
		Object oldValue = null;
		if (!isSame(totalPaid, oldValue)) {
			this.totalPaid = totalPaid;
			setProperty("TOTAL_PAID", totalPaid, oldValue);
		}
		return (Invoice)this;
	}
	@Column(name="COMMENT",nullable=true,length=1024)
	public String getComment(){
		return comment;
	}
	public Invoice setComment(String comment) throws Exception  {
		Object oldValue = null;
		if (!isSame(comment, oldValue)) {
			this.comment = comment;
			setProperty("COMMENT", comment, oldValue);
		}
		return (Invoice)this;
	}
	@Column(name="DATA",nullable=true)
	public byte[] getData(){
		return data;
	}
	public Invoice setData(byte[] data) throws Exception  {
		Object oldValue = null;
		if (!isSame(data, oldValue)) {
			this.data = data;
			setProperty("DATA", data, oldValue);
		}
		return (Invoice)this;
	}
	public GeneralData getStatus(){
		if(status==null)
			status=GeneralData.getInstance(getStatusId());
		return status;
	}
	public Invoice setStatus(GeneralData status) throws Exception {
		setStatusId(status==null?null:status.getId());
		this.status=status;
		return (Invoice)this;
	}
	public Vendor getVendor(){
		if(vendor==null)
			vendor=Vendor.getInstance(getVendorId());
		return vendor;
	}
	public Invoice setVendor(Vendor vendor) throws Exception {
		setVendorId(vendor==null?null:vendor.getId());
		this.vendor=vendor;
		return (Invoice)this;
	}
	public List<Appointment> getAppointments(){
		if(isNewInstance() || appointments != null){
			if(appointments == null)
				appointments = new SortedList<Appointment>();
			return appointments;
		}
		return Appointment.getNamedCollection("findByInvoice",getId());
	}
	public Invoice addAppointment(Appointment appointment) throws Exception {
		appointment.setInvoice((Invoice)this);
		if(isNewInstance() || appointments != null)
			getAppointments().add(appointment);
		else
			appointment.insert();
		return (Invoice)this;
	}
	public Invoice removeAppointment(Appointment appointment) throws Exception {
		if(isNewInstance() || appointments != null)
			getAppointments().remove(appointment);
		else
			appointment.delete();
		return (Invoice)this;
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
			case VENDOR_ID: return getVendorId();
			case STATUS_ID: return getStatusId();
			case NAME: return getName();
			case GENERATION_TIME: return getGenerationTime();
			case TOTAL_DUE: return getTotalDue();
			case TOTAL_PAID: return getTotalPaid();
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
			case VENDOR_ID:setVendorId(Integer.valueOf(value)); break;
			case STATUS_ID:setStatusId(Integer.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case GENERATION_TIME:setGenerationTime(new DateTime(value)); break;
			case TOTAL_DUE:setTotalDue(Double.valueOf(value)); break;
			case TOTAL_PAID:setTotalPaid(Double.valueOf(value)); break;
			case COMMENT:setComment(String.valueOf(value)); break;
		}
	}
	public Invoice copy() throws Exception {
		Invoice cp = new Invoice((Invoice)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(InvoiceDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(Appointment child:getAppointments())
			cp.addAppointment(child.copy());
	}
	public Vector<String> getDifference(InvoiceDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getVendorId(),o.getVendorId())) diffs.add("VENDOR_ID");
		if(!isSame(getStatusId(),o.getStatusId())) diffs.add("STATUS_ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getGenerationTime(),o.getGenerationTime())) diffs.add("GENERATION_TIME");
		if(!isSame(getTotalDue(),o.getTotalDue())) diffs.add("TOTAL_DUE");
		if(!isSame(getTotalPaid(),o.getTotalPaid())) diffs.add("TOTAL_PAID");
		if(!isSame(getComment(),o.getComment())) diffs.add("COMMENT");
		if(!isSame(getData(),o.getData())) diffs.add("DATA");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(vendor != null && vendor.isNewInstance())
				vendor.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getVendorId()))
			 throw new Exception("VENDOR_ID is required.");
		if (isNull(getStatusId()))
			 throw new Exception("STATUS_ID is required.");
		if (isNull(getName()))
			 throw new Exception("NAME is required.");
		if (isNull(getTotalDue()))
			 throw new Exception("TOTAL_DUE is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (appointments != null) {
			for (Appointment appointment : getAppointments()) {
				appointment.setInvoice((Invoice)this);
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
