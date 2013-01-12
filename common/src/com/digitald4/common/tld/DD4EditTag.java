package com.digitald4.common.tld;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.tagext.TagSupport;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.util.Pair;
/**
 * This is a simple tag example to show how content is added to the
 * output stream when a tag is encountered in a JSP page. 
 */
public class DD4EditTag extends TagSupport {
	public enum EditType {
		TEXTO("<input type=\"text\" name=\"%name\" id=\"%name\" value=\"%value\" class=\"full-width\" />\n"),
		COMBO("<select name=\"%name\" id=\"%name\" value=\"%value\" class=\"full-width\" />\n","\t<option value=\"%op_value\">%op_text</option>\n","\t\t</select>\n"),
		CHECK("<input type=\"checkbox\" name=\"%name\" id=\"%name\" value=\"%value\" class=\"switch\" />\n");
		public final String start;
		public final String option;
		public final String end;
		
		EditType(String start) {
			this(start,null,"");
		}
		EditType(String start, String option, String end) {
			this.start = start;
			this.option = option;
			this.end = end;
		}
	};
	private static final String LABEL = "<label for=\"%name\">%labelText</label>\n";
	private String name;
	private Collection<Pair<String, String>> options = new ArrayList<Pair<String, String>>();
	private String labelText;
	private DataAccessObject dao;
	private EditType et;
	
	/**
	 * Getter/Setter for the attribute name as defined in the tld file 
	 * for this tag
	 */
	public void setName(String name){
		this.name=name;
		options.clear();
	}
	
	public String getName(){
		return name;
	}
	
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}
	
	public String getLabelText() {
		return labelText;
	}
	
	public void setDAO(DataAccessObject dao) {
		this.dao = dao;
	}
	
	public DataAccessObject getDataAccessObject() {
		return dao;
	}
	
	public void setOptions(Collection<Pair<String, String>> options){
		this.options = options;
	}
	
	public Collection<Pair<String, String>> getOptions(){
		return options;
	}
	
	public Object getValue() {
		Object value = dao.getPropertyValue(getName());
		if (value == null) {
			return "";
		}
		return value;
	}
	
	/**
	 * doStartTag is called by the JSP container when the tag is encountered
	 */
	public int doStartTag() {
		try {
			pageContext.getOut().write(getOutput());
		} catch (Exception e) {
			throw new Error(e.getMessage());
		}
		// Must return SKIP_BODY because we are not supporting a body for this tag.
		return SKIP_BODY;
	}
	
	public String getStart() {
		return et.start.replaceAll("%name", getName()).replace("%value", ""+getValue());
	}
	
	public String getEnd() {
		return et.end;
	}
	
	public String getOutput() {
		if (options.size() > 0) {
			et = EditType.COMBO;
		} else if (getValue() instanceof Boolean) {
			et = EditType.CHECK;
		} else {
			et = EditType.TEXTO;
		}
		String out = LABEL.replaceAll("%name", getName()).replaceAll("%labelText", getLabelText());
		out += getStart();
		for(Pair<String, String> option : getOptions()){
			out += et.option.replaceAll("%op_value", option.getLeft()).replaceAll("%op_text", option.getRight());
		}
		out += getEnd();
		return out;
	}
	
	/**
	 * doEndTag is called by the JSP container when the tag is closed
	 */
	public int doEndTag(){
		return 0;
	}
}
