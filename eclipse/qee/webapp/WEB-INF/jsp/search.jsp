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


<%Connection con = null;
	try {

		con =  DBConnector.getInstance().getConnection();

		User user = (User)session.getAttribute("user");

		String searchLimit_s = request.getParameter("SearchLimit");
		if(searchLimit_s == null || searchLimit_s.length() == 0)
			searchLimit_s="15";

		int searchLimit = Integer.parseInt(searchLimit_s);

		String offset_s = request.getParameter("Offset");
		if(offset_s == null || offset_s.length() == 0)
			offset_s="0";

		int offset = Integer.parseInt(offset_s);

		String orderBy = request.getParameter("OrderBy");
		if(orderBy == null || orderBy.length() == 0)
			orderBy="tbl_client.last_name, tbl_client.first_name";

		String search_data = request.getParameter("SearchData");
		if(search_data == null || search_data.length() == 0)
			search_data="";

		search_data = search_data.replaceAll("'","");

		StringTokenizer search_token = new StringTokenizer(search_data);
		Vector tokens = new Vector();
		%>



		<%if(search_data==null||search_data.length()<1){%>

			<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;Search</h2>

			<div>
				<p class="error">Error: Your search must contain at least 1 characters.</p>
			</div>


		<%}else{

			int index=0;

			String like_clause = "";
			while(search_token.hasMoreTokens()){
				if(like_clause.length()>0){
					like_clause += " AND ";
				}else{
					like_clause = "AND (";
				}
				String token = search_token.nextToken();
				tokens.add(token);
				like_clause += "(tbl_client.first_name like '%" + token + "%' OR tbl_client.last_name like '%" + token + "%' OR tbl_client.clientId like '%" + token + "%' OR AES_DECRYPT(tbl_client.ssn,'"+user.KEY+"') like '%" + token + "%' OR tbl_client.note like '%" + token + "%' OR tbl_client.lab_code like '%" + token + "%' OR tbl_client.insuranceId like '%" + token + "%' OR tbl_client.instructions like '%" + token + "%')" ;
			}
			if(like_clause.length()>0)
				like_clause += ")";

			int totalCount = 0;
			ResultSet rsNCount = con.createStatement().executeQuery("SELECT COUNT(tbl_client.clientId) FROM tbl_client WHERE tbl_client.hide=0 "+like_clause);
			if(rsNCount.next()){
				totalCount += rsNCount.getInt(1);
			}%>

			<h2 id="nav">&nbsp;Navigation .: <a href="home" name="ref">Home</a>&nbsp;>&nbsp;Search for <a style="color: red;">'<%=search_data%>'</a> (<%=totalCount%> <%=(totalCount==1?" Matching Item":" Matching Items")%>)</h2>

			<%ResultSet rsN = con.createStatement().executeQuery("SELECT tbl_client.clientId FROM tbl_client WHERE tbl_client.hide=0 "+like_clause+" ORDER BY "+orderBy+" LIMIT "+searchLimit+" OFFSET "+offset);
			while(rsN.next()){
				index++;
				if(index==1){%>

					<h4><a>Clients:</a>&nbsp;
						<%for (int i=0; i<totalCount; i=i+searchLimit){
							if(i == offset){
								if(i+searchLimit > totalCount){%>
									&nbsp;<a><%=i+1%> - <%=totalCount%></a>
								<%}else{%>
									&nbsp;<a><%=i+1%> - <%=i+searchLimit%></a>
								<%}%>
							<%}else{
								if(i+searchLimit > totalCount){%>
									&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>&amp;OrderBy=<%=orderBy%>#ref"><%=i+1%> - <%=totalCount%></a>
								<%}else{%>
									&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>&amp;OrderBy=<%=orderBy%>#ref"><%=i+1%> - <%=i+searchLimit%></a>&nbsp;
								<%}%>
							<%}%>
						<%}%>
					</h4>

					<div class="bgGrey">
						<div style="width:100%;">

						<h4>
							&nbsp;<a>Sort By:</a>

							<%if(orderBy.equals("tbl_client.clientId")){%>
									&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.clientId DESC#ref">Order #</a>&nbsp;<img src="img/sort.gif" alt=""/>
								<%}else if(orderBy.equals("tbl_client.clientId DESC")){%>
									&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.clientId#ref">Order #</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
								<%}else{%>
									&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.clientId#ref">Order #</a>
							<%}%>

							or

							<%if(orderBy.equals("tbl_client.last_update")){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.last_update DESC#ref">Last Update</a>&nbsp;<img src="img/sort.gif" alt=""/>
							<%}else if(orderBy.equals("tbl_client.last_update DESC")){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.last_update#ref">Last Update</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
							<%}else{%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.last_update#ref">Last Update</a>
							<%}%>

							or

							<%if(orderBy.equals("tbl_client.last_name, tbl_client.first_name")){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.last_name, tbl_client.first_name DESC#ref">Name</a>&nbsp;<img src="img/sort.gif" alt=""/>
							<%}else if(orderBy.equals("tbl_client.last_name DESC")){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
							<%}else{%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.last_name, tbl_client.first_name#ref">Name</a>
							<%}%>

							or

							<%if(orderBy.equals("tbl_client.start_date")){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.start_date DESC#ref">Exam Date</a>&nbsp;<img src="img/sort.gif" alt=""/>
							<%}else if(orderBy.equals("tbl_client.start_date DESC")){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.start_date#ref">Exam Date</a>&nbsp;<img src="img/sort_desc.gif" alt=""/>
							<%}else{%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;OrderBy=tbl_client.start_date#ref">Exam Date</a>
							<%}%>


						</h4>



					<%}//first entry%>

						<%
						Client client = Client.getClient(rsN.getInt(1));
						Address clientAdd = client.getAddress();
						Address clientAdd2 = client.getAddress2();
						%>


						<div style="width:100%">
							&nbsp;
						</div>

						<h3 style="float:left;">
							<div style="float:left">
								<a href="client?clientId=<%=client.getId()%>">Order #<%=HighlightTerm.highlightTerms(""+client.getId(),tokens)%></a>&nbsp;<a href="requisition?clientId=<%=client.getId()%>" target="blank">[Print]</a>
								:&nbsp;<%=HighlightTerm.highlightTerms(""+client,tokens)%>
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
										Phone: <%=HighlightTerm.highlightTerms(""+client.getPhoneNo(),tokens)%>, Alt Phone 1: <%=HighlightTerm.highlightTerms(""+client.getAltPhone1(),tokens)%><br/>
									</td>
								</tr>
								<tr>
									<th>&nbsp;&nbsp;&nbsp;Comments:</th>
									<td>
										<%=HighlightTerm.highlightTerms(""+client.getNote(),tokens)%>
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

			<%if(index==0){%>

				<div style="text-align:left;"><p class="error">There are no matching clients at this time.</p></div>

				<% // Need a fix, Shows navigation home %>

			<%}else{%>

				</div></div>

				<h4><a>Clients:</a>&nbsp;
					<%for (int i=0; i<totalCount; i=i+searchLimit){
						if(i == offset){
							if(i+searchLimit > totalCount){%>
								&nbsp;<a><%=i+1%> - <%=totalCount%></a>
							<%}else{%>
								&nbsp;<a><%=i+1%> - <%=i+searchLimit%></a>
							<%}%>
						<%}else{
							if(i+searchLimit > totalCount){%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>&amp;OrderBy=<%=orderBy%>#nav"><%=i+1%> - <%=totalCount%></a>
							<%}else{%>
								&nbsp;<a href="search?SearchData=<%=search_data%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>&amp;OrderBy=<%=orderBy%>#nav"><%=i+1%> - <%=i+searchLimit%></a>&nbsp;
							<%}%>
						<%}%>
					<%}%>
				</h4>

			<%}%>

		<%}%>

<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>