var com = com || {};
com.digitald4 = com.digitald4 || {};
com.digitald4.iis = com.digitald4.iis || {};

ONE_MONTH = 1000 * 60 * 60 * 24 * 30;

initMap = function(map) {
 // console.log('initMap called with: ' + map);
}

enums = {
  NurseStatus: ['Applicant', 'Rejected', 'Pending', 'Active', 'Hold', 'Suspended', 'Terminated', '*All'],
  PatientStatus: ['Active', 'Denied', 'Discharged', 'Hospitalized', 'On_Hold', 'Pending', 'Vacation',
      'Waiting For Authorization', '*All'],
}

AppointmentState = {
  UNCONFIRMED: 'state=UNCONFIRMED',
  PENDING_ASSESSMENT: 'state=PENDING_ASSESSMENT',
  PENDING_ASSESSMENT_30DAYS: 'state=PENDING_ASSESSMENT,end>' + (Date.now() - ONE_MONTH),
  PENDING_APPROVAL: 'state=PENDING_APPROVAL',
  BILLABLE: 'state IN BILLABLE|BILLABLE_AND_PAYABLE',
  PAYABLE: 'state IN PAYABLE|BILLABLE_AND_PAYABLE',
  COMPLETED_ASSESSMENT: 'state IN PENDING_APPROVAL|BILLABLE|PAYABLE|BILLABLE_AND_PAYABLE|CLOSED',
  PAID_INVOICE: 'state IN PAYABLE|CLOSED'
}
