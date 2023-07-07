package com.digitald4.iis.storage;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.DataFile;
import com.digitald4.common.model.FileReference;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.common.storage.Query.OrderBy;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.report.PaystubReportCreator;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import javax.inject.Inject;
import javax.inject.Provider;

public class PaystubStore extends GenericLongStore<Paystub> {

	private final AppointmentStore appointmentStore;
	private final NurseStore nurseStore;
	private final Store<DataFile, String> dataFileStore;
	private final PaystubReportCreator paystubReportCreator;
	private final Clock clock;

	@Inject
	public PaystubStore(Provider<DAO> daoProvider,  AppointmentStore appointmentStore,
			NurseStore nurseStore, Store<DataFile, String> dataFileStore,
			PaystubReportCreator paystubReportCreator, Clock clock) {
		super(Paystub.class, daoProvider);
		this.appointmentStore = appointmentStore;
		this.nurseStore = nurseStore;
		this.dataFileStore = dataFileStore;
		this.paystubReportCreator = paystubReportCreator;
		this.clock = clock;
	}

	@Override
	public Paystub create(Paystub paystub) {
		if (appointmentStore.get(paystub.getAppointmentIds()).stream().anyMatch(app -> app.getInvoiceId() != null)) {
			throw new DD4StorageException(
					"One of more appointments already assigned to a paystub.", ErrorCode.BAD_REQUEST);
		}

		Paystub mostRecent = getMostRecent(paystub.getNurseId());
		ZoneId z = ZoneId.of("America/Los_Angeles");
		Instant now = Instant.now(clock);
		if (mostRecent == null || mostRecent.getGenerationTime().atZone(z).getYear() != now.atZone(z).getYear()) {
			mostRecent = new Paystub();
		}

		paystub = super.create(paystub
				.setGenerationTime(now.toEpochMilli())
				.setNurseName(nurseStore.get(paystub.getNurseId()).fullName())
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
		try {
			ByteArrayOutputStream buffer = paystubReportCreator.createPDF(paystub);
			DataFile dataFile = dataFileStore.create(
					new DataFile()
							.setName("paystub-" + paystub.getId() + ".pdf")
							.setType("pdf")
							.setSize(buffer.size())
							.setData(buffer.toByteArray()));

			paystub = update(paystub.getId(), ps -> ps.setFileReference(FileReference.of(dataFile)));
		} catch (DocumentException e) {
			throw new DD4StorageException("Error creating data file", e);
		}

		long paystubId = paystub.getId();
		paystub.getAppointmentIds().forEach(appId ->
				appointmentStore.update(appId, appointment -> appointment.setPaystubId(paystubId)));

		return paystub;
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
