package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.License;
import com.digitald4.iis.storage.LicenseStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "licenses",
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
public class LicenseService extends AdminService<License> {

  @Inject
  LicenseService(LicenseStore licenseStore, LoginResolver loginResolver) {
    super(licenseStore, loginResolver);
  }
}
