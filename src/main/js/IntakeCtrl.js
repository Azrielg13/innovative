com.digitald4.iis.IntakeCtrl = function($location, patientService, vendorService) {
	this.location = $location;
  this.patientService = patientService;
  this.vendorService = vendorService;
  this.refresh();
  this.patient = {serviceAddress: {}, primaryPhone: {}, alternatePhone: {}, emergencyContactPhone: {}};
  this.saveAction = 'save_only';
}

com.digitald4.iis.IntakeCtrl.prototype.refresh = function() {
  this.vendorService.listAsIds({filter: 'status=Active', orderBy: 'name'}, response => {
    this.vendors = response.items;
    this.referralList = [];
    for (var i = 0; i < 60 && i < this.vendors.length; i++) {
      this.referralList.push(this.vendors[i]);
    }
  });
}

com.digitald4.iis.IntakeCtrl.prototype.create = function() {
  this.errorMessage = this.patient.billingVendorId ? undefined : 'Please fill all required fields';
  if (!this.errorMessage) {
    this.patient.status = this.saveAction == 'save_and_accept' ? 'Active' : 'Pending';
    this.patientService.create(this.patient, patient => {
      this.location.path('/patient/' + patient.id);
    });
  }
}