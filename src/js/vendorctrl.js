com.digitald4.iis.VendorCtrl = function($routeParams, $filter, vendorService, appointmentService, invoiceService) {
  this.filter = $filter;
	this.vendorId = parseInt($routeParams.id, 10);
	this.vendorService = vendorService;
	this.appointmentService = appointmentService;
	this.invoiceService = invoiceService;
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
			         'status_id': '1520'}}
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
                                'start_1': '<=' + endDate.valueOf()},
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
  if (tab == com.digitald4.iis.VendorCtrl.TABS.billable) {
    this.refreshBillables();
  }
	this.selectedTab = tab;
};

com.digitald4.iis.VendorCtrl.prototype.update = function(prop) {
	this.vendorService.update(this.vendor, [prop], function(vendor) {
		this.vendor = vendor;
	}.bind(this), notify);
};

com.digitald4.iis.VendorCtrl.prototype.refreshBillables = function() {
  var filter = {'state': '>=' + AppointmentState.AS_BILLABLE,
                'state_1': '<=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
                'vendor_id': this.vendorId};
  this.appointmentService.list(filter, function(billables) {
    this.billables = billables;
  }.bind(this), notify);
};

com.digitald4.iis.VendorCtrl.prototype.updateBillable = function(billable, prop) {
  var index = this.billables.indexOf(billable);
  this.appointmentService.update(billable, [prop], function(billable_) {
    billable_.selected = billable.selected;
    this.billables.splice(index, 1, billable_);
    this.updateInvoice();
  }.bind(this), notify);
};

com.digitald4.iis.VendorCtrl.prototype.updateInvoice = function() {
  this.invoice = this.invoice || {};
  this.invoice.vendor_id = this.vendorId;
  this.invoice.status_id = com.digitald4.iis.GenData.PAYMENT_STATUS_PAYMENT_STATUS_UNPAID;
  this.invoice.appointment_id = [];
  this.invoice.logged_hours = 0;
  this.invoice.standard_billing = 0;
  this.invoice.mileage = 0;
  this.invoice.billed_mileage = 0;
  this.invoice.total_due = 0;
  for (var i = 0; i < this.billables.length; i++) {
    var billable = this.billables[i];
    if (billable.selected) {
      var billingInfo =  billable.billing_info || {};
      this.invoice.appointment_id.push(billable.id);
      this.invoice.logged_hours += billable.logged_hours || 0;
      this.invoice.standard_billing += billingInfo.sub_total || 0;
      this.invoice.mileage += billingInfo.mileage || 0;
      this.invoice.billed_mileage += billingInfo.mileage_total || 0;
    }
  }
  this.invoice.total_due = this.invoice.standard_billing + this.invoice.billed_mileage;
};

com.digitald4.iis.VendorCtrl.prototype.createPaystub = function() {
  this.invoiceService.create(this.invoice, function(invoice) {
    this.refreshBillables();
    this.invoice = {};
  }.bind(this), notify);
};