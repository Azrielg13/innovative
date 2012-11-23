package com.digitald4.order.servlet;
import com.digitald4.order.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;
import java.text.*;

public class AdminServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkAdminLogin(request,response))
				return;
      		//String adminBody = request.getParameter("adminBody");
      		//if(adminBody == null)
      		//	adminBody = "admin.jsp";

      		//request.setAttribute("adminBody", "/WEB-INF/jsp/"+adminBody);

			request.setAttribute("adminBody", "/WEB-INF/jsp/admin.jsp");
			System.out.println("--------------------------------adminBody = Admin.jsp-------------------------------");
			if(company == null)
				System.out.println("*************************************************Company is null************************************************");
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
