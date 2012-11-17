package com.dd4.common.jpa.criteria;

import java.io.Serializable;

/**
 * Represents an order imposed upon a <tt>CriteriaQuery</tt> result set
 * 
 */
public class Order implements Serializable {

	private boolean ascending;
//	private boolean ignoreCase;
	private String propertyName;

	public String toString() {
		return propertyName + ' ' + (ascending ? "asc" : "desc");
	}

	// public Order ignoreCase() {
	// ignoreCase = true;
	// return this;
	// }

	/**
	 * Constructor for Order.
	 */
	protected Order(String propertyName, boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}

	public String toJpql(CriteriaQuery criteriaQuery) {

		return criteriaQuery.getAliasedPropertyName(propertyName) + (ascending ? " ASC" : " DESC");
	}

	/**
	 * Ascending order
	 * 
	 * @param propertyName
	 * @return Order
	 */
	public static Order asc(String propertyName) {
		return new Order(propertyName, true);
	}

	/**
	 * Descending order
	 * 
	 * @param propertyName
	 * @return Order
	 */
	public static Order desc(String propertyName) {
		return new Order(propertyName, false);
	}

}
