package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import com.digitald4.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;

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
