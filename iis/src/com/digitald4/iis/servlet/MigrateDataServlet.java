package com.digitald4.iis.servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Invoice;
import com.digitald4.iis.model.Paystub;
import com.digitald4.iis.report.InvoiceReport;
import com.digitald4.iis.report.PaystubReport;

@WebServlet(name = "MigrateDataServlet", urlPatterns = {"/migrate"})
public class MigrateDataServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			String out = "Migrated " + migrateInvoices() + " invoices\n";
			out += "Migrated " + migratePaystubs() + " paystubs\n";
			response.setContentType("text");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setContentLength(out.length());
			response.getOutputStream().write(out.getBytes());
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public int migrateInvoices() throws Exception {
		List<Invoice> invoices = new ArrayList<Invoice>(Invoice.getAllActive());
		for (Invoice invoice : invoices) {
			invoice.setData(new InvoiceReport(invoice).createPDF().toByteArray()).save();
		}
		return invoices.size();
	}
	
	public int migratePaystubs() throws Exception {
		List<Paystub> paystubs = new ArrayList<Paystub>(Paystub.getAllActive());
		for (Paystub paystub : paystubs) {
			paystub.setData(new PaystubReport(paystub).createPDF().toByteArray()).save();
		}
		return paystubs.size();
	}
}
