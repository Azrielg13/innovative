package com.dd4.common.jdbc;

public interface Observer<DataAccessObject> {
	public void update(DataAccessObject dao);
}
