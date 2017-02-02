com.digitald4.iis.IntakeCtrl = function(restService) {
  this.patientService = new com.digitald4.common.ProtoService('patient', restService);
  this.vendorService = new com.digitald4.common.ProtoService('vendor', restService);
  this.refresh();
  this.patient = {};
};

com.digitald4.iis.IntakeCtrl.prototype.patientService;
com.digitald4.iis.IntakeCtrl.prototype.vendorService;

com.digitald4.iis.IntakeCtrl.prototype.refresh = function() {
  this.vendorService.list([], function(vendors) {
    this.vendors = vendors;
  }.bind(this), notify);
};

com.digitald4.iis.IntakeCtrl.prototype.create = function() {
  this.nameError = this.patient.name ? undefined : 'required';
  if (!this.nameError) {
    this.patientService.create(this.patient, function(patient) {
      this.lastAdded = patient;
      this.message = 'Patient added';
      this.patient = {};
    }.bind(this), notify);
  }
};