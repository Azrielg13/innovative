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
	//Class.forName("org.gjt.mm.mysql.Driver");
	//con = DriverManager.getConnection(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));

	String host[] = new String[]{getServletContext().getInitParameter("emailserver"),getServletContext().getInitParameter("emailuser"),getServletContext().getInitParameter("emailpass")};

	String action = request.getParameter("Action");
		if(action == null || action.length() == 0){
		action="";
	}

	String from_name = request.getParameter("From_Name");
		if(from_name == null || from_name.length() == 0){
		from_name="noname";
	}

	String from = request.getParameter("From");
		if(from == null || from.length() == 0){
		from="noemail";
	}

	String message = request.getParameter("Message");
		if(message == null || message.length() == 0){
		message="nomessage";
	}
	%>

	<h2 id="nav">&nbsp;Navigation&nbsp;.:&nbsp;<a href="home">Home</a>&nbsp;>&nbsp;Contact Us</h2>


	<%if(action.equals("Submit")){

		//Send the email
		String to = company.getEmail();
		String subject = company.getWebsite() + ": Inquire from " + from_name;
		emailer.sendmail(from, to, host, subject, message);


		//Send autoresponse
		to = from;
		from = company.getEmail();
		subject = "Auto Response from " + company.getWebsite();
		message = from_name+",<br/><br/>Thank you for interest in "+company.getWebsite()+", we will respond to your email as soon as possible.<br/><br/>Thanks!<br/><br/>"+company.getName();
		//emailer.sendmail(from, to, host, subject, message);
		%>

		<h4>Message Sent</h4>
		<div class="bgGrey">
			<%=message%>

			<br/><br/><br/>

			<a href="home"><img src="img/continue.gif"></a>
		</div>

	<%}else{%>

		<div class="text-left">

			<h4>Contact Information</h4>

			<div class="bgGrey">
				<a>
					If you have any questions, you can contact us via the information below and we'll get back to you as soon as possible.
				</a>
				<br/><br/>
				<c:out value="${company.name}"/><br/>
				<%=(company.getAddress()==null || company.getAddress().length()==0?"":company.getAddress()+"<br/>")%>
				<%=(company.getPhone()==null || company.getPhone().length()==0?"":"Phone "+company.getPhone()+"<br/>")%>
				<%=(company.getFax()==null || company.getFax().length()==0?"":"FAX "+company.getFax()+"<br/>")%>
			</div>


			<br/>
			<!--
			<h4>Send Us an Email</h4>

			<form class="bgGrey" action="contact" name="contact" method="get">

				<table>
					<tr>
						<td>Your Name</td>
						<td><input name="From_Name" class="searchform" type="text" size="50" maxlength="50"/></td>
					</tr>
					<tr>
						<td>Your Email</td>
						<td><input name="From" class="searchform" type="text" size="50" maxlength="50"/></td>
					</tr>
				</table>
				Comments/Questions:<br/>
				<textarea name="Message" rows="6" cols="35"></textarea><br/><br/>
				<input type="hidden" name="Action" value="Submit"/>
				<input type="image" src="img/continue.gif" />

			</form>
			//-->
		</div>
	<%}%>

<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>
