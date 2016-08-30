com.digitald4.iis.TableCtrl = function($scope, restService) {
	this.metadata = $scope.metadata;
	this.base = $scope.metadata.base;
	this.protoService = new com.digitald4.common.ProtoService(this.base.entity, restService);
	this.refresh();
};

com.digitald4.iis.TableCtrl.prototype.refresh = function() {
	this.protoService.list(this.metadata.request, function(entities) {
			this.entities = entities;
		}.bind(this), notify);
};

com.digitald4.iis.TableCtrl.prototype.update = function(entity, prop) {
  var index = this.entities.indexOf(entity);
  this.protoService.update(entity, prop, function(entity_) {
      this.entities.splice(index, 1, entity_);
    }.bind(this), notify);
};
