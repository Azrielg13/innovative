package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;

public class PendingAssServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(!checkLogin(request, response)) return;
			ArrayList<Column> columns = new ArrayList<Column>();
			columns.add(new Column("Patient Name", "Link", String.class, true));
			columns.add(new Column("Nurse", "Nurse", String.class, true));
			columns.add(new Column("Appointment Date", ""+Appointment.PROPERTY.START, String.class, false));
			columns.add(new Column("Duration", "Duration", String.class, false));
			columns.add(new Column("Canceled", ""+Appointment.PROPERTY.CANCELLED, Boolean.class, true));
			columns.add(new Column("Percent Complete", "Percent Complete", String.class, false));
			request.setAttribute("columns", columns);
			request.setAttribute("appointments", Appointment.getPending());
			getLayoutPage(request, "/WEB-INF/jsp/pending.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
}
