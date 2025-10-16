package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.ReferralResponse;
import com.digitald4.iis.storage.ReferralResponseStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "referralResponse",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class ReferralResponseService extends EntityServiceImpl<ReferralResponse, String> {
  @Inject
  ReferralResponseService(ReferralResponseStore referralResponseStore, LoginResolver loginResolver) {
    super(referralResponseStore, loginResolver);
  }
}
