package com.digitald4.common.report;

import java.io.ByteArrayOutputStream;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.digitald4.common.model.Company;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public abstract class PDFReport {
	private Image logo;
	public PDFReport() {
	}
	
	public abstract String getTitle();
	
	public String getSubject() {
		return getTitle();
	}
	
	public String getAuthor() {
		return "Digital D4";
	}
	
	public PDFReport setLogo(Image logo) {
		this.logo = logo;
		return this;
	}
	
	public Image getLogo() {
		return logo;
	}
	
	public ByteArrayOutputStream createPDF() throws Exception {
		Document document = new Document(PageSize.A4, 25, 25, 25, 25);
		document.addAuthor(getAuthor());
		document.addSubject(getSubject());
		document.addTitle(getTitle());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, buffer);
		document.open();
		document.resetHeader();
		document.setHeader(getHeader());
		document.setFooter(getFooter());
		document.setPageSize(PageSize.A4);
		document.setMargins(25,25,25,25);
		document.newPage();
		document.add(getBody());
		document.close();
		return buffer;
	}
	
	public HeaderFooter getHeader() {
		String DATE_FORMAT = "MM/dd/yyyy";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());

		String website = "";

		Paragraph companyPara = new Paragraph("");
		
		Image logo = getLogo();
		if (logo != null) {
			logo.setAlignment(Image.LEFT);
			//gif.setAbsolutePosition(10, 10);
			logo.setAlignment(Image.LEFT | Image.UNDERLYING);
			logo.scaleAbsolute(100, 100);
			companyPara.add(logo);
		}

		Company company = Company.get();
		if (company != null) {
			companyPara.setAlignment(Element.ALIGN_CENTER);

			if (company.getName() != null && company.getName().length() > 0)
				companyPara.add(new Chunk(""+company.getName(), new Font(Font.HELVETICA, 20, Font.BOLD)));

			if (company.getSlogan() != null && company.getSlogan().length() > 0)
				companyPara.add(new Chunk("\n"+company.getSlogan(), new Font(Font.HELVETICA, 10, Font.ITALIC)));

			if (company.getAddress() != null && company.getAddress().length() > 0)
				companyPara.add(new Chunk("\n"+company.getAddress(), new Font(Font.HELVETICA, 10)));

			if (company.getPhone() != null && company.getPhone().length() > 0)
				companyPara.add(new Chunk("\n Office "+company.getPhone(), new Font(Font.HELVETICA, 10)));

			if (company.getFax() != null && company.getFax().length() > 0)
				companyPara.add(new Chunk("\n Fax "+company.getFax(), new Font(Font.HELVETICA, 10)));

			if (company.getWebsite() != null && company.getWebsite().length() > 0) {
				website = company.getWebsite();
				companyPara.add(new Chunk("\n"+website, new Font(Font.HELVETICA, 10, Font.BOLD)));
			}
		}
		HeaderFooter header = new HeaderFooter(companyPara, true);
		header.setBorder(Rectangle.NO_BORDER);
		return header;
	}
	
	public abstract Paragraph getBody() throws DocumentException, Exception;
	
	public HeaderFooter getFooter() {
		Paragraph paragraph = new Paragraph();
		Company company = Company.get();
		if (company != null) {
			paragraph.add(new Phrase(company.getReportFooter(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
		}
		paragraph.add(new Phrase("Generated: " + DateTime.now(), FontFactory.getFont(FontFactory.HELVETICA, 8)));;
		HeaderFooter footer = new HeaderFooter(paragraph, true);
		footer.setBorder(Rectangle.NO_BORDER);
		return footer;
	}
}
