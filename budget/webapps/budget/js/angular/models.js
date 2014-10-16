com = {};
com.digitald4 = {};
com.digitald4.common = {};
com.digitald4.common.model = {};
com.digitald4.iis = {};
com.digitald4.iis.model = {};

com.digitald4.iis.model.License = function(json) {
	this.name = json.name;
	this.number = json.number;
	this.validDate = json.validDate;
	this.expirationDate = json.expirationDate;
};