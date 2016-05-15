package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.model.User;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;

public class AssessmentServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try{
			if (!checkLoginAutoRedirect(request, response)) return;
			EntityManager entityManager = getEntityManager();
			Appointment appointment = entityManager.find(Appointment.class,
					Integer.parseInt(request.getParameter("id")));
  		request.setAttribute("appointment", appointment);
  		if (((User) request.getSession().getAttribute("user")).getId() == appointment.getNurseId()) {
  			request.setAttribute("backPage", "nurse?id=" + appointment.getNurseId() + "#&tab-pending");
  		} else {
  			request.setAttribute("backPage", "penass");
  		}
  		getLayoutPage(request, "/WEB-INF/jsp/assessment.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			EntityManager entityManager = getEntityManager();
			Appointment appointment = entityManager.find(Appointment.class,
					Integer.parseInt(request.getParameter("id")));
			String paramName = null;
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
