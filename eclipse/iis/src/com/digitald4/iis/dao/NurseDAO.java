package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.common.model.User;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
public abstract class NurseDAO extends DataAccessObject{
	public static enum KEY_PROPERTY{ID};
	public static enum PROPERTY{ID,ACTIVE,ADDRESS,PAY_RATE,PAY_RATE_LESS_2HR,MILEAGE_RATE};
	private Integer id;
	private boolean active = false;
	private String address;
	private double payRate;
	private double payRateLess2Hr;
	private double mileageRate;
	private Collection<Patient> patients;
	private User user;
	public static Nurse getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Nurse getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Nurse o = null;
		if(cache != null && cache.contains(Nurse.class, pk))
			o = em.find(Nurse.class, pk);
		if(o==null && fetch)
			o = em.find(Nurse.class, pk);
		return o;
	}
	public static Collection<Nurse> getAll(){
		return getNamedCollection("findAll");
	}
	public static Collection<Nurse> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static Collection<Nurse> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Nurse o";
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
	public synchronized static Collection<Nurse> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Nurse> tq = em.createQuery(jpql,Nurse.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static Collection<Nurse> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Nurse> tq = em.createNamedQuery(name,Nurse.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public NurseDAO(){}
	public NurseDAO(Integer id){
		this.id=id;
	}
	public NurseDAO(NurseDAO orig){
		super(orig);
		this.id=orig.getId();
		copyFrom(orig);
	}
	public void copyFrom(NurseDAO orig){
		this.active=orig.isActive();
		this.address=orig.getAddress();
		this.payRate=orig.getPayRate();
		this.payRateLess2Hr=orig.getPayRateLess2Hr();
		this.mileageRate=orig.getMileageRate();
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
		user=null;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public void setActive(boolean active){
		if(isSame(active, isActive()))return;
		boolean oldValue = isActive();
		this.active=active;
		setProperty("ACTIVE", active, oldValue);
	}
	@Column(name="ADDRESS",nullable=false,length=50)
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		if(isSame(address, getAddress()))return;
		String oldValue = getAddress();
		this.address=address;
		setProperty("ADDRESS", address, oldValue);
	}
	@Column(name="PAY_RATE",nullable=false)
	public double getPayRate(){
		return payRate;
	}
	public void setPayRate(double payRate){
		if(isSame(payRate, getPayRate()))return;
		double oldValue = getPayRate();
		this.payRate=payRate;
		setProperty("PAY_RATE", payRate, oldValue);
	}
	@Column(name="PAY_RATE_LESS_2HR",nullable=false)
	public double getPayRateLess2Hr(){
		return payRateLess2Hr;
	}
	public void setPayRateLess2Hr(double payRateLess2Hr){
		if(isSame(payRateLess2Hr, getPayRateLess2Hr()))return;
		double oldValue = getPayRateLess2Hr();
		this.payRateLess2Hr=payRateLess2Hr;
		setProperty("PAY_RATE_LESS_2HR", payRateLess2Hr, oldValue);
	}
	@Column(name="MILEAGE_RATE",nullable=true)
	public double getMileageRate(){
		return mileageRate;
	}
	public void setMileageRate(double mileageRate){
		if(isSame(mileageRate, getMileageRate()))return;
		double oldValue = getMileageRate();
		this.mileageRate=mileageRate;
		setProperty("MILEAGE_RATE", mileageRate, oldValue);
	}
	public User getUser(){
		if(user==null)
			user=User.getInstance(getId());
		return user;
	}
	public void setUser(User user){
		setId(user==null?0:user.getId());
		this.user=user;
	}
	public Collection<Patient> getPatients(){
		if(isNewInstance() || patients != null){
			if(patients == null)
				patients = new TreeSet<Patient>();
			return patients;
		}
		return Patient.getNamedCollection("findByNurse",getId());
	}
	public void addPatient(Patient patient){
		patient.setNurse((Nurse)this);
		if(isNewInstance() || patients != null)
			getPatients().add(patient);
		else
			patient.insert();
	}
	public void removePatient(Patient patient){
		if(isNewInstance() || patients != null)
			getPatients().remove(patient);
		else
			patient.delete();
	}
	public Nurse copy(){
		Nurse cp = new Nurse((Nurse)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(NurseDAO cp){
		super.copyChildrenTo(cp);
		for(Patient child:getPatients())
			cp.addPatient(child.copy());
	}
	public Vector<String> getDifference(NurseDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(isActive(),o.isActive())) diffs.add("ACTIVE");
		if(!isSame(getAddress(),o.getAddress())) diffs.add("ADDRESS");
		if(!isSame(getPayRate(),o.getPayRate())) diffs.add("PAY_RATE");
		if(!isSame(getPayRateLess2Hr(),o.getPayRateLess2Hr())) diffs.add("PAY_RATE_LESS_2HR");
		if(!isSame(getMileageRate(),o.getMileageRate())) diffs.add("MILEAGE_RATE");
		return diffs;
	}
	public void insertParents(){
		if(user != null && user.isNewInstance())
				user.insert();
	}
	public void insertChildren(){
		if(patients != null){
			for(Patient patient:getPatients())
				patient.setNurse((Nurse)this);
		}
		if(patients != null){
			for(Patient patient:getPatients())
				if(patient.isNewInstance())
					patient.insert();
			patients = null;
		}
	}
}
