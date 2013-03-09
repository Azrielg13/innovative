package com.digitald4.common.servlet;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.GenData;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
public class ParentServlet extends HttpServlet{
	private RequestDispatcher layoutPage;
	public void init() throws ServletException{
		System.out.println("********************** Init for "+this);
		checkEntityManager();
		ServletContext sc = getServletContext();
		layoutPage = sc.getRequestDispatcher(getLayoutURL());
		if (layoutPage == null) {
			throw new ServletException(getLayoutURL()+" not found");
		}
	}
	public static boolean isAjax(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equalsIgnoreCase("xmlhttprequest");
	}
	public void checkEntityManager() throws ServletException{
		ServletContext sc = getServletContext();
		if(EntityManagerHelper.getEntityManager()==null){
			try{
				System.out.println("***********HHHHHH###### Loading driver");
				EntityManagerHelper.init(sc.getInitParameter("dbdriver"), 
						sc.getInitParameter("dburl"), 
						sc.getInitParameter("dbuser"), 
						sc.getInitParameter("dbpass"));
			}
			catch(Exception e){
				System.out.println("************************************error init entity manager*********************************");
				throw new ServletException(e);
			}
		}
	}
	public RequestDispatcher getLayoutPage(){
		return layoutPage;
	}
	public String getLayoutURL(){
		return "/WEB-INF/jsp/layout.jsp";
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
	public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response)throws Exception{
		HttpSession session = request.getSession(true);
		if(session.getAttribute("user") == null || ((User)session.getAttribute("user")).getId() == null)
			session.setAttribute("user", User.getInstance(1));
		if(session.getAttribute("user") == null || ((User)session.getAttribute("user")).getId() == null){
			session.setAttribute("user", User.getInstance(1));
			session.setAttribute("redirect",request.getRequestURL().toString());
			response.sendRedirect("login");
			return false;
		}
		return true;
	}
	public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response, GeneralData level)throws Exception{
		if(!checkLogin(request,response)) return false;
		HttpSession session = request.getSession(true);
		if(((User)session.getAttribute("user")).isOfRank(level)){
			response.sendRedirect("denied");
			return false;
		}
		return true;
	}
	public static boolean checkAdminLogin(HttpServletRequest request, HttpServletResponse response)throws Exception{
		return checkLogin(request,response,GenData.UserType_Admin.get());
	}
}
