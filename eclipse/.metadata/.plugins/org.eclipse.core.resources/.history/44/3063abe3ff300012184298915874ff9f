package com.dd4.common.jpa.criteria;


public class AllProjection implements Projection {

	@Override
	public String[] getAliases() {
		return new String[1];
	}

	@Override
	public boolean isGrouped() {
		return false;
	}

	@Override
	public String toGroupJpql(CriteriaQuery criteriaQuery) {
		throw new UnsupportedOperationException("not a grouping projection");
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery, int position) {
		return criteriaQuery.getAlias();
	}

}
