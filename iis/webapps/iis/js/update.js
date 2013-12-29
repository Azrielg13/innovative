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

function payableCallback(object) {
  console.log(object);
  for (var prop in object) {
  	console.log(prop, object[prop]);
  	updateValue(object, prop);
  }
  /* updateValue('totalPayment' + object.id, '$' + Math.round(object.totalPayment*100)/100.0);
  updateValue('PAY_RATE' + object.id, object.payRate);
  updateValue('MILEAGE' + object.id, Math.round(object.mileage));
  updateValue('billingTotal' + object.id, '$' + Math.round(object.billingTotal*100)/100.0);
  updateValue('BILLING_RATE' + object.id, object.billingRate); */
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