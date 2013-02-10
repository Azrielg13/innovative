package com.digitald4.common.tld;

import java.util.Collection;

import com.digitald4.common.component.Column;
import com.digitald4.common.dao.DataAccessObject;

public class TableTag extends DD4Tag {
	private final static String START = "\t<section class=\"grid_12\">\n\t\t<div class=\"block-border\">\n"
			+"\t\t\t<form class=\"block-content form\" id=\"table_form\" method=\"post\" action=\"\">\n"
			+"\t\t\t\t<h1>%title</h1>\n\t\t\t\t<table class=\"table sortable no-margin\" cellspacing=\"0\" width=\"100%\">\n";
	private final static String TITLE_START = "\t\t\t\t\t<thead>\n\t\t\t\t\t\t<tr>\n"
			+"\t\t\t\t\t\t\t<th class=\"black-cell\"><span class=\"loading\"></span></th>\n";
	private final static String TITLE_CELL = "\t\t\t\t\t\t\t<th scope=\"col\"><span class=\"column-sort\">\n"
			+"\t\t\t\t\t\t\t\t<a href=\"#\" title=\"Sort up\" class=\"sort-up\"></a> <a href=\"#\" title=\"Sort down\" class=\"sort-down\"></a>\n"
			+"\t\t\t\t\t\t\t\t</span> %colname</th>\n";
	private final static String TITLE_END = "\t\t\t\t\t\t</tr>\n\t\t\t\t\t</thead>\n\t\t\t\t\t<tbody>\n";
	private final static String ROW_START = "\t\t\t\t\t\t<tr>\n\t\t\t\t\t\t\t<td class=\"th table-check-cell\"><input type=\"checkbox\" name=\"selected[]\" id=\"table-selected-5\" value=\"5\"></td>\n";
	private final static String CELL = "\t\t\t\t\t\t\t<td>%value</td>\n";
	private final static String ROW_END = "\t\t\t\t\t\t</tr>\n";
	private final static String END = "\t\t\t\t\t</tbody>\n\t\t\t\t</table>\n\t\t\t</form>\n\t\t</div>\n\t</section>\n";
	private String title;
	private Collection<Column> columns;
	private Collection<? extends DataAccessObject> data;

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setColumns(Collection<Column> columns) {
		this.columns = columns;
	}
	
	public Collection<Column> getColumns() {
		return columns;
	}
	
	public void setData(Collection<? extends DataAccessObject> data) {
		this.data = data;
	}
	
	public Collection<? extends DataAccessObject> getData() {
		return data;
	}
	
	@Override
	public String getOutput() throws Exception {
		String out = START.replace("%title", getTitle());
		out += TITLE_START;
		for (Column col : getColumns()) {
			out += TITLE_CELL.replace("%colname", col.getName());
		}
		out += TITLE_END;
		for (DataAccessObject dao : getData()) {
			out += ROW_START;
			for (Column col : getColumns()) {
				out += CELL.replace("%value", ""+col.getValue(dao));
			}
			out += ROW_END;
		}
		out += END;
		return out;
	}
}
