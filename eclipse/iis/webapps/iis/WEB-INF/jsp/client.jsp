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
	<form name="client" action="client" method="post">

		<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;Edit Proposed Insured</h2>

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

		<h4><a href="#" onclick="showHideTable('t2','i2');return false;"><img id="i2" src="img/expand.gif"></a>&nbsp;Insurance Company Information:  <%=client.getIns()%></h4>
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

		<h4><a href="#" onclick="showHideTable('t1','i1');return false;"><img id="i1" src="img/expand.gif"></a>&nbsp;Agent Information:  <%=client.getAgent()%></h4>
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



		<h4><a href="#" onclick="showHideTable('t3','i3');return false;"><img id="i3" src="img/expand.gif"></a>&nbsp;Proposed Insured Information:  <%=client%></h4>
		<table class="bgGrey" id="t3" style="display:none">
			<tr><th>First Name:</th><td width="550px"><%=client.getFirstName()%></td></tr>
			<tr><th>Last Name:</th><td><%=client.getLastName()%></td></tr>
			<tr><th>Sex:</th><td><%=client.getSex()%></td></tr>
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
			<tr>
				<th>Exam Complete Date:</th>
				<td width="550px">
					<%// need to handle on change via AJAX - incomplete
					// need to handle client status%>
					<input name="end_date" type="textbox" size="10" value="<%=(client.getEndDate()==null?"":client.getEndDate())%>" onchange="oupdate('Client','end_date','end_date','<%=client.getId()%>','dd4')"/>
					<a href="javascript:cal1.popup();"><img src="img/cal.gif" /></a>
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

			<%
			int i;
			%>

			<div style="float:left;width:100%;height:100%;">
				<h4>&nbsp;<a href="#oTb2" onclick="showHideTable('oTb2','expand2');return false;"><img id="expand2" src="img/collapse.gif"></a>&nbsp;Messages (<%=client.getMessages().size()%>)&nbsp;&nbsp;&nbsp;<a href="#oTb4" onclick="showHideTable('oTb4','');showHideTable('oTb5','');return false;">Add a Message</a></h4>

				<div class="bgGrey" id="oTb2">

					<table id="oTb4" name="oTb4" style="display:none;">

						<tr>
							<th>#</th>
							<th>From</th>
							<th>Message</td>
							<th>Add</th>
						</tr>

						<tr>
							<th><%=client.getMessages().size()+1%></th>
							<td><input name="creator" type="textbox" size="20" value="<%=user%>" disabled/></td>
							<td><input name="message" type="textbox" size="90" value="" /></td>
							<td align="left"><a href="#" onclick="client.submit()">Add</a></td>
						</tr>
					</table>

					<table>
						<%for(i=client.getMessages().size()-1; i>=0;i--){
							Message message = (Message) client.getMessages().get(i);
							if(i==client.getMessages().size()-1){%>
								<tr>
									<th>#</th>
									<th>From</th>
									<th>Message</td>
									<th>Last Update</th>
								</tr>
							<%}%>

							<tr>
								<th><%=message.getId()%></th>
								<td><input name="creator_<%=i%>" type="textbox" size="20" value="<%=message.getCreator()%>" disabled/></td>
								<td><input name="message_<%=i%>" type="textbox" size="90" value="<%=message.getText()%>" onchange="oupdate('Message','text','message_<%=i%>','<%=client.getId()%>','<%=message.getId()%>','dd4')"/></td>
								<td><%=message.getLastUpdate()%></td>
							</tr>
						<%}if(client.getMessages().size()==0){%>
							<div id="oTb5">There are no messages at this time</div>
						<%}%>
					</table>
				</div>
			</div>
			<%if(user.getType()<=User.STAFF){%>
				<div style="float:left;width:100%;height:100%;">
					<h4>&nbsp;<a href="#oTb7" onclick="showHideTable('oTb7','expand7');return false;"><img id="expand7" src="img/collapse.gif"></a>&nbsp;Admin Only Section&nbsp;&nbsp;</h4>

					<div class="bgGrey">

						<table id="oTb7" name="oTb7">
							<tr>
								<th>Client Status:</th>
								<td width="550px">
									<select name="status" onchange="oupdate('Client','status','status','<%=client.getId()%>','dd4')">
										<%Vector statusNames = Client.getStatusNames();
										for(int s=1;s<statusNames.size();s++){%>
											<option value="<%=s%>" <%=((client.getStatus()==s)?"SELECTED":"")%>><%=statusNames.get(s)%></option>
										<%}%>
									</select>
								</td>
							</tr>
							<tr>
								<th>Archieve (Hide this client)</th>
								<td width="550px">
									<select name="hide" onchange="oupdate('Client','hide','hide','<%=client.getId()%>','dd4')">
										<%String hide[] = new String[] {"No","Yes"};
										for(int s=0;s<hide.length;s++){%>
											<option value="<%=s%>" <%=((client.isHidden()&&s==1)?"SELECTED":"")%>><%=hide[s]%></option>
										<%}%>
									</select>
								</td>
							</tr>
							<tr>
								<th>Redo Step 2:</th>
								<td width="550px">
									<a href="clientexam?clientId=<%=client.getId()%>">Modify Exams and/or Exam Date</a>
								</td>
							</tr>
							<tr>
								<th>Redo Step 3:</th>
								<td width="550px">
									<a href="assignexaminer?clientId=<%=client.getId()%>">Select Different Examiner</a>
								</td>
							</tr>

						</table>
					</div>
				</div>
			<%}%>

		</div>
	</form>
	<script language="JavaScript">
		<!--
		// create calendar object(s) just after form tag closed
		// specify form element as the only parameter (document.forms['formname'].elements['inputname']);
		// note: you can have as many calendar objects as you need for your application
		var cal1 = new calendar2(document.forms['client'].elements['end_date']);
		cal1.year_scroll = true;
		cal1.time_comp = false;
		//-->
	</script>
</div>
