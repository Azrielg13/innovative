package com.dd4.common.jpa.criteria;


public class AliasedProjection implements Projection {

	private final Projection projection;
	private final String alias;

	public String toString() {
		return projection.toString() + " as " + alias;
	}

	protected AliasedProjection(Projection projection, String alias) {
		this.projection = projection;
		this.alias = alias;
	}

	public String[] getAliases() {
		return new String[] { alias };
	}

	public boolean isGrouped() {
		return projection.isGrouped();
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery, int position) {
		return projection.toJpql(criteriaQuery, position) + " AS " + alias;
	}

	@Override
	public String toGroupJpql(CriteriaQuery criteriaQuery) {
		return projection.toGroupJpql(criteriaQuery);
	};
}
