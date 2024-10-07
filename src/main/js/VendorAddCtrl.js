com.digitald4.iis.VendorAddCtrl = function($location, flags, vendorService) {
  this.location = $location;
  this.flags = flags;
  this.vendorService = vendorService;
  this.vendor = {address: {}, mileageRate: .55};
}

com.digitald4.iis.VendorAddCtrl.prototype.create = function() {
  this.vendorService.create(this.vendor, vendor => this.location.path('/vendor/' + vendor.id));
}