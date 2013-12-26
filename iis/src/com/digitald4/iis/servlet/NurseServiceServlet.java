package com.digitald4.iis.servlet;


import java.net.MalformedURLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.digitald4.common.dao.DataAccessObject;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Nurse;

public class NurseServiceServlet extends ParentServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		JSONObject json = new JSONObject();
		String action = request.getParameter("action");
		try {
			try {
				if (action == null) {
					throw new MalformedURLException("Invalid Request");
				}
				if (!checkLogin(request.getSession(true))) {
					throw new Exception("Session Expired");
				}
				if (action.equals("get")) {
					processGet(json, request);
				} else if (action.equals("update")) {
					update(json, request);
				} else if (action.equals("getPendAsses")) {
					processGetPendAsses(json, request);
				} else if (action.equals("getAppointment")) {
					processGetAppointment(json, request);
				} else {
					throw new MalformedURLException("Invalid Request");
				}
			} catch (Exception e) {
				json.put("valid", false)
						.put("error", e.getMessage());
				//throw e;
			} finally {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().println(json);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void processGet(JSONObject json, HttpServletRequest request) throws Exception {
		User user = (User)request.getSession().getAttribute("user");
		Integer id = Integer.valueOf(request.getParameter("id"));
		if (id == null) {
			throw new MalformedURLException("Invalid Request");
		}
		if (user.getType() != GenData.UserType_Admin.get() && user.getId() != id) {
			throw new Exception("Access Denied");
		}
		json.put("valid", true)
				.put("data", Nurse.getInstance(id).toJSON());
	}
	
	private void processGetPendAsses(JSONObject json, HttpServletRequest request) throws Exception {
		User user = (User)request.getSession().getAttribute("user");
		Integer id = Integer.valueOf(request.getParameter("id"));
		if (id == null) {
			throw new MalformedURLException("Invalid Request");
		}
		if (user.getType() != GenData.UserType_Admin.get() && user.getId() != id) {
			throw new Exception("Access Denied");
		}
		JSONArray jsonArray = new JSONArray();
		for (Appointment appointment : Nurse.getInstance(id).getPendAsses()) {
			jsonArray.put(appointment.toJSON());
		}
		json.put("valid", true)
				.put("data", jsonArray);
	}
	
	private void update(JSONObject json, HttpServletRequest request) throws Exception {
		//User user = (User)request.getSession().getAttribute("user");
		JSONObject reqObj = new JSONObject();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String pn = paramNames.nextElement();
			if (pn.startsWith("object[")) {
				reqObj.put(pn.substring(pn.indexOf('[') + 1, pn.indexOf(']')), request.getParameter(pn));
			}
		}
		
		if (!reqObj.has("className")) {
			throw new MalformedURLException("Invalid Request: className required");
		}
		
		DataAccessObject dao = DataAccessObject.get(reqObj);
		/* If the object is a license we shouldn't use a new instance without getting from the Nurse object;
		 * for an instance of this NurseId, LicTypeId may have already been created.
		 */
		if (dao instanceof License && dao.isNewInstance()) {
			dao = Nurse.getInstance(reqObj.getInt("nurseId")).getLicense(GeneralData.getInstance(reqObj.getInt("licTypeId")));
		}
		dao.update(reqObj);
		dao.save();
		
		json.put("valid", true)
				.put("data", dao.toJSON());
	}
	
	private void processGetAppointment(JSONObject json, HttpServletRequest request) throws Exception {
		User user = (User)request.getSession().getAttribute("user");
		Integer id = Integer.valueOf(request.getParameter("id"));
		if (id == null) {
			throw new MalformedURLException("Invalid Request");
		}
		if (user.getType() != GenData.UserType_Admin.get() && user.getId() != id) {
			throw new Exception("Access Denied");
		}
		json.put("valid", true)
				.put("data", Appointment.getInstance(id).toAssessmentJSON());
	}
}
