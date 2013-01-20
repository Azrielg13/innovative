<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<% Patient patient = (Patient)session.getAttribute("patient"); %>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<form class="block-content form" id="simple_form" method="post" action="intake">
				<h1>Referral form</h1>

				<fieldset class="white-bg required">
					<legend>Referral</legend>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="referral_date" label="Referral Date:" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="referral_source" label="Referral Source:" />
						</p>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="name" label="Name:" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="mr_num" label="Medical Record #:" />
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="dianosis" label="Dianosis:" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="therapy_type" label="Therapy Type:" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="iv_access" label="IV Access:" />
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
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="start_of_care_date" label="Start Date:" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="service_address" label="Service Address:" />
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="rx" label="Pt Rx:" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="billing" label="Billing:" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="est_last_day_of_service" label="Est. Last Day of Serice:" />
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="checkbox" object="<%=patient%>" prop="labs" label="Labs:" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="labs_frequency" label="Frequency:" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="first_recert_due" label="1st Re-certification:" />
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="checkbox" object="<%=patient%>" prop="info_in_s_o_s" label="PT Info in SOS?" />
						</div>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=patient%>" prop="d_c_date" label="DC Date:" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=patient%>" prop="scheduling_preference" label="Scheduling Preference:" />
						</p>
					</div>
					<label for="patient.referral_note">Notes:</label>
					<textarea name="patient.referral_note" id="patient.referral_note" rows=10 class="full-width"><%=patient.getReferralNote()%></textarea>
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
							<label for="anti-delivery-date">Anticipated Delivery Date:</label><input
								type="<%=InputTag.Type.TEXT%>" name="anti-delivery-date" id="anti-delivery-date" value=""
								class="datepicker"><img
								src="images/icons/fugue/calendar-month.png" width="16"
								height="16">
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