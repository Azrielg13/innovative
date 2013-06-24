package com.digitald4.iis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;

public class DashboardServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			PendingAssServlet.setupTable(request);
			PendingIntakeServlet.setupTable(request);
			PendingPaymentServlet.setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/dashboard.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
}
