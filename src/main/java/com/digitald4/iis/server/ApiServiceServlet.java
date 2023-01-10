package com.digitald4.iis.server;

import com.digitald4.common.model.Company;
import com.digitald4.common.server.service.JSONServiceHelper;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.SessionStore;
import com.digitald4.common.storage.LongStore;
import com.digitald4.iis.model.*;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.digitald4.iis.report.PaystubReportCreator;
import com.digitald4.iis.server.NurseService.NurseJSONService;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.InvoiceStore;
import com.digitald4.iis.storage.NurseStore;
import com.digitald4.iis.storage.PaystubStore;
import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import java.time.Clock;
import java.time.Duration;

@WebServlet(name = "API Service Servlet", urlPatterns = {"/api/*"})
public class ApiServiceServlet extends com.digitald4.common.server.ApiServiceServlet {
	private Company company;
	
	public ApiServiceServlet() {
		useViews = true;
		Provider<Company> companyProvider = () -> company;
		Clock clock = Clock.systemUTC();

		SessionStore<User> sessionStore =
				new SessionStore<>(daoProvider, userStore, passwordStore, userProvider, Duration.ofMinutes(30), true, clock);

		NurseStore nurseStore = new NurseStore(daoProvider);
		addService("nurse", new NurseJSONService(new NurseService(nurseStore, sessionStore)));

		LongStore<License> licenseStore = new GenericLongStore<>(License.class, daoProvider);
		addService("license", new JSONServiceHelper<>(new LicenseService(licenseStore, sessionStore)));

		LongStore<Patient> patientStore = new GenericLongStore<>(Patient.class, daoProvider);
		addService("patient", new JSONServiceHelper<>(new PatientService(patientStore, sessionStore)));

		LongStore<Vendor> vendorStore = new GenericLongStore<>(Vendor.class, daoProvider);
		addService("vendor", new JSONServiceHelper<>(new VendorService(vendorStore, sessionStore)));

		AppointmentStore appointmentStore = new AppointmentStore(daoProvider, patientStore, nurseStore, vendorStore, clock);
		addService("appointment", new JSONServiceHelper<>(new AdminService<>(appointmentStore, sessionStore)));

		LongStore<Invoice> invoiceStore = new InvoiceStore(
				daoProvider,
				appointmentStore,
				dataFileStore,
				new InvoiceReportCreator(companyProvider, appointmentStore, vendorStore));
		addService("invoice", new JSONServiceHelper<>(new AdminService<>(invoiceStore, sessionStore)));

		LongStore<Paystub> paystubStore = new PaystubStore(
				daoProvider,
				appointmentStore,
				dataFileStore,
				new PaystubReportCreator(companyProvider, appointmentStore, nurseStore, generalDataStore));
		addService("paystub", new JSONServiceHelper<>(new AdminService<>(paystubStore, sessionStore)));

		addService(
				"notification",
				new NotificationService.NotificationJSONService(new NotificationService(licenseStore, patientStore)));
	}

	public void init() {
		super.init();
		ServletContext context = getServletContext();
		company = new Company().setName(context.getInitParameter("company_name"));
	}
}
