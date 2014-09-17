package com.digitald4.iis.servlet;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.report.PayrollReport;

@WebServlet(name = "CreatePayrollReportServlet", urlPatterns = {"/create_payroll_report"})
public class CreatePayrollReportServlet extends ParentServlet {
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy");
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String type = request.getParameter("type");
			int year = Integer.parseInt(request.getParameter("year"));
			int month = Integer.parseInt(request.getParameter("month"));
			PayrollReport payrollReport = null;
			switch (PayrollReport.REPORT_TYPE.valueOf(type)) {
				case YEARLY: payrollReport = new PayrollReport(year); break;
				case MONTHLY: payrollReport = new PayrollReport(year, month); break;
				case WEEKLY: payrollReport = new PayrollReport(DateTime.parse(request.getParameter("end_date"), DATE_FORMAT)); break;
			}
			ByteArrayOutputStream buffer = payrollReport.createPDF();
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			byte[] bytes = buffer.toByteArray();
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
}
