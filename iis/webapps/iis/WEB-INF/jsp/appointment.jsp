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
			<%if (!appointment.isNewInstance()) {%>
				<div class="columns">
					<div class="colx2-right">
						<a href="assessment?id=<%=appointment.getId()%>">Assessment<img src="images/icons/link_arrow.png"></a>
					</div>
				</div>
			<%}%>
			<%if (appointment.getId() != null) {%>
				<input type="hidden" id="appointment_id" value="<%=appointment.getId()%>" />
			<%}%>
			<dd4:input label="Patient" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="patient_id"  options="<%=(Collection<Patient>)request.getAttribute(\"patients\")%>" />
			<dd4:input label="Nurse" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="nurse_id"  options="<%=(Collection<Nurse>)request.getAttribute(\"nurses\")%>" />
			<div class="columns">
				<div class="colx2-left">
					<span class="label">Date</span>
					<input type="TEXT" name="appointment.start_date" id="start_date" value="<%=FormatText.formatDate(appointment.getStartDate())%>" class="datepicker"/>
					<img src="images/icons/fugue/calendar-month.png" width="16" height="16" />
				</div>
				<div class="colx2-right">
					<%if (appointment.isCancelled()) {%>
						<label>Cancelled</label>
						<p><%=appointment.getCancelReason()%></p>
					<%} else if (!appointment.isNewInstance()) {%>
						<br>
						<button onclick="showCancelAppointmentDialog()"><img src="images/icons/fugue/cross-circle.png" width="16" height="16">Cancel Appointment</button>
					<%}%>
				</div>	
			</div>
			<p><span class="label">Time</span>
				Start:<input type="TEXT" name="appointment.start_time" id="start_time" value="<%=appointment.getStartTime()%>"/>
				End:<input type="TEXT" name="appointment.end_time" id="end_time" value="<%=appointment.getEndTime()%>"/>
			</p>
			<button onClick="submitAppointment()">Save</button> <button onClick="cancelEditApp()">Cancel</button>
		</div>
	</section>
</article>
