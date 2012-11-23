package com.digitald4.order.servlet;
import com.digitald4.order.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;
import java.text.*;

public class UploadServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			if(!checkAdminLogin(request,response))
				return;
      		uploadPage.forward(request, response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}
