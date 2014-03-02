package com.digitald4.iis.reports;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;

import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.joda.time.DateTime;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Vendor;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;

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

	@Override
	public Paragraph getBody() throws DocumentException, Exception {
		Paragraph body = new Paragraph();
		body.add(new Phrase(getVendor() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		body.add(new Phrase("Name: " + getReportName() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		body.add(new Phrase("Date: " + formatDate(getTimestamp()) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		body.add(new Phrase("Invoice#: " + formatDate(getTimestamp(), new SimpleDateFormat("yyMMdd")) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		Table datatable = new Table(11);
		datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(2);
		datatable.setSpacing(0);
		datatable.setWidth(100);
		datatable.setWidths(new int[]{13,9,13,5,5,9,9,10,9,9,9});
		datatable.addCell(new Cell(new Phrase("Patient", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Visit Date", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("RN", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Start", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("End", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Total Hrs", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Rate $/hr", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Rate $/per visit (2hrs or less)", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Mileage", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Mileage Cost", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		double totalHours = 0, totalMileage = 0, totalBilled = 0;
		for (Appointment appointment : getAppointments()) {
			datatable.addCell(new Cell(new Phrase("" + appointment.getPatient() + " " + appointment.getPatientId(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase(formatDate(appointment.getStartDate()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase("" + appointment.getNurse(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase(formatTime(appointment.getTimeIn()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase(formatTime(appointment.getTimeOut()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			totalHours += appointment.getBilledHours();
			datatable.addCell(new Cell(new Phrase("" + appointment.getBilledHours(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase(FormatText.CURRENCY.format(appointment.getBillingRate()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase(FormatText.CURRENCY.format(appointment.getBillingFlat()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			datatable.addCell(new Cell(new Phrase("" + appointment.getVendorMileage(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			totalMileage += appointment.getVendorMileageTotal();
			datatable.addCell(new Cell(new Phrase(FormatText.CURRENCY.format(appointment.getVendorMileageTotal()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
			totalBilled += appointment.getBillingTotal();
			datatable.addCell(new Cell(new Phrase(FormatText.CURRENCY.format(appointment.getBillingTotal()), FontFactory.getFont(FontFactory.HELVETICA, 9))));
		}
		datatable.addCell(new Cell(new Phrase("Totals", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase("" + totalHours, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		datatable.addCell(new Cell(new Phrase(FormatText.CURRENCY.format(totalMileage), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		datatable.addCell(new Cell(new Phrase(FormatText.CURRENCY.format(totalBilled), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))));
		body.add(datatable);
		return body;
	}
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		if (Patient.getAllActive().size() == 0) {
			new Patient().setName("Tod Lame").setActive(true).setVendor(Vendor.getAllActive().get(0)).insert();
		}
		Invoice invoice = new Invoice().setVendor(Patient.getAllActive().get(0).getVendor())
				.setName("Test Invoice")
				.addAppointment(Patient.getAllActive().get(0).getAppointments().get(0));
		ByteArrayOutputStream buffer = new InvoiceReport(invoice).createPDF();
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("bin/Appointments.pdf"));
		System.out.println(buffer.toByteArray().length);
		invoice.setData(buffer.toByteArray());
		invoice.insert();
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("bin/Appointments.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}

}
