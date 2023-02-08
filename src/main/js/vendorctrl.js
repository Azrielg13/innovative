com.digitald4.iis.VendorCtrl = function($routeParams, $filter, vendorService, appointmentService, invoiceService) {
  this.filter = $filter;
	this.vendorId = parseInt($routeParams.id, 10);
	this.vendorService = vendorService;
	this.appointmentService = appointmentService;
	this.invoiceService = invoiceService;
	this.tabs = com.digitald4.iis.VendorCtrl.TABS;
	this.TableType = {
		PATIENTS: {
		  base: com.digitald4.iis.TableBaseMeta.PATIENTS, filter: 'billingVendorId=' + this.vendorId},
		PENDING_ASSESSMENT: {
		  base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: AppointmentState.PENDING_ASSESSMENT + ',vendorId=' + this.vendorId},
		UNPAID_INVOICES: {
		  base: com.digitald4.iis.TableBaseMeta.UNPAID_INVOICES,
		  filter: 'vendorId=' + this.vendorId + ',statusId=1521'},
		PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES,
			filter: 'vendorId=' + this.vendorId + ',statusId=1520'}
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
	}.bind(this), notifyError);
};

com.digitald4.iis.VendorCtrl.prototype.refreshAppointments = function(startDate, endDate) {
	this.appointmentService.list({'vendorId': this.vendorId,
                                'start': '>=' + startDate.valueOf(),
                                'start_1': '<=' + endDate.valueOf()},
      function(response) {
	  this.events.length = 0;
	  var appointments = response.items;
	  for (var a = 0; a < appointments.length; a++) {
	    var appointment = appointments[a];
	    this.events.push({id: appointment.id,
	        title: this.filter('date')(appointment.start, 'shortTime') + ' ' + appointment.patientName,
          start: new Date(appointment.start),
          end: new Date(appointment.end),
          appointment: appointment,
          className: ['appointment']
      });
	  }
	}.bind(this), notifyError);
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
	}.bind(this), notifyError);
};

com.digitald4.iis.VendorCtrl.prototype.refreshBillables = function() {
  var filter = {'state': '>=' + AppointmentState.AS_BILLABLE,
                'state_1': '<=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
                'vendorId': this.vendorId};
  this.appointmentService.list(filter, function(billables) {
    this.billables = billables;
  }.bind(this), notifyError);
};

com.digitald4.iis.VendorCtrl.prototype.updateBillable = function(billable, prop) {
  var index = this.billables.indexOf(billable);
  this.appointmentService.update(billable, [prop], function(billable_) {
    billable_.selected = billable.selected;
    this.billables.splice(index, 1, billable_);
    this.updateInvoice();
  }.bind(this), notifyError);
};

com.digitald4.iis.VendorCtrl.prototype.updateInvoice = function() {
  this.invoice = this.invoice || {};
  this.invoice.vendorId = this.vendorId;
  this.invoice.statusId = com.digitald4.iis.GenData.PAYMENT_STATUS_PAYMENT_STATUS_UNPAID;
  this.invoice.appointmentId = [];
  this.invoice.loggedHours = 0;
  this.invoice.standardBilling = 0;
  this.invoice.mileage = 0;
  this.invoice.billedMileage = 0;
  this.invoice.totalDue = 0;
  for (var i = 0; i < this.billables.length; i++) {
    var billable = this.billables[i];
    if (billable.selected) {
      var billingInfo =  billable.billingInfo || {};
      this.invoice.appointmentId.push(billable.id);
      this.invoice.loggedHours += billable.loggedHours || 0;
      this.invoice.standardBilling += billingInfo.subTotal || 0;
      this.invoice.mileage += billingInfo.mileage || 0;
      this.invoice.billedMileage += billingInfo.mileageTotal || 0;
    }
  }
  this.invoice.totalDue = this.invoice.standardBilling + this.invoice.billedMileage;
};

com.digitald4.iis.VendorCtrl.prototype.createPaystub = function() {
  this.invoiceService.create(this.invoice, function(invoice) {
    // Remove the billabes that were included in the invoice.
    for (var i = this.billables.length - 1; i >= 0; i--) {
      if (this.billables[i].selected) {
        this.billables.splice(i, 1);
      }
    }
    this.invoice = {};
  }.bind(this), notifyError);
};