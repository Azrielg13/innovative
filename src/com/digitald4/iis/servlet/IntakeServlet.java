package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Patient;

public class IntakeServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			if (request.getSession().getAttribute("patient") == null) {
				request.getSession().setAttribute("patient", new Patient(getEntityManager()));
			}
      getLayoutPage(request, "/WEB-INF/jsp/intake.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		JSONObject json = new JSONObject();
		try {
			try {
				if (!checkLoginAutoRedirect(request, response)) return;
				Patient patient = new Patient(getEntityManager());
				String paramName = null;
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
				json.put("valid", true).put("redirect", "pintake");
			} catch (Exception e) {
				e.printStackTrace();
				json.put("valid", false)
				.put("error", e.getMessage());
			}
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.getWriter().println(json);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
