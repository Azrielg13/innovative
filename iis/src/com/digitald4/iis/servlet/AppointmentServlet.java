package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;

public class AppointmentServlet extends ParentServlet {

	private Appointment getAppointment(HttpServletRequest request) throws ServletException {
		Appointment appointment = null;
		if (request.getParameter("appointment.id") != null) {
			appointment = Appointment.getInstance(Integer.parseInt(request.getParameter("appointment.id")));
		}
		if (appointment == null) {
			appointment = new Appointment();
		}
		try {
			String paramName = null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("appointment.")) {
					Object attr = request.getParameter(paramName);
					appointment.setPropertyValue(paramName, (String)attr);
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		return appointment;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			request.setAttribute("appointment", getAppointment(request));
			if (request.getParameter("vendor_id") == null) {
				request.setAttribute("patients", Patient.getByState(GenData.PATIENT_ACTIVE.get()));
			} else {	
				request.setAttribute("patients", Vendor.getInstance(Integer.parseInt(request.getParameter("vendor_id"))).getPatients());
			}
			request.setAttribute("nurses", Nurse.getAll());
			getLayoutPage(request, "/WEB-INF/jsp/appointment.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Appointment appointment = getAppointment(request);
		try {
			if (appointment.isNewInstance()) {
				appointment.insert();
			} else {
				appointment.save();
			}
		} catch (Exception e) {
			request.setAttribute("error", e.getMessage());
		}
		if (isAjax(request)) {
			JSONObject json = new JSONObject();
			try {
				if (request.getAttribute("error") == null) {
					String calType = request.getParameter("cal_type");
					LargeCalTag cal = null;
					int year = appointment.getStart().getYear();
					int month = appointment.getStart().getMonthOfYear();
					if (calType.contains("nurse")) {
						cal = NurseServlet.getCalendar(appointment.getNurse(), year, month);
					} else if (calType.contains("patient")) {
						cal = PatientServlet.getCalendar(appointment.getPatient(), year, month);
					} else if (calType.contains("dashboard")) {
						cal = DashboardServlet.getCalendar(year, month);
					} else if (calType.contains("vendor")) {
						cal = VendorServlet.getCalendar(appointment.getPatient().getVendor(), year, month);
					}
					json.put("valid", true)
							.put("html", cal.getOutput());
				} else {
					json.put("valid", false)
							.put("error", request.getAttribute("error"));
				}
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().println(json);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else {
			doGet(request,response);
		}
	}
}
