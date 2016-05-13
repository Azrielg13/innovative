package com.digitald4.iis.tld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.digitald4.common.component.Column;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.tld.DD4Tag;
import com.digitald4.common.tld.InputTag;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.dao.AppointmentDAO;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;

public class AssTabs extends DD4Tag {
	
	private final static String START = "<section class=\"grid_10\">"
			+"<div class=\"block-border\"><form class=\"block-content form\" id=\"complex_form\" method=\"post\" action=\"\">"
			+"<h1>%title</h1>"
			+"<div class=\"columns\">"
			+"<div class=\"col200pxL-left\">"
			+"<h3>%subtitle</h3>"
			+"<ul class=\"side-tabs js-tabs same-height\">";
	private final static String TAB_DEF = "<li><a href=\"#%name\" title=\"%title\">%title</a></li>";
	private final static String MID = "</ul>"
			+"</div>"
			+"<div class=\"col200pxL-right\">";
	private final static String TAB_BODY_START = "<div id=\"%name\" class=\"tabs-content\">";
	private final static String TAB_BODY_END = "</div>";
	private final static String END = "</div>"
			+"</div>"
			+"</form>"
			+"</div>"
			+"</section>"
			+"<div class=\"clear\"></div>"
			+"<div class=\"clear\"></div>";
	
	private String title;
	private Appointment appointment;
	private boolean admin;
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		if (title != null) {
			return title;
		}
		Appointment app = getAppointment();
		return "Assessment: " + app.getPatient() + " " + FormatText.formatDate(app.getStartDate());
	}
	
	public String getSubTitle() {
		Appointment app = getAppointment();
		return "Patient: <a href=\"patient?id=" + app.getPatientId() + "\">" + app.getPatient() + "</a>\n" + 
				"Date: " + FormatText.formatDate(app.getStartDate()) + "\n" +
				"Time: " + FormatText.formatTime(app.getStart()) + " - " + FormatText.formatTime(app.getEnd()) + "\n" +
				"Nurse: <a href=\"nurse?id=" + app.getNurseId() + "#&tab-payable\">" + app.getNurse() + "</a>";
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	@Override
	public String getOutput() throws Exception {
		String out = START.replaceAll("%title", getTitle()).replaceAll("%subtitle", getSubTitle());
		out += TAB_DEF.replaceAll("%name", "general").replaceAll("%title", "General");
		String tabBody = TAB_BODY_START.replaceAll("%name", "general").replaceAll("%title", "General");
		{
			InputTag inTag = new InputTag();
			inTag.setType(InputTag.Type.TEXT);
			inTag.setObject(getAppointment());
			inTag.setProp("TIME_IN");
			inTag.setValue(FormatText.formatTime(getAppointment().getTimeIn()));
			inTag.setLabel("Time In");
			inTag.setSize(5);
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			inTag = new InputTag();
			inTag.setType(InputTag.Type.TEXT);
			inTag.setObject(getAppointment());
			inTag.setProp("TIME_OUT");
			inTag.setValue(FormatText.formatTime(getAppointment().getTimeOut()));
			inTag.setLabel("Time Out");
			inTag.setSize(5);
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			inTag = new InputTag();
			inTag.setType(InputTag.Type.TEXT);
			inTag.setObject(getAppointment());
			inTag.setProp("MILEAGE");
			inTag.setValue(getAppointment().getMileageD());
			inTag.setLabel("Mileage");
			inTag.setSize(5);
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			tabBody += "<br>";
			
			tabBody += "<div id=\"dataFileHTML" + getAppointment().getId() + "\">" + getAppointment().getDataFileHTML() + "</div>";
			
			tabBody += "<br>";
			inTag = new InputTag();
			inTag.setType(InputTag.Type.CHECK);
			inTag.setObject(getAppointment());
			inTag.setProp("" + AppointmentDAO.PROPERTY.ASSESSMENT_COMPLETE);
			inTag.setLabel("Assessment Complete");
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			if (isAdmin()) {
				tabBody += "<br>";
				tabBody += "<section class=\"grid_12\"><div class=\"block-border\">";
				inTag = new InputTag();
				inTag.setType(InputTag.Type.CHECK);
				inTag.setObject(getAppointment());
				inTag.setProp("" + AppointmentDAO.PROPERTY.ASSESSMENT_APPROVED);
				inTag.setLabel("Approved");
				inTag.setAsync(true);
				tabBody += inTag.getOutput();
				
				tabBody += getBillingTable();

				tabBody += "</div></section>";
			}
		}
		tabBody += TAB_BODY_END;
		
		for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
				String name = cat.getName().toLowerCase().replaceAll(" ", "_");
				out += TAB_DEF.replaceAll("%name", name).replaceAll("%title", cat.getName());
				tabBody += TAB_BODY_START.replaceAll("%name", name).replaceAll("%title", cat.getName());
			for (GeneralData ques : cat.getGeneralDatas()) {
					try {
						InputTag inTag = new InputTag();
						inTag.setType(InputTag.Type.valueOf(ques.getDataAttribute("type").toString()));
						inTag.setObject(getAppointment());
						inTag.setProp("" + ques.getId());
						inTag.setOptions(ques.getGeneralDatas());
						inTag.setLabel(ques.getName());
						inTag.setAsync(true);
						tabBody += inTag.getOutput();
					} catch (Exception e) {
						System.out.println("Error processing: " + ques);
						e.printStackTrace();
					}
			}
			tabBody += TAB_BODY_END;
		}
		out += MID;
		out += tabBody;
		out += END;
		return out;
	}
	
	public String getBillingTable() throws Exception {
		Appointment app = getAppointment();
		StringBuffer table = new StringBuffer();
		table.append("<TABLE><TR><TH></TH><TH>Type</TH><TH>Hours</TH><TH>Hourly Rate</TH>");
		table.append("<TH>Per Vist</TH><TH>Mileage</TH><TH>Mileage Rate</TH><TH>Total</TH></TR>");
		table.append("<TR><TD>Payable</TD>");
		table.append(getField("PAYING_TYPE_ID", app.getPayingType(), GenData.ACCOUNTING_TYPE.get().getGeneralDatas()));
		table.append(getField("PAY_HOURS", app.getPayHours()));
		table.append(getField("PAY_RATE", app.getPayRate()));
		table.append(getField("PAY_FLAT", app.getPayFlat()));
		table.append(getField("PAY_MILEAGE", app.getPayMileage()));
		table.append(getField("PAY_MILEAGE_RATE", app.getPayMileageRate()));
		table.append("<TD id=\"paymentTotal" + app.getId() + "\">" + app.getPaymentTotal() + "</TD></TR>");
		table.append("<TR><TD>Billable</TD>");
		table.append(getField("BILLING_TYPE_ID", app.getBillingType(), GenData.ACCOUNTING_TYPE.get().getGeneralDatas()));
		table.append(getField("BILLED_HOURS", app.getBilledHours()));
		table.append(getField("BILLING_RATE", app.getBillingRate()));
		table.append(getField("BILLING_FLAT", app.getBillingFlat()));
		table.append(getField("BILLING_MILEAGE", app.getBillingMileage()));
		table.append(getField("BILLING_MILEAGE_RATE", app.getBillingMileageRate()));
		table.append("<TD id=\"billingTotal" + app.getId() + "\">" + app.getBillingTotal() + "</TD></TR>");
		table.append("</TABLE>");
		List<Column<Appointment>> columns = new ArrayList<Column<Appointment>>();
		columns.add(new Column<Appointment>("Billing Type", "" + Appointment.PROPERTY.BILLING_TYPE_ID_D, String.class, true, GenData.ACCOUNTING_TYPE.get().getGeneralDatas()) {
			@Override public Object getValue(Appointment app) {
				return app.getBillingType();
			}
 		});
		columns.add(new Column<Appointment>("Billed Hours", "", String.class, false) {
			@Override public Object getValue(Appointment app) {
				return app.getLoggedHours();
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
		return table.toString();
	}
	
	private String getField(String prop, Object value) {
		return getField(prop, value, null);
	}
	
	private String getField(String prop, Object value, Collection<GeneralData> options) {
		InputTag input = new InputTag();
		input.setObject(getAppointment());
		input.setProp(prop);
		input.setValue(value);
		input.setAsync(true);
		if (options != null) {
			input.setType(InputTag.Type.COMBO);
			input.setOptions(options);
		} else {
			input.setType(InputTag.Type.TEXT);
		}
		return "<TD>" + input.getOutput() + "</TD>";
	}
}
