<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.util.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Patient patient = (Patient)request.getAttribute("patient");
int year = (Integer)request.getAttribute("year");
int month = (Integer)request.getAttribute("month");
Collection<Pair<Nurse, Double>> nurses = patient.getNursesByDistance();%>
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
				<li><a href="#tab-assessments" title="Assessments">Pending Assessment</a></li>
				<li><span>Advanced</span></li>
			</ul>
			<div class="tabs-content">
				<div id="tab-calendar">
					<div id="cal_sec">
						<dd4:medcal title="Patient Calendar" year="<%=year%>" month="<%=month%>" events="<%=patient.getAppointments()%>"/>
					</div>
				</div>
				<div id="tab-general">
					<form class="block-content form" id="simple_form" method="post" action="patient">
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_resolution_id" label="Patient State" options="<%=GenData.PATIENT_STATE.get().getGeneralDatas()%>"/>
						<input type="text" id="address" name="address" value="<%=patient.getServiceAddress()%>" class="full-width" />
					</form>
				</div>
				<div id="tab-map">
		 			<div id="map-canvas" style="height: 100%; width: 90%"/>
		 			 <% for (Pair<Nurse, Double> pair : nurses) { %>
	  	 				<div>(<%=pair.getLeft()%>, <%=pair.getRight()%>)</div>
	  	 			<% } %>	
		 		</div>
		 	</div>
		</div>
	</section>
</article>

<script type="text/javascript">
 	function initialize() {
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
   google.maps.event.addDomListener(window, 'load', initialize);
   google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('address'), function(place) {
		saveAddress(place, '<%=patient.getClass().getName()%>', <%=patient.getId()%>);
	}));
 </script>

<script>
	function setMonth(year, month) {
		// Request
		var data = {
			action: "cal",
			id: $('#id').val(),
			year: year,
			month: month
		};
		var target = "patient";//document.location.href.match(/^([^#]+)/)[1];
		// Send
		$.ajax({
			url: target,
			dataType: 'json',
			type: 'POST',
			data: data,
			success: function(data, textStatus, XMLHttpRequest) {
				if (data.valid) {
					document.all.cal_sec.innerHTML = data.html;
				} else {
					document.all.cal_sec.innerHTML = 'An unexpected error occured, please try again';
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				document.all.cal_sec.innerHTML = 'Error while contacting server, please try again: ' + errorThrown;
			}
		});
	}
</script>