package com.digitald4.iis.tld;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.tld.DD4Tag;
import com.digitald4.common.tld.InputTag;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.dao.AppointmentDAO;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;

public class AssTabs extends DD4Tag {
	
	private final static String START = "<section class=\"grid_8\">"
			+"<div class=\"block-border\"><form class=\"block-content form\" id=\"complex_form\" method=\"post\" action=\"\">"
			+"<h1>%title</h1>"
			+"<div class=\"columns\">"
			+"<div class=\"col200pxL-left\">"
			+"<h2>Status</h2>"
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
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	@Override
	public String getOutput() throws Exception {
		String out = START.replaceAll("%title", getTitle());
		out += TAB_DEF.replaceAll("%name", "general").replaceAll("%title", "General");
		String tabBody = TAB_BODY_START.replaceAll("%name", "general").replaceAll("%title", "General");
		{
			InputTag inTag = new InputTag();
			inTag.setType(InputTag.Type.TEXT);
			inTag.setObject(getAppointment());
			inTag.setProp("TIME_IN");
			inTag.setValue(FormatText.formatTime(getAppointment().getTimeIn()));
			inTag.setLabel("Time In");
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			inTag = new InputTag();
			inTag.setType(InputTag.Type.TEXT);
			inTag.setObject(getAppointment());
			inTag.setProp("TIME_OUT");
			inTag.setValue(FormatText.formatTime(getAppointment().getTimeOut()));
			inTag.setLabel("Time Out");
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			inTag = new InputTag();
			inTag.setType(InputTag.Type.TEXT);
			inTag.setObject(getAppointment());
			inTag.setProp("MILEAGE");
			inTag.setValue(getAppointment().getMileageD());
			inTag.setLabel("Mileage");
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			tabBody += "<div id=\"dataFileHTML" + getAppointment().getId() + "\">" + getAppointment().getDataFileHTML() + "</div>";
			
			inTag = new InputTag();
			inTag.setType(InputTag.Type.CHECK);
			inTag.setObject(getAppointment());
			inTag.setProp("" + AppointmentDAO.PROPERTY.ASSESSMENT_COMPLETE);
			inTag.setLabel("Assessment Complete");
			inTag.setAsync(true);
			tabBody += inTag.getOutput();
			
			if (isAdmin()) {
				inTag = new InputTag();
				inTag.setType(InputTag.Type.CHECK);
				inTag.setObject(getAppointment());
				inTag.setProp("" + AppointmentDAO.PROPERTY.ASSESSMENT_APPROVED);
				inTag.setLabel("Approved");
				inTag.setAsync(true);
				tabBody += inTag.getOutput();
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

}
