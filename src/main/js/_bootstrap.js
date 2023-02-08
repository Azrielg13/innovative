var com = com || {};
com.digitald4 = com.digitald4 || {};
com.digitald4.iis = com.digitald4.iis || {};

initMap = function(map) {
 // console.log('initMap called with: ' + map);
}

AppointmentState = {
  UNCONFIRMED: 'state=UNCONFIRMED',
  PENDING_ASSESSMENT: 'state=PENDING_ASSESSMENT',
  PENDING_APPROVAL: 'state=PENDING_APPROVAL',
  BILLABLE: 'state IN BILLABLE|BILLABLE_AND_PAYABLE',
  PAYABLE: 'state IN PAYABLE|BILLABLE_AND_PAYABLE',
  COMPLETED_ASSESSMENT: 'state IN PENDING_APPROVAL|BILLABLE|PAYABLE|BILLABLE_AND_PAYABLE|CLOSED'
}
