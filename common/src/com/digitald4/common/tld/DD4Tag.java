package com.digitald4.common.tld;

import javax.servlet.jsp.tagext.TagSupport;

public abstract class DD4Tag extends TagSupport {
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
	
	public abstract String getOutput() throws Exception;
	
	/**
	 * doEndTag is called by the JSP container when the tag is closed
	 */
	public int doEndTag(){
		return 0;
	}
}
