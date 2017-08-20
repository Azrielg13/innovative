package com.digitald4.iis.server;

import static org.junit.Assert.assertEquals;

import com.digitald4.common.proto.DD4UIProtos.UpdateRequest;
import com.digitald4.common.server.ProtoService;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.test.TestCase;
import com.google.protobuf.Any;
import com.google.protobuf.FieldMask;
import com.google.protobuf.util.JsonFormat;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class AppointmentServiceTest extends TestCase {
	@Test
	public void testMapToJSON() throws Exception {
		Map<Integer, String> map = new HashMap<>();
		map.put(927, "102");
		map.put(292, "Hello there");
		Appointment.Builder appointment = Appointment.newBuilder()
				.putAllAssessment(map);
		String output = JsonFormat.printer().print(appointment.build());
		System.out.println(output);
		assertEquals("{\n  \"assessment\": {\n    \"292\": \"Hello there\",\n    \"927\": \"102\"\n  }\n}", output);
	}

	@Test
	public void testMapMerge() throws Exception {
		Appointment.Builder appointment = Appointment.newBuilder();
		JsonFormat.parser().merge("{\"assessment\":{\"927\":\"102\", \"292\":\"Hello there\"}}", appointment);
		System.out.println(JsonFormat.printer().print(appointment.build()));
		assertEquals("102", appointment.getAssessmentMap().get(927));
		assertEquals("Hello there", appointment.getAssessmentMap().get(292));
	}

	@Test
	public void testUpdateAssessment() {
		ProtoService<Appointment> service = new SingleProtoService<>(
				new GenericStore<>(new DAOProtoSQLImpl<>(Appointment.class, dbConnector)));

		Appointment appointment = service.update(UpdateRequest.newBuilder()
				.setId(72)
				.setProto(Any.pack(Appointment.newBuilder()
						.putAssessment(927, "102")
						.build()))
				.setUpdateMask(FieldMask.newBuilder().addPaths("assessment"))
				.build());
		assertEquals("102", appointment.getAssessmentMap().get(927));
	}
}
