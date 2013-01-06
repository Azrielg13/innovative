package com.digitald4.common.component;

import java.util.Collection;

public class Navigation {
	public static Navigation navigation;
	private final Collection<TopNavItem> navItems;
	
	public static Navigation getInstance() {
		return navigation;
	}
	
	public static void setNavigation(Navigation _navigation) {
		navigation = _navigation;
	}
	
	public Navigation(Collection<TopNavItem> navItems) {
		this.navItems = navItems;
	}
	
	public Collection<TopNavItem> getNavItems() {
		return navItems;
	}
}
