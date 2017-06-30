com.digitald4.iis.DashboardCtrl = function($scope, sharedData) {
  this.scope = $scope;
	this.sharedData = sharedData;
};

com.digitald4.iis.DashboardCtrl.prototype.refreshTables = function() {
  this.scope.TableType.UNCONFIRMED.refresh();
  this.scope.TableType.PENDING_ASSESSMENT.refresh();
};
