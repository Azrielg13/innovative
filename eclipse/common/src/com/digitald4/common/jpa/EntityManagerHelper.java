package com.digitald4.common.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {

	private static EntityManagerFactory emf;
	
	public static void init(String url, String username, String password) throws Exception{
		init("ESPJPA",url,username,password,username);
	}
	public static void init(String url, String username, String password, String appUserName) throws Exception{
		init("ESPJPA",url,username,password,appUserName);
	}
	public static void init(String persistenceUnit, String url, String username, String password, String appUserName) throws Exception{
		HashMap<String,Object> properties = new HashMap<String,Object>();
		
//		properties.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
		properties.put("javax.persistence.jdbc.driver", "oracle.jdbc.OracleDriver");
		properties.put("javax.persistence.jdbc.url", url);
		properties.put("javax.persistence.jdbc.user", username);
		properties.put("ApplicationUser", appUserName);
		properties.put("javax.persistence.jdbc.password", password);
		init(persistenceUnit,properties);
	}
	
	public static void init(String persistenceUnit, Map<String, Object> map) throws Exception{
		if(persistenceUnit.equals("ESPJPA"))
			emf = (EntityManagerFactory)Class.forName("com.sce.esp.object.jpa.ESPEntityManagerFactory").getConstructor(Map.class).newInstance(map);
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