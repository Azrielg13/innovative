package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class AgentInfoAjax extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse  response) throws IOException, ServletException {
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% AJAX Request Caught %%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		String agentCode="", agency="", agencyCode="";
		try{
        	String agentId = request.getParameter("agentid");
        	String insId = request.getParameter("insid");
        	if(agentId != null && agentId.length() > 0 && insId != null && insId.length() > 0){
				User agent = User.getUser(Integer.parseInt(agentId));
				InsuranceCo ins = InsuranceCo.getInsuranceCo(Integer.parseInt(insId));

				String[] agentInfo = agent.getAgentInfo(ins);
				agentCode = agentInfo[0];
				if(agentCode == null)
					agentCode = "";
				agency = agentInfo[1];
				if(agency == null)
					agency = "";
				agencyCode = agentInfo[2];
				if(agencyCode == null)
					agencyCode = "";
			}
		}catch(Exception e){
			e.printStackTrace();
			//response.getWriter().write("<agentcode>"+e.getMessage()+"</agentcode>");
		}
		response.getWriter().write("<agent agentCode='"+agentCode+"' agency='"+agency+"' agencyCode='"+agencyCode+"'></agent>");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse  response) throws IOException, ServletException {
		doGet(request,response);
	}
}