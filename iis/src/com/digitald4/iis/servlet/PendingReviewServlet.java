package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;

public class PendingReviewServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/penreview.jsp").forward(request, response);
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
		columns.add(new Column("Patient", "", String.class, true) {
			@Override public Object getValue(Object dao) throws Exception {
				return "<a href=\"assessment?id=" + ((Appointment)dao).getId() + "\">" + ((Appointment)dao).getPatient() + "</a>";
			}
		});
		columns.add(new Column("Nurse", "", String.class, true) {
			@Override public Object getValue(Object dao) throws Exception {
				return ((Appointment)dao).getNurse();
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
		columns.add(new Column("Billed Mileage", "", String.class, true) {
			@Override public Object getValue(Object o) {
				return ((Appointment)o).getMileage();
			}
		});
		columns.add(new Column("Percent Complete", "", String.class, true) {
			@Override public Object getValue(Object o) throws Exception {
				return ((Appointment)o).getPercentComplete() + "%";
			}
		});
		request.setAttribute("reviewable_cols", columns);
		request.setAttribute("reviewables", Appointment.getReviewables());
	}
}
