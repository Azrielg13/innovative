package com.digitald4.iis.servlet;


import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.report.AssessmentReport;

public class PDFReportServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			EntityManager entityManager = getEntityManager();
			String type = request.getParameter("type");
			byte[] bytes = null;
			if (type.equalsIgnoreCase("ass")) {
				bytes = new AssessmentReport(entityManager.find(Appointment.class,
						Integer.parseInt(request.getParameter("id")))).createPDF().toByteArray();
			} else if (type.equalsIgnoreCase("inv")) {
				bytes = entityManager.find(Invoice.class, Integer.parseInt(request.getParameter("id")))
						.getData();
			} else if (type.equalsIgnoreCase("paystub")) {
				bytes = entityManager.find(Paystub.class, Integer.parseInt(request.getParameter("id")))
						.getData();
			}
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
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