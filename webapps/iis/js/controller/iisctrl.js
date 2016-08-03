

com.digitald4.iis.IISCtrl = function($scope, $filter, sharedData) {
	com.digitald4.iis.TableBaseMeta = {
			NURSES: {title: 'Nurses',
				url: 'nurses',
				link: {url: '#nurse', title: 'Name'},
				columns: [{title: 'Status', getValue: function(nurse) {
						      	nurse.name = nurse.name || nurse.first_name + ' ' + nurse.last_name;
										return nurse.status_id;}},
				          {title: 'Phone #', prop: 'phone_number'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Address', prop: 'address'},
				          {title: 'Pending Evaluations', prop: 'pend_asses_count'}]},
			PATIENTS: {title: 'Patients',
				url: 'patients',
				link: {url: '#patient', title: 'Name'},
				columns: [{title: 'Vendor', prop: 'billing_id'},
				          {title: 'RX', prop: 'rx'},
				          {title: 'Dianosis', prop: 'dianosis_id'},
				          {title: 'Last Appointment', prop: 'last_appointment'},
				          {title: 'Next Appointment', prop: 'next_appointment'}]},
			USERS: {title: 'Users',
				url: 'users',
				link: {url: '#user', title: 'Name'},
				columns: [{title: 'Type', prop: 'type_id'},
				          {title: 'Email Address', prop: 'email'},
				          {title: 'Disabled', prop: 'disabled'},
				          {title: 'Last Login', getValue: function(user) {
				          	user.name = user.name || user.first_name + ' ' + user.last_name;
										return $filter('date')(user.last_login, 'MM/dd/yyyy HH:mm');}},
				          {title: 'Notes', prop: 'notes'}]},
			VENDORS: {title: 'Vendors',
					url: 'vendors',
					link: {url: '#vendor', title: 'Vendor'},
					columns: [{title: 'Address', prop: 'address'},
					          {title: 'Fax Number', prop: 'fax_number'},
					          {title: 'Contact Name', prop: 'contact_name'},
					          {title: 'Contact Phone', prop: 'contact_number'},
					          {title: 'Pending Assessments', prop: 'pend_asses_count'}]},
			PENDING_ASSESSMENT: {title: 'Pending Assessment',
				url: 'appointments',
				link: {url: 'appointment', title: 'Patient'},
				columns: [{title: 'Date', getValue: function(appointment) {
										return $filter('date')(appointment.start, 'MM/dd/yyyy');}},
				          {title: 'Time In', getValue: function(appointment) {
				          	return $filter('date')(appointment.time_in || appointment.start, 'shortTime');}},
				          {title: 'Time Out', getValue: function(appointment) {
				          	return $filter('date')(appointment.time_out || appointment.end, 'shortTime');}},
				          {title: 'Percent Complete', getValue: function(appointment) {
				          	appointment.assessment_entry = appointment.assessment_entry || [];
				          	return (appointment.assessment_entry.length / 25) + '%';}},
				          {title: 'Action', prop: 'action'}]},
			INVOICES: {title: 'Unpaid Invoices',
				url: 'invoices',
				link: {url: 'invoice', title: 'Name'},
				columns: [{title: 'Date', getValue: function(invoice) {return $filter('date')(invoice.generation_time, 'MM/dd/yyyy');}},
				          {title: 'Billed', getValue: function(invoice) {return $filter('currency')(invoice.total_due);}},
				          {title: 'Status', prop: 'status_id'},
				          {title: 'Comment', prop: 'comment'},
				          {title: 'Received', getValue: function(invoice) {return $filter('currency')(invoice.total_paid);}}]},
	};
	$scope.TableType = {
			NURSES: {base: com.digitald4.iis.TableBaseMeta.NURSES, request: {}},
			PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS, request: {}},
			USERS: {base: com.digitald4.iis.TableBaseMeta.USERS, request: {}},
			VENDORS: {base: com.digitald4.iis.TableBaseMeta.VENDORS, request: {}},
			PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
				request: {query_param: [{column: 'assessment_complete', operan: '=', value: 'false'},
				                        {column: 'start', operan: '<', value: Date.now().toString()}]}},
			UNPAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.INVOICES,
				request: {query_param: [{column: 'status_id', operan: '=', value: '1521'}]}},
			PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.INVOICES,
				request: {query_param: [{column: 'status_id', operan: '=', value: '1520'}]}}
	};
	this.sharedData = sharedData;
};
