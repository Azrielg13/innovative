package com.digitald4.budget.model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;

public class GenDataTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/budget?autoReconnect=true", "eddiemay", "");
		User.setActiveUser(User.getInstance(1));
	}

	@Test
	public void test() {
		GeneralData accountCat = GeneralData.getInstance(80);
		GeneralData gd = GenData.AccountCategory.get();
		assertSame(accountCat, gd);
	}
}
