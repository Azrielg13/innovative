package com.digitald4.iis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.digitald4.iis.model.GenData;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Nurse;

public class DashboardServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			for (GenData gd : GenData.values()) {
				gd.get();
			}
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			PendingAssServlet.setupTable(request);
			PendingIntakeServlet.setupTable(request);
			PendingReviewServlet.setupTable(request);
			PendingPaymentServlet.setupTable(request);
			BillableServlet.setupTable(request);
			LicenseAlertServlet.setupTable(request);
			DateTime now = DateTime.now();
			request.setAttribute("calendar", getCalendar(now.getYear(), now.getMonthOfYear()).getOutput());
			getLayoutPage(request, "/WEB-INF/jsp/dashboard.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	private void processCalendarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		LargeCalTag cal = getCalendar(year, month);
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
	
	public static LargeCalTag getCalendar(int year, int month) {
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Calendar");
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(Appointment.getAllActive());
		cal.setNotifications(Nurse.getAllNotifications());
		return cal;
	}
}
