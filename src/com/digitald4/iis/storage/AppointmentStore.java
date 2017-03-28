package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.DAOStore;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Appointment.AccountingInfo;
import com.digitald4.iis.proto.IISProtos.Appointment.Builder;
import com.digitald4.iis.proto.IISProtos.Appointment.AppointmentState;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISProtos.Vendor;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import java.util.function.UnaryOperator;

public class AppointmentStore extends GenericDAOStore<Appointment> {
	private final DAOStore<Nurse> nurseStore;
	private final DAOStore<Vendor> vendorStore;
	public AppointmentStore(DAO<Appointment> dao, DAOStore<Nurse> nurseStore, DAOStore<Vendor> vendorStore) {
			super(dao);
			this.nurseStore = nurseStore;
			this.vendorStore = vendorStore;
	}

	@Override
	public Appointment update(int id, final UnaryOperator<Appointment> updater)
			throws DD4StorageException {
		return super.update(id, original -> {
			Builder appointment = statusUpdater.apply(updater.apply(original).toBuilder());
			updateLoggedHours(appointment);
			if (appointment.getMileage() != original.getMileage()) {
				appointment
						.setPaymentInfo(appointment.getPaymentInfoBuilder()
								.setMileage(appointment.getMileage()))
						.setBillingInfo(appointment.getBillingInfoBuilder()
								.setMileage(appointment.getMileage()));
			}
			if (appointment.hasPaymentInfo()) {
				updatePaymentInfo(appointment, original);
			}
			if (appointment.hasBillingInfo()) {
				updateBillingInfo(appointment, original);
			}
			return appointment.build();
		});
	}

	private Builder updateLoggedHours(Builder appointment) {
		long minsDiff = TimeUnit.MILLISECONDS.toMinutes(appointment.getTimeOut() - appointment.getTimeIn());
		minsDiff = Math.round(minsDiff / 15.0) * 15;
		double hours = minsDiff / 60.0;
		if (hours < 0) {
			hours += 24;
		}
		return appointment.setLoggedHours(hours);
	}
	
	private Builder updatePaymentInfo(Builder appointment, Appointment original) {
		AccountingInfo.Builder paymentInfo = appointment.getPaymentInfoBuilder();
		// If the payment type has been changed we need to fill in payment amounts.
		if (paymentInfo.getAccountingTypeId() != original.getPaymentInfo().getAccountingTypeId()) {
			Nurse nurse = nurseStore.get(appointment.getNurseId());
			switch(paymentInfo.getAccountingTypeId()) {
				case GenData.ACCOUNTING_TYPE_FIXED:
					paymentInfo.setFlatRate(nurse.getPayFlat());
					paymentInfo.setHourlyRate(0);
					paymentInfo.setHours(appointment.getLoggedHours());
					break;
				case GenData.ACCOUNTING_TYPE_HOURLY:
					paymentInfo.setFlatRate(0);
					paymentInfo.setHourlyRate(nurse.getPayRate());
					paymentInfo.setHours(appointment.getLoggedHours());
					break;
				case GenData.ACCOUNTING_TYPE_ROC2_HR:
					paymentInfo.setFlatRate(nurse.getPayFlat2HrRoc());
					paymentInfo.setHourlyRate(nurse.getPayRate2HrRoc());
					paymentInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
					break;
				case GenData.ACCOUNTING_TYPE_SOC2_HR:
					paymentInfo.setFlatRate(nurse.getPayFlat2HrSoc());
					paymentInfo.setHourlyRate(nurse.getPayRate2HrSoc());
					paymentInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
					break;
			}
		}
		if (!paymentInfo.hasMileage()) {
			paymentInfo.setMileage(appointment.getMileage());
		}
		if (paymentInfo.getMileage() != original.getPaymentInfo().getMileage()) {
			Nurse nurse = nurseStore.get(appointment.getNurseId());
			paymentInfo.setMileageRate(nurse.getMileageRate());
		}
		return appointment.setPaymentInfo(paymentInfo
				.setSubTotal(paymentInfo.getFlatRate() + paymentInfo.getHours() * paymentInfo.getHourlyRate())
				.setMileageTotal(paymentInfo.getMileage() * paymentInfo.getMileageRate())
				.setTotal(paymentInfo.getSubTotal() + paymentInfo.getMileageTotal()));
	}

	private Builder updateBillingInfo(Builder appointment, Appointment original) {
		AccountingInfo.Builder billingInfo = appointment.getBillingInfoBuilder();
		// If the payment type has been changed we need to fill in payment amounts.
		if (billingInfo.getAccountingTypeId() != original.getBillingInfo().getAccountingTypeId()) {
			Vendor vendor = vendorStore.get(appointment.getVendorId());
			switch(billingInfo.getAccountingTypeId()) {
				case GenData.ACCOUNTING_TYPE_FIXED:
					billingInfo.setFlatRate(vendor.getBillingFlat());
					billingInfo.setHourlyRate(0);
					billingInfo.setHours(appointment.getLoggedHours());
					break;
				case GenData.ACCOUNTING_TYPE_HOURLY:
					billingInfo.setFlatRate(0);
					billingInfo.setHourlyRate(vendor.getBillingRate());
					billingInfo.setHours(appointment.getLoggedHours());
					break;
				case GenData.ACCOUNTING_TYPE_ROC2_HR:
					billingInfo.setFlatRate(vendor.getBillingFlat2HrRoc());
					billingInfo.setHourlyRate(vendor.getBillingRate2HrRoc());
					billingInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
					break;
				case GenData.ACCOUNTING_TYPE_SOC2_HR:
					billingInfo.setFlatRate(vendor.getBillingFlat2HrSoc());
					billingInfo.setHourlyRate(vendor.getBillingRate2HrSoc());
					billingInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
					break;
			}
		}
		if (billingInfo.getMileage() != original.getBillingInfo().getMileage()) {
			Vendor vendor = vendorStore.get(appointment.getVendorId());
			billingInfo.setMileageRate(vendor.getMileageRate());
		}
		return appointment.setBillingInfo(
				billingInfo.setTotal(billingInfo.getFlatRate()
						+ billingInfo.getHours() * billingInfo.getHourlyRate()
						+ billingInfo.getMileage() + billingInfo.getMileageRate()));
	}

	private static final UnaryOperator<Builder> statusUpdater = appointment -> {
		if (appointment.getState() == AppointmentState.AS_CANCELLED) {
			return appointment;
		}
		if (appointment.getCancelled()) {
			appointment.setState(AppointmentState.AS_CANCELLED);
		} else if (appointment.hasInvoiceId() && appointment.hasPaystubId()) {
			appointment.setState(AppointmentState.AS_CLOSED);
		} else if (appointment.getAssessmentApproved()) {
			if (!appointment.hasInvoiceId() && !appointment.hasPaystubId()) {
				appointment.setState(AppointmentState.AS_BILLABLE_AND_PAYABLE);
			} else if (!appointment.hasPaystubId()) {
				appointment.setState(AppointmentState.AS_PAYABLE);
			} else {
				appointment.setState(AppointmentState.AS_BILLABLE);
			}
		} else if (appointment.getAssessmentComplete()) {
			appointment.setState(AppointmentState.AS_PENDING_APPROVAL);
		} else if (appointment.getStart() < DateTime.now().getMillis()) {
			appointment.setState(AppointmentState.AS_PENDING_ASSESSMENT);
		} else if (appointment.hasNurseConfirmTs()) {
			appointment.setState(AppointmentState.AS_CONFIRMED);
		} else {
			appointment.setState(AppointmentState.AS_UNCONFIRMED);
		}
		return appointment;
	};
}
