package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4Protos.DataFile;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.QueryParam;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOStore;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Invoice;
import com.digitald4.iis.proto.IISUIProtos;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.google.protobuf.ByteString;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import org.joda.time.DateTime;

public class InvoiceStore extends GenericDAOStore<Invoice> {

	private final DAO<Invoice> dao;
	private final DAOStore<Appointment> appointmentStore;
	private final DAOStore<DataFile> dataFileStore;
	private final InvoiceReportCreator invoiceReportCreator;
	public InvoiceStore(DAO<Invoice> dao,
											DAOStore<Appointment> appointmentStore,
											DAOStore<DataFile> dataFileStore,
											InvoiceReportCreator invoiceReportCreator) {
		super(dao);
		this.dao = dao;
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.invoiceReportCreator = invoiceReportCreator;
	}

	@Override
	public Invoice create(Invoice invoice_) throws DD4StorageException {
		Invoice mostRecent = getMostRecent(invoice_.getVendorId());
		DateTime now = DateTime.now();
		if (mostRecent == null || new DateTime(mostRecent.getGenerationTime()).getYear() != now.getYear()) {
			mostRecent = Invoice.getDefaultInstance();
		}
		Invoice invoice = super.create(invoice_.toBuilder()
				.setGenerationTime(now.getMillis())
				.setLoggedHoursYTD(mostRecent.getLoggedHoursYTD() + invoice_.getLoggedHours())
				.setStandardBillingYTD(mostRecent.getStandardBillingYTD() + invoice_.getStandardBilling())
				.setMileageYTD(mostRecent.getMileageYTD() + invoice_.getMileage())
				.setBilledMileageYTD(mostRecent.getBilledMileageYTD() + invoice_.getBilledMileage())
				.setBilledYTD(mostRecent.getBilledYTD() + invoice_.getTotalDue())
				.build());
		invoice.getAppointmentIdList().forEach(appId ->
				appointmentStore.update(appId, appointment -> appointment.toBuilder().setInvoiceId(invoice.getId()).build()));
		try {
			ByteArrayOutputStream buffer = invoiceReportCreator.createPDF(invoice);
			DataFile dataFile = dataFileStore.create(DataFile.newBuilder()
					.setName("invoice-" + invoice.getId() + ".pdf")
					.setType("pdf")
					.setSize(buffer.size())
					.setData(ByteString.copyFrom(buffer.toByteArray()))
					.build());
			return dao.update(invoice.getId(), invoice1 -> invoice1.toBuilder()
					.setDataFile(IISUIProtos.DataFile.newBuilder()
							.setId(dataFile.getId())
							.setName(dataFile.getName())
							.setType(dataFile.getType())
							.setSize(dataFile.getSize()))
					.build());
		} catch (DocumentException e) {
			throw new DD4StorageException("Error creating data file", e);
		}
	}

	/**
	 * Gets the most recent invoice for a nurse.
	 */
	private Invoice getMostRecent(int nurseId) {
		Invoice mostRecent = null;
		for (Invoice invoice : dao.get(
				QueryParam.newBuilder().setColumn("vendor_id").setOperan("=").setValue(Integer.toString(nurseId)).build())) {
			if (mostRecent == null || invoice.getId() > mostRecent.getId()) {
				mostRecent = invoice;
			}
		}
		return mostRecent;
	}
}
