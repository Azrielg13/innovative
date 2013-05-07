<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.common.model.*" %>
<%@ page import="com.digitald4.common.util.*" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Nurse nurse = (Nurse)request.getAttribute("nurse");
User user = nurse.getUser();
int year = (Integer)request.getAttribute("year");
int month = (Integer)request.getAttribute("month");
Collection<GeneralData> licenseTypes = (Collection<GeneralData>)request.getAttribute("licenses");%>
<script src="js/large-cal.js"></script>
<script src="js/angular/models.js"></script>
<script src="js/angular/connector.js"></script>
<script src="js/angular/main.js"></script>
<article class="container_8">
	<section class="grid_8" id="cal_sec">
		<div id="tab-global" class="tabs-content">
			<ul class="tabs js-tabs same-height">
				<li class="current"><a href="#tab-calendar" title="Calendar">Calendar</a>
				<li><a href="#tab-general" title="General">General</a></li>
				<li><a href="#tab-license" title="Licenses">Licenses</a></li>
				<li><span>Advanced</span></li>
			</ul>
			<div class="tabs-content">
				<div id="tab-calendar">
					<dd4:largecal title="Nurse Calendar" year="<%=year%>" month="<%=month%>" events="<%=nurse.getAppointments()%>"/>
				</div>
				<div id="tab-general">
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
				</div>
				<div id="tab-license">
					<div ng-app="iis">
						<div ng-controller="AppCtrl">
							<div enter>Angular Test</div>
						</div>
						<div licenses="<%=nurse.getId()%>"></div>
					</div>
				</div>
			</div>
		</div>
	</section>
</article>
