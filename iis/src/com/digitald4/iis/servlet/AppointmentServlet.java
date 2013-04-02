package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;

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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try{
			if(!checkLogin(request, response)) return;
			request.setAttribute("appointment", getAppointment(request));
			request.setAttribute("patients", Patient.getPatientsByState(GenData.PATIENT_ACTIVE.get()));
			request.setAttribute("nurses", Nurse.getAll());
			getLayoutPage(request, "/WEB-INF/jsp/appointment.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		Appointment appointment = getAppointment(request);
		try {
			if (appointment.isNewInstance()) {
				appointment.insert();
			} else {
				appointment.save();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
}
