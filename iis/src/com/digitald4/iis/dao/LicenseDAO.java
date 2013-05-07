package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
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
public abstract class LicenseDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,NURSE_ID,LIC_TYPE_ID,NUMBER,VALID_DATE,EXPIRATION_DATE};
	private Integer id;
	private Integer nurseId;
	private Integer licTypeId;
	private String number;
	private Date validDate;
	private Date expirationDate;
	private GeneralData licType;
	private Nurse nurse;
	public static License getInstance(Integer id){
		return getInstance(id, true);
	}
	public static License getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		License o = null;
		if(fetch || cache != null && cache.contains(License.class, pk))
			o = em.find(License.class, pk);
		return o;
	}
	public static Collection<License> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<License> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<License> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM License o";
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
	public synchronized static Collection<License> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<License> tq = em.createQuery(jpql,License.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static Collection<License> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<License> tq = em.createNamedQuery(name,License.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public LicenseDAO(){}
	public LicenseDAO(Integer id){
		this.id=id;
	}
	public LicenseDAO(LicenseDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(LicenseDAO orig){
		this.nurseId=orig.getNurseId();
		this.licTypeId=orig.getLicTypeId();
		this.number=orig.getNumber();
		this.validDate=orig.getValidDate();
		this.expirationDate=orig.getExpirationDate();
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
	public License setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
		}
		return (License)this;
	}
	@Column(name="NURSE_ID",nullable=false)
	public Integer getNurseId(){
		return nurseId;
	}
	public License setNurseId(Integer nurseId)throws Exception{
		if(!isSame(nurseId, getNurseId())){
			Integer oldValue = getNurseId();
			this.nurseId=nurseId;
			setProperty("NURSE_ID", nurseId, oldValue);
			nurse=null;
		}
		return (License)this;
	}
	@Column(name="LIC_TYPE_ID",nullable=false)
	public Integer getLicTypeId(){
		return licTypeId;
	}
	public License setLicTypeId(Integer licTypeId)throws Exception{
		if(!isSame(licTypeId, getLicTypeId())){
			Integer oldValue = getLicTypeId();
			this.licTypeId=licTypeId;
			setProperty("LIC_TYPE_ID", licTypeId, oldValue);
			licType=null;
		}
		return (License)this;
	}
	@Column(name="NUMBER",nullable=true,length=32)
	public String getNumber(){
		return number;
	}
	public License setNumber(String number)throws Exception{
		if(!isSame(number, getNumber())){
			String oldValue = getNumber();
			this.number=number;
			setProperty("NUMBER", number, oldValue);
		}
		return (License)this;
	}
	@Column(name="VALID_DATE",nullable=true)
	public Date getValidDate(){
		return validDate;
	}
	public License setValidDate(Date validDate)throws Exception{
		if(!isSame(validDate, getValidDate())){
			Date oldValue = getValidDate();
			this.validDate=validDate;
			setProperty("VALID_DATE", validDate, oldValue);
		}
		return (License)this;
	}
	@Column(name="EXPIRATION_DATE",nullable=true)
	public Date getExpirationDate(){
		return expirationDate;
	}
	public License setExpirationDate(Date expirationDate)throws Exception{
		if(!isSame(expirationDate, getExpirationDate())){
			Date oldValue = getExpirationDate();
			this.expirationDate=expirationDate;
			setProperty("EXPIRATION_DATE", expirationDate, oldValue);
		}
		return (License)this;
	}
	public GeneralData getLicType(){
		if(licType==null)
			licType=GeneralData.getInstance(getLicTypeId());
		return licType;
	}
	public License setLicType(GeneralData licType)throws Exception{
		setLicTypeId(licType==null?null:licType.getId());
		this.licType=licType;
		return (License)this;
	}
	public Nurse getNurse(){
		if(nurse==null)
			nurse=Nurse.getInstance(getNurseId());
		return nurse;
	}
	public License setNurse(Nurse nurse)throws Exception{
		setNurseId(nurse==null?null:nurse.getId());
		this.nurse=nurse;
		return (License)this;
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
			case NURSE_ID: return getNurseId();
			case LIC_TYPE_ID: return getLicTypeId();
			case NUMBER: return getNumber();
			case VALID_DATE: return getValidDate();
			case EXPIRATION_DATE: return getExpirationDate();
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
			case NURSE_ID:setNurseId(Integer.valueOf(value)); break;
			case LIC_TYPE_ID:setLicTypeId(Integer.valueOf(value)); break;
			case NUMBER:setNumber(String.valueOf(value)); break;
			case VALID_DATE:setValidDate(FormatText.parseDate(value)); break;
			case EXPIRATION_DATE:setExpirationDate(FormatText.parseDate(value)); break;
		}
	}
	public License copy()throws Exception{
		License cp = new License((License)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(LicenseDAO cp)throws Exception{
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(LicenseDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getNurseId(),o.getNurseId())) diffs.add("NURSE_ID");
		if(!isSame(getLicTypeId(),o.getLicTypeId())) diffs.add("LIC_TYPE_ID");
		if(!isSame(getNumber(),o.getNumber())) diffs.add("NUMBER");
		if(!isSame(getValidDate(),o.getValidDate())) diffs.add("VALID_DATE");
		if(!isSame(getExpirationDate(),o.getExpirationDate())) diffs.add("EXPIRATION_DATE");
		return diffs;
	}
	public void insertParents()throws Exception{
		if(licType != null && licType.isNewInstance())
				licType.insert();
		if(nurse != null && nurse.isNewInstance())
				nurse.insert();
	}
	public void insertPreCheck()throws Exception{
		if (isNull(nurseId))
			 throw new Exception("NURSE_ID is required.");
		if (isNull(licTypeId))
			 throw new Exception("LIC_TYPE_ID is required.");
	}
	public void insertChildren()throws Exception{
	}
}
