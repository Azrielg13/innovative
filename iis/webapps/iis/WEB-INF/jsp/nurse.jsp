<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.common.model.*" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Nurse nurse = (Nurse)request.getAttribute("nurse");
User user = nurse.getUser();
int year = (Integer)request.getAttribute("year");
int month = (Integer)request.getAttribute("month");%>
<article class="container_12">
	<section class="grid_8" id="cal_sec">
		<dd4:largecal title="Nurse Calendar" year="<%=year%>" month="<%=month%>" events="<%=nurse.getAppointments()%>"/>
	</section>
	<section class="grid_4">
		<form class="block-content form" id="simple_form" method="post" action="patient">
			<input type="hidden" name="id" id="id" value="<%=nurse.getId()%>"/>
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="first_name" label="First Name" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="last_name" label="Last Name" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="address" label="Home Address" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="email" label="Email Address" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate" label="Pay Rate" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2hr_soc" label="< 2hr SOC Pay Rate" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2hr_roc" label="< 2hr ROC Pay Rate" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="mileage_rate" label="Mileage Rate" />
			<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=user%>" prop="notes" label="Notes" />
			<button type="submit">Save</button>
		</form>
	</section>
</article>
<script src="js/large-cal.js"></script>
