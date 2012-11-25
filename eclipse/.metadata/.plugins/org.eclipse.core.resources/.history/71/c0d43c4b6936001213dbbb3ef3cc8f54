package com.digitald4.order.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkAdminLogin(request,response))
				return;
      		//String adminBody = request.getParameter("adminBody");
      		//if(adminBody == null)
      		//	adminBody = "admin.jsp";

      		//request.setAttribute("adminBody", "/WEB-INF/jsp/"+adminBody);

			request.setAttribute("adminBody", "/WEB-INF/jsp/admin.jsp");
			System.out.println("--------------------------------adminBody = Admin.jsp-------------------------------");
			if(company == null)
				System.out.println("*************************************************Company is null************************************************");
      		request.setAttribute("company", company);

      		adminLayoutPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
