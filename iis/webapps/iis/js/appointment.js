$(document).ready(function() {
	console.log("document ready");
	// We'll catch form submission to do it in AJAX, but this works also with JS disabled
	$('#appointment-form').submit(function(event) {
		// Stop full page load
		event.preventDefault();
		// Check fields
		var startDate = $('#appointment.start_date').val();
		var startTime = $('#appointment.start_time').val();
		var endDate = $('#appointment.end_date').val();
		var endTime = $('#appointment.end_time').val();
		if (!startDate || startDate.length == 0) {
			$('#login-block').removeBlockMessages().blockMessage('Please enter Start Date', {type: 'warning'});
		} else if (!startTime || startTime.length == 0) {
			$('#login-block').removeBlockMessages().blockMessage('Please enter Start Time', {type: 'warning'});
		} else if (!endDate || endDate.length == 0) {
			$('#login-block').removeBlockMessages().blockMessage('Please enter End Date', {type: 'warning'});
		} else if (!endTime || endTime.length == 0) {
			$('#login-block').removeBlockMessages().blockMessage('Please enter End Time', {type: 'warning'});
		} else {
			var submitBt = $(this).find('button[type=submit]');
			submitBt.disableBt();
			// Target url
			var target = $(this).attr('action');
			if (!target || target == '') {
				// Page url without hash
				target = document.location.href.match(/^([^#]+)/)[1];
			}
			// Request
			var data = {
					'appointment.id': $('#appointment.id').val(),
					'appointment.startDate': startDate,
					'appointment.startTime': startTime,
					'appointment.endDate': endDate,
					'appointment.endTtme': endTime,
			};
			sendTimer = new Date().getTime();
			if (redirect.length > 0) {
				data.redirect = redirect.val();
			}
			// Send
			$.ajax({
				url: target,
				dataType: 'json',
				type: 'POST',
				data: data,
				success: function(data, textStatus, XMLHttpRequest) {
					if (data.valid) {
						// Small timer to allow the 'checking login' message to show when server is too fast
						var receiveTimer = new Date().getTime();
						if (receiveTimer-sendTimer < 500) {
							setTimeout(function() {
								document.all.cal_supp.innerHTML = '';
							}, 500-(receiveTimer-sendTimer));
						} else {
							document.all.cal_supp.innerHTML = '';
						}
					} else {
						// Message
						$('#login-block').removeBlockMessages().blockMessage(data.error || 'An unexpected error occured, please try again', {type: 'error'});
						submitBt.enableBt();
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
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
