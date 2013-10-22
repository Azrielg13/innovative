package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Patient;

public class PendingIntakeServlet extends ParentServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/pintake.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) throws Exception {
		ArrayList<Column<Patient>> columns = new ArrayList<Column<Patient>>();
		columns.add(new Column<Patient>("Name", "Link", String.class, true));
		columns.add(new Column<Patient>("Source", "Referral_Source", String.class, false));
		columns.add(new Column<Patient>("Name", "Name", String.class, true));
		columns.add(new Column<Patient>("Dianosis", "Dianosis", String.class, false));
		columns.add(new Column<Patient>("Referral Date", "Referral_Date", String.class, false));
		columns.add(new Column<Patient>("Start Date", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
		request.setAttribute("pintake_cols", columns);
		request.setAttribute("patients", Patient.getByState(GenData.PATIENT_PENDING.get()));
	}
}
