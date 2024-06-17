com.digitald4.iis.PayableCtrl = function(appointmentService, serviceCodeService, quickBooksExportService, userService) {
	this.appointmentService = appointmentService;
	this.serviceCodeService = serviceCodeService;
	this.quickBooksExportService = quickBooksExportService;
	this.getFileUrl = (fileReference, type) => { return userService.getFileUrl(fileReference, type)}
	this.userService = userService;

	if (this.purpose == 'Pending') {
		this.request = {filter: AppointmentState.PENDING_ASSESSMENT + ',date>' + (Date.now() - DAYS_30), orderBy: 'date'};
	} else {
		this.request = {filter: AppointmentState.BILLABLE_AND_PAYABLE + ',date>' + (Date.now() - DAYS_30), orderBy: 'date'};
	}

	if (this.entityType == 'Nurse') {
		this.request.filter += ',nurseId=' + this.entityId;
	} else if (this.entityType == 'Vendor') {
		this.request.filter += ',vendorId=' + this.entityId;
	} else if (this.entityType == 'Patient') {
		this.request.filter += ',patientId=' + this.entityId;
  }
	this.refresh();
	this.exportedDisabled = true;
}

com.digitald4.iis.PayableCtrl.prototype.refresh = function() {
	this.payCodeMap = {};
	this.billCodeMap = {};
  this.appointmentService.list(this.request, response => {
  	this.appointments = [];

  	var nurseIds = new Set();
  	var vendorIds = new Set();
  	for (const appointment of response.items) {
  		this.appointments.push(appointment);
  		nurseIds.add(appointment.nurseId);
  		vendorIds.add(appointment.vendorId);
  	}

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

  	this.serviceCodeService.list({filter: 'nurseId IN ' + nurseIdStr}, response => {
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

  	this.serviceCodeService.list({filter: 'vendorId IN ' + vendorIdStr}, response => {
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
    if (prop == 'assessmentApproved') {
    	this.appointments.splice(index, 1);
    	return;
    }

    this.appointments.splice(index, 1, updated);

    if (this.paystubEnabled) {
    	this.updatePaystub();
    }
  });
}

com.digitald4.iis.PayableCtrl.prototype.setApproved = function(appointment) {
	appointment.assessmentApproved = true;
	this.update(appointment, 'assessmentApproved');
}

com.digitald4.iis.PayableCtrl.prototype.updateExport = function() {
	this.exportedDisabled = true;
	for (var i = 0; i < this.appointments.length; i++) {
    var payable = this.appointments[i];
    if (payable.selected) {
    	this.exportedDisabled = false;
    	return;
    }
	}
}

com.digitald4.iis.PayableCtrl.prototype.exportSelected = function() {
	var qbExport = {appointmentIds: []};
	for (var i = this.appointments.length - 1; i >= 0; i--) {
		var appointment = this.appointments[i];
		if (appointment.selected) {
			qbExport.appointmentIds.push(appointment.id);
		}
	}

  this.quickBooksExportService.create(qbExport, _qbExport => {
    // Remove the appointments that were included in the export.
    for (var i = this.appointments.length - 1; i >= 0; i--) {
      if (this.appointments[i].selected) {
        this.appointments.splice(i, 1);
      }
    }
    this.fileReference = _qbExport.fileReference;
  });
}

com.digitald4.iis.PayableCtrl.prototype.updatePaystub = function() {
  var paystub = this.paystub || {};
  paystub.nurseId = this.entityId;
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
  for (var i = 0; i < this.appointments.length; i++) {
    var payable = this.appointments[i];
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
  paystub.netPay = paystub.taxable - paystub.taxTotal - paystub.postTaxDeduction + paystub.nonTaxWages;
  this.paystub = paystub;
}

com.digitald4.iis.PayableCtrl.prototype.createPaystub = function() {
  this.paystubService.create(this.paystub, paystub => {
    // Remove the appointments that were included in the paystub
    for (var i = this.appointments.length - 1; i >= 0; i--) {
      if (this.appointments[i].selected) {
        this.appointments.splice(i, 1);
      }
    }
    this.paystub = {};
  });
}