var app = angular.module('iis', []);

app.controller('AppCtrl', function($scope) {
	$scope.doSomething = function() {
		console.log('hello');
	};
});

app.controller('LicensesCtrl', function($scope, $attrs) {
	$scope.nurseId = $attrs.licenses;
	var nurseStream = new com.digitald4.common.connector.NurseStream();
	
	nurseStream.get($scope.nurseId, function(nurse) {
		$scope.nurse = nurse;
	});
	
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

app.directive('enter', function() {
	return function(scope, element, attrs) {
		element.bind('mouseenter', function() {
			scope.doSomething();
		});
	};
});

app.directive('licenses', function() {
	return {
		controller: 'LicensesCtrl',
		templateUrl: 'licenses.html'
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

app.directive('customEvent', function() {
    return function(scope, element, attrs) {
        var dest = attrs.customEvent;

        $(element[0]).onChange(function(e) {
            e.preventDefault();

            // on the event, copy the contents of model
            // to the destination variable
            scope.$apply(dest);

            //if (!scope.$$phase)
              //  scope.apply();
        });
    }
});


