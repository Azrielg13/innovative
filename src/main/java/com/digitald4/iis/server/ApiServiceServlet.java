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
		Provider<User> userProvider1 = () -> (User) userProvider.get();

		SequenceStore sequenceStore = new SequenceStore(daoProvider);

		EntityStore entityStore = new EntityStore(daoProvider);
		SessionStore<User> sessionStore =
				new SessionStore<>(daoProvider, userStore, passwordStore, userProvider, Duration.ofHours(8), true, clock);

		LicenseStore licenseStore = new LicenseStore(daoProvider, userProvider1);
		LicenseService licenseService = new LicenseService(licenseStore, sessionStore);
		addService("license", new JSONServiceHelper<>(licenseService));

		NurseStore nurseStore = new NurseStore(daoProvider, licenseStore);
		addService("nurse", new NurseJSONService(new NurseService(nurseStore, sessionStore)));

		VendorStore vendorStore = new VendorStore(daoProvider);
		addService("vendor", new JSONServiceHelper<>(new VendorService(vendorStore, sessionStore)));

		ReferralResponseStore referralResponseStore = new ReferralResponseStore(daoProvider);
		addService("referralResponseStore", new JSONServiceHelper<>(new ReferralResponseService(referralResponseStore, sessionStore)));

		PatientStore patientStore = new PatientStore(daoProvider, clock);
		addService("patient", new JSONServiceHelper<>(
				new PatientService(patientStore, sessionStore, userProvider1, referralResponseStore)));

		ServiceCodeStore serviceCodeStore = new ServiceCodeStore(daoProvider);
		addService("billCode", new JSONServiceHelper<>(new ServiceCodeService(serviceCodeStore, sessionStore)));

		AppointmentStore appointmentStore = new AppointmentStore(daoProvider, userProvider1, serviceCodeStore, clock);
		addService("appointment",
				new JSONServiceHelper<>(new AppointmentService(appointmentStore, sessionStore, sequenceStore)));

		LongStore<Invoice> invoiceStore = new InvoiceStore(
				daoProvider,
				appointmentStore,
				dataFileStore,
				new InvoiceReportCreator(companyProvider, appointmentStore, vendorStore, clock),
				null,
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
