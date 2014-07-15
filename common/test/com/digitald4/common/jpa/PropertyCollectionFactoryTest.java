package com.digitald4.common.jpa;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class PropertyCollectionFactoryTest {

	@Test
	public void test() throws Exception {
		PropertyCollectionFactory<TestClass> pcf = new PropertyCollectionFactory<TestClass>();
		assertTrue(pcf.isEmpty());
		TestClass tc3 = new TestClass().setS("str").setX(3).setB(false);
		TestClass tc4 = new TestClass().setS("str").setX(4).setB(true);
		TestClass tc5 = new TestClass().setS("str").setX(5).setB(false);
		TestClass tc6 = new TestClass().setS("str").setX(6).setB(false);
		TestClass tc7 = new TestClass().setS("str").setX(7).setB(true);
		DD4TypedQuery<TestClass> tq = new DD4TypedQuery<TestClass>(null, "test", "SELECT o FROM TestClass WHERE o.X=?1", TestClass.class);
		tq.setParameter(1, 5);
		pcf.getList(false, tq);
		assertTrue(pcf.isEmpty());
		pcf.cache(tc5, tq);
		assertFalse(pcf.isEmpty());
		assertEquals(1, pcf.getList(false, tq).size());
		pcf.cache(tc4);
		assertEquals(1, pcf.getList(false, tq).size());
		assertEquals(5, pcf.getList(false, tq).get(0).getX());
		pcf.cache(tc6);
		assertEquals(1, pcf.getList(false, tq).size());
		assertEquals(5, pcf.getList(false, tq).get(0).getX());
		
		DD4TypedQuery<TestClass> tq2 = new DD4TypedQuery<TestClass>(null, "test", "SELECT o FROM TestClass WHERE o.B=?1", TestClass.class);
		tq2.setParameter(1, true);
		pcf.getList(true, tq2);
		DD4TypedQuery<TestClass> tq3 = new DD4TypedQuery<TestClass>(null, "test", "SELECT o FROM TestClass WHERE o.B=?1", TestClass.class);
		tq3.setParameter(1, false);
		pcf.getList(true, tq3);
		assertEquals(2, pcf.getPropertyCollections().size());
		pcf.cache(tc3);
		pcf.cache(tc4);
		pcf.cache(tc5);
		pcf.cache(tc6);
		pcf.cache(tc7);
		assertEquals(1, pcf.getList(false, tq).size());
		assertEquals(5, pcf.getList(false, tq).get(0).getX());
		assertEquals(2, pcf.getList(false, tq2).size());
		assertEquals(true, pcf.getList(false, tq2).get(0).isB());
		assertEquals(3, pcf.getList(false, tq3).size());
		assertEquals(false, pcf.getList(false, tq3).get(0).isB());
		
		DD4TypedQuery<TestClass> tq4 = new DD4TypedQuery<TestClass>(null, "test", "SELECT o FROM TestClass WHERE o.X<?1", TestClass.class);
		tq4.setParameter(1, 6);
		pcf.getList(true, tq4);
		pcf.cache(tc3);
		pcf.cache(tc4);
		pcf.cache(tc5);
		pcf.cache(tc6);
		pcf.cache(tc7);
		for (TestClass tc : pcf.getList(false, tq4)) {
			System.out.println(tc.getX());
		}
		assertEquals(3, pcf.getList(false, tq4).size());
		for (TestClass tc : pcf.getList(false, tq4)) {
			assertTrue(tc.getX() < 6);
		}
		
		DD4TypedQuery<TestClass> tq5 = new DD4TypedQuery<TestClass>(null, "test", "SELECT o FROM TestClass WHERE o.X>?1", TestClass.class);
		tq5.setParameter(1, 5);
		pcf.getList(true, tq5);
		pcf.cache(tc3);
		pcf.cache(tc4);
		pcf.cache(tc5);
		pcf.cache(tc6);
		pcf.cache(tc7);
		for (TestClass tc : pcf.getList(false, tq5)) {
			System.out.println(tc.getX());
		}
		assertEquals(2, pcf.getList(false, tq5).size());
		for (TestClass tc : pcf.getList(false, tq5)) {
			assertTrue(tc.getX() > 5);
		}
	}
	
	@Test @Ignore
	public void testGetExpressions() {
		String[] expr = "select e from Employee e where e.dprt = :DE AND e.salary > ?2;"
				.split("[\\w]");
		for (String e : expr) {
			System.out.println(e);
		}
		assertEquals(2, expr.length);
	}
	
	private class TestClass implements Comparable<Object>{
		private String s;
		private int x;
		private boolean b;
		
		public String getS() {
			return s;
		}
		public TestClass setS(String s) {
			this.s = s;
			return this;
		}
		public int getX() {
			return x;
		}
		public TestClass setX(int x) {
			this.x = x;
			return this;
		}
		public boolean isB() {
			return b;
		}
		public TestClass setB(boolean b) {
			this.b = b;
			return this;
		}
		
		public String toString() {
			return getS() + getX() + isB();
		}
		@Override
		public int compareTo(Object o) {
			return toString().compareTo(o.toString());
		}
	}
}
