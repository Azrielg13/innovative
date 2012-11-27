<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>


<%Connection con = null;
try {

	con =  DBConnector.getInstance().getConnection();
	//Class.forName("org.gjt.mm.mysql.Driver");
	//con = DriverManager.getConnection(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));
	%>

	<h2 id="nav">&nbsp;Navigation&nbsp;.:&nbsp;<a href="home">Home</a>&nbsp;>&nbsp;Links</h2>


	<h4>Useful Links</h4>

	<div>
		<div class="text-left" style="width:570px;float:left;">
			<%ResultSet rsLinks = con.createStatement().executeQuery("SELECT * FROM tbl_links WHERE hide = 0 ORDER BY name");
			while(rsLinks.next()){%>
				<h4><a style="color:#f00;" href="<%=rsLinks.getString("website")%>" target="_blank"><%=rsLinks.getString("name")%></a></h4>

				<div class="bgGrey">
					<%if(rsLinks.getString("description")!=null && rsLinks.getString("description").length()>0){%>
						Description:&nbsp;<%=rsLinks.getString("description")%><br/>
					<%}%>
					Link:&nbsp;<a href="<%=rsLinks.getString("website")%>" target="_blank"><%=rsLinks.getString("website")%></a>
				</div>

			<%}%>
		</div>
		<div style="width:10px;height:100%;float:left;">
			&nbsp;
		</div>
		<div style="width:200px;height:100%;float:left;">
			<h4>Search the Web</h4>

			<div class="bgGrey">
				<form action="http://www.google.com/search" name="google" target="_blank">
					<table>
						<tr>
							<td><a href="http://www.google.com/" target="_blank"><img src="img/search_google.gif" alt=""/></a></td>
							<td><input type="text" maxlength="256" size="15" name="q" value=""/></td>
							<td><input type="submit" value="Go" name="btnG"/></td>
						</tr>
					</table>
				</form>
				<form action="http://search.msn.com/results.asp" name="msn" method="get" target="_blank">
					<table>
						<tr>
							<td><a href="http://www.msn.com/" target="_blank"><img src="img/search_msn.gif" alt=""/></a></td>
							<td><input type="text" maxlength="256" size="15" name="MT" value=""/></td>
							<td><input type="submit" value="Go" name="B1"/></td>
						</tr>
					</table>
				</form>
				<form action="http://search.yahoo.com/bin/search" name="yahoo" method="get" target="_blank">
					<table>
						<tr>
							<td><a href="http://www.yahoo.com/" target="_blank"><img src="img/search_yahoo.gif" alt=""/></a></td>
							<td><input type="text" maxlength="256" size="15" name="p" value=""/></td>
							<td><input type="submit" value="Go" name="y1"/></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>

