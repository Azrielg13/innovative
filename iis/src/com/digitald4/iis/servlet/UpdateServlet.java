package com.digitald4.iis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;

public class UpdateServlet extends ParentServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		update(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		update(request, response);
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		JSONObject json = new JSONObject();
		try {
			try {
				String className = request.getParameter("classname");
				int id = Integer.parseInt(request.getParameter("id"));
				Class<?> c = Class.forName(className);
				DataAccessObject dao = (DataAccessObject)c.getMethod("getInstance", Integer.class).invoke(null, id);
				String colName = request.getParameter("attribute");
				if (colName.equals("address")) {
					updateAddress(request, dao);
				} else {
					String value = request.getParameter("value");
					dao.setPropertyValue(colName, value);
					dao.save();
				}
				json.put("valid", true).put("object", dao.toJSON());
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
	public static void updateAddress(HttpServletRequest request, DataAccessObject dao) throws Exception {
		String address = request.getParameter("address");
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		if (dao instanceof Patient) {
			Patient patient = (Patient)dao;
			patient.setServiceAddress(address);
			patient.setLatitude(latitude);
			patient.setLongitude(longitude);
			dao.save();
		} else if (dao instanceof Nurse) {
			Nurse nurse = (Nurse)dao;
			nurse.setAddress(address);
			nurse.setLatitude(latitude);
			nurse.setLongitude(longitude);
			dao.save();
		}
	}
}
