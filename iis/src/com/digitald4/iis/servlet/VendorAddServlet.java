package com.digitald4.iis.servlet;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Vendor;

public class VendorAddServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			request.setAttribute("vendor", new Vendor());
			getLayoutPage(request, "/WEB-INF/jsp/vendoradd.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			Vendor vendor = new Vendor();
			String paramName=null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("vendor.")) {
					Object attr = request.getParameter(paramName);
					vendor.setPropertyValue(paramName, (String)attr);
				}
			}
			vendor.insert();
			response.sendRedirect("vendors");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
