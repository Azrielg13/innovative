package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.*;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.model.Company;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.report.PDFReport;
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
		datatable.addCell(createCell("Patient", Font.BOLD, Element.ALIGN_LEFT));
		datatable.addCell(createCell("Visit Date", Font.BOLD));
		datatable.addCell(createCell("Time In", Font.BOLD));
		datatable.addCell(createCell("Time Out", Font.BOLD));
		datatable.addCell(createCell("Total Hrs", Font.BOLD));
		datatable.addCell(createCell("Rate $/hr", Font.BOLD));
		datatable.addCell(createCell("Visit Pay", Font.BOLD));
		datatable.addCell(createCell("Total Gross", Font.BOLD));
		cell = createCell("Mileage", Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(createCell("Mileage Pay", Font.BOLD));
		for (long appId : paystub.getAppointmentIds()) {
			Appointment appointment = appointmenetStore.get(appId);
			datatable.addCell(createCell(appointment.getPatientName(), Font.NORMAL, Element.ALIGN_LEFT));
			datatable.addCell(createCell(formatDate(appointment.getStart())));
			datatable.addCell(createCell(formatTime(appointment.getTimeIn())));
			datatable.addCell(createCell(formatTime(appointment.getTimeOut())));
			AccountingInfo paymentInfo = appointment.getPaymentInfo();
			datatable.addCell(createCell(Double.toString(paymentInfo.getHours())));
			datatable.addCell(createCell(formatCurrency(paymentInfo.getHourlyRate())));
			datatable.addCell(createCell(formatCurrency(paymentInfo.getFlatRate())));
			datatable.addCell(createCell(formatCurrency(paymentInfo.getSubTotal())));
			cell = createCell(Double.toString(paymentInfo.getMileage()));
			cell.setBorder(Rectangle.LEFT);
			datatable.addCell(cell);
			datatable.addCell(createCell(formatCurrency(paymentInfo.getMileageTotal())));
		}
		datatable.addCell(createCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		cell = new PdfPCell();
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(createCell("" + paystub.getLoggedHours(), Font.BOLD));
		cell = new PdfPCell();
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(createCell(formatCurrency(paystub.getGrossPay()), Font.BOLD));
		cell = createCell("" + paystub.getMileage(), Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(createCell(formatCurrency(paystub.getPayMileage()), Font.BOLD));
		
		datatable.addCell(createCell("YTD", Font.BOLD, Element.ALIGN_LEFT));
		cell = new PdfPCell();
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(createCell("" + paystub.getLoggedHoursYTD(), Font.BOLD));
		cell = new PdfPCell();
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		datatable.addCell(cell);
		datatable.addCell(createCell(formatCurrency(paystub.getGrossPayYTD()), Font.BOLD));
		cell = createCell("" + paystub.getMileageYTD(), Font.BOLD);
		cell.setBorder(Rectangle.LEFT);
		datatable.addCell(cell);
		datatable.addCell(createCell(formatCurrency(paystub.getPayMileageYTD()), Font.BOLD));
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
		deducTable.addCell(createCell("Deduction", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(createCell("Current", Font.BOLD));
		deducTable.addCell(createCell("YTD", Font.BOLD));
		for (Deduction deduction : paystub.getDeductions()) {
			if (deduction.getType() == DeductionType.PRETAX) {
				deducTable.addCell(createCell(deductionMap.get(deduction.getTypeId()), Font.NORMAL, Element.ALIGN_LEFT));
				deducTable.addCell(createCell(formatCurrency(deduction.getAmount())));
				deducTable.addCell(createCell(formatCurrency(deduction.getAmountYTD())));
			}
		}
		deducTable.addCell(createCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(createCell(formatCurrency(paystub.getPreTaxDeduction()), Font.BOLD));
		deducTable.addCell(createCell(formatCurrency(paystub.getPreTaxDeductionYTD()), Font.BOLD));
		cell = new PdfPCell(deducTable);
		mainTable.addCell(cell);
		
		PdfPTable taxTable = new PdfPTable(3);
		taxTable.addCell(createCell("Tax", Font.BOLD, Element.ALIGN_LEFT));
		taxTable.addCell(createCell("Current", Font.BOLD));
		taxTable.addCell(createCell("YTD", Font.BOLD));
		for (Deduction deduction : paystub.getDeductions()) {
			if (deduction.getType() == DeductionType.TAX) {
				taxTable.addCell(createCell(deductionMap.get(deduction.getTypeId()), Font.NORMAL, Element.ALIGN_LEFT));
				taxTable.addCell(createCell(formatCurrency(deduction.getAmount())));
				taxTable.addCell(createCell(formatCurrency(deduction.getAmountYTD())));
			}
		}
		taxTable.addCell(createCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		taxTable.addCell(createCell(formatCurrency(paystub.getTaxTotal()), Font.BOLD));
		taxTable.addCell(createCell(formatCurrency(paystub.getTaxTotalYTD()), Font.BOLD));
		cell = new PdfPCell(taxTable);
		mainTable.addCell(cell);
		
		deducTable = new PdfPTable(3);
		deducTable.addCell(createCell("Deduction", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(createCell("Current", Font.BOLD));
		deducTable.addCell(createCell("YTD", Font.BOLD));
		for (Deduction deduction : paystub.getDeductions()) {
			if (deduction.getType() == DeductionType.POSTTAX) {
				deducTable.addCell(createCell(deductionMap.get(deduction.getTypeId()), Font.NORMAL, Element.ALIGN_LEFT));
				deducTable.addCell(createCell(formatCurrency(deduction.getAmount())));
				deducTable.addCell(createCell(formatCurrency(deduction.getAmountYTD())));
			}
		}
		deducTable.addCell(createCell("Totals", Font.BOLD, Element.ALIGN_LEFT));
		deducTable.addCell(createCell(formatCurrency(paystub.getPostTaxDeduction()), Font.BOLD));
		deducTable.addCell(createCell(formatCurrency(paystub.getPostTaxDeductionYTD()), Font.BOLD));
		cell = new PdfPCell(deducTable);
		mainTable.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Pay Summary", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(3);
		cell.setGrayFill(.875f);
		mainTable.addCell(cell);
		
		PdfPTable summaryTable = new PdfPTable(8);
		summaryTable.setWidths(new int[]{9, 9, 17, 15, 9, 17, 15, 9});
		summaryTable.addCell(createCell("", Font.BOLD));
		summaryTable.addCell(createCell("Gross", Font.BOLD));
		summaryTable.addCell(createCell("PreTax Deductions", Font.BOLD));
		summaryTable.addCell(createCell("Taxable Wages", Font.BOLD));
		summaryTable.addCell(createCell("Taxes", Font.BOLD));
		summaryTable.addCell(createCell("PostTax Deductions", Font.BOLD));
		summaryTable.addCell(createCell("NonTaxable Wages", Font.BOLD));
		summaryTable.addCell(createCell("Net Pay", Font.BOLD));
		summaryTable.addCell(createCell("Current", Font.NORMAL, Element.ALIGN_LEFT));
		summaryTable.addCell(createCell(formatCurrency(paystub.getGrossPay())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getPreTaxDeduction())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getTaxable())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getTaxTotal())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getPostTaxDeduction())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getNonTaxWages())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getNetPay())));
		summaryTable.addCell(createCell("YTD", Font.NORMAL, Element.ALIGN_LEFT));
		summaryTable.addCell(createCell(formatCurrency(paystub.getGrossPayYTD())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getPreTaxDeductionYTD())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getTaxableYTD())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getTaxTotalYTD())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getPostTaxDeductionYTD())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getNonTaxWagesYTD())));
		summaryTable.addCell(createCell(formatCurrency(paystub.getNetPayYTD())));
		cell = new PdfPCell(summaryTable);
		cell.setColspan(3);
		mainTable.addCell(cell);
		
		body.add(mainTable);
		return body;
	}
}
