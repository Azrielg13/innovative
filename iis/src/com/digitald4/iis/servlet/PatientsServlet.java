package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Patient;

public class PatientsServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			request.setAttribute("patients", Patient.getByState(GenData.PATIENT_STATE_ACTIVE.get()));
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/patients.jsp").forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column<Patient>> columns = new ArrayList<Column<Patient>>();
		columns.add(new Column<Patient>("Name", "Link", String.class, false));
		columns.add(new Column<Patient>("Source", "REFERRAL_SOURCE", String.class, false));
		columns.add(new Column<Patient>("RX", "RX", String.class, false));
		columns.add(new Column<Patient>("Dianosis", "DIANOSIS", String.class, false));
		columns.add(new Column<Patient>("Last Appointment", "Referral_Date", String.class, false));
		columns.add(new Column<Patient>("Next Appointment", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
		request.setAttribute("patients_cols", columns);
	}
}
