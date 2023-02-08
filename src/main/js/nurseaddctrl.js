com.digitald4.iis.NurseAddCtrl = function(nurseService) {
  this.nurseService = nurseService;
  this.nurse = {address: {}};
};

com.digitald4.iis.NurseAddCtrl.prototype.create = function() {
  this.nameError = this.nurse.firstName ? undefined : 'required';
  if (!this.nameError) {
    this.nurseService.create(this.nurse, function(nurse) {
      this.lastAdded = nurse;
      this.message = 'Nurse added';
      this.nurse = {address: {}};
    }.bind(this), notifyError);
  }
};
