package com.digitald4.iis.servlet;

import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.MedCalTag;
import com.digitald4.iis.model.Patient;

public class PatientServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			request.setAttribute("patient", Patient.getInstance(Integer.parseInt(request.getParameter("id"))));
			request.setAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
			request.setAttribute("month", Calendar.getInstance().get(Calendar.MONTH) + 1);
			getLayoutPage(request, "/WEB-INF/jsp/patient.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
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
		MedCalTag cal = new MedCalTag();
		cal.setTitle("Patient Calendar");
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(patient.getAppointments());
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
