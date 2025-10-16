package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Comparator.comparing;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Note;
import com.digitald4.iis.model.Note.Type;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.NoteStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.common.collect.Streams;
import java.time.Clock;
import java.time.Period;
import javax.inject.Inject;

@Api(
    name = "notes",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class NoteService extends AdminService<Note> {
  private final AppointmentStore appointmentStore;
  private final Clock clock;
  @Inject
  NoteService(NoteStore store, LoginResolver loginResolver, AppointmentStore appointmentStore, Clock clock) {
    super(store, loginResolver);
    this.appointmentStore = appointmentStore;
    this.clock = clock;
  }

  @Override @ApiMethod(httpMethod = "GET", path = "list")
  public QueryResult<Note> list(
      @Nullable @Named("fields") String fields, @Nullable @Named("filter") String filter,
      @Nullable @Named("orderBy") String orderBy, @Named("pageSize") @DefaultValue("200") int pageSize,
      @Named("pageToken") @DefaultValue("1") int pageToken, @Nullable @Named("idToken") String idToken)
      throws ServiceException {
    try {
      resolveLogin(idToken, true);
      var query = Query.forList(fields, filter, orderBy, pageSize, pageToken);
      QueryResult<Note> queryResult = getStore().list(query);
      // If these are notes for a Patient, we want to add the cancel note for appointments cancelled in the last 90 days.
      if (query.getFilters().stream().anyMatch( f -> f.getColumn().equals("entityType") && f.getValue().equals("Patient"))) {
        var appQuery = Query.forList(Filter.of("state", "CANCELLED"),
            Filter.of("date", ">", clock.instant().minus(Period.ofDays(90)).toEpochMilli()));
        query.getFilters().stream().filter(f -> f.getColumn().equals("entityId")).map(Filter::getValue).findAny()
            .ifPresent(patientId -> appQuery.addFilter(Filter.of("patientId", patientId)));
        var notes = Streams
            .concat(queryResult.getItems().stream(),
                appointmentStore.list(appQuery).getItems().stream().map(NoteService::toNote))
            .sorted(comparing(Note::getCreationTime).reversed())
            .collect(toImmutableList());
        queryResult = QueryResult.of(Note.class, notes,
            queryResult.getTotalSize() + notes.size() - queryResult.getItems().size(), query);
      }

      return queryResult;
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }

  private static Note toNote(Appointment app) {
    return (Note) new Note()
        .setId(app.getId())
        .setEntityType("Patient")
        .setEntityId(String.valueOf(app.getPatientId()))
        .setEntityName(app.getPatientName())
        .setNote(String.format("Cancelled Appointment for %s: %s", FormatText.formatDate(app.getDate()), app.getCancelReason()))
        .setType(Type.Cancelled_Appointment)
        .setCreationUsername(app.getLastModifiedUsername())
        .setCreationTime(app.getLastModifiedTime());
  }
}
