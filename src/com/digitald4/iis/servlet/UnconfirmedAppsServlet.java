package com.digitald4.iis.servlet;

import static com.digitald4.common.util.FormatText.formatDate;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Nurse;

@WebServlet(name = "Unconfirmed Appointments Servlet", urlPatterns = {"/unconfirmed"})
public class UnconfirmedAppsServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/unconfirmed.jsp" ).forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Nurse", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"nurse?id=" + app.getNurseId() + "\">" + app.getNurse() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Patient", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Start Time", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return formatDate(app.getStart(), FormatText.USER_DATETIME);
			}
		});
		columns.add(new Column<Appointment>("Contact Info", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				Nurse nurse = app.getNurse();
				if (nurse != null) {
					return nurse.getPhoneNumber() + " | " + nurse.getEmail();
				}
				return null;
			}
		});
		String enableConfirmationRequest = request.getServletContext().getInitParameter("enable_confirmation_request");
		if (Boolean.parseBoolean(enableConfirmationRequest)) {
			columns.add(new Column<Appointment>("Send Confirmation Request", "", String.class, false) {
				@Override public Object getValue(Appointment app) {
					return "<button onclick=\"sendConfirmationRequest(" + app.getId() + ")\">Send Request</button>";
				}
	 		});
		}
		columns.add(new Column<Appointment>("Confirm", "" + Appointment.PROPERTY.NURSE_CONFIRM_TS, String.class, false) {
			@Override public Object getValue(Appointment app) {
				return "<button onclick=\"confirmAppointment(" + app.getId() + ")\">Set Confirmed</button>";
			}
		});
		request.setAttribute("unconfirmed_cols", columns);
	}
}
