package com.digitald4.order.servlet;
import com.digitald4.order.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;

public class RegisterServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Customer cust = (Customer)session.getAttribute("cust");
			if(cust == null){
				cust = new Customer();
				session.setAttribute("cust",cust);
			}
			Vector states = new Vector();
			Class.forName("org.gjt.mm.mysql.Driver");
			String dbUrl = getServletContext().getInitParameter("dburl");
			String dbUser = getServletContext().getInitParameter("dbuser");
			String dbPasswd = getServletContext().getInitParameter("dbpass");
			Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
			ResultSet rs = con.createStatement().executeQuery("SELECT state_abbrev FROM tbl_state ORDER BY state_abbrev");
			while(rs.next())
				states.add(rs.getString(1));
			request.setAttribute("states",states);
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/register.jsp");
      		layoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Customer cust = (Customer)session.getAttribute("cust");
			if(cust == null){
				cust = new Customer();
				session.setAttribute("cust",cust);
			}
			String pass = request.getParameter("password");
			String cPass = request.getParameter("cpassword");
			cust.setEmail(request.getParameter("email"));
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
			if(!pass.equals(cPass)){
				Vector errors = new Vector();
				errors.add("Passwords do not match");
		    	request.setAttribute("errors", errors);
				doGet(request,response);
			}
			if(pass.length() > 0 && cust.isGood()){
				Class.forName("org.gjt.mm.mysql.Driver");
				String dbUrl = getServletContext().getInitParameter("dburl");
				String dbUser = getServletContext().getInitParameter("dbuser");
				String dbPasswd = getServletContext().getInitParameter("dbpass");
				Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
				con.createStatement().executeUpdate("INSERT INTO tbl_client(email,password,first_name,last_name,phone_no,address1,address2,city,state,zip_code)"
				+" VALUES('"+cust.getEmail()+"',MD5('"+pass+"'),'"+cust.getFName()+"','"+cust.getLName()+"','"+address.getPhoneNo()+"','"+address.getStreet1()+"','"+address.getStreet2()+"','"+address.getCity()+"','"+address.getState()+"','"+address.getZip()+"')");
				ResultSet rs = con.createStatement().executeQuery("SELECT ID FROM tbl_client WHERE email='"+cust.getEmail()+"' AND password=MD5('"+pass+"')");
				if(rs.next()){
					cust.setCustID(rs.getInt(1));
					String redirect = (String)session.getAttribute("redirect");
					if(redirect == null)
						redirect = "account";
					response.sendRedirect(redirect);
				}
				else{
					Vector errors = new Vector();
					errors.add("Unable to create account");
			    	request.setAttribute("errors", errors);
					doGet(request,response);
				}
			}
			else{
				Vector errors = new Vector();
				errors.add("Please fill in all required fields");
		    	request.setAttribute("errors", errors);
				doGet(request,response);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Vector errors = new Vector();
			errors.add(e.getMessage());
		    request.setAttribute("errors", errors);
			doGet(request,response);
		}	
	}
}