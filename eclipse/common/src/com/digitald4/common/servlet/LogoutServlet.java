package com.digitald4.common.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogoutServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			request.getSession().invalidate();
			//request.getSession(true).removeAttribute("user");
			request.setAttribute("body", "/WEB-INF/jsp/logout.jsp");
      		getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}