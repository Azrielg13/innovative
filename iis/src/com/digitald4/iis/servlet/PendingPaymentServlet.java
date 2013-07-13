package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;

public class PendingPaymentServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/penpay.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Nurse", "", String.class, true) {
			@Override public Object getValue(Object dao) throws Exception {
				return "<a href=\"assessment?id=" + ((Appointment)dao).getId() + "\">" + ((Appointment)dao).getNurse() + "</a>";
			}
		});
		columns.add(new Column("Date", "" + Appointment.PROPERTY.START, String.class, false) {
			@Override public Object getValue(Object dao) throws Exception {
				return FormatText.formatDate(((Appointment)dao).getStart());
			}
		});
		columns.add(new Column("Billed Hours", "", String.class, false) {
			@Override public Object getValue(Object o) {
				return ((Appointment)o).getBilledHours();
			}
		});
		columns.add(new Column("Pay Rate", "", String.class, true) {
			@Override public Object getValue(Object o) {
				return FormatText.CURRENCY.format(((Appointment)o).getPayRate());
			}
		});
		columns.add(new Column("Billed Mileage", "", String.class, true) {
			@Override public Object getValue(Object o) {
				return ((Appointment)o).getMileage();
			}
		});
		columns.add(new Column("Total Payment", "", String.class, false) {
			@Override public Object getValue(Object dao) throws Exception {
				return FormatText.CURRENCY.format(((Appointment)dao).getTotalPayment());
			}
		});
		request.setAttribute("payable_cols", columns);
		request.setAttribute("payables", Appointment.getPayables());
	}
}
