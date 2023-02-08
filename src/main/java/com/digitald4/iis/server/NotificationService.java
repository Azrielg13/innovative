package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.server.service.JSONService;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.JSONUtil;
import com.digitald4.iis.model.License;
import com.digitald4.iis.model.Notification;
import com.digitald4.iis.model.Notification.EntityType;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.storage.LicenseStore;
import com.digitald4.iis.storage.PatientStore;
import com.google.api.server.spi.config.*;
import com.google.common.collect.ImmutableList;
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
public class NotificationService {

	private final LicenseStore licenseStore;
	private final PatientStore patientStore;

	@Inject
	NotificationService(LicenseStore licenseStore, PatientStore patientStore) {
		this.licenseStore = licenseStore;
		this.patientStore = patientStore;
	}

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public QueryResult<Notification> list(
			@Nullable @Named("entityType") EntityType entityType, @Named("entityId") @DefaultValue("0") long entityId,
			@Named("startDate") long startDate, @Named("endDate") long endDate,
			@Named("pageSize") @DefaultValue("250") int pageSize, @Named("pageToken") @DefaultValue("0") int pageToken) {
		entityType = entityType == null ? EntityType.ALL : entityType;
		ImmutableList.Builder<Notification> notifications = ImmutableList.builder();
		long warningEndDate = endDate + 30 * Calculate.ONE_DAY;
		switch (entityType) {
			case PATIENT: {
				Patient patient = patientStore.get(entityId);
				if (patient != null && patient.getEstLastDayOfService() >= startDate
						&& patient.getEstLastDayOfService() <= endDate) {
					notifications.add(patientConverter.apply(patient));
				}
				break;
			}
			case NURSE:
				licenseStore
						.list(
								Query.forList().setFilters(
										Filter.of("expirationDate", ">=", startDate),
										Filter.of("expirationDate", "<=", warningEndDate),
										Filter.of("nurseId", "=", entityId)))
						.getItems()
						.forEach(license -> {
							long expMillis = license.getExpirationDate();
							if (expMillis >= startDate && expMillis <= endDate) {
								notifications.add(licenseErrorConverter.apply(license));
							}
							long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
							if (warningDate >= startDate && warningDate <= endDate) {
								notifications.add(licenseWarningConverter.apply(license));
							}
						});
				break;
			case VENDOR:
				notifications.addAll(patientStore
						.list(
								Query.forList().setFilters(
										Filter.of("est_last_day_of_service", ">=", startDate),
										Filter.of("est_last_day_of_service", "<=", endDate),
										Filter.of("vendor_id", "=", entityId)))
						.getItems()
						.stream()
						.map(patientConverter)
						.collect(Collectors.toList()));
				break;
			case ALL: {
				notifications.addAll(
						patientStore
								.list(
										Query.forList().setFilters(
												Filter.of("est_last_day_of_service", ">=", startDate),
												Filter.of("est_last_day_of_service", "<=", endDate)))
								.getItems()
								.stream()
								.map(patientConverter)
								.collect(toImmutableList()));

				licenseStore
						.list(
								Query.forList().setFilters(
										Filter.of("expiration_date", ">=", startDate),
										Filter.of("expiration_date", "<=", warningEndDate)))
						.getItems().forEach(license -> {
							long expMillis = license.getExpirationDate();
							if (expMillis >= startDate && expMillis <= endDate) {
								notifications.add(licenseErrorConverter.apply(license));
							}
							long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
							if (warningDate >= startDate && warningDate <= endDate) {
								notifications.add(licenseWarningConverter.apply(license));
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
	}

	private static final Function<Patient, Notification> patientConverter = patient -> new Notification(
			Notification.Type.INFO,
			"Last day of service for: " + patient.getName(),
			patient.getEstLastDayOfService(),
			EntityType.PATIENT,
			patient.getId());

	private static final Function<License, Notification> licenseWarningConverter = license -> new Notification(
			Notification.Type.WARNING,
			"30 days till " + license.getLicTypeName() + " expiration: " + license.getNurseName(),
			license.getExpirationDate() - 30 * Calculate.ONE_DAY,
			EntityType.NURSE,
			license.getNurseId());

	private static final Function<License, Notification> licenseErrorConverter = license -> new Notification(
			Notification.Type.ERROR,
			"Expiration of " + license.getLicTypeName() + ": " + license.getNurseName(),
			license.getExpirationDate(),
			EntityType.NURSE,
			license.getNurseId());

	protected static class NotificationJSONService implements JSONService {

		private final NotificationService notificationService;
		public NotificationJSONService(NotificationService notificationService) {
			this.notificationService = notificationService;
		}

		public JSONObject performAction(String action, JSONObject jsonRequest) {
			if ("list".equals(action)) {
				return JSONUtil.toJSON(
						notificationService.list(
								EntityType.valueOf(jsonRequest.optString("entityType", "ALL")), jsonRequest.optLong("entityId"),
								jsonRequest.optLong("startDate"), jsonRequest.optLong("endDate"),
								jsonRequest.optInt("pageSize"), jsonRequest.optInt("pageToken")));
			}
			throw new DD4StorageException("Invalid action: " + action, DD4StorageException.ErrorCode.BAD_REQUEST);
		}
	}
}
