package com.digitald4.common.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IframeServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			getLayoutPage(request, "/WEB-INF/jsp/include/iframe.jsp").forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public String getLayoutURL(){
		return "/WEB-INF/jsp/layout2.jsp";
	}
}
