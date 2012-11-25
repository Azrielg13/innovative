package com.digitald4.common.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HomeServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{

      		String body = request.getParameter("body");
      		if(body == null)
      			body = "fill.jsp";

      		request.setAttribute("body", "/WEB-INF/jsp/"+body);
			if(getCompany() == null)
				System.out.println("*************************************************Company is null************************************************");
      		request.setAttribute("company", getCompany());

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
