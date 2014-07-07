package com.digitald4.iis.servlet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.reports.PaystubReport;

@WebServlet(name = "CreatePaystubServlet", urlPatterns = {"/create_paystub"})
public class CreatePaystubServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			int nurseId = Integer.parseInt(request.getParameter("nurse_id"));
			String[] appIds = request.getParameterValues("selected[]");
			Paystub paystub = new Paystub().setNurseId(nurseId);
			paystub.setPropertyValue("pay_date", request.getParameter("Paystub.pay_date"));
			paystub.getAppointments().addAll(getAppointments(appIds));
			ByteArrayOutputStream buffer = new PaystubReport(paystub.calc()).createPDF();
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			byte[] bytes = buffer.toByteArray();
			paystub.setData(bytes);
			paystub.insert();
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
