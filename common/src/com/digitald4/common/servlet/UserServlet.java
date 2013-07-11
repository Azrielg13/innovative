package com.digitald4.common.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.model.User;

public class UserServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			request.setAttribute("patient", User.getInstance(Integer.parseInt(request.getParameter("id"))));
			getLayoutPage(request, "/WEB-INF/jsp/user.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			User user = User.getInstance(Integer.parseInt(request.getParameter("id")));
			String paramName=null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("user.")) {
					Object attr = request.getParameter(paramName);
					user.setPropertyValue(paramName, (String)attr);
				}
			}
			user.save();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
}
