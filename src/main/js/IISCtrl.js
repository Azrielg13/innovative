com.digitald4.iis.GeneralData = com.digitald4.iis.GenData;

com.digitald4.iis.IISCtrl = function($scope, $filter,
    appointmentService, flags, flagService, generalDataService, noteService, userService) {
  this.userService = userService;
  this.flags = flags;
  flags.nursePayEnabled = true;
  flags.vendorBillingEnabled = true;
  $scope.ONE_MONTH = ONE_MONTH;
  $scope.GenData = com.digitald4.iis.GenData;
  $scope.GeneralData = com.digitald4.iis.GeneralData;
  $scope.generalDataService = generalDataService;
  $scope.enums = enums;
	com.digitald4.iis.TableBaseMeta = {
    NURSES: {
      title: 'Nurses',
      entity: 'nurse',
      orderBy: 'fullName',
      columns: [
        {title: 'First Name', prop: 'firstName', url: nurse => {return '#nurse/' + nurse.id}},
        {title: 'Last Name', prop: 'lastName', url: nurse => {return '#nurse/' + nurse.id}},
        {title: 'Status', prop: 'status', filterOptions: enums.EmployeeStatus, filter: 'Active'},
        {title: 'Phone #', prop: 'phoneNumber'},
        {title: 'Email Address', prop: 'email'},
        {title: 'Address', prop: 'address', type: 'address'},
        {title: 'Pending Evaluations', prop: 'pendAssesCount'}]},
    LICENSE_ALERT: {
      title: 'License Expiration',
      entity: 'license',
      filter: 'nurseStatus=Active,expirationDate<' + (Date.now() + DAYS_90),
      orderBy: 'expirationDate DESC',
      columns: [
        {title: 'Nurse', prop: 'nurseName', url: nurse => {return '#nurse/' + nurse.id + '/licenses'}},
        {title: 'License', value: lic => {return generalDataService.get(lic.licTypeId).name}},
        {title: 'Status', value: lic => {
            if (lic.expirationDate > Date.now() + DAYS_30) {
              return 'Info';
            }
            return lic.expirationDate < Date.now() ? 'Expired' : 'Warning';
        }},
        {title: 'Exp Date', prop: 'expirationDate', type: 'date'},
        {title: 'Valid Date', prop: 'validDate', type: 'date'}]},
    PATIENTS: {title: 'Patients',
      entity: 'patient',
      pageSize: '100',
      orderBy: 'lastName',
      columns: [
        {title: 'First Name', prop: 'firstName', url: patient => {return '#patient/' + patient.id}},
        {title: 'Last Name', prop: 'lastName', url: patient => {return '#patient/' + patient.id}},
        {title: 'Status', prop: 'status', filterOptions: enums.PatientStatus, filter: 'Active'},
        {title: 'Vendor', prop: 'billingVendorName'},
        {title: 'RX', prop: 'rx'},
        {title: 'Diagnosis', prop: 'diagnosis'}]},
    PENDING_INTAKE: {title: 'Pending Intakes',
      entity: 'patient',
      filter: 'status=Pending',
      columns: [
        {title: 'Name', prop: 'fullName', url: patient => {return '#patient/' + patient.id;}},
        {title: 'Referral Source', prop: 'referralSourceName'},
        {title: 'Billing Vendor', prop: 'billingVendorName'},
        {title: 'Diagnosis', prop: 'diagnosis'},
        {title: 'Referral Date', prop: 'referralDate', type: 'date'},
        {title: 'Start Date', prop: 'startOfCareDate', type: 'date'}]},
    USERS: {title: 'Users',
      entity: 'user',
      columns: [
        {title: 'First Name', prop: 'firstName', url: user => {return '#user/' + user.id}},
        {title: 'Last Name', prop: 'lastName', url: user => {return '#user/' + user.id}},
        {title: 'Status', prop: 'status', filterOptions: enums.EmployeeStatus, filter: 'Active'},
        {title: 'Phone #', prop: 'phoneNumber'},
        {title: 'Username', prop: 'username'},
        {title: 'Email Address', prop: 'email'},
        {title: 'Last Login', prop: 'lastLogin', type: 'datetime'}]},
    VENDORS: {title: 'Vendors',
      entity: 'vendor',
      columns: [
        {title: 'Vendor', prop: 'name', url: vendor => {return '#vendor/' + vendor.id}},
        {title: 'Status', prop: 'status', filterOptions: enums.VendorStatus, filter: 'Active'},
        {title: 'Address', prop: 'address', type: 'address'},
        {title: 'Fax Number', prop: 'faxNumber'},
        {title: 'Contact Name', prop: 'contactName'},
        {title: 'Contact Phone', prop: 'contactNumber'},
        {title: 'Pending Assessments', prop: 'pendAssesCount'}]},
    UNCONFIRMED: {title: 'Unconfirmed Appointments',
      entity: 'appointment',
      filter: 'state=UNCONFIRMED',
      columns: [
        {title: 'Nurse', prop: 'nurseName', url: app => {return '#nurse/' + app.nurseId + '/unconfirmed'}},
        {title: 'Patient', prop: 'patientName', url: appointment => {return '#patient/' + appointment.patientId}},
        {title: 'Date', prop: 'date', type: 'date'},
        /* {title: 'Confirmation Request',
          button: {display: 'Send Request', disabled: app => {return app.state != 'UNCONFIRMED'},
            action: appointment => {console.log('Confirmation request sent')}}}, */
        {title: 'Confirm',
          button: {
            display: appointment => {return appointment.state == 'UNCONFIRMED' ? 'Set Confirmed' : 'Set Unconfirmed'},
            disabled: appointment => {return appointment.state != 'UNCONFIRMED' && appointment.state != 'CONFIRMED'},
            action: appointment => {
              appointment.state = appointment.state == 'UNCONFIRMED' ? 'CONFIRMED' : 'UNCONFIRMED';
              appointmentService.update(appointment, ['state'], update => {});
            }}}]},
    PENDING_ASSESSMENT: {title: 'Pending Assessment',
      entity: 'appointment',
      filter: AppointmentState.PENDING_ASSESSMENT_30DAYS,
      orderBy: 'date DESC',
      columns: [
        {title: 'Patient', prop: 'patientName', url: appointment => {return '#assessment/' + appointment.id}},
        {title: 'Date', prop: 'date', type: 'date'},
        {title: 'Time In', value: app => {return $filter('date')(app.timeIn || app.startTime, 'shortTime')}},
        {title: 'Time Out', value: app => {return $filter('date')(app.timeOut || app.endTime, 'shortTime')}},
        {title: 'Percent Complete', prop: 'assPercentComplete', type: 'percent'}]},
    REVIEWABLE: {title: 'Awaiting Review',
      entity: 'appointment',
      filter: 'state=PENDING_APPROVAL',
      columns: [
        {title: 'Patient', prop: 'patientName', url: appointment => {return '#assessment/' + appointment.id}},
        {title: 'Nurse', prop: 'nurseName'},
        {title: 'Date', prop: 'date', type: 'date'},
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
        {title: 'Date', prop: 'date', type: 'date'},
        {title: 'Billing Hours', prop: 'billingHours'},
        {title: 'Billing Rate', prop: 'billingRate', type: 'currency'},
        {title: 'Visit Pay', prop: 'billingFlat', type: 'currency'},
        {title: 'Billing Mileage', prop: 'billingMileage', type: 'currency'},
        {title: 'Total Payment', prop: 'billingTotal', type: 'currency'}]},
    PAY_CODES: {title: 'Pay Codes',
      entity: 'serviceCode',
      filter: 'type=Pay',
      columns: [
        {title: 'Code', prop: 'code'},
        {title: 'Unit Price', prop: 'unitPrice', type: 'currency'},
        {title: 'Unit', prop: 'unit'},
        {title: 'Description', prop: 'description'},
        {title: 'Last Modified By', prop: 'lastModifiedUsername'},
        {title: 'Last Modified', prop: 'lastModifiedTime', type: 'datetime'}]},
    BILL_CODES: {title: 'Bill Codes',
      entity: 'serviceCode',
      filter: 'type=Bill',
      deleteEnabled: true,
      columns: [
        {title: 'Vendor', prop: 'vendorName',
          url: billCode => {return '#vendor/' + billCode.vendorId + '/payCodes'}},
        {title: 'Code', prop: 'code'},
        {title: 'Unit Price', prop: 'unitPrice', type: 'currency'},
        {title: 'Unit', prop: 'unit'},
        {title: 'Description', prop: 'description'},
        {title: 'Last Modified By', prop: 'lastModifiedUsername'},
        {title: 'Last Modified', prop: 'lastModifiedTime', type: 'datetime'}]},
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
        {title: 'Date', prop: 'date', type: 'date'},
        {title: 'Hours', prop: 'payHours'},
        {title: 'Pay Rate', prop: 'payRate', type: 'currency'},
        {title: 'Visit Pay', prop: 'payFlat', type: 'currency'},
        {title: 'Mileage', prop: 'payMileage', type: 'currency'},
        {title: 'Total Payment', prop: 'payTotal', type: 'currency'}]},
    INVOICES: {title: 'Unpaid Invoices',
      entity: 'invoice',
      columns: [
        {title: 'Name', prop: 'name', url: invoice => {return '#vendor/' + invoice.vendorId + '/invoices'}},
        {title: 'Date', prop: 'generationTime', type: 'date',
            imageLink: {src: 'images/icons/fugue/document-pdf.png',
                url: invoice => {return this.getFileUrl(invoice.fileReference)}}},
        {title: 'Billed', prop: 'totalDue', type: 'currency'},
        {title: 'Status', prop: 'status', filterOptions: enums.VendorStatus},
        {title: 'Comment', prop: 'comment', editable: true},
        {title: 'Received', prop: 'totalPaid', editable: true}]},
    PAY_HISTORY: {title: 'Pay History',
      entity: 'paystub',
      columns: [
        {title: 'Nurse', prop: 'nurseName',
          url: paystub => {return '#nurse/' + paystub.nurseId + '/payHistory'}},
        {title: 'Pay Date', prop: 'payDate', type: 'date',
          imageLink: {src: 'images/icons/fugue/document-pdf.png',
            url: paystub => {return this.getFileUrl(paystub.fileReference)}}},
        {title: 'Gross', prop: 'grossPay', type: 'currency'},
        {title: 'Deductions', value: p => {return $filter('currency')(p.preTaxDeductions + p.postTaxDeductions)}},
        {title: 'Taxes', prop: 'taxTotal', type: 'currency'},
        {title: 'Mileage Reimbursement', prop: 'payMileage', type: 'currency'},
        {title: 'Net Pay', prop: 'netPay', type: 'currency'}]},
    NOTES: {title: 'Notes',
        entity: 'note',
        orderBy: 'creationTime DESC',
        columns: [
          {title: 'Note', prop: 'note'},
          {title: 'Created On', prop: 'creationTime', type: 'datetime'},
          {title: 'Created By', prop: 'creationUsername', url: note => {return '#user/' + note.creationUserId}},
          {title: 'Type', prop: 'type'},
          {title: 'Status', prop: 'status', filterOptions: enums.NoteStatus, filter: 'Active'},
          {title: '',
            button: {
              display: note => {return note.status == 'Active' ? 'Archive' : 'Set Active'},
              action: note => {
                note.status = note.status == 'Active' ? 'Archived' : 'Active';
                noteService.update(note, ['status'], updated => {});
              }}}]},
    GLOBAL_NOTES: {title: 'Notes',
        entity: 'note',
        orderBy: 'creationTime DESC',
        columns: [
        	{title: 'Name', prop: 'entityName', url: note => {return '#' + note.entityType.toLowerCase() + '/' + note.entityId}},
          {title: 'Note', prop: 'note'},
          {title: 'Created On', prop: 'creationTime', type: 'datetime'},
          {title: 'Created By', prop: 'creationUsername', url: note => {return '#user/' + note.creationUserId}},
          {title: 'Type', prop: 'type'},
          {title: 'Status', prop: 'status', filterOptions: enums.NoteStatus, filter: 'Active'},
          {title: '',
            button: {
              display: note => {return note.status == 'Active' ? 'Archive' : 'Set Active'},
              action: note => {
                note.status = note.status == 'Active' ? 'Archived' : 'Active';
                noteService.update(note, ['status'], updated => {});
              }}}]},
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
    FILES: {title: 'Files',
      entity: 'file',
      columns: [
        {title: 'Name', prop: 'name',
          imageLink: {src: 'images/icons/fugue/document-pdf.png', url: file => {return this.getFileUrl(file.name)}}},
        {title: 'Date', prop: 'creationTime', type: 'date'},
        {title: 'Comment', prop: 'comment', editable: true}]},
		QUICKBOOKS_EXPORTS: {title: 'QuickBooks Exports',
		 entity: 'quickBooksExport',
		 orderBy: 'creationTime DESC',
		 columns: [
			 {title: 'Name', prop: 'id',
				 imageLink: {src: 'images/icons/fugue/document-pdf.png',
					 url: qbExport => {return this.getFileUrl(qbExport.fileReference.name)}}},
			 {title: 'Date', prop: 'creationTime', type: 'datetime'},
			 {title: 'Comment', prop: 'comment', editable: true}]}
	};
	$scope.TableType = com.digitald4.iis.TableBaseMeta;
}

com.digitald4.iis.IISCtrl.prototype.getFileUrl = function(fileReference, type) {
  return this.userService.getFileUrl(fileReference, type);
}

com.digitald4.iis.IISCtrl.prototype.logout = function() {
  this.userService.logout();
}
