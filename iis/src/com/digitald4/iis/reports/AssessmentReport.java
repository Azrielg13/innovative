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
import java.awt.Color;
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
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;


public class AssessmentReport extends PDFReport{
	private Appointment appointment;
	
	public AssessmentReport(Appointment appointment) {
		this.appointment = appointment;
	}
	
	public String getTitle() {
		return "Patient Assessment";
	}
	
	@Override
	public Paragraph getBody() throws DocumentException, Exception {
		Paragraph body = new Paragraph("");

		Table datatable = new Table(2);
		datatable.setBorderWidth(0);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setAlignment(Element.ALIGN_CENTER);
		Cell defaultCell = new Cell();
		defaultCell.setBorder(Rectangle.NO_BORDER);
		defaultCell.setHorizontalAlignment(0);
		defaultCell.setColspan(1);
		datatable.setDefaultCell(defaultCell);
		datatable.setAlignment(0);
		datatable.setPadding(1);
		datatable.setSpacing(1);
		int headerwidths[] = {40, 60};
		datatable.setWidths(headerwidths);
		datatable.setWidth(100);
		datatable.endHeaders();

		Patient patient = appointment.getPatient();

		Cell cell = new Cell(new Phrase("Patient Name: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.add(new Phrase(patient.getName(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		cell = new Cell(new Phrase("Pharmacy: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.add(new Phrase(patient.getReferralSource() + "", FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		datatable.addCell(new Phrase(""));
		cell = new Cell(new Phrase("Certification Period: ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		cell.add(new Phrase(formatDate(appointment.getStartDate()) + " to " + formatDate(appointment.getEndDate()), FontFactory.getFont(FontFactory.HELVETICA, 11)));
		datatable.addCell(cell);
		body.add(datatable);
		
		datatable = new Table(12);
		datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(1);
		datatable.setSpacing(1);
		datatable.setWidth(100);
		int[] colspans = new int[]{1, 1, 1, 1, 1, 1, 3, 3,
															 3, 2, 2, 2, 3};
		int c = 0;
		for (GeneralData assessment : GenData.ASS_CAT_VITAL.get().getGeneralDatas()) {
			cell = new Cell(new Phrase(assessment + "\n", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			cell.add(new Phrase(addValue(appointment.getAssessmentValue(assessment)), FontFactory.getFont(FontFactory.HELVETICA, 9)));
			cell.setColspan(colspans[c++]);
			datatable.addCell(cell);
		}
		body.add(datatable);
		
		datatable = new Table(2);
		datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(1);
		datatable.setSpacing(0);
		datatable.setWidth(100);
		for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
			if (GenData.ASS_CAT_VITAL.get() != cat) {
				Paragraph p = new Paragraph("");
				p.setSpacingAfter(0);
				p.setSpacingBefore(0);
				p.setKeepTogether(true);
				p.setLeading(12);
				Phrase phrase = new Phrase(cat + "", new Font(Font.HELVETICA, 11, Font.BOLD));
				p.add(phrase);
				for (GeneralData assessment : cat.getGeneralDatas()) {
					p.add(new Phrase("\n" + assessment + ": ", new Font(Font.HELVETICA, 9, Font.BOLD)));
					p.add(new Phrase(addValue(appointment.getAssessmentValue(assessment)), new Font(Font.HELVETICA, 9, Font.UNDERLINE)));
				}
				datatable.addCell(new Cell(p));
			}
		}
		body.add(datatable);
		datatable = new Table(10);
		datatable.setBorderWidth(0);
		datatable.setBorder(Rectangle.NO_BORDER);
		defaultCell = new Cell();
		defaultCell.setBorder(Rectangle.NO_BORDER);
		defaultCell.setHorizontalAlignment(0);
		defaultCell.setColspan(1);
		datatable.setDefaultCell(defaultCell);
		datatable.setWidth(100);
		cell = new Cell(new Phrase("Nurse Name:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("" + appointment.getNurse(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("Nurse Signature:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("" + appointment.getNurse(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell.setColspan(2);
		datatable.addCell(cell);
		datatable.addCell(new Phrase("Date:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatDate(appointment.getEndDate()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		datatable.addCell(new Phrase("Time In:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatTime(appointment.getTimeIn()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		datatable.addCell(new Phrase("Time Out:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		datatable.addCell(new Phrase(formatTime(appointment.getTimeOut()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
		cell = new Cell(new Phrase("Patient Signature:", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("" + patient, FontFactory.getFont(FontFactory.HELVETICA, 10)));
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
