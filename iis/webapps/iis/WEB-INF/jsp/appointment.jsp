<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Appointment appointment = (Appointment)session.getAttribute("appointment");%>
<article class="container_12">
	<section class="grid_4">
		<form class="block-content form" id="simple_form" method="post" action="new_app">
			<dd4:input label="Patient" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="patient_id"  options="<%=Patient.getPatientsByState(GenData.PATIENT_ACTIVE.get())%>" />
			<dd4:input label="Nurse" type="<%=InputTag.Type.COMBO%>" object="<%=appointment%>" prop="nurse_id"  options="<%=Nurse.getAll()%>" />
			<dd4:input label="Start Date" type="<%=InputTag.Type.DATE%>" object="<%=appointment%>" prop="start_date" />
			<dd4:input label="Start Time" type="<%=InputTag.Type.TEXT%>" object="<%=appointment%>" prop="start_time" />
			<dd4:input label="End Date" type="<%=InputTag.Type.DATE%>" object="<%=appointment%>" prop="end_date" />
			<dd4:input label="End Time" type="<%=InputTag.Type.TEXT%>" object="<%=appointment%>" prop="end_time" />
			<button type="submit">Save</button>
		</form>
	</section>
</article>
