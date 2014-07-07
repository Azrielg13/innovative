/***************************************************************************

$name: Invoice Report
$version: 2.0
$date_modified: 07242006
$description:
$owner: Brian Stonerock
Copyright (c) 2006 BSto Productions. All Rights Reserved.

 ****************************************************************************/
package com.digitald4.iis.reports;
import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.joda.time.DateTime;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.common.report.PDFReport;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


public class AssessmentReport extends PDFReport{
	private Appointment appointment;
	
	public AssessmentReport(Appointment appointment) {
		this.appointment = appointment;
	}
	
	@Override
	public String getTitle() {
		return "Patient Assessment";
	}
	
	@Override
	public Paragraph getBody() throws DocumentException, Exception {
		Paragraph body = new Paragraph("");

		PdfPTable datatable = new PdfPTable(2);
		PdfPCell defaultCell = new PdfPCell();
		defaultCell.setBorder(Rectangle.NO_BORDER);
		defaultCell.setHorizontalAlignment(0);
		defaultCell.setColspan(1);
		int headerwidths[] = {40, 60};
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(100);

		Patient patient = appointment.getPatient();

		PdfPCell cell = new PdfPCell(new Phrase("Patient Name: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(patient.getName(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("Pharmacy: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(patient.getReferralSource() + "", FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		datatable.addCell(new Phrase(""));
		cell = new PdfPCell(new Phrase("Certification Period: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(formatDate(appointment.getStartDate()) + " to " + formatDate(appointment.getEndDate()), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		body.add(datatable);
		
		datatable = new PdfPTable(12);
		datatable.setWidthPercentage(100);
		int[] colspans = new int[]{1, 1, 1, 1, 1, 1, 3, 3,
															 3, 2, 2, 2, 3};
		int c = 0;
		for (GeneralData assessment : GenData.ASS_CAT_VITAL.get().getGeneralDatas()) {
			cell = new PdfPCell(new Phrase(assessment + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			cell.addElement(new Phrase(addValue(appointment.getAssessmentValue(assessment)), FontFactory.getFont(FontFactory.HELVETICA, 9)));
			cell.setColspan(colspans[c++]);
			datatable.addCell(cell);
		}
		body.add(datatable);
		
		datatable = new PdfPTable(2);
		datatable.setWidthPercentage(100);
		for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
			if (GenData.ASS_CAT_VITAL.get() != cat) {
				Paragraph p = new Paragraph("");
				p.setSpacingAfter(0);
				p.setSpacingBefore(0);
				p.setKeepTogether(true);
				p.setLeading(12);
				Phrase phrase = new Phrase(cat + "", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
				p.add(phrase);
				for (GeneralData assessment : cat.getGeneralDatas()) {
					p.add(new Phrase("\n" + assessment + ": ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
					p.add(new Phrase(addValue(appointment.getAssessmentValue(assessment)), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.UNDERLINE)));
				}
				datatable.addCell(new PdfPCell(p));
			}
		}
		body.add(datatable);
		datatable = new PdfPTable(10);
		defaultCell = datatable.getDefaultCell();
		defaultCell.setBorder(Rectangle.NO_BORDER);
		defaultCell.setHorizontalAlignment(0);
		defaultCell.setColspan(1);
		datatable.setWidthPercentage(100);
		cell = new PdfPCell(new Phrase("Nurse Name:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("" + appointment.getNurse(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("Nurse Signature:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("" + appointment.getNurse(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		datatable.addCell(new Phrase("Date:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatDate(appointment.getEndDate()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		datatable.addCell(new Phrase("Time In:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatTime(appointment.getTimeIn()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		datatable.addCell(new Phrase("Time Out:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatTime(appointment.getTimeOut()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell = new PdfPCell(new Phrase("Patient Signature:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("" + patient, FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		datatable.addCell(new Phrase("Date:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatDate(appointment.getEndDate()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		body.add(datatable);
		return body;
	}
	public static String addValue(Object value) {
		if (value == null) {
			return null;
		}
		return "" + value; 
	}
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		ByteArrayOutputStream buffer = new AssessmentReport(new Appointment().setStart(DateTime.now().minusHours(1)).setEnd(DateTime.now().plusHours(1))
				.setTimeIn(DateTime.now().minusHours(1)).setTimeOut(DateTime.now().plusHours(1))
				.setPatient(new Patient().setName("Eddie Mayfield"))
				.setNurse(new Nurse().setUser(new User().setFirstName("Nurse").setLastName("Betty")))
				.setAssessmentEntry(GenData.ASS_CAT_VITAL.get().getGeneralDatas().get(1), "98.6")
				.setAssessmentEntry(GenData.ASS_CAT.get().getGeneralDatas().get(1).getGeneralDatas().get(1), "" + GenData.ASS_CAT.get().getGeneralDatas().get(1).getGeneralDatas().get(1).getGeneralDatas().get(1).getId())
		).createPDF();

		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("bin/Assessment.pdf"));
		System.out.println(buffer.toByteArray().length);
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("bin/Assessment.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}
}
