package com.dd4.common.jpa.criteria;

/**
 * A comparison between a constant value and the the result of a subquery
 * 
 */
public class SimpleSubqueryExpression extends SubqueryExpression {

	private Object value;

	protected SimpleSubqueryExpression(Object value, String op, String quantifier, CriteriaQuery subCriteria) {
		super(op, quantifier, subCriteria);
		this.value = value;
	}

	@Override
	protected String toLeftJpqlString(CriteriaQuery criteriaQuery) {
		return criteriaQuery.getJpaValue(value);
	}

}
