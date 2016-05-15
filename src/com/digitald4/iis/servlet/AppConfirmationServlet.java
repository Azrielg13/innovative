package com.digitald4.iis.servlet;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.digitald4.common.model.User;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.Obfuscator;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;

@WebServlet(name = "Appointment Confirmation Servlet", urlPatterns = {"/app_confirm"})
public class AppConfirmationServlet extends ParentServlet {
	public enum ErrorCode {UNAUTHORIZED};
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			EntityManager entityManager = getEntityManager();
			Appointment appointment = entityManager.find(Appointment.class, Obfuscator.unobfuscate(request.getParameter("obf_id")));
			request.setAttribute("appointment", appointment);
			Nurse nurse = entityManager.find(Nurse.class, Obfuscator.unobfuscate(request.getParameter("nurse_oid")));
			if (appointment == null || nurse == null || nurse != appointment.getNurse()) {
				request.setAttribute("error", ErrorCode.UNAUTHORIZED);
			} else {
				User.setActiveUser(nurse.getUser());
				String action = request.getParameter("action");
				if (action.equals("confirm")) {
					appointment.setNurseConfirmRes(GenData.CONFIRMED_ACCEPTED.get(entityManager),
							DateTime.now(), request.getParameter("notes")).save();
				} else if (action.equals("decline")) {
					appointment.setNurseConfirmRes(GenData.CONFIRMED_REJECTED.get(entityManager), 
							DateTime.now(), request.getParameter("confirm_notes")).save();
				}
			}
			getLayoutPage(request, "/WEB-INF/jsp/app_confirm.jsp" ).forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}

	@Override
	public String getLayoutURL(){
		return "/WEB-INF/jsp/app_confirm.jsp";
	}
}
