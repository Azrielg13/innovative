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

	<h2 id="nav">&nbsp;Navigation&nbsp;.:&nbsp;<a href="home">Home</a>&nbsp;>&nbsp;Frequently Asked Questions</h2>


	<h4>Questions</h4>
	<div class="bgGrey">
		<ol>
			<%int index=0;
			ResultSet rsFAQ_Q = con.createStatement().executeQuery("SELECT tbl_faq.entry_no, tbl_faq.question, tbl_faq.answer FROM tbl_faq WHERE tbl_faq.hide = 0 ORDER BY tbl_faq.entry_no");
			while (rsFAQ_Q.next()){%>
				<li><a href="faq#<%=rsFAQ_Q.getString(1)%>"><%=rsFAQ_Q.getString(2)%></a></li>
			<%index++;
			}%>
		</ol>
	</div>

		<br/>

		<%ResultSet rsFAQ_A = con.createStatement().executeQuery("SELECT tbl_faq.entry_no, tbl_faq.question, tbl_faq.answer FROM tbl_faq WHERE tbl_faq.hide = 0 ORDER BY tbl_faq.entry_no");
		while (rsFAQ_A.next()){%>
			<h4><a name="<%=rsFAQ_A.getString(1)%>"><%=rsFAQ_A.getString(2)%></a></h4>
			<div class="bgGrey">
				<p><%=rsFAQ_A.getString(3)%></p>
				<p><a href="faq#top">Back to Top</a></p>
			</div>
		<%}%>
		<%if(index==0){%>
			<p>The are no FAQs at this time.</p>
		<%}%>



<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>

