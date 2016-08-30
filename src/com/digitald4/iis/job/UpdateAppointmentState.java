package com.digitald4.iis.job;

import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.distributed.Function;
import com.digitald4.common.distributed.MultiCoreThreader;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest.QueryParam;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISUIProtos.AppointmentUI;
import com.digitald4.iis.store.AppointmentStore;

import java.util.List;

/**
 * Created by eddiemay on 8/6/16.
 */
public class UpdateAppointmentState {
	private final static MultiCoreThreader threader = new MultiCoreThreader();
	public static void main(String[] args) throws Exception {
		DBConnector dbConnector = new DBConnectorThreadPoolImpl(
				"org.gjt.mm.mysql.Driver",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"dd4_user", "getSchooled85");
		final AppointmentStore store = new AppointmentStore(
				new DAOProtoSQLImpl<>(Appointment.class, dbConnector));
		threader.parDo(store.getAll(), new Function<Appointment, Appointment>() {
			@Override
			public Appointment execute(Appointment appointment) {
				try {
					return store.update(appointment.getId(), new Function<Appointment, Appointment>() {
						@Override
						public Appointment execute(Appointment appointment) {
							// No need to change anything, calling AppointmentStore.update will update the state.
							return appointment;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});

		List<Appointment> billable = store.get(QueryParam.newBuilder().setColumn("billing_vendor_id").setOperan("=").setValue("7").build(),
				QueryParam.newBuilder().setColumn("state").setOperan(">=").setValue("6").build(),
				QueryParam.newBuilder().setColumn("state").setOperan("<=").setValue("7").build());
		System.out.println("Billable: " + billable.size());

		List<Appointment> pending = store.get(QueryParam.newBuilder().setColumn("billing_vendor_id").setOperan("=").setValue("7").build(),
				QueryParam.newBuilder().setColumn("state").setOperan("=").setValue("4").build());
		System.out.println("Pending: " + pending.size());

		DualProtoService<AppointmentUI, Appointment> service = new DualProtoService<>(AppointmentUI.class, store);
		List<AppointmentUI> pendingUI =  service.list(ListRequest.newBuilder()
				.addQueryParam(QueryParam.newBuilder().setColumn("billing_vendor_id").setOperan("=").setValue("7"))
				.addQueryParam(QueryParam.newBuilder().setColumn("state").setOperan("=").setValue("4"))
				.build());
		System.out.println("PendingUI: " + pendingUI.size());
	}
}
