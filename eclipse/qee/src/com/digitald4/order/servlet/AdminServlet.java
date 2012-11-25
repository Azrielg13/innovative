package com.digitald4.order.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;

public class AdminServlet extends ParentServlet{
	@Override
	public String getLayoutURL(){
		return "/WEB-INF/jsp/adminLayout.jsp";
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkAdminLogin(request,response))
				return;

			request.setAttribute("adminBody", "/WEB-INF/jsp/admin.jsp");
			System.out.println("--------------------------------adminBody = Admin.jsp-------------------------------");
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
