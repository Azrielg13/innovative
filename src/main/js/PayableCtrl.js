com.digitald4.iis.PayableCtrl = function($filter,
    appointmentService, serviceCodeService, quickBooksExportService, userService) {
  this.dateFilter = $filter('date');
	this.appointmentService = appointmentService;
	this.serviceCodeService = serviceCodeService;
	this.quickBooksExportService = quickBooksExportService;
	this.getFileUrl = (fileReference, type) => userService.getFileUrl(fileReference, type);
	this.userService = userService;
	this.dateRange = {};

	this.filter = AppointmentState.PAYABLE;
	if (this.entityType == 'Nurse') {
		this.filter += ',nurseId=' + this.entityId;
	} else if (this.entityType == 'Vendor') {
		this.filter += ',vendorId=' + this.entityId;
	} else if (this.entityType == 'Patient') {
		this.filter += ',patientId=' + this.entityId;
  }
	this.refresh();
	this.exportedDisabled = true;
}

com.digitald4.iis.PayableCtrl.prototype.refresh = function() {
	this.payCodeMap = {};
	this.billCodeMap = {};
	var filter = this.filter;
	if (this.dateRange.start) {
	  filter += ',date>=' + this.dateRange.start;
	}
	if (this.dateRange.end) {
	  filter += ',date<=' + this.dateRange.end;
  }
  this.appointmentService.list({filter: filter, orderBy: 'vendorName,patientName,date'}, response => {
  	this.appointments = response.items;

  	var nurseIds = new Set();
  	var vendorIds = new Set();
  	this.appointments.forEach(appointment => {
  		if (appointment.nurseId) {
  		  nurseIds.add(appointment.nurseId);
  		}
  		if (appointment.vendorId) {
  		  vendorIds.add(appointment.vendorId);
  		}
  	});

  	if (this.purpose == 'Pending' || this.appointments.length == 0) {
  		return;
  	}

  	var nurseIdStr = '';
  	for (const id of nurseIds) {
  		if (nurseIdStr.length > 0) {
  			nurseIdStr += '|';
  		}
  		nurseIdStr += id;
  	}

  	this.serviceCodeService.list({filter: 'nurseId IN ' + nurseIdStr + ',active=True'}, response => {
  		for (const serviceCode of response.items) {
  			var entityId = serviceCode.nurseId;
  			if (!this.payCodeMap[entityId]) {
  				this.payCodeMap[entityId] = [];
  			}
  			this.payCodeMap[entityId].push(serviceCode);
  		}
  	});

  	var vendorIdStr = '';
  	for (const id of vendorIds) {
  		if (vendorIdStr.length > 0) {
  			vendorIdStr += '|';
  		}
  		vendorIdStr += id;
  	}

  	this.serviceCodeService.list({filter: 'vendorId IN ' + vendorIdStr + ',active=True'}, response => {
  		for (const serviceCode of response.items) {
  			var entityId = serviceCode.vendorId;
  			if (!this.billCodeMap[entityId]) {
  				this.billCodeMap[entityId] = [];
  			}
  			this.billCodeMap[entityId].push(serviceCode);
  		}
  	});
	});
}

com.digitald4.iis.PayableCtrl.prototype.getPayCodes = function(appointment) {
  return this.payCodeMap[appointment.nurseId];
}

com.digitald4.iis.PayableCtrl.prototype.getBillCodes = function(appointment) {
  return this.billCodeMap[appointment.vendorId];
}

com.digitald4.iis.PayableCtrl.prototype.update = function(appointment, prop) {
  var index = this.appointments.indexOf(appointment);
  this.appointmentService.update(appointment, [prop], updated => {
    updated.selected = appointment.selected;

    // this.appointments.splice(index, 1, updated);
    // appointment[prop] = updated[prop];
    appointment.loggedHours = updated.loggedHours;
    appointment.state = updated.state;
    appointment.paymentInfo = updated.paymentInfo;
    appointment.billingInfo = updated.billingInfo;
  });
}

com.digitald4.iis.PayableCtrl.prototype.setAll = function(selected) {
  this.appointments.forEach(appointment => appointment.selected = selected);
  this.updateExport();
}

com.digitald4.iis.PayableCtrl.prototype.updateExport = function() {
	this.exportedDisabled = true;
	var foundSelected = false;
	var foundError = false;

	this.appointments.forEach(appointment => {
	  appointment._billInfoStyle = appointment._payInfoStyle = undefined;
    if (appointment.selected) {
    	foundSelected = true;
    	if (!appointment.billingInfo || !appointment.billingInfo.serviceCode) {
    	  appointment._billInfoStyle = 'border: 0.333em solid #eb310b;';
    	  foundError = true;
    	}
    	if (!appointment.paymentInfo || !appointment.paymentInfo.serviceCode) {
    	  appointment._payInfoStyle = 'border: 0.333em solid #eb310b;';
    	  foundError = true;
    	}
    }
	});

	this.exportedDisabled = !foundSelected || foundError;
}

com.digitald4.iis.PayableCtrl.prototype.showCreateDialog = function() {
  var now = Date.now();
  var date = this.dateFilter(now, 'yyyyMMdd');
  var time = this.dateFilter(now, 'HHmmss');
  this.qbExport = {id: `IP360_${date}_${time}_QUICKBOOKS_INV`, billingDate: now};
  this.createDialogShown = true;
}

com.digitald4.iis.PayableCtrl.prototype.exportSelected = function() {
  this.exportedDisabled = true;
	this.qbExport.appointmentIds = [];
	this.appointments.forEach(appointment => {
		if (appointment.selected) {
			this.qbExport.appointmentIds.push(appointment.id);
		}
	});

  this.quickBooksExportService.create(this.qbExport, created => {
    // Remove the appointments that were included in the export.
    for (var i = this.appointments.length - 1; i >= 0; i--) {
      if (this.appointments[i].selected) {
        this.appointments.splice(i, 1);
      }
    }
    this.fileReference = created.fileReference;
    this.invoiceFileReference = created.invoiceFileReference;
    this.createDialogShown = false;
  });
}