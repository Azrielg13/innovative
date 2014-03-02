package com.digitald4.iis.servlet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.reports.InvoiceReport;

public class CreateInvoiceServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			int vendorId = Integer.parseInt(request.getParameter("vendor_id"));
			String[] appIds = request.getParameterValues("selected[]");
			Invoice invoice = new Invoice().setVendorId(vendorId)
					.setName(request.getParameter("report_name"));
			invoice.getAppointments().addAll(getAppointments(appIds));
			ByteArrayOutputStream buffer = new InvoiceReport(invoice).createPDF();
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			byte[] bytes = buffer.toByteArray();
			invoice.setData(bytes);
			invoice.insert();
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		doGet(request,response);
	}
	
	private List<Appointment> getAppointments(String[] appIds) {
		List<Appointment> appointments = new ArrayList<Appointment>();
		if (appIds != null) {
			for (String appId : appIds) {
				appointments.add(Appointment.getInstance(Integer.parseInt(appId)));
			}
		}
		return appointments;
	}
}
