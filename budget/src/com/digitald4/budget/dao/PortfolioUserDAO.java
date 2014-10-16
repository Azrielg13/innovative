package com.digitald4.budget.dao;
/**Copy Right Frank todo */
/**Description of class, (we need to get this from somewhere, database? xml?)*/
import com.digitald4.common.model.GeneralData;
import com.digitald4.budget.model.Portfolio;
import com.digitald4.budget.model.PortfolioUser;
import com.digitald4.common.model.User;
import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
public abstract class PortfolioUserDAO extends DataAccessObject{
	public enum KEY_PROPERTY{ID};
	public enum PROPERTY{ID,PORTFOLIO_ID,USER_ID,ROLE_ID};
	private Integer id;
	private Integer portfolioId;
	private Integer userId;
	private Integer roleId;
	private Portfolio portfolio;
	private GeneralData role;
	private User user;
	public static PortfolioUser getInstance(Integer id){
		return getInstance(id, true);
	}
	public static PortfolioUser getInstance(Integer id, boolean fetch){
		if(isNull(id))return null;
		EntityManager em = EntityManagerHelper.getEntityManager();
		PrimaryKey pk = new PrimaryKey(id);
		Cache cache = em.getEntityManagerFactory().getCache();
		PortfolioUser o = null;
		if(fetch || cache != null && cache.contains(PortfolioUser.class, pk))
			o = em.find(PortfolioUser.class, pk);
		return o;
	}
	public static List<PortfolioUser> getAll(){
		return getNamedCollection("findAll");
	}
	public static List<PortfolioUser> getAllActive(){
		return getNamedCollection("findAllActive");
	}
	public static List<PortfolioUser> getCollection(String[] props, Object... values){
		String qlString = "SELECT o FROM PortfolioUser o";
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
	public synchronized static List<PortfolioUser> getCollection(String jpql, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<PortfolioUser> tq = em.createQuery(jpql,PortfolioUser.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public synchronized static List<PortfolioUser> getNamedCollection(String name, Object... values){
		EntityManager em = EntityManagerHelper.getEntityManager();
		TypedQuery<PortfolioUser> tq = em.createNamedQuery(name,PortfolioUser.class);
		if(values != null && values.length > 0){
			int p=1;
			for(Object value:values)
				if(value != null)
					tq = tq.setParameter(p++, value);
		}
		return tq.getResultList();
	}
	public PortfolioUserDAO(){}
	public PortfolioUserDAO(Integer id){
		this.id=id;
	}
	public PortfolioUserDAO(PortfolioUserDAO orig){
		super(orig);
		this.id=orig.getId();
		copyFrom(orig);
	}
	public void copyFrom(PortfolioUserDAO orig){
		this.portfolioId=orig.getPortfolioId();
		this.userId=orig.getUserId();
		this.roleId=orig.getRoleId();
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
	@Column(name="ID",nullable=false)
	public Integer getId(){
		return id;
	}
	public PortfolioUser setId(Integer id) throws Exception  {
		Integer oldValue = getId();
		if (!isSame(id, oldValue)) {
			this.id = id;
			setProperty("ID", id, oldValue);
		}
		return (PortfolioUser)this;
	}
	@Column(name="PORTFOLIO_ID",nullable=false)
	public Integer getPortfolioId(){
		return portfolioId;
	}
	public PortfolioUser setPortfolioId(Integer portfolioId) throws Exception  {
		Integer oldValue = getPortfolioId();
		if (!isSame(portfolioId, oldValue)) {
			this.portfolioId = portfolioId;
			setProperty("PORTFOLIO_ID", portfolioId, oldValue);
			portfolio=null;
		}
		return (PortfolioUser)this;
	}
	@Column(name="USER_ID",nullable=false)
	public Integer getUserId(){
		return userId;
	}
	public PortfolioUser setUserId(Integer userId) throws Exception  {
		Integer oldValue = getUserId();
		if (!isSame(userId, oldValue)) {
			this.userId = userId;
			setProperty("USER_ID", userId, oldValue);
			user=null;
		}
		return (PortfolioUser)this;
	}
	@Column(name="ROLE_ID",nullable=true)
	public Integer getRoleId(){
		return roleId;
	}
	public PortfolioUser setRoleId(Integer roleId) throws Exception  {
		Integer oldValue = getRoleId();
		if (!isSame(roleId, oldValue)) {
			this.roleId = roleId;
			setProperty("ROLE_ID", roleId, oldValue);
			role=null;
		}
		return (PortfolioUser)this;
	}
	public Portfolio getPortfolio(){
		if(portfolio==null)
			portfolio=Portfolio.getInstance(getPortfolioId());
		return portfolio;
	}
	public PortfolioUser setPortfolio(Portfolio portfolio) throws Exception {
		setPortfolioId(portfolio==null?null:portfolio.getId());
		this.portfolio=portfolio;
		return (PortfolioUser)this;
	}
	public GeneralData getRole(){
		if(role==null)
			role=GeneralData.getInstance(getRoleId());
		return role;
	}
	public PortfolioUser setRole(GeneralData role) throws Exception {
		setRoleId(role==null?null:role.getId());
		this.role=role;
		return (PortfolioUser)this;
	}
	public User getUser(){
		if(user==null)
			user=User.getInstance(getUserId());
		return user;
	}
	public PortfolioUser setUser(User user) throws Exception {
		setUserId(user==null?null:user.getId());
		this.user=user;
		return (PortfolioUser)this;
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
			case PORTFOLIO_ID: return getPortfolioId();
			case USER_ID: return getUserId();
			case ROLE_ID: return getRoleId();
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
			case PORTFOLIO_ID:setPortfolioId(Integer.valueOf(value)); break;
			case USER_ID:setUserId(Integer.valueOf(value)); break;
			case ROLE_ID:setRoleId(Integer.valueOf(value)); break;
		}
	}
	public PortfolioUser copy() throws Exception {
		PortfolioUser cp = new PortfolioUser((PortfolioUser)this);
		copyChildrenTo(cp);
		return cp;
	}
	public void copyChildrenTo(PortfolioUserDAO cp) throws Exception {
		super.copyChildrenTo(cp);
	}
	public Vector<String> getDifference(PortfolioUserDAO o){
		Vector<String> diffs = super.getDifference(o);
		if(!isSame(getId(),o.getId())) diffs.add("ID");
		if(!isSame(getPortfolioId(),o.getPortfolioId())) diffs.add("PORTFOLIO_ID");
		if(!isSame(getUserId(),o.getUserId())) diffs.add("USER_ID");
		if(!isSame(getRoleId(),o.getRoleId())) diffs.add("ROLE_ID");
		return diffs;
	}
	@Override
	public void insertParents() throws Exception {
	}
	@Override
	public void insertPreCheck() throws Exception {
		if (isNull(getId()))
			 throw new Exception("ID is required.");
		if (isNull(getPortfolioId()))
			 throw new Exception("PORTFOLIO_ID is required.");
		if (isNull(getUserId()))
			 throw new Exception("USER_ID is required.");
	}
	@Override
	public void insertChildren() throws Exception {
	}
}
