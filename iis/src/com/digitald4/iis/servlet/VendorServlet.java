package com.digitald4.iis.servlet;

import java.util.ArrayList;
import java.util.List;

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
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Vendor;

public class VendorServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String action = request.getParameter("action");
			if (action != null && action.equalsIgnoreCase("cal")) {
				processCalendarRequest(request, response);
				return;
			}
			Vendor vendor = Vendor.getInstance(Integer.parseInt(request.getParameter("id")));
			request.setAttribute("vendor", vendor);
			DateTime now = DateTime.now();
			request.setAttribute("calendar", getCalendar(vendor, now.getYear(), now.getMonthOfYear()).getOutput());
			NurseServlet.setupTables(request);
			PatientsServlet.setupTable(request);
			setupTables(request);
			getLayoutPage(request, "/WEB-INF/jsp/vendor.jsp").forward(request, response);
		} catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	private void processCalendarRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Vendor vendor = Vendor.getInstance(Integer.parseInt(request.getParameter("id")));
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		LargeCalTag cal = getCalendar(vendor, year, month);
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
	
	public static LargeCalTag getCalendar(Vendor vendor, int year, int month) {
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Vendor Calendar");
		cal.setIdType("vendor_id");
		cal.setUserId(vendor.getId());
		cal.setYear(year);
		cal.setMonth(month);
		cal.setEvents(vendor.getAppointments(year, month));
		cal.setNotifications(vendor.getNotifications());
		return cal;
	}
	
	public static void setupTables(HttpServletRequest request) {
		List<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Patient", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<a href=\"assessment?id=" + app.getId() + "\">" + app.getPatient() + "</a>";
			}
		});
		columns.add(new Column<Appointment>("Date", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return FormatText.formatDate(app.getStart());
			}
		});
		columns.add(new Column<Appointment>("Billing Type", "" + Appointment.PROPERTY.BILLING_TYPE_ID_D, String.class, true, GenData.ACCOUNTING_TYPE.get().getGeneralDatas()) {
			@Override public Object getValue(Appointment app) {
				return app.getBillingType();
			}
 		});
		columns.add(new Column<Appointment>("Billed Hours", "" + Appointment.PROPERTY.BILLED_HOURS_D, String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getBilledHours();
			}
		});
		columns.add(new Column<Appointment>("Hourly Rate", "BILLING_RATE", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getBillingRate();
			}
 		});
		columns.add(new Column<Appointment>("Per Visit Cost", "BILLING_FLAT", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getBillingFlat();
			}
 		});
		columns.add(new Column<Appointment>("Billed Mileage", "BILLING_MILEAGE", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getBillingMileage();
			}
 		});
		columns.add(new Column<Appointment>("Mileage Rate", "BILLING_MILEAGE_RATE", String.class, true) {
			@Override public Object getValue(Appointment app) {
				return app.getBillingMileageRate();
			}
 		});
		columns.add(new Column<Appointment>("Total Payment", "", String.class, false) {
			@Override public Object getValue(Appointment app) throws Exception {
				return "<div id='billingTotal" + app.getId() + "'>" + app.getBillingTotal() + "</div>";
			}
		});
		request.setAttribute("billcols", columns);
		
		List<Column<Invoice>> cols2 = new ArrayList<Column<Invoice>>();
		cols2.add(new Column<Invoice>("Name", "" + Invoice.PROPERTY.NAME, String.class, false));
		cols2.add(new Column<Invoice>("Date", "", String.class, false) {
			@Override public Object getValue(Invoice invoice) throws Exception {
				return FormatText.formatDate(invoice.getGenerationTime(), FormatText.MYSQL_DATETIME)
						+ " <span><a href=\"report.pdf?type=inv&id=" + invoice.getId() + "\" target=\"_blank\">"
						+ "<img src=\"images/icons/fugue/document-pdf.png\"/></a></span>"; 
			}
		});
		cols2.add(new Column<Invoice>("Billed", "", String.class, false) {
			@Override public Object getValue(Invoice invoice) {
				return FormatText.CURRENCY.format(invoice.getTotalDue())
						+ " <span><a onclick=\"showDeleteDialog('" + invoice.getClass().getName() + "', " + invoice.getId() + ")\" target=\"_blank\">"
						+ "<img src=\"images/icons/fugue/cross-circle.png\"/></a></span>";
			}
		});
		cols2.add(new Column<Invoice>("Status", "STATUS", String.class, false));
		cols2.add(new Column<Invoice>("Comment", "" + Invoice.PROPERTY.COMMENT, String.class, true));
		cols2.add(new Column<Invoice>("Received", "TOTAL_PAID", String.class, true));
		request.setAttribute("invoicecols", cols2);
	}
}
