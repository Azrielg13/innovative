package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import com.digitald4.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;

public class ClientServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			if(request.getParameter("clientId") == null){
				//need to send error that client not found
				response.sendRedirect("home");
				return;
			}

			// Need to deny access on user type,
			// admin & corp: all
			// rep where neighborhood is in tbl_rep_hood
			// agent where tbl_client userId = aClientId

			int clientId = Integer.parseInt(request.getParameter("clientId"));
			HttpSession session = request.getSession(true);
			Client client = Client.getClient(clientId);
			//need to log error if client does not exist
			if(client == null){
				response.sendRedirect("home");
				return;
			}
			if(!isAuthorized(request,response,client))
				return;

			session.setAttribute("client",client);

			forwardToJSP(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response){
		try{
			// EDDIE - Why send company? 7/1/06
			//I don't know 7/5/2006 EM
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/client.jsp");
      		layoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			//Not updated
			if(!checkLogin(request,response))return;
			HttpSession session = request.getSession(true);
			Client client = (Client)session.getAttribute("client");
			if(client == null){
				response.sendRedirect("home");
				return;
			}
			User user = (User)session.getAttribute("user");
			client.addMessage(request.getParameter("message"),user);
			response.sendRedirect("client?clientId="+client.getId());
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
