package com.digitald4.order.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.order.Cart;

public class InvoiceServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			if(session.getAttribute("cart") == null){
				response.sendRedirect("home");
				return;
			}
			if(((Cart)session.getAttribute("cart")).getItemCount() == 0){
				response.sendRedirect("cart");
				return;
			}
			if(!checkLogin(request,response))
				return;
      		request.setAttribute("body", "/WEB-INF/jsp/invoice.jsp");
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
