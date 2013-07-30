package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Patient;

public class IntakeServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLoginAutoRedirect(request, response)) return;
			if (request.getSession().getAttribute("patient") == null) {
				request.getSession().setAttribute("patient", new Patient());
			}
      getLayoutPage(request, "/WEB-INF/jsp/intake.jsp").forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		HttpSession session = request.getSession();
		Patient patient = null;//(Patient)session.getAttribute("patient");
		if (patient == null) {
			patient = new Patient();
			session.setAttribute("patient", patient);
		}
		String paramName = null;
		try {
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("patient.")) {
					Object attr = request.getParameter(paramName);
					patient.setPropertyValue(paramName, (String)attr);
				}
			}
			if (!patient.isNewInstance())
				throw new Exception("Existing user being edited");
			patient.insert();
			if (patient.isNewInstance())
				throw new Exception("insert failed");
			session.removeAttribute("patient");
			response.sendRedirect("pintake");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
