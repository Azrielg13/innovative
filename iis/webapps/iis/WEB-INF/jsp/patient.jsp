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
		<dd4:midcal title="Patient Calendar" year="<%=year%>" month="<%=month%>" events="<%=patient.getAppointments()%>"/>
	</section>
	<section class="grid_4">
		<form class="block-content form" id="simple_form" method="post" action="patient">
			<input type="hidden" name="id" id="id" value="<%=patient.getId()%>"/>
			<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_resolution_id" label="Patient State" options="<%=GenData.PATIENT_STATE.get().getGeneralDatas()%>"/>
			<button type="submit">Save</button>
		</form>
	</section>
</article>

<!-- login script -->
	<script>
		function setMonth(year, month) {
			// Request
			var data = {
					action: "cal",
					id: $('#id').val(),
					year: year,
					month: month
				};
			var target = "http://192.168.1.19:8080/iis/patient";//document.location.href.match(/^([^#]+)/)[1];
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
					document.all.cal_sec.innerHTML = target;//'Error while contacting server, please try again';
				}
			});
		}
	</script>
	<script>
		$(document).ready(function()
		{
			// We'll catch form submission to do it in AJAX, but this works also with JS disabled
			$('#login-form').submit(function(event)
			{
				// Stop full page load
				event.preventDefault();
				
				// Check fields
				var login = $('#username').val();
				var pass = $('#pass').val();
				
				if (!login || login.length == 0)
				{
					$('#login-block').removeBlockMessages().blockMessage('Please enter your user name', {type: 'warning'});
				}
				else if (!pass || pass.length == 0)
				{
					$('#login-block').removeBlockMessages().blockMessage('Please enter your password', {type: 'warning'});
				}
				else
				{
					var submitBt = $(this).find('button[type=submit]');
					submitBt.disableBt();
					
					// Target url
					var target = $(this).attr('action');
					if (!target || target == '')
					{
						// Page url without hash
						target = document.location.href.match(/^([^#]+)/)[1];
					}
					
					// Request
					var data = {
							a: $('#a').val(),
							username: login,
							pass: pass,
							'keep-logged': $('#keep-logged').attr('checked') ? 1 : 0
						},
						redirect = $('#redirect'),
						sendTimer = new Date().getTime();
					
					if (redirect.length > 0)
					{
						data.redirect = redirect.val();
					}
					
					// Send
					$.ajax({
						url: target,
						dataType: 'json',
						type: 'POST',
						data: data,
						success: function(data, textStatus, XMLHttpRequest)
						{
							if (data.valid)
							{
								// Small timer to allow the 'checking login' message to show when server is too fast
								var receiveTimer = new Date().getTime();
								if (receiveTimer-sendTimer < 500)
								{
									setTimeout(function()
									{
										document.location.href = data.redirect;
										
									}, 500-(receiveTimer-sendTimer));
								}
								else
								{
									document.location.href = data.redirect;
								}
							}
							else
							{
								// Message
								$('#login-block').removeBlockMessages().blockMessage(data.error || 'An unexpected error occured, please try again', {type: 'error'});
								
								submitBt.enableBt();
							}
						},
						error: function(XMLHttpRequest, textStatus, errorThrown)
						{
							// Message
							$('#login-block').removeBlockMessages().blockMessage('Error while contacting server, please try again', {type: 'error'});
							
							submitBt.enableBt();
						}
					});
					
					// Message
					$('#login-block').removeBlockMessages().blockMessage('Please wait, checking login...', {type: 'loading'});
				}
			});
		});
	</script>