package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class FaqServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{

      		request.setAttribute("body", "/WEB-INF/jsp/faq.jsp");
      		layoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
