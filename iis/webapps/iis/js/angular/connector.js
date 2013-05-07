com.digitald4.common.connector = {};

com.digitald4.common.connector.JQuery = function(url, dataType, requestType) {
	this.url = url;
	this.dataType = dataType;
	this.requestType = requestType;
	
	this.performRequest = function(request, successCallback, errorCallback) {
		// Send
		$.ajax({
			url: this.url,
			dataType: this.dataType,
			type: this.requestType,
			data: request,
			success: function(response, textStatus, XMLHttpRequest) {
				successCallback(response, textStatus, XMLHttpRequest);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				errorCallback(XMLHttpRequest, textStatus, errorThrown);
			}
		});
	};
};

com.digitald4.common.connector.NurseStream = function() {
	this.connector = new com.digitald4.common.connector.JQuery('nurseService', 'json', 'GET');

	this.get = function(id, callBack) {
		var request = {
			action: 'get',
			id: id
		};
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid) {
				callBack(response.data);
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
			callBack(undefined);
		});
	};
	
	this.getLicenses = function(id, callBack) {
		var request = {
			action: 'getLicenses',
			id: id
		};
		
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid && angular.isArray(response.data)) {
				callBack(response.data);
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
			callBack([]);
		});
	};
	
	this.updateObject = function(object, callBack, errorCall) {
		var request = {
			action: 'update',
			object: object
		};
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid) {
				callBack();
			} else {
				errorCall(response.error);
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
			errorCall(errorThrown);
		});
	};
};

com.digitald4.common.connector.JSONStream = function(url) {
	this.connector = new com.digitald4.common.connector.JQuery(url, 'json', 'GET');

	this.get = function(className, id) {
		var request = {
			action: 'get',
			className: className,
			id: id
		};
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid) {
				console.log(response.data);
				return response.data;
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
		});
		return null;
	};
	this.getAll = function(className) {
		var request = {
			action: 'getAll',
			className: className
		};
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid && angular.isArray(response.data)) {
				return response.data;
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
		});
		return [];
	};
	this.getCollection = function(className, queryName, values) {
		var request = {
			action: 'getCollection',
			className: className,
			queryName: queryName,
			values: values
		};
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid && angular.isArray(response.data)) {
				return response.data;
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
		});
		return [];
	};
};

com.digitald4.common.connector.ObjectStream = function(url) {
	this.connector = new com.digitald4.common.connector.JQuery(url, 'json', 'GET');

	this.get = function(T, id) {
		var request = {
			action: 'get',
			className: T,
			id: id
		};
		var t;
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid) {
				console.log(response.data);
				t = new T(response.data);
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
		});
		return t;
	};
	this.getAll = function(T) {
		var request = {
			action: 'getAll',
			className: T
		};
		var items = [];
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid && angular.isArray(response.data)) {
				for (var idx = 0; idx < response.data.length; idx++) {
					items.push(new T(response.data[idx]));
				}
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
		});
		return items;
	};
	this.getCollection = function(T, query) {
		var request = {
			action: 'getCollection',
			className: T,
			query: query
		};
		var items = [];
		this.connector.performRequest(request, function(response, textStatus, XMLHttpRequest) {
			if (response.valid && angular.isArray(response.data)) {
				for (var idx = 0; idx < response.data.length; idx++) {
					items.push(new T(response.data[idx]));
				}
			}
		}, function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ' ' + textStatus + ' ' + errorThrown);
		});
		return items;
	};
};