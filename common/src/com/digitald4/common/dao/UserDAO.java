package com.digitald4.common.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import org.joda.time.DateTime;
public abstract class UserDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,TYPE_ID,EMAIL,FIRST_NAME,LAST_NAME,DISABLED,READ_ONLY,PASSWORD,NOTES,LAST_LOGIN};
	private Integer id;
	private Integer typeId;
	private String email;
	private String firstName;
	private String lastName;
	private boolean disabled;
	private boolean readOnly;
	private String password;
	private String notes;
	private DateTime lastLogin;
	private GeneralData type;
	public static User getInstance(Integer id){
		return getInstance(id, true);
	}
	public static User getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		User o = null;
		if(fetch || cache != null && cache.contains(User.class, pk))
			o = em.find(User.class, pk);
		return o;
	}
	public static Collection<User> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<User> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<User> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM User o";
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
	public synchronized static Collection<User> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<User> tq = em.createQuery(jpql,User.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static Collection<User> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<User> tq = em.createNamedQuery(name,User.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public UserDAO(){}
	public UserDAO(Integer id){
		this.id=id;
	}
	public UserDAO(UserDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(UserDAO orig){
		this.typeId=orig.getTypeId();
		this.email=orig.getEmail();
		this.firstName=orig.getFirstName();
		this.lastName=orig.getLastName();
		this.disabled=orig.isDisabled();
		this.readOnly=orig.isReadOnly();
		this.password=orig.getPassword();
		this.notes=orig.getNotes();
		this.lastLogin=orig.getLastLogin();
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
	public User setId(Integer id)throws Exception{
		if(!isSame(id, getId())){
			Integer oldValue = getId();
			this.id=id;
			setProperty("ID", id, oldValue);
		}
		return (User)this;
	}
	@Column(name="TYPE_ID",nullable=false)
	public Integer getTypeId(){
		return typeId;
	}
	public User setTypeId(Integer typeId)throws Exception{
		if(!isSame(typeId, getTypeId())){
			Integer oldValue = getTypeId();
			this.typeId=typeId;
			setProperty("TYPE_ID", typeId, oldValue);
			type=null;
		}
		return (User)this;
	}
	@Column(name="EMAIL",nullable=false,length=64)
	public String getEmail(){
		return email;
	}
	public User setEmail(String email)throws Exception{
		if(!isSame(email, getEmail())){
			String oldValue = getEmail();
			this.email=email;
			setProperty("EMAIL", email, oldValue);
		}
		return (User)this;
	}
	@Column(name="FIRST_NAME",nullable=false,length=20)
	public String getFirstName(){
		return firstName;
	}
	public User setFirstName(String firstName)throws Exception{
		if(!isSame(firstName, getFirstName())){
			String oldValue = getFirstName();
			this.firstName=firstName;
			setProperty("FIRST_NAME", firstName, oldValue);
		}
		return (User)this;
	}
	@Column(name="LAST_NAME",nullable=false,length=20)
	public String getLastName(){
		return lastName;
	}
	public User setLastName(String lastName)throws Exception{
		if(!isSame(lastName, getLastName())){
			String oldValue = getLastName();
			this.lastName=lastName;
			setProperty("LAST_NAME", lastName, oldValue);
		}
		return (User)this;
	}
	@Column(name="DISABLED",nullable=true)
	public boolean isDisabled(){
		return disabled;
	}
	public User setDisabled(boolean disabled)throws Exception{
		if(!isSame(disabled, isDisabled())){
			boolean oldValue = isDisabled();
			this.disabled=disabled;
			setProperty("DISABLED", disabled, oldValue);
		}
		return (User)this;
	}
	@Column(name="READ_ONLY",nullable=true)
	public boolean isReadOnly(){
		return readOnly;
	}
	public User setReadOnly(boolean readOnly)throws Exception{
		if(!isSame(readOnly, isReadOnly())){
			boolean oldValue = isReadOnly();
			this.readOnly=readOnly;
			setProperty("READ_ONLY", readOnly, oldValue);
		}
		return (User)this;
	}
	@Column(name="PASSWORD",nullable=true,length=128)
	public String getPassword(){
		return password;
	}
	public User setPassword(String password)throws Exception{
		if(!isSame(password, getPassword())){
			String oldValue = getPassword();
			this.password=password;
			setProperty("PASSWORD", password, oldValue);
		}
		return (User)this;
	}
	@Column(name="NOTES",nullable=true,length=256)
	public String getNotes(){
		return notes;
	}
	public User setNotes(String notes)throws Exception{
		if(!isSame(notes, getNotes())){
			String oldValue = getNotes();
			this.notes=notes;
			setProperty("NOTES", notes, oldValue);
		}
		return (User)this;
	}
	@Column(name="LAST_LOGIN",nullable=true)
	public DateTime getLastLogin(){
		return lastLogin;
	}
	public User setLastLogin(DateTime lastLogin)throws Exception{
		if(!isSame(lastLogin, getLastLogin())){
			DateTime oldValue = getLastLogin();
			this.lastLogin=lastLogin;
			setProperty("LAST_LOGIN", lastLogin, oldValue);
		}
		return (User)this;
	}
	public GeneralData getType(){
		if(type==null)
			type=GeneralData.getInstance(getTypeId());
		return type;
	}
	public User setType(GeneralData type)throws Exception{
		setTypeId(type==null?null:type.getId());
		this.type=type;
		return (User)this;
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
			case TYPE_ID: return getTypeId();
			case EMAIL: return getEmail();
			case FIRST_NAME: return getFirstName();
			case LAST_NAME: return getLastName();
			case DISABLED: return isDisabled();
			case READ_ONLY: return isReadOnly();
			case PASSWORD: return getPassword();
			case NOTES: return getNotes();
			case LAST_LOGIN: return getLastLogin();
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
			case TYPE_ID:setTypeId(Integer.valueOf(value)); break;
			case EMAIL:setEmail(String.valueOf(value)); break;
			case FIRST_NAME:setFirstName(String.valueOf(value)); break;
			case LAST_NAME:setLastName(String.valueOf(value)); break;
			case DISABLED:setDisabled(Boolean.valueOf(value)); break;
			case READ_ONLY:setReadOnly(Boolean.valueOf(value)); break;
			case PASSWORD:setPassword(String.valueOf(value)); break;
			case NOTES:setNotes(String.valueOf(value)); break;
			case LAST_LOGIN:setLastLogin(new DateTime(value)); break;
		}
	}
	public User copy()throws Exception{
		User cp = new User((User)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(UserDAO cp)throws Exception{
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(UserDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getTypeId(),o.getTypeId())) diffs.add("TYPE_ID");
		if(!isSame(getEmail(),o.getEmail())) diffs.add("EMAIL");
		if(!isSame(getFirstName(),o.getFirstName())) diffs.add("FIRST_NAME");
		if(!isSame(getLastName(),o.getLastName())) diffs.add("LAST_NAME");
		if(!isSame(isDisabled(),o.isDisabled())) diffs.add("DISABLED");
		if(!isSame(isReadOnly(),o.isReadOnly())) diffs.add("READ_ONLY");
		if(!isSame(getPassword(),o.getPassword())) diffs.add("PASSWORD");
		if(!isSame(getNotes(),o.getNotes())) diffs.add("NOTES");
		if(!isSame(getLastLogin(),o.getLastLogin())) diffs.add("LAST_LOGIN");
		return diffs;
	}
	public void insertParents()throws Exception{
	}
	public void insertPreCheck()throws Exception{
		if (isNull(typeId))
			 throw new Exception("TYPE_ID is required.");
		if (isNull(email))
			 throw new Exception("EMAIL is required.");
		if (isNull(firstName))
			 throw new Exception("FIRST_NAME is required.");
		if (isNull(lastName))
			 throw new Exception("LAST_NAME is required.");
	}
	public void insertChildren()throws Exception{
	}
}
