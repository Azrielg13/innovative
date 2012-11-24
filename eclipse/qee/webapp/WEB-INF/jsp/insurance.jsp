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

Neighborhood hood = (Neighborhood)session.getAttribute("hood");

Vector canClients = (Vector)request.getAttribute("canClients");
int canClientSize = 0;
if(canClients!=null)
	canClientSize = canClients.size();

Vector compClients = (Vector)request.getAttribute("compClients");
int compClientSize = 0;
if(compClients!=null)
	compClientSize = compClients.size();

Vector clients = (Vector)request.getAttribute("clients");
int clientSize = 0;
if(clients!=null)
	clientSize = clients.size();

//Todays Date
Calendar cal = Calendar.getInstance(TimeZone.getDefault());
String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
sdf.setTimeZone(TimeZone.getDefault());
String date = sdf.format(cal.getTime());//starting date
%>

<div>
	<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;Neighborhood</h2>

	<br/>
	<c:forEach var="error" items="${errors}">
		<p class="error"><c:out value="${error}"/></p>
	</c:forEach>

	<h4 style="font-size:120%;">&nbsp;Neighborhood: <%=hood%> (<%=clientSize+canClientSize+compClientSize%> Clients)</a></h4>

	<div class="bgGrey">
		<%=hood.getDesc()%>
	</div>

	<br/>
	Sort by: <a href="neighborhood?hoodId=<%=hood.getId()%>&SortBy=last_update">Last Update</a>, <a href="neighborhood?hoodId=<%=hood.getId()%>&SortBy=last_name,first_name">Alphabetical</a>, <a href="neighborhood?hoodId=<%=hood.getId()%>&SortBy=target_date">Target Date</a>, <a href="neighborhood?hoodId=<%=hood.getId()%>&SortBy=start_date">Start Date</a>
	<br/>
	<br/>

	<div>
		<div style="float:left;width:49.5%;height:100%;">

			<div>
				<h4>&nbsp;<a href="#" onclick="showHideTable('oTb1','expand1');return false;"><img id="expand1" src="img/collapse.gif"></a>&nbsp;Active Clients (<%=clientSize%> Clients)</h4>

				<div class="bgGrey" style="height:100%;">

					<table id="oTb1">
						<%for(int i=0; i<clientSize;i++){
							Client client = (Client)clients.get(i);
							Address clientAdd = client.getAddress();%>

							<tr>
								<th colspan="2"><a href="client?clientId=<%=client.getId()%>"><%=i+1%>.&nbsp;<%=client%> (last update: <%=client.getLastUpdate()%>)</a></th>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;Home:</th>
								<td>
									<%=clientAdd%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;Contact:</th>
								<td>
									Phone: <%=client.getPhoneNo()%>, Alt Phone: <%=client.getAltPhone1()%><br/>
									Email: <%=client.getEmail()%>, Fax: <%=client.getFaxNo()%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;From:</th>
								<td>
									<%=client.getRep()%> On: <%=client.getStartDate()%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;Assigned To:</th>
								<td>
									<%=client.getAgent()%> Due: <%=client.getTargetDate()%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;</th>
								<td>
									&nbsp;
								</td>
							</tr>
						<%}if(clientSize==0){%>
							<div>There are no open clients at this time</div>
						<%}%>
					</table>
				</div>
			</div>
			<div>

				<h4>&nbsp;<a href="#" onclick="showHideTable('oTb3','expand3');return false;"><img id="expand3" src="img/collapse.gif"></a>&nbsp;Cancelled Clients (<%=canClientSize%> Clients)</h4>

				<div class="bgGrey" style="height:100%;">

					<table id="oTb3">
						<%for(int i=0; i<canClientSize;i++){
							Client client = (Client)canClients.get(i);
							Address clientAdd = client.getAddress();%>

							<tr>
								<th colspan="2"><a href="client?clientId=<%=client.getId()%>"><%=i+1%>.&nbsp;<%=client%> (last update: <%=client.getLastUpdate()%>)</a></th>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;Home:</th>
								<td>
									<%=clientAdd%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;Contact:</th>
								<td>
									Phone: <%=client.getPhoneNo()%>, Alt Phone: <%=client.getAltPhone1()%><br/>
									Email: <%=client.getEmail()%>, Fax: <%=client.getFaxNo()%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;From:</th>
								<td>
									<%=client.getRep()%> On: <%=client.getStartDate()%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;&nbsp;&nbsp;Assigned To:</th>
								<td>
									<%=client.getAgent()%> Due: <%=client.getTargetDate()%>
								</td>
							</tr>
							<tr>
								<th>&nbsp;</th>
								<td>
									&nbsp;
								</td>
							</tr>
						<%}if(canClientSize==0){%>
							<div>There are no cancelled clients at this time</div>
						<%}%>
					</table>
				</div>
			</div>

		</div>

		<div style="float:left;width:1%;height:100%;">
			&nbsp;
		</div>

		<div style="float:left;width:49.5%;height:100%;">
			<h4>&nbsp;<a href="#" onclick="showHideTable('oTb2','expand2');return false;"><img id="expand2" src="img/collapse.gif"></a>&nbsp;Completed Clients (<%=compClientSize%> Clients)</h4>

			<div class="bgGrey" style="height:100%;">

				<table id="oTb2">
					<%for(int i=0; i<compClients.size();i++){
						Client client = (Client)compClients.get(i);
						Address clientAdd = client.getAddress();%>

						<tr>
							<th colspan="2"><a href="client?clientId=<%=client.getId()%>"><%=i+1%>.&nbsp;<%=client%> (last update: <%=client.getLastUpdate()%>)</a></th>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Home:</th>
							<td>
								<%=clientAdd%>
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Contact:</th>
							<td>
								Phone: <%=client.getPhoneNo()%>, Alt Phone: <%=client.getAltPhone1()%><br/>
								Email: <%=client.getEmail()%>, Fax: <%=client.getFaxNo()%>
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;From:</th>
							<td>
								<%=client.getRep()%> On: <%=client.getStartDate()%>
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Assigned To:</th>
							<td>
								<%=client.getAgent()%> Due: <%=client.getTargetDate()%>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td>
								&nbsp;
							</td>
						</tr>
					<%}if(compClientSize==0){%>
						<div>There are no completed clients at this time</div>
					<%}%>
				</table>
			</div>
		</div>

	</div>
</div>
