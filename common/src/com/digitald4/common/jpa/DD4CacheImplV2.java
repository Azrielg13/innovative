package com.digitald4.common.jpa;

import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.persistence.Cache;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;

import com.digitald4.common.jdbc.ESPHashtable;
import com.digitald4.common.log.EspLogger;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.Retryable;

public class DD4CacheImplV2 implements DD4Cache {
	public static enum NULL_TYPES{IS_NULL, IS_NOT_NULL};
	private DD4EntityManagerFactory emf;
	private ESPHashtable<Class<?>,ESPHashtable<String,Object>> hashById = new ESPHashtable<Class<?>,ESPHashtable<String,Object>>(199);
	private Hashtable<Class<?>,PropertyCollectionFactory<?>> propFactories = new Hashtable<Class<?>,PropertyCollectionFactory<?>>();

	public DD4CacheImplV2(DD4EntityManagerFactory emf){
		this.emf = emf;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean contains(Class c, Object o) {
		ESPHashtable<String,Object> classHash = hashById.get(c);
		if(classHash != null)
			return classHash.containsKey(((Entity)o).getHashKey());
		return false;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void evict(Class c) {
		hashById.remove(c);
		propFactories.remove(c);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void evict(Class c, Object o) {
		ESPHashtable<String,Object> classHash = hashById.get(c);
		if(classHash != null)
			classHash.remove(((Entity)o).getHashKey());
		@SuppressWarnings("unchecked")
		PropertyCollectionFactory<Object> pcf = getPropertyCollectionFactory(false, c);
		if(pcf!=null)
			pcf.evict(o);
	}
	
	@Override
	public void evictAll() {
		hashById.clear();
		propFactories.clear();
	}
	
	@Override
	public <T> T find(Class<T> c, PrimaryKey pk) throws Exception{
		return getCachedObj(c, pk);
	}
	
	@Override
	public <T> void reCache(T o) {
		Class<T> c = (Class<T>)o.getClass();
		PropertyCollectionFactory<T> pcf = getPropertyCollectionFactory(false, c);
		if (pcf != null) {
			pcf.evict(o);
			pcf.cache(o);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getCachedObj(Class<T> c, Object pk){
		ESPHashtable<String,Object> classHash = hashById.get(c);
		if (classHash == null) { 
			return null;
		}
		return (T)classHash.get(((Entity)pk).getHashKey());
	}
	
	public <T> PropertyCollectionFactory<T> getPropertyCollectionFactory(boolean create, Class<T> c){
		@SuppressWarnings("unchecked")
		PropertyCollectionFactory<T> pcf = (PropertyCollectionFactory<T>)propFactories.get(c);
		if(pcf == null && create){
			pcf = new PropertyCollectionFactory<T>();
			propFactories.put(c, pcf);
		}
		return pcf;
	}
	
	
	
	public <T> void put(T o) {
		ESPHashtable<String, Object> classHash = hashById.get(o.getClass());
		if (classHash == null) {
			classHash = new ESPHashtable<String, Object>(199);
			hashById.put(o.getClass(), classHash);
		}
		classHash.put(((Entity)o).getHashKey(), o);
	}
	
	private <T> void put(T o, DD4TypedQuery<T> tq) throws Exception {
		PropertyCollectionFactory<T> pcf = getPropertyCollectionFactory(true, tq.getTypeClass());
		pcf.cache(o,tq);
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> c) { 
		String query = EntityManagerHelper.getNamedQuery(name, c);
		if(query == null)
			query = EntityManagerHelper.getNamedQuery(name, c.getSuperclass());
		return new DD4TypedQuery<T>(this, name, query, c);
	}
	
	Hashtable<String,PropCPU> propCPUs = new Hashtable<String,PropCPU>(); 
	public PropCPU getPropCPU(Object o, String prop){
		String ss = o.getClass()+"."+prop;
		PropCPU pc = propCPUs.get(ss);
		if (pc == null) {
			pc = new PropCPU();

			propCPUs.put(ss, pc);
			Method getMethod = null;
			String upperCamel = FormatText.toUpperCamel(prop);
			try {
				getMethod = o.getClass().getMethod("get" + upperCamel);
			} catch(Exception e) {
			}
			if (getMethod == null) {
				try {
					getMethod = o.getClass().getMethod("is" + upperCamel);
				} catch(Exception e2) {
				}
			}
			if (getMethod != null) {
				Method setMethod = null;
				try {
					setMethod = o.getClass().getMethod("set" + upperCamel,getMethod.getReturnType());
					pc.javaType = getMethod.getReturnType();
					pc.setMethod = setMethod;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return pc;
	}
	private class PropCPU {
		Class<?> javaType;
		Method setMethod;
	}
	private Object getValue(ResultSet rs, int col, String colName, Class<?> javaType) throws SQLException{
		if(javaType == String.class)
			return rs.getString(col);
		if(javaType == int.class || javaType == Integer.class)
			return rs.getInt(col);
		if (javaType == short.class)
			return rs.getShort(col);
		if(javaType == long.class)
			return rs.getLong(col);
		if(javaType == Clob.class)
			return rs.getClob(col);
		if(javaType == double.class)
			return rs.getDouble(col);
		if(javaType == boolean.class)
			return rs.getBoolean(col);
		if(javaType == Calendar.class){
			if(colName.toUpperCase().contains("DATE"))
				return getCalendar(rs.getDate(col));
			return getCalendar(rs.getTimestamp(col));
		}
		if(javaType == Time.class)
			return rs.getTime(col);
		if(javaType == Date.class)
			return rs.getDate(col);
		if (javaType == DateTime.class) {
			if (rs.getObject(col) == null) {
				return null;
			}
			return new DateTime(rs.getTimestamp(col));
		}
		return rs.getObject(col);
	}
	public static Calendar getCalendar(Date date){
		if(date == null)
			return null;
		Calendar cal = Calculate.getCal(2002, Calendar.APRIL, 8);
		cal.setTime(date);
		return cal;
	}
	public static Calendar getCalendar(Timestamp ts){
		if(ts == null)
			return null;
		Calendar cal = Calculate.getCal(2002, Calendar.APRIL, 8);
		cal.setTime(ts);
		return cal;
	}
	public static boolean isNull(Object value){
		if(value == null) return true;
		if(value instanceof Number)
			return ((Number)value).doubleValue()==0.0;
		return false;
	}
	
	protected void processGenKeysOracle(HashMap<String,Method> gKeys, PreparedStatement ps, String table, Object o) throws Exception {
		if (gKeys.size() > 0) {
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				String gCols = "";
				for (String gk:gKeys.keySet()) {
					if(gCols.length()>0)
						gCols+=",";
					gCols+=gk;
				}
				Connection con = emf.getConnection();
				PreparedStatement ps2 = null;
				try {
					ps2 = con.prepareStatement("SELECT "+gCols+" FROM "+table+" WHERE ROWID=?");
					ps2.setString(1,rs.getString(1));
					rs.close();
					rs = ps2.executeQuery();
					if (rs.next()) {
						for (String gk:gKeys.keySet()) {
							gKeys.get(gk).invoke(o, rs.getInt(gk));
						}
					}
				} catch (Exception e) {
					throw e;
				} finally {
					if (rs != null) {
						rs.close();
					}
					if (ps2 != null) {
						ps2.close();
					}
					con.close();
				}
			}
		}
	}
}
