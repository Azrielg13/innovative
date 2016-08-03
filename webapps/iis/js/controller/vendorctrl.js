com.digitald4.iis.VendorCtrl = function($routeParams, $filter, restService) {
	var vendorId = $routeParams.vendorId;
	this.vendorId = parseInt(vendorId, 10);
	this.TableType = {
		PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS,
			request: {query_param: [{column: 'billing_id', operan: '=', value: vendorId]},
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PATIENTS,
			request: {query_param: [{column: 'billing_vendor_id', operan: '=', value: vendorId},
			                        {column: 'assessment_complete', operan: '=', value: 'false'},
			                        {column: 'start', operan: '<', value: Date.now().toString()}]},
		UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.INVOICES,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'status_id', operan: '=', value: '1521'}]},
		PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.INVOICES,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'status_id', operan: '=', value: '1520'}]},
	};
	this.vendorService = new com.digitald4.iis.ProtoService('vendor', restService);
	this.selectedTab = this.TABS.general;
	this.refresh();
};

com.digitald4.iis.VendorCtrl.prototype.TABS = {
	calendar: 1,
	general: 2,
	patients: 3,
	pending: 4,
	billable: 5,
	invoices: 6,
	reports: 7	
};
com.digitald4.iis.VendorCtrl.prototype.vendorId;
com.digitald4.iis.VendorCtrl.prototype.vendorService;
com.digitald4.iis.VendorCtrl.prototype.vendor;
com.digitald4.iis.VendorCtrl.prototype.selectedTab;

com.digitald4.iis.VendorCtrl.prototype.refresh = function() {
	this.vendorService.get(this.vendorId, function(vendor) {
		this.vendor = vendor;
	}.bind(this), notify);
};

com.digitald4.iis.VendorCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
};

com.digitald4.iis.VendorCtrl.prototype.update = function(prop) {
	this.vendorService.update(this.vendor, prop, function(vendor) {
		this.vendor = vendor;
	}.bind(this), notify);
};

com.digitald4.iis.VendorCtrl.prototype.updateAddress = function(place) {
	this.vendor.address = place.formatted_address;
	this.update('address');
	this.vendor.latitude = place.geometry.location.lat();
	this.update('latitude');
	this.vendor.longitude = place.geometry.location.lng();
	this.update('longitude');
};