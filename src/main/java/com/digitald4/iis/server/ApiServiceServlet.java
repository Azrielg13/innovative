package com.digitald4.iis.server;

import com.digitald4.common.proto.DD4Protos.Company;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.JSONServiceImpl;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.proto.IISProtos.*;
import com.digitald4.iis.proto.IISUIProtos.*;
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

@WebServlet(name = "API Service Servlet", urlPatterns = {"/api/*"})
public class ApiServiceServlet extends com.digitald4.common.server.ApiServiceServlet {
	private Company company;
	
	public ApiServiceServlet() {
		useViews = true;
		Provider<Company> companyProvider = () -> company;

		NurseStore nurseStore = new NurseStore(daoProvider, userStore);
		addService("nurse", new NurseJSONService(new NurseService(nurseStore)));

		Store<License> licenseStore = new GenericStore<>(License.class, daoProvider);
		addService("license", new JSONServiceImpl<>(new DualProtoService<>(LicenseUI.class, licenseStore), true));
		
		Store<Patient> patientStore = new GenericStore<>(Patient.class, daoProvider);
		addService("patient", new JSONServiceImpl<>(new DualProtoService<>(PatientUI.class, patientStore), true));

		Store<Vendor> vendorStore = new GenericStore<>(Vendor.class, daoProvider);
		addService("vendor", new JSONServiceImpl<>(new DualProtoService<>(VendorUI.class, vendorStore), true));

		AppointmentStore appointmentStore = new AppointmentStore(daoProvider, nurseStore, vendorStore);
		addService("appointment", new JSONServiceImpl<>(new SingleProtoService<>(appointmentStore), true));
		
		Store<Invoice> invoiceStore = new InvoiceStore(
				daoProvider,
				appointmentStore,
				dataFileStore,
				new InvoiceReportCreator(companyProvider, appointmentStore, vendorStore));
		addService("invoice", new JSONServiceImpl<>(new DualProtoService<>(InvoiceUI.class, invoiceStore), true));

		Store<Paystub> paystubStore = new PaystubStore(
				daoProvider,
				appointmentStore,
				dataFileStore,
				new PaystubReportCreator(companyProvider, appointmentStore, nurseStore, generalDataStore));
		addService("paystub", new JSONServiceImpl<>(new SingleProtoService<>(paystubStore), true));

		addService("notification", new NotificationService(licenseStore, patientStore));
	}

	public void init() {
		super.init();
		ServletContext context = getServletContext();
		company = Company.newBuilder()
				.setName(context.getInitParameter("company_name"))
				.build();
	}
}
