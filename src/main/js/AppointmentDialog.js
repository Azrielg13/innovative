com.digitald4.iis.AppointmentDialog = function($filter, $window, globalData, flags, appointmentService,
    fileService, nurseService, patientService, serviceCodeService) {
	this.dateFilter = $filter('date');
	this.window = $window;
	this.role = globalData.activeSession.user.role;
	this.flags = flags;
	this.appointmentService = appointmentService;
	this.fileService = fileService;
	this.nurseService = nurseService;
	this.patientService = patientService;
	this.serviceCodeService = serviceCodeService;
	this.edits = [];
	this.appointment = this.dialogRequest.entity;
	this.TableType = {
		CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=Appointment,entityId=' + this.appointment.id
    }
	};
	if (this.isEditable('nurse')) {
	  nurseService.getActive(nurses => {
	    var found = false;
	    nurses.forEach(nurse => {
	      if (nurse.id == this.appointment.nurseId) {
	        found = true;
	      }
	    });
	    if (!found) {
	      nurses.push({id: this.appointment.nurseId, fullName: this.appointment.nurseName});
      }
      this.nurses = nurses;
    });
	  patientService.getActive(patients => {
	    var found = false;
      patients.forEach(patient => {
        if (patient.id == this.appointment.patientId) {
          found = true;
        }
      });
      if (!found) {
        patients.push({id: this.appointment.patientId, fullName: this.appointment.patientName});
      }
	    this.patients = patients;
    });
	}
	setDialogStyle(this);
	this.setView(this.dialogRequest.metadata);
}

com.digitald4.iis.AppointmentDialog.prototype.setView = function(view) {
  this.dialogStyle.width = '365px';
  var appointment = this.appointment;
  if (!view) {
    view = 'Info';
  }

  if (view == 'Info') {
    if (this.flags.billableEnabled) {
      if (appointment.invoiceId) {
        appointment._invoiceLink = this.getFileUrl("invoice-" + appointment.invoiceId + ".pdf");
      }
      if (appointment.exportId) {
        appointment._exportLink = this.getFileUrl(appointment.exportId + ".csv");
      }
    }
	} else if (view == 'Assessment') {
    if (this.flags.billableEnabled) {
      this.refreshServiceCodes();
      this.dialogStyle.width = '650px';
    }
  } else if (view == 'Patient' && !this.patient) {
    this.patientService.get(appointment.patientId, patient => this.patient = patient);
  } else if (view == 'Nurse' && !this.nurse) {
    this.nurseService.get(appointment.nurseId, nurse => this.nurse = nurse);
  } else if (view == 'History') {
    this.dialogStyle.width = '1024px';
  }
  this.view = view;
}

com.digitald4.iis.AppointmentDialog.prototype.isEditable = function(field) {
  var state = this.appointment.state;
  var role = this.role;
  if (state == 'CLOSED' || state == 'DELETED') {
    return false;
  }

  if (state == 'CANCELLED') {
    return role != 'Nurse' && (field == 'cancelled' || field == 'cancelReason');
  }

  switch (field) {
    case 'delete':
      return role != 'Nurse' && state == 'UNCONFIRMED';
    case 'startTime':
      return state == 'UNCONFIRMED' || state == 'CONFIRMED' || state == 'PENDING_ASSESSMENT';
    case 'cancelled':
    case 'cancelReason':
    case 'nurse':
    case 'patient':
    case 'date':
    case 'titration':
      return role != 'Nurse' && (state == 'UNCONFIRMED' || state == 'CONFIRMED' || state == 'PENDING_ASSESSMENT');
    case 'timeIn':
    case 'timeOut':
    case 'fromZipCode':
    case 'toZipCode':
    case 'mileage':
    case 'upload':
    case 'removeAttachment':
    case 'assessmentComplete':
      return state == 'PENDING_ASSESSMENT' || (role != 'Nurse' && state == 'PENDING_APPROVAL');
    case 'assessmentApproved':
    case 'billing':
      return role != 'Nurse' && (state == 'PENDING_ASSESSMENT' || state == 'PENDING_APPROVAL' || state == 'BILLABLE_AND_PAYABLE');
  }

  // UNCONFIRMED, CONFIRMED, CANCELLED, DELETED, PENDING_ASSESSMENT, PENDING_APPROVAL, BILLABLE_AND_PAYABLE, CLOSED;

  console.log('No case for: ' + field);
  return false;
}

com.digitald4.iis.AppointmentDialog.prototype.refreshServiceCodes = function() {
  if (!this.payCodes) {
    this.serviceCodeService.list({filter: 'nurseId=' + this.appointment.nurseId + ',active=true'}, response => {
      this.payCodes = response.items;
    });

    this.serviceCodeService.list({filter: 'vendorId=' + this.appointment.vendorId + ',active=true'}, response => {
      this.billCodes = response.items;
    });
	}
}

com.digitald4.iis.AppointmentDialog.prototype.closeDialog = function() {
  this.dialogRequest.shown = false;
}

com.digitald4.iis.AppointmentDialog.prototype.update = function(prop) {
  this.edits.push(prop);
  this.saveEdits();
}

com.digitald4.iis.AppointmentDialog.prototype.saveEdits = function() {
  this.appointmentService.update(this.dialogRequest.entity, this.edits, updated => {
    this.dialogRequest.entity = updated;
    this.appointment = updated;
    this.edits = [];
    this.dialogRequest.postUpdate(this.dialogRequest);
    this.dialogRequest.original = updated;
  });
}

com.digitald4.iis.AppointmentDialog.prototype.deleteSelected = function() {
  this.appointmentService.cancelOut(this.appointment.id, this.eventOption, response => {
	  this.closeDialog();
	  this.postDelete(response);
  });
}

com.digitald4.iis.AppointmentDialog.prototype.showUploadDialog = function() {
	this.uploadDialogShown = true;
}

com.digitald4.iis.AppointmentDialog.prototype.closeUploadDialog = function() {
	this.uploadDialogShown = false;
}

com.digitald4.iis.AppointmentDialog.prototype.uploadFile = function() {
  var file = document.getElementById('file');
  var request = {file: file, entityType: 'Appointment', entityId: this.appointment.id};
  this.fileService.upload(request, fileReference => {
    this.appointment.attachments = this.appointment.attachments || [];
    this.appointment.attachments.push(fileReference);
    this.closeUploadDialog();
    this.$scope.$apply();
  });
}

com.digitald4.iis.AppointmentDialog.prototype.removeAttachment = function(attachment) {
  this.appointmentService.removeAttachment(this.appointment.id, attachment.id, response => {
    this.appointment.attachments.splice(attachment, 1);
  });
}

com.digitald4.iis.AppointmentDialog.prototype.getFileUrl = function(fileReference, type) {
  return this.appointmentService.getFileUrl(fileReference, type);
}
