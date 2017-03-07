var ONE_HOUR = 1000 * 60 * 60;
var ONE_DAY = 24 * ONE_HOUR;

com.digitald4.iis.CalendarCtrl = function($scope, $filter, appointmentService, notificationService, patientService,
    nurseService) {
	this.dateFilter = $filter('date');
	if ($scope.config) {
	  this.entity = $scope.config.entity;
	  this.entityId = $scope.config.entityId;
	  if (this.entity == 'nurse') {this.nurseId = this.entityId};
	  if (this.entity == 'patient') {this.patientId = this.entityId};
	  if (this.entity == 'vendor') {this.vendorId = this.entityId};
  }
	this.appointmentService = appointmentService;
	this.notificationService = notificationService;
	this.patientService = patientService;
	this.nurseService = nurseService;
	var today = new Date();
	this.setMonth(today.getFullYear(), today.getMonth());
};

com.digitald4.iis.CalendarCtrl.prototype.dateFilter;
com.digitald4.iis.CalendarCtrl.prototype.nurseId;
com.digitald4.iis.CalendarCtrl.prototype.patientId;
com.digitald4.iis.CalendarCtrl.prototype.vendorId;
com.digitald4.iis.CalendarCtrl.prototype.appointmentService;

com.digitald4.iis.CalendarCtrl.prototype.setupCalendar = function() {
	var month = this.getMonth();
	var currDay = new Date(this.getStartDate());
	var weeks = [];
	var woy = 1;
	var days = {};
	do {
		var week = {
			weekOfYear: woy++
		};
		var weekdays = [];
		for (var d = 0; d < 7; d++) {
			var day = {
				date: currDay,
				weekend: (d == 0 || d == 6),
				otherMonth: currDay.getMonth() != month.getMonth(),
				appointments: [],
				notifications: []
			};
			weekdays.push(day);
			days[this.dateFilter(day.date.getTime(), 'MMdd')] = day;
			currDay = addDay(currDay);
		}
		week.days = weekdays;
		weeks.push(week);
	} while (currDay.getMonth() == month.getMonth());
	this.days = days;
	this.weeks = weeks;
};

addDay = function(date) {
	var ret = new Date(date.getTime() + ONE_DAY);
	if (ret.getDay() == date.getDay() || ret.getHours() < date.getHours()) {
		ret.setTime(ret.getTime() + ONE_HOUR);
	} else if (ret.getHours() > date.getHours()) {
		ret.setTime(ret.getTime() - ONE_HOUR);
	}
	return ret;
};

com.digitald4.iis.CalendarCtrl.prototype.refresh = function() {
	this.setupCalendar();

	var appFilter = {'start': '>=' + this.getStartDate().getTime(),
	                 'start': '<=' + this.getEndDate().getTime()};
	appFilter[this.entity] = this.entityId;

	this.appointmentService.list(appFilter, function(appointments) {
	  for (var d in this.days) {
    		this.days[d].appointments = [];
    }
    this.appointments = appointments;
    for (var t = 0; t < appointments.length; t++) {
      var appointment = appointments[t];
      var day = this.days[this.dateFilter(appointment.start, 'MMdd')];
      if (day) {
        day.appointments.push(appointment);
      }
    }
	}.bind(this), notify);

  var notificationRequest = {start_date: this.getStartDate().getTime(),
      end_date: this.getEndDate().getTime(),
      entity: this.entity,
      entity_id: this.entityId};
	this.notificationService.performRequest('list', notificationRequest, function(notifications) {
	  for (var d in this.days) {
	    this.days[d].notifications = [];
    }
    this.notifications = notifications;
    for (var t = 0; t < notifications.length; t++) {
      var notification = notifications[t];
      switch (notification.type) {
        case 1: notification.color = 'blue'; break;
        case 2: notification.color = 'yellow'; break;
        case 3: notification.color = 'red'; break;
      }
      var day = this.days[this.dateFilter(notification.date, 'MMdd')];
      if (day) {
        day.notifications.push(notification);
      }
    }
	}.bind(this), notify);
};

com.digitald4.iis.CalendarCtrl.prototype.refreshLists = function() {
  var requestParams = this.vendorId ? {column: 'billing_id', operan: '=', value: this.vendorId.toString()} : [];
  this.patientService.list(requestParams, function(patients) {
    this.patients = patients;
  }.bind(this), notify);

  this.nurseService.list([], function(nurses) {
    this.nurses = nurses;
  }.bind(this), notify);
};

com.digitald4.iis.CalendarCtrl.prototype.showAddDialog = function(date) {
  if (!this.patients) {
    this.refreshLists();
  }
  var start = date.getTime() + 10 * ONE_HOUR;
  var end = date.getTime() + 12 * ONE_HOUR;
	this.newAppointment = {start: start, end: end, nurse_id: this.nurseId, patient_id: this.patientId};
	this.addDialogShown = true;
};

com.digitald4.iis.CalendarCtrl.prototype.closeDialog = function() {
	this.addDialogShown = false;
	this.editDialogShown = false;
};

com.digitald4.iis.CalendarCtrl.prototype.create = function() {
	this.addError = undefined;
	this.appointmentService.create(this.newAppointment, function(appointment) {
	  this.appointments.push(appointment);
	  var day = this.days[this.dateFilter(appointment.start, 'MMdd')];
    if (day) {
      day.appointments.push(appointment);
    }
	  this.closeDialog();
	}.bind(this), notify);
};

com.digitald4.iis.CalendarCtrl.prototype.edit = function(appointment) {
  if (!this.patients) {
    this.refreshLists();
  }
  this.editAppointment = {id: appointment.id, start: appointment.start, end: appointment.end,
      nurse_id: appointment.nurse_id, patient_id: appointment.patient_id,
      cancelled: appointment.cancelled, cancel_reason: appointment.cancel_reason};
	this.origAppointment = appointment;
	this.editDialogShown = true;
};

com.digitald4.iis.CalendarCtrl.prototype.saveEdits = function() {
  var edits = [];
  if (this.editAppointment.start != this.origAppointment.start) {edits.push('start');}
  if (this.editAppointment.end != this.origAppointment.end) {edits.push('end');}
  if (this.editAppointment.nurse_id != this.origAppointment.nurse_id) {edits.push('nurse_id');}
  if (this.editAppointment.patient_id != this.origAppointment.patient_id) {edits.push('patient_id');}
  if (this.editAppointment.cancelled != this.origAppointment.cancelled) {edits.push('cancelled');}
  if (this.editAppointment.cancel_reason != this.origAppointment.cancelled) {edits.push('cancel_reason');}

	this.updateError = undefined;
	this.appointmentService.update(this.editAppointment, edits, function(appointment) {
    var oldDay = this.days[this.dateFilter(this.origAppointment.start, 'MMdd')];
    if (oldDay) {
      var index = oldDay.appointments.indexOf(this.origAppointment);
      oldDay.appointments.splice(index, 1);
      index = this.appointments.indexOf(this.origAppointment);
      this.appointments.splice(index, 1);
    }
    var newDay = this.days[this.dateFilter(appointment.start, 'MMdd')];
    if (newDay) {
      newDay.appointments.push(appointment);
      this.appointments.push(appointment);
    }
	  this.closeDialog();
	}.bind(this), function(error) {
		this.updateError = error;
	}.bind(this));
};

com.digitald4.iis.CalendarCtrl.prototype.getMonth = function() {
	return this.month;
};

com.digitald4.iis.CalendarCtrl.prototype.setMonth = function(year, month) {
	this.month = new Date(year, month, 1);
	this.refresh();
};

com.digitald4.iis.CalendarCtrl.prototype.prevMonth = function() {
	this.month.setDate(0);
	this.month.setDate(1);
	this.refresh();
};

com.digitald4.iis.CalendarCtrl.prototype.nextMonth = function() {
	this.month.setDate(32);
	this.month.setDate(1);
	this.refresh();
};

com.digitald4.iis.CalendarCtrl.prototype.getStartDate = function() {
	var startDate = new Date(this.month.getFullYear(), this.month.getMonth(), 1);
	startDate.setTime(startDate.getTime() - (startDate.getDay() * ONE_DAY));
	return startDate;
};

com.digitald4.iis.CalendarCtrl.prototype.getEndDate = function() {
	var endDate = new Date(this.month);
	endDate.setDate(31);
	while (endDate.getMonth() != this.month.getMonth()) {
		endDate.setTime(endDate.getTime() - ONE_DAY);
	}
	endDate.setTime(endDate.getTime() + ((6 - endDate.getDay()) * ONE_DAY));
	return endDate;
};