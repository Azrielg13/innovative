package com.digitald4.iis.tld;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.digitald4.common.component.Column;
import com.digitald4.common.component.Navigation;
import com.digitald4.common.component.SubNavItem;
import com.digitald4.common.component.TopNavItem;
import com.digitald4.iis.model.GenData;
import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.tld.InputTag;
import com.digitald4.common.tld.MidCalTag;
import com.digitald4.common.tld.NavTag;
import com.digitald4.common.tld.TableTag;
import com.digitald4.iis.dao.PatientDAO;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Patient;

public class TagTests extends DD4TestCase {
	
	@Test
	public void testNavTag() {
		NavTag nt = new NavTag();
		nt.setSelected("Undo");
		ArrayList<TopNavItem> navItems = new ArrayList<TopNavItem>();
		navItems.add(new TopNavItem("File", "File")
			.addSubItem(new SubNavItem("New", "New"))
			.addSubItem(new SubNavItem("Print", "Print Page"))
			.addSubItem(new SubNavItem("Close", "Close App")));
		navItems.add(new TopNavItem("View", "View")
			.addSubItem(new SubNavItem("Small", "Small Font"))
			.addSubItem(new SubNavItem("Large", "Large Font")));
		navItems.add(new TopNavItem("Edit", "Edit Menu")
			.addSubItem(new SubNavItem("Undo", "Undo Last Action"))
			.addSubItem(new SubNavItem("Redo", "Redo Last Action"))
			.addSubItem(new SubNavItem("Copy", "Copy")));
		nt.setNavigation(new Navigation(navItems));
		assertEquals(3, nt.getTopNavItems().size());
		System.out.print(nt.getOutput());
	}
	
	@Test
	public void testTableTag() throws Exception {
		assertTrue(Patient.getPatientsByState(GenData.PATIENT_ACTIVE.get()) != null);
		assertTrue(Appointment.getPending() != null);
		TableTag tt = new TableTag();
		tt.setTitle("Test Table");
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Name", "Name", String.class, true));
		columns.add(new Column("Source", "Referral_Source", String.class, false));
		columns.add(new Column("Dianosis", "Dianosis", String.class, false));
		tt.setColumns(columns);
		tt.setData(Patient.getPatientsByState(GenData.PATIENT_PENDING.get()));
		String out = tt.getOutput();
		System.out.print(out);
		assertTrue(out.contains("Test Table"));
		//CMC East
		tt = new TableTag();
		tt.setTitle("Test Table");
		columns = new ArrayList<Column>();
		columns.add(new Column("Name", "Link", String.class, true));
		columns.add(new Column("Source", "REFERRAL_SOURCE", String.class, false));
		columns.add(new Column("RX", "RX", String.class, true));
		columns.add(new Column("Nurse", "DIANOSIS", String.class, false));
		columns.add(new Column("Last Appointment", "Referral_Date", String.class, false));
		columns.add(new Column("Next Appointment", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
		tt.setColumns(columns);
		tt.setData(Patient.getPatientsByState(GenData.PATIENT_ACTIVE.get()));
		out = tt.getOutput();
		System.out.print(out);
		assertTrue(out.contains("Test Table"));
	}
	
	@Test
	public void testEditTag() throws Exception {
		Patient patient = new Patient();
		patient.setName("Larry");
		InputTag tt = new InputTag();
		tt.setType(InputTag.Type.TEXT);
		tt.setObject(patient);
		tt.setProp("name");
		tt.setLabel("name");
		String out = tt.getOutput();
		System.out.print(out);
		assertTrue(out.contains("name"));
		assertTrue(out.contains("name=\"Patient.name\""));
		
		tt.setType(InputTag.Type.COMBO);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.DIANOSIS_ID);
		tt.setLabel("Dianosis:");
		tt.setOptions(GenData.DIANOSIS.get().getGeneralDatas());
		out = tt.getOutput();
		System.out.println(out);
		assertTrue(out.contains("Dianosis"));
		assertTrue(out.toLowerCase().contains("name=\"patient.dianosis_id\""));
		
		tt.setType(InputTag.Type.CHECK);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.LABS);
		tt.setLabel("Labs:");
		out = tt.getOutput();
		System.out.println(out);
		
		tt.setType(InputTag.Type.RADIO);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.I_V_ACCESS_ID);
		tt.setLabel("IV Access:");
		tt.setOptions(GenData.IV_ACCESS.get().getGeneralDatas());
		assertEquals(GenData.IV_ACCESS.get().getGeneralDatas().size(), tt.getOptions().size());
		out = tt.getOutput();
		System.out.println(out);
		assertTrue(out.contains("IV Access"));
		assertTrue(out.toLowerCase().contains("name=\"patient.i_v_access_id\""));
	}
	
	@Test
	public void testAssTabs() throws Exception {
		Appointment app = new Appointment().setPatient(Patient.getInstance(7));
		AssTabs at = new AssTabs();
		at.setTitle("Test Ass Tabs");
		at.setAppointment(app);
		String out = at.getOutput();
		System.out.println(out);
	}
	
	@Test
	public void testMidCalTag() {
		MidCalTag cal = new MidCalTag();
		cal.setTitle("Patient Calendar");
		cal.setYear(2013);
		cal.setMonth(2);
		String out = cal.getOutput();
		System.out.println(out);
	}
}
