package com.digitald4.common.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.GeneralData;
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
public abstract class DataFileDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,NAME,TYPE_ID,SIZE,DATA};
	private Integer id;
	private String name;
	private Integer typeId;
	private int size;
	private byte[] data;
	private GeneralData type;
	public static DataFile getInstance(Integer id){
		return getInstance(id, true);
	}
	public static DataFile getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		DataFile o = null;
		if(fetch || cache != null && cache.contains(DataFile.class, pk))
			o = em.find(DataFile.class, pk);
		return o;
	}
	public static List<DataFile> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<DataFile> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<DataFile> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM DataFile o";
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
	public synchronized static List<DataFile> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<DataFile> tq = em.createQuery(jpql,DataFile.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<DataFile> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<DataFile> tq = em.createNamedQuery(name,DataFile.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public DataFileDAO(){}
	public DataFileDAO(Integer id){
		this.id=id;
	}
	public DataFileDAO(DataFileDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(DataFileDAO orig){
		this.name=orig.getName();
		this.typeId=orig.getTypeId();
		this.size=orig.getSize();
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
	public DataFile setId(Integer id) throws Exception  {
		Object oldValue = null;
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (DataFile)this;
	}
	@Column(name="NAME",nullable=false,length=32)
	public String getName(){
		return name;
	}
	public DataFile setName(String name) throws Exception  {
		Object oldValue = null;
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (DataFile)this;
	}
	@Column(name="TYPE_ID",nullable=false)
	public Integer getTypeId(){
		return typeId;
	}
	public DataFile setTypeId(Integer typeId) throws Exception  {
		Object oldValue = null;
		if (!isSame(typeId, oldValue)) {
			this.typeId = typeId;
			setProperty("TYPE_ID", typeId, oldValue);
			type=null;
		}
		return (DataFile)this;
	}
	@Column(name="SIZE",nullable=true)
	public int getSize(){
		return size;
	}
	public DataFile setSize(int size) throws Exception  {
		Object oldValue = null;
		if (!isSame(size, oldValue)) {
			this.size = size;
			setProperty("SIZE", size, oldValue);
		}
		return (DataFile)this;
	}
	@Column(name="DATA",nullable=true)
	public byte[] getData(){
		return data;
	}
	public DataFile setData(byte[] data) throws Exception  {
		Object oldValue = null;
		if (!isSame(data, oldValue)) {
			this.data = data;
			setProperty("DATA", data, oldValue);
		}
		return (DataFile)this;
	}
	public GeneralData getType(){
		if(type==null)
			type=GeneralData.getInstance(getTypeId());
		return type;
	}
	public DataFile setType(GeneralData type) throws Exception {
		setTypeId(type==null?null:type.getId());
		this.type=type;
		return (DataFile)this;
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
			case TYPE_ID: return getTypeId();
			case SIZE: return getSize();
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
			case NAME:setName(String.valueOf(value)); break;
			case TYPE_ID:setTypeId(Integer.valueOf(value)); break;
			case SIZE:setSize(Integer.valueOf(value)); break;
		}
	}
	public DataFile copy() throws Exception {
		DataFile cp = new DataFile((DataFile)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(DataFileDAO cp) throws Exception {
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(DataFileDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getTypeId(),o.getTypeId())) diffs.add("TYPE_ID");
		if(!isSame(getSize(),o.getSize())) diffs.add("SIZE");
		if(!isSame(getData(),o.getData())) diffs.add("DATA");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getName()))
			 throw new Exception("NAME is required.");
		if (isNull(getTypeId()))
			 throw new Exception("TYPE_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
	}
}