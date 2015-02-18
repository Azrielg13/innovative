package com.digitald4.budget.model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.User;

public class PortfolioTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/budget?autoReconnect=true", "eddiemay", "");
		User.setActiveUser(User.getInstance(1));
	}
	
	@Test
	public void testInsert() throws Exception {
		Portfolio portfolio = (Portfolio) new Portfolio().setName("test")
				.addUserPortfolio(new UserPortfolio().setUser(User.getActiveUser()).setRole(GenData.UserPortfolioRole_OWNER.get()))
				.save();
		assertNotNull(portfolio.getId());
	}
}
