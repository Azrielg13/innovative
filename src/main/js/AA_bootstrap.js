var com = com || {};
com.digitald4 = com.digitald4 || {};
com.digitald4.iis = com.digitald4.iis || {};

ONE_DAY = 1000 * 60 * 60 * 24
ONE_WEEK = ONE_DAY * 7;
ONE_MONTH = ONE_DAY * 30;
DAYS_30 = ONE_DAY * 30;
DAYS_60 = ONE_DAY * 60;
DAYS_90 = ONE_DAY * 90;

lastSunday = function() {
  var now = new Date();
  var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  return today.getTime() - today.getDay() * ONE_DAY;
}

TEST_URL = 'https://test-dot-ip360-179401.uc.r.appspot.com/';
PROD_URL = 'https://ip360-179401.appspot.com/';

initMap = function(map) {
 // console.log('initMap called with: ' + map);
}

enums = {
	AccountingType: ['Auto_Detect', 'Hourly', 'Fixed', 'Soc2Hr', 'Roc2Hr'],
	AppointmentStates: ['UNCONFIRMED', 'CONFIRMED', 'CANCELLED', 'PENDING_ASSESSMENT',
	    'PENDING_APPROVAL', 'BILLABLE_AND_PAYABLE', 'BILLABLE', 'PAYABLE', 'CLOSED', '*All'],
  EmployeeStatus: ['Applicant', 'Rejected', 'Pending', 'Active', 'Hold', 'Suspended', 'Terminated', '*All'],
  InvoiceStatus: ['Unpaid', 'Partially_Paid', 'Paid', 'Cancelled', '*All'],
  NoteStatus: ['Active', 'Archived', '*All'],
  PatientCondition: ['Unspecified', 'Acute', 'Chronic'],
  ResolutionEfforts: ['Unspecified', '<30', '30-60', '>60'],
  PatientStatus: ['Active', 'Cancelled_By_Agency', 'Cancelled_By_Pharmacy', 'Denied', 'Declined_No_Nurse',
      'Discharged', 'Hospitalized', 'On_Hold', 'Pending', 'Vacation', 'Waiting For Authorization', '*All'],
  VendorStatus: ['Active', 'In_Active', '*All'],
  PayPreferences: ['Weekly', 'After2Days', 'NextDay'],
  UserRoles: {
    Administrator: 'Administrator',
    Credentialing_Clerk: 'Credentialing_Clark',
    Reimbursement_Clerk: 'Reimbursement_Clerk',
    Senior_Bookkeeper: 'Senior_Bookkeeper',
    Referrals_Coordinator: 'Referrals_Coordinator',
    Clinical_Coordinator: 'Clinical_Coordinator'
  },
  ReferralResponses: ['', 'Interested', 'Not_Interested', 'Not_Qualified'],
}


ADMIN = enums.UserRoles.Administrator;
CC = enums.UserRoles.Credentialing_Clerk;
RC = enums.UserRoles.Reimbursement_Clerk;
SB = enums.UserRoles.Senior_Bookkeeper;
RCO = enums.UserRoles.Referrals_Coordinator;
CCO = enums.UserRoles.Clinical_Coordinator;

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
