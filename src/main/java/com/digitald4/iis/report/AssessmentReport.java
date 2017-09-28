package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.formatDate;
import static com.digitald4.common.util.FormatText.formatTime;

import com.digitald4.common.jdbc.DBConnectorThreadPoolImpl;
import com.digitald4.common.proto.DD4Protos.Company;
import com.digitald4.common.proto.DD4Protos.GeneralData;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.storage.DAOSQLImpl;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.util.Provider;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.storage.GenData;
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
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;

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
		int headerwidths[] = {40, 60};
		datatable.setWidths(headerwidths);
		datatable.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell(new Phrase("Patient Name: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(appointment.getPatientName(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		cell = new PdfPCell(new Phrase("Pharmacy: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(appointment.getVendorName(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		datatable.addCell(new Phrase(""));
		cell = new PdfPCell(new Phrase("Certification Period: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.addElement(new Phrase(formatDate(appointment.getStart()) + " to " + formatDate(appointment.getEnd()), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		body.add(datatable);
		
		datatable = new PdfPTable(12);
		datatable.setWidthPercentage(100);
		int[] colspans = new int[]{1, 1, 1, 1, 1, 1, 3, 3,
															 3, 2, 2, 2, 3};
		int c = 0;
		Map<Long, String> assessmentMap = appointment.getAssessmentMap();
		for (GeneralData assessment : generalDataStore.listByGroupId(GenData.ASS_CAT_VITAL_SIGNS)) {
			cell = new PdfPCell(new Phrase(assessment + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			cell.addElement(new Phrase(addValue(assessmentMap.get(assessment.getId())), FontFactory.getFont(FontFactory.HELVETICA, 9)));
			cell.setColspan(colspans[c++]);
			datatable.addCell(cell);
		}
		body.add(datatable);
		
		datatable = new PdfPTable(2);
		datatable.setWidthPercentage(100);
		for (GeneralData cat : generalDataStore.listByGroupId(GenData.ASS_CAT)) {
			if (cat.getId() != GenData.ASS_CAT_VITAL_SIGNS) {
				Paragraph p = new Paragraph("");
				p.setSpacingAfter(0);
				p.setSpacingBefore(0);
				p.setKeepTogether(true);
				p.setLeading(12);
				Phrase phrase = new Phrase(cat + "", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD));
				p.add(phrase);
				for (GeneralData assessment : generalDataStore.listByGroupId(cat.getId())) {
					p.add(new Phrase("\n" + assessment + ": ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
					p.add(new Phrase(addValue(assessmentMap.get(assessment.getId())), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.UNDERLINE)));
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
		datatable.addCell(new Phrase(formatDate(appointment.getEnd()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
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
		datatable.addCell(new Phrase(formatDate(appointment.getEnd()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
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
		DBConnectorThreadPoolImpl dbConnector = new DBConnectorThreadPoolImpl(
			"org.gjt.mm.mysql.Driver",
			"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
			"iisosnet_user", "getSchooled85");
		Map<Long, String> assessmentMap = new HashMap<>();
		assessmentMap.put(GenData.ASS_CAT_VITAL_SIGNS + 1L, "98.6");
		assessmentMap.put(GenData.ASS_CAT_BEHAVIORAL_STATUS + 1L, "Not Good");
		Company company = Company.newBuilder()
				.setName("Test Company")
				.build();
		ByteArrayOutputStream buffer = new AssessmentReport(
				() -> company,
				new GeneralDataStore(() -> new DAOSQLImpl(dbConnector)),
				Appointment.newBuilder()
					.setStart(DateTime.now().minusHours(1).getMillis()).setEnd(DateTime.now().plusHours(1).getMillis())
					.setTimeIn(DateTime.now().minusHours(1).getMillis()).setTimeOut(DateTime.now().plusHours(1).getMillis())
					.setPatientName("Eddie Mayfield")
					.setNurseName("Nurse Betty")
					.putAllAssessment(assessmentMap)
					.build()
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
