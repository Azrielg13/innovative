package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Transaction.Op;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Invoice.Status;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AtomicDouble;
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
		AtomicDouble standardBilling = new AtomicDouble(),
				mileage = new AtomicDouble(),
				billedMileage = new AtomicDouble(),
				totalDue = new AtomicDouble();
		stream(appointments)
				.peek(appointment -> invoice.setLoggedHours(invoice.getLoggedHours() + appointment.getLoggedHours()))
				.map(Appointment::getBillingInfo)
				.forEach(billingInfo -> {
					standardBilling.addAndGet(billingInfo.subTotal());
					mileage.addAndGet(billingInfo.getMileage() == null ? 0 : billingInfo.getMileage());
					billedMileage.addAndGet(billingInfo.mileageTotal());
					totalDue.addAndGet(billingInfo.total());
				});

		invoice.setStandardBilling(standardBilling.get())
				.setMileage(mileage.get())
				.setBilledMileage(billedMileage.get())
				.setTotalDue(totalDue.get());

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
	protected Iterable<Op<Invoice>> preprocess(Iterable<Op<Invoice>> ops) {
		CachedReader cachedReader = new CachedReader(daoProvider.get());
		stream(ops)
				.map(Op::getEntity)
				.map(inv -> inv.setVendorName(cachedReader.get(Vendor.class, inv.getVendorId()).getName()))
				.forEach(inv -> {
					if (inv.getStatus() == Status.Cancelled) {
						return;
					} else if (inv.getTotalPaid() == 0) {
						inv.setStatus(Status.Unpaid);
					} else if (inv.getTotalPaid() < inv.getTotalDue()) {
						inv.setStatus(Status.Partially_Paid);
					} else {
						inv.setStatus(Status.Paid);
					}
				});

		return ops;
	}
}
