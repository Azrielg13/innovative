function saveChange(data) {
	$.ajax({
		url: 'update',
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

function asyncUpdate(comp, className, id, attribute) {
	// Request
	var data = {
		classname: className,
		id: id,
		attribute: attribute,
		value: comp.value
	};
	//console.log(comp.checked);
	saveChange(data);
}

function asyncCheckbox(comp, className, id, attribute) {
	// Request
	var data = {
		classname: className,
		id: id,
		attribute: attribute,
		value: comp.checked
	};
	//console.log(comp.checked);
	saveChange(data);
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
	saveChange(data);
}