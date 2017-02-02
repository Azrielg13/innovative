com.digitald4.iis.NurseAddCtrl = function(restService) {
  this.nurseService = new com.digitald4.common.ProtoService('nurse', restService);
  this.nurse = {};
};

com.digitald4.iis.NurseAddCtrl.prototype.create = function() {
  this.nameError = this.nurse.first_name ? undefined : 'required';
  if (!this.nameError) {
    this.nurseService.create(this.nurse, function(nurse) {
      this.lastAdded = nurse;
      this.message = 'Nurse added';
      this.nurse = {};
    }.bind(this), notify);
  }
};
