package com.digitald4.iis.tld;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class MenuTag extends BodyTagSupport {
	private final static String START_NAV = "<!-- Main nav -->\n<nav id=\"main-nav\">\n<ul class=\"container_12\">";
	private final static String MAIN_MENU_OPEN = "<li class=\"%cn\"><a href=\"%sn\" title=\"%n\">%n</a>\n<ul>\n";
	private final static String MAIN_MENU_CLOSE = "</ul></li>";
	private final static String SUB_MENU = "<li%cn><a href=\"%sn\" title=\"%n\">%n</a></li>\n";
	private final static String END_NAV = "</ul></nav>";
	private String selected;
	private ArrayList<TopMenuItem> menus = new ArrayList<TopMenuItem>();

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
			out.println(START_NAV);
			for (TopMenuItem top : menus) {
				out.println(top.getHTMLEntry());
			}
			out.println(END_NAV);
		} catch (Exception ioe) {
			throw new JspException("Error: " + ioe.getMessage() + " for input: "+body);
		}
		return SKIP_BODY;
	}

	public ArrayList<TopMenuItem> getTopMenus() {
		return menus;
	}

	public void parseBody(String body) {
		StringTokenizer st = new StringTokenizer(body,"\n");
		while (st.hasMoreTokens()) {
			String line = st.nextToken().trim();
			if (line.length() > 0) {
				String parent = line.substring(0,line.indexOf('-')).trim();
				TopMenuItem top = new TopMenuItem(parent);
				String children = line.substring(line.indexOf('-')+1).trim();
				StringTokenizer st2 = new StringTokenizer(children,",");
				while (st2.hasMoreElements()) {
					top.addSubItem(new SubMenuItem(st2.nextToken()));
				}
				menus.add(top);
			}
		}
	}

	private class SubMenuItem {
		private String shortName;
		private String name;

		public SubMenuItem(String unparsedNames) {
			this(unparsedNames.substring(0, unparsedNames.indexOf(':')).trim(), unparsedNames.substring(unparsedNames.indexOf(':')+1).trim());
		}

		public SubMenuItem(String shortName, String name) {
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

	private class TopMenuItem extends SubMenuItem{
		private ArrayList<SubMenuItem> subItems = new ArrayList<SubMenuItem>();

		public TopMenuItem(String unparsedNames) {
			super(unparsedNames);
		}

		public TopMenuItem(String shortName, String name) {
			super(shortName, name);
		}

		public void addSubItem(SubMenuItem subItem) {
			subItems.add(subItem);
		}

		public ArrayList<SubMenuItem> getSubItems() {
			return subItems;
		}

		public boolean isSelected() {
			for (SubMenuItem subMenu : getSubItems()) {
				if(subMenu.isSelected())
					return true;
			}
			return false;
		}

		@Override
		public String getHTMLEntry() {
			String html = MAIN_MENU_OPEN.replaceAll("%cn", getShortName()+(isSelected()?" current":"")).replaceAll("%sn", getShortName()).replaceAll("%n", getName());
			for (SubMenuItem sub : getSubItems())
				html += sub.getHTMLEntry();
			return html+MAIN_MENU_CLOSE;
		}
	}
}
