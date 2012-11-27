package com.digitald4.pm.servlet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.pm.BillingCo;
import com.digitald4.pm.Client;
import com.digitald4.pm.Exam;
import com.digitald4.pm.LabCo;
import com.digitald4.pm.SortableExaminer;
import com.digitald4.pm.User;
import com.digitald4.util.DBConnector;

public class AssignExaminerServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response,User.STAFF))return;
			if(request.getParameter("clientId") == null){
				//need to send error that user not found
				response.sendRedirect("home");
				return;
			}
			int clientId = Integer.parseInt(request.getParameter("clientId"));
			HttpSession session = request.getSession(true);
			Client client = Client.getClient(clientId);
			//need to log error if client does not exist
			if(client == null){
				response.sendRedirect("home");
				return;
			}
			session.setAttribute("client",client);
			forwardToJSP(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Client client = (Client)session.getAttribute("client");
			Connection con = DBConnector.getInstance().getConnection();

			Hashtable<String,Exam> reqExams = new Hashtable<String,Exam>();
			for(int x=0; x<client.getExams().size(); x++)
				reqExams.put(""+client.getExams().get(x).getId(),client.getExams().get(x));

			TreeSet<SortableExaminer> examiners = new TreeSet<SortableExaminer>();
			ResultSet rs = con.createStatement().executeQuery("SELECT userId FROM tbl_user WHERE type='Examiner' AND hide!=1 ORDER BY Last_Name, First_Name");
			while(rs.next())
				examiners.add(new SortableExaminer(User.getUser(rs.getInt("userId")),reqExams));

			Vector<BillingCo> billCos = new Vector<BillingCo>();
			rs = con.createStatement().executeQuery("SELECT billingId FROM tbl_billing_co WHERE hide=0 ORDER BY Name");
			while(rs.next())
				billCos.add(BillingCo.getBillingCo(rs.getInt(1)));

			Vector<LabCo> labCos = new Vector<LabCo>();
			rs = con.createStatement().executeQuery("SELECT labId FROM tbl_lab_co WHERE hide=0 ORDER BY Name");
			while(rs.next())
				labCos.add(LabCo.getLabCo(rs.getInt(1)));

			request.setAttribute("examiners",new Vector<SortableExaminer>(examiners));
			request.setAttribute("billCos",billCos);
			request.setAttribute("labCos",labCos);
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/assign.jsp");
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
			Client client = (Client)session.getAttribute("client");
			if(client == null){
				client = new Client();
				session.setAttribute("client",client);
			}

			String redirect = request.getParameter("redirect");
			if(redirect != null && redirect.length() > 0){
				response.sendRedirect(redirect);
				return;
			}

			if(request.getParameter("examinerid") != null){
				int examinerId = Integer.parseInt(request.getParameter("examinerid"));
				User examiner = client.getExaminer();
				if(examiner == null || examiner.getId() != examinerId)
					client.setExaminer(User.getUser(examinerId));
			}
			if(request.getParameter("billingId") != null){
				int billingId = Integer.parseInt(request.getParameter("billingId"));
				BillingCo b = BillingCo.getBillingCo(billingId);
				client.setBillingCo(b);
			}
			if(request.getParameter("labId") != null){
				int labId = Integer.parseInt(request.getParameter("labId"));
				LabCo b = LabCo.getLabCo(labId);
				client.setLabCo(b);
			}
			client.setLabCode(request.getParameter("labCode"));
			client.setMailIns(request.getParameter("instructions"));

			if(client.getExaminer() != null && client.getExaminer().getId() > 0){
				response.sendRedirect("assignexaminerconfirm");
			}
			else{
				Vector<String> errors = new Vector<String>();
				errors.add("Please select an examiner");
		    	request.setAttribute("errors", errors);
				forwardToJSP(request,response);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Vector<String> errors = new Vector<String>();
			errors.add(e.getMessage());
		    request.setAttribute("errors", errors);
			forwardToJSP(request,response);
		}
	}
}
