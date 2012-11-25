<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@page import="com.sce.mdi.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>

<%
Connection con = null;

try {

	Company company = (Company)request.getAttribute("company");
	con =  DBConnector.getInstance().getConnection();

	String host[] = new String[]{getServletContext().getInitParameter("emailserver"),getServletContext().getInitParameter("emailuser"),getServletContext().getInitParameter("emailpass")};


	String to = request.getParameter("to");
	if(to == null || to.length() == 0)
		to="";

	String action = request.getParameter("Action");
	if(action == null || action.length() == 0)
		action="";

	String rem = request.getParameter("rem");
	if(rem == null || rem.length() == 0)
		rem = "0";
	%>

	<h2 id="nav">&nbsp;Navigation .: Login</h2>

	<%if(action.equals("Forgot")){%>

		<h4>&nbsp;Login</h4>
		<form class="bgGrey" name="forgot" action="login" method="post">

			<label for="usr">Enter your Email Address:</label>
			   <input type="text" name="to" id="usr" size="30" value="<%=((User)session.getAttribute("user")).getEmail()%>"/>
			<br/>

			<input type="image" src="img/continue.gif" />
			<input name="Action" type="hidden" value="Send" />

		</form>

	<%}else if(action.equals("Send")){

		ResultSet rsC = con.createStatement().executeQuery("SELECT tbl_user.email FROM tbl_user WHERE tbl_user.email = '"+to+"'");
		if(rsC.next()){

			String password = "";
			for(int x=0; x<6; x++)
				password+=(char)('a'+Math.random()*26);

			con.createStatement().executeUpdate("UPDATE IGNORE tbl_user SET password=MD5('"+password+"') WHERE tbl_user.email='"+to+"'");

			//Send the email

			String from = company.getEmail();
			String subject = company.getWebsite() + ": New Password for " + to;
			String message = "New Password for " + to + " is <b>" + password + "</b><br/><br/>"+

							"Please change change your password on the <a href=http://"+getServletContext().getInitParameter("website")+"/account>Account Page</a> now.<br/><br/>"+

							"<p>"+
								"Please note: If you have any questions you can contact us via our website.<br>"+
								"Thank You, <a href=http://"+getServletContext().getInitParameter("website")+">"+getServletContext().getInitParameter("website")+"</a>."+
							"</p>";

			emailer.sendmail(from, to, host, subject, message);
			%>

			<h4>&nbsp;Login</h4>
			<div class="bgGrey">
				A new Password has been sent to <%=to%>.

				<br/><br/><br/>

				<a href="home"><img src="img/continue.gif"></a>
			</div>

		<%}else{%>

			<h4>&nbsp;Login</h4>
			<div class="bgGrey">
				<p class="error"><%=(to==null||to.equals("")?"Blank":to)%>, is not a vaild email.</p>

				If you feel that this is an error please <a href="contact">contact us.</a>

				<br/><br/>

				<a href="login"><img src="img/continue.gif"></a>
			</div>

		<%}%>

	<%}else{%>

		<div class="error">
		  <% String error = (String) request.getAttribute("error");
		   if (error != null) {
			  out.print(error);
		   }%>
		</div>

		<h4>&nbsp;Login</h4>
		<form class="bgGrey" name="loginF" action="login" method="post" onLoad="readCookieSetInput('login','u')" onSubmit="if(getElementById('rem').checked){makeCookie('login', getElementById('u').value, { expires: 30 });}else{rmCookie('login');}">

			<label for="usr">Enter your Email Address:</label>
			   <input type="text" name="u" id="usr" size="30" value="<%=((User)session.getAttribute("user")).getEmail()%>"/>
			<br/>
			<label for="pss">Enter your Password:</label>
			   <input type="password" name="key" id="pss" size="30" />

			<br/>
			<input type="checkbox" name="rem" checked/>Remember me on this computer<br/>

			<input type="image" src="img/continue.gif" />

		</form>
	<%}%>

<%} catch(Exception e) {%>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>

