package com.digitald4.pm.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkLogin(request,response))return;
      		// need to check that search string is at least 3 characters


      		forwardToJSP(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response){
		try{


			/*
			HttpSession session = request.getSession(true);
			Neighborhood hood = (Neighborhood)session.getAttribute("hood");
			String SortBy = (String)request.getAttribute("SortBy");
			User user = (User)session.getAttribute("user");

			// Need to edit where clause based on user type,
			// admin & corp: all
			// rep where neighborhood is in tbl_rep_hood
			// agent where tbl_client userId = aClientId

			String whereClause ="";
			if(user.getType()==User.REP){

				whereClause = "AND tbl_rep_hood.userId="+user.getId();

			}else if(user.getType()>User.REP){

				whereClause = "AND aUserId="+user.getId();

			}

			Connection con = DBConnector.getInstance().getConnection();

			Vector<Client> clients = new Vector<Client>();
			ResultSet rs = con.createStatement().executeQuery("SELECT clientId FROM tbl_client LEFT JOIN tbl_rep_hood USING(hoodId) WHERE hoodId="+hood.getId()+" "+whereClause+" ORDER BY "+SortBy);
			while(rs.next())
				clients.add(Client.getClient(rs.getInt("clientId")));

			request.setAttribute("clients",clients);

			request.setAttribute("company",company);
			*/

      		request.setAttribute("body", "/WEB-INF/jsp/search.jsp");
      		layoutPage.forward(request, response);
      		//con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
