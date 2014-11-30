com.digitald4.budget.ListCtrl = function($scope, BillService, AccountService) {
	this.scope = $scope;
	this.billService = BillService;
	this.accountService = AccountService;
	this.refresh();
};

com.digitald4.budget.ListCtrl.prototype.scope;

com.digitald4.budget.ListCtrl.prototype.billService;
com.digitald4.budget.ListCtrl.prototype.accountService;

com.digitald4.budget.ListCtrl.prototype.refresh = function() {
	var scope = this.scope;
	this.accountService.getBankAccounts(function(bankAccounts) {
		scope.bankAccounts = bankAccounts;
		scope.$apply();
	}, function(error) {
		notify(error);
	});
	this.accountService.getAccounts(function(accounts) {
		scope.accounts = accounts;
		scope.$apply();
	}, function(error) {
		notify(error);
	});
	this.scope.bills = this.billService.getBills();
};