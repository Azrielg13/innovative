package com.dd4.common.jpa.criteria;

/**
 * An avg() projection
 *
 */
public class AvgProjection extends AggregateProjection {

	public AvgProjection(String propertyName) {
		super("AVG", propertyName);
	}
	
}
