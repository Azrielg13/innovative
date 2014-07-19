package com.digitald4.common.jpa;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.digitald4.common.model.TransHist;
import com.digitald4.common.model.User;

public class DD4EntityManagerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManagerHelper.init("DD4JPA2", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		User.setActiveUser(User.getInstance(1));
	}
	
	@Test
	public void testFind() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		assertNotNull(em);
		User user = em.find(User.class, new PrimaryKey<Integer>(1));
		assertNotNull(user);
		assertEquals("Eddie", user.getFirstName());
	}
	
	@Test
	public void testFindByQuery() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		assertNotNull(em);
		TypedQuery<TransHist> tq = em.createQuery("SELECT o FROM WHERE USER_ID=?1", TransHist.class);
		List<TransHist> transactions = tq.getResultList();
		assertTrue(transactions.size() > 5);
		for (TransHist th : transactions) {
			assertSame(1, th.getUserId());
		}
	}
	
	@Test
	public void testMerge() throws Exception {
		EntityManager em = EntityManagerHelper.getEntityManager();
		assertNotNull(em);
		User user = em.find(User.class, new PrimaryKey<Integer>(1));
		user.setLastLogin(DateTime.now());
		em.merge(user);
	}
	
	@Test
	public void testRemove() throws Exception {
		EntityManager em = EntityManagerHelper.getEntityManager();
		assertNotNull(em);
		TransHist th = new TransHist().setId(42);
		em.remove(th);
	}
	
	@Test
	public void testPersist() throws Exception {
		EntityManager em = EntityManagerHelper.getEntityManager();
		assertNotNull(em);
		TransHist th = new TransHist().setUserId(1).setObject(User.class.getName()).setRowId(1635)
				.setTypeId(1) //.setType(GenData.TransType_Update.get())
				.setData("TEST").setTimestamp(DateTime.now());
		em.persist(th);
	}
	
	@Test
	public void testRefresh() throws Exception {
		EntityManager em = EntityManagerHelper.getEntityManager();
		assertNotNull(em);
		User user = em.find(User.class, new PrimaryKey<Integer>(1));
		em.refresh(user);
	}
}
