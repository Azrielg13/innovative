package com.digitald4.iis.dao;

import com.digitald4.common.dao.sql.DAOProtoSQLImpl;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.iis.proto.IISProtos.Nurse;

public class NurseSQLDao extends DAOProtoSQLImpl<Nurse> {

	public NurseSQLDao(DBConnector dbConnector) {
		super(Nurse.class, dbConnector);
	}
	
	@Override
	public String getView() {
		return "V_NURSE";
	}
}
