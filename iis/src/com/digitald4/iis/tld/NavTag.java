package com.digitald4.iis.tld;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class NavTag extends BodyTagSupport {
	private final static String START_NAV = "\t\t<nav id=\"main-nav\">\n\t\t\t<ul class=\"container_12\">";
	private final static String MAIN_MENU_OPEN = "\t\t\t\t<li class=\"%cn\"><a href=\"%sn\" title=\"%n\">%n</a>\n\t\t\t\t<ul>\n";
	private final static String MAIN_MENU_CLOSE = "\t\t\t\t</ul></li>";
	private final static String SUB_MENU = "\t\t\t\t\t<li%cn><a href=\"%sn\" title=\"%n\">%n</a></li>\n";
	private final static String END_NAV = "\t\t\t</ul>\n\t\t</nav>";
	private String selected;
	private ArrayList<TopNavItem> topNavItems = new ArrayList<TopNavItem>();

	public void setSelected(String selected) {
		this.selected = selected;
	}

	@Override
	public int doAfterBody() throws JspException {
		String body = null;
		try {
			BodyContent bc = getBodyContent();
			body = bc.getString();
			if (body != null) {
				parseBody(body);
			}
			JspWriter out = bc.getEnclosingWriter();
			out.print(getOutput());
		} catch (Exception ioe) {
			throw new JspException("Error: " + ioe.getMessage() + " for input: "+body);
		}
		return SKIP_BODY;
	}
	
	public String getOutput() {
		String out = START_NAV+"\n";
		for (TopNavItem top : topNavItems) {
			out += top.getHTMLEntry()+"\n";
		}
		out += END_NAV+"\n";
		return out;
	}

	public ArrayList<TopNavItem> getTopNavItems() {
		return topNavItems;
	}

	public void parseBody(String body) {
		topNavItems.clear();
		StringTokenizer st = new StringTokenizer(body,"\n");
		while (st.hasMoreTokens()) {
			String line = st.nextToken().trim();
			if (line.length() > 0) {
				String parent = line.substring(0,line.indexOf('-')).trim();
				TopNavItem top = new TopNavItem(parent);
				String children = line.substring(line.indexOf('-')+1).trim();
				StringTokenizer st2 = new StringTokenizer(children,",");
				while (st2.hasMoreElements()) {
					top.addSubItem(new SubNavItem(st2.nextToken()));
				}
				topNavItems.add(top);
			}
		}
	}

	private class SubNavItem {
		private String shortName;
		private String name;

		public SubNavItem(String unparsedNames) {
			this(unparsedNames.substring(0, unparsedNames.indexOf(':')).trim(), unparsedNames.substring(unparsedNames.indexOf(':')+1).trim());
		}

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

		public boolean isSelected() {
			return selected != null && selected.equalsIgnoreCase(getShortName());
		}

		public String getHTMLEntry() {
			return SUB_MENU.replaceAll("%cn", isSelected()?" class=\"current\"":"").replaceAll("%sn", getShortName()).replaceAll("%n", getName());
		}
	}

	private class TopNavItem extends SubNavItem{
		private ArrayList<SubNavItem> subItems = new ArrayList<SubNavItem>();

		public TopNavItem(String unparsedNames) {
			super(unparsedNames);
		}

		public TopNavItem(String shortName, String name) {
			super(shortName, name);
		}

		public void addSubItem(SubNavItem subItem) {
			subItems.add(subItem);
		}

		public ArrayList<SubNavItem> getSubItems() {
			return subItems;
		}

		public boolean isSelected() {
			for (SubNavItem subMenu : getSubItems()) {
				if(subMenu.isSelected())
					return true;
			}
			return false;
		}

		@Override
		public String getHTMLEntry() {
			String html = MAIN_MENU_OPEN.replaceAll("%cn", getShortName()+(isSelected()?" current":"")).replaceAll("%sn", getShortName()).replaceAll("%n", getName());
			for (SubNavItem sub : getSubItems())
				html += sub.getHTMLEntry();
			return html+MAIN_MENU_CLOSE;
		}
	}
}
