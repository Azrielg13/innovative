com.digitald4.iis.DashboardCtrl = function($scope, globalData) {
  this.scope = $scope;
	this.globalData = globalData;
}

com.digitald4.iis.DashboardCtrl.prototype.refreshTables = function() {
  this.scope.TableType.UNCONFIRMED.refresh();
  // this.scope.TableType.PENDING_ASSESSMENT.refresh();
}
