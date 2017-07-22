com.digitald4.iis.PatientCtrl = function($routeParams, patientService, appointmentService, nurseService) {
	var patientId = $routeParams.id;
	this.patientId = parseInt(patientId, 10);
	this.patientService = patientService;
	this.nurseService = nurseService;
	this.appointmentService = appointmentService;
	this.tabs = com.digitald4.iis.PatientCtrl.TABS;
	this.TableType = {
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: {'patient_id': patientId,
			          'state': AppointmentState.AS_PENDING_ASSESSMENT}},
		COMPLETED_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: {'patient_id': patientId,
			          'state': '>' + AppointmentState.AS_PENDING_ASSESSMENT}}
	};
	this.refresh();
	this.setSelectedTab(this.tabs[$routeParams.tab] || this.tabs.general);
};

com.digitald4.iis.PatientCtrl.TABS = {
	calendar: 'Calendar',
	general: 'General',
	map: 'Map',
	pending: 'Pending Assessment',
	completed: 'Completed Assessments'
};
com.digitald4.iis.PatientCtrl.prototype.patientId;
com.digitald4.iis.PatientCtrl.prototype.patientService;
com.digitald4.iis.PatientCtrl.prototype.patient;
com.digitald4.iis.PatientCtrl.prototype.selectedTab;

com.digitald4.iis.PatientCtrl.prototype.refresh = function() {
	this.patientService.get(this.patientId, function(patient) {
		this.patient = patient;
	}.bind(this), notify);
};

com.digitald4.iis.PatientCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
	if (tab == com.digitald4.iis.PatientCtrl.TABS.map) {
	  this.loadMap();
	}
};

com.digitald4.iis.PatientCtrl.prototype.update = function(prop) {
	this.patientService.update(this.patient, [prop], function(patient) {
		this.patient = patient;
	}.bind(this), notify);
};

com.digitald4.iis.PatientCtrl.prototype.loadMap = function() {
  console.log('Loading map...');
  var serviceAddress = this.patient.serviceAddress;
  this.nurseService.listClosest(serviceAddress.latitude, serviceAddress.longitude, function(response) {
    var latLng = new google.maps.LatLng(serviceAddress.latitude, serviceAddress.longitude);
    var mapOptions = {
      center: latLng,
      zoom: 9,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
    new google.maps.Marker({
      position: latLng,
      map: map,
      icon: 'images/icons/patient_24.png',
      title: 'Patient - ' + this.patient.name
    });
    var nurses = response.result;
    for (var x = 0; x < nurses.length; x++) {
      var nurse = nurses[x];
      nurse.name = nurse.firstName + ' ' + nurse.lastName;
      nurse.address = nurse.address || {};
      new google.maps.Marker({
        position: new google.maps.LatLng(nurse.address.latitude, nurse.address.longitude),
        map: map,
        icon: 'images/icons/nurse24-icon.png',
        title: 'Nurse - ' + nurse.name + ' (' + nurse.distance + ' miles)'
      });
    }
    this.closestNurses = nurses;
    console.log('Map Ready');
  }.bind(this), notify);
};