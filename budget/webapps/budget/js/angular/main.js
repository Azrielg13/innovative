var app = angular.module('iis', []);

app.controller('AppCtrl', function($scope) {
	$scope.doSomething = function() {
		console.log('hello');
	};
});

app.controller('NurseGeneralCtrl', function($scope, $attrs) {
	$scope.nurseId = $attrs.nurseGeneral;
	var nurseStream = new com.digitald4.common.connector.NurseStream();
	
	nurseStream.get($scope.nurseId, function(nurse) {
		$scope.nurse = nurse;
		$scope.$apply();
	});
	
	$scope.update = function(object) {
		console.log('update ' + object);
		nurseStream.updateObject(object, function() {
			console.log('update complete');
		}, function(error) {
			console.log('error: ' + error);
		});
	};
});

app.controller('LicensesCtrl', function($scope, $attrs) {
	$scope.nurseId = $attrs.licenses;
	var nurseStream = new com.digitald4.common.connector.NurseStream();
	
	nurseStream.getLicenses($scope.nurseId, function(licenses) {
		$scope.licenses = licenses;
		$scope.$apply();
	});
	
	$scope.update = function(object) {
		console.log('update ' + object);
		nurseStream.updateObject(object, function() {
			console.log('update complete');
		}, function(error) {
			console.log('error: ' + error);
		});
	};
});

app.controller('PendAssCtrl', function($scope, $attrs) {
	$scope.nurseId = $attrs.pendass;
	var nurseStream = new com.digitald4.common.connector.NurseStream();
	
	nurseStream.getPendAsses($scope.nurseId, function(pendAsses) {
		$scope.pendAsses = pendAsses;
		$scope.$apply();
	});
	
	$scope.update = function(object) {
		console.log('update ' + object);
		nurseStream.updateObject(object, function() {
			console.log('update complete');
		}, function(error) {
			console.log('error: ' + error);
		});
	};
});

app.directive('enter', function() {
	return function(scope, element, attrs) {
		element.bind('mouseenter', function() {
			scope.doSomething();
		});
	};
});

app.directive('nurseGeneral', function() {
	return {
		controller: 'NurseGeneralCtrl',
		templateUrl: 'html/nurse/general.html'
	};
});

app.directive('licenses', function() {
	return {
		controller: 'LicensesCtrl',
		templateUrl: 'html/nurse/licenses.html'
	};
});

app.directive('pendass', function() {
	return {
		controller: 'PendAssCtrl',
		templateUrl: 'html/nurse/pendass.html'
	};
});

app.directive('onChange', function() {
    return function(scope, element, attrs) {
    	var stValue = element.val();
    	element.bind('blur', function() {
    		if (stValue != element.val()) {
    			scope.$apply(attrs.onChange);
    			stValue = element.val();
    		}
    	});
    };
});
