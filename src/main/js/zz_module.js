com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'angular-bind-html-compile'])
    .config(com.digitald4.iis.router)
    .filter('trusted', ['$sce', $sce => { return url => { return $sce.trustAsResourceUrl(url); }}])
    .service('appointmentService', function(apiConnector) {
      var appointmentService = new com.digitald4.common.JSONService('appointment', apiConnector);
      appointmentService.cancelOut = function(id, eventOption, success, error) {
        appointmentService.sendRequest({action: 'cancelOut', method: 'DELETE',
            params: {id: id, eventOption: eventOption}}, success, error);
      }
      return appointmentService;
    })
    .service('invoiceService', function(apiConnector) {
      return new com.digitald4.common.JSONService('invoice', apiConnector);
    })
    .service('licenseService', function(apiConnector) {
      return new com.digitald4.common.JSONService('license', apiConnector);
    })
    .service('noteService', function(apiConnector) {
      return new com.digitald4.common.JSONService('note', apiConnector)
    })
    .service('notificationService', function(apiConnector) {
      return new com.digitald4.common.JSONService('notification', apiConnector)
    })
    .service('nurseService', function(apiConnector) {
      var nurseService = new com.digitald4.common.JSONService('nurse', apiConnector);
      nurseService.listClosest = function(lat, lon, success, error) {
        nurseService.sendRequest({action: 'closest',
            params: {'latitude': lat, 'longitude': lon, 'pageSize': 15}}, success, error);
      };
      return nurseService;
    })
    .service('patientService', function(apiConnector) {
      return new com.digitald4.common.JSONService('patient', apiConnector);
    })
    .service('paystubService', function(apiConnector) {
      return new com.digitald4.common.JSONService('paystub', apiConnector);
    })
    .service('quickBooksExportService', function(apiConnector) {
      return new com.digitald4.common.JSONService('quickBooksExport', apiConnector);
    })
    .service('serviceCodeService', function(apiConnector) {
      return new com.digitald4.common.JSONService('serviceCode', apiConnector);
    })
    .service('vendorService', function(apiConnector) {
      return new com.digitald4.common.JSONService('vendor', apiConnector);
    })
    .service('searchService', function(apiConnector) {
      return {search: function(request, success, error) {
        apiConnector.sendRequest({url: 'search/v1/search', params: request}, success, error);
      }};
    })
    .controller('IISCtrl', com.digitald4.iis.IISCtrl)
    .controller('SettingsCtrl', ['apiConnector', '$location', function(apiConnector, $location) {
    	if ($location.host() == 'localhost') apiConnector.baseUrl = TEST_URL; // PROD_URL
    }])
    .component('iisCalendar', {
      controller: com.digitald4.iis.CalendarCtrl,
      bindings: {
        entityType: '@',
        entityId: '@',
        onUpdate: '&',
      },
      controllerAs: 'calCtrl',
      templateUrl: 'js/html/calendar.html'
    })
    .component('noteTable', {
      controller: com.digitald4.iis.NoteTableCtrl,
      bindings: {
        allowAdd: '@',
        entityType: '@',
        entityId: '@',
        metadata: '=',
      },
      templateUrl: 'js/html/note_table.html'
    })
    .component('iisPayable', {
      controller: com.digitald4.iis.PayableCtrl,
      bindings: {
        entityType: '@',
        entityId: '@',
        onUpdate: '&',
        purpose: '@',
      },
      templateUrl: 'js/html/payable.html'
    })
    .component('iisQuickbooksExports', {
      controller: com.digitald4.iis.QuickBooksExportsCtrl,
      bindings: {
        entityType: '@',
        entityId: '@',
        onUpdate: '&',
      },
      templateUrl: 'js/html/quickbooks_exports.html'
    })
    .component('search', {
      controller: com.digitald4.iis.SearchCtrl,
      templateUrl: 'js/html/search.html'
    });
