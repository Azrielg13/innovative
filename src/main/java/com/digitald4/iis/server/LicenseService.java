package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.License;
import com.digitald4.iis.storage.LicenseStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "licenses",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    )
)
public class LicenseService extends EntityServiceImpl<License, String> {

  @Inject
  LicenseService(LicenseStore licenseStore, LoginResolver loginResolver) {
    super(licenseStore, loginResolver, true);
  }
}
