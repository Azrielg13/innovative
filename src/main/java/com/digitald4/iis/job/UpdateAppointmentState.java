package com.digitald4.iis.job;

import com.digitald4.common.proto.DD4Protos.Query;
import com.digitald4.common.proto.DD4Protos.Query.Filter;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.DAOSQLImpl;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.storage.AppointmentStore;
import java.util.List;

public class UpdateAppointmentState {
	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnectorThreadPoolImpl(
				"org.gjt.mm.mysql.Driver",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"dd4_user", "getSchooled85");
		final AppointmentStore store =
				new AppointmentStore(() -> new DAOSQLImpl(dbConnector), null, null);
		// No need to change anything, calling AppointmentStore.update will update the state.
		store.list(Query.getDefaultInstance())
				.getResults()
				.forEach(appointment -> store.update(appointment.getId(), appointment1 -> appointment1));

		List<Appointment> billable = store
				.list(Query.newBuilder()
						.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperator("=").setValue("7"))
						.addFilter(Filter.newBuilder().setColumn("state").setOperator(">=").setValue("6"))
						.addFilter(Filter.newBuilder().setColumn("state").setOperator("<=").setValue("7"))
						.build())
				.getResults();
		System.out.println("Billable: " + billable.size());

		List<Appointment> pending = store
				.list(Query.newBuilder()
						.addFilter(Filter.newBuilder().setColumn("vendor_id").setOperator("=").setValue("7"))
						.addFilter(Filter.newBuilder().setColumn("state").setOperator("=").setValue("4"))
						.build())
				.getResults();
		System.out.println("Pending: " + pending.size());

		SingleProtoService<Appointment> service = new SingleProtoService<>(store);
		List<Appointment> pendingUI = service
				.list(ListRequest.newBuilder()
						.setFilter("vendor_id = 7, state = 4")
						.build())
				.getResults();
		System.out.println("PendingUI: " + pendingUI.size());
	}
}
