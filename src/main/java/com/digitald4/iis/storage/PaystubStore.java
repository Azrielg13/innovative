package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.OrderBy;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.report.PaystubReportCreator;
import com.google.protobuf.ByteString;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import javax.inject.Inject;
import javax.inject.Provider;
import org.joda.time.DateTime;

public class PaystubStore extends GenericLongStore<Paystub> {

	private final AppointmentStore appointmentStore;
	private final Store<DataFile, Long> dataFileStore;
	private final PaystubReportCreator paystubReportCreator;

	@Inject
	public PaystubStore(Provider<DAO> daoProvider,
											AppointmentStore appointmentStore,
											Store<DataFile, Long> dataFileStore,
											PaystubReportCreator paystubReportCreator) {
		super(Paystub.class, daoProvider);
		this.appointmentStore = appointmentStore;
		this.dataFileStore = dataFileStore;
		this.paystubReportCreator = paystubReportCreator;
	}

	@Override
	public Paystub create(Paystub paystub) {
		Paystub mostRecent = getMostRecent(paystub.getNurseId());
		DateTime now = DateTime.now();
		if (mostRecent == null || new DateTime(mostRecent.getGenerationTime()).getYear() != now.getYear()) {
			mostRecent = new Paystub();
		}

		paystub = super.create(paystub
				.setGenerationTime(now.getMillis())
				.setLoggedHoursYTD(mostRecent.getLoggedHoursYTD() + paystub.getLoggedHours())
				.setGrossPayYTD(mostRecent.getGrossPayYTD() + paystub.getGrossPay())
				.setMileageYTD(mostRecent.getMileageYTD() + paystub.getMileage())
				.setPayMileageYTD(mostRecent.getPayMileageYTD() + paystub.getPayMileage())
				.setPreTaxDeductionYTD(mostRecent.getPreTaxDeductionYTD() + paystub.getPreTaxDeduction())
				.setTaxableYTD(mostRecent.getTaxableYTD() + paystub.getTaxable())
				.setTaxTotalYTD(mostRecent.getTaxTotalYTD() + paystub.getTaxTotal())
				.setPostTaxDeductionYTD(mostRecent.getPostTaxDeductionYTD() + paystub.getPostTaxDeduction())
				.setNonTaxWagesYTD(mostRecent.getNonTaxWagesYTD() + paystub.getNonTaxWages())
				.setNetPayYTD(mostRecent.getNetPayYTD() + paystub.getNetPay()));

		long paystubId = paystub.getId();
		paystub.getAppointmentIds().forEach(appId ->
				appointmentStore.update(appId, appointment -> appointment.setPaystubId(paystubId)));
		try {
			ByteArrayOutputStream buffer = paystubReportCreator.createPDF(paystub);
			DataFile dataFile = dataFileStore.create(new DataFile()
					.setName("paystub-" + paystub.getId() + ".pdf")
					.setType("pdf")
					.setSize(buffer.size())
					.setData(ByteString.copyFrom(buffer.toByteArray())));

			return update(paystub.getId(), paystub1 -> paystub1.setFileReference(FileReference.of(dataFile)));
		} catch (DocumentException e) {
			throw new DD4StorageException("Error creating data file", e);
		}
	}

	/**
	 * Gets the most recent paystub for a nurse.
	 */
	private Paystub getMostRecent(long nurseId) {
		return
				list(
						Query.forList()
								.setFilters(Filter.of("nurse_id", nurseId))
								.setOrderBys(OrderBy.of("id", true))
								.setLimit(1))
				.getItems()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
