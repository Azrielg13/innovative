var AppointmentState = proto.iis.AppointmentStateUI;

com.digitald4.iis.IISCtrl = function($scope, $filter, sharedData, generalDataService) {
  $scope.GeneralData = com.digitald4.iis.GeneralData;
  $scope.generalDataService = generalDataService;
	com.digitald4.iis.TableBaseMeta = {
			NURSES: {title: 'Nurses',
				entity: 'nurse',
				columns: [{title: 'Name', prop: 'name', getUrl: function(nurse) {
                    nurse.name = nurse.name || nurse.first_name + ' ' + nurse.last_name;
                    return '#nurse/' + nurse.id;
                  }},
				          {title: 'Status', prop: 'status_id'},
				          {title: 'Phone #', prop: 'phone_number'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Address', prop: 'address'},
				          {title: 'Pending Evaluations', prop: 'pend_asses_count'}]},
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
				          {title: 'Dianosis', prop: 'dianosis_id'},
				          {title: 'Referral Date', prop: 'referral_date', type: 'date'},
				          {title: 'Start Date', prop: 'start_of_care_date', type: 'date'}]},
			USERS: {title: 'Users',
				entity: 'user',
				columns: [{title: 'Name', prop: 'name', getUrl: function(user) {
				            user.name = user.name || user.first_name + ' ' + user.last_name;
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
                  {title: 'Address', prop: 'address'},
                  {title: 'Fax Number', prop: 'fax_number'},
                  {title: 'Contact Name', prop: 'contact_name'},
                  {title: 'Contact Phone', prop: 'contact_number'},
                  {title: 'Pending Assessments', prop: 'pend_asses_count'}]},
			UNCONFIRMED: {title: 'Unconfirmed Appointments',
				entity: 'appointment',
				columns: [{title: 'Nurse', prop: 'nurse_name',
				              getUrl: function(appointment){return '#nurse/' + appointment.nurse_id;}},
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
				          	return (appointment.assessment_entry.length / 25) + '%';}},
				          {title: 'Action', prop: 'action'}]},
			BILLABLE: {title: 'Billable',
				entity: 'appointment',
				columns: [{title: 'Vendor', prop: 'vendor_name',
				              getUrl: function(appointment){return '#vendor/' + appointment.vendor_id;}},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Billing Hours', prop: 'billed_hours'},
				          {title: 'Billing Rate', prop: 'billing_rate', type: 'currency'},
				          {title: 'Visit Pay', prop: 'billing_flat', type: 'currency'},
				          {title: 'Billing Mileage', prop: 'billing_mileage'},
				          {title: 'Total Payment', prop: 'billing_total', type: 'currency'}]},
			PAYABLE: {title: 'Payable',
				entity: 'appointment',
				columns: [{title: 'Nurse', prop: 'nurse_name',
				              getUrl: function(appointment){return '#nurse/' + appointment.nurse_id;}},
				          {title: 'Date', prop: 'start', type: 'datetime'},
				          {title: 'Hours', prop: 'pay_hours'},
				          {title: 'Pay Rate', prop: 'pay_rate', type: 'currency'},
				          {title: 'Visit Pay', prop: 'pay_flat', type: 'currency'},
				          {title: 'Mileage', prop: 'pay_mileage', type: 'currency'},
				          {title: 'Total Payment', prop: 'pay_total', type: 'currency'}]},
			UNPAID_INVOICES: {title: 'Unpaid Invoices',
				entity: 'invoice',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(invoice){return '#vendor/' + invoice.vendor_id;}},
				          {title: 'Date', prop: 'generation_time', type: 'date'},
				          {title: 'Billed', prop: 'total_due', type: 'currency'},
				          {title: 'Status', prop: 'status_id'},
				          {title: 'Comment', prop: 'comment', editable: true},
				          {title: 'Received', prop: 'total_paid', editable: true}]},
			PAID_INVOICES: {title: 'Paid Invoices',
				entity: 'invoice',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(invoice){return '#vendor/' + invoice.vendor_id;}},
				          {title: 'Date', prop: 'generation_time', type: 'date'},
				          {title: 'Billed', prop: 'total_due', type: 'currency'},
				          {title: 'Status', prop: 'status_id'},
				          {title: 'Comment', prop: 'comment', editable: true},
				          {title: 'Received', prop: 'total_paid', editable: true}]},
			PAY_HISTORY: {title: 'Pay History',
				entity: 'paystub',
				columns: [{title: 'Name', prop: 'name',
                      getUrl: function(paystub){return '#nurse/' + paystub.nurse_id;}},
				          {title: 'Pay Date', prop: 'pay_date', type: 'date'},
				          {title: 'Gross', prop: 'gross_pay', type: 'currency'},
				          {title: 'Deductions', getValue: function(paystub) {return $filter('currency')(paystub.pre_tax_deductions + paystub.post_tax_deductions);}},
				          {title: 'Taxes', prop: 'tax_total', type: 'currency'},
				          {title: 'Mileage Reimbursement', prop: 'pay_mileage', type: 'currency'},
                  {title: 'Net Pay', prop: 'net_pay', type: 'currency'}]}
	};
	$scope.TableType = {
			NURSES: {base: com.digitald4.iis.TableBaseMeta.NURSES, request: {}},
			PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS, request: {}},
			USERS: {base: com.digitald4.iis.TableBaseMeta.USERS, request: {}},
			VENDORS: {base: com.digitald4.iis.TableBaseMeta.VENDORS, request: {}},
			PENDING_INTAKE: {base: com.digitald4.iis.TableBaseMeta.PENDING_INTAKE,
			    request: {query_param: [{column: 'referral_resolution_id', operan: '=', value: '885'}]}},
			UNCONFIRMED: {base: com.digitald4.iis.TableBaseMeta.UNCONFIRMED,
			    request: {query_param: [{column: 'state', operan: '=', value: AppointmentState.AS_UNCONFIRMED.toString()}]}},
			PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
				  request: {query_param: [{column: 'state', operan: '=', value: AppointmentState.AS_PENDING_ASSESSMENT.toString()}]}},
		  BILLABLE: {base: com.digitald4.iis.TableBaseMeta.BILLABLE,
				  request: {query_param: [{column: 'state', operan: '=', value: AppointmentState.AS_BILLABLE.toString()}]}},
			PAYABLE: {base: com.digitald4.iis.TableBaseMeta.PAYABLE,
      		request: {query_param: [{column: 'state', operan: '=', value: AppointmentState.AS_PAYABLE.toString()}]}},
			UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.UNPAID_INVOICES,
				  request: {query_param: [{column: 'status_id', operan: '=', value: '1521'}]}},
			PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES,
				  request: {query_param: [{column: 'status_id', operan: '=', value: '1520'}]}},
			PAY_HISTORY: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES, request: {}}
	};
	this.sharedData = sharedData;
};