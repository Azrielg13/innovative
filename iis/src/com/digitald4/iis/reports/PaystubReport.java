package com.digitald4.iis.reports;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;

import java.awt.Color;
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
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.model.Nurse;
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
	
	public String getNetPay() {
		return FormatText.CURRENCY.format(paystub.getNetPay());
	}
	
	public String getDocument() {
		return "" + paystub.getId();
	}
	
	public DateTime getTimestamp() {
		return paystub.getGenerationTime();
	}

	@Override
	public String getTitle() {
		return "Pay Statement";
	}
	
	public Collection<Appointment> getAppointments() {
		return paystub.getAppointments();
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}

	@Override
	public Paragraph getBody() throws DocumentException, Exception {
		Paragraph body = new Paragraph();
		Table mainTable = new Table(1);
		mainTable.setBorderColor(new Color(0, 0, 0));
		mainTable.setPadding(2);
		mainTable.setSpacing(0);
		mainTable.setWidth(100);
		mainTable.setBorder(Rectangle.BOX);
		Table headTable = new Table(2);
		headTable.setWidth(100);
		headTable.setWidths(new int[]{70, 30});
		headTable.setBorder(Rectangle.NO_BORDER);
		headTable.addCell(new Cell());
		Cell cell = new Cell();
		cell.add(new Phrase("Pay Statement" + "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.add(new Phrase(getNurse() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.add(new Phrase("Pay Date: " + formatDate(getPayDate()) + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.add(new Phrase("Document#: " + getDocument() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.add(new Phrase("Net Pay: " + getNetPay() + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		headTable.addCell(cell);
		mainTable.addCell(new Cell(headTable));
		mainTable.addCell(new Cell(new Phrase("Pay Details", FontFactory.getFont(FontFactory.HELVETICA, 10))));
		
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
			totalHours += appointment.getLoggedHours();
			datatable.addCell(new Cell(new Phrase("" + appointment.getLoggedHours(), FontFactory.getFont(FontFactory.HELVETICA, 9))));
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
		body.add(mainTable);
		return body;
	}
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		Nurse nurse = new Nurse().setUser(new User().setFirstName("Test").setLastName("Nurse"))
				.setPayFlat(100).setPayRate(50)
				.setPayFlat2HrSoc(100).setPayRate2HrSoc(75)
				.setPayFlat2HrRoc(90).setPayFlat2HrRoc(70);
		Vendor vendor = new Vendor().setName("Test Vendor");
		Patient patient = new Patient().setName("Test Patient").setActive(true).setVendor(vendor).setId(123);
		Appointment appointment = new Appointment().setPatient(patient).setNurse(nurse)
				.setStart(DateTime.now().minusHours(3)).setEnd(DateTime.now())
				.setTimeIn(DateTime.now().minusHours(3)).setTimeOut(DateTime.now());
		Paystub paystub = new Paystub().setNurse(nurse).setId(10001)
				.setPayDate(DateTime.now().toDate())
				.addAppointment(appointment);
		ByteArrayOutputStream buffer = new PaystubReport(paystub).createPDF();
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("bin/Appointments.pdf"));
		System.out.println(buffer.toByteArray().length);
		paystub.setData(buffer.toByteArray());
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("bin/Appointments.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}

}
