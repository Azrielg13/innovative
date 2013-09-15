package com.digitald4.iis.servlet;

import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.iis.model.Appointment;

public class DashboardServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			PendingAssServlet.setupTable(request);
			PendingIntakeServlet.setupTable(request);
			PendingReviewServlet.setupTable(request);
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
	
	private void processCalendarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Calendar");
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(Appointment.getAllActive());
		JSONObject json = new JSONObject();
		try {
			json.put("valid", true)
				.put("html", cal.getOutput());
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.getWriter().println(json);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
