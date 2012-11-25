package com.digitald4.common.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {

	private static EntityManagerFactory emf;
	
	public static void init(String dbDriver, String url, String username, String password) throws Exception{
		init("ESPJPA",dbDriver,url,username,password);
	}
	public static void init(String persistenceUnit, String dbDriver, String dbUrl, String username, String password) throws Exception{
		HashMap<String,Object> properties = new HashMap<String,Object>();
		
//		properties.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
		properties.put("javax.persistence.jdbc.driver", dbDriver);
		properties.put("javax.persistence.jdbc.url", dbUrl);
		properties.put("javax.persistence.jdbc.user", username);
		properties.put("javax.persistence.jdbc.password", password);
		init(persistenceUnit,properties);
	}
	
	public static void init(String persistenceUnit, Map<String, Object> map) throws Exception{
		if(persistenceUnit.equals("ESPJPA"))
			emf = (EntityManagerFactory)Class.forName("com.digitald4.common.jpa.ESPEntityManagerFactory").getConstructor(Map.class).newInstance(map);
		else
			emf = Persistence.createEntityManagerFactory(persistenceUnit, map);
	}
	
	public static EntityManagerFactory getEntityManagerFactory(){
		return emf;
	}

	public static EntityManager getEntityManager(){
		if(emf==null)
			return null;
		return emf.createEntityManager();
	}
}