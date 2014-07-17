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

public class DD4EntityManagerFactory implements EntityManagerFactory {
	private DD4Cache cache;
	private DBConnector pdb;
	private Map<String, Object> properties;
	private EntityManager em;
	
	public DD4EntityManagerFactory() {
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public DD4EntityManagerFactory(Map properties) {
		this.properties = properties;
	}
	
	@Override
	public void close() {
	}

	@Override
	public EntityManager createEntityManager() {
		if (em == null) {
			if (getProperties().get("version") == null) {
				cache = new DD4CacheImpl(this);
				em = new DD4EntityManager(this, (DD4CacheImpl)cache);
			} else if (getProperties().get("version").equals("2")) {
				cache = new DD4CacheImplV2(this);
				em = new DD4EntityManagerV2(this, cache);
			}
		}
		return em;
	}

	@Override	
	@SuppressWarnings("rawtypes")
	public EntityManager createEntityManager(Map map) {
		return createEntityManager();
	}
	
	@Override
	public DD4Cache getCache() {
		return cache;
	}

	@Override	
	public CriteriaBuilder getCriteriaBuilder() {
		return null;
	}

	@Override	
	public Metamodel getMetamodel() {
		return null;
	}

	@Override	
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return null;
	}

	@Override	
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override	
	public boolean isOpen() {
		return true;
	}
	
	public Connection getConnection() throws Exception {
		if (pdb==null) {
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
