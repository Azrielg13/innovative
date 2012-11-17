package com.dd4.common.jpa;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

public class ESPPersistenceProvider implements PersistenceProvider {

	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo pu, Map properties) {
		try {
			EntityManagerHelper.init(pu.getPersistenceUnitName(), properties);
			return EntityManagerHelper.getEntityManagerFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public EntityManagerFactory createEntityManagerFactory(String name, Map properties) {
		try {
			EntityManagerHelper.init(name, properties);
			return EntityManagerHelper.getEntityManagerFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ProviderUtil getProviderUtil() {
		return new ESPProviderUtil();
	}

}
