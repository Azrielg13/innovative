var TableBaseMeta = {PAYABLE: {title: 'Payable', entity: 'appointment',
      columns: [
      	{title: 'Patient', prop: 'patientName', url: appointment => {return '#patient/' + appointment.patientId}},
        {title: 'Date', prop: 'date', type: 'date'},
        {title: 'Payment Type', prop: 'payingTypeId', editable: true},
        {title: 'Pay Hours', prop: 'payHours', editable: true},
        {title: 'Hourly Rate', prop: 'payRate', editable: true},
        {title: 'Visit Pay', prop: 'payFlat', editable: true},
        {title: 'Pay Mileage', prop: 'payMileage', editable: true},
        {title: 'Mileage Rate', prop: 'mileageRate', editable: true},
        {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]}};

com.digitald4.iis.NurseCtrl = function($routeParams, $filter, appointmentService, generalDataService,
    fileService, flags, licenseService, noteService, nurseService, serviceCodeService) {
  this.filter = $filter;
  this.nurseId = parseInt($routeParams.id, 10);
  this.nurseStatuses = enums.EmployeeStatus;
  this.payPreferences = enums.PayPreferences;
  this.appointmentService = appointmentService;
  this.flags = flags;
  this.fileService = fileService;
  this.generalDataService = generalDataService;
  this.licenseService = licenseService;
  this.noteService = noteService;
  this.nurseService = nurseService;
  this.serviceCodeService = serviceCodeService;
  this.tabs = com.digitald4.iis.NurseCtrl.TABS;
  if (!flags.nursePayEnabled) {
    delete this.tabs.payable;
    delete this.tabs.payHistory;
  }
  this.TableType = {
    APPOINTMENTS: {
      base: com.digitald4.iis.TableBaseMeta.APPOINTMENTS,
      filter: 'nurseId=' + this.nurseId},
    PENDING_ASSESSMENT: {
      base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
      filter: AppointmentState.PENDING_ASSESSMENT + ',nurseId=' + this.nurseId},
    REVIEWABLE: {
      base: com.digitald4.iis.TableBaseMeta.REVIEWABLE,
      filter: AppointmentState.PENDING_APPROVAL + ',nurseId=' + this.nurseId},
	  PAYABLE: {
	    base: TableBaseMeta.PAYABLE,
	    filter: AppointmentState.PAYABLE + ',nurseId=' + this.nurseId},
	  PAY_CODES: {
	    base: com.digitald4.iis.TableBaseMeta.PAY_CODES,
	    filter: 'nurseId=' + this.nurseId},
	  PAY_HISTORY: {
	    base: com.digitald4.iis.TableBaseMeta.PAY_HISTORY,
	    filter: 'nurseId=' + this.nurseId},
    NOTES: {
      base: com.digitald4.iis.TableBaseMeta.NOTES,
      filter: 'entityType=Nurse,entityId=' + this.nurseId},
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
	payCodes: 'Pay Codes',
	appointments: 'Appointments',
	pending: 'Pending Assessment',
	reviewable: 'Awaiting Review',
	payable: 'Payable',
	payHistory: 'Pay History',
	notes: 'Notes',
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

com.digitald4.iis.NurseCtrl.prototype.refreshAppointments = function(startDate, endDate) {
  var request = {filter:
      'nurseId=' + this.nurseId + ',start>=' + startDate.valueOf() + ',start<=' + endDate.valueOf()};
	this.appointmentService.list(request, response => {
    this.events.length = 0;
    var appointments = response.items;
    for (var a = 0; a < appointments.length; a++) {
      var appointment = appointments[a];
      this.events.push({id: appointment.id,
          title: this.filter('date')(appointment.startTime, 'shortTime') + ' ' + appointment.patientName,
          start: new Date(appointment.date + appointment.startTime),
          end: new Date(appointment.date + appointment.endTime),
          appointment: appointment,
          className: ['appointment']
      });
    }
  });
}

com.digitald4.iis.NurseCtrl.prototype.setSelectedTab = function(tab) {
  if (tab == com.digitald4.iis.NurseCtrl.TABS.licenses) {
    this.refreshLicenses();
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
