package com.digitald4.iis.server;

import com.digitald4.common.proto.DD4UIProtos;
import com.digitald4.common.storage.DAOProtoSQLImpl;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.proto.DD4Protos.GeneralData;
import com.digitald4.common.proto.DD4Protos.User;
import com.digitald4.common.proto.DD4UIProtos.CreateRequest;
import com.digitald4.common.proto.DD4UIProtos.DeleteRequest;
import com.digitald4.common.proto.DD4UIProtos.GeneralDataUI;
import com.digitald4.common.proto.DD4UIProtos.GetRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.UpdateRequest;
import com.digitald4.common.proto.DD4UIProtos.UserUI;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.ProtoService;
import com.digitald4.common.server.ServiceServlet;
import com.digitald4.common.storage.DAOStore;
import com.digitald4.common.storage.GenericDAOStore;
import com.digitald4.common.storage.UserStore;
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
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.digitald4.iis.proto.IISUIProtos.PatientUI;
import com.digitald4.iis.proto.IISUIProtos.PaystubUI;
import com.digitald4.iis.proto.IISUIProtos.VendorUI;
import com.digitald4.iis.store.AppointmentStore;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "JSON Service Servlet", urlPatterns = {"/json/*"})
public class JSONServiceServlet extends ServiceServlet {
	public enum ACTIONS {GENERAL_DATA, GENERAL_DATAS,
			NURSE, NURSES, CREATE_NURSE, UPDATE_NURSE, DELETE_NURSE,
			LICENSE, LICENSES, CREATE_LICENSE, UPDATE_LICENSE, DELETE_LICENSE,
			APPOINTMENT, APPOINTMENTS, CREATE_APPOINTMENT, UPDATE_APPOINTMENT, DELETE_APPOINTMENT,
			PATIENT, PATIENTS, CREATE_PATIENT, UPDATE_PATIENT, DELETE_PATIENT,
			USER, USERS, CREATE_USER, UPDATE_USER, DELETE_USER,
			VENDOR, VENDORS, CREATE_VENDOR, UPDATE_VENDOR, DELETE_VENDOR,
			INVOICE, INVOICES, CREATE_INVOICE, UPDATE_INVOICE, DELETE_INVOICE,
			PAYSTUB, PAYSTUBS, CREATE_PAYSTUB, UPDATE_PAYSTUB, DELETE_PAYSTUB};

	private ProtoService<GeneralDataUI> generalDataService;
	private ProtoService<NurseUI> nurseService;
	private ProtoService<LicenseUI> licenseService;
	private ProtoService<AppointmentUI> appointmentService;
	private ProtoService<PatientUI> patientService;
	private ProtoService<UserUI> userService;
	private ProtoService<VendorUI> vendorService;
	private ProtoService<InvoiceUI> invoiceService;
	private ProtoService<PaystubUI> paystubService;
	
	public void init() throws ServletException {
		super.init();
		DBConnector dbConnector = getDBConnector();

		generalDataService = new DualProtoService<>(GeneralDataUI.class, new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(GeneralData.class, dbConnector, null, "general_data")));

		DAOStore<Nurse> nurseStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Nurse.class, dbConnector, "V_NURSE"));
		nurseService = new DualProtoService<>(NurseUI.class, nurseStore);

		DAOStore<License> licenseStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(License.class, dbConnector, "V_LICENSE"));
		licenseService = new DualProtoService<>(LicenseUI.class, licenseStore);
		
		DAOStore<Patient> patientStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Patient.class, dbConnector, "V_PATIENT"));
		patientService = new DualProtoService<>(PatientUI.class, patientStore);
		
		DAOStore<Appointment> appointmentStore = new AppointmentStore(
				new DAOProtoSQLImpl<>(Appointment.class, dbConnector, "V_APPOINTMENT"));
		appointmentService = new DualProtoService<>(AppointmentUI.class, appointmentStore);
		
		UserStore userStore = new UserStore(new DAOProtoSQLImpl<>(User.class, dbConnector));
		userService = new DualProtoService<>(UserUI.class, userStore);
		
		DAOStore<Vendor> vendorStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Vendor.class, dbConnector));
		vendorService = new DualProtoService<>(VendorUI.class, vendorStore);
		
		DAOStore<Invoice> invoiceStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Invoice.class, dbConnector));
		invoiceService = new DualProtoService<>(InvoiceUI.class, invoiceStore);

		DAOStore<Paystub> paystubStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Paystub.class, dbConnector));
		paystubService = new DualProtoService<>(PaystubUI.class, invoiceStore);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			JSONObject json = new JSONObject();
			try {
				String action = request.getRequestURL().toString();
				action = action.substring(action.lastIndexOf("/") + 1).toUpperCase();
				if (action.equals("LOGIN")) {
					json.put("data", handleLogin(request, response,
							transformJSONRequest(DD4UIProtos.LoginRequest.getDefaultInstance(), request)));
				} else {
					if (!checkLogin(request, response)) return;
					switch (ACTIONS.valueOf(action)) {
						case GENERAL_DATA:
							json.put("data", convertToJSON(generalDataService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case GENERAL_DATAS:
							json.put("data", convertToJSON(generalDataService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case NURSE:
							json.put("data", convertToJSON(nurseService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case NURSES:
							json.put("data", convertToJSON(nurseService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_NURSE:
							json.put("data", convertToJSON(nurseService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_NURSE:
							json.put("data", convertToJSON(nurseService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_NURSE:
							json.put("data", convertToJSON(nurseService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case LICENSE:
							json.put("data", convertToJSON(licenseService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case LICENSES:
							json.put("data", convertToJSON(licenseService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_LICENSE:
							json.put("data", convertToJSON(licenseService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_LICENSE:
							json.put("data", convertToJSON(licenseService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_LICENSE:
							json.put("data", convertToJSON(licenseService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case APPOINTMENT:
							json.put("data", convertToJSON(appointmentService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case APPOINTMENTS:
							json.put("data", convertToJSON(appointmentService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_APPOINTMENT:
							json.put("data", convertToJSON(appointmentService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_APPOINTMENT:
							json.put("data", convertToJSON(appointmentService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_APPOINTMENT:
							json.put("data", convertToJSON(appointmentService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case PATIENT:
							json.put("data", convertToJSON(patientService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case PATIENTS:
							json.put("data", convertToJSON(patientService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_PATIENT:
							json.put("data", convertToJSON(patientService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_PATIENT:
							json.put("data", convertToJSON(patientService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_PATIENT:
							json.put("data", convertToJSON(patientService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case USER:
							json.put("data", convertToJSON(userService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case USERS:
							json.put("data", convertToJSON(userService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_USER:
							json.put("data", convertToJSON(userService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_USER:
							json.put("data", convertToJSON(userService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_USER:
							json.put("data", convertToJSON(userService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case VENDOR:
							json.put("data", convertToJSON(vendorService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case VENDORS:
							json.put("data", convertToJSON(vendorService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_VENDOR:
							json.put("data", convertToJSON(vendorService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_VENDOR:
							json.put("data", convertToJSON(vendorService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_VENDOR:
							json.put("data", convertToJSON(vendorService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case INVOICE:
							json.put("data", convertToJSON(invoiceService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case INVOICES:
							json.put("data", convertToJSON(invoiceService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_INVOICE:
							json.put("data", convertToJSON(invoiceService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_INVOICE:
							json.put("data", convertToJSON(invoiceService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_INVOICE:
							json.put("data", convertToJSON(invoiceService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
						case PAYSTUB:
							json.put("data", convertToJSON(paystubService.get(
									transformJSONRequest(GetRequest.getDefaultInstance(), request))));
							break;
						case PAYSTUBS:
							json.put("data", convertToJSON(paystubService.list(
									transformJSONRequest(ListRequest.getDefaultInstance(), request))));
							break;
						case CREATE_PAYSTUB:
							json.put("data", convertToJSON(paystubService.create(
									transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
							break;
						case UPDATE_PAYSTUB:
							json.put("data", convertToJSON(paystubService.update(
									transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
							break;
						case DELETE_PAYSTUB:
							json.put("data", convertToJSON(paystubService.delete(
									transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
							break;
					}
				}
				json.put("valid", true);
			} catch (Exception e) {
				json.put("valid", false)
						.put("error", e.getMessage())
						.put("stackTrace", formatStackTrace(e))
						.put("requestParams", "" + request.getParameterMap().keySet())
						.put("queryString", request.getQueryString());
				e.printStackTrace();
			} finally {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().println(json);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		process(request, response);
	}
}
