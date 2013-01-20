package com.digitald4.common.tld;
import java.util.ArrayList;
import java.util.Collection;

import com.digitald4.common.dao.DataAccessObject;

/**
 * This is a simple tag example to show how content is added to the
 * output stream when a tag is encountered in a JSP page. 
 */
public class InputTag extends DD4Tag {
	public enum Type {
		TEXT("<input type=\"text\" name=\"%name\" id=\"%name\" value=\"%value\" class=\"full-width\" />\n"),
		COMBO("<select name=\"%name\" id=\"%name\" value=\"%value\" class=\"full-width\" />\n","\t<option value=\"%op_value\">%op_text</option>\n","</select>\n"),
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
	private Collection<? extends DataAccessObject> options = new ArrayList<DataAccessObject>();
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
	
	public void setType(Type type) {
		this.type = type;
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
	
	public void setOptions(Collection<? extends DataAccessObject> options){
		this.options = options;
	}
	
	public Collection<? extends DataAccessObject> getOptions(){
		return options;
	}
	
	public Object getValue() {
		Object value = getObject().getPropertyValue(getName());
		if (value == null) {
			return "";
		}
		return value;
	}
	
	public String getStart() {
		return getType().start.replaceAll("%name", getName()).replace("%value", ""+getValue());
	}
	
	public String getEnd() {
		return getType().end;
	}
	
	public String getOutput() {
		String out = LABEL.replaceAll("%name", getName()).replaceAll("%labelText", getLabel());
		out += getStart();
		for(DataAccessObject option : getOptions()){
			out += getType().option.replaceAll("%op_value", ""+option.getId()).replaceAll("%op_text", ""+option);
		}
		out += getEnd();
		return out;
	}
}
