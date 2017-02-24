package com.digitald4.iis.server;

import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.ServiceServlet;
import com.digitald4.common.storage.DAOStore;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Invoice;
import com.digitald4.iis.proto.IISProtos.License;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISProtos.Patient;
import com.digitald4.iis.proto.IISProtos.Paystub;
import com.digitald4.iis.proto.IISProtos.Vendor;
import com.digitald4.iis.proto.IISUIProtos.AppointmentUI;
import com.digitald4.iis.proto.IISUIProtos.InvoiceUI;
import com.digitald4.iis.proto.IISUIProtos.LicenseUI;
import com.digitald4.iis.proto.IISUIProtos.PatientUI;
import com.digitald4.iis.proto.IISUIProtos.PaystubUI;
import com.digitald4.iis.proto.IISUIProtos.VendorUI;
import com.digitald4.iis.storage.AppointmentStore;

import com.digitald4.iis.storage.NurseDAOProtoSQL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "JSON Service Servlet", urlPatterns = {"/json/*"})
public class JSONServiceServlet extends ServiceServlet {
	
	public void init() throws ServletException {
		super.init();
		DBConnector dbConnector = getDBConnector();

		DAOStore<Nurse> nurseStore = new GenericDAOStore<>(
				new NurseDAOProtoSQL(dbConnector, userStore));
		addService("nurse", new NurseService(nurseStore));

		DAOStore<License> licenseStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(License.class, dbConnector, "V_LICENSE"));
		addService("license", new DualProtoService<>(LicenseUI.class, licenseStore));
		
		DAOStore<Patient> patientStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Patient.class, dbConnector, "V_PATIENT"));
		addService("patient", new DualProtoService<>(PatientUI.class, patientStore));

		AppointmentStore appointmentStore = new AppointmentStore(
				new DAOProtoSQLImpl<>(Appointment.class, dbConnector, "V_APPOINTMENT"));
		addService("appointment", new DualProtoService<>(AppointmentUI.class, appointmentStore));
		
		DAOStore<Vendor> vendorStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Vendor.class, dbConnector));
		addService("vendor", new DualProtoService<>(VendorUI.class, vendorStore));
		
		DAOStore<Invoice> invoiceStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Invoice.class, dbConnector));
		addService("invoice", new DualProtoService<>(InvoiceUI.class, invoiceStore));

		DAOStore<Paystub> paystubStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Paystub.class, dbConnector, "V_PAYSTUB"));
		addService("paystub", new DualProtoService<>(PaystubUI.class, paystubStore));

		addService("notification", new NotificationService(licenseStore, patientStore));
	}
}
