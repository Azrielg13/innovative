package com.digitald4.order.servlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.order.Address;
import com.digitald4.order.Cart;
import com.digitald4.order.CreditCard;
import com.digitald4.order.Customer;
import com.digitald4.order.Item;

public class PaymentServlet extends ParentServlet{
	public static final String key="_=XMOTY<Nc~Qb[SaBTD=2VYN";
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			String method = request.getParameter("method");
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
			Customer cust = (Customer)session.getAttribute("cust");
			if(method == null || method.equals("credit")){
				int year=Calendar.getInstance().get(Calendar.YEAR);
				Vector<Integer> months = new Vector<Integer>();
				Vector<Integer> years = new Vector<Integer>();
				for(int x=0; x<12; x++){
					months.add(x+1);
					years.add(year+x);
				}
				request.setAttribute("years",years);
				request.setAttribute("months",months);
      			request.setAttribute("body", "/WEB-INF/jsp/payment.jsp");
      			layoutPage.forward(request, response);
			}
			else if(method.equals("contact")){
				saveOrder(cart,cust,method);
				response.sendRedirect("thankyou");
			}
			else if(method.equals("paypal")){
				saveOrder(cart,cust,method);
      			request.setAttribute("body", "/WEB-INF/jsp/paypal.jsp");
      			layoutPage.forward(request, response);
			}
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
			}
			Customer cust = (Customer)session.getAttribute("cust");
			CreditCard card = cart.getCreditCard();
			//card.setName(request.getParameter("name"));
			card.setType(request.getParameter("type"));
			card.setNumber(request.getParameter("number"));
			card.setBackCode(request.getParameter("code"));
			card.setMonth(request.getParameter("month"));
			card.setYear(request.getParameter("year"));
			if(card.isGood()){
				saveOrder(cart,cust,"creditcard");
	      		response.sendRedirect("thankyou");
			}
			else{
				Vector<String> errors = new Vector<String>();
				errors.add("Please fill in all required fields");
				request.setAttribute("errors",errors);
				doGet(request,response);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void saveOrder(Cart cart, Customer cust, String billingMethod)throws Exception{
		System.out.println("save: "+billingMethod);
		Address shipAdd = cart.getShipAddress();
		Address billAdd = cart.getBillAddress();
		CreditCard card = cart.getCreditCard();
		Class.forName("org.gjt.mm.mysql.Driver");
		Connection con = DriverManager.getConnection(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));
		if(cart.getOrderID() <= 0){
			con.createStatement().executeUpdate("INSERT INTO tbl_order(Id,gt_shipping,gt_tax,gt_price,order_date,s_name,s_phone_no,s_address1,s_address2,s_city,s_state,s_zip_code,b_name,b_phone_no,b_address1,b_address2,b_city,b_state,b_zip_code,payment_type,acc_no,acc_type,acc_exp_date,acc_code)"
			+" VALUES ("+cust.getCustID()+","+cart.getShippingCost()+","+cart.getTotalTax()+","+cart.getGrandTotal()+",now()"
			+",'"+shipAdd.getName()+"','"+shipAdd.getPhoneNo()+"','"+shipAdd.getStreet1()+"','"+shipAdd.getStreet2()+"','"+shipAdd.getCity()+"','"+shipAdd.getState()+"','"+shipAdd.getZip()+"'"
			+",'"+billAdd.getName()+"','"+billAdd.getPhoneNo()+"','"+billAdd.getStreet1()+"','"+billAdd.getStreet2()+"','"+billAdd.getCity()+"','"+billAdd.getState()+"','"+billAdd.getZip()+"'"
			+",'"+billingMethod+"',AES_ENCRYPT('"+card.getNumber()+"','"+key+"'),'"+card.getType()+"','"+card.getExpDate()+"',AES_ENCRYPT('"+card.getBackCode()+"','"+key+"'));");
			ResultSet rs = con.createStatement().executeQuery("SELECT MAX(order_id) FROM tbl_order WHERE Id="+cust.getCustID());
			if(rs.next())
				cart.setOrderID(rs.getInt(1));
		}
		else{
			con.createStatement().executeUpdate("UPDATE tbl_order SET"
			+" ID="+cust.getCustID()+",gt_Shipping="+cart.getShippingCost()+",gt_tax="+cart.getTotalTax()+",gt_price="+cart.getGrandTotal()+",order_date=now()"
			+",s_name='"+shipAdd.getName()+"',s_phone_no='"+shipAdd.getPhoneNo()+"',s_address1='"+shipAdd.getStreet1()+"',s_address2='"+shipAdd.getStreet2()+"',s_city='"+shipAdd.getCity()+"',s_state='"+shipAdd.getState()+"',s_zip_code='"+shipAdd.getZip()+"'"
			+",b_name='"+billAdd.getName()+"',b_phone_no='"+billAdd.getPhoneNo()+"',b_address1='"+billAdd.getStreet1()+"',b_address2='"+billAdd.getStreet2()+"',b_city='"+billAdd.getCity()+"',b_state='"+billAdd.getState()+"',b_zip_code='"+billAdd.getZip()+"'"
			+",payment_type='"+billingMethod+"',acc_no=AES_ENCRYPT('"+card.getNumber()+"','"+key+"'),acc_type='"+card.getType()+"',acc_exp_date='"+card.getExpDate()+"',acc_code=AES_ENCRYPT('"+card.getBackCode()+"','"+key+"')"
			+" WHERE Order_ID="+cart.getOrderID()); 
		}
		if(cart.getOrderID() > 0){
			con.createStatement().executeUpdate("DELETE FROM tbl_order_item WHERE Order_ID="+cart.getOrderID());
			for(int x=0; x<cart.getItemCount(); x++){
				Item item = cart.getItem(x);
				con.createStatement().executeUpdate("INSERT IGNORE INTO tbl_order_item(Order_ID,Item_no,Quantity,Price) VALUES("+cart.getOrderID()+",'"+item.getItemNo()+"',"+item.getQty()+","+item.getPrice()+")");
			}
		}
		con.close();
	}
}
