package com.dd4.common.jpa.criteria;


/**
 * A row count
 * 
 */
public class RowCountProjection extends SimpleProjection {

	protected RowCountProjection() {
	}

	public String toString() {
		return "count(*)";
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery, int position) {
		return "COUNT(" + criteriaQuery.getAlias() + ")";
	}

}
