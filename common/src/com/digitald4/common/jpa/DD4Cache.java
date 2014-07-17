package com.digitald4.common.jpa;

import java.util.List;

import javax.persistence.Cache;
import javax.persistence.TypedQuery;

public interface DD4Cache extends Cache {
	enum NULL_TYPES{IS_NULL, IS_NOT_NULL};
	
	public <T> T find(Class<T> c, PrimaryKey pk) throws Exception;
	
	/*public <T> T persist(T entity) throws Exception;
	
	public <T> List<T> find(DD4TypedQuery<T> tq) throws Exception;
	
	public <T> T merge(T entity) throws Exception;
	
	public <T> T refresh(T entity) throws Exception;
	
	public <T> T remove(T entity) throws Exception;*/
	
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> c);
}
