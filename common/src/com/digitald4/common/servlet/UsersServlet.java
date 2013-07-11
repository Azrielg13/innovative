package com.digitald4.common.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.digitald4.common.component.Column;
import com.digitald4.common.model.User;
import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.common.util.FormatText;

public class UsersServlet extends ParentServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			if (!checkLoginAutoRedirect(request, response)) return;
			ArrayList<Column> columns = new ArrayList<Column>();
			columns.add(new Column("Name", "Link", String.class, true) {
				@Override
				public Object getValue(Object o) {
					User user = (User)o;
					return "<a href=\"user?id="+user.getId()+"\">"+user+"</a>";
				}
			});
			columns.add(new Column("Type", "type", String.class, true));
			columns.add(new Column("Email Address", "email", String.class, false));
			columns.add(new Column("Disabled", "disabled", Boolean.class, false));
			columns.add(new Column("Last Login", "Last Login", DateTime.class, false) {
				@Override
				public Object getValue(Object o) {
					return FormatText.formatDate(((User)o).getLastLogin(), FormatText.USER_DATETIME);
				}
			});
			columns.add(new Column("Notes", "notes", String.class, false));
			request.setAttribute("columns", columns);
			request.setAttribute("users", User.getAll());
			getLayoutPage(request, "/WEB-INF/jsp/users.jsp" ).forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
}
