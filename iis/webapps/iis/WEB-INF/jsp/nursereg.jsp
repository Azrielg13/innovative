<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<% Nurse nurse = (Nurse)session.getAttribute("nurse"); %>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<form class="block-content form" id="simple_form" method="post" action="intake">
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
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="first_name" label="First Name" />
						</div>
						<div class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="last_name" label="Last Name" />
						</div>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="address" label="Home Address" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="email" label="Email Address" />
						</p>
					</div>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate" label="Pay Rate" />
						</p>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2_hr_or_less" label="< 2hr Pay Rate" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="mileage_rate" label="Mileage Rate" />
						</p>
					</div>
					<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=nurse%>" prop="notes" label="Notes" />
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