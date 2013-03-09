package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;

public class AppointmentServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try{
			if(!checkLogin(request, response)) return;
			if (request.getSession().getAttribute("appointment") == null) {
				request.getSession().setAttribute("appointment", new Appointment());
			}
			request.setAttribute("patients", Patient.getPatientsByState(GenData.PATIENT_ACTIVE.get()));
			request.setAttribute("nurses", Nurse.getAll());
			request.setAttribute("body", "/WEB-INF/jsp/appointment.jsp");
			getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		HttpSession session = request.getSession();
		Appointment appointment = (Appointment)session.getAttribute("appointment");
		if (appointment == null) {
			appointment = new Appointment();
			session.setAttribute("appointment", appointment);
		}
		String paramName=null;
		try {
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("appointment.")) {
					Object attr = request.getParameter(paramName);
					appointment.setPropertyValue(paramName, (String)attr);
				}
			}
			appointment.insert();
			session.removeAttribute("appointment");
			//response.sendRedirect("pintake");
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
}
