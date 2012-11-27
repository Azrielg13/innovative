package com.digitald4.pm.servlet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.pm.Client;
import com.digitald4.pm.InsuranceCo;
import com.digitald4.pm.User;
import com.digitald4.util.DBConnector;

public class InsuranceServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			if(request.getParameter("hoodId") == null){
				//need to send error that insurance not found
				response.sendRedirect("home");
				return;
			}
			int insuranceId = Integer.parseInt(request.getParameter("insuranceId"));
			HttpSession session = request.getSession(true);

			InsuranceCo ins = InsuranceCo.getInsuranceCo(insuranceId);
			//need to log error if ins does not exist
			if(ins == null)
				response.sendRedirect("home");
			session.setAttribute("ins",ins);

			String SortBy = "last_update";
			if(request.getParameter("SortBy") != null){
				SortBy = request.getParameter("SortBy");
			}
			request.setAttribute("SortBy",SortBy);

			forwardToJSP(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			InsuranceCo ins = (InsuranceCo)session.getAttribute("ins");
			String SortBy = (String)request.getAttribute("SortBy");
			User user = (User)session.getAttribute("user");

			// Need to edit where clause based on user type,
			// admin & corp: all
			// rep where neighborhood is in tbl_rep_hood
			// agent where tbl_client userId = aClientId

			String whereClause ="";
			if(user.getType()==User.AGENT){
				whereClause = "AND aUserId="+user.getId();
			}else if(user.getType()==User.EXAMINER){
				whereClause = "AND eUserId="+user.getId();
			}

			Connection con = DBConnector.getInstance().getConnection();

			Vector<Client> compClients = new Vector<Client>();
			Vector<Client> canClients = new Vector<Client>();
			Vector<Client> clients = new Vector<Client>();
			ResultSet rs = con.createStatement().executeQuery("SELECT clientId FROM tbl_client INNER JOIN tbl_insurance_co USING(insuranceId) WHERE tbl_client.hide=0 AND tbl_client.insuranceId="+ins.getId()+" "+whereClause+" ORDER BY "+SortBy);
			while(rs.next()){
				if(Client.getClient(rs.getInt("clientId")).getStatus()==1)
					compClients.add(Client.getClient(rs.getInt("clientId")));
				else if(Client.getClient(rs.getInt("clientId")).getStatus()==2)
					canClients.add(Client.getClient(rs.getInt("clientId")));
				else
					clients.add(Client.getClient(rs.getInt("clientId")));
			}

			request.setAttribute("compClients",compClients);
			request.setAttribute("canClients",canClients);
			request.setAttribute("clients",clients);

			// EDDIE - Why send company? 7/1/06
			request.setAttribute("company",company);

      		request.setAttribute("body", "/WEB-INF/jsp/neighborhood.jsp");
      		layoutPage.forward(request, response);
      		con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
