package com.dd4.common.jpa.criteria;


/**
 * Constrains the property to a specified list of values
 * 
 */
public class InExpression extends AbstractPropertyBasedCriterion {

	private final Object[] values;

	protected InExpression(String propertyName, Object[] values) {
		super(propertyName);
		this.values = values;
	}

	public String toString() {

		StringBuilder valuesStr = getValuesAsString();

		return propertyName + " in (" + valuesStr + ')';
	}

	@Override
	public String toJpql(CriteriaQuery criteriaQuery) {
		return criteriaQuery.getAliasedPropertyName(propertyName) + " IN (" + getValuesAsString() + ')';
	}

	private StringBuilder getValuesAsString() {

		StringBuilder valuesStr = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			valuesStr.append(values[i]);
			if (i != values.length - 1) {
				valuesStr.append(",");
			}
		}
		return valuesStr;
	}
}
