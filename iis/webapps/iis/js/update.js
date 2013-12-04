function saveChange(data, successCallback) {
	$.ajax({
		url: 'update',
		dataType: 'json',
		type: 'GET',
		data: data,
		success: function(data, textStatus, XMLHttpRequest) {
			if (data.valid) {
				notify('Change Saved');
				successCallback(data.object);
			} else {
				alert(data.error || 'An unexpected error occurred, please try again');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert('Error while contacting server, please try again');
		}
	});
}

function asyncUpdate(comp, className, id, attribute, successCallback) {
	// Request
	var data = {
		classname: className,
		id: id,
		attribute: attribute,
		value: comp.value
	};
	saveChange(data, successCallback);
}

function asyncCheckbox(comp, className, id, attribute, successCallback) {
	// Request
	var data = {
		classname: className,
		id: id,
		attribute: attribute,
		value: comp.checked
	};
	saveChange(data, successCallback);
}

function saveAddress(place, className, id, successCallback)	{
	var data = {
		id: id,
		classname: className,
		attribute: 'address',
		address: place.formatted_address,
		latitude: place.geometry.location.lat(),
		longitude: place.geometry.location.lng()
	};
	saveChange(data, successCallback);
}