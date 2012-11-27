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

<%
Client client = (Client)session.getAttribute("newclient");
if(client == null)
	client = new Client();
Address clientAdd = client.getAddress();
Address clientAdd2 = client.getAddress2();

%>

<div>
	<form name="confirm" action="addclientconfirm" method="post">

		<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;<a href="addclient">New Client Entry Form</a>&nbsp;>&nbsp;Confirm</h2>

		<div>
			<table class="checkout">
				<tr>
					<td class="checkout">Step 1: Add Proposed Insured</td>
					<td>&gt;&gt;</td>
					<td>Step 2: Add Exams</td>
					<td>&gt;&gt;</td>
					<td>Step 3: Assign Examiner</td>
				</tr>
			</table>
		</div>

		</br>

		<c:forEach var="error" items="${errors}">
			<p class="error"><c:out value="${error}"/></p>
		</c:forEach>


		<h2>Please Confirm Proposed Insured Information</h2>

		<h4>Insurance Company Information</h4>
		<table class="bgGrey">
			<tr>
				<th>Insurance Company:</th>
				<td width="550px">
					<%=client.getIns()%>
				</td>
			</tr>
			<tr>
				<th>Insurance Type</th>
				<td><%=client.getInsType()%></td>
			</tr>
			<tr>
				<th>Insurance Amount ($)</th>
				<td><%=client.getInsAmount()%></td>
			</tr>
		</table>

		<h4>Agent Information</h4>
		<table class="bgGrey">
			<tr>
				<th>Agent:</th>
				<td width="550px">
					<%=client.getAgent()%>
				</td>
			</tr>
			<%User agent = (User)client.getAgent();
			Address agentAdd = client.getAddress();%>

			<tr>
				<th>&nbsp;&nbsp;Contact Info:</th>
				<td width="550px">Email: <%=agent.getEmail()%>, Phone: <%=agent.getPhoneNo()%>, Alt Phone: <%=agent.getFaxNo()%></td>
			</tr>
				<th>&nbsp;&nbsp;Address</th>
				<td width="550px"><%=agentAdd%></td>
			</tr>
			<tr>
				<th>Agent Code:</th>
				<td width="550px">
					<%=client.getAgentCode()%>
				</td>
			</tr>
			<tr>
				<th>Agency:</th>
				<td width="550px">
					<%=client.getAgency()%>
				</td>
			</tr>
			<tr>
				<th>Agency Code:</th>
				<td width="550px">
					<%=client.getAgencyCode()%>
				</td>
			</tr>
		</table>



		<h4>Proposed Insured Information</h4>
		<table class="bgGrey">
			<tr><th>First Name:</th><td width="550px"><%=client.getFirstName()%></td></tr>
			<tr><th>Last Name:</th><td><%=client.getLastName()%></td></tr>

			<tr><th>Date of Birth:</th><td><%=client.getDOB()%></td></tr>
			<tr><th>Age:</th><td><%=client.getAge()%></td></tr>
			<tr><th>Social Secutiry Number:</th><td><%=client.getSSN()%></td></tr>

			<tr><th>Phone Number:</th><td><%=client.getPhoneNo()%></td></tr>
			<tr><th>Alt Phone Number 1:</th><td><%=client.getAltPhone1()%></td></tr>
			<tr><th>Alt Phone Number 2:</th><td><%=client.getAltPhone2()%></td></tr>

			<tr><th>Address of Examination:</th><td><%=clientAdd.getStreet1()%></td></tr>
			<tr><th>City:</th><td><%=clientAdd.getCity()%></td></tr>
			<tr><th>State:</th><td><%=clientAdd.getState()%></td></tr>
			<tr><th>Zip Code:</th><td><%=clientAdd.getZip()%></td></tr>

			<tr><th>Alt. Address:</th><td><%=clientAdd2.getStreet1()%></td></tr>
			<tr><th>City:</th><td><%=clientAdd2.getCity()%></td></tr>
			<tr><th>State:</th><td><%=clientAdd2.getState()%></td></tr>
			<tr><th>Zip Code:</th><td><%=clientAdd2.getZip()%></td></tr>
		</table>

		<h4>Special Considerations</h4>
		<table class="bgGrey">
			<tr><th>Primary Language:</th><td><%=client.getPrimaryLang()%></td></tr>
			<tr>
				<th>Comments:</th>
				<td width="550px">
					<%=client.getNote()%>
				</td>
			</tr>
		</table>

		<a href="addclient"><img class="left" src="img/edit.gif" alt=""/></a>
		<input type="image" class="right" src="img/continue.gif" />

	</form>
</div>
