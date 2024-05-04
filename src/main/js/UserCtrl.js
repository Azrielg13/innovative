com.digitald4.iis.UserCtrl = function($routeParams, noteService, userService) {
  this.userId = parseInt($routeParams.id, 10);
  this.noteService = noteService;
  this.userService = userService;
  this.tabs = com.digitald4.iis.UserCtrl.TABS;
  this.TableType = {
    NOTES: {
      base: com.digitald4.iis.TableBaseMeta.NOTES,
      filter: 'entityType=User,entityId=' + this.userId},
    CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=User,entityId=' + this.userId}
  }
	this.refresh();
	this.setSelectedTab(this.tabs[$routeParams.tab] || ($routeParams.tab || this.tabs.general));
}

com.digitald4.iis.UserCtrl.TABS = {
	general: 'General',
	notes: 'Notes',
	changeHistory: 'Change History'
}

com.digitald4.iis.UserCtrl.prototype.refresh = function() {
  this.userService.get(this.userId, user => {this.user = user});
}

com.digitald4.iis.UserCtrl.prototype.setPassword = function() {
  if (this.password != this.confirmation) {
    alert('Confirmation does not match!');
    return;
  }

  this.userService.setPassword(this.userId, this.password, response => {alert('Password updated successfully')});
}

com.digitald4.iis.UserCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
}

com.digitald4.iis.UserCtrl.prototype.update = function(prop) {
	this.userService.update(this.user, [prop], user => {this.user = user});
}
