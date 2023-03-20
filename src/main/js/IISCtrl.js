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
          {title: 'Address', value: nurse => {return nurse.address || ''}},
          {title: 'Pending Evaluations', prop: 'pendAssesCount'}]},
			LICENSE_ALERT: {title: 'License Expiration',
				entity: 'license',
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
				columns: [
				  {title: 'Name', prop: 'name', url: patient => {return '#patient/' + patient.id}},
          {title: 'Vendor', prop: 'billingVendorName'},
          {title: 'RX', prop: 'rx'},
          {title: 'Diagnosis', value: pat => {return generalDataService.get(pat.diagnosisId).name}},
          {title: 'Last Appointment', prop: 'lastAppointment'},
          {title: 'Next Appointment', prop: 'nextAppointment'}]},
			PENDING_INTAKE: {title: 'Pending Intakes',
				entity: 'patient',
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
          {title: 'Address', value: vendor => {vendor.address || ''}},
          {title: 'Fax Number', prop: 'faxNumber'},
          {title: 'Contact Name', prop: 'contactName'},
          {title: 'Contact Phone', prop: 'contactNumber'},
          {title: 'Pending Assessments', prop: 'pendAssesCount'}]},
			UNCONFIRMED: {title: 'Unconfirmed Appointments',
				entity: 'appointment',
				columns: [
				  {title: 'Nurse', prop: 'nurseName',
				    url: appointment => {return '#nurse/' + appointment.nurseId + '/unconfirmed'}},
          {title: 'Patient', prop: 'patientName',
            url: appointment => {return '#patient/' + appointment.patientId}},
          {title: 'Start Time', prop: 'start', type: 'datetime'},
          {title: 'Contact Info', value: appointment => {return appointment.nursePhoneNumber}},
          {title: 'Confirmation Request',
            button: {display: 'Send Request', disabled: app => {return app.state != 'UNCONFIRMED'},
              action: appointment => {console.log('Confirmation request sent')}}},
          {title: 'Confirm',
            button: {display: 'Set Confirmed', disabled: app => {return app.state != 'UNCONFIRMED'},
              action: appointment => {
                appointment.state = 'CONFIRMED';
                appointmentService.update(appointment, ['state'], update => {});}}}]},
			PENDING_ASSESSMENT: {title: 'Pending Assessment',
				entity: 'appointment',
				columns: [
				  {title: 'Patient', prop: 'patientName',
				    url: appointment => {return '#assessment/' + appointment.id}},
				  {title: 'Date', prop: 'start', type: 'datetime'},
				  {title: 'Time In',
				    value: app => {return $filter('date')(app.timeIn || app.start, 'shortTime')}},
          {title: 'Time Out',
            value: app => {return $filter('date')(app.timeOut || app.end, 'shortTime')}},
          {title: 'Percent Complete', prop: 'assessmentPercentComplete', type: 'percent'}]},
			REVIEWABLE: {title: 'Awaiting Review',
				entity: 'appointment',
				columns: [
				  {title: 'Patient', prop: 'patientName',
				    url: appointment => {return '#assessment/' + appointment.id}},
          {title: 'Nurse', prop: 'nurseName'},
          {title: 'Date', prop: 'start', type: 'datetime'},
          {title: 'Hours', prop: 'payHours'},
          {title: 'Mileage', prop: 'mileage'},
          {title: 'Percent Complete', prop: 'assPercentComplete', type: 'percent'}]},
			BILLABLE: {title: 'Billable',
				entity: 'appointment',
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
				columns: [
				  {title: 'Name', prop: 'name',
				    url: invoice => {return '#vendor/' + invoice.vendorId + '/invoices'}},
          {title: 'Date', prop: 'generationTime', type: 'date',
              imageLink: {src: 'images/icons/fugue/document-pdf.png',
                  url: invoice => {return userService.getFileUrl(invoice.fileReference)}}},
          {title: 'Billed', prop: 'totalDue', type: 'currency'},
          {title: 'Status', prop: 'statusId'},
          {title: 'Comment', prop: 'comment', editable: true},
          {title: 'Received', prop: 'totalPaid', editable: true}]},
			PAID_INVOICES: {title: 'Paid Invoices',
				entity: 'invoice',
				columns: [
				  {title: 'Name', prop: 'name',
				      url: invoice => {return '#vendor/' + invoice.vendorId + '/invoices'}},
          {title: 'Date', prop: 'generationTime', type: 'date',
              imageLink: {src: 'images/icons/fugue/document-pdf.png',
                  url: invoice => {return userService.getFileUrl(invoice.fileReference)}}},
          {title: 'Billed', prop: 'totalDue', type: 'currency'},
          {title: 'Status', prop: 'statusId'},
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
            value: paystub => {
              return $filter('currency')(paystub.preTaxDeductions + paystub.postTaxDeductions)}},
          {title: 'Taxes', prop: 'taxTotal', type: 'currency'},
          {title: 'Mileage Reimbursement', prop: 'payMileage', type: 'currency'},
          {title: 'Net Pay', prop: 'netPay', type: 'currency'}]},
      CHANGE_HISTORY: {title: 'Change History',
          entity: 'changeHistory',
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
          {title: 'Name', prop: 'name', imageLink: {src: 'images/icons/fugue/document-pdf.png',
              url: report => {return userService.getFileUrl(report.dataFile)}}},
          {title: 'Date', prop: 'generationTime', type: 'date'},
          {title: 'Comment', prop: 'comment', editable: true}]}
	};
	$scope.TableType = {
	  NURSES: {base: com.digitald4.iis.TableBaseMeta.NURSES},
	  LICENSE_ALERT: {
	      base: com.digitald4.iis.TableBaseMeta.LICENSE_ALERT,
	      filter: 'expiration_date<' + (Date.now() + DAYS_30)},
    PATIENTS: {base: com.digitald4.iis.TableBaseMeta.PATIENTS},
    USERS: {base: com.digitald4.iis.TableBaseMeta.USERS},
    VENDORS: {base: com.digitald4.iis.TableBaseMeta.VENDORS},
    PENDING_INTAKE: {
        base: com.digitald4.iis.TableBaseMeta.PENDING_INTAKE, filter: 'referral_resolution_id=885'},
    UNCONFIRMED: {base: com.digitald4.iis.TableBaseMeta.UNCONFIRMED, filter: 'state=UNCONFIRMED'},
    PENDING_ASSESSMENT: {
        base: com.digitald4.iis.TableBaseMeta.PENDING_ASSESSMENT,
        filter: AppointmentState.PENDING_ASSESSMENT},
    REVIEWABLE:{base: com.digitald4.iis.TableBaseMeta.REVIEWABLE, filter: 'state=PENDING_APPROVAL'},
    BILLABLE: {base: com.digitald4.iis.TableBaseMeta.BILLABLE, filter: AppointmentState.BILLABLE},
    PAYABLE: {base: com.digitald4.iis.TableBaseMeta.PAYABLE, filter: AppointmentState.PAYABLE},
    UNPAID_INVOICES:
      {base: com.digitald4.iis.TableBaseMeta.UNPAID_INVOICES, filter: 'status_id=1521'},
		PAID_INVOICES: {base: com.digitald4.iis.TableBaseMeta.PAID_INVOICES, filter: 'status_id=1520'},
		PAY_HISTORY: {base: com.digitald4.iis.TableBaseMeta.PAY_HISTORY},
		REPORTS: {base: com.digitald4.iis.TableBaseMeta.REPORTS}
	};
};

com.digitald4.iis.IISCtrl.prototype.logout = function() {
  this.userService.logout();
};
