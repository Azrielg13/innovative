package com.digitald4.iis.server;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4Protos.Query;
import com.digitald4.common.proto.DD4Protos.Query.Filter;
import com.digitald4.common.server.JSONService;
import com.digitald4.common.storage.QueryResult;
import com.digitald4.common.storage.Store;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.ProtoUtil;
import com.digitald4.iis.proto.IISProtos.License;
import com.digitald4.iis.proto.IISProtos.Patient;
import com.digitald4.iis.proto.IISUIProtos.Notification;
import com.digitald4.iis.proto.IISUIProtos.NotificationRequest;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
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
public class NotificationService implements JSONService {

	private final Store<License> licenseStore;
	private final Store<Patient> patientStore;

	NotificationService(Store<License> licenseStore, Store<Patient> patientStore) {
		this.licenseStore = licenseStore;
		this.patientStore = patientStore;
	}

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, path = "/notifications/v1/")
	public QueryResult<Notification> list(NotificationRequest request) {
		List<Notification> notifications = new ArrayList<>();
		long startDate = request.getStartDate();
		long endDate = request.getEndDate();
		long warningEndDate = endDate + 30 * Calculate.ONE_DAY;
		switch (request.getEntity()) {
			case "patient": {
				Patient patient = patientStore.get(request.getEntityId());
				if (patient != null && patient.getEstLastDayOfService() >= startDate
						&& patient.getEstLastDayOfService() <= endDate) {
					notifications.add(patientConverter.apply(patient));
				}
				break;
			}
			case "nurse":
				licenseStore.list(Query.newBuilder()
						.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperator(">=").setValue("" + startDate))
						.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperator("<=").setValue("" + warningEndDate))
						.addFilter(Filter.newBuilder().setColumn("nurse_id").setOperator("=").setValue("" + request.getEntityId()))
						.build()).getResults().forEach(license -> {
							if (license.getExpirationDate() >= startDate && license.getExpirationDate() <= endDate) {
								notifications.add(licenseErrorConverter.apply(license));
							}
							long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
							if (warningDate >= startDate && warningDate <= endDate) {
								notifications.add(licenseWarningConverter.apply(license));
							}
						});
				break;
			case "vendor":
				notifications.addAll(patientStore
						.list(Query.newBuilder()
								.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperator(">=").setValue("" + startDate))
								.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperator("<=").setValue("" + endDate))
								.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperator("=").setValue("" + request.getEntityId()))
								.build())
						.getResults()
						.stream()
						.map(patientConverter)
						.collect(Collectors.toList()));
				break;
			default: {
				notifications.addAll(patientStore
						.list(Query.newBuilder()
								.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperator(">=").setValue("" + startDate))
								.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperator("<=").setValue("" + endDate))
								.build())
						.getResults()
						.stream()
						.map(patientConverter)
						.collect(Collectors.toList()));
				for (License license : licenseStore.list(Query.newBuilder()
						.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperator(">=").setValue("" + startDate))
						.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperator("<=").setValue("" + warningEndDate))
						.build()).getResults()) {
					if (license.getExpirationDate() >= startDate && license.getExpirationDate() <= endDate) {
						notifications.add(licenseErrorConverter.apply(license));
					}
					long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
					if (warningDate >= startDate && warningDate <= endDate) {
						notifications.add(licenseWarningConverter.apply(license));
					}
				}
			}
		}
		return new QueryResult<>(
				notifications
						.stream()
						.skip(request.getPageToken())
						.limit(request.getPageSize() != 0 ? request.getPageSize() : 250)
						.collect(Collectors.toList()),
				notifications.size());
	}

	private static final Function<Patient, Notification> patientConverter = patient -> Notification.newBuilder()
			.setType(Notification.Type.NT_INFO)
			.setTitle("Last day of service for: " + patient.getName())
			.setDate(patient.getEstLastDayOfService())
			.setEntity("patient")
			.setEntityId(patient.getId())
			.build();

	private static final Function<License, Notification> licenseWarningConverter = license -> Notification.newBuilder()
			.setType(Notification.Type.NT_WARNING)
			.setTitle("30 days till " + license.getLicTypeName() + " expiration: " + license.getNurseName())
			.setDate(license.getExpirationDate() - 30 * Calculate.ONE_DAY)
			.setEntity("nurse")
			.setEntityId(license.getNurseId())
			.build();

	private static final Function<License, Notification> licenseErrorConverter = license -> Notification.newBuilder()
			.setType(Notification.Type.NT_ERROR)
			.setTitle("Expiration of " + license.getLicTypeName() + ": " + license.getNurseName())
			.setDate(license.getExpirationDate())
			.setEntity("nurse")
			.setEntityId(license.getNurseId())
			.build();

	@Override
	public JSONObject performAction(String action, JSONObject jsonRequest) throws DD4StorageException {
		switch (action) {
			case "list":
				return ProtoUtil.toJSON(list(ProtoUtil.toProto(NotificationRequest.getDefaultInstance(), jsonRequest)));
			default:
				throw new DD4StorageException("Invalid action: " + action, HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	public boolean requiresLogin(String action) {
		return true;
	}
}
