com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common'])
    .config(com.digitald4.iis.router)
    .service('appointmentService', function(apiConnector) {
      return new com.digitald4.common.JSONService('appointment', apiConnector);
    })
    .service('invoiceService', function(apiConnector) {
      return new com.digitald4.common.JSONService('invoice', apiConnector);
    })
    .service('licenseService', function(apiConnector) {
      return new com.digitald4.common.JSONService('license', apiConnector);
    })
    .service('notificationService', function(apiConnector) {
      return new com.digitald4.common.JSONService('notification', apiConnector)
    })
    .service('nurseService', function(apiConnector) {
      var nurseService = new com.digitald4.common.JSONService('nurse', apiConnector);
      nurseService.listClosest = function(lat, lon, success, error) {
        nurseService.performRequest(['closest'], undefined, {'latitude': lat, 'longitude': lon, 'pageSize': 15}, undefined, success, error);
      };
      return nurseService;
    })
    .service('patientService', function(apiConnector) {
      return new com.digitald4.common.JSONService('patient', apiConnector);
    })
    .service('paystubService', function(apiConnector) {
      return new com.digitald4.common.JSONService('paystub', apiConnector);
    })
    .service('vendorService', function(apiConnector) {
      return new com.digitald4.common.JSONService('vendor', apiConnector);
    })
    .controller('IISCtrl', com.digitald4.iis.IISCtrl)
    .directive('iisCalendar', ['$compile', function($compile) {
      return {
        restrict: 'AE',
        replace: true,
        transclude: true,
        scope: {
          config: '=',
          onUpdate: '&'
        },
        controller: com.digitald4.iis.CalendarCtrl,
        controllerAs: 'calCtrl',
        templateUrl: 'js/html/calendar.html'
      };
    }]);
