com.digitald4.iis.DashboardCtrl = function($scope, globalData) {
  this.scope = $scope;
	this.globalData = globalData;
}

com.digitald4.iis.DashboardCtrl.prototype.refreshTables = function() {
  this.scope.TableType.PENDING_ASSESSMENT.refresh();
}
