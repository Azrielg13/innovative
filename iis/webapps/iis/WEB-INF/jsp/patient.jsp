<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.util.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Patient patient = (Patient)request.getAttribute("patient");
Collection<Pair<Nurse, Double>> nurses = patient.getNursesByDistance();%>
<script src="js/large-cal.js"></script>
<!--  Google Maps -->
<style type="text/css">
  html { height: 100% }
  body { height: 100%; width: 100%; margin: 0; padding: 0 }
  #map-canvas { height: 25%; width: 25% }
</style>
 <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=maps,places"></script>
 
<!-- Maps End -->
<article class="container_12">
	<section class="grid_8">
		<div id="tab-global" class="tabs-content">
			<ul class="tabs js-tabs same-height">
				<li class="current"><a href="#tab-calendar" title="Calendar">Calendar</a>
				<li><a href="#tab-general" title="General">General Info</a></li>
				<li><a href="#tab-map" title="Map">Map</a></li>
				<li><a href="#tab-assessments" title="Assessments">Assessments</a></li>
				<li><span>Advanced</span></li>
			</ul>
			<div class="tabs-content">
				<div id="tab-calendar">
					<div id="cal_sec">
						<%=request.getAttribute("calendar")%>
					</div>
				</div>
				<div id="tab-general">
					<form class="block-content form" id="simple_form" method="post" action="patient">
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_resolution_id" label="Patient Status" async="true" options="<%=GenData.PATIENT_STATE.get().getGeneralDatas()%>" />
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="name" label="Name" async="true" />
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="d_o_b" label="Date of Birth" async="true" />
						<label for="address">Service Address</label>
						<input type="text" id="address" name="address" value="<%=patient.getServiceAddress()%>" class="full-width" />
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="service_addr_unit" label="Unit #" size="4" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="phone_number" label="Primary Phone Number" async="true" size="20" /> <dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="primary_phone_type_id" label="" size="-1" async="true" options="<%=GenData.PHONE_TYPE.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="alt_contact_number" label="Alt Contact Number" async="true" size="20" /> <dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="alt_phone_type_id" label="" size="-1" async="true" options="<%=GenData.PHONE_TYPE.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="emergency_contact" label="Emergency Contact" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="emergency_contact_phone" label="Emergency Contact Number" size="20" async="true"/> <dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="emergency_contact_phone_type_id" label="" size="-1" async="true" options="<%=GenData.PHONE_TYPE.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="referral_date" label="Referral Date" async="true" />
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_source_id" label="Referral Source" async="true" options="<%=Vendor.getAllActive()%>"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="mr_num" label="Medical Record #" async="true" />
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="dianosis_id" label="Dianosis" async="true" options="<%=GenData.DIANOSIS.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="therapy_type_id" label="Therapy Type" async="true" options="<%=GenData.THERAPY_TYPE.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="rx" label="Pt Rx" async="true" />
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="i_v_access_id" label="IV Access" async="true" options="<%=GenData.IV_ACCESS.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.RADIO%>" object="<%=patient%>" prop="patient_status_id" label="Patient Type" async="true" options="<%=GenData.PATIENT_STATUS.get().getGeneralDatas()%>"/>
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="start_of_care_date" label="Start Date" async="true" />
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="est_last_day_of_service" label="Est. Last Day of Serice" async="true" />
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="billing_id" label="Billing" async="true" options="<%=Vendor.getAllActive()%>"/>
						<dd4:input type="<%=InputTag.Type.ACK_TEXT%>" object="<%=patient%>" prop="labs_frequency" label="Labs? [Frequency]" async="true" />
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="first_recert_due" label="1st Re-certification" async="true" />
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="d_c_date" label="DC Date" async="true" />
						<dd4:input type="<%=InputTag.Type.CHECK%>" object="<%=patient%>" prop="info_in_s_o_s" label="PT Info in SOS?" async="true" />
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="scheduling_preference" label="Scheduling Preference" async="true" />
						<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=patient%>" prop="referral_note" label="Notes" async="true" />
					</form>
				</div>
				<div id="tab-map">
		 			<div id="map-canvas" style="height: 100%; width: 100%">
		 				<button onclick="loadMap()">Load Map</button>
		 			</div>
		 		</div>
		 		<div id="tab-assessments">
		 			<dd4:table title="Assessments" columns="<%=(Collection<Column>)request.getAttribute(\"penass_cols\")%>" data="<%=patient.getAppointments()%>"/>
		 		</div>
		 	</div>
		</div>
	</section>
</article>

<script type="text/javascript">
 	function loadMap() {
 		console.log('loading map...');
	   var latLng = new google.maps.LatLng(<%=patient.getLatitude()%>, <%=patient.getLongitude()%>);
	   var mapOptions = {
	     center: latLng,
	     zoom: 9,
	     mapTypeId: google.maps.MapTypeId.ROADMAP
	   };
	   var map = new google.maps.Map(document.getElementById("map-canvas"),
	       mapOptions);
	   var beachMarker = new google.maps.Marker({
	       position: latLng,
	       map: map,
	       icon: 'images/icons/patient_24.png',
	       title: 'Patient - <%=patient%>'
	   });
	   <% for (Pair<Nurse, Double> pair : nurses) {
	  	 	Nurse nurse = pair.getLeft(); %>
		   new google.maps.Marker({
		       position: new google.maps.LatLng(<%=nurse.getLatitude()%>, <%=nurse.getLongitude()%>),
		       map: map,
		       icon: 'images/icons/nurse24-icon.png',
		       title: 'Nurse - <%=nurse%> (<%=pair.getRight()%> miles)'
		   });
	   	<% } %>
	}
   google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('address'), function(place) {
		saveAddress(place, '<%=patient.getClass().getName()%>', <%=patient.getId()%>);
	}));
 </script>
 