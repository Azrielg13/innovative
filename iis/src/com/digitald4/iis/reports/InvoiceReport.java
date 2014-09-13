package com.digitald4.iis.reports;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatCurrency;
import static com.digitald4.common.util.FormatText.formatTime;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

import org.joda.time.DateTime;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.User;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class InvoiceReport extends PDFReport {
	
	private final Invoice invoice;
	
	public InvoiceReport(Invoice invoice) {
		this.invoice = invoice;
	}

	public Vendor getVendor() {
		return invoice.getVendor();
	}
	
	public String getReportName() {
		return invoice.getName();
	}
	
	public DateTime getTimestamp() {
		return invoice.getGenerationTime();
	}

	@Override
	public String getTitle() {
		return "Invoice";
	}
	
	public Collection<Appointment> getAppointments() {
		return invoice.getAppointments();
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}
	
	public boolean isPayingMileage() {
		for (Appointment app : getAppointments()) {
			if (app.getBillingMileageTotal() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Paragraph getBody() throws DocumentException, Exception {
		Paragraph body = new Paragraph();
		body.add(new Phrase(getVendor() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		body.add(new Phrase("Name: " + getReportName() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		body.add(new Phrase("Date: " + formatDate(getTimestamp(), FormatText.USER_DATETIME_SHORT) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		//body.add(new Phrase("Invoice#: " + formatDate(getTimestamp(), new SimpleDateFormat("yyMMdd")) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		boolean mileage = isPayingMileage();
		PdfPTable datatable = new PdfPTable(mileage ? 11 : 9);
		/*datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(2);
		datatable.setSpacing(0);*/
		datatable.setWidthPercentage(100);
		
		datatable.setWidths(mileage ? new int[]{13,9,13,5,5,9,9,10,9,9,9}: new int[]{15,11,15,7,7,11,11,12,11});
		datatable.addCell(new PdfPCell(new Phrase("Patient", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Visit Date", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("RN", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Start", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("End", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Total Hrs", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Rate $/hr", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("Rate $/per visit (2hrs or less)", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		if (mileage) {
			datatable.addCell(new PdfPCell(new Phrase("Mileage", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
			datatable.addCell(new PdfPCell(new Phrase("Mileage Cost", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		}
		datatable.addCell(new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		double totalHours = 0, totalMileage = 0, totalBilled = 0;
		for (Appointment appointment : getAppointments()) {
			datatable.addCell(new PdfPCell(new Phrase("" + appointment.getPatient(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatDate(appointment.getStartDate()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase("" + appointment.getNurse(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatTime(appointment.getTimeIn()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatTime(appointment.getTimeOut()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			totalHours += appointment.getLoggedHours();
			datatable.addCell(new PdfPCell(new Phrase("" + appointment.getLoggedHours(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(appointment.getBillingRate()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(appointment.getBillingFlat()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			if (mileage) {
				datatable.addCell(new PdfPCell(new Phrase("" + appointment.getBillingMileage(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
				totalMileage += appointment.getBillingMileageTotal();
				datatable.addCell(new PdfPCell(new Phrase(formatCurrency(appointment.getBillingMileageTotal()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			}
			totalBilled += appointment.getBillingTotal();
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(appointment.getBillingTotal()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
		}
		datatable.addCell(new PdfPCell(new Phrase("Totals", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("" + totalHours, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		if (mileage) {
			datatable.addCell(new PdfPCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
			datatable.addCell(new PdfPCell(new Phrase(formatCurrency(totalMileage), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		}
		datatable.addCell(new PdfPCell(new Phrase(formatCurrency(totalBilled), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		body.add(datatable);
		return body;
	}
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		Vendor vendor = new Vendor().setName("Test Vendor")
				.setBillingFlat(90).setBillingRate(50)
				.setBillingFlat2HrSoc(190).setBillingRate2HrSoc(100)
				.setBillingFlat2HrRoc(180).setBillingRate2HrRoc(90);
		Patient patient = new Patient().setName("Tod Lame").setId(123).setActive(true).setVendor(vendor);
		Nurse nurse = new Nurse().setUser(new User().setFirstName("Test").setLastName("Nurse"));
		Appointment appointment = new Appointment().setPatient(patient).setNurse(nurse)
				.setStart(DateTime.now().minusHours(3)).setEnd(DateTime.now())
				.setTimeInD(DateTime.now().minusHours(3)).setTimeOutD(DateTime.now());
		Invoice invoice = new Invoice().setVendor(vendor)
				.setName("Test Invoice")
				.addAppointment(appointment);
		ByteArrayOutputStream buffer = new InvoiceReport(invoice).createPDF();
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("bin/Appointments.pdf"));
		System.out.println(buffer.toByteArray().length);
		invoice.setData(buffer.toByteArray());
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("bin/Appointments.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}

}
