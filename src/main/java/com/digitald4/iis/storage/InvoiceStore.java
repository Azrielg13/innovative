package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.OrderBy;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import javax.inject.Inject;
import javax.inject.Provider;
import org.joda.time.DateTime;

public class InvoiceStore extends GenericLongStore<Invoice> {

	private final AppointmentStore appointmentStore;
	private final Store<DataFile, Long> dataFileStore;
	private final InvoiceReportCreator invoiceReportCreator;

	@Inject
	public InvoiceStore(
			Provider<DAO> daoProvider,
			AppointmentStore appointmentStore,
			Store<DataFile, Long> dataFileStore,
			InvoiceReportCreator invoiceReportCreator) {
		super(Invoice.class, daoProvider);
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.invoiceReportCreator = invoiceReportCreator;
	}

	@Override
	public Invoice create(Invoice invoice) throws DD4StorageException {
		Invoice mostRecent = getMostRecent(invoice.getVendorId());
		DateTime now = DateTime.now();
		if (mostRecent == null || new DateTime(mostRecent.getGenerationTime()).getYear() != now.getYear()) {
			mostRecent = new Invoice();
		}

		invoice = super.create(invoice
				.setGenerationTime(now.getMillis())
				.setLoggedHoursYTD(mostRecent.getLoggedHoursYTD() + invoice.getLoggedHours())
				.setStandardBillingYTD(mostRecent.getStandardBillingYTD() + invoice.getStandardBilling())
				.setMileageYTD(mostRecent.getMileageYTD() + invoice.getMileage())
				.setBilledMileageYTD(mostRecent.getBilledMileageYTD() + invoice.getBilledMileage())
				.setBilledYTD(mostRecent.getBilledYTD() + invoice.getTotalDue()));

		long invoiceId = invoice.getId();
		try {
			ByteArrayOutputStream buffer = invoiceReportCreator.createPDF(invoice);
			DataFile dataFile = dataFileStore.create(new DataFile()
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
