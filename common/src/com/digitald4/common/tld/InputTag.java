package com.digitald4.common.tld;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.util.FormatText;

/**
 * This is a simple tag example to show how content is added to the
 * output stream when a tag is encountered in a JSP page. 
 */
public class InputTag extends DD4Tag {
	public enum Type {
		TEXT("<input type=\"text\" name=\"%name\" id=\"%id\" value=\"%value\" class=\"full-width\" %onchange />"),
		ACK_TEXT("<p><span class=\"label\">%label</span>","<input type=\"checkbox\"/><input type=\"text\" name=\"%name\" id=\"%id\" value=\"%value\" %onchange />",null,"</p>"),
		COMBO("<select name=\"%name\" id=\"%id\" class=\"full-width\" %onchange />","<option value=\"%op_value\" %selected>%op_text</option>","</select>"),
		CHECK("<input type=\"checkbox\" class=\"full-width\" name=\"%name\" id=\"%id\" value=\"true\" %checked %onchange />"),
		DATE("<input type=\"text\" name=\"%name\" id=\"%id\" value=\"%value\" class=\"datepicker\" %onchange />"
				+"<img src=\"images/icons/fugue/calendar-month.png\" width=\"16\" height=\"16\" />"),
		RADIO("<p><span class=\"label\">%label</span>", "", 
				"<input type=\"radio\" name=\"%name\" id=\"%name-%op_value\" value=\"%op_value\" %checked %onchange />" +
				"<label for=\"%name-%op_value\">%op_text</label>",
				"</p>"),
		MULTI_CHECK("<p><span class=\"label\">%label</span>", "", 
				"<input type=\"checkbox\" name=\"%name\" id=\"%id-%op_value\" %onchange %checked value=\"%op_value\"/>" +
				"<label for=\"%name-%op_value\">%op_text</label>",
				"</p>"),
		TEXTAREA("<textarea name=\"%name\" id=\"%id\" rows=10 class=\"full-width\" %onchange>%value</textarea>");
		
		private final String label;
		private final String start;
		private final String option;
		private final String end;
		
		Type(String start) {
			this(start,null,"");
		}
		
		Type(String start, String option, String end) {
			this("<label for=\"%id\">%label</label>", start, option, end);
		}
		
		Type(String label, String start, String option, String end) {
			this.label = label;
			this.start = start;
			this.option = option;
			this.end = end;
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getStart() {
			return start;
		}
		
		public String getOption() {
			return option;
		}
		
		public String getEnd() {
			return end;
		}
	};
	
	private String prop;
	private Collection<? extends DataAccessObject> options = new ArrayList<DataAccessObject>();
	private String label;
	private DataAccessObject object;
	private Type type;
	private boolean async;
	private Object value;
	
	/**
	 * Getter/Setter for the attribute name as defined in the tld file 
	 * for this tag
	 */
	public void setProp(String prop){
		this.prop=prop;
		options = new ArrayList<DataAccessObject>();
		value = null;
	}
	
	public String getProp() {
		return prop;
	}
	
	public String getFieldId(){
		return getProp();
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
	
	public void setAsync(boolean async) {
		this.async = async;
	}
	
	public boolean isAsync() {
		return async;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		if (value == null) {
			value = getObject().getPropertyValue(getProp());
			if (value == null)
				value = "";
			else if (value instanceof Date) {
				value = FormatText.formatDate((Date)value);
			}
		}
		return value;
	}
	
	public boolean isChecked() {
		Object value = getValue();
		if (value instanceof Boolean) {
			return (Boolean)value;
		}
		return false;
	}
	
	public String getAsyncCode() {
		if (getType() == Type.CHECK) {
			return "onchange=\"asyncCheckbox(this, '" + getObject().getClass().getName() + "', '" + getObject().getId() + "', '" + getProp() + "')\"";
		}
		return "onchange=\"asyncUpdate(this, '" + getObject().getClass().getName() + "', '" + getObject().getId() + "', '" + getProp() + "')\"";
	}
	
	public String getStart() {
		return getType().getStart().replaceAll("%name", getName()).replaceAll("%id", getFieldId()).replaceAll("%value", ""+getValue())
				.replaceAll("%onchange", isAsync() ? getAsyncCode() : "");
	}
	
	public String getEnd() {
		return getType().getEnd();
	}
	
	public boolean isSelected(DataAccessObject option) {
		return option == getValue() || option.getId().toString().equals(""+getValue());
	}
	
	public String getOutput() {
		String out = (getLabel() != null)?getType().getLabel().replaceAll("%id", getFieldId()).replaceAll("%label", getLabel()):"";
		out += getStart().replaceAll("%checked", isChecked() ? "checked" : "");
		if (getType().getOption() != null) {
			if (getType() == Type.COMBO) {
				out += getType().getOption().replaceAll("%name", getName()).replaceAll("%op_value", "0")
						.replaceAll("%op_text", "[SELECT "+getLabel()+"]").replaceAll("%selected", "");
			}
			for (DataAccessObject option : getOptions()) {
				out += getType().getOption().replaceAll("%name", getName()).replaceAll("%op_value", ""+option.getId())
						.replaceAll("%op_text", ""+option)
						.replaceAll("%selected", isSelected(option) ? "selected" : "").replaceAll("%checked", isSelected(option) ? "checked" : "")
						.replaceAll("%onchange", isAsync() ? getAsyncCode() : "");
			}
		}
		out += getEnd();
		return out;
	}
}
