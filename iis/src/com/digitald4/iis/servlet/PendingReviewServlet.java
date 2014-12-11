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
	@Override
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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Patient", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Nurse", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return app.getNurse();
			}
		});
		columns.add(new Column<Appointment>("Date", "" + Appointment.PROPERTY.START, String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return FormatText.formatDate(app.getStart());
			}
		});
		columns.add(new Column<Appointment>("Hours", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getLoggedHours();
			}
		});
		columns.add(new Column<Appointment>("Mileage", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getMileageD();
			}
 		});
		columns.add(new Column<Appointment>("Percent Complete", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return app.getPercentComplete() + "%";
			}
		});
		request.setAttribute("reviewable_cols", columns);
		request.setAttribute("reviewables", Appointment.getReviewables());
	}
}
