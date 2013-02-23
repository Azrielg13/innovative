package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Nurse;

public class NurseRegServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request, response)) return;
			if (request.getSession().getAttribute("nurse") == null) {
				request.getSession().setAttribute("nurse", new Nurse());
			}
			request.setAttribute("body", "/WEB-INF/jsp/nursereg.jsp");
			getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse)session.getAttribute("nurse");
		if (nurse == null) {
			nurse = new Nurse();
			session.setAttribute("nurse", nurse);
		}
		String paramName=null;
		try {
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("nurse.")) {
					Object attr = request.getParameter(paramName);
					nurse.setPropertyValue(paramName, (String)attr);
				}
			}
			nurse.insert();
			session.removeAttribute("nurse");
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
}
