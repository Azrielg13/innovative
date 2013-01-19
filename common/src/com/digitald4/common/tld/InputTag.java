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
public class InputTag extends TagSupport {
	public enum Type {
		TEXT("<input type=\"text\" name=\"%name\" id=\"%name\" value=\"%value\" class=\"full-width\" />\n"),
		COMBO("<select name=\"%name\" id=\"%name\" value=\"%value\" class=\"full-width\" />\n","\t<option value=\"%op_value\">%op_text</option>\n","\t\t</select>\n"),
		CHECK("<input type=\"checkbox\" name=\"%name\" id=\"%name\" value=\"%value\" class=\"switch\" />\n");
		public final String start;
		public final String option;
		public final String end;
		
		Type(String start) {
			this(start,null,"");
		}
		Type(String start, String option, String end) {
			this.start = start;
			this.option = option;
			this.end = end;
		}
	};
	private static final String LABEL = "<label for=\"%name\">%labelText</label>\n";
	private String prop;
	private Collection<Pair<String, String>> options = new ArrayList<Pair<String, String>>();
	private String label;
	private DataAccessObject object;
	private Type type;
	
	/**
	 * Getter/Setter for the attribute name as defined in the tld file 
	 * for this tag
	 */
	public void setProp(String prop){
		this.prop=prop;
		options.clear();
	}
	
	public String getProp() {
		return prop;
	}
	
	public String getName(){
		return getObject().getClass().getSimpleName() + "." + getProp();
	}
	
	public void setType(String type) {
		this.type = Type.valueOf(type.toUpperCase());
	}
	
	public Type getType() {
		return type;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setObject(DataAccessObject object) {
		this.object = object;
	}
	
	public DataAccessObject getObject() {
		return object;
	}
	
	public void setOptions(Collection<Pair<String, String>> options){
		this.options = options;
	}
	
	public Collection<Pair<String, String>> getOptions(){
		return options;
	}
	
	public Object getValue() {
		Object value = getObject().getPropertyValue(getName());
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
		return type.start.replaceAll("%name", getName()).replace("%value", ""+getValue());
	}
	
	public String getEnd() {
		return type.end;
	}
	
	public String getOutput() {
		String out = LABEL.replaceAll("%name", getName()).replaceAll("%labelText", getLabel());
		out += getStart();
		for(Pair<String, String> option : getOptions()){
			out += type.option.replaceAll("%op_value", option.getLeft()).replaceAll("%op_text", option.getRight());
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
