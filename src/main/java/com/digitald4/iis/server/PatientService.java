package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.storage.PatientStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.common.collect.ImmutableSet;
import javax.inject.Inject;

@Api(
    name = "patients",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class PatientService extends AdminService<Patient> {
  private static final ImmutableSet<String> REPORT_FIELDS = ImmutableSet.of("id", "creationTime", "status",
      "referralResolutionDate", "referralResolution", "billingVendorId", "billingVendorName", "condition", "firstAppointmentDate");

  @Inject
  PatientService(PatientStore patientStore, LoginResolver loginResolver) {
    super(patientStore, loginResolver);
  }

  @Override
  protected Iterable<Patient> transformForReport(Iterable<Patient> entities) {
    return stream(entities)
        .map(JSONUtil::toJSON)
        .peek(json -> ImmutableSet.copyOf(json.keySet()).stream()
            .filter(key -> !REPORT_FIELDS.contains(key)).forEach(json::remove))
        .map(json -> JSONUtil.toObject(Patient.class, json))
        .collect(toImmutableList());
  }
}