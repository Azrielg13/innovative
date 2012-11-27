package com.digitald4.util;
public abstract class DD4Object implements Comparable<Object>{
	protected int id;
	public abstract boolean setField(String field, String value)throws Exception;

	public int getId(){
		return id;
	}
	public int compareTo(Object o){
		return toString().compareTo(o.toString());
	}
}