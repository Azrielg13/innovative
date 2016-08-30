package com.digitald4.iis.store;

import com.digitald4.common.storage.DAO;
import com.digitald4.common.distributed.Function;
import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.AppointmentState;

import org.joda.time.DateTime;

/**
 * Created by eddiemay on 8/4/16.
 */
public class AppointmentStore extends GenericDAOStore<Appointment> {
    public AppointmentStore(DAO<Appointment> dao) {
        super(dao);
    }

    @Override
    public Appointment update(int id, final Function<Appointment, Appointment> updater)
				throws DD4StorageException {
    	return super.update(id, new Function<Appointment, Appointment>() {
            @Override
            public Appointment execute(Appointment appointment) {
				appointment = updater.execute(appointment);
				if (appointment.getState() == AppointmentState.AS_CANCELLED) {
					return appointment;
				}
				Appointment.Builder builder = appointment.toBuilder();
				if (appointment.getCancelled()) {
					builder.setState(AppointmentState.AS_CANCELLED);
				} else if (appointment.hasInvoiceId() && appointment.hasPaystubId()) {
					builder.setState(AppointmentState.AS_CLOSED);
				} else if (appointment.getAssessmentApproved()) {
					if (!appointment.hasInvoiceId() && !appointment.hasPaystubId()) {
						builder.setState(AppointmentState.AS_BILLABLE_AND_PAYABLE);
					} else if (!appointment.hasPaystubId()) {
						builder.setState(AppointmentState.AS_PAYABLE);
					} else {
						builder.setState(AppointmentState.AS_BILLABLE);
					}
				} else if (appointment.getAssessmentComplete()) {
					builder.setState(AppointmentState.AS_PENDING_APPROVAL);
				} else if (appointment.getStart() < DateTime.now().getMillis()) {
					builder.setState(AppointmentState.AS_PENDING_ASSESSMENT);
				} else if (appointment.hasNurseConfirmTs()) {
					builder.setState(AppointmentState.AS_CONFIRMED);
				} else {
					builder.setState(AppointmentState.AS_UNCONFIRMED);
				}
				return builder.build();
					}
			});
    }
}
