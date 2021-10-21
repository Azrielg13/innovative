package com.digitald4.iis.job;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.storage.AppointmentStore;
import java.util.List;

public class UpdateAppointmentState {
	public static void main(String[] args) {
		APIConnector apiConnector = new APIConnector("https://ip360-179401.appspot.com/_ah/api", "v1");
		DAOApiProtoImpl messageDAO = new DAOApiProtoImpl(apiConnector);
		DAORouterImpl dao = new DAORouterImpl(messageDAO, new HasProtoDAO(messageDAO), new DAOApiImpl(apiConnector));
		final AppointmentStore appointmentStore = new AppointmentStore(() -> dao, null, null);
		// No need to change anything, calling AppointmentStore.update will update the state.
		appointmentStore.list(new Query())
				.getResults()
				.forEach(appointment -> appointmentStore.update(appointment.getId(), appointment1 -> appointment1));

		List<Appointment> billable = appointmentStore
				.list(new Query()
						.setFilters(
								new Filter().setColumn("vendor_id").setOperator("=").setValue("7"),
								new Filter().setColumn("state").setOperator(">=").setValue("6"),
								new Filter().setColumn("state").setOperator("<=").setValue("7")))
				.getResults();
		System.out.println("Billable: " + billable.size());

		List<Appointment> pending = appointmentStore
				.list(new Query()
						.setFilters(
								new Filter().setColumn("vendor_id").setOperator("=").setValue("7"),
								new Filter().setColumn("state").setOperator("=").setValue("4")))
				.getResults();
		System.out.println("Pending: " + pending.size());
	}
}
