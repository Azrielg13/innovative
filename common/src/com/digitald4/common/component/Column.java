package com.digitald4.common.component;

import com.digitald4.common.util.FormatText;

public class Column {
	private final String name;
	private final String prop;
	private final Class<?> type;
	private final boolean editable;
	
	public Column(String name, String prop, Class<?> type, boolean editable) {
		this.name = name;
		this.prop = prop;
		this.type = type;
		this.editable = editable;
	}
	
	public String getName() {
		return name;
	}
	
	public String getProp() {
		return prop;
	}
	
	public String getMethodName() {
		return "get"+FormatText.toUpperCamel(getProp());
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public Object getValue(Object dao) throws Exception {
		return dao.getClass().getMethod(getMethodName()).invoke(dao);
	}
}
