package com.digitald4.common.component;

import java.util.ArrayList;

public class TopNavItem extends SubNavItem {
	private ArrayList<SubNavItem> subItems = new ArrayList<SubNavItem>();

	public TopNavItem(String shortName, String name) {
		super(shortName, name);
	}

	public TopNavItem addSubItem(SubNavItem subItem) {
		subItems.add(subItem);
		return this;
	}

	public ArrayList<SubNavItem> getSubItems() {
		return subItems;
	}

	public boolean contains(String selected) {
		for (SubNavItem subMenu : getSubItems()) {
			if(subMenu.getShortName().equals(selected))
				return true;
		}
		return false;
	}
}
