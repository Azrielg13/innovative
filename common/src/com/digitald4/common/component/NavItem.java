package com.digitald4.common.component;

import java.util.ArrayList;

public class NavItem {
	private String name;
	private String url;
	private NavItem parent;
	private ArrayList<NavItem> subItems = new ArrayList<NavItem>();
	
	public NavItem(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setParent(NavItem parent) {
		this.parent = parent;
	}
	
	public NavItem getParent() {
		return parent;
	}

	public NavItem addSubItem(NavItem subItem) {
		subItems.add(subItem);
		subItem.setParent(this);
		return this;
	}

	public ArrayList<NavItem> getSubItems() {
		return subItems;
	}

	public boolean contains(String url) {
		for (NavItem subMenu : getSubItems()) {
			if (subMenu.getUrl().equals(url))
				return true;
		}
		return false;
	}

	public NavItem findNavItem(String url) {
		if (getUrl().equals(url)) return this;
		for (NavItem navItem : getSubItems()) {
			NavItem item = navItem.findNavItem(url);
			if (item != null) {
				return item;
			}
		}
		return null;
	}
}
