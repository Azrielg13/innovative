package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.iis.model.Vendor;

public class VendorsServlet extends ParentServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/vendors.jsp").forward(request, response);
		} catch(Exception e){
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column<Vendor>> columns = new ArrayList<Column<Vendor>>();
		columns.add(new Column<Vendor>("Vendor", "", String.class, false) {
			@Override
			public Object getValue(Vendor vendor) {
				return "<a title=\"" + vendor + "\" href=\"vendor?id=" + vendor.getId() + "\">" + vendor + "</a>";
			}
		});
		columns.add(new Column<Vendor>("Address", "" + Vendor.PROPERTY.ADDRESS, String.class, false));
		columns.add(new Column<Vendor>("Fax Number", "" + Vendor.PROPERTY.FAX_NUMBER, String.class, false));
		columns.add(new Column<Vendor>("Contact Name", "" + Vendor.PROPERTY.CONTACT_NAME, String.class, false));
		columns.add(new Column<Vendor>("Contact Phone", "" + Vendor.PROPERTY.CONTACT_NUMBER, String.class, false));
		columns.add(new Column<Vendor>("Pending Assessments", "", String.class, false) {
			@Override
			public Object getValue(Vendor vendor) throws Exception {
				return vendor.getPendingAssessments().size();
			}
		});
		request.setAttribute("vendors_cols", columns);
		request.setAttribute("vendors", Vendor.getAllActive());
	}
}
