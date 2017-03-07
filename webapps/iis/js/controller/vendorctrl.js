com.digitald4.iis.VendorCtrl = function($routeParams, $filter, vendorService, appointmentService) {
  this.filter = $filter;
	this.vendorId = parseInt($routeParams.id, 10);
	this.vendorService = vendorService;
	this.appointmentService = appointmentService;
	this.tabs = com.digitald4.iis.VendorCtrl.TABS;
	this.TableType = {
		PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS,
			filter: {'billing_id': this.vendorId}},
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: {'state': AppointmentState.AS_PENDING_ASSESSMENT,
			         'vendor_id': this.vendorId}},
		UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.UNPAID_INVOICES,
			filter: {'vendor_id': this.vendorId,
			         'status_id': '1521'}},
		PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES,
			filter: {'vendor_id': this.vendorId,
			         'status_id': '1520'}},
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
      filter: {'state': '>=' + AppointmentState.AS_BILLABLE,
               'state': '<=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
               'vendor_id': this.vendorId}}
	};

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
	this.setSelectedTab(this.tabs[$routeParams.tab] || this.tabs.general);
};

com.digitald4.iis.VendorCtrl.TABS = {
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
	this.appointmentService.list({'vendor_id': this.vendorId,
                                'start': '>=' + startDate.valueOf(),
                                'start': '<=' + endDate.valueOf()},
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
	this.vendorService.update(this.vendor, [prop], function(vendor) {
		this.vendor = vendor;
	}.bind(this), notify);
};