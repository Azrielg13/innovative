package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.storage.InvoiceStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "invoices",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class InvoiceService extends AdminService<Invoice> {

  @Inject
  InvoiceService(InvoiceStore invoiceStore, LoginResolver loginResolver) {
    super(invoiceStore, loginResolver);
  }
}