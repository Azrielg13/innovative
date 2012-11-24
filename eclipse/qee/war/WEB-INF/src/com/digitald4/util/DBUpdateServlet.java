import com.digitald4.util.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class DBUpdateServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse  response) throws IOException, ServletException {
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		try{

			Connection con = DBConnector.getInstance().getConnection();

        	//String table = request.getParameter("table");
        	//String column = request.getParameter("column");

        	String object = request.getParameter("object");
        	String field = request.getParameter("field");
        	String key = request.getParameter("key");
        	String keyValue = request.getParameter("keyValue");
        	String value = request.getParameter("value");

        	String dim = request.getParameter("dim");
        	if(dim == null || dim.equals("0"))
        		dim="";
        	else
        		dim="'";


        	//String key = request.getParameter("key");
        	//String keyValue = request.getParameter("keyValue");

        	/*if (isGood(table) && isGood(column) && value != null && key != null && isGood(object)) {
				int count = con.createStatement().executeUpdate("UPDATE "+table+" SET "+column+"="+dim+value+dim+" WHERE "+key.replace('|','\''));

        	    (Client) client.refresh();

        	    //response.getWriter().write("<count>"+count+"</count>");
        	    response.getWriter().write("<error>Valid</error>");
        	} else {
        	    response.getWriter().write("<error>Missing Parameters</error>");
        	}
        	*/

		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().write("<error>"+e.getMessage()+"</error>");
		}
    }
    public void doPost(HttpServletRequest request, HttpServletResponse  response) throws IOException, ServletException {
		doGet(request,response);
	}
    public boolean isGood(String value){
		return (value != null && value.length() > 0);
	}
}