package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;

import com.digitald4.common.model.Company;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.*;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.Assessment;
import com.digitald4.iis.storage.GenData;
import com.google.common.collect.ImmutableList;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.inject.Provider;

public class AssessmentReport extends PDFReport{
	private final GeneralDataStore generalDataStore;
	private final Appointment appointment;

	private AssessmentReport(
			Provider<Company> companyProvider,
			GeneralDataStore generalDataStore,
			Appointment appointment) {
		super(companyProvider);
		this.generalDataStore = generalDataStore;
		this.appointment = appointment;
	}
	
	@Override
	public String getTitle() {
		return "Patient Assessment";
	}
	
	@Override
	public Paragraph getBody() throws DocumentException {
		Paragraph body = new Paragraph("");

		PdfPTable datatable = new PdfPTable(2);
		PdfPCell defaultCell = new PdfPCell();
		defaultCell.setBorder(Rectangle.NO_BORDER);
		defaultCell.setHorizontalAlignment(0);
		defaultCell.setColspan(1);
		int[] headerWidths = {40, 60};
		datatable.setWidths(headerWidths);
		datatable.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell(new Phrase("Patient Name: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(appointment.getPatientName(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("Pharmacy: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(appointment.getVendorName(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		datatable.addCell(new Phrase(""));
		cell = new PdfPCell(new Phrase("Certification Period: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(formatDate(appointment.getStart()) + " Titration: " + appointment.getTitration(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		body.add(datatable);
		
		datatable = new PdfPTable(12);
		datatable.setWidthPercentage(100);
		int[] colspans = new int[]{1, 1, 1, 1, 1, 1, 3, 3, 3, 2, 2, 2, 3};
		int c = 0;
		for (GeneralData assessment : generalDataStore.listByGroupId(GenData.ASS_CAT_VITAL_SIGNS).getItems()) {
			cell = new PdfPCell(new Phrase(assessment + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			cell.addElement(new Phrase(addValue(appointment.getAssessment(assessment.getId())), FontFactory.getFont(FontFactory.HELVETICA, 9)));
			cell.setColspan(colspans[c++]);
			datatable.addCell(cell);
		}
		body.add(datatable);
		
		datatable = new PdfPTable(2);
		datatable.setWidthPercentage(100);
		for (GeneralData cat : generalDataStore.listByGroupId(GenData.ASS_CAT).getItems()) {
			if (cat.getId() != GenData.ASS_CAT_VITAL_SIGNS) {
				Paragraph p = new Paragraph("");
				p.setSpacingAfter(0);
				p.setSpacingBefore(0);
				p.setKeepTogether(true);
				p.setLeading(12);
				Phrase phrase =
						new Phrase(cat.toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
				p.add(phrase);
				for (GeneralData assessment : generalDataStore.listByGroupId(cat.getId()).getItems()) {
					p.add(new Phrase("\n" + assessment + ": ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
					p.add(new Phrase(addValue(appointment.getAssessment(assessment.getId())), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.UNDERLINE)));
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
		cell = new PdfPCell(new Phrase(appointment.getNurseName(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("Nurse Signature:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase(appointment.getNurseName(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		datatable.addCell(new Phrase("Date:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatDate(appointment.getDate()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		datatable.addCell(new Phrase("Time In:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatTime(appointment.getTimeIn()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		datatable.addCell(new Phrase("Time Out:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatTime(appointment.getTimeOut()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell = new PdfPCell(new Phrase("Patient Signature:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase(appointment.getPatientName(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		datatable.addCell(new Phrase("Date:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatDate(appointment.getDate()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		body.add(datatable);
		return body;
	}

	private static String addValue(Object value) {
		if (value == null) {
			return "";
		}
		return String.valueOf(value);
	}

	public static void main(String[] args) throws Exception {
		APIConnector apiConnector = new APIConnector("https://ip360-179401.appspot.com/_ah/api", "v1");
		DAO dao = new DAOApiImpl(apiConnector);
		ImmutableList<Assessment> assessments = ImmutableList.of(
				new Assessment(GenData.ASS_CAT_VITAL_SIGNS + 1L, "98.6"),
				new Assessment(GenData.ASS_CAT_BEHAVIORAL_STATUS + 1L, "Not Good"));
		Company company = new Company().setName("Test Company");
		ByteArrayOutputStream buffer = new AssessmentReport(
				() -> company,
				new GeneralDataStore(() -> dao),
				new Appointment()
					.setStart(Instant.now().minus(1, ChronoUnit.HOURS)).setEnd(Instant.now().plus(1, ChronoUnit.HOURS))
					.setTimeIn(Instant.now().minus(1, ChronoUnit.HOURS)).setTimeOut(Instant.now().plus(1, ChronoUnit.HOURS))
					.setPatientName("Eddie Mayfield")
					.setNurseName("Nurse Betty")
					.setAssessments(assessments)
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
