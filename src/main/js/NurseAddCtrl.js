com.digitald4.iis.NurseAddCtrl = function($location, flags, nurseService) {
  this.location = $location;
  this.flags = flags;
  this.nurseService = nurseService;
  this.nurse = {address: {}, mileageRate: .5};
}

com.digitald4.iis.NurseAddCtrl.prototype.create = function() {
  this.nameError = this.nurse.firstName ? undefined : 'required';
  if (!this.nameError) {
    this.nurseService.create(this.nurse, nurse => this.location.path('/nurse/' + nurse.id));
  }
}
