package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.storage.VendorStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "vendors",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class VendorService extends AdminService<Vendor> {
  @Inject
  VendorService(VendorStore vendorStore, LoginResolver loginResolver) {
    super(vendorStore, loginResolver);
  }
}
