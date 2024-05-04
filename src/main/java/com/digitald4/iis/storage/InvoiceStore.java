package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.OrderBy;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import javax.inject.Inject;
import javax.inject.Provider;

public class InvoiceStore extends GenericLongStore<Invoice> {
	private final AppointmentStore appointmentStore;
	private final Store<DataFile, String> dataFileStore;
	private final InvoiceReportCreator invoiceReportCreator;
	private final Clock clock;

	@Inject
	public InvoiceStore(
			Provider<DAO> daoProvider,
			AppointmentStore appointmentStore,
			Store<DataFile, String> dataFileStore,
			InvoiceReportCreator invoiceReportCreator,
			Clock clock) {
		super(Invoice.class, daoProvider);
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.invoiceReportCreator = invoiceReportCreator;
		this.clock = clock;
	}

	@Override
	public Invoice create(Invoice invoice) throws DD4StorageException {
		var listResult = appointmentStore.get(invoice.getAppointmentIds());
		if (!listResult.getMissingIds().isEmpty()) {
			throw new DD4StorageException(
					String.format("One of more appointments do not exist. Missing: %s", listResult.getMissingIds()),
					ErrorCode.BAD_REQUEST);
		}
		if (listResult.getItems().stream().anyMatch(app -> app.getInvoiceId() != null)) {
			throw new DD4StorageException("One of more appointments already assigned to an invoice.", ErrorCode.BAD_REQUEST);
		}

		Invoice mostRecent = getMostRecent(invoice.getVendorId());
		ZoneId z = ZoneId.of("America/Los_Angeles");
		Instant now = Instant.now(clock);
		if (mostRecent == null || mostRecent.getGenerationTime().atZone(z).getYear() != now.atZone(z).getYear()) {
			mostRecent = new Invoice();
		}

		invoice = super.create(invoice
				.setGenerationTime(now.toEpochMilli())
				.setLoggedHoursYTD(mostRecent.getLoggedHoursYTD() + invoice.getLoggedHours())
				.setStandardBillingYTD(mostRecent.getStandardBillingYTD() + invoice.getStandardBilling())
				.setMileageYTD(mostRecent.getMileageYTD() + invoice.getMileage())
				.setBilledMileageYTD(mostRecent.getBilledMileageYTD() + invoice.getBilledMileage())
				.setBilledYTD(mostRecent.getBilledYTD() + invoice.getTotalDue()));

		long invoiceId = invoice.getId();
		try {
			ByteArrayOutputStream buffer = invoiceReportCreator.createPDF(invoice);
			DataFile dataFile = dataFileStore.create(
					new DataFile()
							.setName("invoice-" + invoice.getId() + ".pdf")
							.setType("pdf")
							.setData(buffer.toByteArray()));

			invoice = update(invoiceId, i -> i.setFileReference(FileReference.of(dataFile)));
			invoice.getAppointmentIds().forEach(
					appId -> appointmentStore.update(appId, app -> app.setInvoiceId(invoiceId)));
		} catch (DocumentException e) {
			throw new DD4StorageException("Error creating data file", e);
		}

		return invoice;
	}

	/**
	 * Gets the most recent invoice for a nurse.
	 */
	private Invoice getMostRecent(long vendorId) {
		return
				list(Query.forList()
						.setFilters(Filter.of("vendor_id", vendorId))
						.setOrderBys(OrderBy.of("id", true))
						.setLimit(1))
				.getItems()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
