var ONE_HOUR = 1000 * 60 * 60;
var ONE_DAY = 24 * ONE_HOUR;

com.digitald4.iis.CalendarCtrl = function($filter, $window, globalData, appointmentService,
    notificationService, nurseService, patientService) {
	this.dateFilter = $filter('date');
	this.window = $window;
	this.appointmentService = appointmentService;
	this.notificationService = notificationService;
	this.nurseService = nurseService;
	this.patientService = patientService;
	this.repeatEnabled = true;
	this.repeatOptions = ['Does_not_repeat', 'Daily', 'Weekly_on_same_day', 'Monthly_on_same_day',
	    'Every_weekday', 'Weekly_on_days', 'Every_N_days', 'Every_N_Weeks', 'Every_N_Months'];
	this.daysOptions = [{id: 1, name: 'Su'}, {id: 2, name: 'M'}, {id: 3, name: 'Tu'},
	    {id: 4, name: 'W'}, {id: 5, name: 'Th'}, {id: 6, name: 'F'}, {id: 7, name: 'S'}];
	this.role = globalData.activeSession.user.role;
	if (this.role == 'Nurse') {
	  this.entityType = 'nurse';
	  this.entityId = globalData.activeSession.user.id;
	  this.addEnabled = false;
	  this.editDisabled = true;
	} else {
	  this.addEnabled = true;
	  this.editDisabled = false;
	}
  if (this.entityType == 'nurse') {this.nurseId = this.entityId};
  if (this.entityType == 'patient') {this.patientId = this.entityId};
  if (this.entityType == 'vendor') {this.vendorId = this.entityId};
	var today = new Date();
	this.setMonth(today.getFullYear(), today.getMonth());
}

com.digitald4.iis.CalendarCtrl.prototype.needsNumber = function(type) {
  return type != undefined && type.startsWith('Every_N_');
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

com.digitald4.iis.CalendarCtrl.prototype.refresh = function() {
	this.setupCalendar();

	var entityFilter = this.entityType ? ',' + this.entityType + 'Id=' + this.entityId : '';

	var request = {pageSize: 1000,
	    filter: 'start>=' + this.getStartDate().getTime() + ',start<=' + this.getEndDate().getTime() + entityFilter};
	    // filter: 'date>=' + this.getStartDate().getTime() + ',date<=' + this.getEndDate().getTime() + entityFilter};

	this.appointmentService.list(request, response => {
	  for (var d in this.days) {
	    this.days[d].appointments = [];
    }
    this.appointmentCount = response.items.length;
    response.items.forEach(appointment => {
      switch (appointment.state) {
        case 'PENDING_ASSESSMENT': appointment.style = 'border: 0.333em solid #eb310b;'; break;
        case 'PENDING_APPROVAL': appointment.style = 'border: 0.333em solid #f28c28;'; break;
        case 'BILLABLE_AND_PAYABLE': appointment.style = 'border: 0.333em solid #f8ef2f;'; break;
        case 'CLOSED': appointment.style = 'border: 0.333em solid #3399cc;'; break;
        case 'DELETED': appointment.cancelled = true; break;
      }
      var day = this.days[this.dateFilter(appointment.date, 'MMdd')];
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
  var request = {filter: 'status=Active' + (this.vendorId ? ',billingVendorId=' + this.vendorId : ''), pageSize: 500};
  this.patientService.list(request, response => {
  	this.patients = response.items;
  });

  this.nurseService.list({filter: 'status=Active'}, response => {
  	this.nurses = response.items;
	});
}

com.digitald4.iis.CalendarCtrl.prototype.showAddDialog = function(date) {
  if (!this.patients) {
    this.refreshLists();
  }
  var startTime = (7 + 8) * ONE_HOUR;
  var endTime = (9 + 8) * ONE_HOUR;
	this.newAppointment = {date: date.getTime(), startTime: startTime, endTime: endTime,
	    nurseId: this.nurseId, patientId: this.patientId, repeat: {type: 'Does_not_repeat'}};
	setDialogStyle(this);
	this.addDialogShown = true;
}

com.digitald4.iis.CalendarCtrl.prototype.showDeleteDialog = function(date) {
  this.deleteDialogShown = true;
  this.editDialogShown = false;
}

com.digitald4.iis.CalendarCtrl.prototype.closeDialog = function() {
	this.addDialogShown = this.deleteDialogShown = false;
}

com.digitald4.iis.CalendarCtrl.prototype.create = function() {
	this.addError = undefined;
	this.appointmentService.batchCreate([this.newAppointment], response => {
	  response.items.forEach(appointment => {
      var day = this.days[this.dateFilter(appointment.date, 'MMdd')];
      if (day) {
        day.appointments.push(this.setDisplay(appointment));
        this.appointmentCount++;
      }
    });
    if (this.onUpdate) {
      this.onUpdate();
    }
    this.closeDialog();
	});
}

com.digitald4.iis.CalendarCtrl.prototype.patientSelected = function() {
  this.newAppointment.patientId = this.newAppointment._patient.id;
  this.newAppointment.titration = this.newAppointment._patient.titration;
}

com.digitald4.iis.CalendarCtrl.prototype.edit = function(appointment) {
  this.dialogRequest = {
    original: appointment,
    entity: JSON.parse(JSON.stringify(appointment)),
    shown: true,
    postUpdate: transaction => this.postUpdate(transaction),
    postDelete: response => this.postDelete(response),
  }
}

com.digitald4.iis.CalendarCtrl.prototype.postUpdate = function(transaction) {
  var appointment = JSON.parse(JSON.stringify(transaction.entity));
  var original = transaction.original;
  this.setDisplay(appointment);
  var oldDay = this.days[this.dateFilter(original.date, 'MMdd')];
  var newDay = this.days[this.dateFilter(appointment.date, 'MMdd')];
  if (oldDay && oldDay == newDay) {
    var index = oldDay.appointments.indexOf(original);
    oldDay.appointments[index] = appointment;
  } else {
    console.log('date changed');
    if (oldDay) {
      var index = oldDay.appointments.indexOf(original);
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
  this.dialogRequest.original = appointment;
}

com.digitald4.iis.CalendarCtrl.prototype.postDelete = function(response) {
  if (response.items.length == 1) {
    var day = this.days[this.dateFilter(this.appointment.date, 'MMdd')];
    if (day) {
      var index = day.appointments.indexOf(this.dialogRequest.original);
      oldDay.appointments.splice(index, 1);
      this.appointmentCount--;
    }
  } else {
    this.refresh();
  }
  if (this.onUpdate) {
    this.onUpdate();
  }
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
	var date = new Date(this.month.getFullYear(), this.month.getMonth(), 1);
	date.setTime(date.getTime() - (date.getDay() * ONE_DAY));
	return date;
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