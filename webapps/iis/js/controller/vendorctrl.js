com.digitald4.iis.VendorCtrl = function($routeParams, $filter, restService) {
  this.filter = $filter;
	var vendorId = $routeParams.id;
	this.vendorId = parseInt(vendorId, 10);
	this.TableType = {
		PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS,
			request: {query_param: [{column: 'billing_id', operan: '=', value: vendorId}]}},
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'state', operan: '=', value: AppointmentState.AS_PENDING_ASSESSMENT.toString()}]}},
		UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.UNPAID_INVOICES,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'status_id', operan: '=', value: '1521'}]}},
		PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES,
			request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
			                        {column: 'status_id', operan: '=', value: '1520'}]}},
		BILLABlE: {base: {title: 'Billable',
      entity: 'appointment',
      columns: [{title: 'Name', prop: 'name',
                    getUrl: function(appointment){return '#assessment/' + appointment.id;}},
                {title: 'Date', getValue: function(appointment) {
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
      request: {query_param: [{column: 'vendor_id', operan: '=', value: vendorId},
                              {column: 'state', operan: '>=', value: AppointmentState.AS_BILLABLE.toString()},
                              {column: 'state', operan: '<=', value: AppointmentState.AS_BILLABLE_AND_PAYABLE.toString()}]}},
	};
	this.vendorService = new com.digitald4.common.ProtoService('vendor', restService);
	this.appointmentService = new com.digitald4.common.ProtoService('appointment', restService);
	this.selectedTab = this.TABS.general;

  var eventClicked = function(event, jsEvent, view) {
		console.log('Click event: ' + event.title);
	}.bind(this);

  /* Render Tooltip */
  var eventRender = function(event, element, view) {
    element.attr({'tooltip': event.title,
                  'tooltip-append-to-body': true});
    // $compile(element)($scope);
  };

  var viewRender = function(view, element) {
    this.refreshAppointments(view.start, view.end);
  }.bind(this);

  this.uiConfig = {
    calendar:{
      height: 450,
      editable: false,
      header:{
        left: 'title',
        center: '',
        right: 'today prev,next'
      },
      eventClick: eventClicked,
      eventRender: eventRender,
      viewRender: viewRender
    }
  };
  this.eventSources = [this.events];
	this.refresh();
};

com.digitald4.iis.VendorCtrl.prototype.TABS = {
	calendar: 'Calendar',
	general: 'General',
	patients: 'Patients',
	pending: 'Pending Assessment',
	billable: 'Billable',
	invoices: 'Invoices',
	reports: 'Reports'
};
com.digitald4.iis.VendorCtrl.prototype.vendorId;
com.digitald4.iis.VendorCtrl.prototype.vendorService;
com.digitald4.iis.VendorCtrl.prototype.vendor;
com.digitald4.iis.VendorCtrl.prototype.selectedTab;
com.digitald4.iis.VendorCtrl.prototype.events = [];

com.digitald4.iis.VendorCtrl.prototype.refresh = function() {
	this.vendorService.get(this.vendorId, function(vendor) {
		this.vendor = vendor;
	}.bind(this), notify);
};

com.digitald4.iis.VendorCtrl.prototype.refreshAppointments = function(startDate, endDate) {
	this.appointmentService.list({query_param: [{column: 'vendor_id', operan: '=', value: this.vendorId.toString()},
                                              {column: 'start', operan: '>=', value: startDate.valueOf().toString()},
                                              {column: 'start', operan: '<=', value: endDate.valueOf().toString()}]},
      function(appointments) {
	  this.events.length = 0;
	  for (var a = 0; a < appointments.length; a++) {
	    var appointment = appointments[a];
	    this.events.push({id: appointment.id,
	        title: this.filter('date')(appointment.start, 'shortTime') + ' ' + appointment.patient_name,
          start: new Date(appointment.start),
          end: new Date(appointment.end),
          appointment: appointment,
          className: ['appointment']
      });
	  }
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