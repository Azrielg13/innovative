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

<%Connection con = null;

try {

	con =  DBConnector.getInstance().getConnection();

	User user = (User)session.getAttribute("user");
	if(user == null)
		user = new User();

	Client client = (Client)session.getAttribute("newclient");
	if(client == null)
		client = new Client();
	Address clientAdd = client.getAddress();
	Address clientAdd2 = client.getAddress2();

	Vector agents = (Vector)request.getAttribute("agents");
	int agentSize = 0;
	if(agents!=null)
		agentSize = agents.size();

	Vector ins = (Vector)request.getAttribute("ins");
	int inSize = 0;
	if(ins!=null)
		inSize = ins.size();

	Vector insType = (Vector)request.getAttribute("insTypes");
	int insTypeSize = 0;
	if(insType!=null)
		insTypeSize = insType.size();

	Vector states = (Vector)request.getAttribute("states");
	int stateSize = 0;
	if(states!=null)
		stateSize = states.size();

	Vector langs = (Vector)request.getAttribute("langs");
	int langSize = 0;
	if(langs!=null)
		langSize = langs.size();

	Vector agentCodes = (Vector)request.getAttribute("agentCodes");
	int agentCodeSize = 0;
	if(agentCodes!=null)
		agentCodeSize = agentCodes.size();
	%>


	<script language="JavaScript">
		<!--
		var req;
		function getAgentInfo() {
		   document.getElementById('agentCode').value = 'UPDATING...';
		   var insField = document.getElementById("insid");
		   var agentField = document.getElementById("agentid");
		   var url = "getAgentInfo?insid="+escape(insField.value)+"&agentid="+escape(agentField.value);
		   if (window.XMLHttpRequest) {
		   	req = new XMLHttpRequest();
		   } else if (window.ActiveXObject) {
		   	req = new ActiveXObject("Microsoft.XMLHTTP");
		   }
		   req.open("GET", url, true);
		   req.onreadystatechange = callback;
		   req.send(null);

		}
		function callback() {
		    if (req.readyState == 4) {
				if (req.status == 200) {
					document.getElementById('agentCode').value = '200';
					 var message = req.responseXML.getElementsByTagName("agent")[0];
					 document.getElementById("agentCode").value = message.attributes[0].value;
					 document.getElementById("agency").value = message.attributes[1].value;
					 document.getElementById("agencyCode").value = message.attributes[2].value;
				}
		    }
		    //document.getElementById('agentCode').value = '4';
		}
		//-->
	</script>

	<div>
		<form name="Register" action="addclient" method="post">

			<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;New Prop. Ins. Entry Form</h2>

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

			<br/>
			<c:forEach var="error" items="${errors}">
				<p class="error"><c:out value="${error}"/></p>
			</c:forEach>

			<h4>Insurance Company Information</h4>
			<table class="bgGrey">
				<tr>
					<th>Select Insurance Company:</th>
					<td width="550px">

						<select name="insid" onchange="javascript:getAgentInfo();">
							<%
							for(int a=0; a<inSize;a++){
								InsuranceCo in = (InsuranceCo)ins.get(a);
								Address insAdd = in.getAddress();%>
								<option value="<%=in.getId()%>" <%=(in.getId()==client.getIns().getId()?"SELECTED":"")%>><%=in%></option>
							<%}%>
						</select>
						<input type="button" value="Add an Insurance Company" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_insurance_co&amp;ASAction=Create&amp;reURL=addclient';Register.submit();"/>

						<%for(int a=0; a<inSize;a++){
							InsuranceCo in = (InsuranceCo)ins.get(a);
							Address insAdd = in.getAddress();%>
							<input type="hidden" name="<%=a+1%>" value="<%=in+" "+insAdd%>"/>
						<%}%>

					</td>
				</tr>
				<tr>
					<th>Insurance Type:</th>
					<td width="550px">
						<select name="insTypeId">
							<option value="0">-- Select --</option>
							<%
							for(int a=0; a<insTypeSize;a++){
								InsuranceType inT = (InsuranceType)insType.get(a);%>
								<option value="<%=inT.getId()%>" <%=(inT.getId()==client.getInsType().getId()?"SELECTED":"")%>><%=inT%></option>
							<%}%>
						</select>
					</td>
				</tr>
				<tr>
					<th>Insurance Amount ($): (*)</th>
					<td width="550px"><input name="insAmount" type="textbox" size="20" maxlength="20" value="<%=client.getInsAmount()%>" onchange="formatCurrency(this)"/></td>
				</tr>
			</table>

			<h4>Agent Information</h4>
			<table class="bgGrey">
				<tr>
					<th>Select Agent: (*)</th>
					<td width="550px">
						<%if(user.getType()<User.AGENT){%>
							<select name="agentid" onchange="javascript:getAgentInfo();">
								<option value="0">---Select---</option>
								<% // Radial Buttons
								for(int a=0; a<agentSize;a++){
									User agent = (User)agents.get(a);%>
									<option value="<%=agent.getId()%>" <%=(agent.getId()==client.getAgent().getId()?"SELECTED":"")%>><%=agent%></option>
								<%}%>
							</select>

							<input name="redirect" type="hidden" value=""/>

							<input type="button" value="Add an Agent" onclick="getElementById('redirect').value='webadmin?Edit_Table=tbl_user&amp;ASAction=Create&amp;reURL=addclient';Register.submit();"/>
						<%}else if(user.getType()==User.AGENT){%>
							<input name="agentName" type="textbox" size="20" value="<%=user%>" disabled />
						<%}%>

					</td>
				</tr>
				<tr>
					<th>Select Agent Code:</th>
					<td width="550px">
						<!--
						Previous:
						<select name="agentCodePrev" onChange="getElementById('agentCode').value=this.options(this.selectedIndex).value;">
							<option value="0"> SELECT ---- Agent (Agent Code) - Insurance ----</option>
							<%
							ResultSet rs = con.createStatement().executeQuery("SELECT DISTINCT tbl_client.aUserId, tbl_client.agentCode, tbl_client.insuranceId FROM tbl_client WHERE tbl_client.agentCode is not null AND tbl_client.agentCode!=''");
							while(rs.next()){
								if(rs.getString("insuranceId")!=null && InsuranceCo.getInsuranceCo(rs.getInt("insuranceId"))!=null){
									User agent = new User();
									if(rs.getString("aUserId")!=null)
										agent = User.getUser(rs.getInt("aUserId"));
									InsuranceCo inX = InsuranceCo.getInsuranceCo(rs.getInt("insuranceId"));%>
									<option value="<%=rs.getString("agentCode")%>"><%=agent%> (<%=rs.getString("agentCode")%>) - <%=inX%></option>
								<%}%>
							<%}%>
						</select>
						<br/>
						or New:
						//-->
						<input name="agentCode" type="textbox" size="10" maxlength="10" value="<%=client.getAgentCode()%>" />
					</td>
				</tr>
				<tr>
					<th>Agency:</th>
					<td width="550px">
						<input name="agency" type="textbox" size="50" maxlength="50" value="<%=client.getAgency()%>" />
					</td>
				</tr>
				<tr>
					<th>Agency Code:</th>
					<td width="550px">
						<input name="agencyCode" type="textbox" size="20" maxlength="20" value="<%=client.getAgencyCode()%>" />
					</td>
				</tr>
			</table>

			<h4>Proposed Insured Information</h4>
			<table class="bgGrey">
				<tr><th>First Name: (*)</th><td width="550px"><input name="fname" type="textbox" size="50" maxlength="50" value="<%=client.getFirstName()%>" /></td></tr>
				<tr><th>Last Name: (*)</th><td><input name="lname" type="textbox" size="50" maxlength="50" value="<%=client.getLastName()%>" /></td></tr>
				<tr><th>Sex:</th><td><select name="sex" size="1"/><option></option><option value="M" <%="M".equalsIgnoreCase(client.getSex())?"Selected":""%>>M</option><option value="F" <%="F".equalsIgnoreCase(client.getSex())?"SELECTED":""%>>F</option></select></td></tr>
				<tr><th>Date of Birth:</th><td><input name="dob" type="textbox" size="15" maxlength="15" value="<%=client.getDOB()%>" />(Ex. 01/01/1970)</td></tr>
				<tr><th>Social Security Number:</th><td><input name="ssn" type="textbox" size="15" maxlength="11" value="<%=client.getSSN()%>" onKeyPress="formatSSN(this)"/></td></tr>

				<tr><th style="border-top:1px #fff solid">Contact Number: (*)</th><td style="border-top:1px #fff solid"><input name="phone" type="textbox" size="20" maxlength="20" value="<%=client.getPhoneNo()%>" onKeyPress="formatPhone(this)"/></td></tr>
				<tr><th>Alt Phone Number 1:</th><td><input name="alt_phone_no1" type="textbox" size="20" maxlength="20" value="<%=client.getAltPhone1()%>" onKeyPress="formatPhone(this)"/></td></tr>
				<tr><th>Alt Phone Number 2:</th><td><input name="alt_phone_no2" type="textbox" size="20" maxlength="20" value="<%=client.getAltPhone2()%>" onKeyPress="formatPhone(this)"/></td></tr>

				<tr><th style="border-top:1px #fff solid">Address of Examination:</th><td style="border-top:1px #fff solid"><input name="street1" type="textbox" size="50" maxlength="100" value="<%=clientAdd.getStreet1()%>" /></td></tr>
				<tr><td colspan="2">Street address, company name, c/o,Apartment, suite, unit, building, floor, etc. </td></tr>
				<tr><th>City: (*)</th><td><input name="city1" type="textbox" size="20" maxlength="20" value="<%=clientAdd.getCity()%>" /></td></tr>
				<tr><th>State: (*)</th><td><dd4:select name="state1" optionValues="<%=states%>" selected="<%=clientAdd.getState()%>"/></td></tr>
				<tr><th>Zip Code:</th><td><input name="zip1" type="textbox" size="5" maxlength="5" value="<%=clientAdd.getZip()%>" /></td></tr>


				<tr><th style="border-top:1px #fff solid">Alt. Address:</th><td style="border-top:1px #fff solid"><input name="street2" type="textbox" size="50" maxlength="100" value="<%=clientAdd2.getStreet1()%>" /></td></tr>
				<tr><td colspan="2">Street address, company name, c/o,Apartment, suite, unit, building, floor, etc. </td></tr>
				<tr><th>City:</th><td><input name="city2" type="textbox" size="20" maxlength="20" value="<%=clientAdd2.getCity()%>" /></td></tr>
				<tr><th>State:</th><td><dd4:select name="state2" optionValues="<%=states%>" selected="<%=clientAdd2.getState()%>"/></td></tr>
				<tr><th>Zip Code:</th><td><input name="zip2" type="textbox" size="5" maxlength="5" value="<%=clientAdd2.getZip()%>" /></td></tr>
			</table>

			<h4>Special Considerations</h4>
			<table class="bgGrey">
				<tr><th>Primary Language:</th><td><dd4:select name="primarylang" optionValues="<%=langs%>" selected="<%=client.getPrimaryLang()%>"/></td></tr>
				<tr>
					<th>Comments:</th>
					<td width="550px">
						<textarea cols="45" rows="4" name="note"><%=client.getNote()%></textarea>
					</td>
				</tr>
			</table>

			<div>(*) Indicates required fields</div>

			<input type="image" class="right" src="img/continue.gif" />
		</form>
		<script language="JavaScript">
			<!--
			// create calendar object(s) just after form tag closed
			// specify form element as the only parameter (document.forms['formname'].elements['inputname']);
			// note: you can have as many calendar objects as you need for your application
			var cal_dob = new calendar2(document.forms['Register'].elements['dob']);
			cal_dob.year_scroll = true;
			cal_dob.time_comp = false;
			//-->
		</script>
		<script language="JavaScript">
			<!--
			// Calc Age
			function calcage() {
				age = document.getElementById("age");
				dob = document.getElementById("dob");
				age.value=dob.value;
			}
			//-->
		</script>
	</div>

<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>
