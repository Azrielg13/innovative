package com.digitald4.common.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.model.User;

public class HomeServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			forwardToJSP(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);

			User user = (User)session.getAttribute("user");
			
			user.getFirstName();

			String sortBy = "last_update DESC";
			if(request.getParameter("sortBy") != null){
				sortBy = request.getParameter("sortBy");
			}
			request.setAttribute("sortBy",sortBy);

			String limit_s = "10";
			if(request.getParameter("limit") != null){
				limit_s = request.getParameter("limit");
			}
			int limit = Integer.parseInt(limit_s);
			request.setAttribute("limit",limit);

			String offsetOpen_s = "0";
			if(request.getParameter("offsetOpen") != null){
				offsetOpen_s = request.getParameter("offsetOpen");
			}
			int offsetOpen = Integer.parseInt(offsetOpen_s);
			request.setAttribute("offsetOpen",offsetOpen);

			String offsetClient_s = "0";
			if(request.getParameter("offsetClient") != null){
				offsetClient_s = request.getParameter("offsetClient");
			}
			int offsetClient = Integer.parseInt(offsetClient_s);
			request.setAttribute("offsetClient",offsetClient);

			String offsetComp_s = "0";
			if(request.getParameter("offsetComp") != null){
				offsetComp_s = request.getParameter("offsetComp");
			}
			int offsetComp = Integer.parseInt(offsetComp_s);
			request.setAttribute("offsetComp",offsetComp);

			/*String whereClause ="";
			if(user.getType()==User.AGENT){
				whereClause = "AND cUserId="+user.getId();
			}else if(user.getType()==User.EXAMINER){
				whereClause = "AND eUserId="+user.getId();
			}

			// No aUserId (done), aUserId does not exist (NEED HELP)


			// Open Clients (No examiner)
			ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(clientId) FROM tbl_client WHERE hide=0 AND eUserId is null");
			if(rs.next())
				request.setAttribute("openClientCount",rs.getInt(1));

			Vector<Client> openClients = new Vector<Client>();
			rs = con.createStatement().executeQuery("SELECT clientId FROM tbl_client WHERE hide=0 AND eUserId is null ORDER BY "+sortBy+" LIMIT "+limit+" OFFSET "+offsetOpen);
			while(rs.next())
				openClients.add(Client.getClient(rs.getInt("clientId")));

			request.setAttribute("openClients",openClients);

			// Active Clients
			rs = con.createStatement().executeQuery("SELECT COUNT(clientId) FROM tbl_client WHERE hide=0 AND end_date is null "+whereClause);
			if(rs.next())
				request.setAttribute("clientCount",rs.getInt(1));

			Vector<Client> clients = new Vector<Client>();
			rs = con.createStatement().executeQuery("SELECT clientId FROM tbl_client WHERE hide=0 AND end_date is null "+whereClause+" ORDER BY "+sortBy+" LIMIT "+limit+" OFFSET "+offsetClient);
			while(rs.next()){
				//if(Client.getClient(rs.getInt("clientId")).getStatus()==1)
				//	compClients.add(Client.getClient(rs.getInt("clientId")));
				//else if(Client.getClient(rs.getInt("clientId")).getStatus()==2)
				//	canClients.add(Client.getClient(rs.getInt("clientId")));
				//else
				clients.add(Client.getClient(rs.getInt("clientId")));
			}
			request.setAttribute("clients",clients);


			// Complete Clients
			rs = con.createStatement().executeQuery("SELECT COUNT(clientId) FROM tbl_client WHERE hide=0 AND end_date is not null "+whereClause);
			if(rs.next())
				request.setAttribute("compClientCount",rs.getInt(1));

			Vector<Client> compClients = new Vector<Client>();
			rs = con.createStatement().executeQuery("SELECT clientId FROM tbl_client WHERE hide=0 AND end_date is not null "+whereClause+" ORDER BY "+sortBy+" LIMIT "+limit+" OFFSET "+offsetComp);
			while(rs.next()){
				compClients.add(Client.getClient(rs.getInt("clientId")));
			}
			request.setAttribute("compClients",compClients);


			if(company == null)
				System.out.println("*************************************************Company is null************************************************");
			request.setAttribute("company", company); */

			String body = request.getParameter("body");
			if(body == null)
				body = "home.jsp";

			request.setAttribute("body", "/WEB-INF/jsp/"+body);

			getLayoutPage().forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
