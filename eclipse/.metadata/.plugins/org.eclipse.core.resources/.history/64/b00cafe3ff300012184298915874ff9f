package com.dd4.common.jpa.criteria;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JpaQueryValueTranslator implements QueryValueTranslator {

	private Map<String, Object> parameterMap;

	public JpaQueryValueTranslator() {
		parameterMap = new HashMap<String, Object>();
	}

	@Override
	public String translateValue(Object value) {

		if (value == null) {
			return null;
		}

		if (value.getClass().equals(ReservedValue.class)) {

			return ((ReservedValue) value).getStringValue();

		} else if (value instanceof CharSequence) {

			String valueAsString = value.toString();
			valueAsString = valueAsString.replaceAll("'", "''");

			return "'" + valueAsString + "'";

		} else if (value instanceof Enum<?>) {

			return String.valueOf(((Enum<?>) value).ordinal());

		} else if (value instanceof java.sql.Date) {

			Date date = new Date(((java.sql.Date) value).getTime());
			return createDateParameter(date, value, "java_sql_Date_", "yyyy_MM_dd");

		} else if (value instanceof java.sql.Time) {

			Date date = new Date(((java.sql.Time) value).getTime());
			return createDateParameter(date, value, "java_sql_Time_", "HH_mm_ss");

		} else if (value instanceof java.sql.Timestamp) {

			Date date = new Date(((java.sql.Timestamp) value).getTime());
			return createDateParameter(date, value, "java_sql_Timestamp_", "yyyy_MM_dd_HH_mm_ss_S");

		} else if (value instanceof java.util.Date) {

			return createDateParameter((Date) value, value, "java_util_Date_", "yyyy_MM_dd_HH_mm_ss_S");

		} else if (value instanceof Calendar) {

			Date date = ((Calendar) value).getTime();
			return createDateParameter(date, value, "java_util_Calendar_", "yyyy_MM_dd_HH_mm_ss_S");
		}

		return value.toString();
	}

	private String createDateParameter(Date date, Object value, String parameterTypeName, String dateFormat) {

		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String formattedDate = formatter.format(date);

		String parameterName = parameterTypeName + formattedDate;

		parameterMap.put(parameterName, value);

		return ":" + parameterName;
	}

	public Map<String, Object> getParametricValues() {
		return new HashMap<String, Object>(parameterMap);
	}
}
