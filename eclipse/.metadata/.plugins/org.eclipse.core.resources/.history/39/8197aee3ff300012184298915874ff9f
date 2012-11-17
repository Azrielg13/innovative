package com.dd4.common.jpa.criteria;


/**
 */
public class Distinct implements Projection {

	private final Projection projection;

	public Distinct(Projection projection) {
		this.projection = projection;
	}

	public Distinct() {
		this.projection = null;
	}

	public String[] getAliases() {
		if (projection != null) {
			return projection.getAliases();
		} else {
			return new String[] {};
		}
	}

	public boolean isGrouped() {
		if (projection != null) {
			return projection.isGrouped();
		} else {
			return false;
		}
	}

	public String toString() {
		return "distinct " + projection.toString();
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery, int position) {

		if (projection == null) {
			return "DISTINCT " + criteriaQuery.getAlias();
		} else {
			return "DISTINCT " + projection.toJpql(criteriaQuery, position);
		}
	}

	@Override
	public String toGroupJpql(CriteriaQuery criteriaQuery) {

		return projection.toGroupJpql(criteriaQuery);
	}
}
