package com.digitald4.common.jpa;

import java.sql.Connection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.log.EspLogger;

public class ESPEntityManagerFactory implements EntityManagerFactory {
	private ESPCache cache = new ESPCache(this);
	private DBConnector pdb;
	private Map<String,Object> properties;
	private ESPEntityManager em;
	
	public ESPEntityManagerFactory(){
	}
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ESPEntityManagerFactory(Map properties){
		this.properties = properties;
	}
	public void close() {
	}

	public EntityManager createEntityManager() {
		if(em == null)
			em = new ESPEntityManager(this);
		return em;
	}
	
	@SuppressWarnings("rawtypes")
	public EntityManager createEntityManager(Map map) {
		if(em == null)
			em = new ESPEntityManager(this);
		return em;
	}
	public ESPCache getCache() {
		return cache;
	}
	public CriteriaBuilder getCriteriaBuilder() {
		return null;
	}
	public Metamodel getMetamodel() {
		return null;
	}
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return null;
	}
	public Map<String, Object> getProperties() {
		return properties;
	}
	public boolean isOpen() {
		return true;
	}
	public Connection getConnection() throws Exception{
		if(pdb==null){
			try {
				properties = getProperties();
				EspLogger.message(this,"javax.persistence.jdbc.url: "+properties.get("javax.persistence.jdbc.url"));
				pdb = DBConnector.getInstance(""+properties.get("javax.persistence.jdbc.url"),
						""+properties.get("javax.persistence.jdbc.user"),
						""+properties.get("javax.persistence.jdbc.password"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pdb.getConnection();
	}
}
