package com.digitald4.common.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.GeneralData;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
public abstract class GeneralDataDAO extends DataAccessObject{
	public static enum KEY_PROPERTY{ID};
	public static enum PROPERTY{ID,GROUP_ID,IN_GROUP_ID,NAME,RANK,ACTIVE,DESCRIPTION};
	private Integer id;
	private Integer groupId;
	private Integer inGroupId;
	private String name;
	private double rank;
	private boolean active = true;
	private String description;
	private Collection<GeneralData> generalDatas;
	private GeneralData group;
	public static GeneralData getInstance(Integer id){
		return getInstance(id, true);
	}
	public static GeneralData getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		GeneralData o = null;
		if(fetch || cache != null && cache.contains(GeneralData.class, pk))
			o = em.find(GeneralData.class, pk);
		return o;
	}
	public static Collection<GeneralData> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<GeneralData> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<GeneralData> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM GeneralData o";
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
	public synchronized static Collection<GeneralData> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<GeneralData> tq = em.createQuery(jpql,GeneralData.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static Collection<GeneralData> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<GeneralData> tq = em.createNamedQuery(name,GeneralData.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public GeneralDataDAO(){}
	public GeneralDataDAO(Integer id){
		this.id=id;
	}
	public GeneralDataDAO(GeneralDataDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(GeneralDataDAO orig){
		this.groupId=orig.getGroupId();
		this.inGroupId=orig.getInGroupId();
		this.name=orig.getName();
		this.rank=orig.getRank();
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
	public GeneralData setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
		}
		return (GeneralData)this;
	}
	@Column(name="GROUP_ID",nullable=true)
	public Integer getGroupId(){
		return groupId;
	}
	public GeneralData setGroupId(Integer groupId)throws Exception{
		if(!isSame(groupId, getGroupId())){
			Integer oldValue = getGroupId();
			this.groupId=groupId;
			setProperty("GROUP_ID", groupId, oldValue);
			group=null;
		}
		return (GeneralData)this;
	}
	@Column(name="IN_GROUP_ID",nullable=false)
	public Integer getInGroupId(){
		return inGroupId;
	}
	public GeneralData setInGroupId(Integer inGroupId)throws Exception{
		if(!isSame(inGroupId, getInGroupId())){
			Integer oldValue = getInGroupId();
			this.inGroupId=inGroupId;
			setProperty("IN_GROUP_ID", inGroupId, oldValue);
		}
		return (GeneralData)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public GeneralData setName(String name)throws Exception{
		if(!isSame(name, getName())){
			String oldValue = getName();
			this.name=name;
			setProperty("NAME", name, oldValue);
		}
		return (GeneralData)this;
	}
	@Column(name="RANK",nullable=true)
	public double getRank(){
		return rank;
	}
	public GeneralData setRank(double rank)throws Exception{
		if(!isSame(rank, getRank())){
			double oldValue = getRank();
			this.rank=rank;
			setProperty("RANK", rank, oldValue);
		}
		return (GeneralData)this;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public GeneralData setActive(boolean active)throws Exception{
		if(!isSame(active, isActive())){
			boolean oldValue = isActive();
			this.active=active;
			setProperty("ACTIVE", active, oldValue);
		}
		return (GeneralData)this;
	}
	@Column(name="DESCRIPTION",nullable=true,length=256)
	public String getDescription(){
		return description;
	}
	public GeneralData setDescription(String description)throws Exception{
		if(!isSame(description, getDescription())){
			String oldValue = getDescription();
			this.description=description;
			setProperty("DESCRIPTION", description, oldValue);
		}
		return (GeneralData)this;
	}
	public GeneralData getGroup(){
		if(group==null)
			group=GeneralData.getInstance(getGroupId());
		return group;
	}
	public GeneralData setGroup(GeneralData group)throws Exception{
		setGroupId(group==null?null:group.getId());
		this.group=group;
		return (GeneralData)this;
	}
	public Collection<GeneralData> getGeneralDatas(){
		if(isNewInstance() || generalDatas != null){
			if(generalDatas == null)
				generalDatas = new TreeSet<GeneralData>();
			return generalDatas;
		}
		return GeneralData.getNamedCollection("findByGroup",getId());
	}
	public GeneralData addGeneralData(GeneralData generalData)throws Exception{
		generalData.setGroup((GeneralData)this);
		if(isNewInstance() || generalDatas != null)
			getGeneralDatas().add(generalData);
		else
			generalData.insert();
		return (GeneralData)this;
	}
	public GeneralData removeGeneralData(GeneralData generalData)throws Exception{
		if(isNewInstance() || generalDatas != null)
			getGeneralDatas().remove(generalData);
		else
			generalData.delete();
		return (GeneralData)this;
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
			case GROUP_ID: return getGroupId();
			case IN_GROUP_ID: return getInGroupId();
			case NAME: return getName();
			case RANK: return getRank();
			case ACTIVE: return isActive();
			case DESCRIPTION: return getDescription();
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
			case GROUP_ID:setGroupId(Integer.valueOf(value)); break;
			case IN_GROUP_ID:setInGroupId(Integer.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case RANK:setRank(Double.valueOf(value)); break;
			case ACTIVE:setActive(Boolean.valueOf(value)); break;
			case DESCRIPTION:setDescription(String.valueOf(value)); break;
		}
	}
	public GeneralData copy()throws Exception{
		GeneralData cp = new GeneralData((GeneralData)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(GeneralDataDAO cp)throws Exception{
		super.copyChildrenTo(cp);
		for(GeneralData child:getGeneralDatas())
			cp.addGeneralData(child.copy());
	}
	public Vector<String> getDifference(GeneralDataDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getGroupId(),o.getGroupId())) diffs.add("GROUP_ID");
		if(!isSame(getInGroupId(),o.getInGroupId())) diffs.add("IN_GROUP_ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getRank(),o.getRank())) diffs.add("RANK");
		if(!isSame(isActive(),o.isActive())) diffs.add("ACTIVE");
		if(!isSame(getDescription(),o.getDescription())) diffs.add("DESCRIPTION");
		return diffs;
	}
	public void insertParents()throws Exception{
		if(group != null && group.isNewInstance())
				group.insert();
	}
	public void insertPreCheck()throws Exception{
		if (isNull(inGroupId))
			 throw new Exception("IN_GROUP_ID is required.");
		if (isNull(name))
			 throw new Exception("NAME is required.");
	}
	public void insertChildren()throws Exception{
		if(generalDatas != null){
			for(GeneralData generalData:getGeneralDatas())
				generalData.setGroup((GeneralData)this);
		}
		if(generalDatas != null){
			for(GeneralData generalData:getGeneralDatas())
				if(generalData.isNewInstance())
					generalData.insert();
			generalDatas = null;
		}
	}
}
