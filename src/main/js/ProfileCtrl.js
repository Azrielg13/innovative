com.digitald4.iis.ProfileCtrl = function($routeParams, $filter, flags, globalData,
    generalDataService, licenseService, nurseService, userService) {
  this.filter = $filter;
  this.flags = flags;
  this.user = globalData.activeSession.user;
  this.generalDataService = generalDataService;
  this.licenseService = licenseService;
  this.nurseService = nurseService;
  this.userService = userService;
  this.tabs = {
    general: {name: 'General', isEnabled: () => true},
    notes: {name: 'Notes', isEnabled: () => flags.userNotesEnabled},
    licenses: {name: 'Licenses', isEnabled: () => flags.licenseAlertEnabled},
    changeHistory: {name: 'Change History', isEnabled: () => flags.userChangeHistoryEnabled}
  }
  this.TableType = {
    NOTES: {
      base: com.digitald4.iis.TableBaseMeta.NOTES,
      filter: 'entityType=User,entityId=' + this.userId},
    CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=User,entityId=' + this.userId}
  }
	this.setSelectedTab(this.tabs[$routeParams.tab] || ($routeParams.tab || this.tabs.general));
}

com.digitald4.iis.ProfileCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
}