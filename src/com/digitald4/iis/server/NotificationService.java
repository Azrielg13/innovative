package com.digitald4.iis.server;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.Filter;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.JSONService;
import com.digitald4.common.storage.ListResponse;
import com.digitald4.common.storage.Store;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.proto.IISProtos.License;
import com.digitald4.iis.proto.IISProtos.Patient;
import com.digitald4.iis.proto.IISUIProtos.Notification;
import com.digitald4.iis.proto.IISUIProtos.NotificationRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class NotificationService implements JSONService {

	private final Store<License> licenseStore;
	private final Store<Patient> patientStore;

	NotificationService(Store<License> licenseStore, Store<Patient> patientStore) {
		this.licenseStore = licenseStore;
		this.patientStore = patientStore;
	}

	public JSONObject create(JSONObject createRequest) {
		throw new UnsupportedOperationException("Unimplemented");
	}

	public JSONObject get(JSONObject getRequest) {
		throw new UnsupportedOperationException("Unimplemented");
	}

	public JSONObject list(JSONObject listRequest) {
		return DualProtoService.listToJSON.apply(
				list(DualProtoService.transformJSONRequest(NotificationRequest.getDefaultInstance(), listRequest)));
	}

	public ListResponse<Notification> list(NotificationRequest request) throws DD4StorageException {
		List<Notification> notifications = new ArrayList<>();
		long startDate = request.getStartDate();
		long endDate = request.getEndDate();
		long warningEndDate = endDate + 30 * Calculate.ONE_DAY;
		if (request.getEntity().equals("patient")) {
			Patient patient = patientStore.get(request.getEntityId());
			if (patient != null && patient.getEstLastDayOfService() >= startDate
					&& patient.getEstLastDayOfService() <= endDate) {
				notifications.add(patientConverter.apply(patient));
			}
		} else if (request.getEntity().equals("nurse")) {
			for (License license : licenseStore.list(ListRequest.newBuilder()
					.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperan(">=").setValue("" + startDate))
					.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperan("<=").setValue("" + warningEndDate))
					.addFilter(Filter.newBuilder().setColumn("nurse_id").setOperan("=").setValue("" + request.getEntityId()))
					.build()).getItemsList()) {
				if (license.getExpirationDate() >= startDate && license.getExpirationDate() <= endDate) {
					notifications.add(licenseErrorConverter.apply(license));
				}
				long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
				if (warningDate >= startDate && warningDate <= endDate) {
					notifications.add(licenseWarningConverter.apply(license));
				}
			}
		} else if (request.getEntity().equals("vendor")) {
			notifications.addAll(patientStore
					.list(ListRequest.newBuilder()
							.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperan(">=").setValue("" + startDate))
							.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperan("<=").setValue("" + endDate))
							.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperan("=").setValue("" + request.getEntityId()))
							.build()).getItemsList()
					.stream()
					.map(patientConverter)
					.collect(Collectors.toList()));
		} else {
			notifications.addAll(patientStore
					.list(ListRequest.newBuilder()
							.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperan(">=").setValue("" + startDate))
							.addFilter(Filter.newBuilder().setColumn("est_last_day_of_service").setOperan("<=").setValue("" + endDate))
							.build()).getItemsList()
					.stream()
					.map(patientConverter)
					.collect(Collectors.toList()));
			for (License license : licenseStore.list(ListRequest.newBuilder()
					.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperan(">=").setValue("" + startDate))
					.addFilter(Filter.newBuilder().setColumn("expiration_date").setOperan("<=").setValue("" + warningEndDate))
					.build()).getItemsList()) {
				if (license.getExpirationDate() >= startDate && license.getExpirationDate() <= endDate) {
					notifications.add(licenseErrorConverter.apply(license));
				}
				long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
				if (warningDate >= startDate && warningDate <= endDate) {
					notifications.add(licenseWarningConverter.apply(license));
				}
			}
		}
		return ListResponse.<Notification>newBuilder()
				.addAllItems(notifications
						.stream()
						.skip(request.getPageToken())
						.limit(request.getPageSize() != 0 ? request.getPageSize() : 250)
						.collect(Collectors.toList()))
				.setTotalSize(notifications.size())
				.build();
	}

	public JSONObject update(JSONObject getRequest) {
		throw new UnsupportedOperationException("Unimplemented");
	}

	public JSONObject delete(JSONObject deleteRequest) {
		throw new UnsupportedOperationException("Unimplemented");
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
			case "create": return create(jsonRequest);
			case "get": return get(jsonRequest);
			case "list": return list(jsonRequest);
			case "update": return update(jsonRequest);
			case "delete": return delete(jsonRequest);
			default:
				throw new DD4StorageException("Invalid action: " + action);
		}
	}

	@Override
	public boolean requiresLogin(String action) {
		return true;
	}
}
