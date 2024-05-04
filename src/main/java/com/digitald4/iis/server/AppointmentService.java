package com.digitald4.iis.server;

import com.digitald4.common.server.service.EntityServiceBulkImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.storage.AppointmentStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.*;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

@Api(
    name = "appointments",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class AppointmentService extends EntityServiceBulkImpl<Long, Appointment> {
  private final AppointmentStore appointmentStore;
  @Inject
  AppointmentService(AppointmentStore appointmentStore, LoginResolver loginResolver) {
    super(appointmentStore, loginResolver);
    this.appointmentStore = appointmentStore;
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST, path = "batchCreate")
  public ImmutableList<Appointment> batchCreate(
      Appointments appointments, @Nullable @Named("idToken") String idToken) throws ServiceException {
    return appointmentStore.create(appointments.getItems());
  }

  public static class Appointments {
    private ImmutableList<Appointment> items = ImmutableList.of();

    public ImmutableList<Appointment> getItems() {
      return items;
    }

    public Appointments setItems(Iterable<Appointment> items) {
      this.items = ImmutableList.copyOf(items);
      return this;
    }
  }
}
