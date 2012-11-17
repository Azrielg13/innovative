package com.dd4.common.jpa.criteria;


/**
 * A property value, or grouped property value
 * 
 */
public class IdentifierProjection extends SimpleProjection {

	protected IdentifierProjection() {
	}

	public String toString() {
		return "id";
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery, int position) {

		Class<?> entityClass = criteriaQuery.getEntityClass();
		String idPropertyName = JPAUtils.findEntityIdProperty(entityClass);
		if (idPropertyName == null) {
			throw new IllegalArgumentException("Id of entity class could not be found: " + entityClass);
		}

		return criteriaQuery.getAliasedPropertyName(idPropertyName);
	}

}
