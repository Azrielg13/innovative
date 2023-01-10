package com.digitald4.iis.storage;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.Store;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Appointment.AppointmentState;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;

import java.time.Clock;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
import javax.inject.Inject;
import javax.inject.Provider;
import org.joda.time.DateTime;

public class AppointmentStore extends GenericLongStore<Appointment> {
	private final Store<Patient, Long> patientStore;
	private final NurseStore nurseStore;
	private final Store<Vendor, Long> vendorStore;
	private final Clock clock;

	@Inject
	public AppointmentStore(Provider<DAO> daoProvider, Store<Patient, Long> patientStore, NurseStore nurseStore,
													Store<Vendor, Long> vendorStore, Clock clock) {
			super(Appointment.class, daoProvider);
			this.patientStore = patientStore;
			this.nurseStore = nurseStore;
			this.vendorStore = vendorStore;
			this.clock = clock;
	}

	@Override
	public Appointment create(Appointment appointment) {
		return super.create(updateStatus(updateNames(appointment)));
	}

	@Override
	public Appointment update(Long id, final UnaryOperator<Appointment> updater) {
		return super.update(id, original -> {
			Appointment appointment = updateStatus(updateNames(updater.apply(original)));
			if (appointment.getPaymentInfo() != null) {
				appointment = updatePaymentInfo(appointment, original);
			}
			if (appointment.getBillingInfo() != null) {
				appointment = updateBillingInfo(appointment, original);
			}
			return appointment;
		});
	}

	private Appointment updateNames(Appointment appointment) {
		Patient patient = appointment.getPatientId() < 1 ? null : patientStore.get(appointment.getPatientId());
		Nurse nurse = appointment.getNurseId() < 1 ? null : nurseStore.get(appointment.getNurseId());
		if (appointment.getVendorId() == 0 && patient != null && patient.getBillingVendorId() > 0) {
			appointment.setVendorId(patient.getBillingVendorId());
		}
		Vendor vendor = appointment.getVendorId() < 1 ? null : vendorStore.get(appointment.getVendorId());

		return appointment
				.setPatientName(patient == null ? null : patient.getName())
				.setNurseName(nurse == null ? null : nurse.fullName())
				.setVendorName(vendor == null ? null : vendor.getName());
	}

	private Appointment updatePaymentInfo(Appointment appointment, Appointment original) {
		AccountingInfo paymentInfo = appointment.getPaymentInfo();
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

	private Appointment updateBillingInfo(Appointment appointment, Appointment original) {
		AccountingInfo billingInfo = appointment.getBillingInfo();
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

	private Appointment updateStatus(Appointment appointment) {
		if (appointment.getTimeIn() != null && appointment.getTimeOut() != null) {
			long minsDiff = TimeUnit.MILLISECONDS.toMinutes(
					appointment.getTimeOut().getMillis() - appointment.getTimeIn().getMillis());
			minsDiff = Math.round(minsDiff / 15.0) * 15;
			double hours = minsDiff / 60.0;
			if (hours < 0) {
				hours += 24;
			}
			appointment.setLoggedHours(hours);
		}

		if (appointment.getState() == AppointmentState.CANCELLED) {
			return appointment;
		}
		if (appointment.isCancelled()) {
			appointment.setState(AppointmentState.CANCELLED);
		} else if (appointment.getInvoiceId() != null && appointment.getPaystubId() != null) {
			appointment.setState(AppointmentState.CLOSED);
		} else if (appointment.isAssessmentApproved()) {
			if (appointment.getInvoiceId() == null && appointment.getPaystubId() == null) {
				appointment.setState(AppointmentState.BILLABLE_AND_PAYABLE);
			} else if (appointment.getPaystubId() == null) {
				appointment.setState(AppointmentState.PAYABLE);
			} else {
				appointment.setState(AppointmentState.BILLABLE);
			}
		} else if (appointment.isAssessmentComplete()) {
			appointment.setState(AppointmentState.PENDING_APPROVAL);
		} else if (appointment.getStart().isBefore(new DateTime(clock.millis()))) {
			appointment.setState(AppointmentState.PENDING_ASSESSMENT);
		} else if (appointment.getNurseConfirmTs() != null) {
			appointment.setState(AppointmentState.CONFIRMED);
		} else {
			appointment.setState(AppointmentState.UNCONFIRMED);
		}

		return appointment;
	}
}
