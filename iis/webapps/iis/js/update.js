function saveChange(data) {
	$.ajax({
		url: 'update',
		dataType: 'json',
		type: 'GET',
		data: data,
		success: function(data, textStatus, XMLHttpRequest) {
			if (data.valid) {
				notify('Change Saved');
				for (var prop in data.object) {
				  	updateValue(data.object, prop);
				}
			} else {
				alert(data.error || 'An unexpected error occurred, please try again');
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
		value: comp.type === 'checkbox' ? comp.checked : comp.value
	};
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

function updateLicense(comp, nurseId, licTypeId, attribute) {
	// Request
	var data = {
		classname: 'License',
		id: nurseId,
		lictypeid: licTypeId,
		attribute: attribute,
		value: comp.value
	};
	saveChange(data);
}

function updateValue(object, prop) {
	var id = prop + object.id;
	var element = document.getElementById(id);
	console.log('looking for : ' + id, element);
	if (element instanceof HTMLInputElement) {
		element.value = object[prop];
	} else if (element instanceof HTMLDivElement) {
		element.innerHTML = object[prop];
	}
}

function dateChanged(date, set) {
	console.log('date changed: ' + date);
}