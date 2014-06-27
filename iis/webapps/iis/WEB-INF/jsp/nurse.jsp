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
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDjNloCm6mOYV0Uk1ilOTAclLbgebGCBQ0&v=3.exp&sensor=false&libraries=places"></script>
<article class="container_12">
	<section class="grid_10">
		<div id="tab-global" class="tabs-content">
			<ul class="tabs js-tabs same-height">
				<li class="current"><a href="#tab-calendar" title="Calendar">Calendar</a>
				<li><a href="#tab-general" title="General">General</a></li>
				<li><a href="#tab-license" title="Licenses">Licenses</a></li>
				<li><a href="#tab-pending" title="Pending Assessment">Pending Assessment</a></li>
				<li><a href="#tab-reviewable" title="Awaiting Review">Awaiting Review</a></li>
				<li><a href="#tab-payable" title="Payable">Payable</a></li>
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
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate" label="Pay Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2hr_soc" label="< 2hr SOC Pay Rate" async="true"/>
						<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=nurse%>" prop="pay_rate_2hr_roc" label="< 2hr ROC Pay Rate" async="true"/>
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
				<div id="tab-pending">
					<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"pendcols\")%>" data="<%=nurse.getPendAsses()%>"/>
				</div>
				<div id="tab-reviewable">
					<dd4:table title="Awaiting Review" columns="<%=(Collection<Column>)request.getAttribute(\"reviewable_cols\")%>" data="<%=nurse.getReviewables()%>"/>
				</div>
				<div id="tab-payable">
					<form id="simple_form" method="get" action="create_paystub">
						<input type="hidden" name="nurse_id" value="<%=nurse.getId()%>" />
						<dd4:table title="Payable" columns="<%=(Collection<Column>)request.getAttribute(\"paycols\")%>" data="<%=nurse.getPayables()%>" callbackCode="payableCallback(object);"/>
						<dd4:input type="<%=InputTag.Type.DATE%>" object="<%=(Paystub)request.getAttribute(\"paystub\")%>" prop="pay_date" label="Pay Date" />
						<button type="submit">Create Paystub</button>
					</form>
				</div>
			</div>
		</div>
	</section>
</article>
<script>
	google.maps.event.addDomListener(window, 'load', addMapAutoComplete(document.getElementById('address'), function(place) {
		saveAddress(place, '<%=nurse.getClass().getName()%>', <%=nurse.getId()%>);
	}));
	
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
