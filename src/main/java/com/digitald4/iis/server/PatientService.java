package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.storage.PatientStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "patients",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class PatientService extends AdminService<Patient> {
  @Inject
  PatientService(PatientStore patientStore, LoginResolver loginResolver) {
    super(patientStore, loginResolver);
  }
}