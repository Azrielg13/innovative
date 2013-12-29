/**
 *           | Master Data Interface Version 2.0 |
 *
 * Copyright (c) 2006, Southern California Edison, Inc.
 * 					   Distribution Staff Engineering Team.
 * 	                   All rights reserved.
 *
 * This software has been developed exclusively for internal usage.
 * Unauthorized use is prohibited.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.digitald4.common.dao;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.Time;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.jpa.Change;
import com.digitald4.common.jpa.ChangeLog;
import com.digitald4.common.jpa.Entity;
import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.jpa.PrimaryKey;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;

/**
 * Data Access Object
 *
 * @author Eddie Mayfield
 */
public abstract class DataAccessObject extends Observable implements Comparable<Object>, ChangeLog, Entity{
	private HashMap<String,Change> changes;

	/**
	 * Creates a new instance of DBObject.
	 */
	public DataAccessObject() {
	}

	/**
	 * @param orig The original object to get data from 
	 */
	public DataAccessObject(DataAccessObject orig){
	}

	public abstract Object getId();

	/**
	 * Checks if is new instance.
	 *
	 * @return true, if is new instance
	 */
	public boolean isNewInstance(){
		return EntityManagerHelper.getEntityManager()==null || !EntityManagerHelper.getEntityManager().contains(this);
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 *
	 * @return the int
	 */
	public int compareTo(Object o){
		//If this is the same exact object in memory then just say so
		if(this == o)
			return 0;
		if(o instanceof DataAccessObject)
			return (toString()+getHashKey()).compareTo(o.toString()+((DataAccessObject)o).getHashKey());
		return 0;
	}

	/**
	 * Gets the hash key.
	 *
	 * @return the hash key
	 */
	public abstract String getHashKey();

	/**
	 * Refresh.
	 *
	 * @return true, if refresh
	 *
	 * @throws SQLException the SQL exception
	 */
	public void refresh() {
		EntityManagerHelper.getEntityManager().refresh(this);
	}

	public Collection<Change> getChanges(){
		return changes.values();
	}

	public void addChange(String prop, Object newValue, Object oldValue){
		if(changes == null)
			changes = new HashMap<String,Change>();
		changes.put(prop, new Change(prop,newValue,oldValue));
	}

	protected void setProperty(String prop, Object newValue, Object oldValue) {
		if (prop==null) return;
		if (isNewInstance()) return;
		addChange(prop, newValue,oldValue);
	}

	/**
	 * @throws Exception 
	 */
	public DataAccessObject save() throws Exception{
		if (isNewInstance()) {
			insert();
		} else if (changes != null && changes.size() > 0) {
			EntityManagerHelper.getEntityManager().merge(this);
			changes.clear();
		}
		return this;
	}

	public void delete() {
		EntityManagerHelper.getEntityManager().remove(this);
	}

	public void insertParents() throws Exception{
	}

	/**
	 * Insert.
	 * @throws Exception 
	 *
	 * @throws SQLException the SQL exception
	 */
	public void insert() throws Exception {
		insertPreCheck();
		insertParents();
		if (isNewInstance()) {
			EntityManagerHelper.getEntityManager().persist(this);
		} else {
			save();
		}
		insertChildren();
	}

	public abstract void insertPreCheck() throws Exception;

	public void insertChildren() throws Exception { 	
	}

	public static boolean isSame(Object o, Object o2){
		return Calculate.isSame(o, o2);
	}

	/**
	 * Returns a string hash code of an object of this type with the
	 * specified parameters. The hash code would be used to find the
	 * object in a hash table.
	 *
	 * @param id - id for the object
	 * @param planyear - planYear for the object
	 * @param k1 the k1
	 *
	 * @return a string hash code of an object of this type with the specified parameters.
	 */
	public static String getHashKey(Object[] keys){
		return PrimaryKey.getHashKey(keys);
	}

	public static String getHashKey(Object key){
		return PrimaryKey.getHashKey(key);
	}

	public static boolean isNull(Object... keys){
		for(Object k:keys)
			if(Calculate.isNull(k))
				return true;
		return false;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString(){
		return getHashKey();
	}

	/**
	 * @param dao The object to compare to. 
	 */
	public Vector<String> getDifference(DataAccessObject dao){
		return new Vector<String>();
	}

	/**
	 * @param cp The object to copy children to 
	 */
	public void copyChildrenTo(DataAccessObject cp){
	}

	public String formatProperty(String colname) {
		colname = colname.toUpperCase();
		if (colname.contains(".")) {
			colname = colname.substring(colname.lastIndexOf('.')+1);
		}
		return colname;
	}

	public abstract Object getPropertyValue(String colName);

	public abstract void setPropertyValue(String colName, String value) throws Exception;

	public static DataAccessObject get(JSONObject json) throws Exception {
		String className = json.getString("className");
		Class<?> c = Class.forName(className);
		if (json.has("id")) {
			return (DataAccessObject) c.getMethod("getInstance", Integer.class).invoke(null, json.getInt("id"));
		}
		return (DataAccessObject) c.newInstance();
	}

	public static DataAccessObject updateFromJSON(DataAccessObject dao, JSONObject json) throws Exception {
		Class<? extends DataAccessObject> c = dao.getClass();
		for (String fieldName : JSONObject.getNames(json)) {
			try {
				Field field = c.getSuperclass().getDeclaredField(fieldName);
				Object value = getValue(json.get(fieldName), field.getType());
				if (value != null)
					c.getMethod("set"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType()).invoke(dao, value);
			} catch (NoSuchFieldException e) {
				//Ignore and just move on
				//if (!fieldName.startsWith("$$"))
				//throw e;
			}
		}
		return dao;
	}

	private static Object getValue(Object value, Class<?> javaType) throws ParseException{
		if (javaType == String.class)
			return value.toString();
		if (javaType == int.class || javaType == Integer.class)
			return Integer.valueOf(value.toString());
		if (javaType == long.class)
			return Long.valueOf(value.toString());
		if (javaType == Clob.class)
			return value.toString();
		if (javaType == double.class)
			return Double.parseDouble(value.toString());
		if (javaType == boolean.class)
			return Boolean.valueOf(value.toString());
		if (javaType == Date.class)
			return FormatText.parseDate(value.toString());
		if (javaType == Time.class)
			return FormatText.parseTime(value.toString());
		if (javaType == DateTime.class)
			return new DateTime(value.toString());
		return null;
	}

	public static JSONObject toJSON(DataAccessObject dao) throws JSONException {
		JSONObject json = new JSONObject()
				.put("className", dao.getClass().getName());
		for (Field field : dao.getClass().getSuperclass().getDeclaredFields()) {
			try {
				Method method = null;
				try {
					method = dao.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
				} catch (NoSuchMethodException e) {
					method = dao.getClass().getMethod("is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
				}
				if (field.getType() != List.class) {
					json.put(field.getName(), method.invoke(dao));
				}
			} catch (Exception e1) {
			}
		}
		return json;
	}

	public JSONObject toJSON() throws JSONException {
		return toJSON(this);
	}

	public void update(JSONObject json) throws Exception {
		updateFromJSON(this, json);
	}
}
