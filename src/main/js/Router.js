com.digitald4.iis.router = function($routeProvider) {
	$routeProvider
		.when('/dashboard', {
			controller: com.digitald4.iis.DashboardCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/dashboard.html'
		}).when('/assessment/:id/:tab?', {
			controller: com.digitald4.iis.AssessmentCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/assessment.html'
		}).when('/patients', {
			template: '<dd4-table metadata="TableType.PATIENTS"></dd4-table>'
		}).when('/patient_notes', {
			template: '<dd4-table metadata="{base: TableType.GLOBAL_NOTES,filter: \'entityType=Patient\'}"></dd4-table>'
		}).when('/nurse_notes', {
			template: '<dd4-table metadata="{base: TableType.GLOBAL_NOTES,filter: \'entityType=Nurse\'}"></dd4-table>'
		}).when('/vendor_notes', {
			template: '<dd4-table metadata="{base: TableType.GLOBAL_NOTES,filter: \'entityType=Vendor\'}"></dd4-table>'
		}).when('/user_notes', {
			template: '<dd4-table metadata="{base: TableType.GLOBAL_NOTES,filter: \'entityType=User\'}"></dd4-table>'
		}).when('/patient/:id/:tab?', {
			controller: com.digitald4.iis.PatientCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/patient.html'
		}).when('/pendass', {
			template: '<iis-pending-assessments purpose="Pending"></iis-pending-assessments>'
		}).when('/intake', {
			controller: com.digitald4.iis.IntakeCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/intake.html'
		}).when('/pintake', {
			template: '<dd4-table metadata="TableType.PENDING_INTAKE"></dd4-table>'
		}).when('/users', {
			template: '<dd4-table metadata="TableType.USERS"></dd4-table>'
		}).when('/user/:id', {
			controller: com.digitald4.iis.UserCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/user.html'
		}).when('/user_add', {
			controller: com.digitald4.iis.UserAddCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/user_add.html'
		}).when('/billable', {
			template: '<iis-payable purpose="Billable"></iis-payable>'
		}).when('/billcodes', {
			template: '<dd4-table metadata="TableType.BILL_CODES"></dd4-table>'
		}).when('/payable', {
			template: '<iis-payable purpose="Payable"></iis-payable>'
		}).when('/paycodes', {
			template: '<dd4-table metadata="TableType.PAY_CODES"></dd4-table>'
		}).when('/invoices', {
			template: '<dd4-table metadata="TableType.INVOICES"></dd4-table>'
		}).when('/pay_history', {
			template: '<dd4-table metadata="TableType.PAY_HISTORY"></dd4-table>'
		}).when('/vendors', {
			template: '<dd4-table metadata="TableType.VENDORS"></dd4-table>'
		}).when('/vendor/:id/:tab?', {
			controller: com.digitald4.iis.VendorCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/vendor.html'
		}).when('/vendor_add', {
			controller: com.digitald4.iis.VendorAddCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/vendor_add.html'
		}).when('/nurses', {
			template: '<dd4-table metadata="TableType.NURSES"></dd4-table>'
		}).when('/nurse/:id/:tab?', {
			controller: com.digitald4.iis.NurseCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/nurse.html'
		}).when('/nurse_add', {
			controller: com.digitald4.iis.NurseAddCtrl,
			controllerAs: '$ctrl',
			templateUrl: 'js/html/nurse_add.html'
		}).when('/license_alert', {
			template: '<dd4-table metadata="TableType.LICENSE_ALERT"></dd4-table>'
		}).when('/unconfirmed', {
			template: '<dd4-table metadata="TableType.UNCONFIRMED"></dd4-table>'
		}).when('/qbExports', {
			template: '<dd4-table metadata="TableType.QUICKBOOKS_EXPORTS"></dd4-table>'
		}).when('/files', {
			template: '<dd4-table metadata="TableType.FILES"></dd4-table>'
		}).when('/exports', {
			templateUrl: 'js/html/exports.html'
		}).otherwise({ redirectTo: '/dashboard'});
};
