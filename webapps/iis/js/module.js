com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'ui.calendar', 'ngMaterial']);

com.digitald4.iis.module.config(com.digitald4.iis.router);

com.digitald4.iis.module.factory('sharedData', function() {
	return new com.digitald4.iis.SharedData();
});

com.digitald4.iis.module.controller('IISCtrl', com.digitald4.iis.IISCtrl);
com.digitald4.iis.module.controller('IntakeCtrl', com.digitald4.iis.IntakeCtrl);
com.digitald4.iis.module.controller('CalCtrl', com.digitald4.iis.CalCtrl);
com.digitald4.iis.module.controller('VendorCtrl', com.digitald4.iis.VendorCtrl);

com.digitald4.iis.module.directive('dd4Calendar', function() {
  return {
    restrict: 'A',
    replace: true,
    // transclude: true,
    // scope: {metadata: '=dd4Calendar'},
    controller: 'CalCtrl as calCtrl',
    template: '<div data-ui-calendar="calCtrl.uiConfig.calendar" data-ng-model="calCtrl.eventSources" class="span8 calendar"></div>'
  };
});
