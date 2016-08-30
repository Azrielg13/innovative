com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'ui.calendar', 'ngMaterial']);

com.digitald4.iis.module.config(com.digitald4.iis.router);

com.digitald4.iis.module.service('restService', com.digitald4.common.JSONConnector);
com.digitald4.iis.module.service('generalDataService', com.digitald4.common.GeneralDataService);
com.digitald4.iis.module.service('nurseService', com.digitald4.iis.NurseService);

com.digitald4.iis.module.factory('sharedData', function() {
	return new com.digitald4.iis.SharedData();
});

com.digitald4.iis.module.controller('IISCtrl', com.digitald4.iis.IISCtrl);
com.digitald4.iis.module.controller('TableCtrl', com.digitald4.iis.TableCtrl);
com.digitald4.iis.module.controller('IntakeCtrl', com.digitald4.iis.IntakeCtrl);
com.digitald4.iis.module.controller('CalCtrl', com.digitald4.iis.CalCtrl);
com.digitald4.iis.module.controller('VendorCtrl', com.digitald4.iis.VendorCtrl);

com.digitald4.iis.module.directive('onChange', function() {
  return function(scope, element, attrs) {
  	var startingValue = element.val();
  	element.bind('blur', function() {
  		// console.log('evaluating: ' + startingValue + ' vs ' + element.val());
  		if (startingValue != element.val()) {
  			scope.$apply(attrs.onChange);
  			startingValue = element.val();
  		}
  	});
  };
});

com.digitald4.iis.module.directive('dd4Table', function() {
  return {
    restrict: 'A',
    replace: true,
    transclude: true,
    scope: {metadata: '=dd4Table',
    				rows: '=rowData'},
    controller: 'TableCtrl as tableCtrl',
    templateUrl: 'js/html/table.html'
  };
});

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

com.digitald4.iis.module.directive('mapauto', function() {
  return function(scope, element, attrs) {
		google.maps.event.addDomListener(window, 'load', addMapAutoComplete(element.get(0), function(place) {
			scope.place = place;
			scope.$apply(attrs.mapauto);
		}));
  };
});
