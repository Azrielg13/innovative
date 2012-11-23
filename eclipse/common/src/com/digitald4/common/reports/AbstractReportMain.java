package com.digitald4.common.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;

import java.io.File;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;

public abstract class AbstractReportMain<T extends ReportDesign<U>, U extends ReportData> {
	
	public AbstractReportMain() {
		//build();
	}
	
	public JasperReportBuilder getBuilder(){
		return reportBuilder;
	}
	private JasperReportBuilder reportBuilder;
	protected void build() {
		try {
			reportBuilder = DynamicReports.report();			
			U data = getReportData();
			if (data != null) {
				reportBuilder.setDataSource(data.createDataSource());
			}
			getReportDesign().configureReport(reportBuilder, data);
			//reportBuilder.show();						
		} catch (DRException e) {
			e.printStackTrace();	
		}
	}
	
	public void show() throws DRException{
		reportBuilder.show();
	}
	
	protected U getReportData() {
		return null;
	}
	
	protected abstract T getReportDesign();	
	
	public String exportPDF(){
		File file=null;
		try {
			file = File.createTempFile("mdi",".pdf",null);
			 JasperPdfExporterBuilder pdfExporter = export.pdfExporter(file.toString()); 
			 
			 reportBuilder = DynamicReports.report();			
				U data = getReportData();
				if (data != null) {
					reportBuilder.setDataSource(data.createDataSource());
				}
				getReportDesign().configureReport(reportBuilder, data);
				reportBuilder.toPdf(pdfExporter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return file.toString();
	}
}
