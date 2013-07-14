<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.common.model.*" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<% Nurse nurse = (Nurse)session.getAttribute("nurse");
User user = nurse.getUser();%>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<form class="block-content form" id="simple_form" method="post" action="nurse_new">
				<h1>Nurse Regitration</h1>

				<fieldset class="white-bg required">
					<legend>Nurse Reg</legend>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=nurse%>" prop="reg_date" label="Registration Date" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="referral_source" label="Referral Source" />
						</p>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="first_name" label="First Name" />
						</div>
						<div class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="last_name" label="Last Name" />
						</div>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="address" label="Home Address" />
							<input type="hidden" id="latitude" name="nurse.latitude">
							<input type="hidden" id="longitude" name="nurse.longitude">
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="email" label="Email Address" />
						</p>
					</div>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate" label="Pay Rate" />
						</p>
						<p class="colx3-center">
							<span class="label">Pay Rate < 2hr</span>
							SOC <input type="TEXT" name="nurse.pay_rate_2hr_soc" size="5" value="<%=nurse.getPayRate2HrSoc()%>"/> ROC <input type="TEXT" name="nurse.pay_rate_2hr_roc" size="5" value="<%=nurse.getPayRate2HrRoc()%>"/>
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="mileage_rate" label="Mileage Rate" />
						</p>
					</div>
					<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=user%>" prop="notes" label="Notes" />
				</fieldset>

				<fieldset>
					<legend>Referral Status</legend>
					<div class="columns">
						<div class="colx3-left">
							<p>
								<span class="label">Accepted?</span> <input type="radio"
									name="accepted[]" id="simple-checkbox-1" value="1">&nbsp;<label
									for="accepted-1">Yes</label> <input type="radio"
									name="accepted[]" id="simple-checkbox-2" value="2">&nbsp;<label
									for="accepted-2">No</label>
							</p>
						</div>
						<p class="colx3-right-double">
							<label for="explain">Explain:</label> <input type="<%=InputTag.Type.TEXT%>"
								name="explain" id="explain" value=""
								class="full-width">
						</p>
					</div>

					<p>
						<span class="label"></span> <input type="checkbox"
							name="simple-checkbox[]" id="simple-checkbox-1" value="1">&nbsp;<label
							for="simple-checkbox-1">Confirmed w/ Vendor</label> <input type="checkbox"
							name="simple-checkbox[]" id="simple-checkbox-2" value="2">&nbsp;<label
							for="simple-checkbox-2">Confirmed w/ Nurse</label> <input type="checkbox"
							name="simple-checkbox[]" id="simple-checkbox-2" value="2">&nbsp;<label
							for="simple-checkbox-2">Sent Patient Info</label> <input type="checkbox"
							name="simple-checkbox[]" id="simple-checkbox-2" value="2">&nbsp;<label
							for="simple-checkbox-2">Added to Calendar</label>
					</p>
					<div class="columns">
						<div class="colx2-left">
							<p>
								<span class="label"></span> <input type="checkbox"
									name="simple-checkbox[]" id="simple-checkbox-1" value="1">&nbsp;<label
									for="simple-checkbox-1">Patient Confirmed</label> <input type="checkbox"
									name="simple-checkbox[]" id="simple-checkbox-2" value="2">&nbsp;<label
									for="simple-checkbox-2">Meds Delivered?</label>
							</p>
						</div>
						<p class="colx2-right">
							<label for="anti-delivery-date">Anticipated Delivery Date:</label>
							<input type="<%=InputTag.Type.TEXT%>" name="anti-delivery-date" id="anti-delivery-date" value="" class="datepicker">
							<img src="images/icons/fugue/calendar-month.png" width="16" height="16">
						</p>
					</div>
				</fieldset>

				<fieldset class="grey-bg no-margin">
					<legend>Action on create</legend>
					<p class="input-with-button">
						<label for="simple-action">Select action</label> <select
							name="simple-action" id="simple-action">
							<option value="1">Save and publish</option>
							<option value="2">Save only</option>
						</select>
						<button type="submit">Create</button>
					</p>
				</fieldset>

			</form>
		</div>
	</section>
	<div class="clear"></div>
	<div class="clear"></div>
</article>
<script>
	google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('address'), function(place) {
		document.getElementById('latitude').value = place.geometry.location.lat();
		document.getElementById('longitude').value = place.geometry.location.lng();
	}));
</script>
