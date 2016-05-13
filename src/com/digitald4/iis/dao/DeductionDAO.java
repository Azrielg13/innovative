package com.digitald4.iis.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.iis.model.Deduction;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.Paystub;
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
public abstract class DeductionDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,PAYSTUB_ID,TYPE_ID,FACTOR,AMOUNT,AMOUNT_Y_T_D};
	private Integer id;
	private Integer paystubId;
	private Integer typeId;
	private double factor;
	private double amount;
	private double amountYTD;
	private Paystub paystub;
	private GeneralData type;
	public static Deduction getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Deduction getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Deduction o = null;
		if(fetch || cache != null && cache.contains(Deduction.class, pk))
			o = em.find(Deduction.class, pk);
		return o;
	}
	public static List<Deduction> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Deduction> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Deduction> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Deduction o";
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
	public synchronized static List<Deduction> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Deduction> tq = em.createQuery(jpql,Deduction.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Deduction> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Deduction> tq = em.createNamedQuery(name,Deduction.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public DeductionDAO(){}
	public DeductionDAO(Integer id){
		this.id=id;
	}
	public DeductionDAO(DeductionDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(DeductionDAO orig){
		this.paystubId=orig.getPaystubId();
		this.typeId=orig.getTypeId();
		this.factor=orig.getFactor();
		this.amount=orig.getAmount();
		this.amountYTD=orig.getAmountYTD();
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
	public Deduction setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Deduction)this;
	}
	@Column(name="PAYSTUB_ID",nullable=false)
	public Integer getPaystubId(){
		return paystubId;
	}
	public Deduction setPaystubId(Integer paystubId) throws Exception  {
		Integer oldValue = getPaystubId();
		if (!isSame(paystubId, oldValue)) {
			this.paystubId = paystubId;
			setProperty("PAYSTUB_ID", paystubId, oldValue);
			paystub=null;
		}
		return (Deduction)this;
	}
	@Column(name="TYPE_ID",nullable=false)
	public Integer getTypeId(){
		return typeId;
	}
	public Deduction setTypeId(Integer typeId) throws Exception  {
		Integer oldValue = getTypeId();
		if (!isSame(typeId, oldValue)) {
			this.typeId = typeId;
			setProperty("TYPE_ID", typeId, oldValue);
			type=null;
		}
		return (Deduction)this;
	}
	@Column(name="FACTOR",nullable=true)
	public double getFactor(){
		return factor;
	}
	public Deduction setFactor(double factor) throws Exception  {
		double oldValue = getFactor();
		if (!isSame(factor, oldValue)) {
			this.factor = factor;
			setProperty("FACTOR", factor, oldValue);
		}
		return (Deduction)this;
	}
	@Column(name="AMOUNT",nullable=true)
	public double getAmount(){
		return amount;
	}
	public Deduction setAmount(double amount) throws Exception  {
		double oldValue = getAmount();
		if (!isSame(amount, oldValue)) {
			this.amount = amount;
			setProperty("AMOUNT", amount, oldValue);
		}
		return (Deduction)this;
	}
	@Column(name="AMOUNT_Y_T_D",nullable=true)
	public double getAmountYTD(){
		return amountYTD;
	}
	public Deduction setAmountYTD(double amountYTD) throws Exception  {
		double oldValue = getAmountYTD();
		if (!isSame(amountYTD, oldValue)) {
			this.amountYTD = amountYTD;
			setProperty("AMOUNT_Y_T_D", amountYTD, oldValue);
		}
		return (Deduction)this;
	}
	public Paystub getPaystub(){
		if(paystub==null)
			paystub=Paystub.getInstance(getPaystubId());
		return paystub;
	}
	public Deduction setPaystub(Paystub paystub) throws Exception {
		setPaystubId(paystub==null?null:paystub.getId());
		this.paystub=paystub;
		return (Deduction)this;
	}
	public GeneralData getType(){
		if(type==null)
			type=GeneralData.getInstance(getTypeId());
		return type;
	}
	public Deduction setType(GeneralData type) throws Exception {
		setTypeId(type==null?null:type.getId());
		this.type=type;
		return (Deduction)this;
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

	public Deduction setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key, data.get(key).toString());
		return (Deduction)this;
	}

	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case PAYSTUB_ID: return getPaystubId();
			case TYPE_ID: return getTypeId();
			case FACTOR: return getFactor();
			case AMOUNT: return getAmount();
			case AMOUNT_Y_T_D: return getAmountYTD();
		}
		return null;
	}

	@Override
	public Deduction setPropertyValue(String property, String value) throws Exception  {
		if(property == null) return (Deduction)this;
		return setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}

	public Deduction setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case PAYSTUB_ID:setPaystubId(Integer.valueOf(value)); break;
			case TYPE_ID:setTypeId(Integer.valueOf(value)); break;
			case FACTOR:setFactor(Double.valueOf(value)); break;
			case AMOUNT:setAmount(Double.valueOf(value)); break;
			case AMOUNT_Y_T_D:setAmountYTD(Double.valueOf(value)); break;
		}
		return (Deduction)this;
	}

	public Deduction copy() throws Exception {
		Deduction cp = new Deduction((Deduction)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(DeductionDAO cp) throws Exception {
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(DeductionDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getPaystubId(),o.getPaystubId())) diffs.add("PAYSTUB_ID");
		if(!isSame(getTypeId(),o.getTypeId())) diffs.add("TYPE_ID");
		if(!isSame(getFactor(),o.getFactor())) diffs.add("FACTOR");
		if(!isSame(getAmount(),o.getAmount())) diffs.add("AMOUNT");
		if(!isSame(getAmountYTD(),o.getAmountYTD())) diffs.add("AMOUNT_Y_T_D");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(paystub != null && paystub.isNewInstance())
				paystub.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getPaystubId()))
			 throw new Exception("PAYSTUB_ID is required.");
		if (isNull(getTypeId()))
			 throw new Exception("TYPE_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
	}
}
