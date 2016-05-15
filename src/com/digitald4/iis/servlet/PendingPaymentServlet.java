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

public class PendingPaymentServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(!checkLoginAutoRedirect(request, response)) return;
			setupTable(getEntityManager(), request);
			getLayoutPage(request, "/WEB-INF/jsp/penpay.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(EntityManager entityManager, HttpServletRequest request) {
		ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Nurse", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"nurse?id=" + app.getNurseId() + "#tab-payable\">" + app.getNurse() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Date", "" + Appointment.PROPERTY.START, String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return FormatText.formatDate(app.getStart());
			}
		});
		columns.add(new Column<Appointment>("Hours", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getPayHours();
			}
		});
		columns.add(new Column<Appointment>("Pay Rate", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return FormatText.CURRENCY.format(app.getPayRate());
			}
		});
		columns.add(new Column<Appointment>("Visit Pay", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getPayFlat();
			}
		});
		columns.add(new Column<Appointment>("Mileage", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getPayMileage();
			}
		});
		columns.add(new Column<Appointment>("Total Payment", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return FormatText.CURRENCY.format(app.getPaymentTotal());
			}
		});
		request.setAttribute("payable_cols", columns);
		request.setAttribute("payables", Appointment.getPayables(entityManager));
	}
}
