package com.digitald4.iis.server;

import com.digitald4.common.storage.SessionStore;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.User;
import com.digitald4.iis.storage.InvoiceStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "invoices",
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
public class InvoiceService extends AdminService<Invoice> {

  @Inject
  InvoiceService(InvoiceStore invoiceStore, SessionStore<User> sessionStore) {
    super(invoiceStore, sessionStore);
  }
}