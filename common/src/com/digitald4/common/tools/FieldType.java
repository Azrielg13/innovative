package com.digitald4.common.tools;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;
import java.sql.Types;

import org.joda.time.DateTime;

import com.digitald4.common.util.FormatText;

public enum FieldType {
	BOOLEAN(boolean.class,"NUMBER(1)","BOOLEAN"),
	SHORT(short.class,"NUMBER(5)","SMALLINT"),
	INT(int.class,"NUMBER(9)","INT"),
	ID(Integer.class,"NUMBER(9)","INT"),
	LONG(long.class,"NUMBER(19)","BIGINT"),
	DOUBLE(double.class,"FLOAT(24)","DECIMAL"),
	DATE(Date.class,"DATE","DATE"),
	DATETIME(DateTime.class,"DATE","DATETIME"),
	STRING(String.class,"VARCHAR2(%s)","VARCHAR(%s)"),
	BLOB(Blob.class,"BLOB","BLOB"),
	CLOB(Clob.class,"CLOB","TEXT");
	
	public enum DataStore {
		ORACLE,
		MYSQL
	}
	
	private final Class<?> javaClass;
	private final String oracleType;
	private final String mysqlType;
	
	private FieldType(Class<?> javaClass, String oracleType, String mysqlType) {
		this.javaClass = javaClass;
		this.oracleType = oracleType;
		this.mysqlType = mysqlType;
	}
	
	public Class<?> getJavaClass(){
		return javaClass;
	}
	
	public String getParseCode(){
		if (getJavaClass().getSimpleName().equals("Date")) {
			return "FormatText.parseDate";
		}
		return FormatText.toUpperCamel(getJavaClass().getSimpleName())+".valueOf";
	}
	
	public String getDataStoreType(DataStore ds) {
		switch(ds) {
			case ORACLE: return getOracleType();
			case MYSQL: return getMysqlType();
		}
		return null;
	}
	
	public String getOracleType(){
		return oracleType;
	}
	
	public String getMysqlType() {
		return mysqlType;
	}

	public static FieldType getColumnTypeFromDB(String colName, int type, int columnSize, int decimalDigits) {
		if(type == Types.DECIMAL && decimalDigits > 0)
			return DOUBLE;
		else if(type == Types.DECIMAL && columnSize == 1)
			return BOOLEAN;
		else if(type == Types.DECIMAL && columnSize > 9)
			return LONG;
		else if(type == Types.DECIMAL && colName.endsWith("ID"))
			return ID;
		else if(type == Types.DECIMAL)
			return INT;
		else if(type==Types.DATE)
			return DATE;
		else if(type == Types.TIME || type==Types.TIMESTAMP)
			return DATETIME;
		else if(type == Types.VARCHAR)
			return STRING;
		else if(type == Types.BLOB)
			return BLOB;
		else if(type == Types.CLOB)
			return CLOB;
		return null;
	}
}
