package com.dd4.common.jpa.criteria;


/**
 * A SQL fragment. The string {alias} will be replaced by the alias of the root
 * entity.
 */
public class SQLCriterion implements Criterion {

	private final String sql;

	public String toString() {
		return sql;
	}

	protected SQLCriterion(String sql) {
		this.sql = sql;
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery) {
		return sql.replaceAll("\\{alias\\}", criteriaQuery.getAlias());
	}

}
