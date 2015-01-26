com.digitald4.budget.BillService = function(RestService) {
	this.restService = RestService;
};

com.digitald4.budget.BillService.prototype.restService;

com.digitald4.budget.BillService.prototype.getBills = function(startDate, endDate, successCallback, errorCallback) {
	this.restService.performRequest({action: 'getBills', startDate: startDate, endDate: endDate}, successCallback, errorCallback);
};

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
	this.restService.performRequest(request, successCallback, errorCallback);
};

com.digitald4.budget.BillService.prototype.addBill = function(newBill, successCallback, errorCallback) {
	newBill.action = 'addBill';
	this.restService.performRequest(newBill, successCallback, errorCallback);
};

com.digitald4.budget.BillService.prototype.updateBill = function(bill, property, successCallback, errorCallback) {
	var request = {action: 'updateBill',
			id: bill.id,
			property: property,
			value: bill[property]};
	this.restService.performRequest(request, successCallback, errorCallback);
};

com.digitald4.budget.BillService.prototype.updateBillTrans = function(billTrans, property, successCallback, errorCallback) {
	var request = {action: 'updateBillTrans',
			id: billTrans.id,
			billId: billTrans.billId,
			accountId: billTrans.accountId,
			property: property,
			value: billTrans[property]};
	this.restService.performRequest(request, successCallback, errorCallback);
};

// 2504 W Cypress St Compton, CA 90220