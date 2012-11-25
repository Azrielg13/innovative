package com.digitald4.order.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;

public class AdminSearchServlet extends ParentServlet{
	@Override
	public String getLayoutURL(){
		return "/WEB-INF/jsp/admin_search.jsp";
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkAdminLogin(request,response))
				return;
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
