package com.digitald4.pm.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.pm.User;
import com.digitald4.util.DBConnector;

public class LoginServlet extends ParentServlet
{
	private final static String defaultPage="home";
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
{
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(-5000);
		User user = (User)session.getAttribute("user");
		if(user != null && user.getId() > 0){
      		response.sendRedirect(defaultPage);
			return;
		}
		if(request.getParameter("u") != null && request.getParameter("key") != null)
			doPost(request,response);
		else
			forward2Jsp(request, response);
   }
	protected void forward2Jsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		if(user == null){
			user = new User();
			session.setAttribute("user",user);
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

      User user = (User)session.getAttribute("user");
		if(user == null){
			user = new User();
      		session.setAttribute("user", user);
		}
      try {
			user.setEmail(userid);
			Connection con = DBConnector.getInstance().getConnection();
			ResultSet rs = con.createStatement().executeQuery("SELECT userid"
			+" FROM tbl_user WHERE email=\""+userid+"\" AND password=MD5('"+passwd+"')");
			if(rs.next()){
				user = User.getUser(rs.getInt("userid"));
				session.setAttribute("user",user);
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
		redirect = defaultPage;
	else
		session.removeAttribute("redirect");
      response.sendRedirect(redirect);
   }
}
