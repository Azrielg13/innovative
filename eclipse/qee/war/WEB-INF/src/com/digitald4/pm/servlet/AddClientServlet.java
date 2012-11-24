package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import com.digitald4.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;

public class AddClientServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");
			if(user == null)
				user = new User();
			Client client = (Client)session.getAttribute("newclient");
			if(client == null){
				client = new Client();
				session.setAttribute("newclient",client);
			}
			forwardToJSP(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response){
		try{
			Vector<String> states = new Vector<String>();
			Vector<String> langs = new Vector<String>();
			Vector<User> agents = new Vector<User>();
			Vector<InsuranceCo> ins = new Vector<InsuranceCo>();
			Vector<InsuranceType> insTypes = new Vector<InsuranceType>();
			Connection con = DBConnector.getInstance().getConnection();
			ResultSet rs = con.createStatement().executeQuery("SELECT state_abbrev FROM tbl_state WHERE hide=0 ORDER BY state_abbrev");
			while(rs.next())
				states.add(rs.getString(1));
			request.setAttribute("states",states);
			rs = con.createStatement().executeQuery("SELECT language FROM tbl_language WHERE hide=0 ORDER BY language");
			while(rs.next())
				langs.add(rs.getString(1));
			rs = con.createStatement().executeQuery("SELECT userId FROM tbl_user WHERE hide=0 AND type='Agent' ORDER BY Last_Name, First_Name");
			while(rs.next())
				agents.add(User.getUser(rs.getInt("userId")));
			rs = con.createStatement().executeQuery("SELECT insuranceId FROM tbl_insurance_co WHERE hide=0 ORDER BY Name");
			while(rs.next())
				ins.add(InsuranceCo.getInsuranceCo(rs.getInt("insuranceId")));
			rs = con.createStatement().executeQuery("SELECT insTypeId FROM "+InsuranceType.TABLE+" WHERE hide=0");
			while(rs.next())
				insTypes.add(InsuranceType.getInsuranceType(rs.getInt("insTypeId")));
			request.setAttribute("states",states);
			request.setAttribute("langs",langs);
			request.setAttribute("agents",agents);
			request.setAttribute("ins",ins);
			request.setAttribute("insTypes",insTypes);
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/register.jsp");
      		layoutPage.forward(request, response);
      		con.close();
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
				client = new Client();
				session.setAttribute("newclient",client);
			}
			client.setInsAmount(request.getParameter("insAmount"));
			client.setInsType(InsuranceType.getInsuranceType(Integer.parseInt(request.getParameter("insTypeId"))));
			client.setEmail(request.getParameter("email"));
			client.setSSN(request.getParameter("ssn"));
			client.setFirstName(request.getParameter("fname"));
			client.setLastName(request.getParameter("lname"));
			client.setSex(request.getParameter("sex"));
			Address address = new Address(request.getParameter("street1"),"",request.getParameter("city1"),request.getParameter("state1"),request.getParameter("zip1"));
			client.setAddress(address);
			Address address2 = new Address(request.getParameter("street2"),"",request.getParameter("city2"),request.getParameter("state2"),request.getParameter("zip2"));
			client.setAddress2(address2);
			client.setPhoneNo(request.getParameter("phone"));
			client.setAltPhone1(request.getParameter("alt_phone_no1"));
			client.setAltPhone2(request.getParameter("alt_phone_no2"));
			client.setDOB(request.getParameter("dob"));

			//client.setLab(request.getParameter("lab"));


			int insId = Integer.parseInt(request.getParameter("insid"));
			InsuranceCo ins = client.getIns();
			if(ins == null || ins.getId() != insId)
				client.setIns(InsuranceCo.getInsuranceCo(insId));

			if(user.getType() >= user.AGENT)
				client.setAgent(user);
			else{
				int agentId = Integer.parseInt(request.getParameter("agentid"));
				User agent = client.getAgent();
				if(agent == null || agent.getId() != agentId)
					client.setAgent(User.getUser(agentId));
			}
			client.setAgentCode(request.getParameter("agentCode"));
			client.setAgency(request.getParameter("agency"));
			client.setAgencyCode(request.getParameter("agencyCode"));
			client.setPrimaryLang(request.getParameter("primarylang"));
			client.setNote(request.getParameter("note"));


			String redirect = request.getParameter("redirect");
			if(redirect != null && redirect.length() > 0){
				response.sendRedirect(redirect);
				return;
			}
			if(client.isGood())
				response.sendRedirect("addclientconfirm");
			else{
				Vector<String> errors = new Vector<String>();
				errors.add("Please fill in all required fields");
		    	request.setAttribute("errors", errors);
				forwardToJSP(request,response);
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
