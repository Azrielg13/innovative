com.digitald4.iis.AppointmentsCtrl =
    function($filter, appointmentService, nurseService, patientService, vendorService) {
  this.dateFilter = $filter('date');
  this.appointmentService = appointmentService;
  this.dateRange = {};
  this.showEnabled = () => this.filterValue && this.groupBy;
  this.entityLists = {};
  this.entityMaps = {};
  this.entityTypes = {
      Nurse: {name: 'Nurse', entity: 'nurse', service: nurseService},
      Patient: {name: 'Patient', entity: 'patient', service: patientService},
      Vendor: {name: 'Vendor', entity: 'vendor', service: vendorService}};
}

com.digitald4.iis.AppointmentsCtrl.prototype.filterTypeChanged = function() {
  this.entityList = [];
  this.filterValue = undefined;
  var entity = this.filterType.entity;
  if (!this.entityLists[entity]) {
    this.entityMaps[entity] = this.entityMaps[entity] || {};
    this.filterType.service.list({filter: 'status=Active'},
        response => {
          this.entityList = this.entityLists[entity] = response.items;
          response.items.forEach(e => this.entityMaps[entity][e.id] = e);
        });
  } else {
    this.entityList = this.entityLists[entity];
  }
}

com.digitald4.iis.AppointmentsCtrl.prototype.refresh = function() {
  var filter = this.filterType.entity + 'Id=' + this.filterValue;
	if (this.dateRange.start) {
	  filter += ',date>=' + this.dateRange.start;
	}
	if (this.dateRange.end) {
	  filter += ',date<=' + this.dateRange.end;
  }
  var groupEntity = this.groupBy.entity;
  var prop = groupEntity + 'Id';
  var display = groupEntity + 'Name';

  this.appointmentService.list({filter: filter}, response => {
    var groups = {};
    this.entityMaps[groupEntity] = this.entityMaps[groupEntity] || {};
    var entityMap = this.entityMaps[groupEntity];
    var fetch = [];
    var fetchMap = {};
    response.items.forEach(appointment => {
      var id = appointment[prop];
      var group = groups[id];
      if (!entityMap[id] && !fetchMap[id]) {
        fetch.push(id);
        fetchMap[id] = id;
      }
      if (!group) {
        group = groups[id] = {name: appointment[display], entity: entityMap[id],
            link: '#' + groupEntity + '/' + id, appointments: []};
      }
      group.appointments.push(appointment);
    });
    if (fetch.length > 0) {
      this.groupBy.service.batchGet(fetch,
          response => response.items.forEach(
              entity => groups[entity.id].entity = entityMap[entity.id] = entity));
    }
    this.groups = groups;
  });
}