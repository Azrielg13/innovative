package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import java.sql.Date;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
public abstract class LicenseDAO extends DataAccessObject{
	public static enum KEY_PROPERTY{ID};
	public static enum PROPERTY{ID,NURSE_ID,LIC_TYPE_ID,EFFECTIVE_DATE,NUMBER,VALID_DATE,EXPIRATION_DATE};
	private Integer id;
	private Integer nurseId;
	private Integer licTypeId;
	private Date effectiveDate;
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
		if(cache != null && cache.contains(License.class, pk))
			o = em.find(License.class, pk);
		if(o==null && fetch)
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
		this.id=orig.getId();
		copyFrom(orig);
	}
	public void copyFrom(LicenseDAO orig){
		this.nurseId=orig.getNurseId();
		this.licTypeId=orig.getLicTypeId();
		this.effectiveDate=orig.getEffectiveDate();
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
	@Column(name="NURSE_ID",nullable=false)
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
	@Column(name="LIC_TYPE_ID",nullable=false)
	public Integer getLicTypeId(){
		return licTypeId;
	}
	public void setLicTypeId(Integer licTypeId){
		if(isSame(licTypeId, getLicTypeId()))return;
		Integer oldValue = getLicTypeId();
		this.licTypeId=licTypeId;
		setProperty("LIC_TYPE_ID", licTypeId, oldValue);
		licType=null;
	}
	@Column(name="EFFECTIVE_DATE",nullable=false)
	public Date getEffectiveDate(){
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate){
		if(isSame(effectiveDate, getEffectiveDate()))return;
		Date oldValue = getEffectiveDate();
		this.effectiveDate=effectiveDate;
		setProperty("EFFECTIVE_DATE", effectiveDate, oldValue);
	}
	@Column(name="NUMBER",nullable=false,length=32)
	public String getNumber(){
		return number;
	}
	public void setNumber(String number){
		if(isSame(number, getNumber()))return;
		String oldValue = getNumber();
		this.number=number;
		setProperty("NUMBER", number, oldValue);
	}
	@Column(name="VALID_DATE",nullable=true)
	public Date getValidDate(){
		return validDate;
	}
	public void setValidDate(Date validDate){
		if(isSame(validDate, getValidDate()))return;
		Date oldValue = getValidDate();
		this.validDate=validDate;
		setProperty("VALID_DATE", validDate, oldValue);
	}
	@Column(name="EXPIRATION_DATE",nullable=false)
	public Date getExpirationDate(){
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate){
		if(isSame(expirationDate, getExpirationDate()))return;
		Date oldValue = getExpirationDate();
		this.expirationDate=expirationDate;
		setProperty("EXPIRATION_DATE", expirationDate, oldValue);
	}
	public GeneralData getLicType(){
		if(licType==null)
			licType=GeneralData.getInstance(getLicTypeId());
		return licType;
	}
	public void setLicType(GeneralData licType){
		setLicTypeId(licType==null?0:licType.getId());
		this.licType=licType;
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
	public License copy(){
		License cp = new License((License)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(LicenseDAO cp){
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(LicenseDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getNurseId(),o.getNurseId())) diffs.add("NURSE_ID");
		if(!isSame(getLicTypeId(),o.getLicTypeId())) diffs.add("LIC_TYPE_ID");
		if(!isSame(getEffectiveDate(),o.getEffectiveDate())) diffs.add("EFFECTIVE_DATE");
		if(!isSame(getNumber(),o.getNumber())) diffs.add("NUMBER");
		if(!isSame(getValidDate(),o.getValidDate())) diffs.add("VALID_DATE");
		if(!isSame(getExpirationDate(),o.getExpirationDate())) diffs.add("EXPIRATION_DATE");
		return diffs;
	}
	public void insertParents(){
		if(licType != null && licType.isNewInstance())
				licType.insert();
		if(nurse != null && nurse.isNewInstance())
				nurse.insert();
	}
	public void insertChildren(){
	}
}
