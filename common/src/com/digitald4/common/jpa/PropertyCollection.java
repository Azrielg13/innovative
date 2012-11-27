package com.digitald4.common.jpa;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import com.digitald4.common.log.EspLogger;
import com.digitald4.common.util.FormatText;

public class PropertyCollection<T> extends PrimaryKey{
	private Hashtable<String,ValueCollection<T>> collections = new Hashtable<String,ValueCollection<T>>();
	public PropertyCollection(Object... names){
		super(names);
	}
	public List<T> getList(ValueCollection<T> crit){
		ValueCollection<T> vc = collections.get(crit.getHashKey());
		if(vc!=null)
			return vc.getList();
		return null;
	}
	public List<T> getList(boolean create, ESPTypedQuery<T> tq) throws Exception{
		ValueCollection<T> crit = tq.getValueCollection();
		List<T> list = getList(crit);
		if(list == null && create){
			ValueCollection<T> vc = new ValueCollection<T>(crit.getKeys());
			collections.put(vc.getHashKey(),vc);
			list = vc.getList();
		}
		return list;
	}
	public boolean cache(T o) throws Exception {
		Class<?> c = o.getClass();
		Object[] values = new Object[getKeys().length];
		int i=0;
		for(Object key:getKeys()){
			Object value=null;
			Method m = null;
			try {
				m = c.getMethod("get"+FormatText.toUpperCamel(""+key));
				value = m.invoke(o);
			} catch (NoSuchMethodException e) {
				m = c.getMethod("is"+FormatText.toUpperCamel(""+key));
				value = m.invoke(o);
			} catch (Exception e){
				EspLogger.error(this, "Error executing: "+m);
			}
			values[i++]=value;
		}
		ValueCollection<T> crit = new ValueCollection<T>(values);
		List<T> collection = getList(crit);
		if(collection != null){
			collection.add(o);
			return true;
		}
		return false;
	}
	public void evict(T o) {
		for(ValueCollection<T> vc:collections.values())
			vc.getList().remove(o);
	}
}
