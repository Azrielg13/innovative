package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.iis.model.Patient;

public class PatientServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			Patient patient = Patient.getInstance(Integer.parseInt(request.getParameter("id")));
			request.setAttribute("patient", patient);
			PendingAssServlet.setupTable(request);
			request.setAttribute("calendar", getCalendar(patient, DateTime.now().getYear(), DateTime.now().getMonthOfYear()).getOutput());
			getLayoutPage(request, "/WEB-INF/jsp/patient.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			Patient patient = Patient.getInstance(Integer.parseInt(request.getParameter("id")));
			String paramName=null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("patient.")) {
					Object attr = request.getParameter(paramName);
					patient.setPropertyValue(paramName, (String)attr);
				}
			}
			patient.save();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
	
	private void processCalendarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Patient patient = Patient.getInstance(Integer.parseInt(request.getParameter("id")));
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		LargeCalTag cal = getCalendar(patient, year, month);
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
	
	public static LargeCalTag getCalendar(Patient patient, int year, int month) {
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Patient Calendar");
		cal.setIdType("appointment.patient_id");
		cal.setUserId(patient.getId());
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(patient.getAppointments(year, month));
		cal.setNotifications(patient.getNotifications(year, month));
		return cal;
	}
}
