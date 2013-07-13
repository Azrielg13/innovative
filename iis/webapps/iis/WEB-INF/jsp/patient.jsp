<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Patient patient = (Patient)request.getAttribute("patient");
int year = (Integer)request.getAttribute("year");
int month = (Integer)request.getAttribute("month");%>
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
						<input type="hidden" name="id" id="id" value="<%=patient.getId()%>"/>
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_resolution_id" label="Patient State" options="<%=GenData.PATIENT_STATE.get().getGeneralDatas()%>"/>
						<input type="text" id="address" name="address" value="<%=patient.getServiceAddress()%>" class="full-width" />
						<button type="submit">Save</button>
					</form>
				</div>
				<div id="tab-map">
		 			<div id="map-canvas" style="height: 100%; width: 90%"/>
		 		</div>
		 	</div>
		</div>
	</section>
</article>

<!--  Google Maps -->
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; width: 100%; margin: 0; padding: 0 }
      #map-canvas { height: 25%; width: 25% }
    </style>
    <!-- script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&sensor=false"></script -->
    <meta charset="utf-8">
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=maps,places"></script>
    
    <script type="text/javascript">
      function initialize() {
    	  console.log('initialise called');
        var mapOptions = {
          center: new google.maps.LatLng(-34.397, 150.644),
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
        var input = /** @type {HTMLInputElement} */(document.getElementById('address'));
		var autocomplete = new google.maps.places.Autocomplete(input);  
		// These are my options for the AutoComplete
		//autocomplete.setTypes(['(cities)']);
		autocomplete.setComponentRestrictions({'country': 'us'});
		
		google.maps.event.addListener(autocomplete, 'place_changed', function() {
		    result = autocomplete.getPlace();
		    if(typeof result.address_components == 'undefined') {
		        // The user pressed enter in the input 
		        // without selecting a result from the list
		        // Let's get the list from the Google API so that
		        // we can retrieve the details about the first result
		        // and use it (just as if the user had actually selected it)
		        autocompleteService = new google.maps.places.AutocompleteService();
		        autocompleteService.getPlacePredictions(
		            {
		                'input': result.name,
		                'offset': result.name.length,
		                // I repeat the options for my AutoComplete here to get
		                // the same results from this query as I got in the 
		                // AutoComplete widget
		                //'types': ['(cities)'],
		                'componentRestrictions': {'country': 'us'}
		            },
		            function listentoresult(list, status) {
		                if(list == null || list.length == 0) {
		                    // There are no suggestions available.
		                    // The user saw an empty list and hit enter.
		                    console.log("No results");
		                } else {
		                    // Here's the first result that the user saw
		                    // in the list. We can use it and it'll be just
		                    // as if the user actually selected it
		                    // themselves. But first we need to get its details
		                    // to receive the result on the same format as we
		                    // do in the AutoComplete.
		                    placesService = new google.maps.places.PlacesService(document.getElementById('placesAttribution'));
		                    placesService.getDetails(
		                        {'reference': list[0].reference},
		                        function detailsresult(detailsResult, placesServiceStatus) {
		                            // Here's the first result in the AutoComplete with the exact
		                            // same data format as you get from the AutoComplete.
		                            console.log("We selected the first item from the list automatically because the user didn't select anything");
		                            console.log(detailsResult);
		                        }
		                    );
		                }
		            }
		        );
		    } else {
		        // The user selected a result from the list, we can 
		        // proceed and use it right away
		        console.log("User selected an item from the list");
		        console.log(result.geometry.location.lat());
		        console.log(result.geometry.location.lng());
		        saveAddress(result);
		    }
		});
	  }
      console.log('got here');
      google.maps.event.addDomListener(window, 'load', initialize);
    </script>
<!-- Maps End -->

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
	
	function saveAddress(place)	{
		var data = {
			id: $('#id').val(),
			classname: 'com.digitald4.iis.model.Patient',
			attribute: 'address',
			address: place.formatted_address,
			latitude: place.geometry.location.lat(),
			longitude: place.geometry.location.lng()
		};
		var target = "update";//document.location.href.match(/^([^#]+)/)[1];
		// Send
		$.ajax({
			url: target,
			dataType: 'json',
			type: 'POST',
			data: data,
			success: function(data, textStatus, XMLHttpRequest) {
				if (data.valid) {
					notify('Change Saved');
				} else {
					alert('An unexpected error occured, please try again');
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert('Error while contacting server, please try again: ' + errorThrown);
			}
		});
	}
</script>