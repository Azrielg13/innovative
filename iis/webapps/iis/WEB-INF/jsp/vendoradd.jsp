<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.common.model.*" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<% Vendor vendor = (Vendor)request.getAttribute("vendor");
%>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<form class="block-content form" id="simple_form" method="post" action="vendoradd">
				<h1>Add Vendor</h1>
				<fieldset class="white-bg required">
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="name" label="Vendor Name" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="address" label="Address" />
							<input type="hidden" id="latitude" name="vendor.latitude">
							<input type="hidden" id="longitude" name="vendor.longitude">
						</p>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="phone_number" label="Office Number" />
						</div>
						<p class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="fax_number" label="Fax Number" />
						</p>
					</div>
					<div class="columns">
						<div class="colx2-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="contact_name" label="Contact Name" />
						</div>
						<div class="colx2-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="contact_number" label="Contact Number" />
						</div>
					</div>
					<h2>Billing Hourly Rates</h2>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_rate" label="Standard" />
						</p>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_rate_2hr_soc" label="< 2hr SOC" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_rate_2hr_roc" label="< 2hr ROC" />
						</p>
					</div>
					<h2>Billing Flat Rates</h2>
					<div class="columns">
						<p class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_flat" label="Standard" />
						</p>
						<p class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_flat_2hr_soc" label="< 2hr SOC" />
						</p>
						<p class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=vendor%>" prop="billing_flat_2hr_roc" label="< 2hr ROC" />
						</p>
					</div>
					<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=vendor%>" prop="notes" label="Notes" />
				</fieldset>

				<fieldset class="grey-bg no-margin">
					<button style="float:right" type="submit">Submit</button>
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
