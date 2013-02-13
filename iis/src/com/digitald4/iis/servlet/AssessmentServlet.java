package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Patient;

public class AssessmentServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try{
			if(!checkLogin(request, response)) return;
      		request.setAttribute("body", "/WEB-INF/jsp/assessment.jsp");
      		request.setAttribute("appointment", new Appointment().setPatient(Patient.getInstance(Integer.parseInt(request.getParameter("id")))));
      		getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if(!checkLogin(request, response)) return;
			Appointment appointment = Appointment.getInstance(Integer.parseInt(request.getParameter("id")));
			String paramName=null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("appointment.")) {
					Object attr = request.getParameter(paramName);
					appointment.setPropertyValue(paramName, (String)attr);
				}
			}
			appointment.save();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
}
