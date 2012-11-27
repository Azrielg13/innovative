package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import com.digitald4.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Vector;

public class ClientExamServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");
			if(user == null)
				user = new User();
			if(request.getParameter("clientId") == null){
				response.sendRedirect("home");
				return;
			}
			Client client = Client.getClient(Integer.parseInt(request.getParameter("clientId").toString()));
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
			Connection con = DBConnector.getInstance().getConnection();
			Vector<Exam> exams = new Vector<Exam>();
			ResultSet rs = con.createStatement().executeQuery("SELECT tbl_exam_type.*, 0 status, NULL finish_date, '' note, 0 price  FROM tbl_exam_type WHERE hide=0 ORDER BY examId");
			while(rs.next())
				exams.add(new Exam(null,rs.getInt("examId"),rs));
			request.setAttribute("exams",exams);
			request.setAttribute("company",company);
      		request.setAttribute("body", "/WEB-INF/jsp/exams.jsp");
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
				response.sendRedirect("home");
				return;
			}

			client.setStartDate(request.getParameter("start_date"));
			client.setStartTime(request.getParameter("start_time"));
			//client.setTargetDate(request.getParameter("target_date"));

			String[] include = request.getParameterValues("include");
			String[] examIds = request.getParameterValues("examId");
			String[] examNames = request.getParameterValues("name");
			String[] examDescs = request.getParameterValues("desc");
			String[] examNotes = request.getParameterValues("note");

			Vector<Exam> exams = new Vector<Exam>();
			for(int e=0; e<include.length; e++){
				int i = Integer.parseInt(include[e]);
				Exam exam = new Exam(Integer.parseInt(examIds[i]));
				exam.setName(examNames[i]);
				exam.setDesc(examDescs[i]);
				exam.setNote(examNotes[i]);
				exam.setClient(client);
				exams.add(exam);
			}
			if(exams.size() > 0){
				client.setExams(exams);

				if(((User)session.getAttribute("user")).getType() <= User.STAFF)
					response.sendRedirect("assignexaminer?clientId="+client.getId());
				else
					response.sendRedirect("home");
			}
			else{
				Vector<String> errors = new Vector<String>();
				errors.add("Please select at least one exam");
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
