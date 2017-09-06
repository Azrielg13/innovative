package com.digitald4.iis.server;

import com.digitald4.common.proto.DD4Protos.Company;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.DAOConnectorImpl;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.common.util.Provider;
import com.digitald4.iis.proto.IISProtos.*;
import com.digitald4.iis.proto.IISUIProtos.*;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.digitald4.iis.report.PaystubReportCreator;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.InvoiceStore;
import com.digitald4.iis.storage.NurseStore;

import com.digitald4.iis.storage.PaystubStore;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "API Service Servlet", urlPatterns = {"/api/*"})
public class ApiServiceServlet extends com.digitald4.common.server.ApiServiceServlet {
	private Company company;
	
	public ApiServiceServlet() throws ServletException {
		Provider<Company> companyProvider = () -> company;

		NurseStore nurseStore = new NurseStore(new DAOConnectorImpl<>(Nurse.class, dataConnectorProvider), userStore);
		addService("nurse", new NurseService(nurseStore));

		Store<License> licenseStore = new GenericStore<>(new DAOConnectorImpl<>(License.class, dataConnectorProvider));
		addService("license", new DualProtoService<>(LicenseUI.class, licenseStore));
		
		Store<Patient> patientStore = new GenericStore<>(new DAOConnectorImpl<>(Patient.class, dataConnectorProvider));
		addService("patient", new DualProtoService<>(PatientUI.class, patientStore));

		Store<Vendor> vendorStore = new GenericStore<>(new DAOConnectorImpl<>(Vendor.class, dataConnectorProvider));
		addService("vendor", new DualProtoService<>(VendorUI.class, vendorStore));

		AppointmentStore appointmentStore = new AppointmentStore(
				new DAOConnectorImpl<>(Appointment.class, dataConnectorProvider),
				nurseStore,
				vendorStore);
		addService("appointment", new SingleProtoService<>(appointmentStore));
		
		Store<Invoice> invoiceStore = new InvoiceStore(
				new DAOConnectorImpl<>(Invoice.class, dataConnectorProvider),
				appointmentStore,
				dataFileStore,
				new InvoiceReportCreator(companyProvider, appointmentStore, vendorStore));
		addService("invoice", new DualProtoService<>(InvoiceUI.class, invoiceStore));

		Store<Paystub> paystubStore = new PaystubStore(
				new DAOConnectorImpl<>(Paystub.class, dataConnectorProvider),
				appointmentStore,
				dataFileStore,
				new PaystubReportCreator(companyProvider, appointmentStore, nurseStore, generalDataStore));
		addService("paystub", new SingleProtoService<>(paystubStore));

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
