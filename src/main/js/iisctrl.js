var AppointmentState = proto.iis.AppointmentStateUI;
var DAYS_30 = 1000 * 60 * 60 * 24 * 30;
com.digitald4.iis.GeneralData = com.digitald4.iis.GenData;

com.digitald4.iis.IISCtrl = function($scope, $filter, sharedData, userService, generalDataService) {
  this.userService = userService;
	userService.getActive(function(user) {
	  sharedData.setUser(user);
	}, notify);
  $scope.GenData = com.digitald4.iis.GenData;
  $scope.GeneralData = com.digitald4.iis.GeneralData;
  $scope.generalDataService = generalDataService;
	com.digitald4.iis.TableBaseMeta = {
			NURSES: {title: 'Nurses',
				entity: 'nurse',
				columns: [{title: 'Name', prop: 'fullName', getUrl: function(nurse) {
                    return '#nurse/' + nurse.id;
                  }},
				          {title: 'Status', getValue: function(nurse){return generalDataService.get(nurse.statusId).name;}},
				          {title: 'Phone #', prop: 'phoneNumber'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Address', getValue: function(nurse){
				            if (nurse.address) {
				              return nurse.address.address;
				            }
				            return '';
				          }},
				          {title: 'Pending Evaluations', prop: 'pendAssesCount'}]},
			LICENSE_ALERT: {title: 'License Expiration',
				entity: 'license',
				columns: [{title: 'Nurse', prop: 'nurseName',
				              getUrl: function(nurse){return '#nurse/' + nurse.id + '/licenses';}},
				          {title: 'License', getValue: function(license){
				            return generalDataService.get(license.licTypeId).name;
				          }},
				          {title: 'Status', getValue: function(license){
				            return license.expirationDate < Date.now() ? 'Expired' : 'Warning';
				          }},
				          {title: 'Valid Date', prop: 'validDate', type: 'date'},
				          {title: 'Exp Date', prop: 'expirationDate', type: 'date'}]},
			PATIENTS: {title: 'Patients',
				entity: 'patient',
				columns: [{title: 'Name', prop: 'name', getUrl: function(patient){return '#patient/' + patient.id;}},
				          {title: 'Vendor', prop: 'billingVendorName'},
				          {title: 'RX', prop: 'rx'},
				          {title: 'Dianosis',
				              getValue: function(patient){return generalDataService.get(patient.dianosisId).name;}},
				          {title: 'Last Appointment', prop: 'lastAppointment'},
				          {title: 'Next Appointment', prop: 'nextAppointment'}]},
			PENDING_INTAKE: {title: 'Pending Intakes',
				entity: 'patient',
				columns: [{title: 'Name', prop: 'name', getUrl: function(patient){return '#patient/' + patient.id;}},
				          {title: 'Referral Source', prop: 'referralSourceId'},
				          {title: 'Billing Vendor', prop: 'billingId'},
				          {title: 'Dianosis',
				              getValue: function(patient){return generalDataService.get(patient.dianosisId).name;}},
				          {title: 'Referral Date', prop: 'referralDate', type: 'date'},
				          {title: 'Start Date', prop: 'startOfCareDate', type: 'date'}]},
			USERS: {title: 'Users',
				entity: 'user',
				columns: [{title: 'Name', prop: 'fullName', getUrl: function(user) {
				            return '#user/' + user.id;
				          }},
				          {title: 'Type', prop: 'typeId'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Disabled', prop: 'disabled'},
				          {title: 'Last Login', prop: 'lastLogin', type: 'datetime'},
				          {title: 'Notes', prop: 'notes'}]},
			VENDORS: {title: 'Vendors',
        entity: 'vendor',
        columns: [{title: 'Vendor', prop: 'name', getUrl: function(vendor){return '#vendor/' + vendor.id;}},
				          {title: 'Address', getValue: function(vendor){
				            if (vendor.address) {
				              return vendor.address.address;
				            }
				            return '';
				          }},
                  {title: 'Fax Number', prop: 'faxNumber'},
                  {title: 'Contact Name', prop: 'contactName'},
                  {title: 'Contact Phone', prop: 'contactNumber'},
                  {title: 'Pending Assessments', prop: 'pendAssesCount'}]},
			UNCONFIRMED: {title: 'Unconfirmed Appointments',
				entity: 'appointment',
				columns: [{title: 'Nurse', prop: 'nurseName',
				              getUrl: function(appointment){return '#nurse/' + appointment.nurseId + '/unconfirmed';}},
				          {title: 'Patient', prop: 'patientId'},
				          {title: 'Start Time', prop: 'start', type: 'datetime'},
				          {title: 'Contact Info', getValue: function(appointment) {
				          	return appointment.nurse;}},
				          {title: 'Confirmation Request', getValue: function(appointment) {
				          	return '<button>Send Request</button>'}},
				          {title: 'Confirm', getValue: function(appointment) {
				          	return '<button>Set Confirmed</button>'}}]},
			PENDING_ASSESSMENT: {title: 'Pending Assessment',
				entity: 'appointment',
				columns: [{title: 'Patient', prop: 'patientName',
				              getUrl: function(appointment){return '#assessment/' + appointment.id;}},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Time In', getValue: function(appointment) {
				          	return $filter('date')(appointment.timeIn || appointment.start, 'shortTime');}},
				          {title: 'Time Out', getValue: function(appointment) {
				          	return $filter('date')(appointment.timeOut || appointment.end, 'shortTime');}},
				          {title: 'Percent Complete', getValue: function(appointment) {
				          	appointment.assessmentEntry = appointment.assessmentEntry || [];
				          	return (appointment.assessmentEntry.length / 25) + '%';}}]},
			REVIEWABLE: {title: 'Awaiting Review',
				entity: 'appointment',
				columns: [{title: 'Patient', prop: 'patientName',
				              getUrl: function(appointment){return '#assessment/' + appointment.id;}},
				          {title: 'Nurse', prop: 'nurseName'},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Hours', prop: 'payHours'},
				          {title: 'Mileage', prop: 'mileage'},
				          {title: 'Percent Complete', getValue: function(appointment) {
				          	appointment.assessmentEntry = appointment.assessmentEntry || [];
				          	return (appointment.assessmentEntry.length / 25) + '%';}}]},
			BILLABLE: {title: 'Billable',
				entity: 'appointment',
				columns: [{title: 'Vendor', prop: 'vendorName',
				              getUrl: function(appointment) {
				                appointment.billingInfo = appointment.billingInfo || {};
				                appointment.billingHours = appointment.billingInfo.hours;
				                appointment.billingRate = appointment.billingInfo.hourlyRate;
				                appointment.billingFlat = appointment.billingInfo.flatRate;
				                appointment.billingMileage = appointment.billingInfo.mileageTotal;
				                appointment.billingTotal = appointment.billingInfo.total;
				                return '#vendor/' + appointment.vendorId + '/billable';
				              }},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Billing Hours', prop: 'billingHours'},
				          {title: 'Billing Rate', prop: 'billingRate', type: 'currency'},
				          {title: 'Visit Pay', prop: 'billingFlat', type: 'currency'},
				          {title: 'Billing Mileage', prop: 'billingMileage', type: 'currency'},
				          {title: 'Total Payment', prop: 'billingTotal', type: 'currency'}]},
			PAYABLE: {title: 'Payable',
				entity: 'appointment',
				columns: [{title: 'Nurse', prop: 'nurseName',
				              getUrl: function(appointment) {
				                appointment.paymentInfo = appointment.paymentInfo || {};
				                appointment.payHours = appointment.paymentInfo.hours;
				                appointment.payRate = appointment.paymentInfo.hourlyRate;
				                appointment.payFlat = appointment.paymentInfo.flatRate;
				                appointment.payMileage = appointment.paymentInfo.mileageTotal;
				                appointment.payTotal = appointment.paymentInfo.total;
				                return '#nurse/' + appointment.nurseId + '/payable';
				          }},
				          {title: 'Date', prop: 'start', type: 'date'},
				          {title: 'Hours', prop: 'payHours'},
				          {title: 'Pay Rate', prop: 'payRate', type: 'currency'},
				          {title: 'Visit Pay', prop: 'payFlat', type: 'currency'},
				          {title: 'Mileage', prop: 'payMileage', type: 'currency'},
				          {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]},
			UNPAID_INVOICES: {title: 'Unpaid Invoices',
				entity: 'invoice',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(invoice) {return '#vendor/' + invoice.vendorId + '/invoices';}},
				          {title: 'Date', prop: 'generationTime', type: 'date'},
				          {title: 'Billed', prop: 'totalDue', type: 'currency'},
				          {title: 'Status', prop: 'statusId'},
				          {title: 'Comment', prop: 'comment', editable: true},
				          {title: 'Received', prop: 'totalPaid', editable: true}]},
			PAID_INVOICES: {title: 'Paid Invoices',
				entity: 'invoice',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(invoice){return '#vendor/' + invoice.vendorId + '/invoices';}},
				          {title: 'Date', prop: 'generationTime', type: 'date'},
				          {title: 'Billed', prop: 'totalDue', type: 'currency'},
				          {title: 'Status', prop: 'statusId'},
				          {title: 'Comment', prop: 'comment', editable: true},
				          {title: 'Received', prop: 'totalPaid', editable: true}]},
			PAY_HISTORY: {title: 'Pay History',
				entity: 'paystub',
				columns: [{title: 'Nurse', prop: 'nurseName',
                      getUrl: function(paystub){return '#nurse/' + paystub.nurseId + '/payHistory';}},
				          {title: 'Pay Date', prop: 'payDate', type: 'date',
				              imageLink: {src: 'images/icons/fugue/document-pdf.png',
				                  getUrl: function(paystub){return 'api/files/' + paystub.dataFile.id + '/' + paystub.dataFile.name}}},
				          {title: 'Gross', prop: 'grossPay', type: 'currency'},
				          {title: 'Deductions', getValue: function(paystub) {
				            return $filter('currency')((paystub.preTaxDeductions || 0) + (paystub.postTaxDeductions || 0));
				          }},
				          {title: 'Taxes', prop: 'taxTotal', type: 'currency'},
				          {title: 'Mileage Reimbursement', prop: 'payMileage', type: 'currency'},
                  {title: 'Net Pay', prop: 'netPay', type: 'currency'}]}
	};
	$scope.TableType = {
			NURSES: {base: com.digitald4.iis.TableBaseMeta.NURSES},
			LICENSE_ALERT: {base: com.digitald4.iis.TableBaseMeta.LICENSE_ALERT,
			    filter: {'expiration_date': '<' + (Date.now() + DAYS_30)}},
			PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS},
			USERS: {base: com.digitald4.iis.TableBaseMeta.USERS},
			VENDORS: {base: com.digitald4.iis.TableBaseMeta.VENDORS},
			PENDING_INTAKE: {base: com.digitald4.iis.TableBaseMeta.PENDING_INTAKE,
			    filter: {'referral_resolution_id': '885'}},
			UNCONFIRMED: {base: com.digitald4.iis.TableBaseMeta.UNCONFIRMED,
			    filter: {'state': AppointmentState.AS_UNCONFIRMED}},
			PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
				  filter: {'state': AppointmentState.AS_PENDING_ASSESSMENT}},
		  REVIEWABLE: {base: com.digitald4.iis.TableBaseMeta.REVIEWABLE,
				  filter: {'state': AppointmentState.AS_PENDING_APPROVAL}},
		  BILLABLE: {base: com.digitald4.iis.TableBaseMeta.BILLABLE,
				  filter: {'state': '>=' + AppointmentState.AS_BILLABLE,
                   'state_1': '<=' + AppointmentState.AS_BILLABLE_AND_PAYABLE}},
			PAYABLE: {base: com.digitald4.iis.TableBaseMeta.PAYABLE,
      		filter: {'state': '>=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
      		         'state_1': '<=' + AppointmentState.AS_PAYABLE}},
			UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.UNPAID_INVOICES,
				  filter: {'status_id': '1521'}},
			PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES,
				  filter: {'status_id': '1520'}},
			PAY_HISTORY: {base: com.digitald4.iis.TableBaseMeta.PAY_HISTORY}
	};
	this.sharedData = sharedData;
};

com.digitald4.iis.IISCtrl.prototype.logout = function() {
  this.userService.logout();
};