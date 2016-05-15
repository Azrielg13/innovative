package com.digitald4.iis.servlet;

import static com.digitald4.common.util.FormatText.formatCurrency;
import static com.digitald4.common.util.FormatText.formatDate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
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
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Paystub;

public class NurseServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			EntityManager entityManager = getEntityManager();
			Nurse nurse = entityManager.find(Nurse.class, Integer.parseInt(request.getParameter("id")));
			request.setAttribute("nurse", nurse);
			DateTime now = DateTime.now();
			request.setAttribute("calendar", getCalendar(nurse, now.getYear(), now.getMonthOfYear()).getOutput());
			request.setAttribute("paystub", new Paystub(entityManager).setNurse(nurse));
			setupTables(request);
			getLayoutPage(request, "/WEB-INF/jsp/nurse.jsp").forward(request, response);
		}
		catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			EntityManager entityManager = getEntityManager();
			Nurse nurse = entityManager.find(Nurse.class, Integer.parseInt(request.getParameter("id")));
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
		EntityManager entityManager = getEntityManager();
		Nurse nurse = entityManager.find(Nurse.class, Integer.parseInt(request.getParameter("id")));
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
		cal.setEvents(nurse.getAppointments(year, month));
		cal.setNotifications(nurse.getNotifications());
		return cal;
	}
	
	private void setupTables(HttpServletRequest request) throws ServletException {
		request.setAttribute("pendcols", getPendingCols());
		request.setAttribute("reviewable_cols", getReviewableCols());
		request.setAttribute("paycols", getPayableCols(getEntityManager()));
		request.setAttribute("payhistcols", getPaystubCols());
		request.setAttribute("unconfirmed_cols", getUnconfirmedCols());
	}
	
	public static List<Column<Appointment>> getPendingCols() {
		List<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Patient", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Date", ""+Appointment.PROPERTY.START, String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return formatDate(app.getStart());
			}
		});
		columns.add(new Column<Appointment>("Time In", "Time In", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return FormatText.formatTime(app.getTimeIn());
			}
		});
		columns.add(new Column<Appointment>("Time Out", "Time Out", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return FormatText.formatTime(app.getTimeOut());
			}
		});
		columns.add(new Column<Appointment>("Percent Complete", "Percent Complete", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return app.getPercentComplete() + "%";
			}
		});
		columns.add(new Column<Appointment>("Action", ""+Appointment.PROPERTY.CANCELLED, Boolean.class, false));
		return columns;
	}
	
	public static List<Column<Appointment>> getReviewableCols() {
		List<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Patient", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Date", "" + Appointment.PROPERTY.START, String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return formatDate(app.getStart());
			}
		});
		columns.add(new Column<Appointment>("Hours", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getPayHours();
			}
		});
		columns.add(new Column<Appointment>("Pay Mileage", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getPayMileage();
			}
 		});
		columns.add(new Column<Appointment>("Percent Complete", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return app.getPercentComplete() + "%";
			}
		});
		columns.add(new Column<Appointment>("Total Payment", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return formatCurrency(app.getPaymentTotal());
			}
		});
		return columns;
	}
	
	public static ArrayList<Column<Appointment>> getPayableCols(EntityManager entityManager) {
		ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Patient", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Date", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return formatDate(app.getStart());
			}
		});
		columns.add(new Column<Appointment>("Payment Type", "PAYING_TYPE_ID", String.class, true,
				GenData.ACCOUNTING_TYPE.get(entityManager).getGeneralDatas()) {
			@Override public Object getValue(Appointment app) {
				return app.getPayingType();
			}
 		});
		columns.add(new Column<Appointment>("Pay Hours", "PAY_HOURS", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getPayHours();
			}
		});
		columns.add(new Column<Appointment>("Hourly Rate", "PAY_RATE", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getPayRate();
			}
 		});
		columns.add(new Column<Appointment>("Per Visit Pay", "PAY_FLAT", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getPayFlat();
			}
 		});
		columns.add(new Column<Appointment>("Pay Mileage", "PAY_MILEAGE", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getPayMileage();
			}
 		});
		columns.add(new Column<Appointment>("Mileage Rate", "PAY_MILEAGE_RATE", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getPayMileageRate();
			}
 		});
		columns.add(new Column<Appointment>("Total Payment", "PAYMENT_TOTAL", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return formatCurrency(app.getPaymentTotal());
			}
		});
		return columns;
	}
	
	public static ArrayList<Column<Paystub>> getPaystubCols() {
		ArrayList<Column<Paystub>> paystubCols = new ArrayList<Column<Paystub>>();
		paystubCols.add(new Column<Paystub>("Pay Date", "", String.class, false) {
			@Override public Object getValue(Paystub stub) throws Exception {
				return formatDate(stub.getPayDate())
						+ " <span><a href=\"report.pdf?type=paystub&id=" + stub.getId() + "\">"
						+ "<img src=\"images/icons/fugue/document-pdf.png\"/></a></span>"; 
			}
		});
		paystubCols.add(new Column<Paystub>("Gross", "", String.class, false) {
			@Override public Object getValue(Paystub stub) throws Exception {
				return formatCurrency(stub.getGrossPay())
						+ " <span><a onclick=\"showDeleteDialog('" + stub.getClass().getName() + "', " + stub.getId() + ")\" target=\"_blank\">"
						+ "<img src=\"images/icons/fugue/cross-circle.png\"/></a></span>";
			}
		});
		paystubCols.add(new Column<Paystub>("Deductions", "", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getPreTaxDeduction() + stub.getPostTaxDeduction());
			}
 		});
		paystubCols.add(new Column<Paystub>("Taxes", "", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getTaxTotal());
			}
		});
		paystubCols.add(new Column<Paystub>("Mileage Reimbursment", "MILEAGE", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getPayMileage());
			}
 		});
		paystubCols.add(new Column<Paystub>("Net Pay", "PAY_RATE", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getNetPay());
			}
 		});
		return paystubCols;
	}
	
	private ArrayList<Column<Appointment>> getUnconfirmedCols() {
		ArrayList<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
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
		String enableConfirmationRequest = getServletContext().getInitParameter("enable_confirmation_request");
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
		return columns;
	}
}
