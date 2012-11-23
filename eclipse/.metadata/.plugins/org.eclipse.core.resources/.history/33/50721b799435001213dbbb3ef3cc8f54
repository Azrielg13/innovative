package com.digitald4.order.servlet;
import com.digitald4.order.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ThankYouServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Cart cart = (Cart)session.getAttribute("cart");
			if(cart == null){
				response.sendRedirect("home");
				return;
			}
			if(!checkLogin(request,response))
				return;
			cart.empty();
      		request.setAttribute("body", "/WEB-INF/jsp/thankyou.jsp");
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
