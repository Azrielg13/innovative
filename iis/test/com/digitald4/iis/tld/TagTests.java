package com.digitald4.iis.tld;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.digitald4.common.component.Column;
import com.digitald4.common.component.Navigation;
import com.digitald4.common.component.SubNavItem;
import com.digitald4.common.component.TopNavItem;
import com.digitald4.common.model.GenData;
import com.digitald4.common.test.DD4TestCase;
import com.digitald4.common.tld.InputTag;
import com.digitald4.common.tld.NavTag;
import com.digitald4.common.tld.TableTag;
import com.digitald4.iis.dao.PatientDAO;
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
	public void testTableTag() {
		TableTag tt = new TableTag();
		tt.setTitle("Test Table");
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Name", "Name", String.class, true));
		columns.add(new Column("Source", "Referral_Source", String.class, false));
		columns.add(new Column("Dianosis", "Dianosis", String.class, false));
		tt.setColumns(columns);
		tt.setData(Patient.getAll());
		String out = tt.getOutput();
		System.out.print(out);
		assertTrue(out.contains("Test Table"));
	}
	
	@Test
	public void testEditTag() throws Exception {
		Patient patient = new Patient();
		patient.setReferralSource("Wells Fargo");
		InputTag tt = new InputTag();
		tt.setType(InputTag.Type.TEXT);
		tt.setObject(patient);
		tt.setProp("referral_source");
		tt.setLabel("Referral Source");
		String out = tt.getOutput();
		System.out.print(out);
		assertTrue(out.contains("Referral Source"));
		assertTrue(out.contains("name=\"Patient.referral_source\""));
		
		tt.setType(InputTag.Type.COMBO);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.DIANOSIS);
		tt.setLabel("Dianosis:");
		tt.setOptions(GenData.UserType.getInstance().getGeneralDatas());
		out = tt.getOutput();
		System.out.println(out);
		assertTrue(out.contains("Dianosis"));
		assertTrue(out.toLowerCase().contains("name=\"patient.dianosis\""));
		
		tt.setType(InputTag.Type.CHECK);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.LABS);
		tt.setLabel("Labs:");
		out = tt.getOutput();
		System.out.println(out);
		
		tt.setType(InputTag.Type.RADIO);
		tt.setObject(patient);
		tt.setProp(""+PatientDAO.PROPERTY.DIANOSIS);
		tt.setLabel("Dianosis:");
		tt.setOptions(GenData.UserType.getInstance().getGeneralDatas());
		assertEquals(2, tt.getOptions().size());
		out = tt.getOutput();
		System.out.println(out);
		assertTrue(out.contains("Dianosis"));
		assertTrue(out.toLowerCase().contains("name=\"patient.dianosis\""));
	}
}
