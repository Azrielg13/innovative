package com.dd4.common.jpa.criteria;


/**
 * Implementation of AbstractEmptinessExpression.
 * 
 */
public abstract class AbstractEmptinessExpression extends AbstractPropertyBasedCriterion {

	protected AbstractEmptinessExpression(String propertyName) {
		super(propertyName);
	}

	protected abstract boolean excludeEmpty();

	public final String toString() {
		return propertyName + (excludeEmpty() ? " is not empty" : " is empty");
	}

	public String toJpql(CriteriaQuery criteriaQuery) {

		return criteriaQuery.getAliasedPropertyName(propertyName) + (excludeEmpty() ? " IS NOT EMPTY" : " IS EMPTY");
	}
}
