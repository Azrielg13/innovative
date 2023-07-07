package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.*;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.Company;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.model.Paystub.Deduction;
import com.digitald4.iis.model.Paystub.DeductionType;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.GenData;
import com.digitald4.iis.storage.NurseStore;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;

public class PaystubReportCreator extends PDFReport {

	private final AppointmentStore appointmenetStore;
	private final NurseStore nurseStore;
	private final GeneralDataStore generalDataStore;

	@Inject
	public PaystubReportCreator(
			Provider<Company> companyProvider,
			AppointmentStore appointmenetStore,
			NurseStore nurseStore,
			GeneralDataStore generalDataStore) {
		super(companyProvider);
		this.appointmenetStore = appointmenetStore;
		this.nurseStore = nurseStore;
		this.generalDataStore = generalDataStore;
	}

	@Override
	public String getTitle() {
		return "Pay Statement\n";
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}

	public ByteArrayOutputStream createPDF(Paystub paystub) throws DocumentException {
		Document document = new Document(getPageSize(), 25, 25, 25, 25);
		document.addAuthor(getAuthor());
		document.addSubject(getSubject());
		document.addTitle(getTitle());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, buffer);
		document.open();
		// document.resetHeader();
		//document.setHeader(getHeader());
		// document.setFooter(getFooter());
		document.setPageSize(getPageSize());
		document.setMargins(25, 25, 25, 25);
		document.newPage();
		document.add(getReportTitle());
		document.add(getBody(paystub));
		//document.add(getFooter());
		document.close();
		return buffer;
	}

	@Override
	public Paragraph getBody() {
		return null;
	}

	private Paragraph getBody(Paystub paystub) throws DD4StorageException, DocumentException {
		Nurse nurse = nurseStore.get(paystub.getNurseId());
		Map<Long, String> deductionMap = generalDataStore.listByGroupId(GenData.DEDUCTION_TYPE).getItems()
				.stream()
				.collect(Collectors.toMap(GeneralData::getId, GeneralData::getName));
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
		cell.addElement(new Phrase(nurse.fullName() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.addElement(new Phrase("Pay Date: " + formatDate(paystub.getPayDate().toEpochMilli()) + "\n",
				FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.addElement(new Phrase("Net Pay: " + formatCurrency(paystub.getNetPay()) + "\n",
				FontFactory.getFont(FontFactory.HELVETICA, 9)));
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Pay Details", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(3);
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		cell = new PdfPCell();
		cell.addElement(
				new Phrase(nurse.fullName() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
		cell.addElement(
				new Phrase(nurse.getAddress().getAddress() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		mainTable.addCell(cell);
		cell = new PdfPCell();
		//cell.addElement(new Phrase("Employee Number " + getNurse().getId() + "\n",
		//    FontFactory.getFont(FontFactory.HELVETICA, 9)));
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
		for (long appId : paystub.getAppointmentIds()) {
			Appointment appointment = appointmenetStore.get(appId);
			datatable.addCell(getCell(appointment.getPatientName(), Font.NORMAL, Element.ALIGN_LEFT));
			datatable.addCell(getCell(formatDate(appointment.getStart())));
			datatable.addCell(getCell(formatTime(appointment.getTimeIn())));
			datatable.addCell(getCell(formatTime(appointment.getTimeOut())));
			AccountingInfo paymentInfo = appointment.getPaymentInfo();
			datatable.addCell(getCell(Double.toString(paymentInfo.getHours())));
			datatable.addCell(getCell(formatCurrency(paymentInfo.getHourlyRate())));
			datatable.addCell(getCell(formatCurrency(paymentInfo.getFlatRate())));
			datatable.addCell(getCell(formatCurrency(paymentInfo.getSubTotal())));
			cell = getCell(Double.toString(paymentInfo.getMileage()));
			cell.setBorder(Rectangle.LEFT);
			datatable.addCell(cell);
			datatable.addCell(getCell(formatCurrency(paymentInfo.getMileageTotal())));
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
		for (Deduction deduction : paystub.getDeductions()) {
			if (deduction.getType() == DeductionType.PRETAX) {
				deducTable.addCell(getCell(deductionMap.get(deduction.getTypeId()), Font.NORMAL, Element.ALIGN_LEFT));
				deducTable.addCell(getCell(formatCurrency(deduction.getAmount())));
				deducTable.addCell(getCell(formatCurrency(deduction.getAmountYTD())));
			}
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
		for (Deduction deduction : paystub.getDeductions()) {
			if (deduction.getType() == DeductionType.TAX) {
				taxTable.addCell(getCell(deductionMap.get(deduction.getTypeId()), Font.NORMAL, Element.ALIGN_LEFT));
				taxTable.addCell(getCell(formatCurrency(deduction.getAmount())));
				taxTable.addCell(getCell(formatCurrency(deduction.getAmountYTD())));
			}
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
		for (Deduction deduction : paystub.getDeductions()) {
			if (deduction.getType() == DeductionType.POSTTAX) {
				deducTable.addCell(getCell(deductionMap.get(deduction.getTypeId()), Font.NORMAL, Element.ALIGN_LEFT));
				deducTable.addCell(getCell(formatCurrency(deduction.getAmount())));
				deducTable.addCell(getCell(formatCurrency(deduction.getAmountYTD())));
			}
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
}
