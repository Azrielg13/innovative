var ONE_HOUR = 1000 * 60 * 60;
var ONE_DAY = ONE_HOUR * 24;

com.digitald4.iis.CalCtrl = function($scope, $filter, $compile, $mdDialog, restService) {
	this.filter = $filter;
	this.mdDialog = $mdDialog;
	this.appointmentService = new com.digitald4.common.ProtoService('appointment', restService);
	
  var eventClicked = function(event, jsEvent, view) {
		this.setSelected(event.appointment);
	}.bind(this);

  /* Render Tooltip */
  var eventRender = function(event, element, view) {
    element.attr({'tooltip': event.title,
                  'tooltip-append-to-body': true});
    $compile(element)($scope);
  };
 
  var viewRender = function(view, element) {
    this.refresh(view.intervalStart);
  }.bind(this);

  this.uiConfig = {
    calendar:{
      height: 450,
      editable: false,
      header:{
        left: 'title',
        center: '',
        right: 'today prev,next'
      },
      eventClick: eventClicked,
      eventRender: eventRender,
      viewRender: viewRender
    }
  };
  this.eventSources = [this.events];
  this.refresh();
};

convertToEvent = function(appointment) {
  return {
    id: appointment.id,
    title: // $filter('date')(appointment.start, 'shortTime') + ' ' +
        appointment.patient_name,
    start: new Date(appointment.start),
    end: new Date(appointment.end),
    appointment: appointment,
    className: ['appointment']
  };
};

com.digitald4.iis.CalCtrl.prototype.appointmentService;

com.digitald4.iis.CalCtrl.prototype.events = [
    convertToEvent({ id: 100,
		  patient_name: 'Test 1',
		  start: new Date().getTime(),
		  end: new Date().getTime() + 3600000}),
		convertToEvent({ id: 200,
			patient_name: 'Test 2',
		  start: new Date().getTime(),
		  end: new Date().getTime() + 3600000})
];

com.digitald4.iis.CalCtrl.prototype.refresh = function(startDate) {
	this.appointmentService.list([], this.setEvents.bind(this), notify);
};

com.digitald4.iis.CalCtrl.prototype.setEvents = function(appointments) {
	this.events.length = 0;
	angular.forEach(appointments, function(appointment) {
		this.events.push(convertToEvent(appointment));
	}.bind(this));
};

com.digitald4.iis.CalCtrl.prototype.setSelected = function(appointment) {
	this.selectedAppointment = appointment;
	console.log('Selected appointment: ' + appointment.id + " " + appointment.patient_name);
	this.showReservationDialog();
};

com.digitald4.iis.CalCtrl.prototype.closeReservationDialog = function() {
	this.reservationDialogVisible = false;
};

var student = {};
var reservation = {
		student: []
};

com.digitald4.iis.CalCtrl.prototype.showReservationDialog = function(ev) {
  this.mdDialog.show({
    controller: DialogController,
    templateUrl: 'js/html/reservation-dialog.html',
    parent: angular.element(document.body),
    targetEvent: ev,
    clickOutsideToClose:true,
    locals: {appointment: this.selectedAppointment}
  }).then(function(reservation_) {
  	reservation = {student: []};
  }, function() {
  	if (reservation.confirmation_code) {
  		reservation = {student: []};
    }
  });
};

function DialogController($scope, $mdDialog, appointment, reservationService) {
	reservation.appointment = appointment;
	$scope.reservation = reservation;
	$scope.student = student;
  
  $scope.cancel = function() {
    $mdDialog.cancel();
  };
  
  $scope.add = function() {
  	if (!student.added) {
	  	student.added = true;
	  	reservation.student.push(student);
  	}
    $scope.student = student = {};
  };
  
  $scope.remove = function(student_) {
  	reservation.student.splice(reservation.student.indexOf(student_), 1);
  	if (student = student_) {
  		$scope.student = student = {};
  	}
  };
  
  $scope.select = function(student_) {
  	$scope.student = student = student_;
  };
  
  $scope.submit = function() {
  	reservationService.createReservation(reservation, function(reservation_) {
  		$scope.reservation = reservation = reservation_;
  	}, function(error) {
  		
  	});
  };
  
  $scope.close = function() {
  	$mdDialog.hide(reservation);
  };
}
