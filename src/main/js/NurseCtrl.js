var TableBaseMeta = {PAYABLE: {title: 'Payable', entity: 'appointment',
      columns: [{title: 'Patient', prop: 'patientName',
            url: appointment => {return '#patient/' + appointment.patientId}},
        {title: 'Date', prop: 'start', type: 'date'},
        {title: 'Payment Type', prop: 'payingTypeId', editable: true},
        {title: 'Pay Hours', prop: 'payHours', editable: true},
        {title: 'Hourly Rate', prop: 'payRate', editable: true},
        {title: 'Visit Pay', prop: 'payFlat', editable: true},
        {title: 'Pay Mileage', prop: 'payMileage', editable: true},
        {title: 'Mileage Rate', prop: 'mileageRate', editable: true},
        {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]}};

com.digitald4.iis.NurseCtrl = function($routeParams, $filter, appointmentService, fileService,
    generalDataService, licenseService, nurseService, paystubService) {
  this.filter = $filter;
  this.nurseId = parseInt($routeParams.id, 10);
  this.appointmentService = appointmentService;
  this.fileService = fileService;
  this.generalDataService = generalDataService;
  this.licenseService = licenseService;
  this.nurseService = nurseService;
  this.paystubService = paystubService;
  this.tabs = com.digitald4.iis.NurseCtrl.TABS;
  this.TableType = {
    UNCONFIRMED: {
      base: com.digitald4.iis.TableBaseMeta.UNCONFIRMED,
      filter: AppointmentState.UNCONFIRMED + ',nurseId=' + this.nurseId},
    PENDING_ASSESSMENT: {
      base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
      filter: AppointmentState.PENDING_ASSESSMENT + ',nurseId=' + this.nurseId},
    REVIEWABLE: {
      base: com.digitald4.iis.TableBaseMeta.REVIEWABLE,
      filter: AppointmentState.PENDING_APPROVAL + ',nurseId=' + this.nurseId},
	  PAYABLE: {
	    base: TableBaseMeta.PAYABLE,
	    filter: AppointmentState.PAYABLE + ',nurseId=' + this.nurseId},
	  PAY_HISTORY: {
	    base: com.digitald4.iis.TableBaseMeta.PAY_HISTORY,
	    filter: 'nurseId=' + this.nurseId},
    CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=Nurse,entityId=' + this.nurseId}
  }

  var eventClicked = (event, jsEvent, view) => {
    console.log('Click event: ' + event.title);
  }

  /* Render Tooltip */
  var eventRender = (event, element, view) => {
    element.attr({'tooltip': event.title, 'tooltip-append-to-body': true});
    // $compile(element)($scope);
  }

  var viewRender = (view, element) => {this.refreshAppointments(view.start, view.end)};

  this.uiConfig = {
    calendar:{
      height: 450,
      editable: false,
      header: {
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
	this.setSelectedTab(this.tabs[$routeParams.tab] || ($routeParams.tab || this.tabs.general));
}

com.digitald4.iis.NurseCtrl.TABS = {
  calendar: 'Calendar',
	general: 'General',
	licenses: 'Licenses',
	unconfirmed: 'Unconfirmed',
	pending: 'Pending Assessment',
	reviewable: 'Awaiting Review',
	payable: 'Payable',
	payHistory: 'Pay History',
	changeHistory: 'Change History'
}

com.digitald4.iis.NurseCtrl.prototype.refresh = function() {
  this.nurseService.get(this.nurseId, nurse => {this.nurse = nurse});
}

com.digitald4.iis.NurseCtrl.prototype.refreshLicenses = function() {
	this.licenseService.list({filter: 'nurseId=' + this.nurseId}, response => {
	  var byTypeHash = {}
	  var licenses = response.items;
	  for (var l = 0; l < licenses.length; l++) {
	    var license = licenses[l];
	    byTypeHash[license.licTypeId] = license;
	  }

	  var licenseCats = {};
	  var allLicenseTypes = {};
	  this.generalDataService.list(com.digitald4.iis.GeneralData.LICENSE, generalDatas => {
      for (var c = 0; c < generalDatas.length; c++) {
        var licenseCat = {id: generalDatas[c].id, name: generalDatas[c].name, licenses: []};
        var licenseTypes = this.generalDataService.list(licenseCat.id);
        for (var t = 0; t < licenseTypes.length; t++) {
          var licenseType = licenseTypes[t];
          var license = byTypeHash[licenseType.id] ||
              {licTypeId: licenseType.id, nurseId: this.nurseId, licTypeName: licenseType.name};
          allLicenseTypes[licenseType.id] = licenseType;
          licenseCat.licenses.push(license);
        }
        licenseCats[licenseCat.id] = licenseCat;
      }
      this.licenseCategories = licenseCats;
      this.licenseTypes = allLicenseTypes;
    });
	});
}

com.digitald4.iis.NurseCtrl.prototype.refreshPayables = function() {
  var request = {filter: AppointmentState.PAYABLE + ',nurseId=' + this.nurseId};
  this.appointmentService.list(request, response => {this.payables = response.items});
}

com.digitald4.iis.NurseCtrl.prototype.refreshAppointments = function(startDate, endDate) {
  var request = {filter:
      'nurseId=' + this.nurseId + ',start>=' + startDate.valueOf() + ',start<=' + endDate.valueOf()};
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

com.digitald4.iis.NurseCtrl.prototype.setSelectedTab = function(tab) {
  if (tab == com.digitald4.iis.NurseCtrl.TABS.licenses) {
    this.refreshLicenses();
  } else if (tab == com.digitald4.iis.NurseCtrl.TABS.payable) {
    this.refreshPayables();
  }
	this.selectedTab = tab;
}

com.digitald4.iis.NurseCtrl.prototype.update = function(prop) {
	this.nurseService.update(this.nurse, [prop], nurse => {this.nurse = nurse});
}

com.digitald4.iis.NurseCtrl.prototype.hasExpDate = function(license) {
  return !this.licenseTypes[license.licTypeId].data;
}

com.digitald4.iis.NurseCtrl.prototype.updateLicense = function(license, prop) {
  console.log(['updateLicense called: ', license]);
  if (license.id) {
    this.licenseService.update(license, [prop], lic => {});
  } else {
    this.licenseService.create(license, lic => {license.id = lic.id;});
  }
}

com.digitald4.iis.NurseCtrl.prototype.updatePayable = function(payable, prop) {
  var index = this.payables.indexOf(payable);
  this.appointmentService.update(payable, [prop], updated => {
    updated.selected = payable.selected;
    this.payables.splice(index, 1, updated);
    this.updatePaystub();
  });
}

com.digitald4.iis.NurseCtrl.prototype.updatePaystub = function() {
  var paystub = this.paystub || {};
  paystub.nurseId = this.nurseId;
  paystub.statusId = com.digitald4.iis.GenData.PAYMENT_STATUS_PAYMENT_STATUS_UNPAID;
  paystub.appointmentIds = [];
  paystub.loggedHours = 0;
  paystub.grossPay = 0;
  paystub.mileage = 0;
  paystub.payMileage = 0;
  paystub.preTaxDeduction = 0;
  paystub.taxable = 0;
  paystub.taxTotal = 0;
  paystub.postTaxDeduction = 0;
  paystub.nonTaxWages = 0;
  paystub.netPay = 0;
  for (var i = 0; i < this.payables.length; i++) {
    var payable = this.payables[i];
    if (payable.selected) {
      var paymentInfo = payable.paymentInfo || {};
      paystub.appointmentIds.push(payable.id);
      paystub.loggedHours += payable.loggedHours || 0;
      paystub.grossPay += paymentInfo.subTotal || 0;
      paystub.mileage += paymentInfo.mileage || 0;
      paystub.payMileage += paymentInfo.mileageTotal || 0;
    }
  }
  paystub.taxable = paystub.grossPay - paystub.preTaxDeduction;
  paystub.nonTaxWages = paystub.payMileage;
  paystub.netPay =
      paystub.taxable - paystub.taxTotal - paystub.postTaxDeduction + paystub.nonTaxWages;
  this.paystub = paystub;
}

com.digitald4.iis.NurseCtrl.prototype.createPaystub = function() {
  this.paystubService.create(this.paystub, paystub => {
    // Remove the payables that were included in the paystub
    for (var i = this.payables.length - 1; i >= 0; i--) {
      if (this.payables[i].selected) {
        this.payables.splice(i, 1);
      }
    }
    this.paystub = {};
  });
}

com.digitald4.iis.NurseCtrl.prototype.showUploadDialog = function(license) {
  this.uploadLicense = license;
	this.uploadDialogShown = true;
}

com.digitald4.iis.NurseCtrl.prototype.closeUploadDialog = function() {
	this.uploadDialogShown = false;
}

com.digitald4.iis.NurseCtrl.prototype.uploadFile = function() {
  var file = document.getElementById('file');
  var request = {file: file, entityType: 'License', entityId: this.uploadLicense.id};
  this.fileService.upload(request, fileReference => {
    this.uploadLicense.fileReference = fileReference;
    this.updateLicense(this.uploadLicense, 'fileReference');
    this.closeUploadDialog();
  });
}

com.digitald4.iis.NurseCtrl.prototype.showDeleteFileDialog = function(license) {
  this.fileService.Delete(license.fileReference.name, deleted => {
    if (deleted) {
      license.fileReference = undefined;
    }
  });
}
