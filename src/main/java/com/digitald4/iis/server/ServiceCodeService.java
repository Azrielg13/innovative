package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.storage.ServiceCodeStore;
import com.google.api.server.spi.config.*;
import javax.inject.Inject;

@Api(
    name = "serviceCodes",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class ServiceCodeService extends EntityServiceImpl<ServiceCode, String> {
  @Inject
  ServiceCodeService(ServiceCodeStore serviceCodeStore, LoginResolver loginResolver) {
    super(serviceCodeStore, loginResolver);
  }
}
