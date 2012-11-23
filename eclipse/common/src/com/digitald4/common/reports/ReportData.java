package com.digitald4.common.reports;

import net.sf.jasperreports.engine.JRDataSource;

public interface ReportData {
	
	public JRDataSource createDataSource();
}
