package com.digitald4.util;
import com.digitald4.pm.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class ObjectUpdateServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse  response) throws IOException, ServletException {
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% AJAX Request Caught %%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		try{
        	String object = request.getParameter("object");
        	String field = request.getParameter("field");
        	String keys[] = request.getParameterValues("key");
        	String value = request.getParameter("value");
        	if (isGood(object) && isGood(field) && value != null && keys != null) {
				DD4Object o=null;
				int id = Integer.parseInt(keys[0]);
        	    switch(object.charAt(0)){
					case 'C': o = Client.getClient(id); break;
					case 'E':
					case 'M': o = Client.getClient(id); break;
					case 'U': o = User.getUser(id); break;
					default: response.getWriter().write("<error>Unknown object: "+object+"</error>"); return;
				}
				if(o != null){
					if((object.charAt(0) == 'E' || object.charAt(0) == 'M')){
						if(keys.length < 2){
							response.getWriter().write("<error>Missing Parameters</error>");
							return;
						}
						if(object.charAt(0) == 'E')
							o = ((Client)o).getExam(Integer.parseInt(keys[1]));
						else if(object.charAt(0) == 'M')
							o = ((Client)o).getMessage(Integer.parseInt(keys[1]));
					}
					if(o.setField(field,value))
						response.getWriter().write("<error>Valid</error>");
					else
						response.getWriter().write("<error>Invalid field: "+field+" for object: "+object+"</error>");
				}
				else{
					response.getWriter().write("<error>Object instance not found</error>");
				}
        	} else {
        	    response.getWriter().write("<error>Missing Parameters</error>");
        	}
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