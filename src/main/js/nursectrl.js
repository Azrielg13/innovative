var TableBaseMeta = {PAYABLE: {title: 'Payable', entity: 'appointment',
      columns: [{title: 'Patient', prop: 'patientName',
            getUrl: function(appointment){return '#patient/' + appointment.patientId;}},
        {title: 'Date', prop: 'start', type: 'date'},
        {title: 'Payment Type', prop: 'payingTypeId', editable: true},
        {title: 'Pay Hours', prop: 'payHours', editable: true},
        {title: 'Hourly Rate', prop: 'payRate', editable: true},
        {title: 'Visit Pay', prop: 'payFlat', editable: true},
        {title: 'Pay Mileage', prop: 'payMileage', editable: true},
        {title: 'Mileage Rate', prop: 'mileageRate', editable: true},
        {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]}};

com.digitald4.iis.NurseCtrl = function($routeParams, $filter, nurseService, licenseService, appointmentService,
    generalDataService, paystubService) {
  this.filter = $filter;
	this.nurseId = parseInt($routeParams.id, 10);
	this.nurseService = nurseService;
	this.licenseService = licenseService;
	this.appointmentService = appointmentService;
	this.generalDataService = generalDataService;
	this.paystubService = paystubService;
	this.tabs = com.digitald4.iis.NurseCtrl.TABS;
	this.TableType = {
		UNCONFIRMED: {base: com.digitald4.iis.TableBaseMeta.UNCONFIRMED,
			filter: {'state': AppointmentState.AS_UNCONFIRMED,
			         'nurse_id': this.nurseId}},
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: {'state': AppointmentState.AS_PENDING_ASSESSMENT,
			         'nurse_id': this.nurseId}},
		REVIEWABLE: {base: com.digitald4.iis.TableBaseMeta.REVIEWABLE,
			filter: {'state': AppointmentState.AS_PENDING_APPROVAL,
			         'nurse_id': this.nurseId}},
		PAYABLE: {base: TableBaseMeta.PAYABLE,
			filter: {'state': '>=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
               'state_1': '<=' + AppointmentState.AS_PAYABLE,
               'nurse_id': this.nurseId}},
		PAY_HISTORY: {base: com.digitald4.iis.TableBaseMeta.PAY_HISTORY,
			filter: {'nurse_id': this.nurseId}}
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
	this.setSelectedTab(this.tabs[$routeParams.tab] || ($routeParams.tab || this.tabs.general));
};

com.digitald4.iis.NurseCtrl.TABS = {
	calendar: 'Calendar',
	general: 'General',
	licenses: 'Licenses',
	unconfirmed: 'Unconfirmed',
	pending: 'Pending Assessment',
	reviewable: 'Awaiting Review',
	payable: 'Payable',
	payHistory: 'Pay History'
};

com.digitald4.iis.NurseCtrl.prototype.refresh = function() {
	this.nurseService.get(this.nurseId, function(nurse) {
		this.nurse = nurse;
	}.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.refreshLicenses = function() {
	this.licenseService.list({'nurseId': this.nurseId}, function(response) {
	  var byTypeHash = {}
	  var licenses = response.result;
	  for (var l = 0; l < licenses.length; l++) {
	    var license = licenses[l];
	    byTypeHash[license.licTypeId] = license;
	  }
	  var licenseCats = {};
	  var generalDatas = this.generalDataService.list(com.digitald4.iis.GeneralData.LICENSE);
	  for (var c = 0; c < generalDatas.length; c++) {
	    var licenseCat = {id: generalDatas[c].id, name: generalDatas[c].name, licenses: []};
	    var licenseTypes = this.generalDataService.list(licenseCat.id);
	    for (var t = 0; t < licenseTypes.length; t++) {
	      var licenseType = licenseTypes[t];
	      var license = byTypeHash[licenseType.id] || {licTypeId: licenseType.id, nurseId: this.nurseId};
	      license.type = licenseType;
	      licenseCat.licenses.push(license);
	    }
	    licenseCats[licenseCat.id] = licenseCat;
	  }
	  this.licenseCategories = licenseCats;
	}.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.refreshPayables = function() {
  var filter = {'state': '>=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
                'state_1': '<=' + AppointmentState.AS_PAYABLE,
                'nurse_id': this.nurseId};
  this.appointmentService.list(filter, function(response) {
    this.payables = response.result;
  }.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.refreshAppointments = function(startDate, endDate) {
	this.appointmentService.list({'nurseId': this.nurseId,
                                'start': '>=' + startDate.valueOf(),
                                'start_1': '<=' + endDate.valueOf()},
      function(response) {
        this.events.length = 0;
        var appointments = response.result;
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
	}.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.setSelectedTab = function(tab) {
  if (tab == com.digitald4.iis.NurseCtrl.TABS.licenses) {
    this.refreshLicenses();
  } else if (tab == com.digitald4.iis.NurseCtrl.TABS.payable) {
    this.refreshPayables();
  }
	this.selectedTab = tab;
};

com.digitald4.iis.NurseCtrl.prototype.update = function(prop) {
	this.nurseService.update(this.nurse, [prop], function(nurse) {
		this.nurse = nurse;
	}.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.updateLicense = function(license, prop) {
  console.log('updateLicense called: ' + JSON.stringify(license));
  if (!license.type) {
    return;
  }
  var licenseCategory = this.licenseCategories[license.type.groupId];
  var index = licenseCategory.licenses.indexOf(license);
  var type = license.type;
  if (license.id) {
    this.licenseService.update(license, [prop], function(license) {
      license.type = type;
      licenseCategory.licenses.splice(index, 1, license);
    }.bind(this), notify);
  } else {
    license.type = undefined;
    this.licenseService.create(license, function(license) {
      license.type = type;
      licenseCategory.licenses.splice(index, 1, license);
    }.bind(this), notify);
  }
};

com.digitald4.iis.NurseCtrl.prototype.showUploadDialog = function(license) {
  this.uploadLicense = license;
	this.uploadDialogShown = true;
};

com.digitald4.iis.NurseCtrl.prototype.closeUploadDialog = function() {
	this.uploadDialogShown = false;
};

com.digitald4.iis.NurseCtrl.prototype.updatePayable = function(payable, prop) {
  var index = this.payables.indexOf(payable);
  this.appointmentService.update(payable, [prop], function(payable_) {
    payable_.selected = payable.selected;
    this.payables.splice(index, 1, payable_);
    this.updatePaystub();
  }.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.updatePaystub = function() {
  this.paystub = this.paystub || {};
  this.paystub.nurseId = this.nurseId;
  this.paystub.statusId = com.digitald4.iis.GenData.PAYMENT_STATUS_PAYMENT_STATUS_UNPAID;
  this.paystub.appointmentId = [];
  this.paystub.loggedHours = 0;
  this.paystub.grossPay = 0;
  this.paystub.mileage = 0;
  this.paystub.payMileage = 0;
  this.paystub.preTaxDeduction = 0;
  this.paystub.taxable = 0;
  this.paystub.taxTotal = 0;
  this.paystub.postTaxDeduction = 0;
  this.paystub.nonTaxWages = 0;
  this.paystub.netPay = 0;
  for (var i = 0; i < this.payables.length; i++) {
    var payable = this.payables[i];
    if (payable.selected) {
      var paymentInfo =  payable.paymentInfo || {};
      this.paystub.appointmentId.push(payable.id);
      this.paystub.loggedHours += payable.loggedHours || 0;
      this.paystub.grossPay += paymentInfo.subTotal || 0;
      this.paystub.mileage += paymentInfo.mileage || 0;
      this.paystub.payMileage += paymentInfo.mileageTotal || 0;
    }
  }
  this.paystub.taxable = this.paystub.grossPay - this.paystub.preTaxDeduction;
  this.paystub.nonTaxWages = this.paystub.payMileage;
  this.paystub.netPay =
      this.paystub.taxable - this.paystub.taxTotal - this.paystub.postTaxDeduction + this.paystub.nonTaxWages;
};

com.digitald4.iis.NurseCtrl.prototype.createPaystub = function() {
  this.paystubService.create(this.paystub, function(paystub) {
    // Remove the payables that were included in the paystub
    for (var i = this.payables.length - 1; i >= 0; i--) {
      if (this.payables[i].selected) {
        this.payables.splice(i, 1);
      }
    }
    this.paystub = {};
  }.bind(this), notify);
};

com.digitald4.iis.NurseCtrl.prototype.uploadFile = function() {
  var file = document.getElementById('file');
  var url = 'api/files';
  var xhr = new XMLHttpRequest();
  xhr.addEventListener('progress', function(e) {
    var done = e.position || e.loaded, total = e.totalSize || e.total;
    console.log('xhr progress: ' + (Math.floor(done / total * 1000) / 10) + '%');
  }, false);
  if (xhr.upload) {
    xhr.upload.onprogress = function(e) {
      var done = e.position || e.loaded, total = e.totalSize || e.total;
      console.log('upload progress: ' + done + ' / ' + total + ' = ' + (Math.floor(done / total * 1000) / 10) + '%');
    };
  }
  xhr.onreadystatechange = function(e) {
    if (this.readyState == 4) {
      console.log(['xhr upload complete', e]);
    }
  };
  xhr.open('post', url, true);
  var fd = new FormData;
  // fd.append('classname', className);
  // fd.append('id', id);
  fd.append('file', file.files[0]);
  xhr.send(fd);
};
