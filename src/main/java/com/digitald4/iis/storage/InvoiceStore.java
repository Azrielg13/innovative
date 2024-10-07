package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Invoice.Status;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

public class InvoiceStore extends GenericLongStore<Invoice> {
	private final Provider<DAO> daoProvider;
	private final AppointmentStore appointmentStore;
	private final Store<DataFile, String> dataFileStore;
	private final InvoiceReportCreator invoiceReportCreator;
	private final SequenceStore sequenceStore;
	private final Clock clock;

	@Inject
	public InvoiceStore(
			Provider<DAO> daoProvider,
			AppointmentStore appointmentStore,
			Store<DataFile, String> dataFileStore,
			InvoiceReportCreator invoiceReportCreator, SequenceStore sequenceStore,
			Clock clock) {
		super(Invoice.class, daoProvider);
		this.daoProvider = daoProvider;
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.invoiceReportCreator = invoiceReportCreator;
		this.sequenceStore = sequenceStore;
		this.clock = clock;
	}

	@Override
	public ImmutableList<Invoice> create(Iterable<Invoice> entities) {
		return stream(entities).map(this::create).collect(toImmutableList());
	}

	@Override
	public Invoice create(Invoice invoice) throws DD4StorageException {
		var listResult = appointmentStore.get(invoice.getAppointmentIds());
		if (!listResult.getMissingIds().isEmpty()) {
			throw new DD4StorageException(
					String.format("One of more appointments do not exist. Missing: %s", listResult.getMissingIds()),
					ErrorCode.BAD_REQUEST);
		}

		return create(invoice, listResult.getItems());
	}

	public Invoice create(Invoice invoice, Iterable<Appointment> appointments) throws DD4StorageException {
		Appointment first = appointments.iterator().next();

		if (invoice.getId() == null) {
			invoice.setId(sequenceStore.getAndIncrement(Invoice.class));
		}

		if (invoice.getCreationTime() == null) {
			invoice.setCreationTime(clock.instant());
		}

		appointments.forEach(appointment -> {
			if (appointment.getVendorId() == null) {
				throw new DD4StorageException("Vendor id null, all appointments must have a vendor id", ErrorCode.BAD_REQUEST);
			} else if (!Objects.equals(appointment.getVendorId(), first.getVendorId())) {
				throw new DD4StorageException("Vendor id missmatch, all appointments must be for the same vendor", ErrorCode.BAD_REQUEST);
			} else if (appointment.getBillingInfo() == null) {
				throw new DD4StorageException("Billing info missing for one more appointments.", ErrorCode.BAD_REQUEST);
			} else if (appointment.getInvoiceId() != null && !Objects.equals(appointment.getInvoiceId(), invoice.getId())) {
				throw new DD4StorageException("One of more appointments already assigned to an invoice", ErrorCode.BAD_REQUEST);
			}
		});

		invoice.setVendorId(first.getVendorId())
				.setAppointmentIds(stream(appointments).map(Appointment::getId).collect(toImmutableSet()));
		stream(appointments)
				.peek(appointment -> invoice.setLoggedHours(invoice.getLoggedHours() + appointment.getLoggedHours()))
				.map(Appointment::getBillingInfo)
				.forEach(billingInfo -> invoice
						.setStandardBilling(invoice.getStandardBilling() + billingInfo.subTotal())
						.setMileage(invoice.getMileage() + (billingInfo.getMileage() == null ? 0 : billingInfo.getMileage()))
						.setBilledMileage(invoice.getBilledMileage() + billingInfo.mileageTotal())
						.setTotalDue(invoice.getTotalDue() + billingInfo.total()));

		ByteArrayOutputStream buffer = invoiceReportCreator.createPDF(invoice);
		DataFile dataFile = dataFileStore.create(
				new DataFile().setName("invoice-" + invoice.getId() + ".pdf").setType("pdf").setData(buffer.toByteArray()));
		invoice.setFileReference(FileReference.of(dataFile));
		// Update the invoiceId in memory and in the database.
		appointments.forEach(appointment -> appointment.setInvoiceId(invoice.getId()));
		invoice.getAppointmentIds().forEach(
				appId -> appointmentStore.update(appId, app -> app.setInvoiceId(invoice.getId())));
		return super.create(invoice);
	}

	@Override
	protected Iterable<Invoice> preprocess(Iterable<Invoice> entities, boolean isCreate) {
		DAO dao = daoProvider.get();
		return super.preprocess(
				stream(entities)
						.map(inv -> inv.setVendorName(dao.get(Vendor.class, inv.getVendorId()).getName()))
						.map(inv -> {
							if (inv.getStatus() == Status.Cancelled) {
								return inv;
							} else if (inv.getTotalPaid() == 0) {
								return inv.setStatus(Status.Unpaid);
							} else if (inv.getTotalPaid() < inv.getTotalDue()) {
								return inv.setStatus(Status.Partially_Paid);
							} else {
								return inv.setStatus(Status.Paid);
							}
						})
						.collect(toImmutableList()),
				isCreate);
	}
}
