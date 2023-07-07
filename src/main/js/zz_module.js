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
        nurseService.sendRequest(
            {action: 'closest', params: {'latitude': lat, 'longitude': lon, 'pageSize': 15}}, success, error);
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
    .controller('SettingsCtrl', ['apiConnector', function(apiConnector) {
      apiConnector.baseUrl = 'https://ip360-179401.appspot.com/';
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
    });
