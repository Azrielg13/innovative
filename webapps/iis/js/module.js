com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'ui.calendar', 'ngMaterial'])
    .config(com.digitald4.iis.router)
    .service('appointmentService', function(restService) {
      return new com.digitald4.common.JSONService('appointment', restService);
    })
    .service('licenseService', function(restService) {
      return new com.digitald4.common.JSONService('license', restService);
    })
    .service('notificationService', function(restService) {
      return new com.digitald4.common.JSONService('notification', restService)
    })
    .service('nurseService', function(restService) {
      var nurseService = new com.digitald4.common.JSONService('nurse', restService);
      nurseService.listClosest = function(lat, lon, success, error) {
        nurseService.performRequest('GET', ['latitude', lat, 'longitude', lon, this.service, 'closest'].join('/'),
            undefined, success, error);
      };
      return nurseService;
    })
    .service('patientService', function(restService) {
      return new com.digitald4.common.JSONService('patient', restService);
    })
    .service('vendorService', function(restService) {
      return new com.digitald4.common.JSONService('vendor', restService);
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
