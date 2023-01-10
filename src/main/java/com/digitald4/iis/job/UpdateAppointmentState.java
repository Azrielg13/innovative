package com.digitald4.iis.job;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.storage.AppointmentStore;

import java.time.Clock;
import java.util.List;

public class UpdateAppointmentState {
	public static void main(String[] args) {
		APIConnector apiConnector = new APIConnector("https://ip360-179401.appspot.com/_ah/api", "v1");
		DAOApiProtoImpl messageDAO = new DAOApiProtoImpl(apiConnector);
		DAORouterImpl dao = new DAORouterImpl(
				messageDAO, new DAOHasProto(messageDAO), new DAOApiImpl(apiConnector, Clock.systemUTC()));
		AppointmentStore appointmentStore =
				new AppointmentStore(() -> dao, null, null, null, null);
		// No need to change anything, calling AppointmentStore.update will update the state.
		appointmentStore.list(Query.forList())
				.getItems()
				.forEach(appointment -> appointmentStore.update(appointment.getId(), appointment1 -> appointment1));

		List<Appointment> billable = appointmentStore
				.list(
						Query.forList().setFilters(
								Filter.of("vendor_id", "=", "7"),
								Filter.of("state", ">=", "6"),
								Filter.of("state", "<=", "7")))
				.getItems();
		System.out.println("Billable: " + billable.size());

		List<Appointment> pending = appointmentStore
				.list(
						Query.forList().setFilters(
								Filter.of("vendor_id", "=", "7"),
								Filter.of("state", "=", "4")))
				.getItems();
		System.out.println("Pending: " + pending.size());
	}
}
