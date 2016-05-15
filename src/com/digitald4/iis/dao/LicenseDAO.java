package com.digitald4.iis.dao;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** TODO Copy Right*/
/**Description of class, (we need to get this from somewhere, database? xml?)*/
public abstract class LicenseDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,NURSE_ID,LIC_TYPE_ID,NUMBER,VALID_DATE,EXPIRATION_DATE,DATA_FILE_ID};
	private Integer id;
	private Integer nurseId;
	private Integer licTypeId;
	private String number;
	private Date validDate;
	private Date expirationDate;
	private Integer dataFileId;
	private DataFile dataFile;
	private GeneralData licType;
	private Nurse nurse;
	public LicenseDAO(EntityManager entityManager) {
		super(entityManager);
	}
	public LicenseDAO(EntityManager entityManager, Integer id) {
		super(entityManager);
		this.id=id;
	}
	public LicenseDAO(EntityManager entityManager, LicenseDAO orig) {
		super(entityManager, orig);
		copyFrom(orig);
	}
	public void copyFrom(LicenseDAO orig){
		this.nurseId = orig.getNurseId();
		this.licTypeId = orig.getLicTypeId();
		this.number = orig.getNumber();
		this.validDate = orig.getValidDate();
		this.expirationDate = orig.getExpirationDate();
		this.dataFileId = orig.getDataFileId();
	}
	@Override
	public String getHashKey() {
		return getHashKey(getKeyValues());
	}
	public Object[] getKeyValues() {
		return new Object[]{id};
	}
	@Override
	public int hashCode() {
		return PrimaryKey.hashCode(getKeyValues());
	}
	@Id
	@GeneratedValue
	@Column(name="ID",nullable=false)
	public Integer getId(){
		return id;
	}
	public License setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (License)this;
	}
	@Column(name="NURSE_ID",nullable=false)
	public Integer getNurseId(){
		return nurseId;
	}
	public License setNurseId(Integer nurseId) throws Exception  {
		Integer oldValue = getNurseId();
		if (!isSame(nurseId, oldValue)) {
			this.nurseId = nurseId;
			setProperty("NURSE_ID", nurseId, oldValue);
			nurse=null;
		}
		return (License)this;
	}
	@Column(name="LIC_TYPE_ID",nullable=false)
	public Integer getLicTypeId(){
		return licTypeId;
	}
	public License setLicTypeId(Integer licTypeId) throws Exception  {
		Integer oldValue = getLicTypeId();
		if (!isSame(licTypeId, oldValue)) {
			this.licTypeId = licTypeId;
			setProperty("LIC_TYPE_ID", licTypeId, oldValue);
			licType=null;
		}
		return (License)this;
	}
	@Column(name="NUMBER",nullable=true,length=32)
	public String getNumber(){
		return number;
	}
	public License setNumber(String number) throws Exception  {
		String oldValue = getNumber();
		if (!isSame(number, oldValue)) {
			this.number = number;
			setProperty("NUMBER", number, oldValue);
		}
		return (License)this;
	}
	@Column(name="VALID_DATE",nullable=true)
	public Date getValidDate(){
		return validDate;
	}
	public License setValidDate(Date validDate) throws Exception  {
		Date oldValue = getValidDate();
		if (!isSame(validDate, oldValue)) {
			this.validDate = validDate;
			setProperty("VALID_DATE", validDate, oldValue);
		}
		return (License)this;
	}
	@Column(name="EXPIRATION_DATE",nullable=true)
	public Date getExpirationDate(){
		return expirationDate;
	}
	public License setExpirationDate(Date expirationDate) throws Exception  {
		Date oldValue = getExpirationDate();
		if (!isSame(expirationDate, oldValue)) {
			this.expirationDate = expirationDate;
			setProperty("EXPIRATION_DATE", expirationDate, oldValue);
		}
		return (License)this;
	}
	@Column(name="DATA_FILE_ID",nullable=true)
	public Integer getDataFileId(){
		return dataFileId;
	}
	public License setDataFileId(Integer dataFileId) throws Exception  {
		Integer oldValue = getDataFileId();
		if (!isSame(dataFileId, oldValue)) {
			this.dataFileId = dataFileId;
			setProperty("DATA_FILE_ID", dataFileId, oldValue);
			dataFile=null;
		}
		return (License)this;
	}
	public DataFile getDataFile() {
		if (dataFile == null) {
			return getEntityManager().find(DataFile.class, getDataFileId());
		}
		return dataFile;
	}
	public License setDataFile(DataFile dataFile) throws Exception {
		setDataFileId(dataFile==null?null:dataFile.getId());
		this.dataFile=dataFile;
		return (License)this;
	}
	public GeneralData getLicType() {
		if (licType == null) {
			return getEntityManager().find(GeneralData.class, getLicTypeId());
		}
		return licType;
	}
	public License setLicType(GeneralData licType) throws Exception {
		setLicTypeId(licType==null?null:licType.getId());
		this.licType=licType;
		return (License)this;
	}
	public Nurse getNurse() {
		if (nurse == null) {
			return getEntityManager().find(Nurse.class, getNurseId());
		}
		return nurse;
	}
	public License setNurse(Nurse nurse) throws Exception {
		setNurseId(nurse==null?null:nurse.getId());
		this.nurse=nurse;
		return (License)this;
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

	public License setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key, data.get(key).toString());
		return (License)this;
	}

	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case NURSE_ID: return getNurseId();
			case LIC_TYPE_ID: return getLicTypeId();
			case NUMBER: return getNumber();
			case VALID_DATE: return getValidDate();
			case EXPIRATION_DATE: return getExpirationDate();
			case DATA_FILE_ID: return getDataFileId();
		}
		return null;
	}

	@Override
	public License setPropertyValue(String property, String value) throws Exception  {
		if(property == null) return (License)this;
		return setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}

	public License setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case NURSE_ID:setNurseId(Integer.valueOf(value)); break;
			case LIC_TYPE_ID:setLicTypeId(Integer.valueOf(value)); break;
			case NUMBER:setNumber(String.valueOf(value)); break;
			case VALID_DATE:setValidDate(FormatText.parseDate(value)); break;
			case EXPIRATION_DATE:setExpirationDate(FormatText.parseDate(value)); break;
			case DATA_FILE_ID:setDataFileId(Integer.valueOf(value)); break;
		}
		return (License)this;
	}

	public License copy() throws Exception {
		License cp = new License(getEntityManager(), (License)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(LicenseDAO cp) throws Exception {
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
		if(!isSame(getDataFileId(),o.getDataFileId())) diffs.add("DATA_FILE_ID");
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
		if (isNull(getLicTypeId()))
			 throw new Exception("LIC_TYPE_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
	}
}
