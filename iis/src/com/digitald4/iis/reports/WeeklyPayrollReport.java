package com.digitald4.iis.reports;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;
import static com.digitald4.common.util.FormatText.formatCurrency;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.User;
import com.digitald4.common.report.PDFReport;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Deduction;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Vendor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class WeeklyPayrollReport extends PDFReport {
	
	private final Date start;
	private final Date end;
	
	public WeeklyPayrollReport(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public String getReportName() {
		return "Weekly Payroll";
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
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
		return "Pay Statement\n";
	}
	
	public Map<Nurse, List<Appointment>> getAppointments() {
		HashMap<Nurse, List<Appointment>> hash = new HashMap<Nurse, List<Appointment>>();
		for (Appointment appointment : Appointment.getCollection("SELECT o FROM Appointment o WHERE o.START >= ?1 AND o.START <= ?2", start, end)) {
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
	
	public String getWeekOf() {
		String weekOf = "WEEK OF: " + formatDate(getStart()) + " - " + formatDate(getEnd());
		return weekOf;
	}

	@Override
	public Paragraph getBody() throws Exception {
		Paragraph body = new Paragraph();
		body.add(new Phrase(getWeekOf() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		PdfPTable mainTable = new PdfPTable(10);
		PdfPCell cell = new PdfPCell();
		
		mainTable.setWidths(new int[]{16, 9, 9, 9, 9, 9, 9, 10, 11, 9});
		mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
		mainTable.addCell(getCell("Start Time", Font.BOLD));
		mainTable.addCell(getCell("End Time", Font.BOLD));
		mainTable.addCell(getCell("Total Hours", Font.BOLD));
		mainTable.addCell(getCell("Rate $/hr", Font.BOLD));
		mainTable.addCell(getCell("Rate $/visit", Font.BOLD));
		mainTable.addCell(getCell("Mileage - 20", Font.BOLD));
		mainTable.addCell(getCell("Mileage * .50", Font.BOLD));
		mainTable.addCell(getCell("Reimbusement", Font.BOLD));
		mainTable.addCell(getCell("Gross Pay", Font.BOLD));
		Map<Nurse, List<Appointment>> map = getAppointments();
		for (Nurse nurse : map.keySet()) {
			mainTable.addCell(getCell("" + nurse, Font.NORMAL, Element.ALIGN_LEFT));
			mainTable.addCell(cell);
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			mainTable.addCell(getCell("", Font.BOLD, Element.ALIGN_LEFT));
			for (Appointment appointment : map.get(nurse)) {
				mainTable.addCell(getCell(" - " + appointment.getPatient().getLastName(), Font.NORMAL, Element.ALIGN_LEFT));
				mainTable.addCell(getCell(formatDate(appointment.getStartDate())));
				mainTable.addCell(getCell(formatTime(appointment.getTimeIn())));
				mainTable.addCell(getCell(formatTime(appointment.getTimeOut())));
				mainTable.addCell(getCell("" + appointment.getLoggedHours()));
				mainTable.addCell(getCell(formatCurrency(appointment.getPayRate())));
				mainTable.addCell(getCell(formatCurrency(appointment.getPayFlat())));
				mainTable.addCell(getCell(formatCurrency(appointment.getGrossTotal())));
				mainTable.addCell(getCell("" + appointment.getPayMileage()));
				mainTable.addCell(getCell(formatCurrency(appointment.getPayMileageTotal())));
			}
		}
		/*datatable.addCell(getCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		cell = new PdfPCell();
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(getCell("" + paystub.getLoggedHours(), Font.BOLD));
		cell = new PdfPCell();
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(getCell(FormatText.CURRENCY.format(paystub.getGrossPay()), Font.BOLD));
		cell = getCell("" + paystub.getMileage(), Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(getCell(FormatText.CURRENCY.format(paystub.getPayMileage()), Font.BOLD));
		
		datatable.addCell(getCell("YTD", Font.BOLD, Element.ALIGN_LEFT));
		cell = new PdfPCell();
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(getCell("" + paystub.getLoggedHoursYTD(), Font.BOLD));
		cell = new PdfPCell();
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(getCell(FormatText.CURRENCY.format(paystub.getGrossPayYTD()), Font.BOLD));
		cell = getCell("" + paystub.getMileageYTD(), Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(getCell(FormatText.CURRENCY.format(paystub.getPayMileageYTD()), Font.BOLD));
		cell = new PdfPCell(datatable);
		cell.setColspan(4);
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("PreTax Deductions", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Taxes", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("PostTax Deductions", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		PdfPTable deducTable = new PdfPTable(3);
		deducTable.addCell(getCell("Deduction", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(getCell("Current", Font.BOLD));
		deducTable.addCell(getCell("YTD", Font.BOLD));
		for (Deduction deduction : paystub.getPreTaxDeductions()) {
			deducTable.addCell(getCell(deduction.getType().getName(), Font.NORMAL, Element.ALIGN_LEFT));
			deducTable.addCell(getCell(formatCurrency(deduction.getAmount())));
			deducTable.addCell(getCell(formatCurrency(deduction.getAmountYTD())));
		}
		deducTable.addCell(getCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(getCell(formatCurrency(paystub.getPreTaxDeduction()), Font.BOLD));
		deducTable.addCell(getCell(formatCurrency(paystub.getPreTaxDeductionYTD()), Font.BOLD));
		cell = new PdfPCell(deducTable);
		mainTable.addCell(cell);
		
		PdfPTable taxTable = new PdfPTable(3);
		taxTable.addCell(getCell("Tax", Font.BOLD, Element.ALIGN_LEFT));
		taxTable.addCell(getCell("Current", Font.BOLD));
		taxTable.addCell(getCell("YTD", Font.BOLD));
		for (Deduction deduction : paystub.getTaxDeductions()) {
			taxTable.addCell(getCell(deduction.getType().getName(), Font.NORMAL, Element.ALIGN_LEFT));
			taxTable.addCell(getCell(formatCurrency(deduction.getAmount())));
			taxTable.addCell(getCell(formatCurrency(deduction.getAmountYTD())));
		}
		taxTable.addCell(getCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		taxTable.addCell(getCell(formatCurrency(paystub.getTaxTotal()), Font.BOLD));
		taxTable.addCell(getCell(formatCurrency(paystub.getTaxTotalYTD()), Font.BOLD));
		cell = new PdfPCell(taxTable);
		mainTable.addCell(cell);
		
		deducTable = new PdfPTable(3);
		deducTable.addCell(getCell("Deduction", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(getCell("Current", Font.BOLD));
		deducTable.addCell(getCell("YTD", Font.BOLD));
		for (Deduction deduction : paystub.getPostTaxDeductions()) {
			deducTable.addCell(getCell(deduction.getType().getName(), Font.NORMAL, Element.ALIGN_LEFT));
			deducTable.addCell(getCell(formatCurrency(deduction.getAmount())));
			deducTable.addCell(getCell(formatCurrency(deduction.getAmountYTD())));
		}
		deducTable.addCell(getCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(getCell(formatCurrency(paystub.getPostTaxDeduction()), Font.BOLD));
		deducTable.addCell(getCell(formatCurrency(paystub.getPostTaxDeductionYTD()), Font.BOLD));
		cell = new PdfPCell(deducTable);
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Pay Summary", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(3);
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		PdfPTable summaryTable = new PdfPTable(8);
		summaryTable.setWidths(new int[]{9, 9, 17, 15, 9, 17, 15, 9});
		summaryTable.addCell(getCell("", Font.BOLD));
		summaryTable.addCell(getCell("Gross", Font.BOLD));
		summaryTable.addCell(getCell("PreTax Deductions", Font.BOLD));
		summaryTable.addCell(getCell("Taxable Wages", Font.BOLD));
		summaryTable.addCell(getCell("Taxes", Font.BOLD));
		summaryTable.addCell(getCell("PostTax Deductions", Font.BOLD));
		summaryTable.addCell(getCell("NonTaxable Wages", Font.BOLD));
		summaryTable.addCell(getCell("Net Pay", Font.BOLD));
		summaryTable.addCell(getCell("Current", Font.NORMAL, Element.ALIGN_LEFT));
		summaryTable.addCell(getCell(formatCurrency(paystub.getGrossPay())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getPreTaxDeduction())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getTaxable())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getTaxTotal())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getPostTaxDeduction())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getNonTaxWages())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getNetPay())));
		summaryTable.addCell(getCell("YTD", Font.NORMAL, Element.ALIGN_LEFT));
		summaryTable.addCell(getCell(formatCurrency(paystub.getGrossPayYTD())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getPreTaxDeductionYTD())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getTaxableYTD())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getTaxTotalYTD())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getPostTaxDeductionYTD())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getNonTaxWagesYTD())));
		summaryTable.addCell(getCell(formatCurrency(paystub.getNetPayYTD())));
		cell = new PdfPCell(summaryTable);
		cell.setColspan(3);
		mainTable.addCell(cell);*/
		
		body.add(mainTable);
		return body;
	}
	
	private PdfPCell getCell(String text) {
		return getCell(text, Font.NORMAL, Element.ALIGN_RIGHT);
	}
	
	private PdfPCell getCell(String text, int font) {
		return getCell(text, font, Element.ALIGN_RIGHT);
	}
	
	private PdfPCell getCell(String text, int font, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9, font)));
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
		Nurse nurse = new Nurse().setUser(new User().setId(123).setFirstName("Nurse").setLastName("Betty"))
				.setAddress("1080 LED Road Panasonic, CA 19201-1080")
				.setPayFlat(100).setPayRate(50)
				.setPayFlat2HrSoc(100).setPayRate2HrSoc(75)
				.setPayFlat2HrRoc(90).setPayFlat2HrRoc(70);
		Vendor vendor = new Vendor().setName("Test Vendor");
		Patient patient = new Patient().setName("Sick Jefferson").setActive(true).setVendor(vendor).setId(123);
		nurse.addPaystub(new Paystub().setNurse(nurse).setId(10001)
				.setPayDate(DateTime.now().toDate())
				.addAppointment(new Appointment().setPatient(patient).setNurse(nurse)
						.setStart(DateTime.now())
						.setTimeIn(DateTime.now().minusHours(40)).setTimeOut(DateTime.now()))
				.addAppointment(new Appointment().setPatient(patient).setNurse(nurse).setMileageD((short)300)
						.setStart(DateTime.now())
						.setTimeIn(DateTime.now().minusHours(119)).setTimeOut(DateTime.now()))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_401K.get()).setFactor(.06))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_HEALTH_CARE.get()).setAmount(32))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_DENTAL.get()).setAmount(6))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_TAX_FEDERAL.get()).setFactor(.2))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_TAX_STATE.get()).setFactor(.1))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_POST_TAX_GROUP_TERM_LIFE.get()).setAmount(10))
				.calc());
		Paystub paystub = new Paystub().setNurse(nurse).setId(10001)
				.setPayDate(DateTime.now().toDate())
				.addAppointment(new Appointment().setPatient(patient).setNurse(nurse)
						.setStart(DateTime.now().minusHours(3)).setEnd(DateTime.now())
						.setTimeIn(DateTime.now().minusHours(4)).setTimeOut(DateTime.now()))
				.addAppointment(new Appointment().setPatient(patient).setNurse(nurse).setMileageD((short)30)
						.setStart(DateTime.now().minusHours(2)).setEnd(DateTime.now())
						.setTimeIn(DateTime.now().minusMinutes(119)).setTimeOut(DateTime.now()))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_401K.get()).setFactor(.06))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_HEALTH_CARE.get()).setAmount(32))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_DENTAL.get()).setAmount(6))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_TAX_FEDERAL.get()).setFactor(.2))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_TAX_STATE.get()).setFactor(.1))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_POST_TAX_GROUP_TERM_LIFE.get()).setAmount(10))
				.calc();
		ByteArrayOutputStream buffer = new WeeklyPayrollReport(DateTime.now().minusDays(7).toDate(), DateTime.now().toDate()).createPDF();
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("bin/Paysummary.pdf"));
		System.out.println(buffer.toByteArray().length);
		paystub.setData(buffer.toByteArray());
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("bin/Paysummary.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}

}
