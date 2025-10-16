com.digitald4.iis.AttachmentsCtrl = function($scope, appointmentService, fileService, globalData) {
  this.$scope = $scope;
  this.appointmentService = appointmentService;
  this.fileService = fileService;
  this.role = globalData.activeSession.user.role;
  if (!this.appointment) {
    appointmentService.get(this.id, appointment => this.appointment = appointment);
  }
}

com.digitald4.iis.AttachmentsCtrl.prototype.showUploadDialog = function() {
	this.uploadDialogShown = true;
}

com.digitald4.iis.AttachmentsCtrl.prototype.closeUploadDialog = function() {
	this.uploadDialogShown = false;
}

com.digitald4.iis.AttachmentsCtrl.prototype.uploadFile = function() {
  var file = document.getElementById('file');
  var request = {file: file, entityType: 'Appointment', entityId: this.appointment.id};
  this.fileService.upload(request, fileReference => {
    this.appointment.attachments = this.appointment.attachments || [];
    this.appointment.attachments.push(fileReference);
    this.closeUploadDialog();
    this.$scope.$apply();
  });
}

com.digitald4.iis.AttachmentsCtrl.prototype.removeAttachment = function(attachment) {
  var attachments = this.appointment.attachments;
  this.appointmentService.removeAttachment(this.appointment.id, attachment.id, response => {
    attachments.splice(attachments.indexOf(attachment), 1);
  });
}

com.digitald4.iis.AttachmentsCtrl.prototype.getFileUrl = function(fileReference, type) {
  return this.appointmentService.getFileUrl(fileReference, type);
}

com.digitald4.iis.AttachmentsCtrl.prototype.isEditable = function(field) {
  var appointment = this.appointment;
  var role = this.role;

  return appointment && (appointment.state == 'PENDING_ASSESSMENT' || (role != 'Nurse' && appointment.state == 'PENDING_APPROVAL'));
}