com.digitald4.iis.ReportsCtrl = function(apiConnector, reportService) {
  this.apiConnector = apiConnector;
  this.reportService = reportService;
  this.init();
}

com.digitald4.iis.ReportsCtrl.prototype.init = function() {
  this.reportService.list({}, response => {this.reports = response.items});
}

com.digitald4.iis.ReportsCtrl.prototype.getReports = function() {
  return this.reports;
}

com.digitald4.iis.ReportsCtrl.prototype.updateReport = function(report) {
  var request = {
    request_url: 'https://reporting-dot-ip360-179401.appspot.com/update',
    params: {report_id: report.id}
  }
  report.updating = true;
  this.apiConnector.sendRequest(request, response => {
    report.updating = undefined;
    console.log(response);
    report.title = response.title;
    report.lastModifiedTime = response.lastModifiedTime;
  });
}