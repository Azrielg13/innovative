
com.digitald4.budget.module = angular.module('budget', []);

com.digitald4.budget.module.config(com.digitald4.budget.router);

com.digitald4.budget.module.service('RestService', com.digitald4.budget.Connector);
com.digitald4.budget.module.service('AccountService', com.digitald4.budget.AccountService);
com.digitald4.budget.module.service('BillService', com.digitald4.budget.BillService);

com.digitald4.budget.module.controller('DefaultViewCtrl', com.digitald4.budget.DefaultViewCtrl);
com.digitald4.budget.module.controller('AccountsCtrl', com.digitald4.budget.AccountsCtrl);
com.digitald4.budget.module.controller('ListCtrl', com.digitald4.budget.ListCtrl);
com.digitald4.budget.module.controller('AccountingCtrl', com.digitald4.budget.AccountingCtrl);

com.digitald4.budget.module.directive('enter', function() {
	return function(scope, element, attrs) {
		element.bind('mouseenter', function() {
			scope.doSomething();
		});
	};
});

com.digitald4.budget.module.directive('onChange', function() {
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
