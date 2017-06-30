package com.digitald4.iis.server;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.Filter;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.JSONService;
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

import org.json.JSONArray;
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

	public JSONArray list(JSONObject getRequest) {
		throw new UnsupportedOperationException("Unimplemented");
	}

	public List<Notification> list(NotificationRequest request) throws DD4StorageException {
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
			for (License license : licenseStore.get(
					Filter.newBuilder().setColumn("expiration_date").setOperan(">=").setValue("" + startDate).build(),
					Filter.newBuilder().setColumn("expiration_date").setOperan("<=").setValue("" + warningEndDate).build(),
					Filter.newBuilder().setColumn("nurse_id").setOperan("=").setValue("" + request.getEntityId()).build())) {
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
					.get(Filter.newBuilder().setColumn("est_last_day_of_service").setOperan(">=").setValue("" + startDate)
									.build(),
							Filter.newBuilder().setColumn("est_last_day_of_service").setOperan("<=").setValue("" + endDate)
									.build(),
							Filter.newBuilder().setColumn("vendor_id").setOperan("=").setValue("" + request.getEntityId())
									.build())
					.stream()
					.map(patientConverter)
					.collect(Collectors.toList()));
		} else {
			notifications.addAll(patientStore
					.get(Filter.newBuilder().setColumn("est_last_day_of_service").setOperan(">=").setValue("" + startDate)
									.build(),
							Filter.newBuilder().setColumn("est_last_day_of_service").setOperan("<=").setValue("" + endDate)
									.build())
					.stream()
					.map(patientConverter)
					.collect(Collectors.toList()));
			for (License license : licenseStore.get(
					Filter.newBuilder().setColumn("expiration_date").setOperan(">=").setValue("" + startDate).build(),
					Filter.newBuilder().setColumn("expiration_date").setOperan("<=").setValue("" + warningEndDate).build())) {
				if (license.getExpirationDate() >= startDate && license.getExpirationDate() <= endDate) {
					notifications.add(licenseErrorConverter.apply(license));
				}
				long warningDate = license.getExpirationDate() - 30 * Calculate.ONE_DAY;
				if (warningDate >= startDate && warningDate <= endDate) {
					notifications.add(licenseWarningConverter.apply(license));
				}
			}
		}
		return notifications;
	}

	public JSONObject update(JSONObject getRequest) {
		throw new UnsupportedOperationException("Unimplemented");
	}

	public boolean delete(JSONObject deleteRequest) {
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
	public JSONObject performAction(String action, JSONObject jsonRequest) throws Exception {
		if (!action.equalsIgnoreCase("list")) {
			throw new UnsupportedOperationException("Unsupported opertion: " + action);
		}
		return new JSONObject().put("data", DualProtoService.convertToJSON(
				list(DualProtoService.transformJSONRequest(NotificationRequest.getDefaultInstance(), jsonRequest))));
	}

	@Override
	public boolean requiresLogin(String action) {
		return true;
	}
}
