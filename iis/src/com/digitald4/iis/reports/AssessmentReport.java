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
		Cell defaultCell = new Cell();
		defaultCell.setBorder(Rectangle.NO_BORDER);
		defaultCell.setHorizontalAlignment(0);
		defaultCell.setColspan(1);
		datatable.setDefaultCell(defaultCell);
		datatable.setAlignment(0);
		datatable.setPadding(1);
		datatable.setSpacing(1);
		int headerwidths[] = {50, 50};
		datatable.setWidths(headerwidths);
		datatable.setWidth(100);
		datatable.endHeaders();

		Patient patient = appointment.getPatient();

		datatable.addCell(new Phrase("Patient Name: " + patient.getName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
		datatable.addCell(new Phrase("Pharmacy: " + patient.getReferralSource(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
		datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 12)));
		datatable.addCell(new Phrase("Certification Period: " + formatDate(appointment.getStartDate()) + " to " + formatDate(appointment.getEndDate())));
		body.add(datatable);
		
		datatable = new Table(12);
		datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(1);
		datatable.setSpacing(1);
		datatable.setWidth(100);
		datatable.addCell(new Phrase("B/P", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		datatable.addCell(new Phrase("TEMP\n98.6", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		datatable.addCell(new Phrase("RR", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		datatable.addCell(new Phrase("HR", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		datatable.addCell(new Phrase("HEIGHT", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		datatable.addCell(new Phrase("WEIGHT", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		Cell cell = new Cell(new Phrase("SOC/Recertification/FU", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(3);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("Diagnosis", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(3);
		datatable.addCell(cell);
		
		cell = new Cell(new Phrase("Physician", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(3);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("MD Phone Number", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("Last Visit", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("Order Change", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(2);
		datatable.addCell(cell);
		cell = new Cell(new Phrase("Allergies", FontFactory.getFont(FontFactory.HELVETICA, 9)));
		cell.setColspan(3);
		datatable.addCell(cell);
		body.add(datatable);
		
		datatable = new Table(2);
		datatable.setBorder(Rectangle.BOX);
		datatable.setBorderColor(new Color(0, 0, 0));
		datatable.setPadding(1);
		datatable.setSpacing(1);
		datatable.setWidth(100);
		for (GeneralData cat : GenData.ASS_CAT.get().getGeneralDatas()) {
			Phrase phrase = new Phrase("" + cat);
			datatable.addCell(new Cell(phrase));
		}
		body.add(datatable);

		return body;
	}
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://198.38.82.101/iisosnet_main?autoReconnect=true", "iisosnet_user", "getSchooled85");
		ByteArrayOutputStream buffer = new AssessmentReport(new Appointment().setStart(DateTime.now())
				.setPatient(new Patient().setName("Eddie Mayfield"))
				.setNurse(new Nurse().setUser(new User().setFirstName("Nurse").setLastName("Betty")))
		).createPDF();

		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("Assessment.pdf"));
		System.out.println(buffer.toByteArray().length);
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("Assessment.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}
}
