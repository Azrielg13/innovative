com.digitald4.iis.IntakeCtrl = function($location, patientService, vendorService) {
	this.location = $location;
  this.patientService = patientService;
  this.vendorService = vendorService;
  this.refresh();
  this.patient = {serviceAddress: {}, primaryPhone: {}, alternatePhone: {}, emergencyContactPhone: {}};
}

com.digitald4.iis.IntakeCtrl.prototype.refresh = function() {
  this.vendorService.list({filter: 'status=Active'}, response => {this.vendors = response.items});
}

com.digitald4.iis.IntakeCtrl.prototype.create = function() {
  this.errorMessage = this.patient.firstName && this.patient.lastName && this.patient.billingVendorId ? undefined : 'Please fill all required fields';
  if (!this.errorMessage) {
    this.patientService.create(this.patient, patient => {
      this.location.path('/patient/' + patient.id);
    });
  }
}