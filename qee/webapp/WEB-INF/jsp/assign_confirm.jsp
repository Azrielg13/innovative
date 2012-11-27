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
User user = (User)session.getAttribute("user");
if(user == null)
	user = new User();

Client client = (Client)session.getAttribute("client");
if(client == null)
	client = new Client();
Address clientAdd = client.getAddress();
Address clientAdd2 = client.getAddress2();

%>

<div>

	<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;<a href="assignexaminer">Assign an Examiner</a>&nbsp;>&nbsp;Completed</h2>

	<div>
		<table class="checkout">
			<tr>
				<td>Step 1: Add Proposed Insured</td>
				<td>&gt;&gt;</td>
				<td>Step 2: Add Exams</td>
				<td>&gt;&gt;</td>
				<td class="checkout">Step 3: Assign Examiner</td>
			</tr>
		</table>
	</div>

	</br>

	<c:forEach var="error" items="${errors}">
		<p class="error"><c:out value="${error}"/></p>
	</c:forEach>

	<h3 style="float:left;">
		<div style="float:left">
			Order #<%=client.getId()%>&nbsp;<a href="requisition?clientId=<%=client.getId()%>" target="blank">[Print]</a>
			:&nbsp;<%=client%>
		</div>
		<div style="float:right">
			Last Update:&nbsp;<%=client.getLastUpdate()%>
		</div>
	</h3>

	<h4><a href="#" onclick="showHideTable('t2','i2');return false;"><img id="i2" src="img/expand.gif"></a>&nbsp;Insurance Company Information: <%=client.getIns()%></h4>
	<table class="bgGrey" id="t2" style="display:none">
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

	<h4><a href="#" onclick="showHideTable('t1','i1');return false;"><img id="i1" src="img/expand.gif"></a>&nbsp;Agent Information: <%=client.getAgent()%></h4>
	<table class="bgGrey" id="t1" style="display:none">
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



	<h4><a href="#" onclick="showHideTable('t3','i3');return false;"><img id="i3" src="img/expand.gif"></a>&nbsp;Proposed Insured Information: <%=client%></h4>
	<table class="bgGrey" id="t3" style="display:none">
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

	<h4><a href="#" onclick="showHideTable('t4','i4');return false;"><img id="i4" src="img/expand.gif"></a>&nbsp;Special Considerations</h4>
	<table class="bgGrey" id="t4" style="display:none">
		<tr><th>Primary Language:</th><td><%=client.getPrimaryLang()%></td></tr>
		<tr>
			<th>Comments:</th>
			<td width="550px">
				<%=client.getNote()%>
			</td>
		</tr>
	</table>

	<h4><a href="#" onclick="showHideTable('t5','i5');return false;"><img id="i5" src="img/expand.gif"></a>&nbsp;Exam Date</h4>
	<table class="bgGrey" id="t5" style="display:none">
		<tr>
			<th>Received Date:</th>
			<td width="550px">
				<%=client.getRecDate()%>
			</td>
		</tr>
		<tr>
			<th>Exam Date:</th>
			<td width="550px">
				<%=client.getStartDate()%>
			</td>
		</tr>
		<tr>
			<th>Preset Time:</th>
			<td width="550px">
				<%=client.getStartTime()%>
			</td>
		</tr>
	</table>

	<h4>Billing and Lab Companies</h4>

	<table class="bgGrey">
		<tr>
			<th>Billing Company:</th>
			<td width="550px">
				<%=(client.getBillingCo()!=null?""+client.getBillingCo():"None")%>
			</td>
		</tr>
		<tr>
			<th>Lab Company:</th></th>
			<td width="550px">
				<%//=client.getLabCo()%>
				<%=(client.getLabCo()!=null?""+client.getLabCo():"None")%>
			</td>
		</tr>
		<tr>
			<th>Lab Code:</th></th>
			<td width="550px">
				<%=client.getLabCode()%>
			</td>
		</tr>
		<tr>
			<th valign="top">Ins. Co Special Instructions:</th></th>
			<td width="550px">
				<%=client.getMailIns()%>
			</td>
		</tr>
	</table>


	<h4>Requested Exams</h4>

	<table class="bgGrey">
		<tr>
			<th>Name</th>
			<th>Description</th>
			<th>Note</th>
		</tr>

		<%for(int a=0; a<client.getExams().size();a++){
			Exam exam = (Exam)client.getExams().get(a);%>

			<tr>
				<td>
					<%=exam.getName()%>
				</td>
				<td>
					<%=exam.getDesc()%>
				</td>
				<td>
					<%=exam.getNote()%>
				</td>
			</tr>
		<%}%>

	</table>

	<h4>Examiner</h4>
	<table class="bgGrey">
		<tr>
			<th valign="top">Examiner:</th>
			<td valign="center" width="550px">
				<%=client.getExaminer()%>
			</td>
		</tr>
	</table>

	<a href="home"><img class="right" src="img/finished.gif" alt=""/></a>

</div>
