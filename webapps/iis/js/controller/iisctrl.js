var AppointmentState = proto.iis.AppointmentStateUI;
var DAYS_30 = 1000 * 60 * 60 * 24 * 30;

com.digitald4.iis.IISCtrl = function($scope, $filter, sharedData, userService, generalDataService) {
  this.userService = userService;
	userService.getActive(function(user) {
	  sharedData.setUser(user);
	}, notify);
  $scope.GeneralData = com.digitald4.iis.GeneralData;
  $scope.generalDataService = generalDataService;
	com.digitald4.iis.TableBaseMeta = {
			NURSES: {title: 'Nurses',
				entity: 'nurse',
				columns: [{title: 'Name', prop: 'full_name', getUrl: function(nurse) {
                    return '#nurse/' + nurse.id;
                  }},
				          {title: 'Status', getValue: function(nurse){return generalDataService.get(nurse.status_id).name;}},
				          {title: 'Phone #', prop: 'phone_number'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Address', getValue: function(nurse){
				            if (nurse.address) {
				              return nurse.address.address;
				            }
				            return '';
				          }},
				          {title: 'Pending Evaluations', prop: 'pend_asses_count'}]},
			LICENSE_ALERT: {title: 'License Expiration',
				entity: 'license',
				columns: [{title: 'Nurse', prop: 'nurse_name',
				              getUrl: function(nurse){return '#nurse/' + nurse.id + '/licenses';}},
				          {title: 'License', getValue: function(license){
				            return generalDataService.get(license.lic_type_id).name;
				          }},
				          {title: 'Status', getValue: function(license){
				            return license.expiration_date < Date.now() ? 'Expired' : 'Warning';
				          }},
				          {title: 'Valid Date', prop: 'valid_date', type: 'date'},
				          {title: 'Exp Date', prop: 'expiration_date', type: 'date'}]},
			PATIENTS: {title: 'Patients',
				entity: 'patient',
				columns: [{title: 'Name', prop: 'name', getUrl: function(patient){return '#patient/' + patient.id;}},
				          {title: 'Vendor', prop: 'billing_vendor_name'},
				          {title: 'RX', prop: 'rx'},
				          {title: 'Dianosis',
				              getValue: function(patient){return generalDataService.get(patient.dianosis_id).name;}},
				          {title: 'Last Appointment', prop: 'last_appointment'},
				          {title: 'Next Appointment', prop: 'next_appointment'}]},
			PENDING_INTAKE: {title: 'Pending Intakes',
				entity: 'patient',
				columns: [{title: 'Name', prop: 'name', getUrl: function(patient){return '#patient/' + patient.id;}},
				          {title: 'Referral Source', prop: 'referral_source_id'},
				          {title: 'Billing Vendor', prop: 'billing_id'},
				          {title: 'Dianosis',
				              getValue: function(patient){return generalDataService.get(patient.dianosis_id).name;}},
				          {title: 'Referral Date', prop: 'referral_date', type: 'date'},
				          {title: 'Start Date', prop: 'start_of_care_date', type: 'date'}]},
			USERS: {title: 'Users',
				entity: 'user',
				columns: [{title: 'Name', prop: 'full_name', getUrl: function(user) {
				            return '#user/' + user.id;
				          }},
				          {title: 'Type', prop: 'type_id'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Disabled', prop: 'disabled'},
				          {title: 'Last Login', prop: 'last_login', type: 'datetime'},
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
                  {title: 'Fax Number', prop: 'fax_number'},
                  {title: 'Contact Name', prop: 'contact_name'},
                  {title: 'Contact Phone', prop: 'contact_number'},
                  {title: 'Pending Assessments', prop: 'pend_asses_count'}]},
			UNCONFIRMED: {title: 'Unconfirmed Appointments',
				entity: 'appointment',
				columns: [{title: 'Nurse', prop: 'nurse_name',
				              getUrl: function(appointment){return '#nurse/' + appointment.nurse_id + '/unconfirmed';}},
				          {title: 'Patient', prop: 'patient_id'},
				          {title: 'Start Time', prop: 'start', type: 'datetime'},
				          {title: 'Contact Info', getValue: function(appointment) {
				          	return appointment.nurse;}},
				          {title: 'Confirmation Request', getValue: function(appointment) {
				          	return '<button>Send Request</button>'}},
				          {title: 'Confirm', getValue: function(appointment) {
				          	return '<button>Set Confirmed</button>'}}]},
			PENDING_ASSESSMENT: {title: 'Pending Assessment',
				entity: 'appointment',
				columns: [{title: 'Patient', prop: 'patient_name',
				              getUrl: function(appointment){return '#assessment/' + appointment.id;}},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Time In', getValue: function(appointment) {
				          	return $filter('date')(appointment.time_in || appointment.start, 'shortTime');}},
				          {title: 'Time Out', getValue: function(appointment) {
				          	return $filter('date')(appointment.time_out || appointment.end, 'shortTime');}},
				          {title: 'Percent Complete', getValue: function(appointment) {
				          	appointment.assessment_entry = appointment.assessment_entry || [];
				          	return (appointment.assessment_entry.length / 25) + '%';}}]},
			REVIEWABLE: {title: 'Awaiting Review',
				entity: 'appointment',
				columns: [{title: 'Patient', prop: 'patient_name',
				              getUrl: function(appointment){return '#assessment/' + appointment.id;}},
				          {title: 'Nurse', prop: 'nurse_name'},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Hours', prop: 'pay_hours'},
				          {title: 'Mileage', prop: 'mileage'},
				          {title: 'Percent Complete', getValue: function(appointment) {
				          	appointment.assessment_entry = appointment.assessment_entry || [];
				          	return (appointment.assessment_entry.length / 25) + '%';}}]},
			BILLABLE: {title: 'Billable',
				entity: 'appointment',
				columns: [{title: 'Vendor', prop: 'vendor_name',
				              getUrl: function(appointment){return '#vendor/' + appointment.vendor_id + '/billable';}},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Billing Hours', prop: 'billed_hours'},
				          {title: 'Billing Rate', prop: 'billing_rate', type: 'currency'},
				          {title: 'Visit Pay', prop: 'billing_flat', type: 'currency'},
				          {title: 'Billing Mileage', prop: 'billing_mileage'},
				          {title: 'Total Payment', prop: 'billing_total', type: 'currency'}]},
			PAYABLE: {title: 'Payable',
				entity: 'appointment',
				columns: [{title: 'Nurse', prop: 'nurse_name',
				              getUrl: function(appointment){return '#nurse/' + appointment.nurse_id + '/payable';}},
				          {title: 'Date', prop: 'start', type: 'date'},
				          {title: 'Hours', prop: 'pay_hours'},
				          {title: 'Pay Rate', prop: 'pay_rate', type: 'currency'},
				          {title: 'Visit Pay', prop: 'pay_flat', type: 'currency'},
				          {title: 'Mileage', prop: 'pay_mileage', type: 'currency'},
				          {title: 'Total Payment', prop: 'pay_total', type: 'currency'}]},
			UNPAID_INVOICES: {title: 'Unpaid Invoices',
				entity: 'invoice',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(invoice){return '#vendor/' + invoice.vendor_id + '/invoices';}},
				          {title: 'Date', prop: 'generation_time', type: 'date'},
				          {title: 'Billed', prop: 'total_due', type: 'currency'},
				          {title: 'Status', prop: 'status_id'},
				          {title: 'Comment', prop: 'comment', editable: true},
				          {title: 'Received', prop: 'total_paid', editable: true}]},
			PAID_INVOICES: {title: 'Paid Invoices',
				entity: 'invoice',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(invoice){return '#vendor/' + invoice.vendor_id + '/invoices';}},
				          {title: 'Date', prop: 'generation_time', type: 'date'},
				          {title: 'Billed', prop: 'total_due', type: 'currency'},
				          {title: 'Status', prop: 'status_id'},
				          {title: 'Comment', prop: 'comment', editable: true},
				          {title: 'Received', prop: 'total_paid', editable: true}]},
			PAY_HISTORY: {title: 'Pay History',
				entity: 'paystub',
				columns: [{title: 'Nurse', prop: 'nurse_name',
                      getUrl: function(paystub){return '#nurse/' + paystub.nurse_id + '/payHistory';}},
				          {title: 'Pay Date', prop: 'pay_date', type: 'date',
				              imageLink: {src: 'images/icons/fugue/document-pdf.png',
				                  getUrl: function(paystub){return 'report.pdf?type=paystub&id=' + paystub.id}}},
				          {title: 'Gross', prop: 'gross_pay', type: 'currency'},
				          {title: 'Deductions', getValue: function(paystub) {
				            return $filter('currency')((paystub.pre_tax_deductions || 0) + (paystub.post_tax_deductions || 0));
				          }},
				          {title: 'Taxes', prop: 'tax_total', type: 'currency'},
				          {title: 'Mileage Reimbursement', prop: 'pay_mileage', type: 'currency'},
                  {title: 'Net Pay', prop: 'net_pay', type: 'currency'}]}
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
                   'state': '<=' + AppointmentState.AS_BILLABLE_AND_PAYABLE}},
			PAYABLE: {base: com.digitald4.iis.TableBaseMeta.PAYABLE,
      		filter: {'state': '>=' + AppointmentState.AS_BILLABLE_AND_PAYABLE,
      		         'state': '<=' + AppointmentState.AS_PAYABLE}},
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