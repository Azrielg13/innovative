package com.digitald4.budget.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.model.GenData;
import com.digitald4.common.servlet.ParentServlet;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			//for (GenData gd : GenData.values()) {
				//gd.get();
			//}
			if (!checkLoginAutoRedirect(request, response)) return;
			getLayoutPage(request, "/WEB-INF/jsp/home.jsp" ).forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
}
