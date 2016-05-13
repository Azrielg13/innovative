<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="com.digitald4.common.component.Column"%>
<%@ page import="com.digitald4.common.model.GeneralData" %>
<%@ page import="com.digitald4.common.model.User" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.util.*" %>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.List"%>

<%Nurse nurse = (Nurse)request.getAttribute("nurse");
User user = nurse.getUser();%>
<script src="js/large-cal.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<article class="container_12">
	<section class="grid_10">
		<div id="tab-global" class="tabs-content">
			<ul class="tabs js-tabs same-height">
				<li class="current"><a href="#tab-calendar" title="Calendar">Calendar</a>
				<li><a href="#tab-general" title="General">General</a></li>
				<li><a href="#tab-license" title="Licenses">Licenses</a></li>
				<li><a href="#tab-unconfirmed" title="Unconfirmed Appointments">Unconfirmed</a></li>
				<li><a href="#tab-pending" title="Pending Assessment">Pending Assessment</a></li>
				<li><a href="#tab-reviewable" title="Awaiting Review">Awaiting Review</a></li>
				<li><a href="#tab-payable" title="Payable">Payable</a></li>
				<li><a href="#tab-payhist" title="Pay History">Pay History</a>
				<li><span>Advanced</span></li>
			</ul>
			<div class="tabs-content">
				<div id="tab-calendar">
					<div id="cal_sec">
						<%=request.getAttribute("calendar")%>
					</div>
				</div>
				<div id="tab-general">
					<div class="block-content form">
						<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=nurse%>" prop="status_id" label="Status" options="<%=GenData.NURSE_STATUS.get().getGeneralDatas()%>" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="first_name" label="First Name" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="last_name" label="Last Name" async="true"/>
						<label for="address">Address</label>
						<input type="text" id="address" name="address" value="<%=nurse.getAddress()%>" class="full-width" />
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="addr_unit" label="Unit #" size="4" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="phone_number" label="Phone Number" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="email" label="Email Address" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="user_name" label="Username" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_flat" label="Per Visit Pay" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate" label="Pay Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_flat_2hr_soc" label="< 2hr SOC Visit Pay" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2hr_soc" label="< 2hr SOC Pay Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_flat_2hr_roc" label="< 2hr FU Visit Pay" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2hr_roc" label="< 2hr FU Pay Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="mileage_rate" label="Mileage Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=user%>" prop="notes" label="Notes" async="true"/>
					</div>
				</div>
				<div id="tab-license">
					<div class="block-content form">
						<%for (Pair<GeneralData, List<License>> licCat : nurse.getAllLicenses()) {%>
							<h2><%=licCat.getLeft()%></h2>
							<%for (License license : licCat.getRight()) {%>
								<div class="columns">
									<div class="colx2-left">
										<div class="columns">
											<div class="colx2-left">
												<label><%=license%></label>
												<%Object value = license.getNumber();
												if (value == null) {
													value = "";
												}%>
												<input type="text" id="number<%=license.getLicTypeId()%>" value="<%=value%>" onchange="updateLicense(this, '<%=nurse.getId()%>', '<%=license.getLicTypeId()%>', 'number')" />
											</div>
											<p class="colx2-right">
												<label>Valid Date</label>
												<%value = FormatText.formatDate(license.getValidDate());
												if (value == null) {
													value = "";
												}%>
												<input type="text" id="validDate<%=license.getLicTypeId()%>" value="<%=value%>" class="datepicker" onSelect="dateChanged" onchange="updateLicense(this, '<%=nurse.getId()%>', '<%=license.getLicTypeId()%>', 'valid_date')" />
												<img src="images/icons/fugue/calendar-month.png" width="16" height="16">
											</p>
										</div>
									</div>
									<div class="colx2-right">
										<div class="columns">
											<%if (license.showExp()) {%>
												<div class="colx2-left">
													<label>Exp Date</label>
													<%value = FormatText.formatDate(license.getExpirationDate());
													if (value == null) {
														value = "";
													}%>
													<input type="text" id="expirationDate<%=license.getLicTypeId()%>" value="<%=value%>" class="datepicker" onchange="updateLicense(this, '<%=nurse.getId()%>', '<%=license.getLicTypeId()%>', 'expiration_date')" />
													<img src="images/icons/fugue/calendar-month.png" width="16" height="16">
												</div>
											<%}%>
											<div class="colx2-right">
												<div id="dataFileHTML<%=license.getLicTypeId()%>">
													<%=license.getDataFileHTML()%>
												</div>
											</div>
										</div>
									</div>
								</div>
							<%}%>
						<%}%>
					</div>
				</div>
				<div id="tab-unconfirmed">
					<dd4:table title="Unconfirmed Appointments" columns="<%=(Collection<Column>)request.getAttribute(\"unconfirmed_cols\")%>" data="<%=nurse.getUnconfirmed()%>"/>
				</div>
				<div id="tab-pending">
					<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"pendcols\")%>" data="<%=nurse.getPendAsses()%>"/>
				</div>
				<div id="tab-reviewable">
					<dd4:table title="Awaiting Review" columns="<%=(Collection<Column>)request.getAttribute(\"reviewable_cols\")%>" data="<%=nurse.getReviewables()%>"/>
				</div>
				<div id="tab-payable">
					<form id="paystub_form" method="post" action="create_paystub" target="_blank">
						<div>
							<input type="hidden" name="nurse_id" value="<%=nurse.getId()%>" />
							<dd4:table title="Payable" columns="<%=(Collection<Column>)request.getAttribute(\"paycols\")%>" data="<%=nurse.getPayables()%>" callbackCode="payableCallback(object);"/>
							<section class="grid_8">
							<div class="block-border">
								<div class="error-block"></div>
								<fieldset class="white-bg block-content form">
									<div class="columns">
										<div class="colx3-left">
											<label>Total Hours</label>
											<div id="paystubHours"></div>
										</div>
										<div class="colx3-center">
											<label>Total Mileage</label>
											<div id="paystubMiles"></div>
										</div>
										<div class="colx3-right">
											<label>Total Payment</label>
											<div id="paystubPayment"></div>
										</div>
									</div>
									<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=(Paystub)request.getAttribute(\"paystub\")%>" prop="pay_date" label="Pay Date" />
									<button type="submit">Create Paystub</button>
								</fieldset>
							</div>
							</section>
						</div>
					</form>
				</div>
				<div id="tab-payhist">
					<dd4:table title="Pay History" columns="<%=(Collection<Column>)request.getAttribute(\"payhistcols\")%>" data="<%=nurse.getPaystubs()%>"/>
				</div>
			</div>
		</div>
	</section>
</article>
<script>
	google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('address'), function(place) {
		saveAddress(place, '<%=nurse.getClass().getName()%>', <%=nurse.getId()%>);
	}));
</script>
<script>
	function updatePaystubTotals() {
		var hours = 0;
		var miles = 0;
		var payment = 0;
		var elements = $('input[type=checkbox]:checked', '#paystub_form');
		for (var idx = 0; idx < elements.length; idx++) {
			var id = elements[idx].value;
			hours += parseFloat($('#payHours' + id, '#paystub_form').val(), 10);
			miles += parseFloat($('#payMileage' + id, '#paystub_form').val(), 10);
			console.log($('#paymentTotal' + id, '#paystub_form').text().substring(1).replace(/[^0-9]/, ''));
			payment += parseFloat($('#paymentTotal' + id, '#paystub_form').text().substring(1).replace(/[^0-9]/, ''), 10);
		}
		document.getElementById('paystubHours').innerHTML = Math.round(hours * 100) / 100.0;
		document.getElementById('paystubMiles').innerHTML = Math.round(miles * 100) / 100.0;
		document.getElementById('paystubPayment').innerHTML = '$' + Math.round(payment * 100) / 100.0;
	}
	
	$('input', '#paystub_form').on("change", function() {
		updatePaystubTotals();
	});
	
	/* $('#paystub_form').submit(function(event) {
		// Stop full page load
		event.preventDefault();
		var submitBt = $(this).find('button[type=submit]');
		// Check fields
		var payDate = $('#payDate').val();
		
		if (!payDate || payDate.length == 0) {
			showErrorMsg('Pay date is required');
			return;
		}
		
		var appIds = [];
		var elements = $('input[type=checkbox]:checked', '#paystub_form');
		for (var idx = 0; idx < elements.length; idx++) {
			appIds.push(elements[idx].value);
		}
		if (appIds.length == 0) {
			showErrorMsg('You must select at least 1 appointment');
			return;
		}
		showErrorMsg(undefined);
		submitBt.disableBt();
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
			type: 'GET',
			data: {
				'nurse_id': <%=nurse.getId()%>,
				'Paystub.pay_date': payDate,
				'selected[]': appIds
			},
			success: function(data, textStatus, XMLHttpRequest) {
				if (data.valid) {
					var elements = $('input[type=checkbox]:checked', '#paystub_form');
					var table = $($('table', '#paystub_form')[0]).DataTable();
					for (var idx = 0; idx < elements.length; idx++) {
						table.row($(elements[idx]).parents('tr')).remove();
					}
					table.draw();
					updatePaystubTotals();
				} else {
					// Message
					showErrorMsg(data.error || 'An unexpected error occured, please try again');
				}
				submitBt.enableBt();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				// Message
				showErrorMsg('Error while contacting server, please try again');
				submitBt.enableBt();
			}
		});
	}); */
	
	var licTypeId_;
	
	function showLicUploadDialog(className, id, licTypeId) {
		console.log('licTypeId: ' + licTypeId);
		licTypeId_ = licTypeId; 
		showUploadDialog(className, id, uploadLicFile);
	}
	
	function uploadLicFile(className, id) {
		console.log('licTypeId_: ' + licTypeId_);
	    var file = document.getElementById('file');
	    console.log("file: " + file);
	    var url = 'upload';
	    var xhr = new XMLHttpRequest();
	    xhr.addEventListener('progress', function(e) {
	        var done = e.position || e.loaded, total = e.totalSize || e.total;
	        console.log('xhr progress: ' + (Math.floor(done/total*1000)/10) + '%');
	    }, false);
	    if ( xhr.upload ) {
	        xhr.upload.onprogress = function(e) {
	            var done = e.position || e.loaded, total = e.totalSize || e.total;
	            console.log('xhr.upload progress: ' + done + ' / ' + total + ' = ' + (Math.floor(done/total*1000)/10) + '%');
	        };
	    }
	    xhr.onreadystatechange = function(e) {
	        if (this.readyState == 4) {
	            console.log(['xhr upload complete', e]);
	            var element = document.getElementById('dataFileHTML' + licTypeId_);
				element.innerHTML = '<%=License.DOWNLOAD_LINK.replaceAll("'", "\\\\'").replaceAll("__className__", "' + className + '").replaceAll("__id__", "' + id + '")%>';
	        }
	    };
	    xhr.open('post', url, true);
	    var fd = new FormData;
	    fd.append('classname', className);
	    fd.append('id', id);
	    fd.append('file', file.files[0]);
	    xhr.send(fd);
	}
</script>
