package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.OrderBy;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.google.protobuf.ByteString;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import javax.inject.Inject;
import javax.inject.Provider;
import org.joda.time.DateTime;

public class InvoiceStore extends GenericStore<Invoice> {

	private final Provider<DAO> daoProvider;
	private final AppointmentStore appointmentStore;
	private final Store<DataFile> dataFileStore;
	private final InvoiceReportCreator invoiceReportCreator;

	@Inject
	public InvoiceStore(Provider<DAO> daoProvider,
											AppointmentStore appointmentStore,
											Store<DataFile> dataFileStore,
											InvoiceReportCreator invoiceReportCreator) {
		super(Invoice.class, daoProvider);
		this.daoProvider = daoProvider;
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
		invoice.getAppointmentIds().forEach(appId ->
				appointmentStore.update(appId, appointment -> appointment.setInvoiceId(invoiceId)));

		try {
			ByteArrayOutputStream buffer = invoiceReportCreator.createPDF(invoice);
			DataFile dataFile = dataFileStore.create(new DataFile()
					.setName("invoice-" + invoice.getId() + ".pdf")
					.setType("pdf")
					.setSize(buffer.size())
					.setData(ByteString.copyFrom(buffer.toByteArray())));

			return daoProvider.get().update(
					Invoice.class, invoice.getId(), invoice1 -> invoice1.setFileReference(FileReference.of(dataFile)));
		} catch (DocumentException e) {
			throw new DD4StorageException("Error creating data file", e);
		}
	}

	/**
	 * Gets the most recent invoice for a nurse.
	 */
	private Invoice getMostRecent(long vendorId) {
		return daoProvider.get()
				.list(Invoice.class, new Query()
						.setFilters(new Filter().setColumn("vendor_id").setValue(String.valueOf(vendorId)))
						.setOrderBys(new OrderBy().setColumn("id").setDesc(true))
						.setLimit(1))
				.getResults()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
