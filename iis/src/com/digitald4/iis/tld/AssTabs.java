package com.digitald4.iis.tld;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.tld.DD4Tag;
import com.digitald4.common.tld.InputTag;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;

public class AssTabs extends DD4Tag {
	
	private final static String START = "<section class=\"grid_8\">\n"
			+"\t<div class=\"block-border\"><form class=\"block-content form\" id=\"complex_form\" method=\"post\" action=\"\">\n"
			+"\t\t<h1>%title</h1>\n"
			+"\t\t<div class=\"block-controls\">\n"
			+"\t\t\t<!-- Top tabs -->\n"
			+"\t\t\t<ul class=\"controls-tabs js-tabs\">\n"
			+"\t\t\t\t<li class=\"current\"><a href=\"#\" title=\"Charts\"><img src=\"images/icons/web-app/24/Bar-Chart.png\" width=\"24\" height=\"24\"></a></li>\n"
			+"\t\t\t\t<li><a href=\"#\" title=\"Comments\"><img src=\"images/icons/web-app/24/Comment.png\" width=\"24\" height=\"24\"></a></li>\n"
			+"\t\t\t\t<li><a href=\"#\" title=\"Medias\"><img src=\"images/icons/web-app/24/Picture.png\" width=\"24\" height=\"24\"></a></li>\n"
			+"\t\t\t\t<li><a href=\"#\" title=\"Users\"><img src=\"images/icons/web-app/24/Profile.png\" width=\"24\" height=\"24\"></a></li>\n"
			+"\t\t\t\t<li><a href=\"#\" title=\"Informations\"><img src=\"images/icons/web-app/24/Info.png\" width=\"24\" height=\"24\"></a></li>\n"
			+"\t\t\t</ul>\n"
			+"\t\t</div>\n"
			+"\t\t<!-- 				<ul class=\"message warning no-margin\">\n"
			+"\t\t\t<li>This is a <strong>warning message</strong>, inside a box</li>\n"
			+"\t\t\t<li class=\"close-bt\"></li>\n"
			+"\t\t</ul> -->\n"
			+"\t\t<div class=\"columns\">\n"
			+"\t\t\t<div class=\"col200pxL-left\">\n"
			+"\t\t\t\t<h2>Status</h2>\n"
			+"\t\t\t\t<ul class=\"side-tabs js-tabs same-height\">\n";
	private final static String TAB_DEF = "\t\t\t\t\t<li><a href=\"#%name\" title=\"%title\">%title</a></li>\n";
	private final static String MID = "\t\t\t\t</ul>\n"
			+"\t\t\t\t</div>\n"
			+"\t\t\t\t<div class=\"col200pxL-right\">\n";
	private final static String TAB_BODY_START = "<div id=\"%name\" class=\"tabs-content\">";
	private final static String TAB_BODY_END = "</div>";
	private final static String END = "\t\t\t\t</div>\n"
			+"\t\t\t</div>\n"
			+"\t\t</form>\n"
			+"\t</div>\n"
			+"</section>\n"
			+"<div class=\"clear\"></div>\n"
			+"<div class=\"clear\"></div>\n";
	
	private String title;
	private Appointment appointment;
	
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
		return title;
	}
	
	@Override
	public String getOutput() {
		String out = START.replaceAll("%title", getTitle());
		String tabBody = "";
		InputTag inTag = new InputTag();
		for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
			String name = cat.getName().toLowerCase().replaceAll(" ", "_");
			out += TAB_DEF.replaceAll("%name", name).replaceAll("%title", cat.getName());
			tabBody += TAB_BODY_START.replaceAll("%name", name).replaceAll("%title", cat.getName());
			for (GeneralData ques : cat.getGeneralDatas()) {
				inTag.setType(InputTag.Type.valueOf(ques.getData()));
				inTag.setObject(getAppointment());
				inTag.setProp(""+ques.getId());
				inTag.setOptions(ques.getGeneralDatas());
				inTag.setLabel(ques.getName());
				inTag.setAsync(true);
				tabBody += inTag.getOutput();
			}
			tabBody += TAB_BODY_END;
		}
		out += MID;
		out += tabBody;
		out += END;
		return out;
	}

}
