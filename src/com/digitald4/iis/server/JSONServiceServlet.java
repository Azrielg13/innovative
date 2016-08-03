package com.digitald4.iis.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.common.dao.sql.DAOProtoSQLImpl;
import com.digitald4.common.jdbc.DBConnector;
import com.digitald4.common.proto.DD4Protos.User;
import com.digitald4.common.proto.DD4UIProtos.CreateRequest;
import com.digitald4.common.proto.DD4UIProtos.DeleteRequest;
import com.digitald4.common.proto.DD4UIProtos.GetRequest;
import com.digitald4.common.proto.DD4UIProtos.ListRequest;
import com.digitald4.common.proto.DD4UIProtos.UpdateRequest;
import com.digitald4.common.proto.DD4UIProtos.UserUI;
import com.digitald4.common.server.DualProtoService;
import com.digitald4.common.server.ProtoService;
import com.digitald4.common.server.ServiceServlet;
import com.digitald4.common.store.DAOStore;
import com.digitald4.common.store.impl.GenericDAOStore;
import com.digitald4.common.store.impl.UserStore;
import com.digitald4.iis.dao.NurseSQLDao;
import com.digitald4.iis.proto.IISProtos.Appointment;
import com.digitald4.iis.proto.IISProtos.Invoice;
import com.digitald4.iis.proto.IISProtos.Nurse;
import com.digitald4.iis.proto.IISProtos.Patient;
import com.digitald4.iis.proto.IISProtos.Vendor;
import com.digitald4.iis.proto.IISUIProtos.AppointmentUI;
import com.digitald4.iis.proto.IISUIProtos.InvoiceUI;
import com.digitald4.iis.proto.IISUIProtos.NurseUI;
import com.digitald4.iis.proto.IISUIProtos.PatientUI;
import com.digitald4.iis.proto.IISUIProtos.VendorUI;

@WebServlet(name = "JSON Service Servlet", urlPatterns = {"/json/*"})
public class JSONServiceServlet extends ServiceServlet {
	public enum ACTIONS {NURSE, NURSES, CREATE_NURSE, UPDATE_NURSE, DELETE_NURSE,
			APPOINTMENT, APPOINTMENTS, CREATE_APPOINTMENT, UPDATE_APPOINTMENT, DELETE_APPOINTMENT,
			PATIENT, PATIENTS, CREATE_PATIENT, UPDATE_PATIENT, DELETE_PATIENT,
			USER, USERS, CREATE_USER, UPDATE_USER, DELETE_USER,
			VENDOR, VENDORS, CREATE_VENDOR, UPDATE_VENDOR, DELETE_VENDOR,
			INVOICE, INVOICES, CREATE_INVOICE, UPDATE_INVOICE, DELETE_INVOICE};

	private ProtoService<NurseUI> nurseService;
	private ProtoService<AppointmentUI> appointmentService;
	private ProtoService<PatientUI> patientService;
	private ProtoService<UserUI> userService;
	private ProtoService<VendorUI> vendorService;
	private ProtoService<InvoiceUI> invoiceService;
	
	public void init() throws ServletException {
		super.init();
		DBConnector dbConnector = getDBConnector();
		
		DAOStore<Nurse> nurseStore = new GenericDAOStore<>(new NurseSQLDao(dbConnector));
		nurseService = new DualProtoService<>(NurseUI.class, nurseStore);
		
		DAOStore<Patient> patientStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Patient.class, dbConnector));
		patientService = new DualProtoService<>(PatientUI.class, patientStore);
		
		DAOStore<Appointment> appointmentStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Appointment.class, dbConnector));
		appointmentService = new DualProtoService<>(AppointmentUI.class, appointmentStore);
		
		UserStore userStore = new UserStore(new DAOProtoSQLImpl<>(User.class, dbConnector));
		userService = new DualProtoService<>(UserUI.class, userStore);
		
		DAOStore<Vendor> vendorStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Vendor.class, dbConnector));
		vendorService = new DualProtoService<>(VendorUI.class, vendorStore);
		
		DAOStore<Invoice> invoiceStore = new GenericDAOStore<>(
				new DAOProtoSQLImpl<>(Invoice.class, dbConnector));
		invoiceService = new DualProtoService<>(InvoiceUI.class, invoiceStore);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			JSONObject json = new JSONObject();
			try {
				if (!checkLoginAutoRedirect(request, response)) return;
				String action = request.getRequestURL().toString();
				action = action.substring(action.lastIndexOf("/") + 1).toUpperCase();
				switch (ACTIONS.valueOf(action)) {
					case NURSE: json.put("data", convertToJSON(nurseService.get(
							transformJSONRequest(GetRequest.getDefaultInstance(), request))));
					break;
					case NURSES: json.put("data", convertToJSON(nurseService.list(
							transformJSONRequest(ListRequest.getDefaultInstance(), request))));
					break;
					case CREATE_NURSE: json.put("data", convertToJSON(nurseService.create(
							transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
					break;
					case UPDATE_NURSE: json.put("data", convertToJSON(nurseService.update(
							transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
					break;
					case DELETE_NURSE: json.put("data", convertToJSON(nurseService.delete(
							transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
					break;
					case APPOINTMENT: json.put("data", convertToJSON(appointmentService.get(
							transformJSONRequest(GetRequest.getDefaultInstance(), request))));
					break;
					case APPOINTMENTS: json.put("data", convertToJSON(appointmentService.list(
							transformJSONRequest(ListRequest.getDefaultInstance(), request))));
					break;
					case CREATE_APPOINTMENT: json.put("data", convertToJSON(appointmentService.create(
							transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
					break;
					case UPDATE_APPOINTMENT: json.put("data", convertToJSON(appointmentService.update(
							transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
					break;
					case DELETE_APPOINTMENT: json.put("data", convertToJSON(appointmentService.delete(
							transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
					break;
					case PATIENT: json.put("data", convertToJSON(patientService.get(
							transformJSONRequest(GetRequest.getDefaultInstance(), request))));
					break;
					case PATIENTS: json.put("data", convertToJSON(patientService.list(
							transformJSONRequest(ListRequest.getDefaultInstance(), request))));
					break;
					case CREATE_PATIENT: json.put("data", convertToJSON(patientService.create(
							transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
					break;
					case UPDATE_PATIENT: json.put("data", convertToJSON(patientService.update(
							transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
					break;
					case DELETE_PATIENT: json.put("data", convertToJSON(patientService.delete(
							transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
					break;
					case USER: json.put("data", convertToJSON(userService.get(
							transformJSONRequest(GetRequest.getDefaultInstance(), request))));
					break;
					case USERS: json.put("data", convertToJSON(userService.list(
							transformJSONRequest(ListRequest.getDefaultInstance(), request))));
					break;
					case CREATE_USER: json.put("data", convertToJSON(userService.create(
							transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
					break;
					case UPDATE_USER: json.put("data", convertToJSON(userService.update(
							transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
					break;
					case DELETE_USER: json.put("data", convertToJSON(userService.delete(
							transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
					break;
					case VENDOR: json.put("data", convertToJSON(vendorService.get(
							transformJSONRequest(GetRequest.getDefaultInstance(), request))));
					break;
					case VENDORS: json.put("data", convertToJSON(vendorService.list(
							transformJSONRequest(ListRequest.getDefaultInstance(), request))));
					break;
					case CREATE_VENDOR: json.put("data", convertToJSON(vendorService.create(
							transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
					break;
					case UPDATE_VENDOR: json.put("data", convertToJSON(vendorService.update(
							transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
					break;
					case DELETE_VENDOR: json.put("data", convertToJSON(vendorService.delete(
							transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
					break;
					case INVOICE: json.put("data", convertToJSON(invoiceService.get(
							transformJSONRequest(GetRequest.getDefaultInstance(), request))));
					break;
					case INVOICES: json.put("data", convertToJSON(invoiceService.list(
							transformJSONRequest(ListRequest.getDefaultInstance(), request))));
					break;
					case CREATE_INVOICE: json.put("data", convertToJSON(invoiceService.create(
							transformJSONRequest(CreateRequest.getDefaultInstance(), request))));
					break;
					case UPDATE_INVOICE: json.put("data", convertToJSON(invoiceService.update(
							transformJSONRequest(UpdateRequest.getDefaultInstance(), request))));
					break;
					case DELETE_INVOICE: json.put("data", convertToJSON(invoiceService.delete(
							transformJSONRequest(DeleteRequest.getDefaultInstance(), request))));
					break;
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
