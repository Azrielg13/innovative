package com.digitald4.order.servlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.servlet.ParentServlet;
import com.digitald4.order.Address;
import com.digitald4.order.Cart;
import com.digitald4.order.ShipMethod;

public class ShippingServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Cart cart = (Cart)session.getAttribute("cart");
			if(cart == null){
				response.sendRedirect("home");
				return;
			}
			if(cart.getItemCount() == 0){
				response.sendRedirect("cart");
				return;
			}
			if(!checkLogin(request,response))
				return;
			String action = request.getParameter("action"); 
			if(action != null && action.equals("clear")){
				Address shipAdd = cart.getShipAddress();
				Address billAdd = cart.getBillAddress();
				shipAdd.clear();
				if(!cart.isSameAddress())
					billAdd.clear();
			}
			Class.forName("org.gjt.mm.mysql.Driver");
			String dbUrl = getServletContext().getInitParameter("dburl");
			String dbUser = getServletContext().getInitParameter("dbuser");
			String dbPasswd = getServletContext().getInitParameter("dbpass");
			Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
			Vector<ShipMethod> shipOptions = new Vector<ShipMethod>();
			ResultSet rsShip = con.createStatement().executeQuery("SELECT * FROM tbl_shipping ORDER BY tbl_shipping.price DESC");
			while(rsShip.next()){
				shipOptions.add(new ShipMethod(rsShip.getString("title"),rsShip.getInt("max_items"),rsShip.getDouble("price")));
			}
			request.setAttribute("shipopts", shipOptions);
			Vector<String> states = new Vector<String>();
			ResultSet rs = con.createStatement().executeQuery("SELECT state_abbrev FROM tbl_state ORDER BY state_abbrev");
			while(rs.next())
				states.add(rs.getString(1));
			request.setAttribute("states",states);
			request.setAttribute("company",getCompany());
      		request.setAttribute("body", "/WEB-INF/jsp/shipping.jsp");
      		getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Cart cart = (Cart)session.getAttribute("cart");
			if(cart == null){
				response.sendRedirect("home");
				return;
			}
			Class.forName("org.gjt.mm.mysql.Driver");
			String dbUrl = getServletContext().getInitParameter("dburl");
			String dbUser = getServletContext().getInitParameter("dbuser");
			String dbPasswd = getServletContext().getInitParameter("dbpass");
			Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
			ShipMethod shipMethod = cart.getShipMethod();
			ResultSet rsShip = con.createStatement().executeQuery("SELECT * FROM tbl_shipping WHERE title='"+request.getParameter("shipmethod")+"'");
			if(rsShip.next()){
				shipMethod.setTitle(rsShip.getString("title"));
				shipMethod.setMaxCount(rsShip.getInt("max_items"));
				shipMethod.setPrice(rsShip.getDouble("price"));
			}
			cart.setSameAddress(request.getParameter("samebilling") == null || request.getParameter("samebilling").equals("yes"));
			cart.setRememberMe(request.getParameter("remember") != null);
			Address shipAdd = cart.getShipAddress();
			Address billAdd = cart.getBillAddress();
			String action = request.getParameter("action"); 
			if(action != null && action.equals("clear")){
				/*System.out.println("clear shipping");
				shipAdd.clear();
				if(!cart.isSameAddress())
					billAdd.clear();*/
				doGet(request,response);
			}
			else{
				shipAdd.setName(request.getParameter("name"));
				shipAdd.setStreet1(request.getParameter("street1"));
				shipAdd.setStreet2(request.getParameter("street2"));
				shipAdd.setCity(request.getParameter("city"));
				shipAdd.setState(request.getParameter("state"));
				shipAdd.setZip(request.getParameter("zip"));
				shipAdd.setPhoneNo(request.getParameter("phone"));
				String taxState=shipAdd.getState();
				if(!cart.isSameAddress()){
					billAdd.setName(request.getParameter("bname"));
					billAdd.setStreet1(request.getParameter("bstreet1"));
					billAdd.setStreet2(request.getParameter("bstreet2"));
					billAdd.setCity(request.getParameter("bcity"));
					billAdd.setState(request.getParameter("bstate"));
					billAdd.setZip(request.getParameter("bzip"));
					billAdd.setPhoneNo(request.getParameter("bphone"));
					taxState=billAdd.getState();
				}
				if((cart.getShipCount()==0 || shipMethod.isGood()) && shipAdd.isGood() && (cart.isSameAddress() || billAdd.isGood())){
					ResultSet rs = con.createStatement().executeQuery("SELECT Tax_Percent FROM tbl_state WHERE state_abbrev=\""+taxState+"\"");
					if(rs.next())
						cart.setTaxRate(rs.getDouble(1));
		      		response.sendRedirect("invoice");
				}
				else{
					Vector<String> errors = new Vector<String>();
					errors.add("Please fill in all required fields");
		      		request.setAttribute("errors", errors);
					doGet(request,response);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}