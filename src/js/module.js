com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'ui.calendar', 'ngMaterial'])
    .config(com.digitald4.iis.router)
    .service('appointmentService', function(apiConnector) {
      return new com.digitald4.common.JSONService('appointment', apiConnector);
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
        nurseService.performRequest(['closest'], {'latitude': lat, 'longitude': lon}, undefined, success, error);
      };
      return nurseService;
    })
    .service('patientService', function(apiConnector) {
      return new com.digitald4.common.JSONService('patient', apiConnector);
    })
    .service('vendorService', function(apiConnector) {
      return new com.digitald4.common.JSONService('vendor', apiConnector);
    })
    .factory('sharedData', function() {
      return new com.digitald4.iis.SharedData();
    }).controller('IISCtrl', com.digitald4.iis.IISCtrl)
    .directive('iisCalendar', function() {
      return {
        restrict: 'A',
        replace: true,
        transclude: true,
        scope: {config: '=iisCalendar'},
        controller: com.digitald4.iis.CalendarCtrl,
        controllerAs: 'calCtrl',
        templateUrl: 'js/html/calendar.html'
      };
    });
