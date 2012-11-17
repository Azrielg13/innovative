package com.dd4.common.jpa.criteria;

public class ExistsSubqueryExpression extends SubqueryExpression {

	@Override
	protected String toLeftJpqlString(CriteriaQuery criteriaQuery) {
		return null;
	}
	
	protected ExistsSubqueryExpression(String quantifier, CriteriaQuery subCriteria) {
		super(null, quantifier, subCriteria);
	}
}
