package com.digitald4.common.jpa;

import java.util.Hashtable;

import javax.persistence.TypedQuery;

import com.digitald4.common.jdbc.ESPHashtable;

public class DD4CacheImplV2 implements DD4Cache {
	private final DD4EntityManagerFactory emf;
	private ESPHashtable<Class<?>, ESPHashtable<String, Object>> hashById = new ESPHashtable<Class<?>, ESPHashtable<String, Object>>(199);
	private Hashtable<Class<?>, PropertyCollectionFactory<?>> propFactories = new Hashtable<Class<?>, PropertyCollectionFactory<?>>();
	private Hashtable<String, DD4TypedQueryImplV2<?>> queries = new Hashtable<String, DD4TypedQueryImplV2<?>>();

	public DD4CacheImplV2(DD4EntityManagerFactory emf){
		this.emf = emf;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean contains(Class c, Object o) {
		ESPHashtable<String,Object> classHash = hashById.get(c);
		if (classHash != null) {
			return classHash.containsKey(((Entity)o).getHashKey());
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void evict(Class c) {
		hashById.remove(c);
		propFactories.remove(c);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void evict(Class c, Object o) {
		ESPHashtable<String,Object> classHash = hashById.get(c);
		if (classHash != null) { 
			classHash.remove(((Entity)o).getHashKey());
		}
		@SuppressWarnings("unchecked")
		PropertyCollectionFactory<Object> pcf = getPropertyCollectionFactory(false, c);
		if (pcf != null) {
			pcf.evict(o);
		}
	}
	
	@Override
	public void evictAll() {
		hashById.clear();
		propFactories.clear();
	}
	
	@Override
	public <T> T find(Class<T> c, PrimaryKey pk) throws Exception {
		return getCachedObj(c, pk);
	}
	
	@Override
	public <T> void reCache(T o) {
		@SuppressWarnings("unchecked")
		Class<T> c = (Class<T>)o.getClass();
		PropertyCollectionFactory<T> pcf = getPropertyCollectionFactory(false, c);
		if (pcf != null) {
			pcf.evict(o);
			pcf.cache(o);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCachedObj(Class<T> c, Object o) {
		ESPHashtable<String,Object> classHash = hashById.get(c);
		if (classHash == null) { 
			return null;
		}
		return (T)classHash.get(((Entity)o).getHashKey());
	}
	
	public <T> PropertyCollectionFactory<T> getPropertyCollectionFactory(boolean create, Class<T> c) {
		@SuppressWarnings("unchecked")
		PropertyCollectionFactory<T> pcf = (PropertyCollectionFactory<T>)propFactories.get(c);
		if (pcf == null && create) {
			pcf = new PropertyCollectionFactory<T>();
			propFactories.put(c, pcf);
		}
		return pcf;
	}
	
	@Override
	public <T> void put(T o) {
		ESPHashtable<String, Object> classHash = hashById.get(o.getClass());
		if (classHash == null) {
			classHash = new ESPHashtable<String, Object>(199);
			hashById.put(o.getClass(), classHash);
		}
		classHash.put(((Entity)o).getHashKey(), o);
	}

	@Override
	public <T> TypedQuery<T> createQuery(String query, Class<T> c) {
		@SuppressWarnings("unchecked")
		DD4TypedQueryImplV2<T> cached = (DD4TypedQueryImplV2<T>)queries.get(query);
		if (cached != null) {
			return new DD4TypedQueryImplV2<T>(cached);
		}
		cached = new DD4TypedQueryImplV2<T>(emf.createEntityManager(), null, query, c);
		queries.put(query, cached);
		return cached;
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> c) { 
		String key = c.getName() + "." + name;
		@SuppressWarnings("unchecked")
		DD4TypedQueryImplV2<T> cached = (DD4TypedQueryImplV2<T>)queries.get(key);
		if (cached != null) {
			return new DD4TypedQueryImplV2<T>(cached);
		}
		cached = new DD4TypedQueryImplV2<T>(emf.createEntityManager(), name, null, c);
		queries.put(key, cached);
		return cached;
	}
}
