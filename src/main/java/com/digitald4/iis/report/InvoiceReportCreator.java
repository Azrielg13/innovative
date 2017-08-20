package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.*;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.proto.DD4Protos.Company;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.storage.Store;
import com.digitald4.common.util.FormatText;
import com.digitald4.common.util.Provider;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Appointment.AccountingInfo;
import com.digitald4.iis.proto.IISProtos.Invoice;
import com.digitald4.iis.proto.IISProtos.Vendor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import org.joda.time.DateTime;

public class InvoiceReportCreator extends PDFReport {

	private final Store<Appointment> appointmenetStore;
	private final Store<Vendor> vendorStore;

	public InvoiceReportCreator(
			Provider<Company> companyProvider,
			Store<Appointment> appointmenetStore,
			Store<Vendor> vendorStore) {
		super(companyProvider);
		this.appointmenetStore = appointmenetStore;
		this.vendorStore = vendorStore;
	}

	public DateTime getTimestamp() {
		return DateTime.now();
	}

	@Override
	public String getTitle() {
		return "Pay Statement\n";
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}

	public ByteArrayOutputStream createPDF(Invoice invoice) throws DD4StorageException, DocumentException {
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
		document.add(getBody(invoice));
		//document.add(getFooter());
		document.close();
		return buffer;
	}

	@Override
	public Paragraph getBody() throws Exception {
		return null;
	}

	private Paragraph getBody(Invoice invoice) throws DD4StorageException, DocumentException {
		Vendor vendor = vendorStore.get(invoice.getVendorId());
		Paragraph body = new Paragraph();
		body.add(new Phrase(vendor.getName() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		body.add(new Phrase("Name: " + invoice.getName() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		body.add(new Phrase("Date: " + formatDate(getTimestamp(), FormatText.USER_DATE) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		//body.add(new Phrase("Invoice#: " + formatDate(getTimestamp(), new SimpleDateFormat("yyMMdd")) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		boolean isPayingMileage = invoice.getBilledMileage() != 0;
		PdfPTable datatable = new PdfPTable(isPayingMileage ? 11 : 9);
		/*datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(2);
		datatable.setSpacing(0);*/
		datatable.setWidthPercentage(100);

		datatable.setWidths(isPayingMileage ? new int[]{13,9,13,5,5,9,9,10,9,9,9}: new int[]{15,11,15,7,7,11,11,12,11});
		datatable.addCell(new PdfPCell(new Phrase("Patient", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Visit Date", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("RN", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Start", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("End", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Total Hrs", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Rate $/hr", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Rate $/per visit (2hrs or less)", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		if (isPayingMileage) {
			datatable.addCell(new PdfPCell(new Phrase("Mileage", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			datatable.addCell(new PdfPCell(new Phrase("Mileage Cost", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		}
		datatable.addCell(new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		for (int appId : invoice.getAppointmentIdList()) {
			Appointment appointment = appointmenetStore.get(appId);
			datatable.addCell(new PdfPCell(new Phrase(appointment.getPatientName(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatDate(appointment.getStart()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(appointment.getNurseName(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatTime(appointment.getTimeIn()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatTime(appointment.getTimeOut()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			AccountingInfo billingInfo = appointment.getBillingInfo();
			datatable.addCell(new PdfPCell(new Phrase("" + billingInfo.getHours(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(billingInfo.getHourlyRate()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(billingInfo.getFlatRate()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			if (isPayingMileage) {
				datatable.addCell(new PdfPCell(new Phrase("" + billingInfo.getMileage(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
				datatable.addCell(new PdfPCell(new Phrase(formatCurrency(billingInfo.getMileageTotal()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			}
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(billingInfo.getTotal()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
		}
		datatable.addCell(new PdfPCell(new Phrase("Totals", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("" + invoice.getLoggedHours(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		if (isPayingMileage) {
			datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(invoice.getBilledMileage()), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		}
		datatable.addCell(new PdfPCell(new Phrase(formatCurrency(invoice.getTotalDue()), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		body.add(datatable);
		return body;
	}
}
