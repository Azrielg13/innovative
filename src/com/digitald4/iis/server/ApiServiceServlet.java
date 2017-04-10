package com.digitald4.iis.server;

import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.SingleProtoService;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.GenericStore;
import com.digitald4.iis.proto.IISProtos.*;
import com.digitald4.iis.proto.IISUIProtos.*;
import com.digitald4.iis.report.InvoiceReportCreator;
import com.digitald4.iis.report.PaystubReportCreator;
import com.digitald4.iis.storage.AppointmentStore;
import com.digitald4.iis.storage.InvoiceStore;
import com.digitald4.iis.storage.NurseDAOProtoSQL;

import com.digitald4.iis.storage.PaystubStore;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "API Service Servlet", urlPatterns = {"/api/*"})
public class ApiServiceServlet extends com.digitald4.common.server.ApiServiceServlet {
	
	public ApiServiceServlet() throws ServletException {
		DBConnector dbConnector = getDBConnector();

		Store<Nurse> nurseStore = new GenericStore<>(
				new NurseDAOProtoSQL(dbConnector, userStore));
		addService("nurse", new NurseService(nurseStore));

		Store<License> licenseStore = new GenericStore<>(
				new DAOProtoSQLImpl<>(License.class, dbConnector, "V_LICENSE"));
		addService("license", new DualProtoService<>(LicenseUI.class, licenseStore));
		
		Store<Patient> patientStore = new GenericStore<>(
				new DAOProtoSQLImpl<>(Patient.class, dbConnector, "V_PATIENT"));
		addService("patient", new DualProtoService<>(PatientUI.class, patientStore));

		Store<Vendor> vendorStore = new GenericStore<>(
				new DAOProtoSQLImpl<>(Vendor.class, dbConnector));
		addService("vendor", new DualProtoService<>(VendorUI.class, vendorStore));

		AppointmentStore appointmentStore = new AppointmentStore(
				new DAOProtoSQLImpl<>(Appointment.class, dbConnector, "V_APPOINTMENT"),
				nurseStore,
				vendorStore);
		addService("appointment", new SingleProtoService<>(appointmentStore));
		
		Store<Invoice> invoiceStore = new InvoiceStore(
				new DAOProtoSQLImpl<>(Invoice.class, dbConnector),
				appointmentStore,
				dataFileStore,
				new InvoiceReportCreator(appointmentStore, vendorStore));
		addService("invoice", new DualProtoService<>(InvoiceUI.class, invoiceStore));

		Store<Paystub> paystubStore = new PaystubStore(
				new DAOProtoSQLImpl<>(Paystub.class, dbConnector, "V_PAYSTUB"),
				appointmentStore,
				dataFileStore,
				new PaystubReportCreator(appointmentStore, nurseStore, generalDataStore));
		addService("paystub", new SingleProtoService<>(paystubStore));

		addService("notification", new NotificationService(licenseStore, patientStore));
	}
}
