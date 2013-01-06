package com.digitald4.common.component;

public class SubNavItem {
	private final String shortName;
	private final String name;

	public SubNavItem(String shortName, String name) {
		this.shortName = shortName;
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public String getName() {
		return name;
	}
}
