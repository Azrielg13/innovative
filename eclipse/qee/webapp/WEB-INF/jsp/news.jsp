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

	<h2 id="nav">&nbsp;Navigation&nbsp;.:&nbsp;<a href="home">Home</a>&nbsp;>&nbsp;News</h2>

	<h4>News Headers</h4>
	<div class="bgGrey">
		<ol>
			<%int index=0;
			ResultSet rsNewsHeader = con.createStatement().executeQuery("SELECT tbl_news.entry_no, tbl_news.header, tbl_news.news, DATE_FORMAT(tbl_news.date,'%m/%d/%Y') AS sdf_date FROM tbl_news WHERE tbl_news.hide = 0 ORDER BY tbl_news.date DESC");
			while(rsNewsHeader.next()){%>
				<li><a href="news#<%=rsNewsHeader.getString("entry_no")%>"><%=rsNewsHeader.getString("header")+" ("+rsNewsHeader.getString("sdf_date")+")"%></a></li>
			<%index++;%>
			<%}%>
		</ol>
	</div>

	<%ResultSet rsAllNews = con.createStatement().executeQuery("SELECT tbl_news.entry_no, tbl_news.header, tbl_news.news, DATE_FORMAT(tbl_news.date, '%m/%d/%Y') AS sdf_date FROM tbl_news WHERE tbl_news.hide = 0 ORDER BY tbl_news.date desc");
	while (rsAllNews.next()){%>
		<div class="text-left">
			<h4><a name="<%=rsAllNews.getString("entry_no")%>"><%=rsAllNews.getString("header")+" ("+rsAllNews.getString("sdf_date")+")"%></a></h4>
			<div class="bgGrey">
				<%=rsAllNews.getString("news")%><br/>
				<a href="news#top">Back to Top</a>
			</div>
		</div>
		<br/>
	<%}%>
	<%if(index==0){%>
		<p class="error">The is no news at this time.</p>
	<%}%>



<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>

