package com.dd4.common.jpa.criteria;

public enum ReservedValue {
	
	CURRENT_DATE("CURRENT_DATE");
	
	private final String value;

	private ReservedValue(String value) {
		this.value = value;
	}

	public String getStringValue() {
		return value;
	}
}