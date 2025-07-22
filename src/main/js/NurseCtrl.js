var TableBaseMeta = {PAYABLE: {title: 'Payable', entity: 'appointment',
      columns: [
      	{title: 'Patient', prop: 'patientName', url: appointment => {return '#patient/' + appointment.patientId}},
        {title: 'Date', prop: 'date', type: 'date'},
        {title: 'Payment Type', prop: 'payingTypeId', editable: true},
        {title: 'Pay Hours', prop: 'payHours', editable: true},
        {title: 'Hourly Rate', prop: 'payRate', editable: true},
        {title: 'Visit Pay', prop: 'payFlat', editable: true},
        {title: 'Pay Mileage', prop: 'payMileage', editable: true},
        {title: 'Mileage Rate', prop: 'mileageRate', editable: true},
        {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]}};

com.digitald4.iis.NurseCtrl = function($routeParams, $filter, appointmentService, flags,
    generalDataService, noteService, nurseService, serviceCodeService, userService) {
  this.filter = $filter;
  this.nurseId = parseInt($routeParams.id, 10);
  this.nurseStatuses = enums.EmployeeStatus;
  this.payPreferences = enums.PayPreferences;
  this.appointmentService = appointmentService;
  this.flags = flags;
  this.generalDataService = generalDataService;
  this.noteService = noteService;
  this.nurseService = nurseService;
  this.serviceCodeService = serviceCodeService;
  this.userService = userService;
  this.tabs = {
    calendar: {name: 'Calendar', isEnabled: () => flags.calendarEnabled},
    general: {name: 'General', isEnabled: () => true},
    licenses: {name: 'Licenses', isEnabled: () => flags.licenseAlertEnabled},
    payCodes: {name: 'Pay Codes', isEnabled: () => flags.payCodesEnabled},
    appointments: {name: 'Appointments', isEnabled: () => flags.appointmentsEnabled},
    pending: {name: 'Pending Assessment', isEnabled: () => flags.pendingAssessmentsEnabled},
    reviewable: {name: 'Awaiting Review', isEnabled: () => flags.pendingAssessmentsEnabled},
    payable: {name: 'Payable', isEnabled: () => flags.billableEnabled},
    payHistory: {name: 'Pay History', isEnabled: () => flags.nursePayEnabled},
    notes: {name: 'Notes', isEnabled: () => flags.nurseNotesEnabled},
    changeHistory: {name: 'Change History', isEnabled: () => flags.nurseChangeHistoryEnabled}
  }
  this.TableType = {
    APPOINTMENTS: {
      base: com.digitald4.iis.TableBaseMeta.APPOINTMENTS,
      filter: 'nurseId=' + this.nurseId},
    PENDING_ASSESSMENT: {
      base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
      filter: AppointmentState.PENDING_ASSESSMENT + ',nurseId=' + this.nurseId},
    REVIEWABLE: {
      base: com.digitald4.iis.TableBaseMeta.REVIEWABLE,
      filter: AppointmentState.PENDING_APPROVAL + ',nurseId=' + this.nurseId},
	  PAYABLE: {
	    base: TableBaseMeta.PAYABLE,
	    filter: AppointmentState.PAYABLE + ',nurseId=' + this.nurseId},
	  PAY_CODES: {
	    base: com.digitald4.iis.TableBaseMeta.PAY_CODES,
	    filter: 'nurseId=' + this.nurseId},
	  PAY_HISTORY: {
	    base: com.digitald4.iis.TableBaseMeta.PAY_HISTORY,
	    filter: 'nurseId=' + this.nurseId},
    NOTES: {
      base: com.digitald4.iis.TableBaseMeta.NOTES,
      filter: 'entityType=Nurse,entityId=' + this.nurseId},
    CHANGE_HISTORY: {
      base: com.digitald4.iis.TableBaseMeta.CHANGE_HISTORY,
      filter: 'entityType=Nurse,entityId=' + this.nurseId}
  }

  var eventClicked = (event, jsEvent, view) => {
    console.log('Click event: ' + event.title);
  }

  /* Render Tooltip */
  var eventRender = (event, element, view) => {
    element.attr({'tooltip': event.title, 'tooltip-append-to-body': true});
    // $compile(element)($scope);
  }

  var viewRender = (view, element) => {this.refreshAppointments(view.start, view.end)};

  this.uiConfig = {
    calendar:{
      height: 450,
      editable: false,
      header: {
        left: 'title',
        center: '',
        right: 'today prev,next'
      },
      eventClick: eventClicked,
      eventRender: eventRender,
      viewRender: viewRender
    }
  }
  this.eventSources = [this.events];
	this.refresh();
	this.setSelectedTab(this.tabs[$routeParams.tab] || ($routeParams.tab || this.tabs.general));
}

com.digitald4.iis.NurseCtrl.prototype.refresh = function() {
  this.nurseService.get(this.nurseId, nurse => {this.nurse = nurse});
}

com.digitald4.iis.NurseCtrl.prototype.refreshAppointments = function(startDate, endDate) {
  var request = {filter:
      'nurseId=' + this.nurseId + ',start>=' + startDate.valueOf() + ',start<=' + endDate.valueOf()};
	this.appointmentService.list(request, response => {
    this.events.length = 0;
    var appointments = response.items;
    for (var a = 0; a < appointments.length; a++) {
      var appointment = appointments[a];
      this.events.push({id: appointment.id,
          title: this.filter('date')(appointment.startTime, 'shortTime') + ' ' + appointment.patientName,
          start: new Date(appointment.date + appointment.startTime),
          end: new Date(appointment.date + appointment.endTime),
          appointment: appointment,
          className: ['appointment']
      });
    }
  });
}

com.digitald4.iis.NurseCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
}

com.digitald4.iis.NurseCtrl.prototype.update = function(prop) {
	this.nurseService.update(this.nurse, [prop], nurse => {this.nurse = nurse});
}

com.digitald4.iis.NurseCtrl.prototype.setPassword = function() {
  if (this.password != this.confirmation) {
    alert('Confirmation does not match!');
    return;
  }

  this.userService.setPassword(
      this.nurse.username, this.password, response => {alert('Password updated successfully')});
}
