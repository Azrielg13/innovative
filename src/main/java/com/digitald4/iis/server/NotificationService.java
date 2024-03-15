package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.server.service.JSONService;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Notification;
import com.digitald4.iis.model.Notification.EntityType;
import com.digitald4.iis.model.Notification.Type;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.storage.LicenseStore;
import com.digitald4.iis.storage.PatientStore;
import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.config.*;
import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.json.JSONObject;

@Api(
    name = "notifications",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    )
)
public class NotificationService {

  private static final Function<Patient, Notification> patientConverter = patient ->
      new Notification(
          Type.INFO,
          "Last day of service for: " + patient.getName(),
          patient.getEstLastDayOfService(),
          EntityType.PATIENT,
          patient.getId());
  private static final Function<License, Notification> licenseInfoConverter = lic ->
      new Notification(
          Type.INFO,
          String.format("90 days till %s expiration: %s", lic.getLicTypeName(), lic.getNurseName()),
          lic.getExpirationDate().minus(90, ChronoUnit.DAYS),
          EntityType.NURSE,
          lic.getNurseId());
  private static final Function<License, Notification> licenseWarningConverter = lic ->
      new Notification(
          Type.WARNING,
          String.format("30 days till %s expiration: %s", lic.getLicTypeName(), lic.getNurseName()),
          lic.getExpirationDate().minus(30, ChronoUnit.DAYS),
          EntityType.NURSE,
          lic.getNurseId());
  private static final Function<License, Notification> licenseErrorConverter = license ->
      new Notification(
          Type.ERROR,
          String.format("Expiration of %s: %s", license.getLicTypeName(), license.getNurseName()),
          license.getExpirationDate(),
          EntityType.NURSE,
          license.getNurseId());
  private final LicenseStore licenseStore;
  private final PatientStore patientStore;
  private final LoginResolver loginResolver;

  @Inject
  NotificationService(LicenseStore licenseStore, PatientStore patientStore, LoginResolver loginResolver) {
    this.licenseStore = licenseStore;
    this.patientStore = patientStore;
    this.loginResolver = loginResolver;
  }

  @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "list")
  public QueryResult<Notification> list(
      @Nullable @Named("entityType") EntityType entityType, @Named("entityId") @DefaultValue("0") long entityId,
      @Named("startDate") long startDateMillis, @Named("endDate") long endDateMillis,
      @Named("pageSize") @DefaultValue("250") int pageSize, @Named("pageToken") @DefaultValue("0") int pageToken,
      @Named("idToken") String idToken) throws ServiceException {
    Instant startDate = Instant.ofEpochMilli(startDateMillis);
    Instant endDate = Instant.ofEpochMilli(endDateMillis);
    Instant warningEndDate = endDate.plus(30, ChronoUnit.DAYS);
    Instant infoEndDate = endDate.plus(90, ChronoUnit.DAYS);
    try {
      loginResolver.resolve(idToken, true);
      entityType = entityType == null ? EntityType.ALL : entityType;
      ImmutableList.Builder<Notification> notifications = ImmutableList.builder();
      switch (entityType) {
        case PATIENT -> {
          Patient patient = patientStore.get(entityId);
          Instant estLast = patient.getEstLastDayOfService();
          if (estLast != null && estLast.isAfter(startDate) && estLast.isBefore(endDate)) {
            notifications.add(patientConverter.apply(patient));
          }
        }
        case NURSE -> licenseStore
            .list(
                Query.forList().setFilters(
                    Filter.of("expirationDate", ">=", startDateMillis),
                    Filter.of("expirationDate", "<=", infoEndDate.toEpochMilli()),
                    Filter.of("nurseId", "=", entityId)))
            .getItems()
            .forEach(license -> {
              Instant expDate = license.getExpirationDate();
              if (expDate.isAfter(startDate) && expDate.isBefore(endDate)) {
                notifications.add(licenseErrorConverter.apply(license));
              } else if (expDate.isAfter(endDate) && expDate.isBefore(warningEndDate)) {
                notifications.add(licenseWarningConverter.apply(license));
              } else if (expDate.isAfter(warningEndDate) && expDate.isBefore(infoEndDate)) {
                notifications.add(licenseInfoConverter.apply(license));
              }
            });
        case VENDOR -> notifications.addAll(
            patientStore
                .list(
                    Query.forList().setFilters(
                        Filter.of("billingVendorId", "=", entityId),
                        Filter.of("estLastDayOfService", ">=", startDateMillis),
                        Filter.of("estLastDayOfService", "<=", endDateMillis)))

                .getItems()
                .stream()
                .map(patientConverter)
                .collect(Collectors.toList()));
        case ALL -> {
          notifications.addAll(
              patientStore
                  .list(
                      Query.forList().setFilters(
                          Filter.of("estLastDayOfService", ">=", startDateMillis),
                          Filter.of("estLastDayOfService", "<=", endDateMillis)))
                  .getItems()
                  .stream()
                  .map(patientConverter)
                  .collect(toImmutableList()));

          licenseStore
              .list(
                  Query.forList().setFilters(
                      Filter.of("expirationDate", ">=", startDateMillis),
                      Filter.of("expirationDate", "<=", infoEndDate.toEpochMilli())))
              .getItems()
              .forEach(license -> {
                Instant expDate = license.getExpirationDate();
                if (expDate.isAfter(startDate) && expDate.isBefore(endDate)) {
                  notifications.add(licenseErrorConverter.apply(license));
                } else if (expDate.isAfter(endDate) && expDate.isBefore(warningEndDate)) {
                  notifications.add(licenseWarningConverter.apply(license));
                } else if (expDate.isAfter(warningEndDate) && expDate.isBefore(infoEndDate)) {
                  notifications.add(licenseInfoConverter.apply(license));
                }
              });
        }
      }

      ImmutableList<Notification> result = notifications.build();

      return QueryResult.of(
          result.stream()
              .skip(pageToken)
              .limit(pageSize != 0 ? pageSize : 250)
              .collect(toImmutableList()),
          result.size(),
          Query.forList(null, null, pageSize, pageToken));
    } catch (DD4StorageException e) {
      throw new ServiceException(e.getErrorCode(), e);
    }
  }

  protected static class NotificationJSONService implements JSONService {

    private final NotificationService notificationService;

    public NotificationJSONService(NotificationService notificationService) {
      this.notificationService = notificationService;
    }

    public JSONObject performAction(String action, JSONObject jsonRequest) throws ServiceException {
      if ("list".equals(action)) {
        return JSONUtil.toJSON(
            notificationService.list(
                EntityType.valueOf(jsonRequest.optString("entityType", "ALL")),
                jsonRequest.optLong("entityId"),
                jsonRequest.optLong("startDate"), jsonRequest.optLong("endDate"),
                jsonRequest.optInt("pageSize"), jsonRequest.optInt("pageToken"),
                jsonRequest.optString("idToken")));
      }
      throw new DD4StorageException(
          "Invalid action: " + action, DD4StorageException.ErrorCode.BAD_REQUEST);
    }
  }
}
