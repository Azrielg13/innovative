package com.digitald4.iis.servlet;

import static com.digitald4.common.util.FormatText.formatCurrency;
import static com.digitald4.common.util.FormatText.formatDate;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Paystub;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "PayHistoryServlet", urlPatterns = {"/payhistory"})
public class PayHistoryServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			request.setAttribute("payStubs", Paystub.getAllActive(Paystub.class, getEntityManager()));
			getLayoutPage(request, "/WEB-INF/jsp/payhistory.jsp").forward(request, response);
		} catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column<Paystub>> payStubCols = new ArrayList<Column<Paystub>>();
		payStubCols.add(new Column<Paystub>("Nurse", "NURSE", String.class, false) {
			@Override public Object getValue(Paystub stub) throws Exception {
				return "<a href=\"nurse?id=" + stub.getNurseId() + "#tab-payhist\">" + stub.getNurse() + "</a>";
			}
		});
		payStubCols.add(new Column<Paystub>("Pay Date", "", String.class, false) {
			@Override public Object getValue(Paystub stub) throws Exception {
				return formatDate(stub.getPayDate())
						+ " <span><a href=\"report.pdf?type=paystub&id=" + stub.getId() + "\">"
						+ "<img src=\"images/icons/fugue/document-pdf.png\"/></a></span>"; 
			}
		});
		payStubCols.add(new Column<Paystub>("Gross", "", String.class, false) {
			@Override public Object getValue(Paystub stub) throws Exception {
				return formatCurrency(stub.getGrossPay())
						+ " <span><a onclick=\"showDeleteDialog('" + stub.getClass().getName() + "', " + stub.getId() + ")\" target=\"_blank\">"
						+ "<img src=\"images/icons/fugue/cross-circle.png\"/></a></span>";
			}
		});
		payStubCols.add(new Column<Paystub>("Deductions", "", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getPreTaxDeduction() + stub.getPostTaxDeduction());
			}
 		});
		payStubCols.add(new Column<Paystub>("Taxes", "", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getTaxTotal());
			}
		});
		payStubCols.add(new Column<Paystub>("Mileage Reimbursment", "MILEAGE", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getPayMileage());
			}
 		});
		payStubCols.add(new Column<Paystub>("Net Pay", "PAY_RATE", String.class, false) {
			@Override public Object getValue(Paystub stub) {
				return formatCurrency(stub.getNetPay());
			}
 		});
		request.setAttribute("payhistcols", payStubCols);
	}
}
