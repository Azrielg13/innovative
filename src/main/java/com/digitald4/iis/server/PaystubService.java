package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.storage.PaystubStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "paystubs",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    )
)
public class PaystubService extends AdminService<Paystub> {

  @Inject
  PaystubService(PaystubStore paystubStore, LoginResolver loginResolver) {
    super(paystubStore, loginResolver);
  }
}
