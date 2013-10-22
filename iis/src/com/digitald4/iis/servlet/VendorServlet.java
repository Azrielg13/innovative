package com.digitald4.iis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.iis.model.Vendor;

public class VendorServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			Vendor vendor = Vendor.getInstance(Integer.parseInt(request.getParameter("id")));
			request.setAttribute("vendor", vendor);
			request.setAttribute("calendar", getCalendar(vendor, DateTime.now().getYear(), DateTime.now().getMonthOfYear()).getOutput());
			NurseServlet.setupTables(request);
			PatientsServlet.setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/vendor.jsp").forward(request, response);
		} catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	private void processCalendarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Vendor vendor = Vendor.getInstance(Integer.parseInt(request.getParameter("id")));
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		LargeCalTag cal = getCalendar(vendor, year, month);
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
	
	public static LargeCalTag getCalendar(Vendor vendor, int year, int month) {
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Vendor Calendar");
		cal.setIdType("vendor_id");
		cal.setUserId(vendor.getId());
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(vendor.getAppointments());
		cal.setNotifications(vendor.getNotifications());
		return cal;
	}
}
