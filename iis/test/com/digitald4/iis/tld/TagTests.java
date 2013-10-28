package com.digitald4.iis.tld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.junit.Test;

import com.digitald4.common.component.Column;
import com.digitald4.common.component.NavItem;
import com.digitald4.common.component.Navigation;
import com.digitald4.common.component.Notification;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.tld.BreadCrumbTag;
import com.digitald4.common.tld.InputTag;
import com.digitald4.common.tld.LargeCalTag;
import com.digitald4.common.tld.MedCalTag;
import com.digitald4.common.tld.NavTag;
import com.digitald4.common.tld.TableTag;
import com.digitald4.common.util.Calculate;
import com.digitald4.iis.dao.PatientDAO;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Patient;

public class TagTests extends DD4TestCase {
	
	@Test
	public void testNavTag() throws Exception {
		NavTag nt = new NavTag();
		nt.setSelected("Delete");
		ArrayList<NavItem> navItems = new ArrayList<NavItem>();
		navItems.add(new NavItem("File", "File")
			.addSubItem(new NavItem("New", "New"))
			.addSubItem(new NavItem("Print Page", "Print"))
			.addSubItem(new NavItem("Close App", "Close").addSubItem(new NavItem("Delete", "Delete"))));
		navItems.add(new NavItem("View", "View")
			.addSubItem(new NavItem("Small Font", "Small"))
			.addSubItem(new NavItem("Large Font", "Large")));
		navItems.add(new NavItem("Edit Menu", "Edit")
			.addSubItem(new NavItem("Undo Last Action", "Undo"))
			.addSubItem(new NavItem("Redo Last Action", "Redo"))
			.addSubItem(new NavItem("Copy", "Copy")));
		nt.setNavigation(new Navigation(navItems));
		assertEquals(3, nt.getTopNavItems().size());
		System.out.print(nt.getOutputIndented());
	}
	
	@Test
	public void testBreadCrumbTag() throws Exception {
		BreadCrumbTag nt = new BreadCrumbTag();
		nt.setSelected("Delete");
		ArrayList<NavItem> navItems = new ArrayList<NavItem>();
		navItems.add(new NavItem("File", "File")
			.addSubItem(new NavItem("New", "New"))
			.addSubItem(new NavItem("Print Page", "Print"))
			.addSubItem(new NavItem("Close App", "Close").addSubItem(new NavItem("Delete", "Delete"))));
		navItems.add(new NavItem("View", "View")
			.addSubItem(new NavItem("Small Font", "Small"))
			.addSubItem(new NavItem("Large Font", "Large")));
		navItems.add(new NavItem("Edit Menu", "Edit")
			.addSubItem(new NavItem("Undo Last Action", "Undo"))
			.addSubItem(new NavItem("Redo Last Action", "Redo"))
			.addSubItem(new NavItem("Copy", "Copy")));
		nt.setNavigation(new Navigation(navItems));
		assertEquals(3, nt.getTopNavItems().size());
		System.out.print(nt.getOutputIndented());
	}
	
	@Test
	public void testTableTag() throws Exception {
		assertTrue(Patient.getByState(GenData.PATIENT_ACTIVE.get()) != null);
		assertTrue(Appointment.getPending() != null);
		TableTag<Patient> tt = new TableTag<Patient>();
		tt.setTitle("Test Table");
		Collection<Column<Patient>> columns = new ArrayList<Column<Patient>>();
		columns.add(new Column<Patient>("Name", "Name", String.class, true));
		columns.add(new Column<Patient>("Source", "Referral_Source", String.class, false));
		columns.add(new Column<Patient>("Dianosis", "Dianosis", String.class, false));
		tt.setColumns(columns);
		tt.setData(Patient.getByState(GenData.PATIENT_PENDING.get()));
		String out = tt.getOutputIndented();
		System.out.print(out);
		assertTrue(out.contains("Test Table"));
		
		tt = new TableTag<Patient>();
		tt.setTitle("Test Table");
		columns = new ArrayList<Column<Patient>>();
		columns.add(new Column<Patient>("Name", "Link", String.class, false));
		columns.add(new Column<Patient>("Source", "REFERRAL_SOURCE", String.class, false));
		columns.add(new Column<Patient>("RX", "RX", String.class, true));
		columns.add(new Column<Patient>("Nurse", "DIANOSIS", String.class, false));
		columns.add(new Column<Patient>("Last Appointment", "Referral_Date", String.class, false));
		columns.add(new Column<Patient>("Next Appointment", ""+Patient.PROPERTY.START_OF_CARE_DATE, String.class, false));
		tt.setColumns(columns);
		tt.setData(Patient.getByState(GenData.PATIENT_ACTIVE.get()));
		out = tt.getOutputIndented();
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
		
		patient.setStartOfCareDate(Calculate.getCal(2013, 07, 22).getTime());
		tt.setType(InputTag.Type.DATE);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.START_OF_CARE_DATE);
		tt.setLabel("Start of Care");
		out = tt.getOutputIndented();
		System.out.println(out);
		assertTrue(out.contains("Start of Care"));
		assertTrue(out.toLowerCase().contains("name=\"patient.start_of_care_date\""));
	}
	
	@Test
	public void testAssTabs() throws Exception {
		Appointment app = new Appointment().setPatient(Patient.getInstance(7));
		app.setAssessmentEntry(GeneralData.getInstance(56), "57");
		AssTabs at = new AssTabs();
		at.setTitle("Test Ass Tabs");
		at.setAppointment(app);
		String out = at.getOutputIndented();
		System.out.println(out);
	}
	
	@Test
	public void testMedCalTag() throws Exception {
		MedCalTag cal = new MedCalTag();
		cal.setTitle("Patient Calendar");
		cal.setYear(2013);
		cal.setMonth(2);
		cal.setNewAppIds("pid=69");
		String out = cal.getOutputIndented();
		System.out.println(out);
	}
	
	@Test
	public void testLargeCalTag() throws Exception {
		TreeSet<Appointment> events = new TreeSet<Appointment>();
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 2, 28, 19, 30, 0))).setDuration(60));
		
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 1, 19, 30, 0))).setDuration(60));
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 1, 1, 30, 0))).setDuration(60));
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 1, 12, 30, 0))).setDuration(60));
		
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 2, 19, 30, 0))).setDuration(60));
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 2, 18, 30, 0))).setDuration(60));
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 2, 2, 30, 0))).setDuration(60));
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 2, 12, 30, 0))).setDuration(60));
		events.add(new Appointment().setStart(new DateTime(Calculate.getCal(2013, 3, 2, 19, 31, 0))).setDuration(60));
		
		List<Notification<?>> notifications = new ArrayList<Notification<?>>();
		notifications.add(new Notification<Object>("Payment Due", DateTime.parse("2013-02-25").toDate(), Notification.Type.ERROR, null));
		notifications.add(new Notification<Object>("Payment Reminder", DateTime.parse("2013-02-10").toDate(), Notification.Type.WARNING, null));
		notifications.add(new Notification<Object>("Statement Ready", DateTime.parse("2013-02-01").toDate(), Notification.Type.INDIFFERENT, null));
		
		LargeCalTag cal = new LargeCalTag();
		cal.setTitle("Patient Calendar");
		cal.setYear(2013);
		cal.setMonth(2);
		cal.setEvents(events);
		cal.setNotifications(notifications);
		String out = cal.getOutputIndented();
		System.out.println(out);
	}
}
