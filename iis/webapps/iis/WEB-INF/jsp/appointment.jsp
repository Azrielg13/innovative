<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.util.FormatText" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Appointment appointment = (Appointment)request.getAttribute("appointment");%>
<article class="container_8">
	<section class="grid_8">
		<div class="block-content form">
			<%if (appointment.getId() != null) {%>
				<input type="hidden" id="appointment_id" value="<%=appointment.getId()%>" />
			<%}%>
			<dd4:input label="Patient" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="patient_id"  options="<%=(Collection<Patient>)request.getAttribute(\"patients\")%>" />
			<dd4:input label="Nurse" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="nurse_id"  options="<%=(Collection<Nurse>)request.getAttribute(\"nurses\")%>" />
			<p><span class="label">Start</span>
				Date:<input type="TEXT" name="appointment.start_date" id="start_date" value="<%=FormatText.formatDate(appointment.getStartDate())%>" class="datepicker"/>
				<img src="images/icons/fugue/calendar-month.png" width="16" height="16" />
				Time:<input type="TEXT" name="appointment.start_time" id="start_time" value="<%=appointment.getStartTime()%>"/>
			</p>
			<p><span class="label">End</span>
				Date:<input type="TEXT" name="appointment.end_date" id="end_date" value="<%=FormatText.formatDate(appointment.getEndDate())%>" class="datepicker"/>
				<img src="images/icons/fugue/calendar-month.png" width="16" height="16" />
				Time:<input type="TEXT" name="appointment.end_time" id="end_time" value="<%=appointment.getEndTime()%>"/>
			</p>
			<button onClick="submitAppointment()">Save</button> <button onClick="cancelEditApp()">Cancel</button>
		</div>
	</section>
</article>
