package com.digitald4.pm.servlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.pm.Client;
import com.digitald4.pm.User;
import com.digitald4.util.Address;
import com.digitald4.util.DBConnector;

public class AddClientConfirmServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			HttpSession session = request.getSession(true);
			Client client = (Client)session.getAttribute("newclient");
			if(client == null){
				response.sendRedirect("addclient");
			}
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/client_confirm.jsp");
      		layoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");
			Client client = (Client)session.getAttribute("newclient");
			if(client == null){
				response.sendRedirect("addclient");
			}
			if(client.isGood()){
				Address address = client.getAddress();
				Address address2 = client.getAddress2();
				Connection con = DBConnector.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("INSERT INTO tbl_client(first_name,last_name,phone_no,alt_phone_no1,alt_phone_no2,address1,city1,state1,zip_code1,address2,city2,state2,zip_code2,ssn,dob,cUserId,aUserId,agentCode,agency,agencyCode,Language,insuranceid,insTypeId,insAmount,note,billingId,labId,lab_code,instructions,received_date)"
				+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,AES_ENCRYPT(?,?),STR_TO_DATE(?,?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())");
				ps.setString(1,client.getFirstName());
				ps.setString(2,client.getLastName());
				ps.setString(3,client.getPhoneNo());
				ps.setString(4,client.getAltPhone1());
				ps.setString(5,client.getAltPhone2());
				ps.setString(6,address.getStreet1());
				ps.setString(7,address.getCity());
				ps.setString(8,address.getState());
				ps.setString(9,address.getZip());
				ps.setString(10,address2.getStreet1());
				ps.setString(11,address2.getCity());
				ps.setString(12,address2.getState());
				ps.setString(13,address2.getZip());
				ps.setString(14,client.getSSN());
				ps.setString(15,User.KEY);
				ps.setString(16,client.getDOB());
				//ps.setString(16,"01/01/1950");
				ps.setString(17,Client.DATE_FORMAT);
				ps.setInt(18,user.getId());
				ps.setInt(19,client.getAgent().getId());
				ps.setString(20,client.getAgentCode());
				ps.setString(21,client.getAgency());
				ps.setString(22,client.getAgencyCode());
				ps.setString(23,client.getPrimaryLang());
				ps.setInt(24,client.getIns().getId());
				ps.setInt(25,client.getInsType().getId());
				ps.setString(26,client.getInsAmount());
				ps.setString(27,client.getNote());
				ps.setInt(28,client.getIns().getBillingCo().getId());
				ps.setInt(29,client.getIns().getLabCo().getId());
				ps.setString(30,client.getIns().getLabCode());
				ps.setString(31,client.getIns().getMailIns());
				if(ps.executeUpdate() > 0){
					session.removeAttribute("newclient");
					ResultSet rs = con.createStatement().executeQuery("SELECT MAX(clientid) FROM tbl_client");
					if(rs.next())
						response.sendRedirect("clientexam?clientId="+rs.getInt(1));
					else
						response.sendRedirect("home");
				}
				else{
					Vector<String> errors = new Vector<String>();
					errors.add("Unable to create client");
			    	request.setAttribute("errors", errors);
					doGet(request,response);
				}
				ps.close();
				con.close();
			}
			else{
				Vector<String> errors = new Vector<String>();
				errors.add("Please fill in all required fields");
		    	request.setAttribute("errors", errors);
				response.sendRedirect("addclient");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Vector<String> errors = new Vector<String>();
			errors.add(e.getMessage());
		    request.setAttribute("errors", errors);
			doGet(request,response);
		}
	}
}
