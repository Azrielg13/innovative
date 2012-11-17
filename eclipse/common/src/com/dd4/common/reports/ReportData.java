package com.dd4.common.reports;

import net.sf.jasperreports.engine.JRDataSource;

public interface ReportData {
	
	public JRDataSource createDataSource();
}
