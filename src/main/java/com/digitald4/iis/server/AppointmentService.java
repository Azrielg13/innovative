package com.digitald4.iis.server;

import static com.digitald4.iis.model.Appointment.AppointmentState.*;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.server.service.EntityServiceBulkImpl;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.SequenceStore;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.Repeat;
import com.digitald4.iis.model.Appointment.Repeat.Type;
import com.digitald4.iis.storage.AppointmentStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

@Api(
    name = "appointments",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class AppointmentService extends EntityServiceBulkImpl<Long, Appointment> {
  public enum EventOption {This, This_and_following, All};
  private final AppointmentStore appointmentStore;
  private final LoginResolver loginResolver;
  private final SequenceStore sequenceStore;

  @Inject
  AppointmentService(AppointmentStore appointmentStore, LoginResolver loginResolver, SequenceStore sequenceStore) {
    super(appointmentStore, loginResolver);
    this.appointmentStore = appointmentStore;
    this.loginResolver = loginResolver;
    this.sequenceStore = sequenceStore;
  }

  @ApiMethod(httpMethod = HttpMethod.POST, path = "batchCreate")
  public ImmutableList<Appointment> batchCreate(
      Appointments appointments, @Nullable @Named("idToken") String idToken) throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      return getStore().create(
          appointments.getItems().stream().flatMap(app -> expand(app).stream()).collect(toImmutableList()));
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }

  @ApiMethod(httpMethod = HttpMethod.DELETE, path = "cancelOut")
  public ImmutableList<Long> cancelOut(@Named("id") long id, @Nullable @Named("idToken") String idToken,
      @Named("eventOption") @DefaultValue("This") EventOption eventOption) throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      if (eventOption == EventOption.This) {
        appointmentStore.delete(id);
        return ImmutableList.of(id);
      }

      Appointment appointment = appointmentStore.get(id);

      Filter seriesFilter = Filter.of("seriesId", appointment.getSeriesId());

      ImmutableList<Long> ids = appointmentStore
          .list(eventOption == EventOption.All ? Query.forList(seriesFilter)
              : Query.forList(seriesFilter, Filter.of("date", ">=", appointment.getDate())))
          .getItems().stream().map(Appointment::getId).collect(toImmutableList());
      appointmentStore.delete(ids);
      return ids;
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    } catch (Exception e) {
      throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), e);
    }
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "closeOut")
  public AtomicInteger closeOut(@Named("priorTo") long priorTo, @Nullable @Named("idToken") String idToken)
      throws ServiceException {
    try {
      loginResolver.resolve(idToken, true);
      ImmutableList<Appointment> appointments = appointmentStore
          .list(Query.forList(
              Query.Filter.of("end", "<", priorTo),
              Query.Filter.of("state", "IN",
                  ImmutableList.of(UNCONFIRMED, CONFIRMED, PENDING_ASSESSMENT, PENDING_APPROVAL, BILLABLE_AND_PAYABLE))))
          .getItems();
      appointments.forEach(a -> a.setState(CLOSED));
      return new AtomicInteger(appointmentStore.create(appointments).size());
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }

  @VisibleForTesting ImmutableList<Appointment> expand(Appointment appointment) {
    Repeat repeat = appointment.getRepeat();
    if (repeat == null || repeat.getType() == Type.Does_not_repeat) {
      return ImmutableList.of(appointment);
    }

    appointment.setSeriesId(sequenceStore.getAndIncrement(Repeat.class));
    DateTime startDate = new DateTime(appointment.getDate().toEpochMilli());

    int visitDays = repeat.getUntil() == null ? 0 :
        (int) Duration.between(appointment.getDate(), repeat.getUntil().plus(1, ChronoUnit.DAYS)).toDays();
    switch (repeat.getType()) {
      case Daily -> {
        int visits = repeat.getVisits() != null ? repeat.getVisits() : visitDays;
        return IntStream.range(0, visits)
            .mapToObj(visit -> appointment.copy().setDate(startDate.plusDays(visit).getMillis()))
            .collect(toImmutableList());
      }
      case Weekly_on_same_day -> {
        int visits = repeat.getVisits() != null ? repeat.getVisits() : (int) Math.ceil(visitDays / 7.0);
        return IntStream.range(0, visits)
            .mapToObj(visit -> appointment.copy().setDate(startDate.plusWeeks(visit).getMillis()))
            .collect(toImmutableList());
      }
      case Monthly_on_same_day -> {
        int visits = repeat.getVisits() != null ? repeat.getVisits() : (int) Math.ceil(visitDays / 30.45);
        return IntStream.range(0, visits)
            .mapToObj(visit -> appointment.copy().setDate(startDate.plusMonths(visit).getMillis()))
            .collect(toImmutableList());
      }
      case Every_N_days -> {
        int nDays = repeat.getNumDays();
        int visits = repeat.getVisits() != null ? repeat.getVisits() : (int) Math.ceil(visitDays / (nDays * 1.0));
        return IntStream.range(0, visits)
            .mapToObj(visit -> appointment.copy().setDate(startDate.plusDays(visit * nDays).getMillis()))
            .collect(toImmutableList());
      }
      case Every_weekday -> {
        int visits = repeat.getVisits() != null ? repeat.getVisits() : (int) Math.floor(visitDays * 5 / 7.0);
        AtomicReference<DateTime> currentDate = new AtomicReference<>(startDate);
        return IntStream.range(0, visits)
            .mapToObj(visit -> {
              DateTime date = currentDate.get().plusDays(visit > 0 ? 1 : 0);
              if (date.getDayOfWeek() == DateTimeConstants.SUNDAY) {
                date = date.plusDays(1);
              } else if (date.getDayOfWeek() == DateTimeConstants.SATURDAY) {
                date = date.plusDays(2);
              }
              currentDate.set(date);
              return date;
            })
            .map(date -> appointment.copy().setDate(date.getMillis()))
            .collect(toImmutableList());
      }
      case Weekly_on_days -> {
        ImmutableSet<Integer> days = repeat.getDays();
        int visits = repeat.getVisits() != null ? repeat.getVisits() : (int) Math.ceil(visitDays * days.size() / 7.0);
        AtomicReference<DateTime> currentDate = new AtomicReference<>(startDate);
        return IntStream.range(0, visits)
            .mapToObj(visit -> {
              DateTime date = currentDate.get().plusDays(visit > 0 ? 1 : 0);
              while (!days.contains(date.getDayOfWeek() % 7 + 1)) {
                date = date.plusDays(1);
              }
              currentDate.set(date);
              return date;
            })
            .map(date -> appointment.copy().setDate(date.getMillis()))
            .collect(toImmutableList());
      }
    }

    throw new DD4StorageException("Unhandled repeat type: " + repeat.getType());
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
