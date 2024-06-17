package com.digitald4.iis.server;

import static com.digitald4.iis.model.Appointment.AppointmentState.*;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.service.EntityServiceBulkImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.storage.AppointmentStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.*;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

@Api(
    name = "appointments",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class AppointmentService extends EntityServiceBulkImpl<Long, Appointment> {
  private final AppointmentStore appointmentStore;
  private final LoginResolver loginResolver;

  @Inject AppointmentService(AppointmentStore appointmentStore, LoginResolver loginResolver) {
    super(appointmentStore, loginResolver);
    this.appointmentStore = appointmentStore;
    this.loginResolver = loginResolver;
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST, path = "batchCreate")
  public ImmutableList<Appointment> batchCreate(Appointments appointments, @Nullable @Named("idToken") String idToken)
      throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      return appointmentStore.create(appointments.getItems());
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "closeOut")
  public AtomicInteger closeOut(@Named("priorTo") long priorTo, @Nullable @Named("idToken") String idToken) throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      ImmutableList<Appointment> appointments = appointmentStore
          .list(Query.forList(
              Query.Filter.of("end", "<", priorTo),
              Query.Filter.of("state", "IN", ImmutableList.of(PENDING_ASSESSMENT, BILLABLE_AND_PAYABLE))))
          .getItems();
      appointments.forEach(a -> a.setState(CLOSED));
      return new AtomicInteger(appointmentStore.create(appointments).size());
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
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
