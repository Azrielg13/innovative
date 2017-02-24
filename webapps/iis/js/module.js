com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'ui.calendar', 'ngMaterial'])
    .config(com.digitald4.iis.router)
    .service('appointmentService', function(restService) {
      console.log('creating new appointmentService');
      return new com.digitald4.common.ProtoService('appointment', restService);
    })
    .service('licenseService', function(restService) {
      return new com.digitald4.common.ProtoService('license', restService);
    })
    .service('nurseService', function(restService) {
      console.log('creating new nurseService');
      var nurseService = new com.digitald4.common.ProtoService('nurse', restService);
      nurseService.listClosest = function(lat, lon, success, error) {
        nurseService.performRequest('list_closest', {latitude: lat, longitude: lon}, success, error);
      };
      return nurseService;
    })
    .service('patientService', function(restService) {
      console.log('creating new patientService');
      return new com.digitald4.common.ProtoService('patient', restService);
    })
    .service('vendorService', function(restService) {
      return new com.digitald4.common.ProtoService('vendor', restService);
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
