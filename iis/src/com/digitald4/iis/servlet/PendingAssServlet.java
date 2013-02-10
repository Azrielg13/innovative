package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Appointment;

public class PendingAssServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request, response)) return;
			request.setAttribute("body", "/WEB-INF/jsp/pending.jsp");
			ArrayList<Column> columns = new ArrayList<Column>();
			columns.add(new Column("Patient Name", "Link", String.class, true));
			columns.add(new Column("Nurse", "Nurse", String.class, true));
			columns.add(new Column("Appointment Date", ""+Appointment.PROPERTY.START_TIME, String.class, false));
			columns.add(new Column("Duration", "Duration", String.class, false));
			columns.add(new Column("Canceled", "Dianosis", Boolean.class, true));
			columns.add(new Column("Duration", ""+Appointment.PROPERTY.DURATION, String.class, false));
			request.setAttribute("columns", columns);
			request.setAttribute("appointments", Appointment.getPending());
			getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
