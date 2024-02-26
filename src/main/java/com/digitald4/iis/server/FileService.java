package com.digitald4.iis.server;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.LicenseStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(
    name = "files",
    version = "v1",
    namespace =
    @ApiNamespace(
        ownerDomain = "nbastats.digitald4.com",
        ownerName = "nbastats.digitald4.com"
    ),
    // [START_EXCLUDE]
    issuers = {
        @ApiIssuer(
            name = "firebase",
            issuer = "https://securetoken.google.com/fantasy-predictor",
            jwksUri =
                "https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system"
                    + ".gserviceaccount.com"
        )
    }
    // [END_EXCLUDE]
)
public class FileService extends com.digitald4.common.server.service.FileService {
  private final AppointmentStore appointmentStore;
  private final LicenseStore licenseStore;

  @Inject
  public FileService(Store<DataFile, String> fileStore, LoginResolver loginResolver,
      Provider<HttpServletRequest> requestProvider, Provider<HttpServletResponse> responseProvider,
      AppointmentStore appointmentStore, LicenseStore licenseStore) {
    super(fileStore, loginResolver, requestProvider, responseProvider);
    this.appointmentStore = appointmentStore;
    this.licenseStore = licenseStore;
  }

  @Override
  @ApiMethod(httpMethod = ApiMethod.HttpMethod.DELETE, path = "delete")
  public AtomicBoolean delete(@Named("id") String id, @Nullable @Named("idToken") String idToken)
      throws ServiceException {
    String[] idParts = id.split("-");
    switch (idParts[0]) {
      case "appointment":
        appointmentStore.update(Long.parseLong(idParts[1]), app -> app.setAssessmentReport(null));
        break;
      case "license":
        licenseStore.update(idParts[1] + "-" + idParts[2], lic -> lic.setFileReference(null));
        break;
      default:
        throw new DD4StorageException(
            "Unknown type for file association: " + idParts[0], ErrorCode.BAD_REQUEST);

    }
    return super.delete(id, idToken);
  }
}
