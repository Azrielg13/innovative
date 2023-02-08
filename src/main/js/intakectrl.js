com.digitald4.iis.IntakeCtrl = function(patientService, vendorService) {
  this.patientService = patientService;
  this.vendorService = vendorService;
  this.refresh();
  this.patient = {serviceAddress: {}, primaryPhone: {}, alternatePhone: {}, emergencyContactPhone: {}};
}

com.digitald4.iis.IntakeCtrl.prototype.refresh = function() {
  this.vendorService.list({}, response => {this.vendors = response.items}, notifyError);
}

com.digitald4.iis.IntakeCtrl.prototype.create = function() {
  this.nameError = this.patient.name ? undefined : 'required';
  if (!this.nameError) {
    this.patientService.create(this.patient, patient => {
      this.lastAdded = patient;
      this.message = 'Patient added';
      this.patient = {serviceAddress: {}, primaryPhone: {}, alternatePhone: {}, emergencyContactPhone: {}};
    }, notifyError);
  }
}