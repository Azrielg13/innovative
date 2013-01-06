package com.digitald4.ctags;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.Vector;
/**
 * This is a simple tag example to show how content is added to the
 * output stream when a tag is encountered in a JSP page. 
 */
public class Select extends TagSupport {
	private String name=null;
	private Vector options;
	private String selected=null;
	private Vector texts;
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
	public void setOptionValues(Vector values){
		if(values != null && values.size() > 0 && values.get(0) instanceof HiddenOption){
			options = new Vector();
			texts = new Vector();
			for(int x=0; x<values.size(); x++){
				options.add(((HiddenOption)values.get(x)).getHidden());
				texts.add(((HiddenOption)values.get(x)).getDisplay());
			}
		}
		else{
			if(values == null)
				values = new Vector();
			options=values;
			//if(texts == null)
				texts = values;
		}
	}
	public Vector getOptionValues(){
		return(options);
	}
	public void setOptionTexts(Vector texts){
		this.texts = texts;
	}
	public Vector getOptionTexts(){
		return texts;
	}	
	public void setSelected(String value){
		this.selected=value;
		if(selected== null)
			selected="";
	}
	public String getSelected(){
		return(selected);
	}
	/**
	 * doStartTag is called by the JSP container when the tag is encountered
	 */
	public int doStartTag() {
		try {
			JspWriter out = pageContext.getOut();
			String tag = "<select";
			if(name != null)
				tag += " name=\""+name+"\"";
			tag += " >";
			out.println(tag);
			for(int s=0; s<options.size() && s<texts.size(); s++){
				String option = "<option value=\""+options.get(s)+"\"";
				if(options.get(s).toString().equalsIgnoreCase(selected))
					option += " selected";
				option += " >"+texts.get(s)+"</option>";
				out.println(option);
			}
		} catch (Exception e) {
			throw new Error(e.getMessage());
		}
		// Must return SKIP_BODY because we are not supporting a body for this tag.
		return SKIP_BODY;
	}
	/**
	 * doEndTag is called by the JSP container when the tag is closed
	 */
	public int doEndTag(){
		try {
			JspWriter out = pageContext.getOut();
			out.print("</select>");
		} catch (Exception e){
			throw new Error(e.getMessage());
		}
		return 0;
	}
}
