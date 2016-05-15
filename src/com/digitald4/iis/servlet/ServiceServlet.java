package com.digitald4.iis.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.iis.service.AppointmentService;
import com.digitald4.common.servlet.ParentServlet;


/**
 * The Budget Service Servlet.
 * 
 * @author Eddie Mayfield (eddiemay@gmail.com)
 */
@WebServlet(name = "Service Servlet", urlPatterns = {"/ss"})
public class ServiceServlet extends ParentServlet {
	public enum ACTIONS {sendConfirmationRequest, setNurseConfirmed};
	private AppointmentService appointmentService;
	
	public void init() throws ServletException {
		super.init();
		appointmentService = new AppointmentService(getEntityManager());
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			JSONObject json = new JSONObject();
			try {
				if (!checkLoginAutoRedirect(request, response)) return;
				String action = request.getParameter("action");
				switch (ACTIONS.valueOf(action)) {
					case setNurseConfirmed: json.put("data", appointmentService.setNurseConfirmed(request)); break;
					case sendConfirmationRequest:
						json.put("data", appointmentService.sendConfirmationRequest(request, getEmailer()));
						break;
				}
				json.put("valid", true);
			} catch (Exception e) {
				json.put("valid", false).put("error", e.getMessage()).put("stackTrace", formatStackTrace(e));
				e.printStackTrace();
			} finally {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().println(json);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static String formatStackTrace(Exception e) {
		String out = "";
		for (StackTraceElement elem : e.getStackTrace()) {
			out += elem + "\n";
		}
		return out;
	}
}
