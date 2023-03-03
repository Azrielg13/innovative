com.digitald4.iis.PatientCtrl =
        function($routeParams, patientService, appointmentService, nurseService, vendorService) {
	var patientId = $routeParams.id;
	this.patientId = parseInt(patientId, 10);
	this.patientService = patientService;
	this.nurseService = nurseService;
	this.vendorService = vendorService;
	this.appointmentService = appointmentService;
	this.tabs = com.digitald4.iis.PatientCtrl.TABS;
	this.TableType = {
		PENDING_ASSESSMENT: {
		  base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: AppointmentState.PENDING_ASSESSMENT + ',patientId=' + patientId},
		COMPLETED_ASSESSMENT: {
		  base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			filter: AppointmentState.COMPLETED_ASSESSMENT + ',patientId=' + patientId}
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
  this.vendorService.list({}, response => {this.vendors = response.items});

  this.patientService.get(this.patientId, patient => {
    patient.serviceAddress = patient.serviceAddress || {};
    patient.primaryPhone = patient.primaryPhone || {};
    patient.alternatePhone = patient.alternatePhone || {};
    patient.emergencyContactPhone = patient.emergencyContactPhone || {};
    this.patient = patient;
  });
}

com.digitald4.iis.PatientCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
	if (tab == com.digitald4.iis.PatientCtrl.TABS.map) {
	  this.loadMap();
	}
}

com.digitald4.iis.PatientCtrl.prototype.update = function(prop) {
	this.patientService.update(this.patient, [prop], patient => {this.patient = patient});
}

com.digitald4.iis.PatientCtrl.prototype.loadMap = function() {
  console.log('Loading map...');
  var serviceAddress = this.patient.serviceAddress;
  this.nurseService.listClosest(serviceAddress.latitude, serviceAddress.longitude, response => {
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
    var closestNurses = response.items;
    for (var x = 0; x < closestNurses.length; x++) {
      var closestNurse = closestNurses[x];
      closestNurse.nurse.address = closestNurse.nurse.address || {};
      new google.maps.Marker({
        position: new google.maps.LatLng(closestNurse.nurse.address.latitude, closestNurse.nurse.address.longitude),
        map: map,
        icon: 'images/icons/nurse24-icon.png',
        title: 'Nurse - ' + closestNurse.nurse.fullName + ' (' + closestNurse.distance + ' miles)'
      });
    }
    this.closestNurses = closestNurses;
    console.log('Map Ready');
  });
}