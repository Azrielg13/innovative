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

//int examinerCount = ((Integer)request.getAttribute("examinerCount")).intValue();
//int offsetExaminer = ((Integer)request.getAttribute("offsetExaminer")).intValue();

String offsetExaminer_s = "0";
if(request.getParameter("offsetExaminer") != null){
	offsetExaminer_s = request.getParameter("offsetExaminer");
}
int offsetExaminer = Integer.parseInt(offsetExaminer_s);

Vector examiners = (Vector)request.getAttribute("examiners");
int examinerCount = 0;
if(examiners!=null)
	examinerCount = examiners.size();

Vector billCos = (Vector)request.getAttribute("billCos");
int bSize = 0;
if(billCos!=null)
	bSize = billCos.size();

Vector labCos = (Vector)request.getAttribute("labCos");
int lSize = 0;
if(labCos!=null)
	lSize = labCos.size();


String limit_s = "25";
if(request.getParameter("limit") != null){
	limit_s = request.getParameter("limit");
}
int limit = Integer.parseInt(limit_s);

String sortBy = (String)request.getAttribute("sortBy");
if(sortBy == null || sortBy.length() == 0)
	sortBy="";

%>

<div>
	<form name="assign" action="assignexaminer" method="post">
	<input name="redirect" type="hidden" value=""/>

		<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;Assign an Examiner</h2>

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

		<h4>Billing and Lab Companies (Default to same as Insurance Co)</h4>

		<table class="bgGrey">
			<tr>
				<th>Billing Company:</th>
				<td width="550px">
					<select name="billingId">
						<option value="0"> -- Select -- </option>
						<%
						for(int a=0; a<bSize;a++){
							BillingCo b = (BillingCo)billCos.get(a);%>
							<option value="<%=b.getId()%>" <%=(client.getBillingCo()!=null && b.getId()==client.getBillingCo().getId()?"SELECTED":"")%>><%=b%></option>
						<%}%>
					</select>
					<input type="button" value="Add Billing Co" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_billing_co&amp;ASAction=Create&amp;reURL=assignexaminer';assign.submit();"/>
					<input type="button" value="Edit Default Billing Co" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_insurance_co&amp;ASAction=Edit&amp;Where_Clause=(tbl_insurance_co.insuranceId=<%=client.getIns().getId()%>)&amp;reURL=assignexaminer';assign.submit();"/>
				</td>
			</tr>
			<tr>
				<th>Lab Company:</th></th>
				<td width="550px">
					<select name="labId">
						<option value="0"> -- Select -- </option>
						<%
						for(int a=0; a<lSize;a++){
							LabCo b = (LabCo)labCos.get(a);%>
							<option value="<%=b.getId()%>" <%=(client.getLabCo()!=null && b.getId()==client.getLabCo().getId()?"SELECTED":"")%>><%=b%></option>
						<%}%>
					</select>
					<input type="button" value="Add Lab Co" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_lab_co&amp;ASAction=Create&amp;reURL=assignexaminer';assign.submit();"/>
					<input type="button" value="Edit Default Lab Co" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_insurance_co&amp;ASAction=Edit&amp;Where_Clause=(tbl_insurance_co.insuranceId=<%=client.getIns().getId()%>)&amp;reURL=assignexaminer';assign.submit();"/>
				</td>
			</tr>
			<tr>
				<th>Lab Code:</th></th>
				<td width="550px">
					<input name="labCode" type="textbox" size="20" maxSize="20" value="<%=client.getLabCode()%>" />
					<input type="button" value="Edit Default Lab Code" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_insurance_co&amp;ASAction=Edit&amp;Where_Clause=(tbl_insurance_co.insuranceId=<%=client.getIns().getId()%>)&amp;reURL=assignexaminer';assign.submit();"/>
				</td>
			</tr>
			<tr>
				<th valign="top">Mailing Instructions:</th></th>
				<td width="550px">
					<textarea cols="50" rows="3" name="instructions"><%=client.getMailIns()%></textarea>
					<input type="button" value="Edit Default Instructions" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_insurance_co&amp;ASAction=Edit&amp;Where_Clause=(tbl_insurance_co.insuranceId=<%=client.getIns().getId()%>)&amp;reURL=assignexaminer';assign.submit();"/>
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

		<h4 id="examiner">
			Assign An Examiner:

			<%for (int i=0; i<examinerCount; i=i+limit){
				if(i == offsetExaminer){
					if(i+limit > examinerCount){%>
						&nbsp;<a><%=i+1%> - <%=examinerCount%></a>
					<%}else{%>
						&nbsp;<a><%=i+1%> - <%=i+limit%></a>
					<%}%>
				<%}else{
					if(i+limit > examinerCount){%>
						&nbsp;<a href="assignexaminer?clientId=<%=client.getId()%>&amp;offsetExaminer=<%=i%>&amp;sortBy=<%=sortBy%>#examiner"><%=i+1%> - <%=examinerCount%></a>
					<%}else{%>
						&nbsp;<a href="assignexaminer?clientId=<%=client.getId()%>&amp;offsetExaminer=<%=i%>&amp;sortBy=<%=sortBy%>#examiner"><%=i+1%> - <%=i+limit%></a>&nbsp;
					<%}%>
				<%}%>
			<%}%>
		</h4>


		<table class="bgGrey">
			<tr>
				<th valign="top">Select Examiner:</th>
				<td align="left" width="550px">

					<%if(User.getUser(client.getExaminer().getId())!=null){

						// Need to show % Match for selected examiner NTD
						//SortableExaminer se = (SortableExaminer)Examiner.getUser(client.getExaminer().getId());
						User examinerS = User.getUser(client.getExaminer().getId());
						Address addS = examinerS.getAddress();%>

						<h4 style="color:#f00">
							SELECTED EXAMINER: <input type="radio" name="examinerid" value="<%=examinerS.getId()%>" CHECKED/>
							<%=examinerS%> &nbsp;&nbsp;&nbsp;&nbsp;
						</h4>

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Address:</b> <%=addS%><br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Contact:</b> Email: <%=examinerS.getEmail()%>, Phone: <%=examinerS.getPhoneNo()%><br/>

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Exams Offered:</b>

							<%for(int a=0; a<examinerS.getExams().size();a++){
								Exam exam = (Exam)examinerS.getExams().get(a);
								if(a!=0){%>
									,&nbsp;
								<%}%>
									<%=exam.getName()%> (<%=exam.getPrice()%>)
							<%}%>

						<br/>
						<br/>
						<br/>
					<%}%>

					<%
					for(int i=offsetExaminer; i<examiners.size() && i<offsetExaminer+limit; i++){
					//for(int i=0; i<examinerSize;i++){

						SortableExaminer se = (SortableExaminer)examiners.get(i);
						User examiner = se.getExaminer();

						Address add = examiner.getAddress();

						%>
						<h4>
							<input type="radio" name="examinerid" value="<%=examiner.getId()%>" <%=(examiner.getId()==client.getExaminer().getId()?"":"")%>/>
							<%=examiner%> &nbsp;&nbsp;&nbsp;&nbsp;(% Match: <%=se.getPercentMatch()%>)
						</h4>

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Address:</b> <%=add%><br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Contact:</b> Email: <%=examiner.getEmail()%>, Phone: <%=examiner.getPhoneNo()%><br/>

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Exams Offered:</b>

							<%for(int a=0; a<examiner.getExams().size();a++){
								Exam exam = (Exam)examiner.getExams().get(a);
								if(a!=0){%>
									,&nbsp;
								<%}%>
									<%=exam.getName()%> (<%=exam.getPrice()%>)
							<%}%>

					<%}%>
				</td>
			</tr>
		</table>

		<h4>
			Assign An Examiner:

			<%for (int i=0; i<examinerCount; i=i+limit){
				if(i == offsetExaminer){
					if(i+limit > examinerCount){%>
						&nbsp;<a><%=i+1%> - <%=examinerCount%></a>
					<%}else{%>
						&nbsp;<a><%=i+1%> - <%=i+limit%></a>
					<%}%>
				<%}else{
					if(i+limit > examinerCount){%>
						&nbsp;<a href="assignexaminer?offsetExaminer=<%=i%>&amp;sortBy=<%=sortBy%>#examiner"><%=i+1%> - <%=examinerCount%></a>
					<%}else{%>
						&nbsp;<a href="assignexaminer?offsetExaminer=<%=i%>&amp;sortBy=<%=sortBy%>#examiner"><%=i+1%> - <%=i+limit%></a>&nbsp;
					<%}%>
				<%}%>
			<%}%>
		</h4>

		<input type="image" class="right" src="img/continue.gif" />
	</form>
</div>
