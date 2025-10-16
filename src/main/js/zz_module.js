com.digitald4.iis.module = angular.module('iis', ['ngRoute', 'DD4Common', 'angular-bind-html-compile'])
    .config(com.digitald4.iis.router)
    .filter('trusted', ['$sce', $sce => { return url => { return $sce.trustAsResourceUrl(url); }}])
    .service('appointmentService', function(apiConnector, globalData) {
      var appointmentService = new com.digitald4.common.JSONService('appointment', apiConnector);

      appointmentService.cancelOut = function(id, eventOption, success, error) {
        appointmentService.sendRequest({action: 'cancelOut', method: 'DELETE',
            params: {id: id, eventOption: eventOption}}, success, error);
      }

      appointmentService.removeAttachment = function(appointmentId, fileId, success, error) {
        appointmentService.sendRequest({action: 'removeAttachment', method: 'DELETE',
            params: {appointmentId: appointmentId, fileId: fileId}}, success, error);
      }

      appointmentService.batchCreate = function(appointments, allowDuplicate, success, error) {
        appointmentService.sendRequest({action: 'batchCreate', method: 'POST', data: {items: appointments},
            params: {allowDuplicate: allowDuplicate}}, success, error);
      }

      appointmentService.transform = function(appointment) {
        var role = globalData.activeSession.user.role;
        appointment.isTimeEditable = role != 'Nurse' && (appointment.state == 'UNCONFIRMED'
            || appointment.state == 'CONFIRMED' || appointment.state == 'PENDING_ASSESSMENT');
        appointment.isAssessmentEditable = appointment.state == 'PENDING_ASSESSMENT';
        appointment.isBillingEditable = appointment.state != 'CLOSED';
        return appointment;
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
      }
      nurseService.getActive = function(success) {
        if (nurseService.active) {
          success(nurseService.active);
        }
        nurseService.list({filter: 'status=Active'}, response => {
          nurseService.active = response.items;
          success(nurseService.active);
        });
      }
      return nurseService;
    })
    .service('patientService', function(apiConnector) {
      var patientService = new com.digitald4.common.JSONService('patient', apiConnector);
      patientService.getActive = function(success) {
        if (patientService.active) {
          success(patientService.active);
        }
        patientService.list({filter: 'status=Active'}, response => {
          patientService.active = response.items;
          success(patientService.active);
        });
      }
      return patientService;
    })
    .service('paystubService', function(apiConnector) {
      return new com.digitald4.common.JSONService('paystub', apiConnector);
    })
    .service('quickBooksExportService', function(apiConnector) {
      return new com.digitald4.common.JSONService('quickBooksExport', apiConnector);
    })
    .service('reportService', function(apiConnector) {
      return new com.digitald4.common.JSONService('report', apiConnector);
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
    .component('appointmentDialog', {
      controller: com.digitald4.iis.AppointmentDialog,
      bindings: {
        dialogRequest: '=',
        postUpdate: '&',
        postDelete: '&',
      },
      templateUrl: 'js/html/appointment_dialog.html'
    })
    .component('attachments', {
      controller: com.digitald4.iis.AttachmentsCtrl,
      bindings: {
        appointment: '<',
        id: '@',
        label: '@',
      },
      templateUrl: 'js/html/attachments.html'
    })
    .component('iisCalendar', {
      controller: com.digitald4.iis.CalendarCtrl,
      bindings: {
        entityType: '@',
        entityId: '@',
        onUpdate: '&',
      },
      templateUrl: 'js/html/calendar.html'
    })
    .component('iisLicense', {
      controller: com.digitald4.iis.LicenseCtrl,
      bindings: {
        nurseId: '@',
      },
      templateUrl: 'js/html/license.html'
    })
    .component('iisTable', {
      controller: function() {
        this.onClick = function(clickRequest) {
          clickRequest.shown = true;
          this.dialogRequest = clickRequest;
        }
      },
      bindings: {
        metadata: '<',
      },
      template: '<dd4-table metadata="$ctrl.metadata" data-on-click="$ctrl.onClick(clickRequest)">'
          + '</dd4-table><appointment-dialog data-ng-if="$ctrl.dialogRequest.shown"'
          + ' data-dialog-request="$ctrl.dialogRequest"></appointment-dialog>'
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
