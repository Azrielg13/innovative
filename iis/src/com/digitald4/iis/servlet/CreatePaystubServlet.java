package com.digitald4.iis.servlet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.TableTag;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.report.PaystubReport;

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
			if (isAjax(request)) {
				JSONObject json = new JSONObject();
				try {
					Nurse nurse = Nurse.getInstance(nurseId);
					
					TableTag<Appointment> payableTable = new TableTag<Appointment>();
					payableTable.setTitle("payable");
					payableTable.setColumns(NurseServlet.getPayableCols());
					payableTable.setData(nurse.getPayables());
					payableTable.setCallbackCode("payableCallback(object);");
					
					TableTag<Paystub> paystubTable = new TableTag<Paystub>();
					paystubTable.setTitle("Pay History");
					paystubTable.setColumns(NurseServlet.getPaystubCols());
					paystubTable.setData(nurse.getPaystubs());
					
					json.put("valid", true)
							.put("paystubId", paystub.getId())
							.put("tables", new JSONArray()
									.put(new JSONObject().put(payableTable.getTableId(), payableTable.getReplaceOutput()))
									.put(new JSONObject().put(paystubTable.getTableId(), paystubTable.getReplaceOutput())));
					response.setContentType("application/json");
					response.setHeader("Cache-Control", "no-cache, must-revalidate");
					response.getWriter().println(json);
				} catch (JSONException e) {
					throw new ServletException(e);
				}
			} else {
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
			}
		} catch(Exception e) {
			if (isAjax(request)) {
				e.printStackTrace();
				JSONObject json = new JSONObject();
				try {
					json.put("valid", false)
						.put("error", e.getMessage());
					response.setContentType("application/json");
					response.setHeader("Cache-Control", "no-cache, must-revalidate");
					response.getWriter().println(json);
				} catch (Exception e1) {
					throw new ServletException(e1);
				}
			} else {
				throw new ServletException(e);
			}
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
