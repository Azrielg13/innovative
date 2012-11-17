package com.dd4.common.jpa.criteria;


/**
 * Constrains a property to be null
 * 
 */
public class NullExpression extends AbstractPropertyBasedCriterion {

	public NullExpression(String propertyName) {
		super(propertyName);
	}

	public String toString() {
		return propertyName + " is null";
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery) {
		return criteriaQuery.getAliasedPropertyName(propertyName) + " IS NULL";
	}

}
