com.digitald4.iis.LicenseCtrl = function(fileService, generalDataService, licenseService) {
  this.fileService = fileService;
  this.generalDataService = generalDataService;
  this.licenseService = licenseService;
  this.refreshLicenses();
}

com.digitald4.iis.LicenseCtrl.prototype.refreshLicenses = function() {
	this.licenseService.list({filter: 'nurseId=' + this.nurseId}, response => {
	  var byTypeHash = {}
	  var licenses = response.items;
	  for (var l = 0; l < licenses.length; l++) {
	    var license = licenses[l];
	    byTypeHash[license.licTypeId] = license;
	  }

	  var licenseCats = {};
	  var allLicenseTypes = {};
	  this.generalDataService.list(com.digitald4.iis.GeneralData.LICENSE, generalDatas => {
      for (var c = 0; c < generalDatas.length; c++) {
        var licenseCat = {id: generalDatas[c].id, name: generalDatas[c].name, licenses: []};
        var licenseTypes = this.generalDataService.list(licenseCat.id);
        for (var t = 0; t < licenseTypes.length; t++) {
          var licenseType = licenseTypes[t];
          var license = byTypeHash[licenseType.id] ||
              {licTypeId: licenseType.id, nurseId: this.nurseId, licTypeName: licenseType.name};
          allLicenseTypes[licenseType.id] = licenseType;
          licenseCat.licenses.push(license);
        }
        licenseCats[licenseCat.id] = licenseCat;
      }
      this.licenseCategories = licenseCats;
      this.licenseTypes = allLicenseTypes;
    });
	});
}

com.digitald4.iis.LicenseCtrl.prototype.hasExpDate = function(license) {
  return !this.licenseTypes[license.licTypeId].data;
}

com.digitald4.iis.LicenseCtrl.prototype.updateLicense = function(license, prop) {
  if (license.id) {
    this.licenseService.update(license, [prop], lic => {});
  } else {
    this.licenseService.create(license, lic => {license.id = lic.id;});
  }
}

com.digitald4.iis.LicenseCtrl.prototype.showUploadDialog = function(license) {
  this.uploadLicense = license;
	this.uploadDialogShown = true;
}

com.digitald4.iis.LicenseCtrl.prototype.closeUploadDialog = function() {
	this.uploadDialogShown = false;
}

com.digitald4.iis.LicenseCtrl.prototype.uploadFile = function() {
  var file = document.getElementById('file');
  var request = {file: file, entityType: 'License', entityId: this.uploadLicense.id};
  this.fileService.upload(request, fileReference => {
    this.uploadLicense.fileReference = fileReference;
    this.updateLicense(this.uploadLicense, 'fileReference');
    this.closeUploadDialog();
  });
}

com.digitald4.iis.LicenseCtrl.prototype.showDeleteFileDialog = function(license) {
  this.fileService.Delete(license.fileReference.id, deleted => {
    if (deleted) {
      license.fileReference = undefined;
    }
  });
}

com.digitald4.iis.LicenseCtrl.prototype.getFileUrl = function(fileReference, type) {
  return this.fileService.getFileUrl(fileReference, type);
}