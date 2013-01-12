<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<form class="block-content form" id="simple_form" method="post" action="intake">
				<h1>Referral form</h1>

				<fieldset class="white-bg required">
					<legend>Referral</legend>
					<div class="columns">
						<div class="colx2-left">
							<label for="patient.referral_date">Referral Date:</label><input
							type="text" name="patient.referral_date" id="patient.referral_date" value=""
							class="datepicker"><img
							src="images/icons/fugue/calendar-month.png" width="16"
							height="16">
						</div>
						<p class="colx2-right">
							<label for="patient.referral_source">Referral Source:</label> <input
							type="text" name="patient.referral_source" id="patient.referral_source" value=""
							class="full-width">
						</p>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<label for="patient.name">Patient Name:</label>
							<input type="text" name="patient.name" id="patient.name" value="" class="full-width">
						</div>
						<p class="colx2-right">
							<label for="patient.mr_num">Medical Record #:</label> <input type="text"
							name="patient.mr_num" id="patient.mr_num" value=""
							class="full-width">
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<label for="patient.dianosis">Dianosis:</label> <input type="text"
								name="patient.dianosis" id="patient.dianosis" value=""
								class="full-width">
						</div>
						<p class="colx3-center">
							<label for="patient.therapy_type">Therapy Type:</label> <input
								type="text" name="patient.therapy_type" id="patient.therapy_type" value=""
								class="full-width">
						</p>
						<p class="colx3-right">
							<label for="patient.iv_access">IV Access:</label> <input type="text"
								name="patient.iv_access" id="patient.iv_access" value=""
								class="full-width">
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
							<label for="patient.start_of_care_date">Start Date:</label><input
								type="text" name="patient.start_of_care_date" id="patient.start_of_care_date" value=""
								class="datepicker"><img
								src="images/icons/fugue/calendar-month.png" width="16" height="16">
						</p>
						<p class="colx3-right">
							<label for="patient.service_address">Service Address:</label> <input
								type="text" name="patient.service_address" id="patient.service_address" value=""
								class="full-width">
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<label for="patient.rx">PT Rx:</label> <input type="text"
								name="patient.rx" id="patient.rx" value=""
								class="full-width">
						</div>
						<p class="colx3-center">
							<label for="patient.billing">Billing:</label> <input type="text"
								name="patient.billing" id="patient.billing" value=""
								class="full-width">
						</p>
						<p class="colx3-right">
							<label for="patient.est_last_day_of_service">Est. Last Day of Service</label><input
								type="text" name="patient.est_last_day_of_service" id="patient.est_last_day_of_service" value=""
								class="datepicker"><img
								src="images/icons/fugue/calendar-month.png" width="16"
								height="16">
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<label for="patient.labs">Labs:</label>
							 <input type="checkbox" name="patient.labs" id="patient.labs"
								value="true" class="switch">
							
						</div>
						<p class="colx3-center">
							<label for="patient.labs_frequency">Frequency:</label> <input type="text"
								name="patient.labs_frequency" id="patient.labs_frequency" value=""
								class="full-width">
						</p>
						<p class="colx3-right">
							<label for="patient.first_recert_due">1st Re-certification:</label><input
								type="text" name="patient.first_recert_due" id="patient.first_recert_due" value=""
								class="datepicker"><img
								src="images/icons/fugue/calendar-month.png" width="16"
								height="16">
						</p>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<label for="patient.info_in_s_o_s">PT Info in SOS?</label>
							 <input type="checkbox" name="patient.info_in_s_o_s" id="patient.info_in_s_o_s"
								value="true" class="switch">
						</div>
						<p class="colx3-center">
							<label for="patient.d_c_date">DC Date:</label><input
								type="text" name="patient.d_c_date" id="patient.d_c_date" value=""
								class="datepicker"><img
								src="images/icons/fugue/calendar-month.png" width="16"
								height="16">
						</p>
						<p class="colx3-right">
							<label for="patient.scheduling_preference">Scheduling Preference:</label> <input type="text"
								name="patient.scheduling_preference" id="patient.scheduling_preference" value=""
								class="full-width">
						</p>
					</div>
					<label for="patient.referral_note">Notes:</label>
					<textarea name="patient.referral_note" id="patient.referral_note" rows=10 class="full-width"></textarea>
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
							<label for="explain">Explain:</label> <input type="text"
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
								type="text" name="anti-delivery-date" id="anti-delivery-date" value=""
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