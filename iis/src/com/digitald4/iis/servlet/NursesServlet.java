package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Nurse;

public class NursesServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(!checkLoginAutoRedirect(request, response)) return;
			ArrayList<Column> columns = new ArrayList<Column>();
			columns.add(new Column("Name", "Link", String.class, true));
			columns.add(new Column("Phone Number", "phone_number", String.class, true));
			columns.add(new Column("Address", "address", String.class, false));
			columns.add(new Column("Pending Evaluations", "pend_asses_count", String.class, false));
			columns.add(new Column("Last Appointment", "last_app", DateTime.class, false));
			columns.add(new Column("Next Appointment", "next_app", DateTime.class, false));
			request.setAttribute("columns", columns);
			request.setAttribute("nurses", Nurse.getAll());
			getLayoutPage(request, "/WEB-INF/jsp/nurses.jsp" ).forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
}
