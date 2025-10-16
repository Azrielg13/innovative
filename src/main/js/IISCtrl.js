com.digitald4.iis.GeneralData = com.digitald4.iis.GenData;

com.digitald4.iis.IISCtrl = function($filter, $scope, flags, flagService, generalDataService,
    globalData, quickBooksExportService, serviceCodeService, userService) {
  this.flags = flags;
  flags.vendorBillingEnabled = true;
  this.globalData = globalData;
  const userRole = globalData.activeSession.user.role;
  const dateRange = userRole == 'Nurse' ? {prop: 'start'} :
      {prop: 'start', start: lastSunday() - ONE_WEEK, end: lastSunday() - ONE_DAY};
  this.serviceCodeService = serviceCodeService;
  this.userService = userService;
  this.scope = $scope;
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
        {title: 'First Name', prop: 'firstName', url: nurse => '#nurse/' + nurse.id},
        {title: 'Last Name', prop: 'lastName', url: nurse => '#nurse/' + nurse.id},
        {title: 'Status', prop: 'status', filterOptions: enums.EmployeeStatus, filter: 'Active'},
        {title: 'Phone #', prop: 'phoneNumber'},
        {title: 'Email Address', prop: 'email'},
        {title: 'Address', prop: 'address', type: 'address'},
        {title: 'Pending Evaluations', prop: 'pendAssesCount'}]},
    LICENSE_ALERT: {
      title: 'License Expiration',
      entity: 'license',
      filter: 'nurseStatus=Active',
      dateRange: {prop: 'expirationDate', start: (Date.now() - ONE_MONTH), end: (Date.now() + DAYS_90)},
      orderBy: 'expirationDate DESC',
      columns: [
        {title: 'Nurse', prop: 'nurseName', url: nurse => '#nurse/' + nurse.id + '/licenses', isHidden: () => userRole == 'Nurse'},
        {title: 'License', prop: 'licTypeName'},
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
        {title: 'First Name', prop: 'firstName', url: patient => '#patient/' + patient.id},
        {title: 'Last Name', prop: 'lastName', url: patient => '#patient/' + patient.id},
        {title: 'Status', prop: 'status', filterOptions: enums.PatientStatus, filter: 'Active'},
        {title: 'Vendor', prop: 'billingVendorName'},
        {title: 'Service Address', prop: 'serviceAddress', type: 'address'},
        {title: 'Service City', prop: 'serviceAddress', type: 'city'},
        {title: 'RX', prop: 'rx'},
        {title: 'Titration', prop: 'titration'},
        {title: 'Diagnosis', prop: 'diagnosis'}]},
    PENDING_INTAKE: {title: 'Pending Intakes',
      entity: 'patient',
      filter: 'status=Pending',
      columns: [
        {title: 'Name', prop: 'fullName', url: patient => '#patient/' + patient.id},
        {title: 'Referral Source', prop: 'referralSourceName'},
        {title: 'Billing Vendor', prop: 'billingVendorName'},
        {title: 'Diagnosis', prop: 'diagnosis'},
        {title: 'Referral Date', prop: 'referralDate', type: 'date'},
        {title: 'Start Date', prop: 'startOfCareDate', type: 'date'}]},
    OPEN_REFERRALS: {title: 'Open Referrals',
      entity: 'patient',
      filter: 'status=Pending',
      columns: [
        {title: 'Referral Date', prop: 'referralDate', type: 'date'},
        {title: 'Service City', prop: 'serviceAddress', type: 'city'},
        {title: 'Diagnosis', prop: 'diagnosis'},
        {title: 'RX', prop: 'rx'},
        {title: 'Titration', prop: 'titration'},
        {title: 'Start Date', prop: 'startOfCareDate', type: 'date'},
        {title: 'Notes', prop: 'referralNote'},
        {title: 'Response', prop: 'response', type: 'select', options: enums.ReferralResponses},
        {title: 'Referral Comment', prop: 'responseComment', type: 'textarea'}]},
    USERS: {title: 'Users',
      entity: 'user',
      columns: [
        {title: 'First Name', prop: 'firstName', url: user => '#user/' + user.id},
        {title: 'Last Name', prop: 'lastName', url: user => '#user/' + user.id},
        {title: 'Status', prop: 'status', filterOptions: enums.EmployeeStatus, filter: 'Active'},
        {title: 'Role', prop: 'role'},
        {title: 'Phone #', prop: 'phoneNumber'},
        {title: 'Username', prop: 'username'},
        {title: 'Email Address', prop: 'email'},
        {title: 'Last Login', prop: 'lastLogin', type: 'datetime'}]},
    VENDORS: {title: 'Vendors',
      entity: 'vendor',
      orderBy: 'name',
      columns: [
        {title: 'Vendor', prop: 'name', url: vendor => '#vendor/' + vendor.id},
        {title: 'Status', prop: 'status', filterOptions: enums.VendorStatus, filter: 'Active'},
        {title: 'Address', prop: 'address', type: 'address'},
        {title: 'Fax Number', prop: 'faxNumber'},
        {title: 'Contact Name', prop: 'contactName'},
        {title: 'Contact Phone', prop: 'contactNumber'},
        {title: 'Pending Assessments', prop: 'pendAssesCount'}]},
    APPOINTMENTS: {title: 'Appointments',
      entity: 'appointment',
      dateRange: {prop: 'start', start: (Date.now() - ONE_WEEK), end: (Date.now() + ONE_WEEK)},
      orderBy: 'date',
      columns: [
        {title: 'Vendor', prop: 'vendorName', url: app => '#vendor/' + app.vendorId, isHidden: () => userRole == 'Nurse'},
        {title: 'Patient', prop: 'patientName', type: 'clickable', clickMeta: app => 'Patient'},
        {title: 'Nurse', prop: 'nurseName', isHidden: () => userRole == 'Nurse', type: 'clickable', clickMeta: app => 'Nurse'},
        {title: 'Scheduled Time', value: app => $filter('date')(app.date, 'MM/dd/yyyy') + ' ' +
            $filter('date')(app.startTime, 'HH:mm') + ' - ' + $filter('date')(app.endTime, 'HH:mm'),
            type: 'clickable'},
        {title: 'Date', prop: 'date', type: 'editableDate', disabled: app => userRole == 'Nurse' || app.state == 'CLOSED'},
        {title: 'Status', prop: 'state', filterOptions: enums.AppointmentStates},
        {title: 'Titration', prop: 'titration'},
        {title: 'Hours', prop: 'loggedHours'},
        {title: 'Invoice', prop: 'invoiceId',
            imageLink: {src: 'images/icons/fugue/document-pdf.png', target: '_blank',
            url: appointment => flags.billableEnabled && appointment.invoiceId
                ? this.getFileUrl("invoice-" + appointment.invoiceId + ".pdf") : undefined}}]},
    APPOINTMENTS_COMPLETED: {title: 'Completed Appointments',
      entity: 'appointment',
      dateRange: {prop: 'start', start: (Date.now() - ONE_MONTH * 6), end: Date.now()},
      filter: AppointmentState.COMPLETED_ASSESSMENT,
      orderBy: 'date',
      columns: [
        {title: 'Patient', prop: 'patientName', type: 'clickable', clickMeta: app => 'Patient'},
        {title: 'Date', value: app => $filter('date')(app.date, 'MM/dd/yyyy'), type: 'clickable'},
        {title: 'Time In', prop: 'timeIn', type: 'time'},
        {title: 'Time Out', prop: 'timeOut', type: 'time'},
        {title: 'Hours', prop: 'loggedHours'},
        {title: 'Mileage', prop: 'mileage'}]},
    PENDING_ASSESSMENT: {title: 'Pending Assessment',
      entity: 'appointment',
      filter: AppointmentState.PENDING_ASSESSMENT,
      dateRange: dateRange,
      orderBy: 'date',
      columns: [
        {title: 'Nurse', prop: 'nurseName', isHidden: () => userRole == 'Nurse', type: 'clickable', clickMeta: app => 'Nurse'},
        {title: 'Patient', prop: 'patientName', type: 'clickable', clickMeta: app => 'Patient'},
        {title: 'Vendor', prop: 'vendorName', url: app => '#vendor/' + app.vendorId, isHidden: () => userRole == 'Nurse'},
        {title: 'Date', prop: 'date', type: 'editableDate', disabled: app => userRole == 'Nurse' || app.state == 'CLOSED'},
        {title: 'Titration', prop: 'titration', isHidden: () => userRole == 'Nurse'},
        {title: 'Time In', prop: 'timeIn', type: 'editableTime'},
        {title: 'Time Out', prop: 'timeOut', type: 'editableTime'},
        {title: 'From Zip Code', prop: 'fromZipCode', type: 'editable', size: 5},
        {title: 'To Zip Code', prop: 'toZipCode', type: 'editable', size: 5},
        {title: 'Mileage', prop: 'mileage', type: 'editable', size: 3},
        {title: 'Attachments', type: 'html', value: app => '<attachments data-id="' + app.id + '"></attachments>'},
        {title: 'Action',
          button: {
            display: app => userRole == 'Nurse' ? 'Set Complete' : 'Approve',
            action: (app, ctrl) => {
              if (userRole == 'Nurse') {
                app.assessmentComplete = true;
                ctrl.updateAndRemove(app, ['assessmentComplete']);
              } else {
                app.assessmentApproved = true;
                ctrl.updateAndRemove(app, ['assessmentApproved']);
              }
            }}}]},
    REVIEWABLE: {title: 'Awaiting Review',
      entity: 'appointment',
      filter: 'state=PENDING_APPROVAL',
      columns: [
        {title: 'Patient', prop: 'patientName', url: appointment => '#assessment/' + appointment.id},
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
      orderBy: 'code',
      columns: [
        {title: 'Code', prop: 'code'},
        {title: 'Unit Price', prop: 'unitPrice', type: 'currency'},
        {title: 'Unit', prop: 'unit'},
        {title: 'Description', prop: 'description'},
        {title: 'Active', prop: 'active', type: 'checkbox'},
        {title: 'Last Modified By', prop: 'lastModifiedUsername'},
        {title: 'Last Modified', prop: 'lastModifiedTime', type: 'datetime'}]},
    BILL_CODES: {title: 'Bill Codes',
      entity: 'serviceCode',
      filter: 'type=Bill',
      orderBy: 'vendorName',
      columns: [
        {title: 'Vendor', prop: 'vendorName',
          url: billCode => '#vendor/' + billCode.vendorId + '/payCodes'},
        {title: 'Code', prop: 'code'},
        {title: 'Unit Price', prop: 'unitPrice', type: 'currency'},
        {title: 'Unit', prop: 'unit'},
        {title: 'Description', prop: 'description'},
        {title: 'Active', prop: 'active', type: 'checkbox'},
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
    INVOICES: {title: 'Invoices',
      entity: 'invoice',
      orderBy: 'id DESC',
      columns: [
        {title: 'Invoice Number', prop: 'id',
            imageLink: {src: 'images/icons/fugue/document-pdf.png', target: '_blank',
                url: invoice => this.getFileUrl(invoice.fileReference)}},
        {title: 'Date', prop: 'date', type: 'date'},
        {title: 'Service Billing', prop: 'standardBilling', type: 'currency'},
        {title: 'Mileage Billing', prop: 'billedMileage', type: 'currency'},
        {title: 'Total Billed', prop: 'totalDue', type: 'currency'},
        {title: 'Status', prop: 'status', filterOptions: enums.InvoiceStatus},
        {title: 'Comment', prop: 'comment', editable: true},
        {title: 'Received', prop: 'totalPaid', editable: true}]},
    PAY_HISTORY: {title: 'Pay History',
      entity: 'paystub',
      columns: [
        {title: 'Nurse', prop: 'nurseName',
          url: paystub => '#nurse/' + paystub.nurseId + '/payHistory'},
        {title: 'Pay Date', prop: 'payDate', type: 'date',
          imageLink: {src: 'images/icons/fugue/document-pdf.png',
            url: paystub => this.getFileUrl(paystub.fileReference)}},
        {title: 'Gross', prop: 'grossPay', type: 'currency'},
        {title: 'Deductions', value: p => $filter('currency')(p.preTaxDeductions + p.postTaxDeductions)},
        {title: 'Taxes', prop: 'taxTotal', type: 'currency'},
        {title: 'Mileage Reimbursement', prop: 'payMileage', type: 'currency'},
        {title: 'Net Pay', prop: 'netPay', type: 'currency'}]},
    NOTES: {title: 'Notes',
        entity: 'note',
        orderBy: 'creationTime DESC',
        columns: [
          {title: 'Note', prop: 'note'},
          {title: 'Created On', prop: 'creationTime', type: 'datetime'},
          {title: 'Created By', prop: 'creationUsername', url: note => '#user/' + note.creationUserId},
          {title: 'Type', prop: 'type'},
          {title: 'Status', prop: 'status', filterOptions: enums.NoteStatus, filter: 'Active'},
          {title: '',
            button: {
              display: note => note.status == 'Active' ? 'Archive' : 'Set Active',
              disabled: note => note.type == 'Cancelled_Appointment',
              action: (note, ctrl) => {
                note.status = note.status == 'Active' ? 'Archived' : 'Active';
                ctrl.update(note, 'status');
              }}}]},
    GLOBAL_NOTES: {title: 'Notes',
        entity: 'note',
        orderBy: 'creationTime DESC',
        columns: [
        	{title: 'Name', prop: 'entityName', url: note => '#' + note.entityType.toLowerCase() + '/' + note.entityId},
          {title: 'Note', prop: 'note'},
          {title: 'Created On', prop: 'creationTime', type: 'datetime'},
          {title: 'Created By', prop: 'creationUsername', url: note => '#user/' + note.creationUserId},
          {title: 'Type', prop: 'type'},
          {title: 'Status', prop: 'status', filterOptions: enums.NoteStatus, filter: 'Active'},
          {title: '',
            button: {
              display: note => note.status == 'Active' ? 'Archive' : 'Set Active',
              action: (note, ctrl) => {
                note.status = note.status == 'Active' ? 'Archived' : 'Active';
                ctrl.update(note, 'status');
              }}}]},
    CHANGE_HISTORY: {title: 'Change History',
        entity: 'changeHistory',
        orderBy: 'timeStamp DESC',
        columns: [
          {title: 'Type', prop: 'entityType',
            url: ch => '#' + ch.entityType + '/' + ch.entityId + '/changeHistory'},
          {title: 'Id', prop: 'entityId', },
          {title: 'Action', prop: 'action'},
          {title: 'Date', prop: 'timeStamp', type: 'datetime'},
          {title: 'User', prop: 'username'},
          {title: 'Diff', prop: 'changeHtml', type: 'html'}]},
    FILES: {title: 'Files',
      entity: 'file',
      columns: [
        {title: 'Name', prop: 'name',
          imageLink: {src: 'images/icons/fugue/document-pdf.png', url: file => this.getFileUrl(file.name)}},
        {title: 'Date', prop: 'creationTime', type: 'date'},
        {title: 'Comment', prop: 'comment', editable: true}]},
		QUICKBOOKS_EXPORTS: {title: 'QuickBooks Exports',
		 entity: 'quickBooksExport',
		 orderBy: 'creationTime DESC',
     pageSize: '25',
		 columns: [
		  {title: 'Date', prop: 'creationTime', type: 'datetime'},
		  {title: 'Export File', prop: 'id',
				 imageLink: {src: 'images/icons/fugue/document-excel-csv.png', target: '_blank',
					 url: qbExport => this.getFileUrl(qbExport.fileReference.name)}},
			{title: 'Invoices', value: qbExport => qbExport.invoiceFileReference ? qbExport.invoiceFileReference.name : '',
			    imageLink: {src: 'images/icons/fugue/document-pdf.png', target: '_blank',
			        url: qbExport => qbExport.invoiceFileReference ? this.getFileUrl(qbExport.invoiceFileReference.name) : ''}},
			 {title: 'Comment', prop: 'comment', editable: true},
			 {title: 'Created By', prop: 'creationUsername'},
			 {title: 'Action',
			    button: {
			      display: qbExport => qbExport._display || 'Recreate',
			      disabled: qbExport => qbExport._disabled,
			      action: qbExport => {
			        qbExport._display = 'Recreating...';
			        qbExport._disabled = true;
			        quickBooksExportService.create(qbExport, updated => {
			          qbExport._display = 'Recreated';
			        });
           }}}]}
	};
	$scope.TableType = com.digitald4.iis.TableBaseMeta;
}

com.digitald4.iis.IISCtrl.prototype.getFileUrl = function(fileReference, type) {
  return this.userService.getFileUrl(fileReference, type);
}

com.digitald4.iis.IISCtrl.prototype.logout = function() {
  this.userService.logout();
}

com.digitald4.iis.IISCtrl.prototype.showAddCodeDialog = function() {
	this.addCode = this.addCode || {type: 'Pay'};
	this.addCodeShown = true;
}

com.digitald4.iis.IISCtrl.prototype.closeDialog = function() {
	this.addCodeShown = false;
}

com.digitald4.iis.IISCtrl.prototype.createCode = function() {
	this.serviceCodeService.create(this.addCode, addedCode => {
		this.addCodeShown = false;
		this.addCode = undefined;
		this.scope.TableType.PAY_CODES.refresh();
	});
}
