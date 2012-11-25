package com.digitald4.common.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.model.User;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
public abstract class UserDAO extends DataAccessObject{
	public static enum KEY_PROPERTY{ID};
	public static enum PROPERTY{ID,USERNAME,FIRST_NAME,LAST_NAME,EMAIL,DISABLED,READ_ONLY,PASSWORD};
	private Integer id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private boolean disabled;
	private boolean readOnly;
	private String password;
	public static User getInstance(Integer id){
		return getInstance(id, true);
	}
	public static User getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		User o = null;
		if(cache != null && cache.contains(User.class, pk))
			o = em.find(User.class, pk);
		if(o==null && fetch)
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
		this.username=orig.getUsername();
		this.firstName=orig.getFirstName();
		this.lastName=orig.getLastName();
		this.email=orig.getEmail();
		this.disabled=orig.isDisabled();
		this.readOnly=orig.isReadOnly();
		this.password=orig.getPassword();
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
	@SequenceGenerator(name="SEQ",sequenceName="SEQ")
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
	@Column(name="USERNAME",nullable=false,length=20)
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		if(isSame(username, getUsername()))return;
		String oldValue = getUsername();
		this.username=username;
		setProperty("USERNAME", username, oldValue);
	}
	@Column(name="FIRST_NAME",nullable=false,length=20)
	public String getFirstName(){
		return firstName;
	}
	public void setFirstName(String firstName){
		if(isSame(firstName, getFirstName()))return;
		String oldValue = getFirstName();
		this.firstName=firstName;
		setProperty("FIRST_NAME", firstName, oldValue);
	}
	@Column(name="LAST_NAME",nullable=false,length=20)
	public String getLastName(){
		return lastName;
	}
	public void setLastName(String lastName){
		if(isSame(lastName, getLastName()))return;
		String oldValue = getLastName();
		this.lastName=lastName;
		setProperty("LAST_NAME", lastName, oldValue);
	}
	@Column(name="EMAIL",nullable=false,length=64)
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		if(isSame(email, getEmail()))return;
		String oldValue = getEmail();
		this.email=email;
		setProperty("EMAIL", email, oldValue);
	}
	@Column(name="DISABLED",nullable=true)
	public boolean isDisabled(){
		return disabled;
	}
	public void setDisabled(boolean disabled){
		if(isSame(disabled, isDisabled()))return;
		boolean oldValue = isDisabled();
		this.disabled=disabled;
		setProperty("DISABLED", disabled, oldValue);
	}
	@Column(name="READ_ONLY",nullable=true)
	public boolean isReadOnly(){
		return readOnly;
	}
	public void setReadOnly(boolean readOnly){
		if(isSame(readOnly, isReadOnly()))return;
		boolean oldValue = isReadOnly();
		this.readOnly=readOnly;
		setProperty("READ_ONLY", readOnly, oldValue);
	}
	@Column(name="PASSWORD",nullable=true)
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		if(isSame(password, getPassword()))return;
		String oldValue = getPassword();
		this.password=password;
		setProperty("PASSWORD", password, oldValue);
	}
	public User copy(){
		User cp = new User((User)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(UserDAO cp){
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(UserDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getUsername(),o.getUsername())) diffs.add("USERNAME");
		if(!isSame(getFirstName(),o.getFirstName())) diffs.add("FIRST_NAME");
		if(!isSame(getLastName(),o.getLastName())) diffs.add("LAST_NAME");
		if(!isSame(getEmail(),o.getEmail())) diffs.add("EMAIL");
		if(!isSame(isDisabled(),o.isDisabled())) diffs.add("DISABLED");
		if(!isSame(isReadOnly(),o.isReadOnly())) diffs.add("READ_ONLY");
		if(!isSame(getPassword(),o.getPassword())) diffs.add("PASSWORD");
		return diffs;
	}
	public void insertParents(){
	}
	public void insertChildren(){
	}
}
