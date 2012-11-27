<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Vector" %>
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

Vector exams = (Vector)request.getAttribute("exams");
int examSize = 0;
if(exams!=null)
	examSize = exams.size();

//Todays Date
Calendar cal = Calendar.getInstance(TimeZone.getDefault());
String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
sdf.setTimeZone(TimeZone.getDefault());
String date = sdf.format(cal.getTime());//starting date
cal.add(cal.DAY_OF_MONTH,1);
String targetDate = sdf.format(cal.getTime());//target date

String month_today = date.substring(0,2);
String day_today = date.substring(3,5);
String year_today = date.substring(6,10);
String time_today = date.substring(10,19);

%>

<div>
	<form name="Exams" action="clientexam" method="post">

		<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;Add Exams</h2>

		<div>
			<table class="checkout">
				<tr>
					<td>Step 1: Add Proposed Insured</td>
					<td>&gt;&gt;</td>
					<td class="checkout">Step 2: Add Exams</td>
					<td>&gt;&gt;</td>
					<td>Step 3: Assign Examiner</td>
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

		<h4><a href="#" onclick="showHideTable('t2','i2');return false;"><img id="i2" src="img/expand.gif"></a>&nbsp;Insurance Company Information</h4>
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

		<h4><a href="#" onclick="showHideTable('t1','i1');return false;"><img id="i1" src="img/expand.gif"></a>&nbsp;Agent Information</h4>
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



		<h4><a href="#" onclick="showHideTable('t3','i3');return false;"><img id="i3" src="img/expand.gif"></a>&nbsp;Proposed Insured Information</h4>
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

		<h4><a href="#" onclick="showHideTable('t4','i4');return false;"><img id="i4" src="img/collapse.gif"></a>&nbsp;Special Considerations</h4>
		<table class="bgGrey" id="t4">
			<tr><th>Primary Language:</th><td><%=client.getPrimaryLang()%></td></tr>
			<tr>
				<th>Comments:</th>
				<td width="550px">
					<%=client.getNote()%>
				</td>
			</tr>
		</table>

		<h4>Select Exam Date</h4>
		<table class="bgGrey">
			<tr>
				<th>Received Date:</th>
				<td width="550px">
					<%=client.getRecDate()%>
				</td>
			</tr>
			<tr>
				<th>Select Exam Date (Optional):</th>
				<td width="550px">
					<input name="start_date" type="textbox" size="20" value="<%=(client.getStartDate()==null?"":client.getStartDate())%>" />
					<a href="javascript:cal1.popup();"><img src="img/cal.gif" /></a>
				</td>
			</tr>
			<tr>
				<th>
					Select Preset Time (Optional):
				</th>
				<td width="550px">
					<input name="start_time" type="textbox" size="20" value="<%=(client.getStartTime()==null?"":client.getStartTime())%>" />
					e.g. "2:30pm" or "After 2pm"
				</td>
			</tr>
		</table>


		<h4>Select Requested Exams</h4>
		<table class="bgGrey">
			<tr>
				<th align="center">Select</th>
				<th>Name</th>
				<th>Description</th>
				<th>Note</th>
			</tr>
			<%TreeSet tsExam = new TreeSet();
			for(int i=0; i<examSize;i++){
				Exam exam = (Exam)exams.get(i);
				if(!exam.isHidden())
					tsExam.add(exam);
			}

			Vector v = new Vector(tsExam);
			for(int i=0; i<v.size();i++){
				Exam exam = (Exam)v.get(i);
				if(client.hasExam(exam.getId()))
					exam=client.getExam(exam.getId());%>
				<tr>
					<td valign="top" align="center">
						<input type="checkbox" name="include" value="<%=i%>" <%=(client.hasExam(exam.getId())?"CHECKED":"")%>/>
					</td>
					<td valign="top">
						<%=exam.getName()%>
					</td>
					<td valign="top">
						<%=exam.getDesc()%>
					</td>
					<td valign="top">
						<textarea cols="60" rows="1" name="note"><%=exam.getNote()%></textarea>
					</td>
				</tr>
				<input name="examId" type="hidden" size="3" maxlength="3" value="<%=exam.getId()%>" />
				<input name="name" type="hidden" size="25" maxlength="100" value="<%=exam.getName()%>" />
				<input type="hidden" name="desc" value="<%=exam.getDesc()%>" />
			<%}%>
		</table>

		<input type="image" class="right" src="img/continue.gif" />
	</form>
	<script language="JavaScript">
		<!--
		// create calendar object(s) just after form tag closed
		// specify form element as the only parameter (document.forms['formname'].elements['inputname']);
		// note: you can have as many calendar objects as you need for your application
		var cal1 = new calendar2(document.forms['Exams'].elements['start_date']);
		cal1.year_scroll = true;
		cal1.time_comp = false;
		//-->
	</script>
</div>