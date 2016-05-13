package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.formatCurrency;
import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Nurse;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PayrollReport extends PDFReport {
	
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	public enum REPORT_TYPE { WEEKLY, MONTHLY, YEARLY };
	
	private final DateTime start;
	private final DateTime end;
	private final REPORT_TYPE type;
	
	public PayrollReport(int year) {
		type = REPORT_TYPE.YEARLY;
		start =  DateTime.parse(year + "-01-01", DATE_FORMAT);
		end = start.plusYears(1);
	}
	
	public PayrollReport(int year, int month) {
		type = REPORT_TYPE.MONTHLY;
		start =  DateTime.parse(year + "-" + month + "-01", DATE_FORMAT);
		end = start.plusMonths(1);
	}
	
	public PayrollReport(int year, int month, int day) {
		this(DateTime.parse(year + "-" + month + "-" + day, DATE_FORMAT).plusDays(1));
	}
	
	public PayrollReport(DateTime date) {
		type = REPORT_TYPE.WEEKLY;
		// Set start to previous Sunday 12:00:00.000
		start = date.minusDays(date.getDayOfWeek() % 7).minusMillis(date.getMillisOfDay());
		// Set end to next Saturday 11:59:59.999 
		this.end = start.plusDays(7).minusMillis(1);
	}
	
	public String getReportName() {
		return "Payroll Report";
	}
	
	public DateTime getStart() {
		return start;
	}
	
	public DateTime getEnd() {
		return end;
	}
	
	public String getDocument() {
		return "";
	}
	
	public DateTime getTimestamp() {
		return DateTime.now();
	}

	@Override
	public String getTitle() {
		return "Payroll Report\n";
	}
	
	public Map<Nurse, List<Appointment>> getAppointments() {
		HashMap<Nurse, List<Appointment>> hash = new HashMap<Nurse, List<Appointment>>();
		for (Appointment appointment : Appointment.getCollection("SELECT o FROM Appointment o WHERE o.START >= ?1 AND o.START < ?2 AND o.CANCELLED = ?3", getStart(), getEnd(), false)) {
			List<Appointment> list = hash.get(appointment.getNurse());
			if (list == null) {
				list = new ArrayList<Appointment>();
				hash.put(appointment.getNurse(), list);
			}
			list.add(appointment);
		}
		return hash;
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}
	
	public String getTimePeriod() {
		switch (type) { 
			case WEEKLY: return "WEEK OF: " + formatDate(getStart()) + " - " + formatDate(getEnd());
			case MONTHLY: return "MONTH OF: " + formatDate(getStart(), FormatText.USER_MONTH);
			case YEARLY: return "YEAR OF: " + getStart().getYear();
		}
		return null;
	}

	@Override
	public Paragraph getBody() throws Exception {
		Paragraph body = new Paragraph();
		body.add(new Phrase(getTimePeriod() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		PdfPTable mainTable = new PdfPTable(10);
		PdfPCell cell = new PdfPCell();
		
		mainTable.setWidths(new int[]{16, 8, 8, 9, 9, 9, 9, 10, 11, 11});
		mainTable.addCell(getCell("", Font.BOLD, 9, Element.ALIGN_LEFT));
		mainTable.addCell(getCell("Start Time", Font.BOLD));
		mainTable.addCell(getCell("End Time", Font.BOLD));
		mainTable.addCell(getCell("Total Hours", Font.BOLD));
		mainTable.addCell(getCell("Rate $/hr", Font.BOLD));
		mainTable.addCell(getCell("Rate $/visit", Font.BOLD));
		mainTable.addCell(getCell("Mileage - 20", Font.BOLD));
		mainTable.addCell(getCell("Mileage * .50", Font.BOLD));
		mainTable.addCell(getCell("Reimbusement", Font.BOLD));
		mainTable.addCell(getCell("Gross Pay", Font.BOLD));
		double grandTotalHours = 0, grandTotalMilePay = 0, grandTotalPay = 0;
		int grandTotalMiles = 0;
		Map<Nurse, List<Appointment>> map = getAppointments();
		for (Nurse nurse : map.keySet()) {
			double totalHours = 0, totalMilePay = 0, totalPay = 0;
			int totalMiles = 0;
			for (Appointment appointment : map.get(nurse)) {
				totalHours += appointment.getLoggedHours();
				totalMiles += appointment.getPayMileage();
				totalMilePay += appointment.getPayMileageTotal();
				totalPay += appointment.getPaymentTotal();
			}
			grandTotalHours += totalHours;
			grandTotalMiles += totalMiles;
			grandTotalMilePay += totalMilePay;
			grandTotalPay += totalPay;
			mainTable.addCell(getCell("" + nurse, Font.NORMAL, 10, Element.ALIGN_LEFT));
			mainTable.addCell(cell);
			mainTable.addCell(cell);
			mainTable.addCell(getCell("" + totalHours, Font.NORMAL, 10, Element.ALIGN_RIGHT));
			mainTable.addCell(cell);
			mainTable.addCell(cell);
			mainTable.addCell(getCell("" + totalMiles, Font.NORMAL, 10, Element.ALIGN_RIGHT));
			mainTable.addCell(getCell(formatCurrency(totalMilePay), Font.NORMAL, 10, Element.ALIGN_RIGHT));
			mainTable.addCell(getCell(""));
			mainTable.addCell(getCell(formatCurrency(totalPay), Font.NORMAL, 10, Element.ALIGN_RIGHT));
			for (Appointment appointment : map.get(nurse)) {
				mainTable.addCell(getCell(" - " + formatDate(appointment.getStartDate()) + " " + appointment.getPatient().getLastName(), Font.NORMAL, 9, Element.ALIGN_LEFT));
				mainTable.addCell(getCell(formatTime(appointment.getTimeIn())));
				mainTable.addCell(getCell(formatTime(appointment.getTimeOut())));
				mainTable.addCell(getCell("" + appointment.getLoggedHours()));
				mainTable.addCell(getCell(formatCurrency(appointment.getPayRate())));
				mainTable.addCell(getCell(formatCurrency(appointment.getPayFlat())));
				mainTable.addCell(getCell("" + appointment.getPayMileage()));
				mainTable.addCell(getCell(formatCurrency(appointment.getPayMileageTotal())));
				mainTable.addCell(getCell("$0.00"));
				mainTable.addCell(getCell(formatCurrency(appointment.getPaymentTotal())));
			}
		}
		mainTable.addCell(getCell("Totals", Font.BOLD, 11, Element.ALIGN_RIGHT));
		mainTable.addCell(cell);
		mainTable.addCell(cell);
		mainTable.addCell(getCell("" + grandTotalHours, Font.BOLD, 11, Element.ALIGN_RIGHT));
		mainTable.addCell(cell);
		mainTable.addCell(cell);
		mainTable.addCell(getCell("" + grandTotalMiles, Font.BOLD, 11, Element.ALIGN_RIGHT));
		mainTable.addCell(getCell(formatCurrency(grandTotalMilePay), Font.BOLD, 11, Element.ALIGN_RIGHT));
		mainTable.addCell(getCell(""));
		mainTable.addCell(getCell(formatCurrency(grandTotalPay), Font.BOLD, 11, Element.ALIGN_RIGHT));
		body.add(mainTable);
		return body;
	}
	
	private PdfPCell getCell(String text) {
		return getCell(text, Font.NORMAL, 9, Element.ALIGN_RIGHT);
	}
	
	private PdfPCell getCell(String text, int font) {
		return getCell(text, font, 9, Element.ALIGN_RIGHT);
	}
	
	private PdfPCell getCell(String text, int font, int fontSize, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, fontSize, font)));
		//cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(alignment);
		return cell;
	}
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		ByteArrayOutputStream buffer = new PayrollReport(2014).createPDF();
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("bin/PayrollReport.pdf"));
		System.out.println(buffer.toByteArray().length);
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("bin/PayrollReport.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}
}
