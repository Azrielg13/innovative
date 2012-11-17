package com.dd4.common.jpa.criteria;


/**
 * An aggregation
 * 
 */
public class AggregateProjection extends SimpleProjection {

	protected final String propertyName;
	private final String aggregate;

	protected AggregateProjection(String aggregate, String propertyName) {
		this.aggregate = aggregate;
		this.propertyName = propertyName;
	}

	public String toString() {
		return aggregate + "(" + propertyName + ')';
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery, int position) {
		return aggregate + '(' + criteriaQuery.getAlias() + "." + propertyName + ')';
	}

}
