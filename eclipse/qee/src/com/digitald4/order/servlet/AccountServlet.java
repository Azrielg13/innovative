package com.digitald4.order.servlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.order.Address;
import com.digitald4.order.Cart;
import com.digitald4.order.Customer;

public class AccountServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{

			if(!checkLogin(request,response))
				return;
			Vector<String> states = new Vector<String>();
			Class.forName("org.gjt.mm.mysql.Driver");
			String dbUrl = getServletContext().getInitParameter("dburl");
			String dbUser = getServletContext().getInitParameter("dbuser");
			String dbPasswd = getServletContext().getInitParameter("dbpass");
			Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
			ResultSet rs = con.createStatement().executeQuery("SELECT state_abbrev FROM tbl_state ORDER BY state_abbrev");
			while(rs.next())
				states.add(rs.getString(1));
			request.setAttribute("states",states);
      		request.setAttribute("body", "/WEB-INF/jsp/account.jsp");
      		getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Vector<String> errors = new Vector<String>();
		try{
			if(!checkLogin(request,response))
				return;
			Customer cust = (Customer)request.getSession().getAttribute("cust");
			String action = request.getParameter("action");
			if(action != null){
				Class.forName("org.gjt.mm.mysql.Driver");
				String dbUrl = getServletContext().getInitParameter("dburl");
				String dbUser = getServletContext().getInitParameter("dbuser");
				String dbPasswd = getServletContext().getInitParameter("dbpass");
				Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
				if(action.equals("user")){
					String user = request.getParameter("user");
					String pass = request.getParameter("pass");
					String cPass = request.getParameter("cpass");
					if(user != null && user.length() > 0){
						con.createStatement().executeUpdate("UPDATE tbl_client SET Email='"+user+"' WHERE ID="+cust.getCustID());
						cust.setEmail(user);
						if(pass != null && pass.length() > 0){
							if(cPass == null || cPass.length() == 0 || !pass.equals(cPass)){
								errors.add("Passwords do not match");
							}
							else{
								con.createStatement().executeUpdate("UPDATE tbl_client SET Password=MD5('"+pass+"') WHERE ID="+cust.getCustID());
							}
						}
					}
					else{
						errors.add("You must have an email address");
					}
				}
				else if(action.equals("account")){
					cust.setFName(request.getParameter("fname"));
					cust.setLName(request.getParameter("lname"));
					Address address = cust.getAddress();
					address.setName(cust.getFName()+" "+cust.getLName());
					address.setStreet1(request.getParameter("street1"));
					address.setStreet2(request.getParameter("street2"));
					address.setCity(request.getParameter("city"));
					address.setState(request.getParameter("state"));
					address.setZip(request.getParameter("zip"));
					address.setPhoneNo(request.getParameter("phone"));
					Cart cart = (Cart)request.getSession().getAttribute("cart");
					if(cart == null){
						cart = new Cart();
						request.getSession().setAttribute("cart",cart);
					}
					if(address.isGood()){
						cart.getShipAddress().copy(address);
						cart.getBillAddress().copy(address);
					}
					if(cust.isGood()){
						con.createStatement().executeUpdate("UPDATE tbl_client SET First_Name='"+cust.getFName()+"',Last_Name='"+cust.getLName()+"',Phone_No='"+address.getPhoneNo()+"',Address1='"+address.getStreet1()+"',Address2='"+address.getStreet2()+"',City='"+address.getCity()+"',State='"+address.getState()+"',Zip_Code='"+address.getZip()+"' WHERE ID="+cust.getCustID());
					}
					else{
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
