function asyncUpdate(comp, className, id, attribute) {
	// Request
	var data = {
		classname: className,
		id: id,
		attribute: attribute,
		value: comp.value
	};
	var target = "update";
	// Send
	$.ajax({
		url: target,
		dataType: 'json',
		type: 'GET',
		data: data,
		success: function(data, textStatus, XMLHttpRequest) {
			if (data.valid) {
				notify('Change Saved');
			} else {
				alert(data.error || 'An unexpected error occured, please try again');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert('Error while contacting server, please try again');
		}
	});
}

function saveAddress(place, className, id)	{
	var data = {
		id: id,
		classname: className,
		attribute: 'address',
		address: place.formatted_address,
		latitude: place.geometry.location.lat(),
		longitude: place.geometry.location.lng()
	};
	var target = "update";//document.location.href.match(/^([^#]+)/)[1];
	// Send
	$.ajax({
		url: target,
		dataType: 'json',
		type: 'POST',
		data: data,
		success: function(data, textStatus, XMLHttpRequest) {
			if (data.valid) {
				notify('Change Saved');
			} else {
				alert('An unexpected error occured, please try again');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert('Error while contacting server, please try again: ' + errorThrown);
		}
	});
}