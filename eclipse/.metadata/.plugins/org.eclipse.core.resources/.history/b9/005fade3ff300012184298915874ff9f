package com.dd4.common.jpa.criteria;


/**
 * Superclass of binary logical expressions
 * 
 */
public class LogicalExpression implements Criterion {

	private final Criterion lhs;
	private final Criterion rhs;
	private final String op;

	protected LogicalExpression(Criterion lhs, Criterion rhs, String op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

	public String getOp() {
		return op;
	}

	public String toString() {
		return lhs.toString() + ' ' + getOp() + ' ' + rhs.toString();
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery) {
		return '(' + lhs.toJpql(criteriaQuery) + ' ' + getOp() + ' ' + rhs.toJpql(criteriaQuery) + ')';
	}
}
