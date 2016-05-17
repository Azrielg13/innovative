package com.digitald4.iis.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Invoice;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "UnPaidInvoicesServlet", urlPatterns = {"/unpaidinvoices"})
public class UnpaidInvoicesServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			request.setAttribute("unpaidInvoices",  Invoice.getUnpaidInvoices(getEntityManager()));
			getLayoutPage(request, "/WEB-INF/jsp/unpaidinvoices.jsp").forward(request, response);
		} catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		List<Column<Invoice>> cols2 = new ArrayList<Column<Invoice>>();
		cols2.add(new Column<Invoice>("Vendor", "", String.class, false) {
			@Override public Object getValue(Invoice inv) throws Exception {
				return "<a href=\"vendor?id=" + inv.getVendorId() + "#tab-invoices\">" + inv.getVendor() + "</a>";
			}
		});
		cols2.add(new Column<Invoice>("Name", "" + Invoice.PROPERTY.NAME, String.class, false));
		cols2.add(new Column<Invoice>("Date", "", String.class, false) {
			@Override public Object getValue(Invoice invoice) throws Exception {
				return FormatText.formatDate(invoice.getGenerationTime(), FormatText.MYSQL_DATETIME)
						+ " <span><a href=\"report.pdf?type=inv&id=" + invoice.getId() + "\" target=\"_blank\">"
						+ "<img src=\"images/icons/fugue/document-pdf.png\"/></a></span>"; 
			}
		});
		cols2.add(new Column<Invoice>("Billed", "", String.class, false) {
			@Override public Object getValue(Invoice invoice) {
				return FormatText.CURRENCY.format(invoice.getTotalDue())
						+ " <span><a onclick=\"showDeleteDialog('" + invoice.getClass().getName() + "', " + invoice.getId() + ")\" target=\"_blank\">"
						+ "<img src=\"images/icons/fugue/cross-circle.png\"/></a></span>";
			}
		});
		cols2.add(new Column<Invoice>("Status", "STATUS", String.class, false));
		cols2.add(new Column<Invoice>("Comment", "" + Invoice.PROPERTY.COMMENT, String.class, true));
		cols2.add(new Column<Invoice>("Received", "" + Invoice.PROPERTY.TOTAL_PAID, String.class, true));
		request.setAttribute("invoicecols", cols2);
	}
}
