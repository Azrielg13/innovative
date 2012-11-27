package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import com.digitald4.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
public class ParentServlet extends HttpServlet{
	RequestDispatcher layoutPage;
	RequestDispatcher layoutPage2;
	RequestDispatcher adminLayoutPage;
	RequestDispatcher adminSearchPage;
	RequestDispatcher uploadPage;
	Company company;
   public void init() throws ServletException{
      ServletContext context = getServletContext();
      layoutPage = context.getRequestDispatcher("/WEB-INF/jsp/layout.jsp");
      if (layoutPage == null) {
         throw new ServletException("/WEB-INF/jsp/layout.jsp not found");
      }
      layoutPage2 = context.getRequestDispatcher("/WEB-INF/jsp/layout2.jsp");
      if (layoutPage2 == null) {
         throw new ServletException("/WEB-INF/jsp/layout2.jsp not found");
      }

      adminLayoutPage = context.getRequestDispatcher("/WEB-INF/jsp/adminLayout.jsp");
      if (adminLayoutPage == null) {
	      throw new ServletException("/WEB-INF/jsp/adminLayout.jsp not found");
      }

	  adminSearchPage = context.getRequestDispatcher("/WEB-INF/jsp/admin_search.jsp");
	  if (adminSearchPage == null) {
		   throw new ServletException("/WEB-INF/jsp/adminSearch.jsp not found");
      }

	  uploadPage = context.getRequestDispatcher("/WEB-INF/jsp/upload.jsp");
	  if (uploadPage == null) {
		   throw new ServletException("/WEB-INF/jsp/upload.jsp not found");
      }

	  try{
		DBConnector.getInstance(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));
	  	company = Company.getCompany();
		if(company == null)
			System.out.println("*************************************company is null***********************************");
		else
			System.out.println("**************************company: "+company.getName()+"*********************************");

	  }
	  catch(Exception e){
		System.out.println("************************************error getting company*********************************");
	  	e.printStackTrace();
	  }
   }
   protected void goBack(HttpServletRequest request, HttpServletResponse response)throws IOException{
	HttpSession session = request.getSession(true);
      String backPage = (String) session.getAttribute("backPage");
		if(backPage != null){
      	session.removeAttribute("backPage");
      	response.sendRedirect(backPage);
		}
		else
			response.sendRedirect("home");
   }
   /*public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");
			if(user == null){
				user = new User();
				session.setAttribute("user",user);
			}
		return true;
	}*/
   public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		if(session.getAttribute("user") == null || ((User)session.getAttribute("user")).getId() < 1){
			session.setAttribute("redirect",request.getRequestURL().toString());
			response.sendRedirect("login");
			return false;
		}
		return true;
	}
   public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response, int level)throws ServletException, IOException{
		if(!checkLogin(request,response)) return false;
		HttpSession session = request.getSession(true);
		if(((User)session.getAttribute("user")).getType() > level){
			response.sendRedirect("denied");
			return false;
		}
		return true;
   }
   public static boolean checkAdminLogin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
	   return checkLogin(request,response,User.ADMIN);
   }
   public static boolean isAuthorized(HttpServletRequest request, HttpServletResponse response, Client client)throws ServletException, IOException{
	   if(checkLogin(request,response)){
		   boolean ret=false;
		   User user = (User)request.getSession(true).getAttribute("user");
		   if(user.getType() <= User.STAFF)
		   		ret = true;
		   else if(user.getType() == User.AGENT)
		   		ret = (client.getAgent().getId() == user.getId());
		   else if(user.getType() == User.EXAMINER)
		   		ret = (client.getExaminer().getId() == user.getId());
		   if(!ret)
		   		response.sendRedirect("denied");
		   return ret;
	   }
	   return false;
   }
}
