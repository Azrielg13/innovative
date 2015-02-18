var ONE_DAY = 1000 * 60 * 60 * 24;
var ONE_HOUR = 1000 * 60 * 60;
com.digitald4.budget.CalCtrl = function($scope, $filter, SharedData, BillService, AccountService) {
	this.scope = $scope;
	this.dateFilter = $filter('date');
	this.sharedData = SharedData;
	this.scope.sharedData = SharedData;
	this.scope.sharedData.refresh = this.refresh.bind(this);
	this.billService = BillService;
	this.accountService = AccountService;
	this.scope.billsSuccessCallback = this.billsSuccessCallback.bind(this);
	this.scope.showAddBillDialog = this.showAddBillDialog.bind(this);
	this.scope.closeAddBillDialog = this.closeAddBillDialog.bind(this);
	this.scope.editBill = this.editBill.bind(this);
	this.scope.closeEditBillDialog = this.closeEditBillDialog.bind(this);
	this.scope.addBill = this.addBill.bind(this);
	this.scope.updateBill = this.updateBill.bind(this);
	this.scope.updateBillTrans = this.updateBillTrans.bind(this);
	this.refresh();
};

com.digitald4.budget.CalCtrl.prototype.scope;

com.digitald4.budget.CalCtrl.prototype.billService;
com.digitald4.budget.CalCtrl.prototype.accountService;

com.digitald4.budget.CalCtrl.prototype.setupCalendar = function() {
	var month = this.scope.sharedData.getMonth();
	var currDay = new Date(this.scope.sharedData.getStartDateCal());
	var weeks = [];
	var woy = 1;
	var days = {};
	do {
		var week = {
			weekOfYear: woy++
		};
		var weekdays = [];
		for (var d = 0; d < 7; d++) {
			var day = {
				date: currDay,
				weekend: (d == 0 || d == 6),
				otherMonth: currDay.getMonth() != month.getMonth(),
				bills: []
			};
			weekdays.push(day);
			days[day.date.getTime()] = day;
			currDay = addDay(currDay);
		}
		week.days = weekdays;
		weeks.push(week);
	} while (currDay.getMonth() == month.getMonth());
	this.scope.days = days;
	this.scope.weeks = weeks;
};

addDay = function(date) {
	var ret = new Date(date.getTime() + ONE_DAY);
	if (ret.getDay() == date.getDay() || ret.getHours() < date.getHours()) {
		ret.setTime(ret.getTime() + ONE_HOUR);
	} else if (ret.getHours() > date.getHours()) {
		ret.setTime(ret.getTime() - ONE_HOUR);
	}
	return ret;
};

com.digitald4.budget.CalCtrl.prototype.refresh = function() {
	this.setupCalendar();
	var scope = this.scope;
	var s = this;
	
	this.accountService.getAccounts(this.sharedData.activePortfolioId, function(accounts) {
		scope.accounts = accounts;
		scope.$apply();
	}, function(error) {
		notify(error);
	});
	
	this.billService.getBills(this.scope.sharedData.getStartDateCal().toJSON(),
			this.scope.sharedData.getEndDateCal().toJSON(),
			scope.billsSuccessCallback, function(error) {
		notify(error);
	});
};

com.digitald4.budget.CalCtrl.prototype.billsSuccessCallback = function(bills) {
	var scope = this.scope;
	for (var d in scope.days) {
		scope.days[d].bills = [];
	}
	scope.bills = bills;
	for (var t = 0; t < bills.length; t++) {
		var bill = bills[t];
		var day = scope.days[Date.parse(bill.dueDate)];
		if (day) {
			day.bills.push(bill);
		}
	}
	scope.closeAddBillDialog();
	scope.$apply();
};

com.digitald4.budget.CalCtrl.prototype.showAddBillDialog = function(date) {
	this.scope.newBill = {dueDate: this.dateFilter(date, 'MM/dd/yyyy')};
	this.scope.addDialogShown = true;
};

com.digitald4.budget.CalCtrl.prototype.closeAddBillDialog = function() {
	this.scope.addDialogShown = false;
};

com.digitald4.budget.CalCtrl.prototype.addBill = function() {
	var scope = this.scope;
	scope.billAddError = undefined;
	this.billService.addBill(scope.newBill, scope.billsSuccessCallback, function(error) {
		scope.billAddError = error;
		scope.$apply();
	});
};

com.digitald4.budget.CalCtrl.prototype.editBill = function(bill) {
	this.scope.eBill = bill;
	this.scope.editDialogShown = true;
};

com.digitald4.budget.CalCtrl.prototype.closeEditBillDialog = function() {
	this.scope.editDialogShown = false;
};

com.digitald4.budget.CalCtrl.prototype.updateBill = function(property) {
	var scope = this.scope;
	scope.billUpdateError = undefined;
	this.billService.updateBill(scope.eBill, property, scope.billsSuccessCallback, function(error) {
		scope.billUpdateError = error;
		scope.$apply();
	});
};

com.digitald4.budget.CalCtrl.prototype.updateBillTrans = function(billTrans, property) {
	var scope = this.scope;
	scope.billUpdateError = undefined;
	this.billService.updateBillTrans(billTrans, property, scope.billsSuccessCallback,
			function(error) {
		scope.billUpdateError = error;
		scope.$apply();
	});
};