var DAYS_30 = 1000 * 60 * 60 * 24 * 30;
com.digitald4.iis.GeneralData = com.digitald4.iis.GenData;

com.digitald4.iis.IISCtrl = function($scope, $filter, userService, generalDataService,
    appointmentService) {
  this.userService = userService;
  $scope.GenData = com.digitald4.iis.GenData;
  $scope.GeneralData = com.digitald4.iis.GeneralData;
  $scope.generalDataService = generalDataService;
	com.digitald4.iis.TableBaseMeta = {
    NURSES: {title: 'Nurses',
      entity: 'nurse',
      columns: [
        {title: 'Name', prop: 'fullName', url: nurse => {return '#nurse/' + nurse.id}},
        {title: 'Status', value: nurse => {return generalDataService.get(nurse.statusId).name}},
        {title: 'Phone #', prop: 'phoneNumber'},
        {title: 'Email Address', prop: 'email'},
        {title: 'Address', prop: 'address', type: 'address'},
        {title: 'Pending Evaluations', prop: 'pendAssesCount'}]},
    LICENSE_ALERT: {title: 'License Expiration',
      entity: 'license',
      filter: 'expirationDate<' + (Date.now() + DAYS_30),
      columns: [
        {title: 'Nurse', prop: 'nurseName',
          url: nurse => {return '#nurse/' + nurse.id + '/licenses'}},
        {title: 'License', value: lic => {return generalDataService.get(lic.licTypeId).name}},
        {title: 'Status',
          value: lic => {return lic.expirationDate < Date.now() ? 'Expired' : 'Warning';}},
        {title: 'Valid Date', prop: 'validDate', type: 'date'},
        {title: 'Exp Date', prop: 'expirationDate', type: 'date'}]},
    PATIENTS: {title: 'Patients',
      entity: 'patient',
      orderBy: 'name',
      columns: [
        {title: 'Name', prop: 'name', url: patient => {return '#patient/' + patient.id}},
        {title: 'Status', value: patient => {return generalDataService.get(patient.statusId).name}},
        {title: 'Vendor', prop: 'billingVendorName'},
        {title: 'RX', prop: 'rx'},
        {title: 'Diagnosis', value: pat => {return generalDataService.get(pat.diagnosisId).name}}]},
    PENDING_INTAKE: {title: 'Pending Intakes',
      entity: 'patient',
      filter: 'referralResolutionId=885',
      columns: [
        {title: 'Name', prop: 'name', url: patient => {return '#patient/' + patient.id;}},
        {title: 'Referral Source', prop: 'referralSourceName'},
        {title: 'Billing Vendor', prop: 'billingVendorName'},
        {title: 'Diagnosis', value: pat => {return generalDataService.get(pat.diagnosisId).name}},
        {title: 'Referral Date', prop: 'referralDate', type: 'date'},
        {title: 'Start Date', prop: 'startOfCareDate', type: 'date'}]},
    USERS: {title: 'Users',
      entity: 'user',
      columns: [
        {title: 'Name', prop: 'fullName', url: user => {return '#user/' + user.id}},
        {title: 'Type', prop: 'typeId'},
        {title: 'Email Address', prop: 'email'},
        {title: 'Disabled', prop: 'disabled'},
        {title: 'Last Login', prop: 'lastLogin', type: 'datetime'},
        {title: 'Notes', prop: 'notes'}]},
    VENDORS: {title: 'Vendors',
      entity: 'vendor',
      columns: [
        {title: 'Vendor', prop: 'name', url: vendor => {return '#vendor/' + vendor.id}},
        {title: 'Address', prop: 'address', type: 'address'},
        {title: 'Fax Number', prop: 'faxNumber'},
        {title: 'Contact Name', prop: 'contactName'},
        {title: 'Contact Phone', prop: 'contactNumber'},
        {title: 'Pending Assessments', prop: 'pendAssesCount'}]},
    UNCONFIRMED: {title: 'Unconfirmed Appointments',
      entity: 'appointment',
      filter: 'state=UNCONFIRMED',
      columns: [
        {title: 'Nurse', prop: 'nurseName',
          url: appointment => {return '#nurse/' + appointment.nurseId + '/unconfirmed'}},
        {title: 'Patient', prop: 'patientName',
          url: appointment => {return '#patient/' + appointment.patientId}},
        {title: 'Start Time', prop: 'start', type: 'datetime'},
        /* {title: 'Confirmation Request',
          button: {display: 'Send Request', disabled: app => {return app.state != 'UNCONFIRMED'},
            action: appointment => {console.log('Confirmation request sent')}}}, */
        {title: 'Confirm',
          button: {display: 'Set Confirmed', disabled: app => {return app.state != 'UNCONFIRMED'},
            action: appointment => {
              appointment.state = 'CONFIRMED';
              appointmentService.update(appointment, ['state'], update => {});
            }}}]},
    PENDING_ASSESSMENT: {title: 'Pending Assessment',
      entity: 'appointment',
      filter: AppointmentState.PENDING_ASSESSMENT,
      columns: [
        {title: 'Patient', prop: 'patientName',
          url: appointment => {return '#assessment/' + appointment.id}},
        {title: 'Date', prop: 'start', type: 'datetime'},
        {title: 'Time In',
          value: app => {return $filter('date')(app.timeIn || app.start, 'shortTime')}},
        {title: 'Time Out',
          value: app => {return $filter('date')(app.timeOut || app.end, 'shortTime')}},
        {title: 'Percent Complete', prop: 'assPercentComplete', type: 'percent'}]},
    REVIEWABLE: {title: 'Awaiting Review',
      entity: 'appointment',
      filter: 'state=PENDING_APPROVAL',
      columns: [
        {title: 'Patient', prop: 'patientName',
          url: appointment => {return '#assessment/' + appointment.id}},
        {title: 'Nurse', prop: 'nurseName'},
        {title: 'Date', prop: 'start', type: 'datetime'},
        {title: 'Hours', prop: 'loggedHours'},
        {title: 'Mileage', prop: 'mileage'},
        {title: 'Percent Complete', prop: 'assPercentComplete', type: 'percent'}]},
    BILLABLE: {title: 'Billable',
      entity: 'appointment',
      filter: AppointmentState.BILLABLE,
      columns: [
        {title: 'Vendor', prop: 'vendorName',
          url: appointment => {
            appointment.billingInfo = appointment.billingInfo || {};
            appointment.billingHours = appointment.billingInfo.hours;
            appointment.billingRate = appointment.billingInfo.hourlyRate;
            appointment.billingFlat = appointment.billingInfo.flatRate;
            appointment.billingMileage = appointment.billingInfo.mileageTotal;
            appointment.billingTotal = appointment.billingInfo.total;
            return '#vendor/' + appointment.vendorId + '/billable';}},
        {title: 'Date', prop: 'start', type: 'datetime'},
        {title: 'Billing Hours', prop: 'billingHours'},
        {title: 'Billing Rate', prop: 'billingRate', type: 'currency'},
        {title: 'Visit Pay', prop: 'billingFlat', type: 'currency'},
        {title: 'Billing Mileage', prop: 'billingMileage', type: 'currency'},
        {title: 'Total Payment', prop: 'billingTotal', type: 'currency'}]},
    PAYABLE: {title: 'Payable',
      entity: 'appointment',
      filter: AppointmentState.PAYABLE,
      columns: [
        {title: 'Nurse', prop: 'nurseName', url: appointment => {
          appointment.paymentInfo = appointment.paymentInfo || {};
          appointment.payHours = appointment.paymentInfo.hours;
          appointment.payRate = appointment.paymentInfo.hourlyRate;
          appointment.payFlat = appointment.paymentInfo.flatRate;
          appointment.payMileage = appointment.paymentInfo.mileageTotal;
          appointment.payTotal = appointment.paymentInfo.total;
          return '#nurse/' + appointment.nurseId + '/payable';}},
        {title: 'Date', prop: 'start', type: 'date'},
        {title: 'Hours', prop: 'payHours'},
        {title: 'Pay Rate', prop: 'payRate', type: 'currency'},
        {title: 'Visit Pay', prop: 'payFlat', type: 'currency'},
        {title: 'Mileage', prop: 'payMileage', type: 'currency'},
        {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]},
    UNPAID_INVOICES: {title: 'Unpaid Invoices',
      entity: 'invoice',
      filter: 'statusId=1521',
      columns: [
        {title: 'Name', prop: 'name',
          url: invoice => {return '#vendor/' + invoice.vendorId + '/invoices'}},
        {title: 'Date', prop: 'generationTime', type: 'date',
            imageLink: {src: 'images/icons/fugue/document-pdf.png',
                url: invoice => {return userService.getFileUrl(invoice.fileReference)}}},
        {title: 'Billed', prop: 'totalDue', type: 'currency'},
        {title: 'Status', value: i => {return generalDataService.get(i.statusId).name}},
        {title: 'Comment', prop: 'comment', editable: true},
        {title: 'Received', prop: 'totalPaid', editable: true}]},
    PAID_INVOICES: {title: 'Paid Invoices',
      entity: 'invoice',
      filter: 'statusId=1520',
      columns: [
        {title: 'Name', prop: 'name', url: i => {return '#vendor/' + i.vendorId + '/invoices'}},
        {title: 'Date', prop: 'generationTime', type: 'date',
          imageLink: {src: 'images/icons/fugue/document-pdf.png',
            url: invoice => {return userService.getFileUrl(invoice.fileReference)}}},
        {title: 'Billed', prop: 'totalDue', type: 'currency'},
        {title: 'Status', value: i => {return generalDataService.get(i.statusId).name}},
        {title: 'Comment', prop: 'comment', editable: true},
        {title: 'Received', prop: 'totalPaid', editable: true}]},
    PAY_HISTORY: {title: 'Pay History',
      entity: 'paystub',
      columns: [
        {title: 'Nurse', prop: 'nurseName',
          url: paystub => {return '#nurse/' + paystub.nurseId + '/payHistory'}},
        {title: 'Pay Date', prop: 'payDate', type: 'date',
          imageLink: {src: 'images/icons/fugue/document-pdf.png',
            url: paystub => {return userService.getFileUrl(paystub.fileReference)}}},
        {title: 'Gross', prop: 'grossPay', type: 'currency'},
        {title: 'Deductions',
          value: p => {return $filter('currency')(p.preTaxDeductions + p.postTaxDeductions)}},
        {title: 'Taxes', prop: 'taxTotal', type: 'currency'},
        {title: 'Mileage Reimbursement', prop: 'payMileage', type: 'currency'},
        {title: 'Net Pay', prop: 'netPay', type: 'currency'}]},
    CHANGE_HISTORY: {title: 'Change History',
        entity: 'changeHistory',
        orderBy: 'timeStamp DESC',
        columns: [
          {title: 'Type', prop: 'entityType',
            url: ch => {return '#' + ch.entityType + '/' + ch.entityId + '/changeHistory'}},
          {title: 'Id', prop: 'entityId', },
          {title: 'Action', prop: 'action'},
          {title: 'Date', prop: 'timeStamp', type: 'datetime'},
          {title: 'User', prop: 'username'}]},
    REPORTS: {title: 'Reports',
      entity: 'file',
      columns: [
        {title: 'Name', prop: 'name',
          imageLink: {src: 'images/icons/fugue/document-pdf.png',
            url: report => {return userService.getFileUrl(report.dataFile)}}},
        {title: 'Date', prop: 'generationTime', type: 'date'},
        {title: 'Comment', prop: 'comment', editable: true}]}
	};
	$scope.TableType = com.digitald4.iis.TableBaseMeta;
}

com.digitald4.iis.IISCtrl.prototype.logout = function() {
  this.userService.logout();
}
