package com.digitald4.common.jpa;

import java.util.Collections;
import java.util.List;

public class ValueCollection<T> extends PrimaryKey{
	private List<T> list = Collections.synchronizedList(new DD4SortedList<T>());
	public ValueCollection(Object... values){
		super(values);
	}
	public List<T> getList(){
		return list;
	}
}
