package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.storage.AppointmentStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "appointments",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    )
)
public class AppointmentService extends AdminService<Appointment> {

  @Inject
  AppointmentService(AppointmentStore appointmentStore, LoginResolver loginResolver) {
    super(appointmentStore, loginResolver);
  }
}
