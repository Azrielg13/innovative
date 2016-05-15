package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;

public class PendingAssServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(getEntityManager(), request);
			getLayoutPage(request, "/WEB-INF/jsp/penass.jsp").forward(request, response);
		} catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(EntityManager entityManager, HttpServletRequest request) {
		ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Patient", "Link", String.class, false) {
			@Override
			public Object getValue(Appointment app) {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Nurse", "Nurse", String.class, false));
		columns.add(new Column<Appointment>("Appointment Date", ""+Appointment.PROPERTY.START, String.class, false));
		columns.add(new Column<Appointment>("Time In", "Time In", String.class, false) {
			@Override
			public Object getValue(Appointment app) {
				return FormatText.formatTime(app.getTimeIn());
			}
		});
		columns.add(new Column<Appointment>("Time Out", "Time Out", String.class, false) {
			@Override
			public Object getValue(Appointment app) {
				return FormatText.formatTime(app.getTimeOut());
			}
		});
		columns.add(new Column<Appointment>("Percent Complete", "Percent Complete", String.class, false) {
			@Override
			public Object getValue(Appointment app) throws Exception {
				return app.getPercentComplete() + "%";
			}
		});
		request.setAttribute("penass_cols", columns);
		request.setAttribute("penass_data", Appointment.getPending(entityManager));
	}
}
