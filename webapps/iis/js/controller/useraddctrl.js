com.digitald4.iis.UserAddCtrl = function(userService) {
  this.userService = userService;
  this.statuses = [
      {id: 0, name: 'Unknown'},
      {id: 1, name: 'Standard'},
      {id: 2, name: 'Admin'}];
  this.user = {type: 1};
};

com.digitald4.iis.UserAddCtrl.prototype.create = function() {
  this.error = this.nurse.first_name ? undefined : 'required';
  var type = user.type;
  if (!this.nameError) {
    this.userService.create(this.user, function(user) {
      this.lastAdded = user;
      this.message = 'User added';
      this.user = {type: type};
    }.bind(this), notify);
  }
};