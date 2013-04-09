<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.util.FormatText" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Appointment appointment = (Appointment)request.getAttribute("appointment");%>
<article class="container_12">
	<section class="grid_4">
		<div class="block-content form">
			<%if (appointment.getId() != null) {%>
				<input type="hidden" id="appointment_id" value="<%=appointment.getId()%>" />
			<%}%>
			<dd4:input label="Patient" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="patient_id"  options="<%=Patient.getPatientsByState(GenData.PATIENT_ACTIVE.get())%>" />
			<dd4:input label="Nurse" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="nurse_id"  options="<%=Nurse.getAll()%>" />
			<p><span class="label">Start</span>
				Date:<input type="TEXT"  class="datepicker" name="appointment.start_date" id="start_date" value="<%=FormatText.formatDate(appointment.getStartDate())%>"/>
				Time:<input type="TEXT" name="appointment.start_time" id="start_time" value="<%=appointment.getStartTime()%>"/>
			</p>
			<p><span class="label">End</span>
				Date:<input type="TEXT"  class="datepicker" name="appointment.end_date" id="end_date" value="<%=FormatText.formatDate(appointment.getEndDate())%>"/>
				Time:<input type="TEXT" name="appointment.end_time" id="end_time" value="<%=appointment.getEndTime()%>"/>
			</p>
			<button onClick="submitAppointment()">Save</button>
		</div>
	</section>
</article>
