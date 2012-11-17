package com.dd4.common.jpa.criteria;


public class EntityJoin implements Join {

	private final Class<?> entityClass;
	private final String alias;

	public EntityJoin(Class<?> entityClass, String alias) {
		this.entityClass = entityClass;
		this.alias = alias;
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery) {
		return entityClass.getSimpleName() + " AS " + alias;
	}

	@Override
	public String getAlias() {
		// TODO Auto-generated method stub
		return null;
	}

}
