package com.digitald4.iis.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitald4.common.server.ProtoService;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.server.UpdateRequest;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.test.TestCase;
import com.google.protobuf.FieldMask;
import com.google.protobuf.util.JsonFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import javax.inject.Provider;
import org.junit.Test;
import org.mockito.Mock;

public class AppointmentServiceTest extends TestCase {
	@Mock private DAO dao = mock(DAO.class);
	private Provider<DAO> daoProvider = () -> dao;

	@Test
	public void testMapToJSON() throws Exception {
		Map<Long, String> map = new HashMap<>();
		map.put(927L, "102");
		map.put(292L, "Hello there");
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
		assertEquals("102", appointment.getAssessmentMap().get(927L));
		assertEquals("Hello there", appointment.getAssessmentMap().get(292L));
	}

	@Test
	public void testUpdateAssessment() {
		Appointment appointment = Appointment.newBuilder()
				.setId(72L)
				.setNurseName("Karen Lee")
				.setPatientId(45L)
				.setPatientName("George Man")
				.putAssessment(995L, "Good")
				.putAssessment(927L, "98.6F")
				.putAssessment(845L, "Hello")
				.build();
		when(dao.get(Appointment.class, 72L)).thenReturn(appointment);
		when(dao.update(eq(Appointment.class), eq(72L), any(UnaryOperator.class)))
				.then((i) -> i.getArgumentAt(2, UnaryOperator.class).apply(appointment));

		ProtoService<Appointment> service = new SingleProtoService<>(new GenericStore<>(Appointment.class, daoProvider));

		Appointment result = service.update(
				72L,
				new UpdateRequest<>(
						Appointment.newBuilder()
								.putAllAssessment(appointment.getAssessmentMap())
								.putAssessment(927L, "102")
								.build(),
						FieldMask.newBuilder().addPaths("assessment").build()));
		assertEquals("102", result.getAssessmentMap().get(927L));
		assertEquals("George Man", result.getPatientName());
		assertEquals("Good", result.getAssessmentMap().get(995L));
		assertEquals("Hello", result.getAssessmentMap().get(845L));
	}
}
