com.digitald4.iis.TableCtrl = function($scope, restService) {
	this.metadata = $scope.metadata;
	this.base = $scope.metadata.base;
	this.restService = restService;
	this.refresh();
};

com.digitald4.iis.TableCtrl.prototype.refresh = function() {
	this.restService.performRequest(this.base.url, this.metadata.request, function(rows) {
			this.rows = rows;
		}.bind(this), notify);
};
