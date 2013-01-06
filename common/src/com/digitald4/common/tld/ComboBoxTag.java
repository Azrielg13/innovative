package com.digitald4.common.tld;
import java.util.Collection;

import javax.servlet.jsp.tagext.TagSupport;

import com.digitald4.common.util.Pair;
/**
 * This is a simple tag example to show how content is added to the
 * output stream when a tag is encountered in a JSP page. 
 */
public class ComboBoxTag extends TagSupport {
	private static final String START = "\t\t<select name=\"%name\" id=\"%name\">\n";
	private static final String OPTION = "\t\t\t<option value=\"%op_value\">%op_text</option>\n";
	private static final String END = "\t\t</select>\n";
	private String name;
	private Collection<Pair<String, String>> options;
	private String selected=null;
	
	/**
	 * Getter/Setter for the attribute name as defined in the tld file 
	 * for this tag
	 */
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setOptions(Collection<Pair<String, String>> options){
		this.options = options;
	}
	
	public Collection<Pair<String, String>> getOptions(){
		return options;
	}
	
	public void setSelected(String value){
		this.selected=value;
		if(selected== null)
			selected="";
	}
	public String getSelected(){
		return selected;
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
	
	public String getOutput() {
		String out = START.replaceAll("%name", getName());
		for(Pair<String, String> option : getOptions()){
			out += OPTION.replaceAll("%op_value", option.getLeft()).replaceAll("%op_text", option.getRight());
		}
		out += END;
		return out;
	}
	
	/**
	 * doEndTag is called by the JSP container when the tag is closed
	 */
	public int doEndTag(){
		return 0;
	}
}
