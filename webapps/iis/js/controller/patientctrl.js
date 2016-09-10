com.digitald4.iis.PatientCtrl = function($routeParams, $filter, restService) {
  this.filter = $filter;
	var patientId = $routeParams.id;
	this.patientId = parseInt(patientId, 10);
	this.TableType = {
		PENDING_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			request: {query_param: [{column: 'patient_id', operan: '=', value: patientId},
			                        {column: 'state', operan: '=', value: AppointmentState.AS_PENDING_ASSESSMENT.toString()}]}},
		COMPLETED_ASSESSMENT: {base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
			request: {query_param: [{column: 'patient_id', operan: '=', value: patientId},
			                        {column: 'state', operan: '>', value: AppointmentState.AS_PENDING_ASSESSMENT.toString()}]}}
	};
	this.patientService = new com.digitald4.common.ProtoService('patient', restService);
	this.nurseService = new com.digitald4.common.ProtoService('nurse', restService);
	this.appointmentService = new com.digitald4.common.ProtoService('appointment', restService);
	this.selectedTab = this.TABS.general;
	this.refresh();
};

com.digitald4.iis.PatientCtrl.prototype.TABS = {
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
	if (tab == this.TABS.map) {
	  this.loadMap();
	}
};

com.digitald4.iis.PatientCtrl.prototype.update = function(prop) {
	this.patientService.update(this.patient, prop, function(patient) {
		this.patient = patient;
	}.bind(this), notify);
};

com.digitald4.iis.PatientCtrl.prototype.updateAddress = function(place) {
	this.patient.address = place.formatted_address;
	this.update('address');
	this.patient.latitude = place.geometry.location.lat();
	this.update('latitude');
	this.patient.longitude = place.geometry.location.lng();
	this.update('longitude');
};

com.digitald4.iis.PatientCtrl.prototype.loadMap = function() {
  this.nurseService.list({}, function(nurses) {
    console.log('loading map...');
    var latLng = new google.maps.LatLng(this.patient.latitude, this.patient.longitude);
    var mapOptions = {
      center: latLng,
      zoom: 9,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
    var beachMarker = new google.maps.Marker({
      position: latLng,
      map: map,
      icon: 'images/icons/patient_24.png',
      title: 'Patient - ' + this.patient.name
    });
    for (var x = 0; x < nurses.length; x++) {
      var nurse = nurses[x];
      nurse.name = nurse.first_name + ' ' + nurse.last_name;
      new google.maps.Marker({
        position: new google.maps.LatLng(nurse.latitude, nurse.longitude),
        map: map,
        icon: 'images/icons/nurse24-icon.png',
        title: 'Nurse - ' + nurse.name + ' (' + nurse.distance + ' miles)'
      });
    }
  }.bind(this), notify);
};