package com.digitald4.common.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.model.Company;
import com.digitald4.common.model.User;


public class LoginServlet extends ParentServlet
{
	private final static String defaultPage="home";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(-5000);
		User user = (User)session.getAttribute("user");
		if(user != null && user.getId() != null){
			response.sendRedirect(defaultPage);
			return;
		}
		if(request.getParameter("u") != null && request.getParameter("key") != null)
			doPost(request,response);
		else
			forward2Jsp(request, response);
	}
	public String getLayoutURL(){
		return "/WEB-INF/jsp/login.jsp";
	}
	protected void forward2Jsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("body", "/WEB-INF/jsp/login.jsp");
		getLayoutPage().forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		checkEntityManager();
		HttpSession session = request.getSession();
		String username = request.getParameter("login");
		if (username == null || username.length() == 0) {
			request.setAttribute("error", "Username is required.");
			forward2Jsp(request, response);
			return;
		}
		String passwd = request.getParameter("pass");
		if (passwd == null) passwd = "";

		User user = User.getInstance(username, passwd);
		if(user != null){
			session.setAttribute("user",user);
		}
		else{
			request.setAttribute("error", "Login incorrect");
			forward2Jsp(request, response);
			return;
		}
		String redirect = (String)session.getAttribute("redirect");
		if(redirect == null)
			redirect = defaultPage;
		else
			session.removeAttribute("redirect");
		response.sendRedirect(redirect);
	}
	protected void sendPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String to = request.getParameter("to");
		if(to == null || to.length() == 0)
			to="";
		User user = User.getInstanceByEmail(to);
		if(user != null){

			String password = "";
			for(int x=0; x<6; x++)
				password+=(char)('a'+Math.random()*26);

			user.setPassword(password);

			//Send the email

			Company company = Company.getInstance();
			//String subject = company.getWebsite() + ": New Password for " + to;
			String message = "New Password for " + to + " is <b>" + password + "</b><br/><br/>"+

							"Please change change your password on the <a href=http://"+company.getWebsite()+"/account>Account Page</a> now.<br/><br/>"+

							"<p>"+
							"Please note: If you have any questions you can contact us via our website.<br>"+
							"Thank You, <a href=http://"+company.getWebsite()+">"+company.getWebsite()+"</a>."+
							"</p>";
			//String host[] = new String[]{getServletContext().getInitParameter("emailserver"),getServletContext().getInitParameter("emailuser"),getServletContext().getInitParameter("emailpass")};
			//emailer.sendmail(company.getEmail(), user.getEmail(), host, subject, message);
			request.setAttribute("action", "sent");
		}
		else{
			request.setAttribute("action", "cantSend");
		}
		forward2Jsp(request, response);
	}
}