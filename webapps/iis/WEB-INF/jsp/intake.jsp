<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%Patient patient = (Patient)session.getAttribute("patient");%>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<div class="error-block"></div>
			<form class="block-content form" id="intake-form" method="post" action="intake">
				<h1>Referral form</h1>
				<fieldset class="white-bg">
					<legend>Referral</legend>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="referral_date" label="Referral Date" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_source_id" label="Referral Source" required="true" options="<%=Vendor.getAllActive()%>"/>
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="name" label="Name" required="true" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="d_o_b" label="Date of Birth" />
						</p>
						<div class="colx3-right">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="dianosis_id" label="Diagnosis" options="<%=GenData.DIANOSIS.get().getGeneralDatas()%>"/>
						</div>
					</div>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="therapy_type_id" label="Therapy Type" options="<%=GenData.THERAPY_TYPE.get().getGeneralDatas()%>"/>
						</p>
						<div class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="rx" label="Pt Rx" />
						</div>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="i_v_access_id" label="IV Access" options="<%=GenData.IV_ACCESS.get().getGeneralDatas()%>"/>
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.RADIO%>" object="<%=patient%>" prop="patient_status_id" label="Patient Type" options="<%=GenData.PATIENT_STATUS.get().getGeneralDatas()%>"/>
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="start_of_care_date" label="Start Date" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="est_last_day_of_service" label="Est. Last Day of Service" />
						</p>
					</div>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="billing_id" label="Billing" required="true" options="<%=Vendor.getAllActive()%>"/>
						</p>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="phone_number" label="Primary Phone Number" size="14"/>
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="primary_phone_type_id" label="" size="-1" options="<%=GenData.PHONE_TYPE.get().getGeneralDatas()%>"/>
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="alt_contact_number" label="Alt Contact Number" size="14"/>
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="alt_phone_type_id" label="" size="-1" options="<%=GenData.PHONE_TYPE.get().getGeneralDatas()%>"/>
						</p>
					</div>
					<div class="columns">
						<p class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="service_address" label="Service Address" />
							<input type="hidden" id="latitude" name="patient.latitude" value="0">
							<input type="hidden" id="longitude" name="patient.longitude" value="0">
						</p>
						<div class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="service_addr_unit" label="Unit #" size="4"/>
						</div>
					</div>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="emergency_contact" label="Emergency Contact"/>
						</p>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="emergency_contact_phone" label="Emergency Contact Number" size="14"/>
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="emergency_contact_phone_type_id" label="" size="-1" options="<%=GenData.PHONE_TYPE.get().getGeneralDatas()%>"/>
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.ACK_TEXT%>" object="<%=patient%>" prop="labs_frequency" label="Labs? [Frequency]" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="first_recert_due" label="1st Re-certification" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="d_c_date" label="DC Date" />
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.CHECK%>" object="<%=patient%>" prop="info_in_s_o_s" label="PT Info in SOS?" />
						</div>
						<p class="colx3-right-double">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="scheduling_preference" label="Scheduling Preference" />
						</p>
					</div>
					<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=patient%>" prop="referral_note" label="Notes" />
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
				<div class="error-block"></div>
				<fieldset class="grey-bg no-margin">
					<legend>Action on create</legend>
					<p class="input-with-button">
						<label for="simple-action">Select action</label> <select
							name="simple-action" id="simple-action">
							<option value="1">Save and accept</option>
							<option value="2" selected>Save only</option>
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
	google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('serviceAddress'), function(place) {
		document.getElementById('latitude').value = place.geometry.location.lat();
		document.getElementById('longitude').value = place.geometry.location.lng();
	}));
</script>
<script>
	$(document).ready(function() {
		// We'll catch form submission to do it in AJAX, but this works also with JS disabled
		$('#intake-form').submit(function(event) {
			// Stop full page load
			event.preventDefault();
			var submitBt = $(this).find('button[type=submit]');
			// Check fields
			var name = $('#name').val();
			var referralSource = $('#referralSourceId').val();
			var billingId = $('#billingId').val();
			
			if (!name || name.length == 0) {
				showErrorMsg('Please enter patient name');
				return;
			}
			
			if (!referralSource || referralSource == 0) {
				showErrorMsg('Please select a referral source');
				return;
			}
			
			if (!billingId || billingId == 0) {
				showErrorMsg('Please select a billing source');
				return;
			}
			showErrorMsg(undefined);
			
			submitBt.disableBt();
			var elements = $(':input', '#intake-form');
			var data = {};
			for (var i = 0; i < elements.length; i++) {
				var element = elements[i];
				console.log(element);
				console.log(element.name + ": " + element.value);
				data[element.name] = element.value;
			}
			console.log(data);
			// Target url
			var target = $(this).attr('action');
			if (!target || target == '') {
				// Page url without hash
				target = document.location.href.match(/^([^#]+)/)[1];
			}
			
			// Send
			$.ajax({
				url: target,
				dataType: 'json',
				type: 'POST',
				data: data,
				success: function(data, textStatus, XMLHttpRequest) {
					if (data.valid) {
						if (!data.redirect) {
							data.redirect = 'pintake';
						}
						document.location.href = data.redirect;
					} else {
						// Message
						showErrorMsg(data.error || 'An unexpected error occured, please try again');
						submitBt.enableBt();
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					// Message
					showErrorMsg('Error while contacting server, please try again');
					submitBt.enableBt();
				}
			});
		});
	});
</script>
