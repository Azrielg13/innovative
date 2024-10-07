package com.digitald4.iis.storage;

import static com.digitald4.iis.model.Vendor.InvoicingModel.Funder_Batched;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.Address;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.*;
import com.digitald4.iis.model.Appointment.AppointmentState;
import com.digitald4.iis.model.ServiceCode.Unit;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.time.Clock;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;

public class QuickBooksExportStore extends GenericStore<QuickBooksExport, String> {
	private enum EntryType {Billing, Mileage};
	private final Provider<DAO> daoProvider;
	private final AppointmentStore appointmentStore;
	private final InvoiceStore invoiceStore;
	private final Store<DataFile, String> dataFileStore;
	private final SequenceStore seqStore;
	private final InvoiceReportCreator invoiceReportCreator;
	private final Clock clock;

	@Inject
	public QuickBooksExportStore(Provider<DAO> daoProvider, AppointmentStore appointmentStore, InvoiceStore invoiceStore,
		 	Store<DataFile, String> dataFileStore, SequenceStore seqStore,
			InvoiceReportCreator invoiceReportCreator, Clock clock) {
		super(QuickBooksExport.class, daoProvider);
		this.daoProvider = daoProvider;
		this.appointmentStore = appointmentStore;
		this.invoiceStore = invoiceStore;
		this.dataFileStore = dataFileStore;
		this.seqStore = seqStore;
		this.invoiceReportCreator = invoiceReportCreator;
		this.clock = clock;
	}

	@Override
	public ImmutableList<QuickBooksExport> create(Iterable<QuickBooksExport> entities) {
		return stream(entities).map(this::create).collect(toImmutableList());
	}

	@Override
	public QuickBooksExport create(QuickBooksExport qbExport) {
		var listResult = appointmentStore.get(qbExport.getAppointmentIds());
		if (!listResult.getMissingIds().isEmpty()) {
			throw new DD4StorageException(
					String.format("One of more appointments do not exist. Missing: %s", listResult.getMissingIds()),
					ErrorCode.BAD_REQUEST);
		}

		ImmutableList<Appointment> appointments = listResult.getItems().stream()
				.sorted(comparing(Appointment::getVendorName).thenComparing(Appointment::getPatientName)
						.thenComparing(Appointment::getDate).thenComparing(Appointment::getTimeIn))
				.collect(toImmutableList());
		if (appointments.stream().anyMatch(app -> app.getExportId() != null && !Objects.equals(app.getExportId(), qbExport.getId()))) {
			throw new DD4StorageException(
					"One of more appointments already assigned to an export ", ErrorCode.BAD_REQUEST);
		}

		if (qbExport.getBillingDate() == null) {
			qbExport.setBillingDate(clock.instant());
		}

		if (Strings.isNullOrEmpty(qbExport.getId())) {
			qbExport.setId(String.format(
					"IP360_%s_QUICKBOOKS_INV", FormatText.formatDate(qbExport.getBillingDate(), FormatText.BUILD_DATETIME)));
		}
		String exportId = qbExport.getId();

		try {
			String export = export(qbExport, appointments);
			DataFile dataFile = new DataFile()
					.setName(String.format("%s.csv", exportId))
					.setType("csv")
					.setSize(export.length())
					.setData(export.getBytes());

			byte[] invoicesPdf = invoiceReportCreator.createPDF(appointments.stream().map(Appointment::getInvoiceId)
					.distinct().map(invoiceStore::get).collect(toImmutableSet())).toByteArray();

			DataFile invoiceFile = new DataFile()
					.setName(String.format("%s.pdf", exportId))
					.setType("pdf")
					.setSize(invoicesPdf.length)
					.setData(invoicesPdf);

			qbExport.setFileReference(FileReference.of(dataFile));
			qbExport.setInvoiceFileReference(FileReference.of(invoiceFile));

			super.create(qbExport);
			dataFileStore.create(dataFile);
			dataFileStore.create(invoiceFile);
			qbExport.getAppointmentIds().forEach(appId -> appointmentStore.update(appId, app -> app.setExportId(exportId)));

			return qbExport;
		} catch (NullPointerException npe) {
			throw new DD4StorageException(
					"Error trying to export to Quickbooks: " + npe.getMessage(), npe, ErrorCode.BAD_REQUEST);
		}
	}

	private String export(QuickBooksExport qbExport, ImmutableList<Appointment> appointments) {
		DAO dao = daoProvider.get();
		ImmutableMap<Long, Vendor> vendors = dao.get(Vendor.class, appointments.stream().map(Appointment::getVendorId)
				.collect(toImmutableList())).getItems().stream().collect(toImmutableMap(Vendor::getId, identity()));
		HashSet<Long> existingInvoices = new HashSet<>();
		// We need to create an invoice for each appointment.
		appointments.stream()
				.filter(app -> {
					if (app.getInvoiceId() != null) {
						existingInvoices.add(app.getInvoiceId());
						return false;
					}
					return true;
				})
				.collect(groupingBy(Appointment::getVendorId)).forEach((vendorId, apps) -> {
					// If the Vendor supports all patients on one invoice.
					if (vendors.get(vendorId).getInvoicingModel() == Funder_Batched) {
						invoiceStore.create(
								new Invoice().setId(seqStore.getAndIncrement(Invoice.class)).setDate(qbExport.getBillingDate()), apps);
						return;
					}
					// Else we need to create by patient.
					apps.stream().collect(groupingBy(Appointment::getPatientId)).values().forEach(as ->
							invoiceStore.create(
									new Invoice().setId(seqStore.getAndIncrement(Invoice.class)).setDate(qbExport.getBillingDate()), as));
				});

		// Recreate the existing invoices.
		invoiceStore.create(invoiceStore.get(existingInvoices).getItems());

		ImmutableMap<Long, Patient> patients = dao.get(Patient.class, appointments.stream().map(Appointment::getPatientId)
				.collect(toImmutableList())).getItems().stream().collect(toImmutableMap(Patient::getId, identity()));
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
				"GL Expense,GL Revenue" +
				",Client,From Zipcode,To Zipcode\n" +
				appointments.stream()
						.flatMap(appointment -> appointment.getBillingInfo().mileageTotal() == 0
								? Stream.of(Pair.of(appointment, EntryType.Billing))
								: Stream.of(Pair.of(appointment, EntryType.Billing), Pair.of(appointment, EntryType.Mileage)))
						.map(pair -> {
							var appointment = pair.getLeft();
							var entryType = pair.getRight();
							var billingInfo = appointment.getBillingInfo();
							var vendor = vendors.get(appointment.getVendorId());
							var patient = patients.get(appointment.getPatientId());
							var paymentInfo = appointment.getPaymentInfo();
							var payCode = payCodes.get(paymentInfo.getServiceCode());
							var billCode = billCodes.get(billingInfo.getServiceCode());
							return String.format(
									"%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
									export(appointment.getInvoiceId()),
									export(appointment.getId()),
									export(vendor.getName()),
									export(FormatText.formatDate(qbExport.getBillingDate(), FormatText.USER_DATE)),
									export(FormatText.formatDate(qbExport.getBillingDate(), FormatText.USER_DATE)),
									export(appointment.getNurseName()),
									"", // Department
									export(entryType == EntryType.Billing ? payCode.getCode() : "Mileage"),
									export(entryType == EntryType.Billing ? payCode.getDescription() : "Mileage"),
									export(entryType == EntryType.Billing ? billCode.getCode() : "Mileage"),
									export(entryType == EntryType.Billing ? billCode.getDescription() : "Mileage"),
									export(entryType == EntryType.Billing ? billingInfo.getUnitCount() : billingInfo.getMileage()),
									entryType == EntryType.Billing ? (billingInfo.getUnit() == Unit.Visit ? "visits" : "hours") : "miles",
									export(entryType == EntryType.Billing ? billingInfo.getUnitRate() : billingInfo.getMileageRate()),
									export(entryType == EntryType.Billing ? billingInfo.subTotal() : billingInfo.mileageTotal()),
									"N",
									export(FormatText.formatDate(appointment.getDate(), FormatText.USER_DATE)),
									export(FormatText.formatTime(appointment.getTimeIn())),
									export(FormatText.formatTime(appointment.getTimeOut())),
									"N/A", // Item Tax Code
									"N/A", // Tax Class %
									export(vendor.getAddress()),
									export(patient.getServiceAddress()),
									export(entryType == EntryType.Billing ? paymentInfo.subTotal() : paymentInfo.mileageTotal()),  // GL Expense
									export(entryType == EntryType.Billing ? billingInfo.subTotal() : billingInfo.mileageTotal()), // GL Revenue
									export(appointment.getPatientName()),
									export(appointment.getFromZipCode()),
									export(appointment.getToZipCode()));
						})
						.collect(joining("\n"));
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
	 * Exports an address as "street1,street2,city,state,zipcode,country"
	 * @param address to export
	 * @return csv string of address
	 */
	private static String export(Address address) {
		if (address == null || address.getAddress() == null) {
			return ",,,,,";
		}

		String[] parts = address.getAddress().split(",");
		if (parts.length < 4) {
			return ",,,,,";
		}

		return String.format("%s,%s,%s,%s,%s,%s",
				export(parts[0]),
				export(address.getUnit() == null ? "" : address.getUnit()),
				export(parts[1]),
				export(parts[2].trim().substring(0, 2)),
				export(parts[2].trim().substring(2)),
				export(parts[3]));
	}

	@Override
	public boolean delete(String id) {
		QuickBooksExport qbExport = get(id);

		// Get the list of invoices that were created.
		var invoices = invoiceStore.get(appointmentStore.get(qbExport.getAppointmentIds()).getItems().stream()
				.map(Appointment::getInvoiceId).filter(Objects::nonNull).collect(toImmutableSet())).getItems();

		// Delete all the files that were created.
		dataFileStore.delete(Stream
				.concat(
						Stream.of(qbExport.getFileReference(), qbExport.getInvoiceFileReference()),
						invoices.stream().map(Invoice::getFileReference))
				.filter(Objects::nonNull)
				.map(FileReference::getName)
				.collect(toImmutableList()));

		// Delete the invoices
		invoiceStore.delete(invoices.stream().map(Invoice::getId).collect(toImmutableList()));

		// Clear the references of the appointments and reset the state.
		appointmentStore.update(qbExport.getAppointmentIds(),
				app -> app.setExportId(null).setInvoiceId(null).setState(AppointmentState.BILLABLE_AND_PAYABLE));

		return super.delete(id);
	}
}
