com.digitald4.iis.VendorCtrl = function($routeParams, $filter,
    appointmentService, flags, invoiceService, vendorService) {
  this.filter = $filter;
	this.vendorId = parseInt($routeParams.id, 10);
	this.appointmentService = appointmentService;
	this.flags = flags;
	this.invoiceService = invoiceService;
	this.vendorService = vendorService;
	this.tabs = com.digitald4.iis.VendorCtrl.TABS;
  if (!flags.vendorBillingEnabled) {
    delete this.tabs.billable;
    delete this.tabs.invoices;
  }
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
			filter: 'vendorId=' + this.vendorId + ',statusId=1520'},
    NOTES: {
      base: com.digitald4.iis.TableBaseMeta.NOTES,
      filter : 'status=Active,entityType=Vendor,entityId=' + this.vendorId},
		CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=Vendor,entityId=' + this.vendorId}
	}

  var eventClicked = (event, jsEvent, view) => {console.log('Click event: ' + event.title)}

  /* Render Tooltip */
  var eventRender = function(event, element, view) {
    element.attr({'tooltip': event.title, 'tooltip-append-to-body': true});
    // $compile(element)($scope);
  }

  var viewRender = (view, element) => {this.refreshAppointments(view.start, view.end)}

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
  }
  this.eventSources = [this.events];
	this.refresh();
	this.setSelectedTab(this.tabs[$routeParams.tab] || this.tabs.general);
}

com.digitald4.iis.VendorCtrl.TABS = {
	calendar: 'Calendar',
	general: 'General',
	patients: 'Patients',
	pending: 'Pending Assessment',
	billable: 'Billable',
	invoices: 'Invoices',
	reports: 'Reports',
	notes: 'Notes',
	changeHistory: 'Change History'
}
com.digitald4.iis.VendorCtrl.prototype.vendorId;
com.digitald4.iis.VendorCtrl.prototype.vendorService;
com.digitald4.iis.VendorCtrl.prototype.vendor;
com.digitald4.iis.VendorCtrl.prototype.selectedTab;
com.digitald4.iis.VendorCtrl.prototype.events = [];

com.digitald4.iis.VendorCtrl.prototype.refresh = function() {
	this.vendorService.get(this.vendorId, vendor => {this.vendor = vendor});
}

com.digitald4.iis.VendorCtrl.prototype.refreshAppointments = function(startDate, endDate) {
  var request = {filter: 'vendorId=' + this.vendorId + "start>=" + startDate + "start<=" + endDate};
	this.appointmentService.list(request, response => {
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
	});
}

com.digitald4.iis.VendorCtrl.prototype.setSelectedTab = function(tab) {
  if (tab == com.digitald4.iis.VendorCtrl.TABS.billable) {
    this.refreshBillables();
  }
	this.selectedTab = tab;
}

com.digitald4.iis.VendorCtrl.prototype.update = function(prop) {
	this.vendorService.update(this.vendor, [prop], vendor => {this.vendor = vendor});
}

com.digitald4.iis.VendorCtrl.prototype.refreshBillables = function() {
  var request = {filter: AppointmentState.BILLABLE + ',vendorId=' + this.vendorId};
  this.appointmentService.list(request, response => {this.billables = response.items});
}

com.digitald4.iis.VendorCtrl.prototype.updateBillable = function(billable, prop) {
  var index = this.billables.indexOf(billable);
  this.appointmentService.update(billable, [prop], updated => {
    updated.selected = billable.selected;
    this.billables.splice(index, 1, updated);
    this.updateInvoice();
  });
}

com.digitald4.iis.VendorCtrl.prototype.updateInvoice = function() {
  this.invoice = this.invoice || {};
  this.invoice.vendorId = this.vendorId;
  this.invoice.statusId = com.digitald4.iis.GenData.PAYMENT_STATUS_PAYMENT_STATUS_UNPAID;
  this.invoice.appointmentIds = [];
  this.invoice.loggedHours = 0;
  this.invoice.standardBilling = 0;
  this.invoice.mileage = 0;
  this.invoice.billedMileage = 0;
  this.invoice.totalDue = 0;
  for (var i = 0; i < this.billables.length; i++) {
    var billable = this.billables[i];
    if (billable.selected) {
      var billingInfo =  billable.billingInfo || {};
      this.invoice.appointmentIds.push(billable.id);
      this.invoice.loggedHours += billable.loggedHours || 0;
      this.invoice.standardBilling += billingInfo.subTotal || 0;
      this.invoice.mileage += billingInfo.mileage || 0;
      this.invoice.billedMileage += billingInfo.mileageTotal || 0;
    }
  }
  this.invoice.totalDue = this.invoice.standardBilling + this.invoice.billedMileage;
}

com.digitald4.iis.VendorCtrl.prototype.createInvoice = function() {
  this.invoiceService.create(this.invoice, invoice => {
    // Remove the billabes that were included in the invoice.
    for (var i = this.billables.length - 1; i >= 0; i--) {
      if (this.billables[i].selected) {
        this.billables.splice(i, 1);
      }
    }
    this.invoice = {};
  });
}

com.digitald4.iis.VendorCtrl.prototype.showAddNoteDialog = function(license) {
	this.addNoteDialogShown = true;
}

com.digitald4.iis.VendorCtrl.prototype.onNoteDialogState = function() {
  this.noteAdding = !this.noteAdding;
}