var com = com || {};
com.digitald4 = com.digitald4 || {};
com.digitald4.iis = com.digitald4.iis || {};

ONE_MONTH = 1000 * 60 * 60 * 24 * 30;
var DAYS_30 = 1000 * 60 * 60 * 24 * 30;
var DAYS_60 = 1000 * 60 * 60 * 24 * 60;
var DAYS_90 = 1000 * 60 * 60 * 24 * 90;

TEST_URL = 'https://test-dot-ip360-179401.uc.r.appspot.com/';
PROD_URL = 'https://ip360-179401.appspot.com/';

initMap = function(map) {
 // console.log('initMap called with: ' + map);
}

enums = {
	AccountingType: ['Auto_Detect', 'Hourly', 'Fixed', 'Soc2Hr', 'Roc2Hr'],
  EmployeeStatus: ['Applicant', 'Rejected', 'Pending', 'Active', 'Hold', 'Suspended', 'Terminated', '*All'],
  InvoiceStatus: ['Unpaid', 'Partially_Paid', 'Paid', 'Cancelled', '*All'],
  NoteStatus: ['Active', 'Archived', '*All'],
  PatientStatus: ['Active', 'Denied', 'Discharged', 'Hospitalized', 'On_Hold', 'Pending', 'Vacation',
      'Waiting For Authorization', '*All'],
  VendorStatus: ['Active', 'In_Active', '*All'],
  PayPreferences: ['Weekly', 'After2Days', 'NextDay'],
}

AppointmentState = {
  UNCONFIRMED: 'state=UNCONFIRMED',
  PENDING_ASSESSMENT: 'state=PENDING_ASSESSMENT',
  PENDING_ASSESSMENT_30DAYS: 'state=PENDING_ASSESSMENT,end>' + (Date.now() - ONE_MONTH),
  PENDING_APPROVAL: 'state=PENDING_APPROVAL',
  BILLABLE_AND_PAYABLE: 'state=BILLABLE_AND_PAYABLE',
  BILLABLE: 'state IN BILLABLE|BILLABLE_AND_PAYABLE',
  PAYABLE: 'state IN PAYABLE|BILLABLE_AND_PAYABLE',
  COMPLETED_ASSESSMENT: 'state IN PENDING_APPROVAL|BILLABLE|PAYABLE|BILLABLE_AND_PAYABLE|CLOSED',
  PAID_INVOICE: 'state IN PAYABLE|CLOSED'
}
