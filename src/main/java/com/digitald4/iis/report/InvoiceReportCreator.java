package com.digitald4.iis.report;

import static com.digitald4.common.util.FormatText.*;
import static com.itextpdf.text.FontFactory.getFont;
import static java.util.Comparator.comparing;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.Address;
import com.digitald4.common.model.Company;
import com.digitald4.common.report.PDFReport;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOInMemoryImpl;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.Appointment.AccountingInfo;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Nurse;
import com.digitald4.iis.model.Patient;
import com.digitald4.iis.model.ServiceCode.Unit;
import com.digitald4.iis.model.Vendor;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.VendorStore;
import com.google.common.collect.ImmutableList;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Provider;

public class InvoiceReportCreator extends PDFReport {
	private static final BaseColor LIGHT_BLUE = new BaseColor(235, 244, 250);
	private final AppointmentStore appointmentStore;
	private final VendorStore vendorStore;
	private final Clock clock;
	private volatile Image logo;

	@Inject
	public InvoiceReportCreator(
			Provider<Company> companyProvider,
			AppointmentStore appointmentStore,
			VendorStore vendorStore,
			Clock clock) {
		super(companyProvider);
		this.appointmentStore = appointmentStore;
		this.vendorStore = vendorStore;
		this.clock = clock;
	}

	private Instant getTimestamp() {
		return Instant.ofEpochMilli(clock.millis());
	}

	@Override
	public String getTitle() {
		return "Invoice\n";
	}
	
	@Override
	public Rectangle getPageSize() {
		return PageSize.A4;
	}

	public ByteArrayOutputStream createPDF(Invoice invoice) {
		return createPDF(ImmutableList.of(invoice));
	}

	private Image getLogoImage() throws IOException, BadElementException {
		if (logo == null) {
			synchronized (this) {
				if (logo == null) {
					logo = Image.getInstance(
							new URL("https://ip360-179401.appspot.com/images/infusion-partners-360-logo-on-white.png"));
				}
			}
		}
		return logo;
	}

	public ByteArrayOutputStream createPDF(Iterable<Invoice> invoices) {
		try {
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
			document.setMargins(25, 25, 50, 50);
			for (Invoice invoice : invoices) {
				document.newPage();
				document.add(getBody(invoice));
			}
			document.close();
			return buffer;
		} catch (DocumentException | IOException e) {
			throw new DD4StorageException("Error creating PDF", e, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Paragraph getBody() {
		return null;
	}

	private Paragraph getBody(Invoice invoice) throws DD4StorageException, DocumentException, IOException {
		Vendor vendor = vendorStore.get(invoice.getVendorId());
		Paragraph body = new Paragraph();
		body.add(new Phrase("INVOICE\n", getFont(FontFactory.HELVETICA, 12, new BaseColor(0, 119, 197))));

		Image logo = getLogoImage();
		logo.setAbsolutePosition(320, 712);
		body.add(logo);

		PdfPTable headerTable = new PdfPTable(2);
		headerTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		headerTable.setWidthPercentage(50);
		headerTable.setSpacingAfter(40);

		Paragraph nameAddress = new Paragraph();
		nameAddress.add(new Phrase(getCompany().getName() + "\n", getFont(FontFactory.HELVETICA, 9, FontStyle.BOLD.ordinal())));
		nameAddress.add(new Phrase(getCompany().getAddress() + "\n", getFont(FontFactory.HELVETICA, 9)));
		PdfPCell nameAddressCell = new PdfPCell(nameAddress);
		nameAddressCell.setBorder(PdfPCell.NO_BORDER);
		headerTable.addCell(nameAddressCell);

		PdfPCell contactCell = new PdfPCell(new Phrase(
				String.format("%s\n%s\n%s", getCompany().getEmail(), getCompany().getPhone(), getCompany().getWebsite()),
				getFont(FontFactory.HELVETICA, 9)));
		contactCell.setBorder(PdfPCell.NO_BORDER);
		headerTable.addCell(contactCell);
		body.add(headerTable);

		PdfPTable labelTable = new PdfPTable(2);
		labelTable.setWidthPercentage(100);
		labelTable.setWidths(new int[]{50, 50});
		labelTable.setSpacingAfter(20);

		Paragraph billTo = new Paragraph();
		billTo.add(phrase(vendor.getName() + "\n", getFont(FontFactory.HELVETICA, 10, new BaseColor(0, 119, 197))));
		billTo.add(phrase("Bill to\n", getFont(FontFactory.HELVETICA, 9, FontStyle.BOLD.ordinal())));
		billTo.add(phrase(vendor.getName() + "\n" + vendor.getAddress().getAddress(), getFont(FontFactory.HELVETICA, 9)));
		PdfPCell billToCell = new PdfPCell(billTo);
		billToCell.setBorder(PdfPCell.BOTTOM);
		billToCell.setBackgroundColor(LIGHT_BLUE);
		labelTable.addCell(billToCell);

		Paragraph shipTo = new Paragraph();
		shipTo.add(new Phrase("\n", getFont(FontFactory.HELVETICA, 10)));
		shipTo.add(new Phrase("Ship to\n", getFont(FontFactory.HELVETICA, 9, FontStyle.BOLD.ordinal())));
		shipTo.add(new Phrase(vendor.getName() + "\n" + vendor.getAddress().getAddress(), getFont(FontFactory.HELVETICA, 9)));
		PdfPCell shipToCell = new PdfPCell(shipTo);
		shipToCell.setBorder(PdfPCell.BOTTOM);
		shipToCell.setBackgroundColor(LIGHT_BLUE);
		labelTable.addCell(shipToCell);

		Paragraph details = new Paragraph();
		details.add(new Phrase("Invoice details\n", getFont(FontFactory.HELVETICA, 9, FontStyle.BOLD.ordinal())));
		details.add(new Phrase("Invoice no: " + invoice.getId() + "\n", getFont(FontFactory.HELVETICA, 9)));
		details.add(new Phrase("Terms: " + vendor.getTerms() + "\n", getFont(FontFactory.HELVETICA, 9)));
		details.add(new Phrase("Invoice date: " + formatDate(invoice.getDate(), FormatText.USER_DATE) + "\n", getFont(FontFactory.HELVETICA, 9)));
		details.add(new Phrase("Due date: " + formatDate(invoice.getDueDate(), FormatText.USER_DATE) + "\n", getFont(FontFactory.HELVETICA, 9)));
		PdfPCell detailsCell = new PdfPCell(details);
		detailsCell.setBorder(PdfPCell.NO_BORDER);
		detailsCell.setBackgroundColor(LIGHT_BLUE);
		labelTable.addCell(detailsCell);

		PdfPCell blankCell = new PdfPCell();
		blankCell.setBorder(PdfPCell.NO_BORDER);
		blankCell.setBackgroundColor(LIGHT_BLUE);
		labelTable.addCell(blankCell);
		body.add(labelTable);


		PdfPTable datatable = new PdfPTable(7);
		datatable.setWidthPercentage(100);
		datatable.setWidths(new int[]{5,15,20,30,10,10,10});
		datatable.setSpacingAfter(10);

		datatable.addCell(createCell("#"));
		datatable.addCell(createCell("Date"));
		datatable.addCell(createCell("Product or service"));
		datatable.addCell(createCell("Description"));
		datatable.addCell(createCell("Qty"));
		datatable.addCell(createCell("Rate"));
		datatable.addCell(createCell("Amount"));
		AtomicInteger index = new AtomicInteger();
		appointmentStore.get(invoice.getAppointmentIds()).getItems().stream()
				.sorted(comparing(Appointment::getDate).thenComparing(Appointment::getTimeIn).thenComparing(Appointment::getTimeOut))
				.forEach(app -> {
					AccountingInfo billingInfo = app.getBillingInfo();
					if (billingInfo.mileageTotal() > 0) {
						datatable.addCell(createCell(String.valueOf(index.incrementAndGet())));
						datatable.addCell(createCell(formatDate(app.getDate(), USER_DATE)));
						datatable.addCell(createCell("Mileage", FontStyle.BOLD));
						datatable.addCell(createCell("Mileage"));
						datatable.addCell(createCell(String.valueOf(billingInfo.getMileage())));
						datatable.addCell(createCell(formatCurrency(billingInfo.getMileageRate())));
						datatable.addCell(createCell(formatCurrency(billingInfo.mileageTotal())));
					}
					String serviceCode = billingInfo.getServiceCode();
					serviceCode = serviceCode.substring(serviceCode.indexOf('-') + 1);
					datatable.addCell(createCell(String.valueOf(index.incrementAndGet())));
					datatable.addCell(createCell(formatDate(app.getDate(), USER_DATE)));
					datatable.addCell(createCell(serviceCode, FontStyle.BOLD));
					datatable.addCell(createCell(String.format("%s - %s %s %s",
							formatTime(app.getTimeIn()), formatTime(app.getTimeOut()), app.getPatientName(), app.getNurseName())));
					datatable.addCell(createCell(String.valueOf(billingInfo.getUnitCount())));
					datatable.addCell(createCell(formatCurrency(billingInfo.getUnitRate())));
					datatable.addCell(createCell(formatCurrency(billingInfo.subTotal())));
				});
		body.add(datatable);

		PdfPTable totalTable = new PdfPTable(2);
		totalTable.setWidthPercentage(25);
		totalTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
		PdfPCell totalLabel =
				new PdfPCell(new Phrase("Total", getFont(FontFactory.HELVETICA, 11, FontStyle.BOLD.ordinal())));
		totalLabel.setBorder(PdfPCell.BOTTOM);
		totalTable.addCell(totalLabel);

		PdfPCell totalCell = new PdfPCell(new Phrase(
				formatCurrency(invoice.getTotalDue()), getFont(FontFactory.HELVETICA, 11, FontStyle.BOLD.ordinal())));
		totalCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		totalCell.setBorder(PdfPCell.BOTTOM);
		totalTable.addCell(totalCell);
		body.add(totalTable);

		return body;
	}

	public static Phrase phrase(String text, Font font) {
		Phrase phrase = new Phrase(text, font);
		phrase.setMultipliedLeading(2f);
		return phrase;
	}

	public static PdfPCell createCell(String text) {
		return createCell(new Phrase(text, getFont(FontFactory.HELVETICA, 9)));
	}

	public static PdfPCell createCell(String text, FontStyle fontStyle) {
		return createCell(new Phrase(text, getFont(FontFactory.HELVETICA, 9, fontStyle.ordinal())));
	}

	public static PdfPCell createCell(Phrase phrase) {
		PdfPCell cell = new PdfPCell(phrase);
		cell.setBorder(PdfPCell.BOTTOM);
		return cell;
	}

	public static void main(String[] args) throws DocumentException, IOException {
		DAO dao = new DAOInMemoryImpl();
		dao.create(new Vendor().setId(123L).setName("Integrated Care System").setMileageRate(.58)
				.setAddress(new Address().setAddress("8989 W Pun Ct Viahoe, CA 92121").setUnit("A8")));
		dao.create(new Nurse().setId(234L).setFirstName("John").setLastName("Doe"));
		dao.create(new Patient().setId(345L).setFirstName("Jane").setLastName("Doe").setBillingVendorId(123L));
		Clock clock = Clock.systemDefaultZone();
		AppointmentStore appointmentStore = new AppointmentStore(() -> dao, null, clock);
		Appointment appointment = new Appointment().setId(1234L).setDate(Instant.parse("2024-06-05T00:00:00.00Z"))
				.setTimeIn(Instant.parse("2019-10-01T17:00:00.00Z")).setTimeOut(Instant.parse("2019-10-01T23:45:00.00Z"))
				.setLoggedHours(6.75).setPatientId(345L).setNurseId(234L).setMileage(222);
		appointment.setBillingInfo(new AccountingInfo().setServiceCode("GPI - 95")
				.setUnitCount(6.75).setUnit(Unit.Hour).setUnitRate(95).setMileageRate(.58).setMileage(222));
		appointmentStore.create(appointment);
		Invoice invoice = (Invoice) new Invoice().setId(29587L).setVendorId(123L)
				.setAppointmentIds(ImmutableList.of(1234L)).setTotalDue(770.01).setCreationTime(clock.instant());
		ByteArrayOutputStream buffer = new InvoiceReportCreator(
				() -> new Company().setName("Infusion Partners 360, LLC").setAddress("7056 Archibald 102-375\nCorona, CA 92880")
						.setEmail("info@infusionpartners360.com")
						.setPhone("(866) 714-3955")
						.setWebsite("www.infusionpartners360.com"),
				appointmentStore, new VendorStore(() -> dao), clock).createPDF(ImmutableList.of(invoice, invoice));

		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("data/Invoice-Sample.pdf"));
		System.out.println(buffer.toByteArray().length);
		output.write(buffer.toByteArray());
		output.close();
		File file = new File("data/Invoice-Sample.pdf");
		Desktop.getDesktop().open(file);
		System.exit(0);
	}
}
