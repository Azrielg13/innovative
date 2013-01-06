package com.digitald4.ctags;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
/**
 * This is a simple tag example to show how content is added to the
 * output stream when a tag is encountered in a JSP page. 
 */
public class Radio extends TagSupport {
	private String name=null;
	private String value=null;
	private String compare=null;
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
	public void setValue(String value){
		this.value=value;
	}
	public String getValue(){
		return(value);
	}
	public void setCompare(String value){
		this.compare=value;
	}
	public String getCompare(){
		return(compare);
	}
	/**
	 * doStartTag is called by the JSP container when the tag is encountered
	 */
	public int doStartTag() {
		try {
			JspWriter out = pageContext.getOut();
			String tag = "<input type=\"radio\"";
			if(name != null)
				tag += " name=\""+name+"\"";
			tag +=" value=\""+value+"\"";
			if(value.equals(compare))
				tag += " checked";
			tag+=" />";
			//System.out.println(compare);
			out.print(tag);
		} catch (Exception ex) {
			throw new Error("All is not well in the world.");
		}
		// Must return SKIP_BODY because we are not supporting a body for this tag.
		return SKIP_BODY;
	}
	/**
	 * doEndTag is called by the JSP container when the tag is closed
	 */
	public int doEndTag(){
		return 0;
	}
}
