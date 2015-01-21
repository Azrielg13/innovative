com.digitald4.budget.ListCtrl = function($scope, BillService, AccountService) {
	this.scope = $scope;
	this.billService = BillService;
	this.accountService = AccountService;
	this.scope.addBill = this.addBill.bind(this);
	this.scope.updateBill = this.updateBill.bind(this);
	this.scope.updateBillTrans = this.updateBillTrans.bind(this);
	this.refresh();
};

com.digitald4.budget.ListCtrl.prototype.scope;

com.digitald4.budget.ListCtrl.prototype.billService;
com.digitald4.budget.ListCtrl.prototype.accountService;

com.digitald4.budget.ListCtrl.prototype.makeNew = function() {
	var newBill = {'accounts': []};
	for (var x = 0; x < this.scope.bankAccounts.length; x++) {
		var ba = this.scope.bankAccounts[x];
		newBill.accounts.push({'id': ba.id});
	}
	this.scope.newBill = newBill;
};

com.digitald4.budget.ListCtrl.prototype.refresh = function() {
	var scope = this.scope;
	var s = this;
	this.accountService.getBankAccounts(function(bankAccounts) {
		scope.bankAccounts = bankAccounts;
		s.makeNew();
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
	this.billService.getBills(function(bills) {
		scope.bills = bills;
		scope.$apply();
	}, function(error) {
		notify(error);
	});
};

com.digitald4.budget.ListCtrl.prototype.addBill = function() {
	var scope = this.scope;
	var s = this;
	scope.billAddError = undefined;
	this.billService.addBill(scope.newBill, function(bills) {
		scope.bills = bills;
		s.makeNew();
		scope.$apply();
	}, function(error) {
		scope.billAddError = error;
		scope.$apply();
	});
};

com.digitald4.budget.ListCtrl.prototype.updateBill = function(bill, property) {
	var scope = this.scope;
	scope.billUpdateError = undefined;
	this.billService.updateBill(bill, property, function(bills) {
		scope.bills = bills;
		scope.$apply();
	}, function(error) {
		scope.billUpdateError = error;
		scope.$apply();
	});
};

com.digitald4.budget.ListCtrl.prototype.updateBillTrans = function(billTrans, property) {
	var scope = this.scope;
	scope.billUpdateError = undefined;
	this.billService.updateBillTrans(billTrans, property, function(bills) {
		scope.bills = bills;
		scope.$apply();
	}, function(error) {
		scope.billUpdateError = error;
		scope.$apply();
	});
};