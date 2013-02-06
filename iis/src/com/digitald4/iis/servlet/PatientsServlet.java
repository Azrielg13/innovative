package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Patient;

public class PatientsServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request, response)) return;
      		request.setAttribute("body", "/WEB-INF/jsp/patients.jsp");
      		ArrayList<Column> columns = new ArrayList<Column>();
      		columns.add(new Column("Name", ""+Patient.PROPERTY.NAME, String.class, true));
    		columns.add(new Column("Source", ""+Patient.PROPERTY.REFERRAL_SOURCE_ID, String.class, false));
    		columns.add(new Column("RX", ""+Patient.PROPERTY.RX_ID, String.class, true));
    		columns.add(new Column("Nurse", ""+Patient.PROPERTY.DIANOSIS_ID, String.class, false));
    		columns.add(new Column("Last Appointment", "Referral_Date", String.class, false));
    		columns.add(new Column("Next Appointment", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
      		request.setAttribute("columns", columns);
    		request.setAttribute("patients", Patient.getAll());
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
