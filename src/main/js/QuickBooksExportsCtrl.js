com.digitald4.iis.QuickBooksExportsCtrl = function(appointmentService) {
  this.appointmentService = appointmentService;
  this.TableType = {
    QUICKBOOKS_EXPORTS: {
        base: com.digitald4.iis.TableBaseMeta.QUICKBOOKS_EXPORTS,
        onSelectionChange: qbExport => this.onSelectionChange(qbExport)
    }
  };
}

com.digitald4.iis.QuickBooksExportsCtrl.prototype.onSelectionChange = function(qbExport) {
  this.appointments = undefined;
  if (!qbExport.selected) {
    return;
  }

  this.appointmentService.list({filter: 'exportId=' + qbExport.id, orderBy: 'vendorName,patientName,date'}, response => {
  	this.appointments = response.items;
  	this.appointments.forEach(app =>
  	    app._invoiceLink = this.appointmentService.getFileUrl("invoice-" + app.invoiceId + ".pdf"));
  });

  // Deselect all the other exports.
  this.TableType.QUICKBOOKS_EXPORTS.resultList.items.forEach(qbe => {
    if (qbe != qbExport) {
      qbe.selected = false;
    }
  });
}
