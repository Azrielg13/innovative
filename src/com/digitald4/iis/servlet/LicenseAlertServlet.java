package com.digitald4.iis.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.component.Column;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.License;

@WebServlet(name = "LicenseAlertServlet", urlPatterns = {"/license_alert"})
public class LicenseAlertServlet extends ParentServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			setupTable(request);
			getLayoutPage(request, "/WEB-INF/jsp/license_alert.jsp" ).forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static void setupTable(HttpServletRequest request) {
		ArrayList<Column<License>> columns = new ArrayList<Column<License>>();
		columns.add(new Column<License>("Nurse", "", String.class, false) {
			@Override public Object getValue(License app) throws Exception {
				return "<a href=\"nurse?id=" + app.getNurseId() + "#tab-license\">" + app.getNurse() + "</a>";
			}
		});
		columns.add(new Column<License>("License", "", String.class, false) {
			@Override public Object getValue(License lic) {
				return lic.getLicType().getName();
			}
		});
		columns.add(new Column<License>("Status", "", String.class, false) {
			@Override public Object getValue(License lic) {
				return lic.isExpired() ? "Expired" : "30Day Warning";
			}
		});
		columns.add(new Column<License>("Valid Date", "" + License.PROPERTY.VALID_DATE, String.class, false) {
			@Override public Object getValue(License lic) throws Exception {
				return FormatText.formatDate(lic.getValidDate());
			}
		});
		columns.add(new Column<License>("Exp Date", "" + License.PROPERTY.EXPIRATION_DATE, String.class, false) {
			@Override public Object getValue(License lic) throws Exception {
				return FormatText.formatDate(lic.getExpirationDate());
			}
		});
		request.setAttribute("alarming_cols", columns);
		request.setAttribute("alarming", License.getAlarming());
	}
}
