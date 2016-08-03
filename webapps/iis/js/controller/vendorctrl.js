com.digitald4.iis.VendorCtrl = function($routeParams, $filter, restService) {
	var vendorId = $routeParams.vendorId;
	this.vendorId = parseInt(vendorId, 10);
	this.TableType = {
		PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS,
			request: {query_param: [{column: 'billing_id', operan: '=', value: vendorId}]}},
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			request: {query_param: [{column: 'billing_vendor_id', operan: '=', value: vendorId},
			                        {column: 'assessment_complete', operan: '=', value: 'false'},
			                        {column: 'start', operan: '<', value: Date.now().toString()}]}},
		UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.INVOICES,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'status_id', operan: '=', value: '1521'}]}},
		PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.INVOICES,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'status_id', operan: '=', value: '1520'}]}},
		BILLABlE: {base: {title: 'Billable',
				url: 'appointments',
				link: {url: '#appointment', title: 'Patient'},
				columns: [{title: 'Date', getValue: function(appointment) {
										return $filter('date')(appointment.start, 'MM/dd/yyyy HH:mm');}},
				          {title: 'Billing Type', prop: 'billing_type_id'},
				          {title: 'Billed Hours', prop: 'billed_hours'},
				          {title: 'Hourly Rate', prop: 'billing_rate'},
				          {title: 'Per Visit Cost', prop: 'billing_flat'},
				          {title: 'Billed Mileage', prop: 'billing_mileage'},
				          {title: 'Mileage Rate', prop: 'billing_mileage_rate'},
				          {title: 'Total Payment', getValue: function(appointment) {
				          	return $filter('currency')(appointment.billing_flat +
				          			appointment.billed_hours * appointment.billing_rate +
				          			appointment.billing_mileage * appointment.billing_mileage_rate);
				          }}]},
      request: {query_param: [{column: 'billing_vendor_id', operan: '=', value: vendorId},
			                        {column: 'assessment_complete', operan: '=', value: 'true'},
			                        {column: 'invoice_id', operan: ' IS ', value: 'null'}]}},
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