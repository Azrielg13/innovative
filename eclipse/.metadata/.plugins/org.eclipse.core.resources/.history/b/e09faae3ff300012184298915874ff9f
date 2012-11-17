package com.dd4.common.jpa.criteria;


/**
 * A single-column projection that may be aliased
 * 
 */
public abstract class SimpleProjection implements Projection {

	public Projection as(String alias) {
		return Projections.alias(this, alias);
	}

	public String[] getAliases() {
		return new String[1];
	}

	@Override
	public String toGroupJpql(CriteriaQuery criteriaQuery) {
		throw new UnsupportedOperationException("not a grouping projection");
	}

	public boolean isGrouped() {
		return false;
	}

}
