package com.digitald4.budget.dao;

import com.digitald4.budget.model.Account;
import com.digitald4.budget.model.Bill;
import com.digitald4.common.model.GeneralData;
import com.digitald4.budget.model.Portfolio;
import com.digitald4.budget.model.Transaction;
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.SortedList;
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
public abstract class AccountDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,PORTFOLIO_ID,NAME,CATEGORY_ID};
	private Integer id;
	private Integer portfolioId;
	private String name;
	private Integer categoryId;
	private List<Bill> bills;
	private List<Transaction> creditTransactions;
	private List<Transaction> debitTransactions;
	private GeneralData category;
	private Portfolio portfolio;
	public AccountDAO(EntityManager entityManager) {
		super(entityManager);
	}
	public AccountDAO(EntityManager entityManager, Integer id) {
		super(entityManager);
		this.id=id;
	}
	public AccountDAO(EntityManager entityManager, AccountDAO orig) {
		super(entityManager, orig);
		copyFrom(orig);
	}
	public void copyFrom(AccountDAO orig){
		this.portfolioId = orig.getPortfolioId();
		this.name = orig.getName();
		this.categoryId = orig.getCategoryId();
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
	public Account setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (Account)this;
	}
	@Column(name="PORTFOLIO_ID",nullable=false)
	public Integer getPortfolioId(){
		return portfolioId;
	}
	public Account setPortfolioId(Integer portfolioId) throws Exception  {
		Integer oldValue = getPortfolioId();
		if (!isSame(portfolioId, oldValue)) {
			this.portfolioId = portfolioId;
			setProperty("PORTFOLIO_ID", portfolioId, oldValue);
			portfolio=null;
		}
		return (Account)this;
	}
	@Column(name="NAME",nullable=false,length=32)
	public String getName(){
		return name;
	}
	public Account setName(String name) throws Exception  {
		String oldValue = getName();
		if (!isSame(name, oldValue)) {
			this.name = name;
			setProperty("NAME", name, oldValue);
		}
		return (Account)this;
	}
	@Column(name="CATEGORY_ID",nullable=true)
	public Integer getCategoryId(){
		return categoryId;
	}
	public Account setCategoryId(Integer categoryId) throws Exception  {
		Integer oldValue = getCategoryId();
		if (!isSame(categoryId, oldValue)) {
			this.categoryId = categoryId;
			setProperty("CATEGORY_ID", categoryId, oldValue);
			category=null;
		}
		return (Account)this;
	}
	public GeneralData getCategory() {
		if (category == null) {
			return getEntityManager().find(GeneralData.class, getCategoryId());
		}
		return category;
	}
	public Account setCategory(GeneralData category) throws Exception {
		setCategoryId(category==null?null:category.getId());
		this.category=category;
		return (Account)this;
	}
	public Portfolio getPortfolio() {
		if (portfolio == null) {
			return getEntityManager().find(Portfolio.class, getPortfolioId());
		}
		return portfolio;
	}
	public Account setPortfolio(Portfolio portfolio) throws Exception {
		setPortfolioId(portfolio==null?null:portfolio.getId());
		this.portfolio=portfolio;
		return (Account)this;
	}
	public List<Bill> getBills() {
		if (isNewInstance() || bills != null) {
			if (bills == null) {
				bills = new SortedList<Bill>();
			}
			return bills;
		}
		return getNamedCollection(Bill.class, "findByAccount", getId());
	}
	public Account addBill(Bill bill) throws Exception {
		bill.setAccount((Account)this);
		if(isNewInstance() || bills != null)
			getBills().add(bill);
		else
			bill.insert();
		return (Account)this;
	}
	public Account removeBill(Bill bill) throws Exception {
		if(isNewInstance() || bills != null)
			getBills().remove(bill);
		else
			bill.delete();
		return (Account)this;
	}
	public List<Transaction> getCreditTransactions() {
		if (isNewInstance() || creditTransactions != null) {
			if (creditTransactions == null) {
				creditTransactions = new SortedList<Transaction>();
			}
			return creditTransactions;
		}
		return getNamedCollection(Transaction.class, "findByCreditAccount", getId());
	}
	public Account addCreditTransaction(Transaction creditTransaction) throws Exception {
		creditTransaction.setCreditAccount((Account)this);
		if(isNewInstance() || creditTransactions != null)
			getCreditTransactions().add(creditTransaction);
		else
			creditTransaction.insert();
		return (Account)this;
	}
	public Account removeCreditTransaction(Transaction creditTransaction) throws Exception {
		if(isNewInstance() || creditTransactions != null)
			getCreditTransactions().remove(creditTransaction);
		else
			creditTransaction.delete();
		return (Account)this;
	}
	public List<Transaction> getDebitTransactions() {
		if (isNewInstance() || debitTransactions != null) {
			if (debitTransactions == null) {
				debitTransactions = new SortedList<Transaction>();
			}
			return debitTransactions;
		}
		return getNamedCollection(Transaction.class, "findByDebitAccount", getId());
	}
	public Account addDebitTransaction(Transaction debitTransaction) throws Exception {
		debitTransaction.setDebitAccount((Account)this);
		if(isNewInstance() || debitTransactions != null)
			getDebitTransactions().add(debitTransaction);
		else
			debitTransaction.insert();
		return (Account)this;
	}
	public Account removeDebitTransaction(Transaction debitTransaction) throws Exception {
		if(isNewInstance() || debitTransactions != null)
			getDebitTransactions().remove(debitTransaction);
		else
			debitTransaction.delete();
		return (Account)this;
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

	public Account setPropertyValues(Map<String,Object> data) throws Exception  {
		for(String key:data.keySet())
			setPropertyValue(key, data.get(key).toString());
		return (Account)this;
	}

	@Override
	public Object getPropertyValue(String property) {
		return getPropertyValue(PROPERTY.valueOf(formatProperty(property)));
	}
	public Object getPropertyValue(PROPERTY property) {
		switch (property) {
			case ID: return getId();
			case PORTFOLIO_ID: return getPortfolioId();
			case NAME: return getName();
			case CATEGORY_ID: return getCategoryId();
		}
		return null;
	}

	@Override
	public Account setPropertyValue(String property, String value) throws Exception  {
		if(property == null) return (Account)this;
		return setPropertyValue(PROPERTY.valueOf(formatProperty(property)),value);
	}

	public Account setPropertyValue(PROPERTY property, String value) throws Exception  {
		switch (property) {
			case ID:setId(Integer.valueOf(value)); break;
			case PORTFOLIO_ID:setPortfolioId(Integer.valueOf(value)); break;
			case NAME:setName(String.valueOf(value)); break;
			case CATEGORY_ID:setCategoryId(Integer.valueOf(value)); break;
		}
		return (Account)this;
	}

	public Account copy() throws Exception {
		Account cp = new Account(getEntityManager(), (Account)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(AccountDAO cp) throws Exception {
		super.copyChildrenTo(cp);
		for(Bill child:getBills())
			cp.addBill(child.copy());
		for(Transaction child:getCreditTransactions())
			cp.addCreditTransaction(child.copy());
		for(Transaction child:getDebitTransactions())
			cp.addDebitTransaction(child.copy());
	}
	public Vector<String> getDifference(AccountDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getPortfolioId(),o.getPortfolioId())) diffs.add("PORTFOLIO_ID");
		if(!isSame(getName(),o.getName())) diffs.add("NAME");
		if(!isSame(getCategoryId(),o.getCategoryId())) diffs.add("CATEGORY_ID");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
		if(portfolio != null && portfolio.isNewInstance())
				portfolio.insert();
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getPortfolioId()))
			 throw new Exception("PORTFOLIO_ID is required.");
		if (isNull(getName()))
			 throw new Exception("NAME is required.");
	}
	@Override
	public void insertChildren() throws Exception {
		if (bills != null) {
			for (Bill bill : getBills()) {
				bill.setAccount((Account)this);
			}
		}
		if (creditTransactions != null) {
			for (Transaction creditTransaction : getCreditTransactions()) {
				creditTransaction.setCreditAccount((Account)this);
			}
		}
		if (debitTransactions != null) {
			for (Transaction debitTransaction : getDebitTransactions()) {
				debitTransaction.setDebitAccount((Account)this);
			}
		}
		if (bills != null) {
			for (Bill bill : getBills()) {
				bill.insert();
			}
			bills = null;
		}
		if (creditTransactions != null) {
			for (Transaction creditTransaction : getCreditTransactions()) {
				creditTransaction.insert();
			}
			creditTransactions = null;
		}
		if (debitTransactions != null) {
			for (Transaction debitTransaction : getDebitTransactions()) {
				debitTransaction.insert();
			}
			debitTransactions = null;
		}
	}
}
