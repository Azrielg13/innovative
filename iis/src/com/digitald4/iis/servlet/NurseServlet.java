package com.digitald4.iis.servlet;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Nurse;

public class NurseServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			Nurse nurse = Nurse.getInstance(Integer.parseInt(request.getParameter("id")));
			request.setAttribute("nurse", nurse);
			request.setAttribute("calendar", getCalendar(nurse, DateTime.now().getYear(), DateTime.now().getMonthOfYear()).getOutput());
			ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
			columns.add(new Column<Appointment>("Patient", "", String.class, true) {
				@Override public Object getValue(Appointment app) throws Exception {
					return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
				}
			});
			columns.add(new Column<Appointment>("Date", ""+Appointment.PROPERTY.START, String.class, false) {
				@Override public Object getValue(Appointment app) throws Exception {
					return FormatText.formatDate(app.getStart());
				}
			});
			columns.add(new Column<Appointment>("Time In", "Time In", String.class, false) {
				@Override public Object getValue(Appointment app) {
					return FormatText.formatTime(app.getTimeIn());
				}
			});
			columns.add(new Column<Appointment>("Time Out", "Time Out", String.class, true) {
				@Override public Object getValue(Appointment app) {
					return FormatText.formatTime(app.getTimeOut());
				}
			});
			columns.add(new Column<Appointment>("Percent Complete", "Percent Complete", String.class, false) {
				@Override public Object getValue(Appointment app) throws Exception {
					return app.getPercentComplete() + "%";
				}
			});
			columns.add(new Column<Appointment>("Action", ""+Appointment.PROPERTY.CANCELLED, Boolean.class, true));
			request.setAttribute("pendcols", columns);
			
			columns = new ArrayList<Column<Appointment>>();
			columns.add(new Column<Appointment>("Patient", "", String.class, true) {
				@Override public Object getValue(Appointment app) throws Exception {
					return "<a href=\"assessment?id=" + app.getId()+"\">" + app.getPatient() + "</a>";
				}
			});
			columns.add(new Column<Appointment>("Date", ""+Appointment.PROPERTY.START, String.class, false) {
				@Override public Object getValue(Appointment app) throws Exception {
					return FormatText.formatDate(app.getStart());
				}
			});
			columns.add(new Column<Appointment>("Billed Hours", "", String.class, false) {
				@Override public Object getValue(Appointment app) {
					return app.getBilledHours();
				}
			});
			columns.add(new Column<Appointment>("Pay Rate", "", String.class, true) {
				@Override public Object getValue(Appointment app) {
					return FormatText.CURRENCY.format(app.getPayRate());
				}
			});
			columns.add(new Column<Appointment>("Billed Mileage", "", String.class, true) {
				@Override public Object getValue(Appointment app) {
					return app.getMileage();
				}
			});
			columns.add(new Column<Appointment>("Total Payment", "", String.class, false) {
				@Override public Object getValue(Appointment app) throws Exception {
					return FormatText.CURRENCY.format(app.getTotalPayment());
				}
			});
			request.setAttribute("billcols", columns);
			getLayoutPage(request, "/WEB-INF/jsp/nurse.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			Nurse nurse = Nurse.getInstance(Integer.parseInt(request.getParameter("id")));
			String paramName=null;
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				paramName = paramNames.nextElement();
				if (paramName.toLowerCase().startsWith("nurse.")) {
					Object attr = request.getParameter(paramName);
					nurse.setPropertyValue(paramName, (String)attr);
				} else if (paramName.toLowerCase().startsWith("user.")) {
					Object attr = request.getParameter(paramName);
					nurse.getUser().setPropertyValue(paramName, (String)attr);
				}
			}
			nurse.save();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		doGet(request,response);
	}
	
	private void processCalendarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Nurse nurse = Nurse.getInstance(Integer.parseInt(request.getParameter("id")));
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		LargeCalTag cal = getCalendar(nurse, year, month);
		JSONObject json = new JSONObject();
		try {
			json.put("valid", true)
					.put("html", cal.getOutput());
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.getWriter().println(json);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	public static LargeCalTag getCalendar(Nurse nurse, int year, int month) {
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Nurse Calendar");
		cal.setIdType("appointment.nurse_id");
		cal.setUserId(nurse.getId());
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(nurse.getAppointments());
		cal.setNotifications(nurse.getNotifications());
		return cal;
	}
}
