com.digitald4.iis.router = function($routeProvider) {
	$routeProvider
		.when('/dashboard', {
			templateUrl: 'js/html/dashboard.html'
		}).when('/profile', {
			controller: 'ProfileCtrl',
			controllerAs: 'profileCtrl',
			templateUrl: 'js/html/profile.html'
		}).when('/patients', {
			template: '<div data-dd4-table="TableType.PATIENTS"></div>'
		}).when('/patient', {
			controller: 'PatientCtrl',
			controllerAs: 'patientCtrl',
			templateUrl: 'js/html/patient.html'
		}).when('/pendass', {
			template: '<div data-dd4-table="TableType.PENDING_ASSESSMENT"></div>'
		}).when('/intake', {
			controller: 'IntakeCtrl',
			controllerAs: 'intakeCtrl',
			templateUrl: 'js/html/intake.html'
		}).when('/pintake', {
			template: '<div data-dd4-table="TableType.PENDING_INTAKE"></div>'
		}).when('/users', {
			template: '<div data-dd4-table="TableType.USERS"></div>'
		}).when('/user', {
			controller: 'UserCtrl',
			controllerAs: 'userCtrl',
			templateUrl: 'js/html/user.html'
		}).when('/useradd', {
			controller: 'UserAddCtrl',
			controllerAs: 'userAddCtrl',
			templateUrl: 'js/html/user_add.html'
		}).when('/billable', {
			template: '<div data-dd4-table="TableType.BILLABLE"></div>'
		}).when('/payable', {
			template: '<div data-dd4-table="TableType.PAYABLE"></div>'
		}).when('/unpaid_invoices', {
			template: '<div data-dd4-table="TableType.UNPAID_INVOICES"></div>'
		}).when('/paid_invoices', {
			template: '<div data-dd4-table="TableType.PAID_INVOICES"></div>'
		}).when('/pay_history', {
			template: '<div data-dd4-table="TableType.PAY_HISTORY"></div>'
		}).when('/vendors', {
			template: '<div data-dd4-table="TableType.VENDORS"></div>'
		}).when('/vendor/:vendorId', {
			controller: 'VendorCtrl',
			controllerAs: 'vendorCtrl',
			templateUrl: 'js/html/vendor.html'
		}).when('/vendor_add', {
			controller: 'VendorAddCtrl',
			controllerAs: 'vendorAddCtrl',
			templateUrl: 'js/html/vendor_add.html'
		}).when('/nurses', {
			template: '<div data-dd4-table="TableType.NURSES"></div>'
		}).when('/nurse', {
			controller: 'NurseCtrl',
			controllerAs: 'nurseCtrl',
			templateUrl: 'js/html/nurse.html'
		}).when('/nurse_add', {
			controller: 'NurseAddCtrl',
			controllerAs: 'nurseAddCtrl',
			templateUrl: 'js/html/nurse_add.html'
		}).when('/license_alert', {
			template: '<div data-dd4-table="TableType.LICENSE_ALERT"></div>'
		}).when('/unconfirmed', {
			template: '<div data-dd4-table="TableType.UNCONFIRMED"></div>'
		}).when('/reports', {
			template: '<div data-dd4-table="TableType.REPORTS"></div>'
		}).otherwise({ redirectTo: '/dashboard'});
};
