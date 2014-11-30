com.digitald4.budget.BillService = function(RestService) {
	this.restService = RestService;
};

com.digitald4.budget.BillService.prototype.restService;

com.digitald4.budget.BillService.prototype.getBills = function() {
	return [{id: '2', name: 'Best Buy', dueDate: '2014-11-05', amountDue: '-$35.00', accounts: [{effect: '', balance: '$2700.00'}, {effect: '-$35.00', balance: '$3065.00'}, {effect: '', balance: '$2050.00'}]},
		  {id: '3', name: 'Condo', dueDate: '2014-11-12', amountDue: '-$1177.52', accounts: [{effect: '', balance: '$2700.00'}, {effect: '', balance: '$3065.00'}, {effect: '-$1177.52', balance: '$872.48'}]},
		  {id: '4', name: 'Wells Fargo', dueDate: '2014-11-16', amountDue: '-$2656.48', accounts: [{effect: '-$2656.48', balance: '$43.52'}, {effect: '', balance: '$3065.00'}, {effect: '', balance: '$872.48'}]},
		  {id: '5', name: 'Pay Check', dueDate: '2014-11-20', amountDue: '$2674.14', accounts: [{effect: '$2674.14', balance: '$2717.66'}, {effect: '', balance: '$3065.00'}, {effect: '', balance: '$872.48'}]},
		  {id: '1', name: 'Capital One', dueDate: '2014-11-27', amountDue: '-$1250.00', accounts: [{effect: '-$1250.00', balance: '$1467.66'}, {effect: '', balance: '$3065.00'}, {effect: '', balance: '$872.48'}]}];
};

var transactions = [{id: '2', name: 'Best Buy Payment', debitAccountId: 1, debitAccountBal: '2700.00', creditAccountId: 4, creditAccountBal: '5100.00', date: '2014-11-05', amount: '35.00'},
              		  {id: '3', name: 'Condo Payment', debitAccountId: 2, debitAccountBal: '3100.00', creditAccountId: 5, date: '2014-11-12', amount: '1177.52'},
              		  {id: '4', name: 'Mortgage Payment', debitAccountId: 1, creditAccountId: 6, date: '2014-11-16', amount: '2656.48'},
              		  {id: '5', name: 'Pay Check', debitAccountId: 7, creditAccountId: 1, date: '2014-11-20', amount: '2674.14'},
              		  {id: '1', name: 'Capital One Payment', debitAccountId: 1, creditAccountId: 8, date: '2014-11-27', amount: '1250.00'}];

com.digitald4.budget.BillService.prototype.getTransactions = function(successCallback, errorCallback) {
	this.restService.performRequest({action: 'getTransactions'}, successCallback, errorCallback);
};

com.digitald4.budget.BillService.prototype.addTransaction = function(newTrans, successCallback, errorCallback) {
	newTrans.action = 'addTransaction';
	this.restService.performRequest(newTrans, successCallback, errorCallback);
};

com.digitald4.budget.BillService.prototype.updateTransaction = function(trans, property, successCallback, errorCallback) {
	var request = {action: 'updateTransaction',
			id: trans.id,
			property: property,
			value: trans[property]};
	this.restService.performRequest(request, function(transactions) {
		successCallback(transactions);
	}, errorCallback);
};

// 2504 W Cypress St Compton, CA 90220