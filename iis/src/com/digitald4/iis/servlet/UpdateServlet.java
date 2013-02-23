package com.digitald4.iis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.servlet.ParentServlet;

public class UpdateServlet extends ParentServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		update(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		update(request, response);
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		JSONObject json = new JSONObject();
		try{
			try {
				String className = request.getParameter("classname");
				int id = Integer.parseInt(request.getParameter("id"));
				Class<?> c = Class.forName(className);
				DataAccessObject dao = (DataAccessObject)c.getMethod("getInstance", Integer.class).invoke(null, id);
				String colName = request.getParameter("attribute");
				String value = request.getParameter("value");
				dao.setPropertyValue(colName, value);
				dao.save();
				json.put("valid", true);
			} catch (Exception e) {
				json.put("valid", false).put("error", e.getMessage());
				throw e;
			} finally {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().println(json);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
