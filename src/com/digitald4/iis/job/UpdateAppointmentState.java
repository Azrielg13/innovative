package com.digitald4.iis.job;

import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.Filter;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.storage.AppointmentStore;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateAppointmentState {
	public static void main(String[] args) throws Exception {
		DBConnector dbConnector = new DBConnectorThreadPoolImpl(
				"org.gjt.mm.mysql.Driver",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"dd4_user", "getSchooled85");
		final AppointmentStore store =
				new AppointmentStore(new DAOProtoSQLImpl<>(Appointment.class, dbConnector), null, null);
		// No need to change anything, calling AppointmentStore.update will update the state.
		store.list(ListRequest.getDefaultInstance()).getResultList()
				.forEach(appointment -> store.update(appointment.getId(), appointment1 -> appointment1));

		List<Appointment> billable = store.list(ListRequest.newBuilder()
				.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperan("=").setValue("7"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperan(">=").setValue("6"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperan("<=").setValue("7"))
				.build()).getResultList();
		System.out.println("Billable: " + billable.size());

		List<Appointment> pending = store.list(ListRequest.newBuilder()
				.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperan("=").setValue("7"))
				.addFilter(Filter.newBuilder().setColumn("state").setOperan("=").setValue("4"))
				.build()).getResultList();
		System.out.println("Pending: " + pending.size());

		SingleProtoService<Appointment> service = new SingleProtoService<>(store);
		List<Appointment> pendingUI = service
				.list(ListRequest.newBuilder()
						.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperan("=").setValue("7"))
						.addFilter(Filter.newBuilder().setColumn("state").setOperan("=").setValue("4"))
						.build())
				.getResultList()
				.stream()
				.map(any -> any.unpack(Appointment.class))
				.collect(Collectors.toList());
		System.out.println("PendingUI: " + pendingUI.size());
	}
}
