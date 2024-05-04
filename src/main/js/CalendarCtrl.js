var ONE_HOUR = 1000 * 60 * 60;
var ONE_DAY = 24 * ONE_HOUR;

com.digitald4.iis.CalendarCtrl =
    function($filter, $window, appointmentService, notificationService, nurseService, patientService) {
	this.dateFilter = $filter('date');
	this.window = $window;
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

com.digitald4.iis.CalendarCtrl.prototype.setDisplay = function(appointment) {
	if (this.entityType == 'nurse') {
		appointment.displayValue = appointment.patientName;
	} else if (this.entityType == 'patient') {
		appointment.displayValue = appointment.nurseName;
	} else {
		appointment.displayValue = 'P:' + appointment.patientName + ' N:' + appointment.nurseName;
	}
	return appointment;
}

com.digitald4.iis.CalendarCtrl.prototype.setNames = function(appointment) {
	appointment.nurseName = (this.nurseMap[appointment.nurseId] || {}).fullName;
	var patient = this.patientMap[appointment.patientId];
	appointment.patientName = patient.name;
	appointment.vendorName = patient.billingVendorName;
	return appointment;
}

com.digitald4.iis.CalendarCtrl.prototype.refresh = function() {
	this.setupCalendar();

	var entityFilter = this.entityType ? ',' + this.entityType + 'Id=' + this.entityId : '';

	var request = {pageSize: 1000,
	    filter: 'start>=' + this.getStartDate().getTime() + ',start<=' + this.getEndDate().getTime() + entityFilter};

	this.appointmentService.list(request, response => {
	  for (var d in this.days) {
	    this.days[d].appointments = [];
    }
    this.appointmentCount = response.items.length;
    response.items.forEach(appointment => {
      var day = this.days[this.dateFilter(appointment.start, 'MMdd')];
      if (day) {
        day.appointments.push(this.setDisplay(appointment));
      }
    });
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
    this.notifications.forEach(notification => {
      switch (notification.type) {
        case 'INFO': notification.style = 'border: 0.333em solid #3399cc;'; break;
        case 'WARNING': notification.style = 'border: 0.333em solid #f8ef2f;'; break;
        case 'ERROR': notification.style = 'border: 0.333em solid #eb310b;'; break;
      }
      var day = this.days[this.dateFilter(notification.date, 'MMdd')];
      if (day) {
        day.notifications.push(notification);
      }
    });
  });
}

com.digitald4.iis.CalendarCtrl.prototype.refreshLists = function() {
  var request = {filter: 'status=Active' + (this.vendorId ? ',billingId=' + this.vendorId : ''), pageSize: 500};
  this.patientService.list(request, response => {
  	this.patients = response.items;
  	this.patientMap = {};
  	this.patients.forEach(patient => {this.patientMap[patient.id] = patient});
  });

  this.nurseService.list({filter: 'status=Active'}, response => {
  	this.nurses = response.items;
  	this.nurseMap = {};
  	this.nurses.forEach(nurse => {this.nurseMap[nurse.id] = nurse});
	});
}

com.digitald4.iis.CalendarCtrl.prototype.showAddDialog = function(date) {
  if (!this.patients) {
    this.refreshLists();
  }
  var start = date.getTime() + 10 * ONE_HOUR;
  var end = date.getTime() + 12 * ONE_HOUR;
	this.newAppointment = {start: start, end: end, nurseId: this.nurseId, patientId: this.patientId};
	setDialogStyle(this);
	this.addDialogShown = true;
}

com.digitald4.iis.CalendarCtrl.prototype.closeDialog = function() {
	this.addDialogShown = false;
	this.editDialogShown = false;
}

com.digitald4.iis.CalendarCtrl.prototype.create = function() {
	this.addError = undefined;
	this.appointmentService.create(this.setNames(this.newAppointment), appointment => {
	  var day = this.days[this.dateFilter(appointment.start, 'MMdd')];
    if (day) {
      day.appointments.push(this.setDisplay(appointment));
      this.appointmentCount++;
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
	setDialogStyle(this);
	this.editDialogShown = true;
}

com.digitald4.iis.CalendarCtrl.prototype.saveEdits = function() {
  var edits = [];
  if (this.editAppointment.start != this.origAppointment.start) edits.push('start');
  if (this.editAppointment.end != this.origAppointment.end) edits.push('end');
  if (this.editAppointment.nurseId != this.origAppointment.nurseId) edits.push('nurseId');
  if (this.editAppointment.patientId != this.origAppointment.patientId) edits.push('patientId');
  if (this.editAppointment.cancelled != this.origAppointment.cancelled) edits.push('cancelled');
  if (this.editAppointment.cancelReason != this.origAppointment.cancelReason) edits.push('cancelReason');

	this.updateError = undefined;
	this.appointmentService.update(this.setNames(this.editAppointment), edits, appointment => {
		this.setDisplay(appointment);
    var oldDay = this.days[this.dateFilter(this.origAppointment.start, 'MMdd')];
    var newDay = this.days[this.dateFilter(appointment.start, 'MMdd')];
    if (oldDay && oldDay == newDay) {
      var index = oldDay.appointments.indexOf(this.origAppointment);
      oldDay.appointments[index] = appointment;
    } else {
			if (oldDay) {
				var index = oldDay.appointments.indexOf(this.origAppointment);
				oldDay.appointments.splice(index, 1);
				this.appointmentCount--;
			}
			if (newDay) {
				newDay.appointments.push(appointment);
				this.appointmentCount++;
			}
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