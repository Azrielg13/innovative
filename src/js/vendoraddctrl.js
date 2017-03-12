com.digitald4.iis.VendorAddCtrl = function(vendorService) {
  this.vendorService = vendorService;
  this.vendor = {};
};

com.digitald4.iis.VendorAddCtrl.prototype.create = function() {
  this.vendorService.create(this.vendor, function(vendor) {
    this.lastAdded = vendor;
    this.message = 'Vendor added';
    this.vendor = {};
  }.bind(this), notify);
};