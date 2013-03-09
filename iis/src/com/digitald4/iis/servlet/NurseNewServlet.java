package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.model.GenData;
import com.digitald4.common.model.User;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Nurse;

public class NurseNewServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try{
			if(!checkLogin(request, response)) return;
			if (request.getSession().getAttribute("nurse") == null) {
				request.getSession().setAttribute("nurse", new Nurse().setUser(new User().setType(GenData.UserType_Standard.get())));
			}
			request.setAttribute("body", "/WEB-INF/jsp/nurse_new.jsp");
			getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse)session.getAttribute("nurse");
		try {
			if (nurse == null) {
				nurse = new Nurse().setUser(new User().setType(GenData.UserType_Standard.get()));
				session.setAttribute("nurse", nurse);
			}
			String paramName=null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("nurse.")) {
					Object attr = request.getParameter(paramName);
					nurse.setPropertyValue(paramName, (String)attr);
				} else if (paramName.toLowerCase().startsWith("user.")) {
					Object attr = request.getParameter(paramName);
					nurse.getUser().setPropertyValue(paramName, (String)attr);
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
