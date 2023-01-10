package com.digitald4.iis.server;

import com.digitald4.common.storage.LongStore;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.iis.model.User;
import com.digitald4.iis.model.Vendor;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "vendors",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    ),
    // [START_EXCLUDE]
    issuers = {
        @ApiIssuer(
            name = "firebase",
            issuer = "https://securetoken.google.com/fantasy-predictor",
            jwksUri = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
    }
    // [END_EXCLUDE]
)
public class VendorService extends AdminService<Vendor> {

  @Inject
  VendorService(LongStore<Vendor> vendorStore, SessionStore<User> sessionStore) {
    super(vendorStore, sessionStore);
  }
}
