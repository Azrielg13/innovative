package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Patient;

public class PendingIntakeServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request, response)) return;
			request.setAttribute("body", "/WEB-INF/jsp/pintake.jsp");
			ArrayList<Column> columns = new ArrayList<Column>();
			columns.add(new Column("Name", "Link", String.class, true));
			columns.add(new Column("Source", "Referral_Source", String.class, false));
			columns.add(new Column("Name", "Name", String.class, true));
			columns.add(new Column("Dianosis", "Dianosis", String.class, false));
			columns.add(new Column("Referral Date", "Referral_Date", String.class, false));
			columns.add(new Column("Start Date", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
			request.setAttribute("columns", columns);
			request.setAttribute("patients", Patient.getPending());
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
