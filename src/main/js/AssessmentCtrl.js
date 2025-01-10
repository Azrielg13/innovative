com.digitald4.iis.AssessmentCtrl = function($routeParams,
    appointmentService, fileService, flags, generalDataService, serviceCodeService) {
  this.appointmentService = appointmentService;
  this.fileService = fileService;
  this.flags = flags;
  this.generalDataService = generalDataService;
  this.serviceCodeService = serviceCodeService;
  this.appointmentId = parseInt($routeParams.id, 10);
  this.accountingTypes = enums.AccountingType;
  this.setupTabs();
  this.refresh();
  this.setSelectedTab($routeParams.tab || 'general');
	this.TableType = {
		CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=Appointment,entityId=' + this.appointmentId
    }
	};
}

com.digitald4.iis.AssessmentCtrl.prototype.setSelectedTab = function(tab) {
  this.selectedTab = tab;
}

com.digitald4.iis.AssessmentCtrl.prototype.setupTabs = function() {
  this.generalDataService.list(com.digitald4.iis.GenData.ASS_CAT, generalDatas => {
    this.assCats = [];
    for (var i = 0; i < generalDatas.length; i++) {
      var assCat = {
        name: generalDatas[i].name.replace(' ', '_'), title: generalDatas[i].name, assessments: []};
      for (var j = 0; j < generalDatas[i].generalDatas.length; j++) {
        var assGD = generalDatas[i].generalDatas[j];
        var ass = {id: assGD.id, name: assGD.name, data: assGD.data, options: []};
        for (var k = 0; k < assGD.generalDatas.length; k++) {
          ass.options.push({id: assGD.generalDatas[k].name, name: assGD.generalDatas[k].name});
        }
        assCat.assessments.push(ass);
      }
      this.assCats.push(assCat);
    }
  });
}

com.digitald4.iis.AssessmentCtrl.prototype.getFileUrl = function(fileReference, type) {
  return this.appointmentService.getFileUrl(fileReference, type);
}

com.digitald4.iis.AssessmentCtrl.prototype.setAppointment = function(appointment) {
	this.serviceCodeService.list({filter: 'nurseId=' + appointment.nurseId}, response => {
		this.payCodes = response.items;
	});

	this.serviceCodeService.list({filter: 'vendorId=' + appointment.vendorId}, response => {
		this.billCodes = response.items;
	});

  if (this.flags.billableEnabled) {
    if (appointment.invoiceId) {
      appointment._invoiceLink = this.getFileUrl("invoice-" + appointment.invoiceId + ".pdf");
    }
    if (appointment.exportId) {
      appointment._exportLink = this.getFileUrl(appointment.exportId + ".csv");
    }
	}

  appointment.assessmentMap = {};
  appointment.assessments = appointment.assessments || [];
  for (var ass = 0; ass < appointment.assessments.length; ass++) {
    var assessment = appointment.assessments[ass];
    if (assessment.value.indexOf('[') == 0) {
      appointment.assessmentMap[assessment.typeId] = JSON.parse(appointment.assessments[ass].value);
    } else {
      appointment.assessmentMap[assessment.typeId] = appointment.assessments[ass].value;
    }
  }
  this.appointment = appointment;
}

com.digitald4.iis.AssessmentCtrl.prototype.refresh = function() {
	this.appointmentService.get(this.appointmentId, appointment => {this.setAppointment(appointment)});
}

com.digitald4.iis.AssessmentCtrl.prototype.updateAppointment = function(prop) {
  var assMap = this.appointment.assessmentMap;
  this.appointment.assessments = [];
  for (var ass in assMap) {
    this.appointment.assessments.push({typeId: ass,
        value: (typeof(assMap[ass]) == 'object') ? JSON.stringify(assMap[ass]) : assMap[ass]});
  }
	this.appointmentService.update(
	    this.appointment, [prop], appointment => {this.setAppointment(appointment)});
}

com.digitald4.iis.AssessmentCtrl.prototype.showUploadDialog = function() {
	this.uploadDialogShown = true;
}

com.digitald4.iis.AssessmentCtrl.prototype.closeUploadDialog = function() {
	this.uploadDialogShown = false;
}

com.digitald4.iis.AssessmentCtrl.prototype.uploadFile = function() {
  var file = document.getElementById('file');
  var request = {file: file, entityType: 'Appointment', entityId: this.appointmentId};
  this.fileService.upload(request, fileReference => {
    this.appointment.assessmentReport = fileReference;
    this.updateAppointment('assessmentReport');
    this.closeUploadDialog();
  });
}

com.digitald4.iis.AssessmentCtrl.prototype.showDeleteFileDialog = function() {
  var filename = this.appointment.assessmentReport.name;
  this.fileService.Delete(filename, () => {this.appointment.assessmentReport = undefined});
}
