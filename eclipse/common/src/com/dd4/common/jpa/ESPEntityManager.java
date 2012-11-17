package com.dd4.common.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import com.dd4.common.jpa.criteria.CriteriaBuilderImpl;


public class ESPEntityManager implements EntityManager {
	
	private ESPEntityManagerFactory emf;

	public ESPEntityManager(ESPEntityManagerFactory emf){
		this.emf = emf;
	}
	public void clear() {
		// TODO Auto-generated method stub
	}

	public void close() {
		// TODO Auto-generated method stub
	}

	public boolean contains(Object entity) {
		return emf.getCache().contains(entity.getClass(), entity);
	}

	public Query createNamedQuery(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> c) { 
		String query = ESPCache.getNamedQuery(name,c);
		if(query == null)
			query = ESPCache.getNamedQuery(name,c.getSuperclass());
		return new ESPTypedQuery<T>(this,name,query,c);
	}

	public Query createNativeQuery(String sqlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Query createNativeQuery(String arg0, Class c) {
		// TODO Auto-generated method stub
		return null;
	}

	public Query createNativeQuery(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Query createQuery(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> TypedQuery<T> createQuery(String jpql, Class<T> c) {
		return new ESPTypedQuery<T>(this,null,jpql,c);
	}

	public void detach(Object o) {
		// TODO Auto-generated method stub
	}

	public <T> T find(Class<T> c, Object pk) {
		try {
			return emf.getCache().find(c,(PrimaryKey)pk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T find(Class<T> c, Object pk, Map<String, Object> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T find(Class<T> c, Object pk, LockModeType mode) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T find(Class<T> c, Object pk, LockModeType mode, Map<String, Object> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	public void flush() {
		// TODO Auto-generated method stub
	}
	
	public CriteriaBuilder getCriteriaBuilder() {
		return getCriteriaBuilderImpl();
	}
	
	public CriteriaBuilder getCriteriaBuilderImpl() {
		return new CriteriaBuilderImpl();
	}

	public Object getDelegate() {
		// TODO Auto-generated method stub
		return null;
	}

	public ESPEntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	public FlushModeType getFlushMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public LockModeType getLockMode(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    // 266912: Criteria API and Metamodel API (See Ch 5 of the JPA 2.0 Specification)
    /** Reference to the Metamodel for this deployment and session. 
     * Please use the accessor and not the instance variable directly*/
    private Metamodel metaModel; 
	public Metamodel getMetamodel() {
		 return metaModel;
	}
	
	public Map<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getReference(Class<T> arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityTransaction getTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	public void joinTransaction() {
		// TODO Auto-generated method stub
	}

	public void lock(Object arg0, LockModeType arg1) {
		// TODO Auto-generated method stub
	}

	public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
		// TODO Auto-generated method stub
	}

	public <T> T merge(T entity){
		try{
			return emf.getCache().merge(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void persist(Object entity) {
		try {
			emf.getCache().persist(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refresh(Object entity) {
		try {
			emf.getCache().refresh(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refresh(Object arg0, Map<String, Object> arg1) {
		// TODO Auto-generated method stub
	}

	public void refresh(Object arg0, LockModeType arg1) {
		// TODO Auto-generated method stub
	}

	public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
		// TODO Auto-generated method stub
	}

	public void remove(Object entity) {
		try {
			emf.getCache().remove(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFlushMode(FlushModeType arg0) {
		// TODO Auto-generated method stub
	}

	public void setProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	public <T> T unwrap(Class<T> c) {
		try {
			return c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
