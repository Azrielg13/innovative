package com.digitald4.pm.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.pm.Client;
import com.digitald4.pm.User;

public class AssignExaminerConfirmServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response,User.STAFF))return;
			HttpSession session = request.getSession(true);
			Client client = (Client)session.getAttribute("client");
			if(client == null){
				response.sendRedirect("home");
				return;
			}
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/assign_confirm.jsp");
      		layoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
	}
}
