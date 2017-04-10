package com.digitald4.iis.server;

import static org.junit.Assert.assertEquals;

import com.digitald4.common.proto.DD4UIProtos.UpdateRequest;
import com.digitald4.common.server.ProtoService;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.test.TestCase;
import com.googlecode.protobuf.format.JsonFormat;
import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;

public class AppointmentServiceTest extends TestCase {
	@Test
	public void testMapToJSON() throws Exception {
		Map<Integer, String> map = new HashMap<>();
		map.put(927, "102");
		map.put(292, "Hello there");
		Appointment.Builder appointment = Appointment.newBuilder()
				.putAllAssessment(map);
		String output = JsonFormat.printToString(appointment.build());
		System.out.println(output);
		assertEquals("{\"assessment\": {292: \"Hello there\", 927: \"102\"}}", output);
	}

	@Test
	public void testMapMerge() throws Exception {
		Appointment.Builder appointment = Appointment.newBuilder();
		JsonFormat.merge("{\"assessment\":{\"927\":\"102\", \"292\":\"Hello there\"}}", appointment);
		System.out.println(JsonFormat.printToString(appointment.build()));
		assertEquals("102", appointment.getAssessment().get("927"));
		assertEquals("Hello there", appointment.getAssessment().get("292"));
	}

	@Test
	public void testMapValueReplace() throws Exception {
		Appointment.Builder builder = Appointment.newBuilder();
		JsonFormat.merge("{\"assessment\":{\"927\":\"102\",\"292\":\"Hello there\"}}", builder);
		Appointment appointment = builder.build();
		assertEquals(2, appointment.getAssessment().size());
		assertEquals("102", appointment.getAssessment().get("927"));
		assertEquals("Hello there", appointment.getAssessment().get("292"));
		String output = JsonFormat.printToString(appointment);
		System.out.println(output);
		assertEquals("{\"assessment\": {927: \"102\", 292: \"Hello there\"}}", output);

		builder = appointment.toBuilder();
		JsonFormat.merge("{\"assessment\":{\"927\":\"98.6\",\"292\":\"Goodbye.\"}}", builder);
		appointment = builder.build();
		assertEquals(2, appointment.getAssessment().size());
		assertEquals("98.6", appointment.getAssessment().get("927"));
		assertEquals("Goodbye.", appointment.getAssessment().get("292"));
		output = JsonFormat.printToString(appointment);
		System.out.println(output);
		assertEquals("{\"assessment\": {927: \"98.6\", 292: \"Goodbye.\"}}", output);
	}

	@Test
	public void testUpdateAssessment() {
		ProtoService<Appointment> service = new SingleProtoService<>(
				new GenericDAOStore<>(new DAOProtoSQLImpl<>(Appointment.class, dbConnector)));

		Appointment appointment = service.update(UpdateRequest.newBuilder()
				.setId(72)
				.setProto("{\"assessment\":{\"927\":\"102\"}}")
				.build());
		assertEquals("102", appointment.getAssessment().get("927"));
	}
}
