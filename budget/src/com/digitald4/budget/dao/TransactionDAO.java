package com.digitald4.budget.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.budget.model.Account;
import com.digitald4.budget.model.Bill;
import com.digitald4.common.model.GeneralData;
import com.digitald4.budget.model.Portfolio;
import com.digitald4.budget.model.Transaction;
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.FormatText;
import java.util.Date;
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
public abstract class TransactionDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,PORTFOLIO_ID,DATE,NAME,BILL_ID,DEBIT_ACCOUNT_ID,CREDIT_ACCOUNT_ID,AMOUNT,STATUS_ID,ACTIVE,DESCRIPTION};
	private Integer id;
	private Integer portfolioId;
	private Date date;
	private String name;
	private Integer billId;
	private Integer debitAccountId;
	private Integer creditAccountId;
	private double amount;
	private Integer statusId;
	private boolean active = true;
	private String description;
	private Bill bill;
	private Account creditAccount;
	private Account debitAccount;
	private Portfolio portfolio;
	private GeneralData status;
	public static Transaction getInstance(Integer id){
		return getInstance(id, true);
	}
	public static Transaction getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		Transaction o = null;
		if(fetch || cache != null && cache.contains(Transaction.class, pk))
			o = em.find(Transaction.class, pk);
		return o;
	}
	public static List<Transaction> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<Transaction> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<Transaction> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM Transaction o";
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
	public synchronized static List<Transaction> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Transaction> tq = em.createQuery(jpql,Transaction.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<Transaction> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<Transaction> tq = em.createNamedQuery(name,Transaction.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public TransactionDAO(){}
	public TransactionDAO(Integer id){
		this.id=id;
	}
	public TransactionDAO(TransactionDAO orig){
		super(orig);
		copyFrom(orig);
	}
	public void copyFrom(TransactionDAO orig){
		this.portfolioId=orig.getPortfolioId();
		this.date=orig.getDate();
		this.name=orig.getName();
		this.billId=orig.getBillId();
		this.debitAccountId=orig.getDebitAccountId();
		this.creditAccountId=orig.getCreditAccountId();
		this.amount=orig.getAmount();
		this.statusId=orig.getStatusId();
		this.active=orig.isActive();
		this.description=orig.getDescription();
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
	public Transaction setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Transaction)this;
	}
	@Column(name="PORTFOLIO_ID",nullable=false)
	public Integer getPortfolioId(){
		return portfolioId;
	}
	public Transaction setPortfolioId(Integer portfolioId) throws Exception  {
		Integer oldValue = getPortfolioId();
		if (!isSame(portfolioId, oldValue)) {
			this.portfolioId = portfolioId;
			setProperty("PORTFOLIO_ID", portfolioId, oldValue);
			portfolio=null;
		}
		return (Transaction)this;
	}
	@Column(name="DATE",nullable=true)
	public Date getDate(){
		return date;
	}
	public Transaction setDate(Date date) throws Exception  {
		Date oldValue = getDate();
		if (!isSame(date, oldValue)) {
			this.date = date;
			setProperty("DATE", date, oldValue);
		}
		return (Transaction)this;
	}
	@Column(name="NAME",nullable=false,length=64)
	public String getName(){
		return name;
	}
	public Transaction setName(String name) throws Exception  {
		String oldValue = getName();
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (Transaction)this;
	}
	@Column(name="BILL_ID",nullable=true)
	public Integer getBillId(){
		return billId;
	}
	public Transaction setBillId(Integer billId) throws Exception  {
		Integer oldValue = getBillId();
		if (!isSame(billId, oldValue)) {
			this.billId = billId;
			setProperty("BILL_ID", billId, oldValue);
			bill=null;
		}
		return (Transaction)this;
	}
	@Column(name="DEBIT_ACCOUNT_ID",nullable=false)
	public Integer getDebitAccountId(){
		return debitAccountId;
	}
	public Transaction setDebitAccountId(Integer debitAccountId) throws Exception  {
		Integer oldValue = getDebitAccountId();
		if (!isSame(debitAccountId, oldValue)) {
			this.debitAccountId = debitAccountId;
			setProperty("DEBIT_ACCOUNT_ID", debitAccountId, oldValue);
			debitAccount=null;
		}
		return (Transaction)this;
	}
	@Column(name="CREDIT_ACCOUNT_ID",nullable=false)
	public Integer getCreditAccountId(){
		return creditAccountId;
	}
	public Transaction setCreditAccountId(Integer creditAccountId) throws Exception  {
		Integer oldValue = getCreditAccountId();
		if (!isSame(creditAccountId, oldValue)) {
			this.creditAccountId = creditAccountId;
			setProperty("CREDIT_ACCOUNT_ID", creditAccountId, oldValue);
			creditAccount=null;
		}
		return (Transaction)this;
	}
	@Column(name="AMOUNT",nullable=true)
	public double getAmount(){
		return amount;
	}
	public Transaction setAmount(double amount) throws Exception  {
		double oldValue = getAmount();
		if (!isSame(amount, oldValue)) {
			this.amount = amount;
			setProperty("AMOUNT", amount, oldValue);
		}
		return (Transaction)this;
	}
	@Column(name="STATUS_ID",nullable=true)
	public Integer getStatusId(){
		return statusId;
	}
	public Transaction setStatusId(Integer statusId) throws Exception  {
		Integer oldValue = getStatusId();
		if (!isSame(statusId, oldValue)) {
			this.statusId = statusId;
			setProperty("STATUS_ID", statusId, oldValue);
			status=null;
		}
		return (Transaction)this;
	}
	@Column(name="ACTIVE",nullable=true)
	public boolean isActive(){
		return active;
	}
	public Transaction setActive(boolean active) throws Exception  {
		boolean oldValue = isActive();
		if (!isSame(active, oldValue)) {
			this.active = active;
			setProperty("ACTIVE", active, oldValue);
		}
		return (Transaction)this;
	}
	@Column(name="DESCRIPTION",nullable=true,length=256)
	public String getDescription(){
		return description;
	}
	public Transaction setDescription(String description) throws Exception  {
		String oldValue = getDescription();
		if (!isSame(description, oldValue)) {
			this.description = description;
			setProperty("DESCRIPTION", description, oldValue);
		}
		return (Transaction)this;
	}
	public Bill getBill(){
		if(bill==null)
			bill=Bill.getInstance(getBillId());
		return bill;
	}
	public Transaction setBill(Bill bill) throws Exception {
		setBillId(bill==null?null:bill.getId());
		this.bill=bill;
		return (Transaction)this;
	}
	public Account getCreditAccount(){
		if(creditAccount==null)
			creditAccount=Account.getInstance(getCreditAccountId());
		return creditAccount;
	}
	public Transaction setCreditAccount(Account creditAccount) throws Exception {
		setCreditAccountId(creditAccount==null?null:creditAccount.getId());
		this.creditAccount=creditAccount;
		return (Transaction)this;
	}
	public Account getDebitAccount(){
		if(debitAccount==null)
			debitAccount=Account.getInstance(getDebitAccountId());
		return debitAccount;
	}
	public Transaction setDebitAccount(Account debitAccount) throws Exception {
		setDebitAccountId(debitAccount==null?null:debitAccount.getId());
		this.debitAccount=debitAccount;
		return (Transaction)this;
	}
	public Portfolio getPortfolio(){
		if(portfolio==null)
			portfolio=Portfolio.getInstance(getPortfolioId());
		return portfolio;
	}
	public Transaction setPortfolio(Portfolio portfolio) throws Exception {
		setPortfolioId(portfolio==null?null:portfolio.getId());
		this.portfolio=portfolio;
		return (Transaction)this;
	}
	public GeneralData getStatus(){
		if(status==null)
			status=GeneralData.getInstance(getStatusId());
		return status;
	}
	public Transaction setStatus(GeneralData status) throws Exception {
		setStatusId(status==null?null:status.getId());
		this.status=status;
		return (Transaction)this;
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

	public Transaction setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key, data.get(key).toString());
		return (Transaction)this;
	}

	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case PORTFOLIO_ID: return getPortfolioId();
			case DATE: return getDate();
			case NAME: return getName();
			case BILL_ID: return getBillId();
			case DEBIT_ACCOUNT_ID: return getDebitAccountId();
			case CREDIT_ACCOUNT_ID: return getCreditAccountId();
			case AMOUNT: return getAmount();
			case STATUS_ID: return getStatusId();
			case ACTIVE: return isActive();
			case DESCRIPTION: return getDescription();
		}
		return null;
	}

	@Override
	public Transaction setPropertyValue(String property, String value) throws Exception  {
		if(property == null) return (Transaction)this;
		return setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}

	public Transaction setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case PORTFOLIO_ID:setPortfolioId(Integer.valueOf(value)); break;
			case DATE:setDate(FormatText.parseDate(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case BILL_ID:setBillId(Integer.valueOf(value)); break;
			case DEBIT_ACCOUNT_ID:setDebitAccountId(Integer.valueOf(value)); break;
			case CREDIT_ACCOUNT_ID:setCreditAccountId(Integer.valueOf(value)); break;
			case AMOUNT:setAmount(Double.valueOf(value)); break;
			case STATUS_ID:setStatusId(Integer.valueOf(value)); break;
			case ACTIVE:setActive(Boolean.valueOf(value)); break;
			case DESCRIPTION:setDescription(String.valueOf(value)); break;
		}
		return (Transaction)this;
	}

	public Transaction copy() throws Exception {
		Transaction cp = new Transaction((Transaction)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(TransactionDAO cp) throws Exception {
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(TransactionDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getPortfolioId(),o.getPortfolioId())) diffs.add("PORTFOLIO_ID");
		if(!isSame(getDate(),o.getDate())) diffs.add("DATE");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getBillId(),o.getBillId())) diffs.add("BILL_ID");
		if(!isSame(getDebitAccountId(),o.getDebitAccountId())) diffs.add("DEBIT_ACCOUNT_ID");
		if(!isSame(getCreditAccountId(),o.getCreditAccountId())) diffs.add("CREDIT_ACCOUNT_ID");
		if(!isSame(getAmount(),o.getAmount())) diffs.add("AMOUNT");
		if(!isSame(getStatusId(),o.getStatusId())) diffs.add("STATUS_ID");
		if(!isSame(isActive(),o.isActive())) diffs.add("ACTIVE");
		if(!isSame(getDescription(),o.getDescription())) diffs.add("DESCRIPTION");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(bill != null && bill.isNewInstance())
				bill.insert();
		if(creditAccount != null && creditAccount.isNewInstance())
				creditAccount.insert();
		if(debitAccount != null && debitAccount.isNewInstance())
				debitAccount.insert();
		if(portfolio != null && portfolio.isNewInstance())
				portfolio.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getPortfolioId()))
			 throw new Exception("PORTFOLIO_ID is required.");
		if (isNull(getName()))
			 throw new Exception("NAME is required.");
		if (isNull(getDebitAccountId()))
			 throw new Exception("DEBIT_ACCOUNT_ID is required.");
		if (isNull(getCreditAccountId()))
			 throw new Exception("CREDIT_ACCOUNT_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
	}
}
