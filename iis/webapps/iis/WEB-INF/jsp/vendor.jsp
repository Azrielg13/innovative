<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.common.model.User" %>
<%@ page import="com.digitald4.common.util.*" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Vendor vendor = (Vendor)request.getAttribute("vendor");%>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<script src="js/angular/models.js"></script>
<script src="js/angular/connector.js"></script>
<script src="js/angular/main.js"></script>
<div ng-app="iis">
<article class="container_12">
	<section class="grid_8">
		<div id="tab-global" class="tabs-content">
			<ul class="tabs js-tabs same-height">
				<li class="current"><a href="#tab-calendar" title="Calendar">Calendar</a>
				<li><a href="#tab-general" title="General">General</a></li>
				<li><a href="#tab-patients" title="Patients">Patients</a></li>
				<li><a href="#tab-pending" title="Pending Assessment">Pending Assessment</a></li>
				<li><a href="#tab-billing" title="Billing">Billable</a></li>
				<li><a href="#tab-reports" title="Reports">Reports</a></li>
				<li><span>Advanced</span></li>
			</ul>
			<div class="tabs-content">
				<div id="tab-calendar">
					<div id="cal_sec">
						<%=request.getAttribute("calendar")%>
					</div>
				</div>
				<div id="tab-general">
					<div class="block-content form">
						<dd4:input type="<%=InputTag.Type.CHECK%>" object="<%=vendor%>" prop="active" label="Active" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="name" label="Name" async="true"/>
						<label for="address">Address</label>
						<input type="text" id="address" name="address" value="<%=vendor.getAddress()%>" class="full-width" />
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="phone_number" label="Phone Number" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="fax_number" label="Fax Number" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="contact_name" label="Contact" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="contact_number" label="Contact Phone #" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="contact_email" label="Contact Email" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_rate" label="Billing Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_rate_2hr_soc" label="Billing Rate < 2hr SOC" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_rate_2hr_roc" label="Billing Rate < 2hr ROC" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_flat" label="Flat Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_flat_2hr_soc" label="Flat < 2hr SOC" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_flat_2hr_roc" label="Flat < 2hr ROC" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="mileage_rate" label="Mileage Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=vendor%>" prop="notes" label="Notes" async="true"/>
					</div>
				</div>
				<div id="tab-patients">
					<dd4:table title="Patients" columns="<%=(Collection<Column>)request.getAttribute(\"patients_cols\")%>" data="<%=vendor.getPatients()%>"/>
				</div>
				<div id="tab-pending">
					<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"pendcols\")%>" data="<%=vendor.getPendingAssessments()%>"/>
				</div>
				<div id="tab-billing">
					<dd4:table title="Billable" columns="<%=(Collection<Column>)request.getAttribute(\"billcols\")%>" data="<%=vendor.getPayables()%>"/>
					<label>Invoice Name</label><input type="text" /> <button>Create Invoice</button>
				</div><div id="tab-reports">
					<a href="report.pdf?type=inv&vendor_id=<%=vendor.getId()%>">Invoice</a>
				</div>
			</div>
		</div>
	</section>
</article>
</div>
<script>
	google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('address'), function(place) {
		saveAddress(place, '<%=vendor.getClass().getName()%>', <%=vendor.getId()%>, function(object){});
	}));
</script>
