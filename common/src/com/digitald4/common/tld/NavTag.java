package com.digitald4.common.tld;

import java.util.Collection;

import com.digitald4.common.component.Navigation;
import com.digitald4.common.component.SubNavItem;
import com.digitald4.common.component.TopNavItem;

public class NavTag extends DD4Tag {
	private final static String START_NAV = "\t\t<nav id=\"main-nav\">\n\t\t\t<ul class=\"container_12\">\n";
	private final static String MAIN_MENU_OPEN = "\t\t\t\t<li class=\"%cn\"><a href=\"%sn\" title=\"%n\">%n</a>\n\t\t\t\t\t<ul>\n";
	private final static String MAIN_MENU_CLOSE = "\t\t\t\t\t</ul>\n\t\t\t\t</li>\n";
	private final static String SUB_MENU = "\t\t\t\t\t\t<li%cn><a href=\"%sn\" title=\"%n\">%n</a></li>\n";
	private final static String END_NAV = "\t\t\t</ul>\n\t\t</nav>\n";
	private String selected;
	private Navigation navigation;

	public void setSelected(String selected) {
		this.selected = selected;
	}
	
	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
	
	public String getOutput() {
		String out = START_NAV;
		for (TopNavItem top : navigation.getNavItems()) {
			out += MAIN_MENU_OPEN.replaceAll("%cn", top.getShortName()+(top.contains(selected)?" current":"")).replaceAll("%sn", top.getShortName()).replaceAll("%n", top.getName());
			for (SubNavItem sub : top.getSubItems()) {
				out += SUB_MENU.replaceAll("%cn", sub.getShortName().equals(selected)?" class=\"current\"":"").replaceAll("%sn", sub.getShortName()).replaceAll("%n", sub.getName());
			}
			out += MAIN_MENU_CLOSE;
		}
		out += END_NAV;
		return out;
	}

	public Collection<TopNavItem> getTopNavItems() {
		return navigation.getNavItems();
	}
}
