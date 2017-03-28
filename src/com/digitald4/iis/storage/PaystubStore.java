package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4Protos.DataFile;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.QueryParam;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOStore;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Paystub;
import com.digitald4.iis.proto.IISUIProtos;
import com.digitald4.iis.report.PaystubReportCreator;
import com.google.protobuf.ByteString;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayOutputStream;
import org.joda.time.DateTime;

public class PaystubStore extends GenericDAOStore<Paystub> {

	private final DAO<Paystub> dao;
	private final DAOStore<Appointment> appointmentStore;
	private final DAOStore<DataFile> dataFileStore;
	private final PaystubReportCreator paystubReportCreator;
	public PaystubStore(DAO<Paystub> dao,
											DAOStore<Appointment> appointmentStore,
											DAOStore<DataFile> dataFileStore,
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
	private Paystub getMostRecent(int nurseId) {
		Paystub mostRecent = null;
		for (Paystub paystub : dao.get(
				QueryParam.newBuilder().setColumn("nurse_id").setOperan("=").setValue(Integer.toString(nurseId)).build())) {
			if (mostRecent == null || paystub.getId() > mostRecent.getId()) {
				mostRecent = paystub;
			}
		}
		return mostRecent;
	}
}
