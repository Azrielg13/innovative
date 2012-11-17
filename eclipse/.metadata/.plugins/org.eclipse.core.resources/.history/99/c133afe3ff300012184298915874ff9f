package com.dd4.common.jpa.criteria;


/**
 * An identifier constraint
 * 
 */
public class IdentifierEqExpression implements Criterion {

	private final Object value;

	protected IdentifierEqExpression(Object value) {
		this.value = value;
	}

	public String toString() {
		return "id = " + value;
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery) {

		Class<?> entityClass = criteriaQuery.getEntityClass();
		String idPropertyName = JPAUtils.findEntityIdProperty(entityClass);
		if (idPropertyName == null) {
			throw new IllegalArgumentException("Id of entity class could not be found: " + entityClass);
		}

		String aliasedPropertyName = criteriaQuery.getAliasedPropertyName(idPropertyName);

		return aliasedPropertyName + "=" + criteriaQuery.getJpaValue(value);
	}

}
