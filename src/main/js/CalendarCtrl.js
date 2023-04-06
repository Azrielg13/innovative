var ONE_HOUR = 1000 * 60 * 60;
var ONE_DAY = 24 * ONE_HOUR;

com.digitald4.iis.CalendarCtrl =
    function($filter, appointmentService, notificationService, nurseService, patientService) {
	this.dateFilter = $filter('date');
	this.appointmentService = appointmentService;
	this.notificationService = notificationService;
	this.nurseService = nurseService;
	this.patientService = patientService;
  if (this.entityType == 'nurse') {this.nurseId = this.entityId};
  if (this.entityType == 'patient') {this.patientId = this.entityId};
  if (this.entityType == 'vendor') {this.vendorId = this.entityId};
	var today = new Date();
	this.setMonth(today.getFullYear(), today.getMonth());
}

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
}

addDay = function(date) {
	var ret = new Date(date.getTime() + ONE_DAY);
	if (ret.getDay() == date.getDay() || ret.getHours() < date.getHours()) {
		ret.setTime(ret.getTime() + ONE_HOUR);
	} else if (ret.getHours() > date.getHours()) {
		ret.setTime(ret.getTime() - ONE_HOUR);
	}
	return ret;
}

com.digitald4.iis.CalendarCtrl.prototype.refresh = function() {
	this.setupCalendar();

	var entityFilter = this.entityType ? ',' + this.entityType + 'Id=' + this.entityId : '';

	var request = {filter: 'start>=' + this.getStartDate().getTime() + ',start<=' +
	    this.getEndDate().getTime() + entityFilter};

	this.appointmentService.list(request, response => {
	  for (var d in this.days) {
	    this.days[d].appointments = [];
    }
    this.appointments = response.items;
    for (var t = 0; t < this.appointments.length; t++) {
      var appointment = this.appointments[t];
      var day = this.days[this.dateFilter(appointment.start, 'MMdd')];
      if (day) {
        day.appointments.push(appointment);
      }
    }
	});

	var notificationRequest = {
	  startDate: this.getStartDate().getTime(),
    endDate: this.getEndDate().getTime(),
    entityType: this.entityType ? this.entityType.toUpperCase() : 'ALL',
    entityId: this.entityId
  }

  this.notificationService.list(notificationRequest, response => {
    for (var d in this.days) {
      this.days[d].notifications = [];
    }
    this.notifications = response.items;
    for (var t = 0; t < this.notifications.length; t++) {
      var notification = this.notifications[t];
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
  });
}

com.digitald4.iis.CalendarCtrl.prototype.refreshLists = function() {
  var request = this.vendorId ? {filter: 'billing_id=' + this.vendorId} : {};
  this.patientService.list(request, response => {this.patients = response.items});

  this.nurseService.list({}, response => {this.nurses = response.items});
}

com.digitald4.iis.CalendarCtrl.prototype.showAddDialog = function(date) {
  if (!this.patients) {
    this.refreshLists();
  }
  var start = date.getTime() + 10 * ONE_HOUR;
  var end = date.getTime() + 12 * ONE_HOUR;
	this.newAppointment = {start: start, end: end, nurseId: this.nurseId, patientId: this.patientId};
	this.addDialogShown = true;
}

com.digitald4.iis.CalendarCtrl.prototype.closeDialog = function() {
	this.addDialogShown = false;
	this.editDialogShown = false;
}

com.digitald4.iis.CalendarCtrl.prototype.create = function() {
	this.addError = undefined;
	this.appointmentService.create(this.newAppointment, appointment => {
	  this.appointments.push(appointment);
	  var day = this.days[this.dateFilter(appointment.start, 'MMdd')];
    if (day) {
      day.appointments.push(appointment);
    }
    if (this.onUpdate) {
      this.onUpdate();
    }
	  this.closeDialog();
	});
}

com.digitald4.iis.CalendarCtrl.prototype.edit = function(appointment) {
  if (!this.patients) {
    this.refreshLists();
  }
  this.editAppointment = {id: appointment.id, start: appointment.start, end: appointment.end,
      nurseId: appointment.nurseId, patientId: appointment.patientId,
      cancelled: appointment.cancelled, cancelReason: appointment.cancelReason};
	this.origAppointment = appointment;
	this.editDialogShown = true;
}

com.digitald4.iis.CalendarCtrl.prototype.saveEdits = function() {
  var edits = [];
  if (this.editAppointment.start != this.origAppointment.start) {edits.push('start');}
  if (this.editAppointment.end != this.origAppointment.end) {edits.push('end');}
  if (this.editAppointment.nurseId != this.origAppointment.nurseId) {edits.push('nurseId');}
  if (this.editAppointment.patientId != this.origAppointment.patientId) {edits.push('patientId');}
  if (this.editAppointment.cancelled != this.origAppointment.cancelled) {edits.push('cancelled');}
  if (this.editAppointment.cancelReason != this.origAppointment.cancelled) {edits.push('cancelReason');}

	this.updateError = undefined;
	this.appointmentService.update(this.editAppointment, edits, appointment => {
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
    if (this.onUpdate) {
      this.onUpdate();
    }
	  this.closeDialog();
	}, error => {this.updateError = error});
}

com.digitald4.iis.CalendarCtrl.prototype.getMonth = function() {
	return this.month;
}

com.digitald4.iis.CalendarCtrl.prototype.setMonth = function(year, month) {
	this.month = new Date(year, month, 1);
	this.refresh();
}

com.digitald4.iis.CalendarCtrl.prototype.prevMonth = function() {
	this.month.setDate(0);
	this.month.setDate(1);
	this.refresh();
}

com.digitald4.iis.CalendarCtrl.prototype.nextMonth = function() {
	this.month.setDate(32);
	this.month.setDate(1);
	this.refresh();
}

com.digitald4.iis.CalendarCtrl.prototype.getStartDate = function() {
	var startDate = new Date(this.month.getFullYear(), this.month.getMonth(), 1);
	startDate.setTime(startDate.getTime() - (startDate.getDay() * ONE_DAY));
	return startDate;
}

com.digitald4.iis.CalendarCtrl.prototype.getEndDate = function() {
	var endDate = new Date(this.month);
	endDate.setDate(31);
	while (endDate.getMonth() != this.month.getMonth()) {
		endDate.setTime(endDate.getTime() - ONE_DAY);
	}
	endDate.setTime(endDate.getTime() + ((6 - endDate.getDay()) * ONE_DAY));
	return endDate;
}