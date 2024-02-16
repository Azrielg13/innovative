package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.storage.PatientStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import java.util.stream.Stream;
import javax.inject.Inject;

@Api(
    name = "patients",
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
public class PatientService extends AdminService<Patient> {

  private final Store<Vendor, Long> vendorStore;
  @Inject
  PatientService(
      PatientStore patientStore, Store<Vendor, Long> vendorStore, LoginResolver loginResolver) {
    super(patientStore, loginResolver);
    this.vendorStore = vendorStore;
  }

  @Override
  protected Iterable<Patient> transform(Iterable<Patient> patients) {
    ImmutableMap<Long, String> vendorNames = vendorStore
        .get(
            stream(patients)
                .flatMap(p -> Stream.of(p.getBillingVendorId(), p.getReferralSourceId()))
                .filter(Objects::nonNull)
                .collect(toImmutableSet()))
        .stream()
        .filter(vendor -> Objects.nonNull(vendor.getName()))
        .collect(toImmutableMap(Vendor::getId, Vendor::getName));

    patients.forEach(p -> p
        .setBillingVendorName(vendorNames.get(p.getBillingVendorId()))
        .setReferralSourceName(vendorNames.get(p.getReferralSourceId())));

    return patients;
  }
}