package com.digitald4.iis.servlet;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.reports.AssessmentReport;
import com.digitald4.iis.reports.Invoice;

public class PDFReportServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String type = request.getParameter("type");
			ByteArrayOutputStream buffer = null;
			if (type.equalsIgnoreCase("ass")) {
				buffer = new AssessmentReport(Appointment.getInstance(Integer.parseInt(request.getParameter("app_id")))).createPDF();
			} else if (type.equalsIgnoreCase("inv")) {
				buffer = new Invoice(Vendor.getInstance(Integer.parseInt(request.getParameter("vendor_id"))), DateTime.now().toDate()).createPDF();
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
