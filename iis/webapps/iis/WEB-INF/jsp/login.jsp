<%@page import="com.digitald4.common.util.*" %>
<%@page import="com.digitald4.common.model.*" %>
<%@page import="com.digitald4.common.servlet.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>

<%
	Company company = Company.getInstance();


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

	<%}else if(action.equals("Sent")){%>

			<h4>&nbsp;Login</h4>
			<div class="bgGrey">
				A new Password has been sent to <%=to%>.

				<br/><br/><br/>

				<a href="home"><img src="img/continue.gif"></a>
			</div>

	<%}else if(action.equals("cantSend")){%>

			<h4>&nbsp;Login</h4>
			<div class="bgGrey">
				<p class="error"><%=(to==null||to.equals("")?"Blank":to)%>, is not a vaild email.</p>

				If you feel that this is an error please <a href="contact">contact us.</a>

				<br/><br/>

				<a href="login"><img src="img/continue.gif"></a>
			</div>

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

