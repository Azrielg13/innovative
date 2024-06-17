package com.digitald4.iis.server;

import com.digitald4.common.model.Company;
import com.digitald4.common.server.service.JSONServiceHelper;
import com.digitald4.common.storage.*;
import com.digitald4.iis.model.*;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.digitald4.iis.report.PaystubReportCreator;
import com.digitald4.iis.server.NotificationService.NotificationJSONService;
import com.digitald4.iis.server.NurseService.NurseJSONService;
import com.digitald4.iis.storage.*;

import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import java.time.Duration;

@WebServlet(name = "API Service Servlet", urlPatterns = {"/api/*"})
public class ApiServiceServlet extends com.digitald4.common.server.ApiServiceServlet {
	private Company company;
	
	public ApiServiceServlet() {
		useViews = true;
		Provider<Company> companyProvider = () -> company;

		EntityStore entityStore = new EntityStore(daoProvider);
		SessionStore<User> sessionStore =
				new SessionStore<>(daoProvider, userStore, passwordStore, userProvider, Duration.ofHours(8), true, clock);

		LicenseStore licenseStore = new LicenseStore(daoProvider);
		LicenseService licenseService = new LicenseService(licenseStore, sessionStore);
		addService("license", new JSONServiceHelper<>(licenseService));

		NurseStore nurseStore = new NurseStore(daoProvider, licenseStore);
		addService("nurse", new NurseJSONService(new NurseService(nurseStore, sessionStore)));

		LongStore<Vendor> vendorStore = new GenericLongStore<>(Vendor.class, daoProvider);
		addService("vendor", new JSONServiceHelper<>(new VendorService(vendorStore, sessionStore)));

		PatientStore patientStore = new PatientStore(daoProvider, vendorStore);
		addService("patient", new JSONServiceHelper<>(new PatientService(patientStore, sessionStore)));

		ServiceCodeStore serviceCodeStore = new ServiceCodeStore(daoProvider);
		addService("billCode", new JSONServiceHelper<>(new ServiceCodeService(serviceCodeStore, sessionStore, nurseStore)));

		AppointmentStore appointmentStore = new AppointmentStore(daoProvider, serviceCodeStore, clock);
		addService("appointment", new JSONServiceHelper<>(new AppointmentService(appointmentStore, sessionStore)));

		LongStore<Invoice> invoiceStore = new InvoiceStore(
				daoProvider,
				appointmentStore,
				dataFileStore,
				new InvoiceReportCreator(companyProvider, appointmentStore, vendorStore, clock),
				clock);
		addService("invoice", new JSONServiceHelper<>(new AdminService<>(invoiceStore, sessionStore)));

		LongStore<Paystub> paystubStore = new PaystubStore(
				daoProvider,
				appointmentStore,
				nurseStore,
				dataFileStore,
				new PaystubReportCreator(companyProvider, appointmentStore, nurseStore, generalDataStore),
				clock);
		addService("paystub", new JSONServiceHelper<>(new AdminService<>(paystubStore, sessionStore)));

		addService("notification",
				new NotificationJSONService(new NotificationService(licenseStore, patientStore, sessionStore)));
	}

	public void init() {
		super.init();
		ServletContext context = getServletContext();
		company = new Company().setName(context.getInitParameter("company_name"));
	}
}
