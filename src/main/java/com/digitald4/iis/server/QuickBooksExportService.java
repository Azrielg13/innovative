package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.QuickBooksExport;
import com.digitald4.iis.storage.QuickBooksExportStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Inject;

@Api(
    name = "quickBooksExports",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com"))
public class QuickBooksExportService extends EntityServiceImpl<QuickBooksExport, String> {

  @Inject
  QuickBooksExportService(QuickBooksExportStore quickBooksExportStore, LoginResolver loginResolver) {
    super(quickBooksExportStore, loginResolver);
  }
}
