<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Vector" %>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>

<% Connection con = null;

try {

	con =  DBConnector.getInstance().getConnection();

	User user = (User)session.getAttribute("user");
	if(user == null)
		user = new User();
	Address userAdd = user.getAddress();

	//Customer cust = (Customer)session.getAttribute("cust");
	//Address address = cust.getAddress();
	Vector states = (Vector)request.getAttribute("states");%>

	<div>

		<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;My Account</h2>
		<br/>
		<c:forEach var="error" items="${errors}">
			<p class="error"><c:out value="${error}"/></p>
		</c:forEach>

		<h4>&nbsp;<a href="#" onclick="showHideTable('uTbl','expand1');return false;"><img id="expand1" src="img/expand.gif"></a>&nbsp;Email and Password</h4>

		<div class="bgGrey">

			Change your sign in settings

			<form name="uForm" action="account" method="post">
				<table id="uTbl" style="display:none">
					<tr><th>Email (Username):</th><td><input type="text" name="user" size="50" maxlength="100" value="<%=user.getEmail()%>"/></td></tr>
					<tr><th>New Password:</th><td><input type="password" name="pass" size="20" maxlength="32" /></td></tr>
					<tr><th>Confirm Password:</th><td><input type="password" name="cpass" size="20" maxlength="32" /></td></tr>
					<tr>
						<td><input type="hidden" name="action" value="user"/></td>
						<td><input type="image" src="img/continue.gif" /></td>
					</tr>
				</table>
			</form>
		</div>


		<h4>&nbsp;<a href="#" onclick="showHideTable('cTbl','expand2');return false;"><img id="expand2" src="img/expand.gif"></a>&nbsp;Contact Information</h4>

		<div class="bgGrey">
			Change your personal data

			<form name="cForm" action="account" method="post">
				<table id="cTbl" style="display:none">
					<tr><th>First Name:</th><td><input name="fname" type="textbox" size="50" maxlength="50" value="<%=user.getFirstName()%>" /></td></tr>
					<tr><th>Last Name:</th><td><input name="lname" type="textbox" size="50" maxlength="50" value="<%=user.getLastName()%>" /></td></tr>
					<tr><th>Address 1:</th><td><input name="street1" type="textbox" size="50" maxlength="50" value="<%=userAdd.getStreet1()%>" /></td></tr>
					<tr><td colspan="2">Street address, P.O. box, company name, c/o</td></tr>
					<tr><th>Address 2:</th><td><input name="street2" type="textbox" size="50" maxlength="50" value="<%=userAdd.getStreet2()%>" /></td></tr>
					<tr><td colspan="2">Apartment, suite, unit, building, floor, etc. </td></tr>
					<tr><th>City:</th><td><input name="city" type="textbox" size="20" maxlength="20" value="<%=userAdd.getCity()%>" /></td></tr>
					<tr><th>State:</th><td><dd4:select name="state" optionValues="<%=states%>" selected="<%=userAdd.getState()%>"/></td></tr>
					<tr><th>ZIP/Postal Code:</th><td><input name="zip" type="textbox" size="5" maxlength="5" value="<%=userAdd.getZip()%>" /></td></tr>
					<tr><th>Phone Number:</th><td><input name="phone" type="textbox" size="20" maxlength="20" value="<%=user.getPhoneNo()%>" /></td></tr>
					<tr><th>Alt Phone Number:</th><td><input name="fax" type="textbox" size="20" maxlength="20" value="<%=user.getFaxNo()%>" /></td></tr>
					<tr>
						<td><input type="hidden" name="action" value="account"/></td>
						<td><input type="image" src="img/continue.gif" /></td>
					</tr>
				</table>
			</form>
		</div>

	</div>

<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>
