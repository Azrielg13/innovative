<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>


<%

// Offsets need to be in the session

User user = (User)session.getAttribute("user");
if(user == null)
	user = new User();

int openClientCount = ((Integer)request.getAttribute("openClientCount")).intValue();
int offsetOpen = ((Integer)request.getAttribute("offsetOpen")).intValue();
Vector openClients = (Vector)request.getAttribute("openClients");
int openClientSize = 0;
if(openClients!=null)
	openClientSize = openClients.size();


int clientCount = ((Integer)request.getAttribute("clientCount")).intValue();
int offsetClient = ((Integer)request.getAttribute("offsetClient")).intValue();
Vector clients = (Vector)request.getAttribute("clients");
int clientSize = 0;
if(clients!=null)
	clientSize = clients.size();

int compClientCount = ((Integer)request.getAttribute("compClientCount")).intValue();
int offsetComp = ((Integer)request.getAttribute("offsetComp")).intValue();
Vector compClients = (Vector)request.getAttribute("compClients");
int compClientSize = 0;
if(compClients!=null)
	compClientSize = compClients.size();

int limit = ((Integer)request.getAttribute("limit")).intValue();

String sortBy = (String)request.getAttribute("sortBy");
if(sortBy == null || sortBy.length() == 0)
	sortBy="";

//Vector[] display = new Vector[4];
//display[0] = openClients;
//display[1] = canClients;
//display[2] = compClients;
//display[3] = clients;

//for(int i=0;i<display.size();i++){

//}
%>

<h2 id="nav">&nbsp;Navigation .: Home</h2>

<h4>
	&nbsp;<a>Sort By:</a>

	<%if(sortBy.equals("tbl_client.clientId")){%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId DESC#ref">Order #</a>&nbsp;<img src="img/sort.gif" alt=""/>
		<%}else if(sortBy.equals("tbl_client.clientId DESC")){%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId#ref">Order #</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
		<%}else{%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId#ref">Order #</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.last_update")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update DESC#ref">Last Update</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.last_update DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update#ref">Last Update</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update#ref">Last Update</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.last_name, tbl_client.first_name")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name DESC, tbl_client.first_name#ref">Name</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.last_name DESC, tbl_client.first_name")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.start_date")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date DESC#ref">Exam Date</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.start_date DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date#ref">Exam Date</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date#ref">Exam Date</a>
	<%}%>


</h4>

<br/>

<%if(user.getType()<=User.STAFF){%>

	<h4>
		&nbsp;<a href="#" onclick="showHideTable('oTb1','expand1');return false;"><img id="expand1" src="img/collapse.gif"></a>&nbsp;Assign Examiner&nbsp;(<%=openClientCount%> Open):

		<%for (int i=0; i<openClientCount; i=i+limit){
			if(i == offsetOpen){
				if(i+limit > openClientCount){%>
					&nbsp;<a><%=i+1%> - <%=openClientCount%></a>
				<%}else{%>
					&nbsp;<a><%=i+1%> - <%=i+limit%></a>
				<%}%>
			<%}else{
				if(i+limit > openClientCount){%>
					&nbsp;<a href="home?offsetOpen=<%=i%>&amp;sortBy=<%=sortBy%>#expand1"><%=i+1%> - <%=openClientCount%></a>
				<%}else{%>
					&nbsp;<a href="home?offsetOpen=<%=i%>&amp;sortBy=<%=sortBy%>#expand1"><%=i+1%> - <%=i+limit%></a>&nbsp;
				<%}%>
			<%}%>
		<%}%>
	</h4>

	<div class="bgGrey">
		<div id="oTb1" style="width:100%;">

			<%for(int i=0; i<openClientSize;i++){
				Client client = (Client)openClients.get(i);
				Address clientAdd = client.getAddress();%>


				<div style="width:100%">
					&nbsp;
				</div>

				<h3 style="float:left;">
					<div style="float:left">
						<a href="client?clientId=<%=client.getId()%>">Order #<%=client.getId()%></a>&nbsp;<a href="requisition?clientId=<%=client.getId()%>" target="blank">[Print]</a>
						:&nbsp;<%=client%>
					</div>
					<div style="float:right">
						Last Update:&nbsp;<%=client.getLastUpdate()%>
					</div>
				</h3>

				<div style="width:50%;float:left;">
					<table>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Insurance Co:</th>
							<td>
								<%=client.getIns()%> for <%=client.getInsAmount()%>
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Client:</th>
							<td>
								<%=clientAdd%>
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Contact:</th>
							<td>
								Primary Language: <%=client.getPrimaryLang()%></br>
								Phone: <%=client.getPhoneNo()%>, Alt Phone 1: <%=client.getAltPhone1()%><br/>
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;&nbsp;Comments:</th>
							<td>
								<%=client.getNote()%>
							</td>
						</tr>
					</table>
				</div>
				<div style="width:50%;float:left;">
					<table>
						<tr>
							<th>Agent:</th>
							<td>
								<%=client.getAgent()%> On: <%=client.getRecDate()%>
							</td>
						</tr>
						<tr>
							<th>Exam Date:</th>
							<td>
								<%=client.getStartDate()%>
							</td>
						</tr>
						<tr>
							<th>Exams:</th>
							<td>
								<%for(int a=0; a<client.getExams().size();a++){
								Exam exam = (Exam)client.getExams().get(a);%>
									<%=exam.getName()%>,
								<%}%>
							</td>
						</tr>
						<tr>
							<th>Examiner:</th>
							<td>
								<a href="assignexaminer?clientId=<%=client.getId()%>">[Assign Examiner]</a>
							</td>
						</tr>
					</table>
				</div>
				<br/><br/><br/>

			<%}%>

			</div>
		</div>

			<%if(openClientSize==0){%>

				<div class="error">There are no open clients at this time</div>

			<%}else{%>
				<h4>
					&nbsp;Assign Examiner&nbsp;(<%=openClientCount%> Open):

					<%for (int i=0; i<openClientCount; i=i+limit){
						if(i == offsetOpen){
							if(i+limit > openClientCount){%>
								&nbsp;<a><%=i+1%> - <%=openClientCount%></a>
							<%}else{%>
								&nbsp;<a><%=i+1%> - <%=i+limit%></a>
							<%}%>
						<%}else{
							if(i+limit > openClientCount){%>
								&nbsp;<a href="home?offsetOpen=<%=i%>&amp;sortBy=<%=sortBy%>#expand1"><%=i+1%> - <%=openClientCount%></a>
							<%}else{%>
								&nbsp;<a href="home?offsetOpen=<%=i%>&amp;sortBy=<%=sortBy%>#expand1"><%=i+1%> - <%=i+limit%></a>&nbsp;
							<%}%>
						<%}%>
					<%}%>
				</h4>
			<%}%>

<%}%>

<br/>
<br/>

<h4>
	&nbsp;<a href="#" onclick="showHideTable('oTb2','expand2');return false;"><img id="expand2" src="img/collapse.gif"></a>&nbsp;Active Client Summary&nbsp;(<%=clientCount%> Active):

	<%for (int i=0; i<clientCount; i=i+limit){
		if(i == offsetClient){
			if(i+limit > clientCount){%>
				&nbsp;<a><%=i+1%> - <%=clientCount%></a>
			<%}else{%>
				&nbsp;<a><%=i+1%> - <%=i+limit%></a>
			<%}%>
		<%}else{
			if(i+limit > clientCount){%>
				&nbsp;<a href="home?offsetClient=<%=i%>&amp;sortBy=<%=sortBy%>#expand2"><%=i+1%> - <%=clientCount%></a>
			<%}else{%>
				&nbsp;<a href="home?offsetClient=<%=i%>&amp;sortBy=<%=sortBy%>#expand2"><%=i+1%> - <%=i+limit%></a>&nbsp;
			<%}%>
		<%}%>
	<%}%>
</h4>

<div class="bgGrey">
	<div id="oTb2" style="width:100%;">
		<%for(int i=0; i<clientSize;i++){
			Client client = (Client)clients.get(i);
			Address clientAdd = client.getAddress();%>

			<div style="width:100%">
				&nbsp;
			</div>

			<h3 style="float:left;">
				<div style="float:left">
					<a href="client?clientId=<%=client.getId()%>">Order #<%=client.getId()%></a>&nbsp;<a href="requisition?clientId=<%=client.getId()%>" target="blank">[Print]</a>
					:&nbsp;<%=client%>
				</div>
				<div style="float:right">
					Last Update:&nbsp;<%=client.getLastUpdate()%>
				</div>
			</h3>

			<div style="width:50%;float:left;">
				<table>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Insurance Co:</th>
						<td>
							<%=client.getIns()%> for <%=client.getInsAmount()%>
						</td>
					</tr>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Client:</th>
						<td>
							<%=clientAdd%>
						</td>
					</tr>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Contact:</th>
						<td>
							Primary Language: <%=client.getPrimaryLang()%></br>
							Phone: <%=client.getPhoneNo()%>, Alt Phone 1: <%=client.getAltPhone1()%><br/>
						</td>
					</tr>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Comments:</th>
						<td>
							<%=client.getNote()%>
						</td>
					</tr>
				</table>
			</div>
			<div style="width:50%;float:left;">
				<table>
					<tr>
						<th>Agent:</th>
						<td>
							<%=client.getAgent()%> On: <%=client.getRecDate()%>
						</td>
					</tr>
					<tr>
						<th>Exam Date:</th>
						<td>
							<%=client.getStartDate()%>
						</td>
					</tr>
					<tr>
						<th>Exams:</th>
						<td>
							<%for(int a=0; a<client.getExams().size();a++){
							Exam exam = (Exam)client.getExams().get(a);%>
								<%=exam.getName()%>,
							<%}%>
						</td>
					</tr>
					<tr>
						<th>Examiner:</th>
						<td>
							<%=client.getExaminer()%>
						</td>
					</tr>
				</table>
			</div>
			<br/><br/><br/>

		<%}%>

		</div>
	</div>
		<%if(clientSize==0){%>
			<div class="error">There are no active clients at this time</div>
		<%}else{%>
			<h4>
				&nbsp;Active Client Summary&nbsp;(<%=clientCount%> Active):

				<%for (int i=0; i<clientCount; i=i+limit){
					if(i == offsetClient){
						if(i+limit > clientCount){%>
							&nbsp;<a><%=i+1%> - <%=clientCount%></a>
						<%}else{%>
							&nbsp;<a><%=i+1%> - <%=i+limit%></a>
						<%}%>
					<%}else{
						if(i+limit > clientCount){%>
							&nbsp;<a href="home?offsetClient=<%=i%>&amp;sortBy=<%=sortBy%>#expand2"><%=i+1%> - <%=clientCount%></a>
						<%}else{%>
							&nbsp;<a href="home?offsetClient=<%=i%>&amp;sortBy=<%=sortBy%>#expand2"><%=i+1%> - <%=i+limit%></a>&nbsp;
						<%}%>
					<%}%>
				<%}%>
			</h4>
		<%}%>
<br/>
<br/>
<h4>
	&nbsp;<a>Sort By:</a>

	<%if(sortBy.equals("tbl_client.clientId")){%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId DESC#ref">Order #</a>&nbsp;<img src="img/sort.gif" alt=""/>
		<%}else if(sortBy.equals("tbl_client.clientId DESC")){%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId#ref">Order #</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
		<%}else{%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId#ref">Order #</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.last_update")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update DESC#ref">Last Update</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.last_update DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update#ref">Last Update</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update#ref">Last Update</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.last_name, tbl_client.first_name")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name DESC#ref">Name</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.last_name DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.start_date")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date DESC#ref">Exam Date</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.start_date DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date#ref">Exam Date</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date#ref">Exam Date</a>
	<%}%>


</h4>

<br/>
<br/>

<h4>
	&nbsp;<a href="#" onclick="showHideTable('oTb3','expand3');return false;"><img id="expand3" src="img/expand.gif"></a>&nbsp;Completed Client Summary&nbsp;(<%=compClientCount%> Complete):

	<%for (int i=0; i<compClientCount; i=i+limit){
		if(i == offsetComp){
			if(i+limit > compClientCount){%>
				&nbsp;<a><%=i+1%> - <%=compClientCount%></a>
			<%}else{%>
				&nbsp;<a><%=i+1%> - <%=i+limit%></a>
			<%}%>
		<%}else{
			if(i+limit > compClientCount){%>
				&nbsp;<a href="home?offsetComp=<%=i%>&amp;sortBy=<%=sortBy%>#expand3"><%=i+1%> - <%=compClientCount%></a>
			<%}else{%>
				&nbsp;<a href="home?offsetComp=<%=i%>&amp;sortBy=<%=sortBy%>#expand3"><%=i+1%> - <%=i+limit%></a>&nbsp;
			<%}%>
		<%}%>
	<%}%>
</h4>

<div class="bgGrey">
	<div id="oTb3" style="width:100%;" style="display:none;">
		<%for(int i=0; i<compClientSize;i++){
			Client client = (Client)compClients.get(i);
			Address clientAdd = client.getAddress();%>

			<div style="width:100%">
				&nbsp;
			</div>

			<h3 style="float:left;">
				<div style="float:left">
					<a href="client?clientId=<%=client.getId()%>">Order #<%=client.getId()%></a>&nbsp;<a href="requisition?clientId=<%=client.getId()%>" target="blank">[Print]</a>
					:&nbsp;<%=client%>
				</div>
				<div style="float:right">
					Last Update:&nbsp;<%=client.getLastUpdate()%>
				</div>
			</h3>

			<div style="width:50%;float:left;">
				<table>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Insurance Co:</th>
						<td>
							<%=client.getIns()%> for <%=client.getInsAmount()%>
						</td>
					</tr>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Client:</th>
						<td>
							<%=clientAdd%>
						</td>
					</tr>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Contact:</th>
						<td>
							Primary Language: <%=client.getPrimaryLang()%></br>
							Phone: <%=client.getPhoneNo()%>, Alt Phone 1: <%=client.getAltPhone1()%><br/>
						</td>
					</tr>
					<tr>
						<th>&nbsp;&nbsp;&nbsp;Comments:</th>
						<td>
							<%=client.getNote()%>
						</td>
					</tr>
				</table>
			</div>
			<div style="width:50%;float:left;">
				<table>
					<tr>
						<th>Agent:</th>
						<td>
							<%=client.getAgent()%> On: <%=client.getRecDate()%>
						</td>
					</tr>
					<tr>
						<th>Exam Date / Complete Date:</th>
						<td>
							<%=client.getStartDate()%> / <%=client.getEndDate()%>
						</td>
					</tr>
					<tr>
						<th>Exams:</th>
						<td>
							<%for(int a=0; a<client.getExams().size();a++){
							Exam exam = (Exam)client.getExams().get(a);%>
								<%=exam.getName()%>,
							<%}%>
						</td>
					</tr>
					<tr>
						<th>Examiner:</th>
						<td>
							<%=client.getExaminer()%>
						</td>
					</tr>
				</table>
			</div>
			<br/><br/><br/>

		<%}%>

		</div>
	</div>
		<%if(clientSize==0){%>
			<div class="error">There are no complete clients at this time</div>
		<%}else{%>
			<h4>
				&nbsp;Completed Client Summary&nbsp;(<%=compClientCount%> Completed):

				<%for (int i=0; i<compClientCount; i=i+limit){
					if(i == offsetComp){
						if(i+limit > compClientCount){%>
							&nbsp;<a><%=i+1%> - <%=compClientCount%></a>
						<%}else{%>
							&nbsp;<a><%=i+1%> - <%=i+limit%></a>
						<%}%>
					<%}else{
						if(i+limit > compClientCount){%>
							&nbsp;<a href="home?offsetComp=<%=i%>&amp;sortBy=<%=sortBy%>#expand3"><%=i+1%> - <%=compClientCount%></a>
						<%}else{%>
							&nbsp;<a href="home?offsetComp=<%=i%>&amp;sortBy=<%=sortBy%>#expand3"><%=i+1%> - <%=i+limit%></a>&nbsp;
						<%}%>
					<%}%>
				<%}%>
			</h4>
		<%}%>

<br/>
<br/>
<h4>
	&nbsp;<a>Sort By:</a>

	<%if(sortBy.equals("tbl_client.clientId")){%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId DESC#ref">Order #</a>&nbsp;<img src="img/sort.gif" alt=""/>
		<%}else if(sortBy.equals("tbl_client.clientId DESC")){%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId#ref">Order #</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
		<%}else{%>
			&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.clientId#ref">Order #</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.last_update")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update DESC#ref">Last Update</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.last_update DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update#ref">Last Update</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_update#ref">Last Update</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.last_name, tbl_client.first_name")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name DESC#ref">Name</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.last_name DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>
	<%}%>

	or

	<%if(sortBy.equals("tbl_client.start_date")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date DESC#ref">Exam Date</a>&nbsp;<img src="img/sort.gif" alt=""/>
	<%}else if(sortBy.equals("tbl_client.start_date DESC")){%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date#ref">Exam Date</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
	<%}else{%>
		&nbsp;<a href="home?offsetOpen=<%=offsetOpen%>&amp;sortBy=tbl_client.start_date#ref">Exam Date</a>
	<%}%>


</h4>

<br/>
<br/>
