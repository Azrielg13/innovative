package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.Address;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.ServiceCode.Unit;
import com.google.api.client.util.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.time.Clock;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;

public class QuickBooksExportStore extends GenericStore<QuickBooksExport, String> {
	private final Provider<DAO> daoProvider;
	private final AppointmentStore appointmentStore;
	private final Store<DataFile, String> dataFileStore;
	private final SequenceStore sequenceStore;
	private final Clock clock;

	@Inject
	public QuickBooksExportStore(Provider<DAO> daoProvider, AppointmentStore appointmentStore,
		 	Store<DataFile, String> dataFileStore, SequenceStore sequenceStore, Clock clock) {
		super(QuickBooksExport.class, daoProvider);
		this.daoProvider = daoProvider;
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.sequenceStore = sequenceStore;
		this.clock = clock;
	}

	@Override
	public QuickBooksExport create(QuickBooksExport qbExport) {
		var listResult = appointmentStore.get(qbExport.getAppointmentIds());
		if (!listResult.getMissingIds().isEmpty()) {
			throw new DD4StorageException(
					String.format("One of more appointments do not exist. Missing: %s", listResult.getMissingIds()),
					ErrorCode.BAD_REQUEST);
		}

		if (listResult.getItems().stream().anyMatch(app -> !Objects.equal(app.getExportId(), qbExport.getId()))) {
			throw new DD4StorageException(
					"One of more appointments already assigned to an export.", ErrorCode.BAD_REQUEST);
		}

		if (qbExport.getCreationTime() == null) {
			qbExport.setCreationTime(clock.instant());
		}

		if (qbExport.getId() == null) {
			qbExport.setId(String.format("IP360_%s_QUICKBOOKS_INV", FormatText.formatDate(qbExport.getCreationTime(), FormatText.BUILD_DATETIME)));
		}
		String exportId = qbExport.getId();

		String export = export(qbExport, listResult.getItems());
		DataFile dataFile = new DataFile()
				.setName(String.format("%s.csv", exportId))
				.setType("csv")
				.setSize(export.length())
				.setData(export.getBytes());

		qbExport.setFileReference(FileReference.of(dataFile));

		super.create(qbExport);
		dataFileStore.create(dataFile);
		qbExport.getAppointmentIds().forEach(appId -> appointmentStore.update(appId, app -> app.setExportId(exportId)));

		return qbExport;
	}

	private String export(QuickBooksExport qbExport, ImmutableList<Appointment> appointments) {
		// First we need to assign each appointment an invoiceId.
		var needInvoiceId = appointments.stream().filter(app -> app.getInvoiceId() == null).collect(toImmutableList());
		Iterator<Long> invoiceIds = sequenceStore.allocate(Invoice.class, needInvoiceId.size()).iterator();
		needInvoiceId.forEach(appointment -> appointment.setInvoiceId(invoiceIds.next()));

		DAO dao = daoProvider.get();
		ImmutableMap<Long, Patient> patients = dao.get(Patient.class, appointments.stream().map(Appointment::getPatientId)
				.collect(toImmutableList())).getItems().stream().collect(toImmutableMap(Patient::getId, identity()));
		ImmutableMap<Long, Vendor> vendors = dao.get(Vendor.class, appointments.stream().map(Appointment::getVendorId)
				.collect(toImmutableList())).getItems().stream().collect(toImmutableMap(Vendor::getId, identity()));
		ImmutableMap<String, ServiceCode> billCodes = dao.get(ServiceCode.class, appointments.stream()
				.map(app -> app.getBillingInfo().getServiceCode()).collect(toImmutableList())).getItems().stream()
				.collect(toImmutableMap(ServiceCode::getId, identity()));
		ImmutableMap<String, ServiceCode> payCodes = dao.get(ServiceCode.class, appointments.stream()
				.map(app -> app.getPaymentInfo().getServiceCode()).collect(toImmutableList())).getItems().stream()
				.collect(toImmutableMap(ServiceCode::getId, identity()));

		return "Invoice No,Visit ID,Customer,Invoice Date,Due Date,Caregiver,Department Name,Item (Product/Service)," +
				"Service Code Description,Bill Code,Bill Description,Quantity,Units,Item Rate,Item Amount,Taxable," +
				"Visit Date,Visit Start Time,Visit End Time,Item Tax Code,Tax Class %," +
				"Bill Address Line 1,Bill Address Line 2,Bill City,Bill Postal Code,Bill State,Bill Country," +
				"Client Address Line 1,Client Address Line 2,Client City,Client State,Client Postal Code,Client Country," +
				"GL Expense,GL Revenue\n" +
				appointments.stream()
						.map(appointment -> {
							var billingInfo = appointment.getBillingInfo();
							var vendor = vendors.get(appointment.getVendorId());
							var patient = patients.get(appointment.getPatientId());
							var payCode = payCodes.get(appointment.getPaymentInfo().getServiceCode());
							var billCode = billCodes.get(billingInfo.getServiceCode());
							return String.format(
									"%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
									export(appointment.getInvoiceId()),
									export(appointment.getId()),
									export(appointment.getPatientName()),
									export(FormatText.formatDate(qbExport.getCreationTime(), FormatText.USER_DATE)),
									export(FormatText.formatDate(qbExport.getCreationTime(), FormatText.USER_DATE)),
									export(appointment.getNurseName()),
									"", // Department
									export(payCode.getCode()),
									export(payCode.getDescription()),
									export(billCode.getCode()),
									export(billCode.getDescription()),
									export(billingInfo.getUnit() == Unit.Visit ? 1 : appointment.getLoggedHours()),
									export(billingInfo.getUnit() == Unit.Visit ? "visits" : "hours"),
									export(billingInfo.getUnitRate()),
									export(billingInfo.getTotal()),
									"N",
									export(FormatText.formatDate(appointment.getDate(), FormatText.USER_DATE)),
									export(FormatText.formatTime(appointment.getTimeIn())),
									export(FormatText.formatTime(appointment.getTimeOut())),
									"N/A", // Item Tax Code
									"N/A", // Tax Class %
									export(vendor.getAddress()),
									export(patient.getServiceAddress()),
									export(appointment.getPaymentInfo().getTotal()),  // GL Expense
									export(billingInfo.getTotal())); // GL Revenue
						})
						.collect(Collectors.joining("\n"));
	}

	private static String export(Long value) {
		return value == null || value == 0 ? "" : String.valueOf(value);
	}

	private static String export(Double value) {
		return value == null || value == 0 ? "" : String.valueOf(value);
	}

	private static String export(String value) {
		if (value == null) {
			return "";
		}

		value = value.trim();
		return value.contains(" ") || value.contains(",") ? "\"" + value + "\"" : value;
	}


	/**
	 * Exports an address as "stree1,street2,city,state,zipcode,country"
	 * @param address to export
	 * @return csv string of address
	 */
	private static String export(Address address) {
		if (address == null) {
			return ",,,,,";
		}

		String[] parts = address.getAddress().split(",");

		return String.format("%s,%s,%s,%s,%s,%s",
				export(parts[0]),
				export(address.getUnit() == null ? "" : address.getUnit()),
				export(parts[1]),
				export(parts[2].trim().substring(0, 2)),
				export(parts[2].trim().substring(2)),
				export(parts[3]));
	}
}
