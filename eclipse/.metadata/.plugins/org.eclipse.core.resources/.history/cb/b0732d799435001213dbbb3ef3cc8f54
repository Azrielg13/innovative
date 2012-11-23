package com.digitald4.order.servlet;
import com.digitald4.order.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.IOException;
   
public class LoginServlet extends ParentServlet
{
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
{
		HttpSession session = request.getSession(true);
		Customer cust = (Customer)session.getAttribute("cust");
		if(cust != null && cust.getCustID() > 0){
      		response.sendRedirect("account");
			return;
		}
		if(request.getParameter("u") != null && request.getParameter("key") != null)
			doPost(request,response);
		else
			forward2Jsp(request, response);
   }
	protected void forward2Jsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		Customer cust = (Customer)session.getAttribute("cust");
		if(cust == null){
			cust = new Customer();
			session.setAttribute("cust",cust);
		}
		request.setAttribute("body", "/WEB-INF/jsp/login.jsp");
   	   layoutPage.forward(request, response);
	}
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
   {
      HttpSession session = request.getSession();
      String userid = request.getParameter("u");
      if (userid == null || userid.length() == 0) {
         request.setAttribute("error", "Email address is required.");
         forward2Jsp(request, response);
         return;
      }
      String passwd = request.getParameter("key");
      if (passwd == null) passwd = "";

      Customer cust = (Customer)session.getAttribute("cust");
		if(cust == null){
			cust = new Customer();
      		session.setAttribute("cust", cust);
		}
		cust.setEmail(userid);
      try {
			Class.forName("org.gjt.mm.mysql.Driver");
			String dbUrl = getServletContext().getInitParameter("dburl");
			String dbUser = getServletContext().getInitParameter("dbuser");
			String dbPasswd = getServletContext().getInitParameter("dbpass");
			Connection con = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
			ResultSet rs = con.createStatement().executeQuery("SELECT *"
			+" FROM tbl_client WHERE email=\""+userid+"\" AND password=MD5('"+passwd+"')");
			if(rs.next()){
				cust.setCustID(rs.getInt("id"));
				cust.setEmail(rs.getString("Email"));
				cust.setFName(rs.getString("First_Name"));
				cust.setLName(rs.getString("Last_Name"));
				cust.setType(rs.getString("type"));
				Address address = cust.getAddress();
				address.setName(cust.getFName()+" "+cust.getLName());
				address.setStreet1(rs.getString("Address1"));
				address.setStreet2(rs.getString("Address2"));
				address.setCity(rs.getString("City"));
				address.setState(rs.getString("State"));
				address.setZip(rs.getString("Zip_Code"));
				address.setPhoneNo(rs.getString("Phone_No"));
				Cart cart = (Cart)session.getAttribute("cart");
				if(cart == null){
					cart = new Cart();
					session.setAttribute("cart",cart);
				}
				cart.getShipAddress().copy(address);
				cart.getBillAddress().copy(address);
			}
			else{
				request.setAttribute("error", "Login incorrect");
         		forward2Jsp(request, response);
				con.close();
         		return;
			}
			con.close();
      } catch (Exception e) {
         throw new ServletException(e);
      }
	String redirect = (String)session.getAttribute("redirect");
	if(redirect == null)
		redirect = "account";
	else
		session.removeAttribute("redirect");
      response.sendRedirect(redirect);
   }
}
