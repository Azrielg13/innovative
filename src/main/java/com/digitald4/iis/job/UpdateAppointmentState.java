package com.digitald4.iis.job;

import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.*;
import com.digitald4.common.storage.Query.Filter;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.ServiceCode;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.ServiceCodeStore;
import com.google.common.collect.ImmutableList;

public class UpdateAppointmentState {
	public static void main(String[] args) {
		APIConnector apiConnector = new APIConnector("https://ip360-179401.appspot.com/_ah/api", "v1");
		DAO dao = new DAOApiImpl(apiConnector);
		AppointmentStore appointmentStore = new AppointmentStore(() -> dao, new ServiceCodeStore(() -> dao), null);
		// No need to change anything, calling AppointmentStore.update will update the state.
		appointmentStore.list(Query.forList())
				.getItems()
				.forEach(appointment -> appointmentStore.update(appointment.getId(), appointment1 -> appointment1));

		ImmutableList<Appointment> billable = appointmentStore
				.list(
						Query.forList().setFilters(
								Filter.of("vendorId", "=", "7"),
								Filter.of("state", ">=", "6"),
								Filter.of("state", "<=", "7")))
				.getItems();
		System.out.println("Billable: " + billable.size());

		ImmutableList<Appointment> pending = appointmentStore
				.list(
						Query.forList().setFilters(
								Filter.of("vendorId", "=", "7"),
								Filter.of("state", "=", "4")))
				.getItems();
		System.out.println("Pending: " + pending.size());
	}
}
