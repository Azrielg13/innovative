package com.digitald4.iis.servlet;


import java.io.IOException;
import java.net.MalformedURLException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.servlet.ParentServlet;

public class ObjectStreamServlet extends ParentServlet {

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		String action = request.getParameter("action");
		String className = request.getParameter("className");
		JSONObject json = new JSONObject();
		try {
			try {
				if (action == null || className == null) {
					throw new MalformedURLException("Invalid Request");
				}
				Class<?> c = Class.forName(className);
				EntityManager em = getEntityManager();
				if (action.equals("get")) {
					Object id = request.getParameter("id");
					if (id == null) {
						throw new MalformedURLException("Invalid Request");
					}
					PrimaryKey<Object> pk = new PrimaryKey<Object>(id);
					Object o = em.find(c, pk);
					if (o != null) {
						json.put("valid", true)
								.put("data", new JSONObject(o));
					} else {
						json.put("valid", false)
								.put("error", "unknown object id: " + id);
					}
				} else if (action.equals("getAll")) {
					TypedQuery<?> tq = em.createNamedQuery("findAll", c);
					json.put("valid", true)
							.put("data", new JSONArray(tq.getResultList().toArray()));
				} else if (action.equals("getCollection")) {
					String[] values = request.getParameterValues("values");
					String queryName = request.getParameter("queryName");
					if (values == null || queryName == null) {
						throw new MalformedURLException("Invalid Request");
					}
					TypedQuery<?> tq = em.createNamedQuery(queryName, c);
					if (values != null && values.length > 0) {
						int p = 1;
						for (Object value:values) {
							if (value != null) {
								tq = tq.setParameter(p++, value);
							}
						}
					}
					json.put("valid", true)
							.put("data", new JSONArray(tq.getResultList().toArray()));
				} else {
					throw new MalformedURLException("Invalid Request");
				}
			} catch (Exception e) {
				json.put("valid", false)
						.put("error", e.getMessage());
			}
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.getWriter().println(json);
		} catch (JSONException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			processRequest(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		doGet(request, response);
	}
}
