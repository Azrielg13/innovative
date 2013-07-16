<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<% Patient patient = (Patient)session.getAttribute("patient"); %>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<form class="block-content form" id="simple_form" method="post" action="intake">
				<h1>Referral form</h1>

				<fieldset class="white-bg required">
					<legend>Referral</legend>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="referral_date" label="Referral Date" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_source_id" label="Referral Source" options="<%=GenData.VENDORS.get().getGeneralDatas()%>"/>
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="name" label="Name" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="mr_num" label="Medical Record #" />
						</p>
						<div class="colx3-right">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="dianosis_id" label="Dianosis" options="<%=GenData.DIANOSIS.get().getGeneralDatas()%>"/>
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
							<p>
								<span class="label">Patient Status</span> <input type="radio"
									name="patient.start_of_care" id="patient.start_of_care-1" value="true">&nbsp;<label
									for="patient.start_of_care-1">SOC</label> <input type="radio"
									name="patient.start_of_care" id="patient.start_of_care-2" value="false">&nbsp;<label
									for="patient.start_of_care-2">ROC</label>
							</p>
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="start_of_care_date" label="Start Date" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="est_last_day_of_service" label="Est. Last Day of Serice" />
						</p>
					</div>
					<div class="columns">
						<p class="colx2-left">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="billing_id" label="Billing" options="<%=GenData.VENDORS.get().getGeneralDatas()%>"/>
						</p>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="service_address" label="Service Address" />
							<input type="hidden" id="latitude" name="patient.latitude">
							<input type="hidden" id="longitude" name="patient.longitude">
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
	google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('service_address'), function(place) {
		document.getElementById('latitude').value = place.geometry.location.lat();
		document.getElementById('longitude').value = place.geometry.location.lng();
	}));
</script>
