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
	<section class="grid_4" id="cal_sec">
		<dd4:medcal title="Patient Calendar" year="<%=year%>" month="<%=month%>" events="<%=patient.getAppointments()%>"/>
	</section>
	<section class="grid_4">
		<form class="block-content form" id="simple_form" method="post" action="patient">
			<input type="hidden" name="id" id="id" value="<%=patient.getId()%>"/>
			<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_resolution_id" label="Patient State" options="<%=GenData.PATIENT_STATE.get().getGeneralDatas()%>"/>
			<button type="submit">Save</button>
		</form>
	</section>
</article>

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
</script>