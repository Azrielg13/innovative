package com.digitald4.iis.tld;

import static org.junit.Assert.*;

import org.junit.Test;

public class NavTagTest {

	@Test
	public void testParseBody() {
		NavTag mt = new NavTag();
		mt.setSelected("Undo");
		mt.parseBody("File:File - New:New,Print:Print Page,Close:Close App\n"
	            +"View:View - Small:Small Font,Large:Large Font\n"
	            +"Edit:Edit Menu - Undo:Undo last action, Redo:Redo Last Action, Copy:Copy");
		assertEquals(3,mt.getTopNavItems().size());
		System.out.print(mt.getOutput());
	}

}
