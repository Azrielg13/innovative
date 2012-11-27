package com.digitald4.common.jpa;

import javax.persistence.Parameter;

public class ESPParameter<T> implements Parameter<T>, Comparable<Object> {
	private String name;
	private Class<T> c;
	private int position;
	public ESPParameter(String name, Class<T> c, int position) {
		this.name = name;
		this.c = c;
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public Class<T> getParameterType() {
		return c;
	}
	public Integer getPosition() {
		return position;
	}
	public String toString(){
		return getName();
	}
	public int hashCode(){
		return getName().hashCode();
	}
	public int compareTo(Object o) {
		if(o == this)return 0;
		if(o instanceof ESPParameter){
			int ret = getPosition().compareTo(((ESPParameter<?>)o).getPosition());
			if(ret != 0)
				return ret;
		}
		return toString().compareTo(o.toString());
	}
	@Override
	public boolean equals(Object o){
		if(o==this)return true;
		if(o instanceof ESPParameter)
			return getName().equals(((ESPParameter<?>)o).getName());
		return false;
	}
}
