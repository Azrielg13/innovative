package com.digitald4.iis.reports;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;
import static com.digitald4.common.util.FormatText.formatCurrency;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Date;

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

public class PaystubReport extends PDFReport {
	
	private final Paystub paystub;
	
	public PaystubReport(Paystub paystub) {
		this.paystub = paystub;
	}

	public Nurse getNurse() {
		return paystub.getNurse();
	}
	
	public String getReportName() {
		return paystub.getName();
	}
	
	public Date getPayDate() {
		return paystub.getPayDate();
	}
	
	public String getDocument() {
		return "" + paystub.getId();
	}
	
	public DateTime getTimestamp() {
		return paystub.getGenerationTime();
	}

	@Override
	public String getTitle() {
		return "Pay Statement\n";
	}
	
	public Collection<Appointment> getAppointments() {
		return paystub.getAppointments();
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}

	@Override
	public Paragraph getBody() throws Exception {
		Paragraph body = new Paragraph();
		PdfPTable mainTable = new PdfPTable(3);
		PdfPCell cell = new PdfPCell();
		cell.addElement(new Phrase(getCompany().getName(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.addElement(new Phrase(getCompany().getAddress(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.addElement(new Phrase(getCompany().getPhone(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		mainTable.addCell(cell);
		cell = new PdfPCell();
		cell.addElement(new Phrase("Pay Statement\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.addElement(new Phrase(getNurse() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.addElement(new Phrase("Pay Date: " + formatDate(getPayDate()) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.addElement(new Phrase("Document#: " + getDocument() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.addElement(new Phrase("Net Pay: " + formatCurrency(paystub.getNetPay()) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Pay Details", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(3);
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		cell = new PdfPCell();
		cell.addElement(new Phrase(getNurse() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
		cell.addElement(new Phrase(getNurse().getAddress() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		mainTable.addCell(cell);
		cell = new PdfPCell();
		//cell.addElement(new Phrase("Employee Number " + getNurse().getId() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		//cell.addElement(new Phrase("SSN xxxxx \n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		mainTable.addCell(cell);
		mainTable.addCell(new PdfPCell());
		
		cell = new PdfPCell(new Phrase("Earnings", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(3);
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		PdfPTable datatable = new PdfPTable(10);
		datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		//datatable.setWidths(new int[]{13,9,13,5,5,9,9,10,9,9,9});
		datatable.addCell(getCell("Patient", Font.BOLD, Element.ALIGN_LEFT));
		datatable.addCell(getCell("Visit Date", Font.BOLD));
		datatable.addCell(getCell("Time In", Font.BOLD));
		datatable.addCell(getCell("Time Out", Font.BOLD));
		datatable.addCell(getCell("Total Hrs", Font.BOLD));
		datatable.addCell(getCell("Rate $/hr", Font.BOLD));
		datatable.addCell(getCell("Visit Pay", Font.BOLD));
		datatable.addCell(getCell("Total Gross", Font.BOLD));
		cell = getCell("Mileage", Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(getCell("Mileage Pay", Font.BOLD));
		for (Appointment appointment : getAppointments()) {
			datatable.addCell(getCell(appointment.getPatient().getLastName(), Font.NORMAL, Element.ALIGN_LEFT));
			datatable.addCell(getCell(formatDate(appointment.getStartDate())));
			datatable.addCell(getCell(formatTime(appointment.getTimeIn())));
			datatable.addCell(getCell(formatTime(appointment.getTimeOut())));
			datatable.addCell(getCell("" + appointment.getLoggedHours()));
			datatable.addCell(getCell(formatCurrency(appointment.getPayRate())));
			datatable.addCell(getCell(formatCurrency(appointment.getPayFlat())));
			datatable.addCell(getCell(formatCurrency(appointment.getGrossTotal())));
			cell = getCell("" + appointment.getPayMileage());
			cell.setBorder(Rectangle.LEFT);
			datatable.addCell(cell);
			datatable.addCell(getCell(formatCurrency(appointment.getPayMileageTotal())));
		}
		datatable.addCell(getCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		cell = new PdfPCell();
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(getCell("" + paystub.getLoggedHours(), Font.BOLD));
		cell = new PdfPCell();
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(getCell(formatCurrency(paystub.getGrossPay()), Font.BOLD));
		cell = getCell("" + paystub.getMileage(), Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(getCell(formatCurrency(paystub.getPayMileage()), Font.BOLD));
		
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
		datatable.addCell(getCell(formatCurrency(paystub.getGrossPayYTD()), Font.BOLD));
		cell = getCell("" + paystub.getMileageYTD(), Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(getCell(formatCurrency(paystub.getPayMileageYTD()), Font.BOLD));
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
		mainTable.addCell(cell);
		
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
		cell.setBorder(Rectangle.NO_BORDER);
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
						.setTimeInD(DateTime.now().minusHours(40)).setTimeOutD(DateTime.now()))
				.addAppointment(new Appointment().setPatient(patient).setNurse(nurse).setMileageD((short)300)
						.setStart(DateTime.now())
						.setTimeInD(DateTime.now().minusHours(119)).setTimeOutD(DateTime.now()))
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
						.setTimeInD(DateTime.now().minusHours(4)).setTimeOutD(DateTime.now()))
				.addAppointment(new Appointment().setPatient(patient).setNurse(nurse).setMileageD((short)30)
						.setStart(DateTime.now().minusHours(2)).setEnd(DateTime.now())
						.setTimeInD(DateTime.now().minusMinutes(119)).setTimeOutD(DateTime.now()))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_401K.get()).setFactor(.06))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_HEALTH_CARE.get()).setAmount(32))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_PRE_TAX_DENTAL.get()).setAmount(6))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_TAX_FEDERAL.get()).setFactor(.2))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_TAX_STATE.get()).setFactor(.1))
				.addDeduction(new Deduction().setType(GenData.DEDUCTION_TYPE_POST_TAX_GROUP_TERM_LIFE.get()).setAmount(10))
				.calc();
		ByteArrayOutputStream buffer = new PaystubReport(paystub).createPDF();
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
