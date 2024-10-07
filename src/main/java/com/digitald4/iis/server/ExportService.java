package com.digitald4.iis.server;

import static java.util.stream.Collectors.*;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.Address;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.server.service.Empty;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.storage.PatientStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;

@Api(
    name = "exports",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    )
)
public class ExportService {
  private static final String CLIENT_COLUMNS = "Staffr Id,Staffr Guid,Groups,Name,Ssn,Medicare Id,"
      + "Communication Method,Client Registration Number,Phone Main,Phone Other,Phone Personal,Email,"
      + "Service Address,Address Suite,Referred By,Gender,Peds Or Adult,Diagnosis,"
      + "Date Of Birth,Patient Rx,Therapy Type,Iv Access,Pump Or Gravity,Pump Brand,"
      + "Labs Yes Or No,Type Of Visit Soc Or Follow Up Or Recert,Recert Period From And To,"
      + "Scheduling Preference,One Time Visit Or Manage,Notes,Services Erl\n";
  private final LoginResolver loginResolver;
  private final Provider<HttpServletResponse> responseProvider;
  private final PatientStore patientStore;

  @Inject
  ExportService(LoginResolver loginResolver, Provider<HttpServletResponse> responseProvider,
      PatientStore patientStore) {
    this.loginResolver = loginResolver;
    this.responseProvider = responseProvider;
    this.patientStore = patientStore;
  }

  @ApiMethod(httpMethod = HttpMethod.GET, path = "{fileName}")
  public Empty getExport(@Named("fileName") String fileName,
      @Nullable @Named("idToken") String idToken) throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      String exportData = exportClients();
      HttpServletResponse response = responseProvider.get();
      response.setContentType("application/csv");
      // Cache for the max of 1 year bcz we like to add a new file rather than update files.
      // response.setHeader("Cache-Control", "max-age=31536000");
      response.setContentLength(exportData.length());
      response.getOutputStream().write(exportData.getBytes());
    } catch (IOException ioe) {
      throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ioe);
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
    return null;
  }

  private String exportClients() {
    return patientStore.list(Query.forList()).getItems().stream()
        .sorted(Comparator.comparing(Patient::fullName))
        .map(p -> Stream
            .of(p.getId(), "", p.fullName(), "", p.getMrNum(), "", "",
                p.getPhonePrimary(), p.getPhoneAlternate(), p.getPhonePersonal(), p.getEmail(),
                getAddress(p.getServiceAddress()), getUnit(p.getServiceAddress()),
                p.getReferralSource(), p.getGender(), "", p.getDiagnosis(), p.getDateOfBirth(),
                p.getRx(), p.getTherapyType(), p.getIvAccess(), p.getIvType(), p.getIvPumpBrand(),
                p.isLabs() ? "Yes" : "No", p.getVisitType(), p.getFirstRecertDue(),
                p.getSchedulingPreference(), p.getVisitFrequency(), p.getReferralNote(), "")
            .map(v -> String.valueOf(v == null ? "" : v))
            .map(v -> v.contains(",") ? "\"" + v + "\"" : v)
            .collect(joining(",")))
        .collect(joining("\n", CLIENT_COLUMNS, ""));
  }

  public static String getAddress(Address address) {
    return address == null ? null : address.getAddress();
  }

  public static String getUnit(Address address) {
    return address == null ? null : address.getUnit();
  }

  public static void main(String[] args) throws Exception {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    PatientStore patientStore = new PatientStore(() -> dao);
    String clientExport = new ExportService(null, null, patientStore).exportClients();
    try (BufferedOutputStream bos =
        new BufferedOutputStream(Files.newOutputStream(Paths.get("data/clientsOutput.csv")))) {
      bos.write(clientExport.getBytes());
    }
  }
}
