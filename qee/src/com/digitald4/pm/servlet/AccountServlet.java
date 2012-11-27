package com.digitald4.pm.servlet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.pm.User;
import com.digitald4.util.Address;
import com.digitald4.util.DBConnector;

public class AccountServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			Vector<String> states = new Vector<String>();
			Connection con =  DBConnector.getInstance().getConnection();
			ResultSet rs = con.createStatement().executeQuery("SELECT state_abbrev FROM tbl_state ORDER BY state_abbrev");
			while(rs.next())
				states.add(rs.getString(1));
			request.setAttribute("states",states);
      		request.setAttribute("body", "/WEB-INF/jsp/account.jsp");
      		layoutPage.forward(request, response);
      		con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Vector<String> errors = new Vector<String>();
		try{
			if(!checkLogin(request,response))return;
			User user = (User)request.getSession().getAttribute("user");
			String action = request.getParameter("action");
			if(action != null){
				if(action.equals("user")){
					String email = request.getParameter("user");
					String pass = request.getParameter("pass");
					String cPass = request.getParameter("cpass");
					if(email != null && email.length() > 0){
						user.setEmail(email);
						if(pass != null && pass.length() > 0){
							if(cPass == null || cPass.length() == 0 || !pass.equals(cPass))
								errors.add("Passwords do not match");
							else
								user.setPassword(pass);
						}
					}
					else
						errors.add("You must have an email address");
				}
				else if(action.equals("account")){
					user.setFirstName(request.getParameter("fname"));
					user.setLastName(request.getParameter("lname"));
					user.setPhoneNo(request.getParameter("phone"));
					user.setFaxNo(request.getParameter("fax"));
					Address address = new Address(request.getParameter("street1"),request.getParameter("street2"),request.getParameter("city"),request.getParameter("state"),request.getParameter("zip"));
					user.setAddress(address);
					if(!user.isGood() || !address.isGood()){
						errors.add("Please fill in all required fields");
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			errors.add(e.getMessage());
		}
		request.setAttribute("errors",errors);
		doGet(request,response);
	}
}
