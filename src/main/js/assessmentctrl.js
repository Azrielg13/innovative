com.digitald4.iis.AssessmentCtrl = function($scope, $routeParams, appointmentService, generalDataService) {
  this.scope = $scope;
  this.appointmentId = parseInt($routeParams.id, 10);
	this.appointmentService = appointmentService;
	this.generalDataService = generalDataService;
	this.setupTabs();
	this.refresh();
	this.setSelectedTab($routeParams.tab || 'general');
};

com.digitald4.iis.AssessmentCtrl.prototype.setSelectedTab = function(tab) {
	this.selectedTab = tab;
};

com.digitald4.iis.AssessmentCtrl.prototype.setupTabs = function() {
  var generalDatas = this.generalDataService.list(com.digitald4.iis.GenData.ASS_CAT);
  if (!generalDatas) {
    console.log('Waiting for general data');
    this.needsApply = true;
    setTimeout(function() {
      this.setupTabs();
    }.bind(this), 1000);
  } else {
    this.assCats = [];
    for (var i = 0; i < generalDatas.length; i++) {
      var assCat = {name: generalDatas[i].name.replace(' ', '_'), title: generalDatas[i].name, assessments: []};
      for (var j = 0; j < generalDatas[i].generalDatas.length; j++) {
        var assGD = generalDatas[i].generalDatas[j];
        var ass = {id: assGD.id, name: assGD.name, data: assGD.data, options: []};
        for (var k = 0; k < assGD.generalDatas.length; k++) {
          ass.options.push({id: assGD.generalDatas[k].name, name: assGD.generalDatas[k].name});
        }
        assCat.assessments.push(ass);
      }
      this.assCats.push(assCat);
    }
    if (this.needsApply) {
      this.needsApply = undefined;
      this.scope.$apply();
    }
  }
}

com.digitald4.iis.AssessmentCtrl.prototype.setAppointment = function(appointment) {
  this.appointment = {};
  for (var prop in appointment) {
    this.appointment[prop] = appointment[prop];
  }
  this.appointment.assessment = {};
  if (appointment.assessment) {
    for (var ass in appointment.assessment) {
      if (appointment.assessment[ass].indexOf('[') == 0) {
        this.appointment.assessment[ass] = JSON.parse(appointment.assessment[ass]);
      } else {
        this.appointment.assessment[ass] = appointment.assessment[ass];
      }
    }
  }
};

com.digitald4.iis.AssessmentCtrl.prototype.refresh = function() {
	this.appointmentService.get(this.appointmentId, this.setAppointment.bind(this), notify);
};

com.digitald4.iis.AssessmentCtrl.prototype.updateAppointment = function(prop) {
  var appointment = {id: this.appointment.id};
  if (prop == 'assessment') {
    appointment.assessment = {};
    for (var ass in this.appointment.assessment) {
      if (typeof(this.appointment.assessment[ass]) == 'object') {
        appointment.assessment[ass] = JSON.stringify(this.appointment.assessment[ass]);
      } else {
        appointment.assessment[ass] = this.appointment.assessment[ass];
      }
    }
  } else {
    appointment = this.appointment;
  }
	this.appointmentService.update(appointment, [prop], this.setAppointment.bind(this), notify);
};
