package com.digitald4.iis.tld;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

public class MenuTagTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testParseBody() {
		MenuTag mt = new MenuTag();
		mt.setSelected("Undo");
		mt.parseBody("File:File - New:New,Print:Print Page,Close:Close App\n"
	            +"View:View - Small:Small Font,Large:Large Font\n"
	            +"Edit:Edit Menu - Undo:Undo last action, Redo:Redo Last Action, Copy:Copy");
		assertEquals(3,mt.getTopMenus().size());
	}

}
