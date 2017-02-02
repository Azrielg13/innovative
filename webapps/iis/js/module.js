com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'ui.calendar', 'ngMaterial'])
    .config(com.digitald4.iis.router)
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
