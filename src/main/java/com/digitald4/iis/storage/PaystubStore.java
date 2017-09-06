package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4Protos.DataFile;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.Filter;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Paystub;
import com.digitald4.iis.proto.IISUIProtos;
import com.digitald4.iis.report.PaystubReportCreator;
import com.google.protobuf.ByteString;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayOutputStream;
import org.joda.time.DateTime;

public class PaystubStore extends GenericStore<Paystub> {

	private final DAO<Paystub> dao;
	private final Store<Appointment> appointmentStore;
	private final Store<DataFile> dataFileStore;
	private final PaystubReportCreator paystubReportCreator;
	public PaystubStore(DAO<Paystub> dao,
											Store<Appointment> appointmentStore,
											Store<DataFile> dataFileStore,
											PaystubReportCreator paystubReportCreator) {
		super(dao);
		this.dao = dao;
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.paystubReportCreator = paystubReportCreator;
	}

	@Override
	public Paystub create(Paystub paystub_) throws DD4StorageException {
		Paystub mostRecent = getMostRecent(paystub_.getNurseId());
		DateTime now = DateTime.now();
		if (mostRecent == null || new DateTime(mostRecent.getGenerationTime()).getYear() != now.getYear()) {
			mostRecent = Paystub.getDefaultInstance();
		}
		Paystub paystub = super.create(paystub_.toBuilder()
				.setGenerationTime(now.getMillis())
				.setLoggedHoursYTD(mostRecent.getLoggedHoursYTD() + paystub_.getLoggedHours())
				.setGrossPayYTD(mostRecent.getGrossPayYTD() + paystub_.getGrossPay())
				.setMileageYTD(mostRecent.getMileageYTD() + paystub_.getMileage())
				.setPayMileageYTD(mostRecent.getPayMileageYTD() + paystub_.getPayMileage())
				.setPreTaxDeductionYTD(mostRecent.getPreTaxDeductionYTD() + paystub_.getPreTaxDeduction())
				.setTaxableYTD(mostRecent.getTaxableYTD() + paystub_.getTaxable())
				.setTaxTotalYTD(mostRecent.getTaxTotalYTD() + paystub_.getTaxTotal())
				.setPostTaxDeductionYTD(mostRecent.getPostTaxDeductionYTD() + paystub_.getPostTaxDeduction())
				.setNonTaxWagesYTD(mostRecent.getNonTaxWagesYTD() + paystub_.getNonTaxWages())
				.setNetPayYTD(mostRecent.getNetPayYTD() + paystub_.getNetPay())
				.build());
		paystub.getAppointmentIdList().forEach(appId ->
				appointmentStore.update(appId, appointment -> appointment.toBuilder().setPaystubId(paystub.getId()).build()));
		try {
			ByteArrayOutputStream buffer = paystubReportCreator.createPDF(paystub);
			DataFile dataFile = dataFileStore.create(DataFile.newBuilder()
					.setName("paystub-" + paystub.getId() + ".pdf")
					.setType("pdf")
					.setSize(buffer.size())
					.setData(ByteString.copyFrom(buffer.toByteArray()))
					.build());
			return dao.update(paystub.getId(), paystub1 -> paystub1.toBuilder()
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
	 * Gets the most recent paystub for a nurse.
	 */
	private Paystub getMostRecent(long nurseId) {
		Paystub mostRecent = null;
		for (Paystub paystub : dao.list(ListRequest.newBuilder()
				.addFilter(Filter.newBuilder().setColumn("nurse_id").setValue(String.valueOf(nurseId)))
				.build()).getResultList()) {
			if (mostRecent == null || paystub.getId() > mostRecent.getId()) {
				mostRecent = paystub;
			}
		}
		return mostRecent;
	}
}
