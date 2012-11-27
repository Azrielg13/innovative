package com.digitald4.pm.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.pm.User;

public class AdminServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response,User.STAFF))return;

			request.setAttribute("adminBody", "/WEB-INF/jsp/admin.jsp");

      		request.setAttribute("company", company);

      		adminLayoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
