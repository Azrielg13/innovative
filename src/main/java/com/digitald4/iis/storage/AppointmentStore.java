package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.util.Provider;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Appointment.AccountingInfo;
import com.digitald4.iis.proto.IISProtos.Appointment.Builder;
import com.digitald4.iis.proto.IISProtos.Appointment.AppointmentState;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISProtos.Vendor;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import java.util.function.UnaryOperator;

public class AppointmentStore extends GenericStore<Appointment> {
	private final Store<Nurse> nurseStore;
	private final Store<Vendor> vendorStore;
	public AppointmentStore(Provider<DAO> daoProvider, Store<Nurse> nurseStore, Store<Vendor> vendorStore) {
			super(Appointment.class, daoProvider);
			this.nurseStore = nurseStore;
			this.vendorStore = vendorStore;
	}

	@Override
	public Appointment create(Appointment appointment) {
		return super.create(statusUpdater.apply(appointment.toBuilder()).build());
	}

	@Override
	public Appointment update(long id, final UnaryOperator<Appointment> updater)
			throws DD4StorageException {
		return super.update(id, original -> {
			Builder appointment = statusUpdater.apply(updater.apply(original).toBuilder());
			if (appointment.hasPaymentInfo()) {
				appointment = updatePaymentInfo(appointment, original);
			}
			if (appointment.hasBillingInfo()) {
				appointment = updateBillingInfo(appointment, original);
			}
			return appointment.build();
		});
	}

	private Builder updatePaymentInfo(Builder appointment, Appointment original) {
		AccountingInfo.Builder paymentInfo = appointment.getPaymentInfoBuilder();
		// If the payment type has been changed we need to fill in payment amounts.
		if (paymentInfo.getAccountingTypeId() != original.getPaymentInfo().getAccountingTypeId()
				|| appointment.getLoggedHours() != original.getLoggedHours()
				|| appointment.getNurseId() != original.getNurseId()) {
			Nurse nurse = nurseStore.get(appointment.getNurseId());
			if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_FIXED) {
				paymentInfo.setFlatRate(nurse.getPayFlat());
				paymentInfo.setHourlyRate(0);
				paymentInfo.setHours(appointment.getLoggedHours());
			} else if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_HOURLY) {
				paymentInfo.setFlatRate(0);
				paymentInfo.setHourlyRate(nurse.getPayRate());
				paymentInfo.setHours(appointment.getLoggedHours());
			} else if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_ROC2_HR) {
				paymentInfo.setFlatRate(nurse.getPayFlat2HrRoc());
				paymentInfo.setHourlyRate(nurse.getPayRate2HrRoc());
				paymentInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
			} else if (paymentInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_SOC2_HR) {
				paymentInfo.setFlatRate(nurse.getPayFlat2HrSoc());
				paymentInfo.setHourlyRate(nurse.getPayRate2HrSoc());
				paymentInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
			}
		}
		if (paymentInfo.getMileage() != 0 || appointment.getMileage() != original.getMileage()) {
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
		if (billingInfo.getAccountingTypeId() != original.getBillingInfo().getAccountingTypeId()
				|| appointment.getLoggedHours() != original.getLoggedHours()
				|| appointment.getVendorId() != original.getVendorId()) {
			Vendor vendor = vendorStore.get(appointment.getVendorId());
			if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_FIXED) {
				billingInfo.setFlatRate(vendor.getBillingFlat());
				billingInfo.setHourlyRate(0);
				billingInfo.setHours(appointment.getLoggedHours());
			} else if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_HOURLY) {
				billingInfo.setFlatRate(0);
				billingInfo.setHourlyRate(vendor.getBillingRate());
				billingInfo.setHours(appointment.getLoggedHours());
			} else if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_ROC2_HR) {
				billingInfo.setFlatRate(vendor.getBillingFlat2HrRoc());
				billingInfo.setHourlyRate(vendor.getBillingRate2HrRoc());
				billingInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
			} else if (billingInfo.getAccountingTypeId() == GenData.ACCOUNTING_TYPE_SOC2_HR) {
				billingInfo.setFlatRate(vendor.getBillingFlat2HrSoc());
				billingInfo.setHourlyRate(vendor.getBillingRate2HrSoc());
				billingInfo.setHours(appointment.getLoggedHours() > 2 ? appointment.getLoggedHours() - 2 : 0);
			}
		}
		if (billingInfo.getMileage() != 0 || appointment.getMileage() != original.getMileage()) {
			billingInfo.setMileage(appointment.getMileage());
		}
		if (billingInfo.getMileage() != original.getBillingInfo().getMileage()) {
			Vendor vendor = vendorStore.get(appointment.getVendorId());
			billingInfo.setMileageRate(vendor.getMileageRate());
		}
		return appointment.setBillingInfo(billingInfo
				.setSubTotal(billingInfo.getFlatRate() + billingInfo.getHours() * billingInfo.getHourlyRate())
				.setMileageTotal(billingInfo.getMileage() * billingInfo.getMileageRate())
				.setTotal(billingInfo.getSubTotal() + billingInfo.getMileageTotal()));
	}

	private static final UnaryOperator<Builder> statusUpdater = appointment -> {
		if (appointment.getTimeIn() != 0 && appointment.getTimeOut() != 0) {
			long minsDiff = TimeUnit.MILLISECONDS.toMinutes(appointment.getTimeOut() - appointment.getTimeIn());
			minsDiff = Math.round(minsDiff / 15.0) * 15;
			double hours = minsDiff / 60.0;
			if (hours < 0) {
				hours += 24;
			}
			appointment.setLoggedHours(hours);
		}

		if (appointment.getState() == AppointmentState.AS_CANCELLED) {
			return appointment;
		}
		if (appointment.getCancelled()) {
			appointment.setState(AppointmentState.AS_CANCELLED);
		} else if (appointment.getInvoiceId() != 0 && appointment.getPaystubId() != 0) {
			appointment.setState(AppointmentState.AS_CLOSED);
		} else if (appointment.getAssessmentApproved()) {
			if (appointment.getInvoiceId() == 0 && appointment.getPaystubId() == 0) {
				appointment.setState(AppointmentState.AS_BILLABLE_AND_PAYABLE);
			} else if (appointment.getPaystubId() == 0) {
				appointment.setState(AppointmentState.AS_PAYABLE);
			} else {
				appointment.setState(AppointmentState.AS_BILLABLE);
			}
		} else if (appointment.getAssessmentComplete()) {
			appointment.setState(AppointmentState.AS_PENDING_APPROVAL);
		} else if (appointment.getStart() < DateTime.now().getMillis()) {
			appointment.setState(AppointmentState.AS_PENDING_ASSESSMENT);
		} else if (appointment.getNurseConfirmTs() != 0) {
			appointment.setState(AppointmentState.AS_CONFIRMED);
		} else {
			appointment.setState(AppointmentState.AS_UNCONFIRMED);
		}
		return appointment;
	};
}
