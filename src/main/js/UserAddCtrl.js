com.digitald4.iis.UserAddCtrl = function($location, userService) {
  this.location = $location;
  this.userService = userService;
  this.userStatuses = enums.EmployeeStatus;
  this.user = {status: 'Active'};
}

com.digitald4.iis.UserAddCtrl.prototype.create = function() {
  this.error = this.user.firstName ? undefined : 'required';
  if (!this.error) {
    this.userService.create(this.user, user => this.location.path('/user/' + user.id));
  }
}