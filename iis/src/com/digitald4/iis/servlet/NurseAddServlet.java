package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.model.GenData;
import com.digitald4.common.model.User;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Nurse;

public class NurseAddServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			request.setAttribute("nurse", new Nurse().setUser(new User().setType(GenData.UserType_Standard.get())));
			getLayoutPage(request, "/WEB-INF/jsp/nurseadd.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			Nurse nurse = new Nurse().setUser(new User().setType(GenData.UserType_Standard.get()));
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
			response.sendRedirect("nurses");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
