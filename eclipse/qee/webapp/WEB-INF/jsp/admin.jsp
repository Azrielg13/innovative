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
Connection con = null;
try {

	Company company = (Company)request.getAttribute("company");
	con =  DBConnector.getInstance().getConnection();

	HttpSession usession = request.getSession(true);

	int cartCount = 0;
	Vector cart = null;
	if(usession.getAttribute("Cart") != null){
		cart = (Vector)usession.getAttribute("Cart");
		cartCount = cart.size();
	}

	String action = request.getParameter("Action");
	if(action == null || action.length() == 0)
		action="";

	String asaction = request.getParameter("ASAction");
	if(asaction == null || asaction.length() == 0)
		asaction="";

	String reURL = request.getParameter("reURL");
	if(reURL == null || reURL.length() == 0)
		reURL="";

	String edit_table = request.getParameter("Edit_Table");
	if(edit_table == null || edit_table.length() == 0)
		edit_table="tbl_insurance_co";

	usession.setAttribute("Changed", "0");
	if(usession.getAttribute("Edit_Table_Prev") == null){
		usession.setAttribute("Edit_Table_Prev", edit_table);
	}else{
		if(!edit_table.equals(usession.getAttribute("Edit_Table_Prev").toString())){
			usession.setAttribute("Edit_Table_Prev", edit_table);
			usession.setAttribute("Changed", "1");
			usession.removeAttribute("SearchColumn");
			usession.removeAttribute("Filter");
			usession.removeAttribute("SearchText");
			usession.removeAttribute("SearchLimit");
			usession.removeAttribute("Offset");
		}
	}

	String searchColumn = request.getParameter("SearchColumn");
	if(searchColumn == null || searchColumn.length()==0){
		if(usession.getAttribute("SearchColumn") == null){
			searchColumn = "";
			usession.setAttribute("SearchColumn", searchColumn);
		}else{
			searchColumn = usession.getAttribute("SearchColumn").toString();
		}
	}else{
		usession.setAttribute("SearchColumn", searchColumn);
	}

	String filter = request.getParameter("Filter");
	if(filter == null || filter.length()==0){
		if(usession.getAttribute("Filter") == null){
			filter = "like";
			usession.setAttribute("Filter", filter);
		}else{
			filter = usession.getAttribute("Filter").toString();
		}
	}else{
		filter = usession.getAttribute("Filter").toString();
	}

	String searchText = request.getParameter("SearchText");
	if(searchText == null || searchText.length()==0){
		if(usession.getAttribute("SearchText") == null){
			searchText = "%";
			usession.setAttribute("SearchText", searchText);
		}else{
			searchText = usession.getAttribute("SearchText").toString();
		}
	}else{
		usession.setAttribute("SearchText", searchText);
	}

	searchText = searchText.replaceAll("'","");

	String searchLimit_s = request.getParameter("SearchLimit");
	if(searchLimit_s == null || searchLimit_s.length()==0){
		if(usession.getAttribute("SearchLimit") == null){
			searchLimit_s = "25";
			usession.setAttribute("SearchLimit", searchLimit_s);
		}else{
			searchLimit_s = usession.getAttribute("SearchLimit").toString();
		}
	}else{
		usession.setAttribute("SearchLimit", searchLimit_s);
	}

	int searchLimit = Integer.parseInt(searchLimit_s);

	String offset_s = request.getParameter("Offset");
	if(offset_s == null || offset_s.length()==0){
		if(usession.getAttribute("Offset") == null){
			offset_s = "0";
			usession.setAttribute("Offset", offset_s);
		}else{
			offset_s = usession.getAttribute("Offset").toString();
		}
	}else{
		usession.setAttribute("Offset", offset_s);
	}

	int offset = Integer.parseInt(offset_s);

	String where_clause = request.getParameter("Where_Clause");
	if(where_clause == null || where_clause.length() == 0)
		where_clause="";

	String edit_count_s = request.getParameter("Edit_Count");
	if(edit_count_s == null || edit_count_s.length() == 0)
		edit_count_s="0";

	int edit_count = Integer.parseInt(edit_count_s);

	String edit[] = new String[edit_count];

	for(int i=1; i<=edit_count; i++){
		edit[i-1] = request.getParameter("Edit_"+i);
		if(edit[i-1] == null || edit[i-1].length() == 0)
			edit[i-1]="";
	}

	String pm_main_category = request.getParameter("PM_Main_Category");
	if(pm_main_category == null || pm_main_category.length() == 0)
		pm_main_category="%";

	String pm_category = request.getParameter("PM_Category");
	if(pm_category == null || pm_category.length() == 0)
		pm_category="%";

	String pm_item = request.getParameter("PM_Item");
	if(pm_item == null || pm_item.length() == 0)
		pm_item="%";

	String pm = request.getParameter("PM");
	if(pm == null || pm.length() == 0)
		pm="0.5";

	String pm_m = request.getParameter("PM_M");
	if(pm_m == null || pm_m.length() == 0)
		pm_m="0.4";

	String cPM = request.getParameter("cPM");
	if(cPM == null || cPM.length() == 0)
		cPM="";

	String cPM_M = request.getParameter("cPM_M");
	if(cPM_M == null || cPM_M.length() == 0)
		cPM_M="";

	String order_id = request.getParameter("Order_id");
	if(order_id == null || order_id.length() == 0)
		order_id="";

	String client_id = request.getParameter("client_id");
	if(client_id == null || client_id.length() == 0){
		ResultSet rsClientID = con.createStatement().executeQuery("SELECT userId FROM tbl_user ORDER BY last_name, first_name, email LIMIT 1");
		if(rsClientID.next())
			client_id=rsClientID.getString(1);
		else
			client_id="";
	}

	String client_type = request.getParameter("client_type");
	if(client_type == null || client_type.length() == 0)
		client_type="";

	String pass = request.getParameter("pass");
	if(pass == null || pass.length() == 0)
		pass="";

	//Todays Date
	Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	String DATE_FORMAT = "MM/dd/yyyy";
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
	sdf.setTimeZone(TimeZone.getDefault());
	String date = sdf.format(cal.getTime());//starting date

	String month_today = date.substring(0,date.indexOf('/'));
	String day_today = date.substring(3,5);
	String year_today = date.substring(6,10);
	String today = month_today + "/" + day_today + "/" + year_today;

	String sqlDate = "" + year_today + "-" + month_today + "-" + day_today;

	String sql_complete_date ="";

	String complete_date = request.getParameter("Complete_Date");
	if(complete_date == null || complete_date.length() == 0){
		complete_date = null;
	}	/*else{

		char delim = '/';
		if(complete_date.indexOf(delim) == -1)
			delim = '-';

		String month = complete_date.substring(0,complete_date.indexOf(delim));
		String day = complete_date.substring(complete_date.indexOf(delim)+1,complete_date.lastIndexOf(delim));
		String year_long = complete_date.substring(complete_date.lastIndexOf(delim)+1,complete_date.length());

		if( year_long.length() == 2)
			year_long = "20"+year_long;
		if(month.length() == 1)
			month = "0"+month;
		if(day.length() == 1)
		day = "0"+day;

		sql_complete_date = "" + year_long + "-" + month + "-" + day;
	}*/

	String paid_s = request.getParameter("Paid");
	if(paid_s == null || paid_s.length() == 0)
		paid_s="0";

	int paid = Integer.parseInt(paid_s);

	int pk_count=0;
	String primary_key[] = new String[10];

	DatabaseMetaData dbmd = con.getMetaData();
	ResultSet rsPK = dbmd.getPrimaryKeys("","",edit_table);

	while(rsPK.next()){
		primary_key[pk_count] = (String)rsPK.getString(4);
		pk_count++;
	}

	int record_count = 0;

	ResultSet rsCount = con.createStatement().executeQuery("SELECT Count(*) FROM "+edit_table+" ");
	if(rsCount.next()){
		record_count = rsCount.getInt(1);
	}

	ResultSet rsSelect = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" LIMIT 1");
	ResultSetMetaData rsMdSelect = rsSelect.getMetaData();
	%>

	<div>
	<h2 id="nav">&nbsp;Navigation .: <a href="home">Home</a>&nbsp;>&nbsp;Admin Page (v4.3)</h2>

		<div id="adminleft">
			<div class="border">
				<h3>Edit Data</h3>
				<ul>
					<li><a href="webadmin?Edit_Table=tbl_user&amp;Action=Edit&amp;SearchColumn=type&amp;SearchText=Agent">Edit Agents <img src="img/help.gif" alt="List of Agents"></a></li>

					<li><a href="webadmin?Edit_Table=tbl_insurance_co&amp;Action=Edit">Edit Insurance Co <img src="img/help.gif" alt="List of Insurance Co's"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_billing_co&amp;Action=Edit"> - Edit Billing Co <img src="img/help.gif" alt="List of Billing Co's"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_lab_co&amp;Action=Edit"> - Edit Lab Co <img src="img/help.gif" alt="List of Lab Co's"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_insurance_type&amp;Action=Edit">Edit Insurance Type <img src="img/help.gif" alt="List of Insurance Types"></a></li>

					<li><a href="webadmin?Edit_Table=tbl_user&amp;Action=Edit&amp;SearchColumn=type&amp;SearchText=Examiner">Edit Examiner <img src="img/help.gif" alt="List of Examiners"></a></li>

					<br/><a href="webadmin?Edit_Table=tbl_examiner_exam&amp;Action=Edit"> - Edit Exams Offerred <img src="img/help.gif" alt="The Exams offered by each Examiner"></a>


					<%/*
					<li><a href="webadmin?Edit_Table=tbl_status_type&amp;Action=Edit">Edit Status Options <img src="img/help.gif" alt="List of possible Status'"></a></li>
					*/%>

					<li><a href="webadmin?Edit_Table=tbl_news&amp;Action=Edit">Edit News <img src="img/help.gif" alt="News displayed on the news page"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_faq&amp;Action=Edit">Edit FAQ <img src="img/help.gif" alt="FAQs displayed on the FAQ page"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_links&amp;Action=Edit">Edit Links <img src="img/help.gif" alt="Links displayed on the links page"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_testimonials&amp;Action=Edit">Edit Testimonials <img src="img/help.gif" alt="Testimonials displayed on the testimonials page"></a></li>

					<li><a href="webadmin?Edit_Table=tbl_user&amp;Action=Edit&amp;SearchColumn=type&amp;SearchText='">Edit All Users <img src="img/help.gif" alt="List of All users"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_user&amp;Action=Edit&amp;SearchColumn=type&amp;SearchText=Admin">Edit Admin <img src="img/help.gif" alt="List of Admin users"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_user&amp;Action=Edit&amp;SearchColumn=type&amp;SearchText=Staff">Edit Staff <img src="img/help.gif" alt="List of Staff users"></a></li>

					<li><a href="webadmin?Edit_Table=tbl_company&amp;Action=Edit">Edit My Company Info <img src="img/help.gif" alt="Company Info is how the website knows the company's name and contact information"></a></li>

				</ul>
			</div>
			<div class="border">
				<h3>Functions</h3>
				<ul>

					<li><a href="webadmin?Edit_Table=tbl_state&amp;Action=Edit">Modify State List <img src="img/help.gif" alt="List of States displayed on entry forms"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_language&amp;Action=Edit">Modify Language List <img src="img/help.gif" alt="List of Language options displayed on entry forms"></a></li>
					<li><a href="webadmin?Edit_Table=tbl_exam_type&amp;Action=Edit">Edit Possible Exams List <img src="img/help.gif" alt="All possible exams that a Prop Ins could request"></a></li>
					<li><a href="webadmin?Action=Password">Change Users Password <img src="img/help.gif" alt="Update a Users Type or Password"></a></li>

				</ul>
			</div>
			<div class="border">

				<h3>Admin Client Editor</h3>
				<ul>
					<li><a href="webadmin?Edit_Table=tbl_client&amp;Action=Edit">Edit Client*</a></li>
					<li><a href="webadmin?Edit_Table=tbl_client_exam&amp;Action=Edit">Edit Exam*</a></li>
					<li><a href="webadmin?Edit_Table=tbl_message&amp;Action=Edit">Edit Messages*</a></li>
					<li><a href="webadmin?Edit_Table=tbl_status&amp;Action=Edit">Edit Status*</a></li>
				</ul>
				<p>* Should only be used if website does not perform desired function</p>
			</div>

			<div class="border">
				<h3>Logout</h3>
				<ul>
					<li><a href="logout">Logout</a></li>
				</ul>
			</div>
		</div>

		<div id="adminright">

			<%// Update Password 1

			if (action.equals("Password")){%>

				<h3>Change a User Password / Account Type</h3>
				<p>Instructions: Select a user, change account type or enter a new password.</p>
				<form name="password" action="webadmin" method="post">

					<div>
						Client:

						<select name="client_id" onChange="password.submit()">
							<%
							ResultSet rsC = con.createStatement().executeQuery("SELECT * FROM tbl_user ORDER BY last_name, first_name, email");
							while(rsC.next()){
								if(client_id.equals(rsC.getString(1))){
									client_type=rsC.getString("type");

								}%>
								<option value="<%=rsC.getString(1)%>" <%=(client_id.equals(rsC.getString(1))?"SELECTED":"")%>><%=rsC.getString(6)+", "+rsC.getString(5)+" ("+rsC.getString(2)+")"%></option>
							<%}%>
						</select>

						<br/>

						Type:

						<select name="client_type">
							<%ResultSet rsEnum2 = con.createStatement().executeQuery("SHOW COLUMNS FROM tbl_user LIKE 'type'");
							if(rsEnum2.next()){
								String enumString = rsEnum2.getString("Type").replaceAll("'","");
								enumString = enumString.substring(enumString.indexOf("(")+1,enumString.indexOf(")"));

								String[] enumArray = enumString.split(",");
								for (int x=0; x<enumArray.length; x++){%>
									<option value="<%=enumArray[x]%>" <%=((enumArray[x].equals(client_type))?"SELECTED":"")%>><%=enumArray[x]%></option>
								<%}%>
							<%}%>
						</select>
						<input name="Action" type="submit" value="Change Type"/>

						<br/>New Password:<input name="pass" type="text" size="32" maxlength="20" value="" />
						<input name="Action" type="submit" value="Change Password"/>
					</div>
					<input name="Action" type="hidden" value="Password"/>

				</form>

			<%// Update Password 2

			}else if (action.equals("Change Password")){

				con.createStatement().executeUpdate("UPDATE IGNORE tbl_user SET password=MD5('"+pass+"') WHERE userId='"+client_id+"'");%>

				<h3>Change a User Password / Account Type</h3>
				<p>Password update completed successfully.</p>

				<form name="Done" action="webadmin" method="post">
				<div>
					<input name="Action" type="hidden" value="Password" />
					<input name="client_id" type="hidden" value="<%=client_id%>" />
					<input name="Button" type="submit" value="Complete" />
				</div>
				</form>

			<%// Update Password 3

			}else if (action.equals("Change Type")){

				con.createStatement().executeUpdate("UPDATE IGNORE tbl_user SET type='"+client_type+"' WHERE userId='"+client_id+"'");%>

				<h3>Change a User Password / Account Type</h3>
				<p>Type update completed successfully.</p>

				<form name="Done" action="webadmin" method="post">
				<div>
					<input name="Action" type="hidden" value="Password" />
					<input name="client_id" type="hidden" value="<%=client_id%>" />
					<input name="Button" type="submit" value="Complete" />
				</div>
				</form>

			<%// Edit Records

			}else{%>

				<form action="webadmin" method="post" style="padding:0px;margin:0px">
				<div>
					<h3>Database Table '<%=edit_table%>'</h3>

						<h3>Record Statistics: <%=record_count%> Total Records (<%=(offset+1)%> to <%=(record_count<(offset+searchLimit)?record_count:(offset+searchLimit))%> Retrieved)</h3>

							<table>

								<tr>
									<th class="alt">Search Column</th>
									<th class="alt">Filter</th>
									<th class="alt">Search For</th>
									<th class="alt">Offset</th>
									<th class="alt">Limit</th>
									<th class="alt">Search</th>

								</tr>

								<tr>
									<td class="alt">
										<select name="SearchColumn">
											<%if(searchColumn.length()==0){
												searchColumn = rsMdSelect.getColumnName(1);
											}
											for(int i=1; i<=rsMdSelect.getColumnCount(); i++){%>
												<option value="<%=rsMdSelect.getColumnName(i)%>" <%=((searchColumn.equals(rsMdSelect.getColumnName(i)))?"SELECTED":"")%>><%=rsMdSelect.getColumnName(i)%></option>
											<%}%>
										</select>
									</td>
									<td class="alt">
										<select name="Filter">
											<option value="like" <%=((filter.equals("like"))?"SELECTED":"")%>>like</option>
											<option value="=" <%=((filter.equals("="))?"SELECTED":"")%>>=</option>
											<option value=">" <%=((filter.equals(">"))?"SELECTED":"")%>>></option>
											<option value=">=" <%=((filter.equals(">="))?"SELECTED":"")%>>>=</option>
											<option value="<" <%=((filter.equals("<"))?"SELECTED":"")%>><</option>
											<option value="<=" <%=((filter.equals("<="))?"SELECTED":"")%>><=</option>
											<option value="<>" <%=((filter.equals("<>"))?"SELECTED":"")%>>not =</option>
											<option value="not like" <%=((filter.equals("not like"))?"SELECTED":"")%>>not like</option>
										</select>
									</td>
									<td class="alt"><input name="SearchText" type="text" size="15" value="<%=searchText%>"></td>
									<td class="alt"><input name="Offset" type="text" size="3" value="<%=offset%>"></td>
									<td class="alt"><input name="SearchLimit" type="text" size="3" value="<%=searchLimit%>"></td>
									<td class="alt"><input name="Action" type="submit" value="Search" /></td>
									<td class="alt"><input name="Edit_Table" type="hidden" value="<%=edit_table%>" /></td>
								</tr>

							</table>
							<iframe src="adminSearch?Edit_Table=<%=edit_table%>&amp;SearchColumn=<%=searchColumn%>&amp;SearchText=<%=searchText%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=offset%>&amp;Filter=<%=filter%>&amp;ASAction=<%=asaction%>&amp;reURL=<%=reURL%>&amp;Where_Clause=<%=where_clause%>" height="450px" width="100%">
								<p>This is an inline frame. If you can read this text your browser is not set to support inline frames.</p>
							</iframe>

							<p>Note: * Indicates Primary Key</p>
				</div>
				</form>
			<%}%>
		</div>
	</div>


<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>


