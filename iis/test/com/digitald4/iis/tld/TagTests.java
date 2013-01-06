package com.digitald4.iis.tld;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.digitald4.common.component.Column;
import com.digitald4.common.component.Navigation;
import com.digitald4.common.component.SubNavItem;
import com.digitald4.common.component.TopNavItem;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.tld.ComboBoxTag;
import com.digitald4.common.tld.NavTag;
import com.digitald4.common.tld.TableTag;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Patient;

public class TagTests {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://192.168.1.19/iis?autoReconnect=true", "iis", "webpass");
	}
	
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
	public void testComboBoxTag() {
		ComboBoxTag tt = new ComboBoxTag();
		tt.setName("Test Table");
		ArrayList<Pair<String, String>> columns = new ArrayList<Pair<String, String>>();
		columns.add(new Pair<String, String>("1", "Eddie"));
		columns.add(new Pair<String, String>("2", "Shalonda"));
		columns.add(new Pair<String, String>("3", "Aniya"));
		tt.setOptions(columns);
		String out = tt.getOutput();
		System.out.print(out);
		assertTrue(out.contains("Test Table"));
	}

}
