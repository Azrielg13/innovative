package com.digitald4.iis.service;

import static com.digitald4.common.util.FormatText.formatDate;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.util.Emailer;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.Obfuscator;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;

public class AppointmentService {
	
	private final static String CONFIRM_SUBJECT = "Confirm Appointment %DATE_TIME% %PATIENT%";
	private final static String CONFIRM_MESSAGE = "<div style=\"font-size:24px;min-height:28px;color:#000\">Innovative Infusion Solutions, LLC</div><br>"
			+ "%NURSE%,<br><br>"
			+ "Please confirm or decline this appointment on %DATE_TIME% for %PATIENT%<br><br>"
			+ "<a href=\"http://www.iisos.net/app_confirm?action=confirm&obf_id=%OBF_ID%&nurse_oid=%NURSE_OID%\">Quick Confirmation</a><br><br>"
			+ "<a href=\"http://www.iisos.net/app_confirm?action=view&obf_id=%OBF_ID%&nurse_oid=%NURSE_OID%\">View Appointment</a>";

	public JSONObject setNurseConfirmed(HttpServletRequest request) throws JSONException, Exception {
		return Appointment.getInstance(Integer.parseInt(request.getParameter("id")))
				.setNurseConfirmRes(GenData.CONFIRMED_ACCEPTED.get(), DateTime.now(), "")
				.save().toJSON();
	}

	public JSONObject sendConfirmationRequest(HttpServletRequest request, Emailer emailer) throws JSONException, Exception {
		Appointment appointment = Appointment.getInstance(Integer.parseInt(request.getParameter("id")));
		Nurse nurse = appointment.getNurse();
		Patient patient = appointment.getPatient();
		emailer.sendmail(request.getServletContext().getInitParameter("emailuser"),
				nurse.getEmail(),
				CONFIRM_SUBJECT.replaceAll("%DATE_TIME%", formatDate(appointment.getStart(), FormatText.USER_DATETIME))
						.replaceAll("%PATIENT%", "" + patient),
				CONFIRM_MESSAGE.replaceAll("%OBF_ID%", "" + Obfuscator.obfuscate(appointment.getId()))
						.replaceAll("%DATE_TIME%", formatDate(appointment.getStart(), FormatText.USER_DATETIME))
						.replaceAll("%PATIENT%", "" + patient).replaceAll("%NURSE%", "" + nurse)
						.replaceAll("%NURSE_OID%", Obfuscator.obfuscate(nurse.getId())));
		return appointment.toJSON();
	}
}
