package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Patient;

public class PatientsServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try{
			if(!checkLogin(request, response)) return;
			request.setAttribute("body", "/WEB-INF/jsp/patients.jsp");
			ArrayList<Column> columns = new ArrayList<Column>();
			columns.add(new Column("Name", "Link", String.class, true));
			columns.add(new Column("Source", "REFERRAL_SOURCE", String.class, false));
			columns.add(new Column("RX", "RX", String.class, true));
			columns.add(new Column("Nurse", "DIANOSIS", String.class, false));
			columns.add(new Column("Last Appointment", "Referral_Date", String.class, false));
			columns.add(new Column("Next Appointment", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
			request.setAttribute("columns", columns);
			request.setAttribute("patients", Patient.getAllActive());
			getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		doGet(request,response);
	}
}
