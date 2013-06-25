package com.digitald4.iis.servlet;

import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;

public class DashboardServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			PendingAssServlet.setupTable(request);
			PendingIntakeServlet.setupTable(request);
			PendingPaymentServlet.setupTable(request);
			setupCalendar(request);
			getLayoutPage(request, "/WEB-INF/jsp/dashboard.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupCalendar(HttpServletRequest request) {
		request.setAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
		request.setAttribute("month", Calendar.getInstance().get(Calendar.MONTH) + 1);
		request.setAttribute("appointments", Appointment.getAllActive());
	}
}
