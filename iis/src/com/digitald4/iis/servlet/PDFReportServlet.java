package com.digitald4.iis.servlet;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.reports.AssessmentReport;

public class PDFReportServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String id = request.getParameter("id");
			ByteArrayOutputStream buffer = null;
			if (id.equalsIgnoreCase("ass")) {
				buffer = new AssessmentReport(Appointment.getInstance(Integer.parseInt(request.getParameter("app_id")))).createPDF();
			}
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			byte[] bytes = buffer.toByteArray();
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		doGet(request,response);
	}
}
