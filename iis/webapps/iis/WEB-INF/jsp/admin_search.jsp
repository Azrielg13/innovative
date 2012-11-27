<%@page contentType="text/html"%>

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


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<%@ include file="/WEB-INF/jsp/include/head.jsp" %>
</head>

<%Connection con = null;
try {

	HttpSession usession = request.getSession(true);

	String reURL = request.getParameter("reURL");
	if(reURL == null || reURL.length() == 0)
		reURL="webadmin";

	//Used for updating tbl_item and tbl_picture
	String upload = request.getParameter("Upload");
	if(upload == null || upload.length() == 0)
		upload="0";

	String upload2 = request.getParameter("Upload2");
	if(upload2 == null || upload2.length() == 0)
		upload2="0";

	String action = request.getParameter("ASAction");
	if(action == null || action.length() == 0)
		action="";

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

	where_clause = where_clause.replaceAll("&#61;","=");
	where_clause = where_clause.replaceAll("&nbsp;"," ");

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

	//Todays Date
	String date_field = "";

	Calendar cal = Calendar.getInstance(TimeZone.getDefault());


	String DATE_FORMAT = "MM/dd/yyyy";
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
	sdf.setTimeZone(TimeZone.getDefault());
	String date = sdf.format(cal.getTime());//starting date

	DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
	sdf.setTimeZone(TimeZone.getDefault());
	String today_datetime = sdf.format(cal.getTime());


	con =  DBConnector.getInstance().getConnection();

	String up_location = "img/upload"; //upload images to this default location

	int pk_count=0;

	String primary_key[] = new String[10];

	DatabaseMetaData dbmd = con.getMetaData();
	ResultSet rsPK = dbmd.getPrimaryKeys("","",edit_table);

	while(rsPK.next()){
		primary_key[pk_count] = (String)rsPK.getString(4);
		pk_count++;
	}

	ResultSet rsSelect = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" LIMIT 1");
	ResultSetMetaData rsMdSelect = rsSelect.getMetaData();

	String set_data = "";
	String set_data_add = "";


	if(edit_count == rsMdSelect.getColumnCount()){
		boolean where_empty = false;

		if(where_clause.equals(""))
			where_empty = true;

		for(int i=1; i<=rsMdSelect.getColumnCount(); i++){

			if(!rsMdSelect.getColumnName(i).equals("last_update")){

				edit[i-1] = edit[i-1].replaceAll("'","");



				edit[i-1] = edit[i-1].replaceAll("\\\\","\\\\\\\\");

				edit[i-1] = edit[i-1].replaceAll("%28", "(");
				edit[i-1] = edit[i-1].replaceAll("%29", ")");
				edit[i-1] = edit[i-1].replaceAll("%22", "\"");
				edit[i-1] = edit[i-1].replaceAll("%5C", "f");
				edit[i-1] = edit[i-1].replaceAll("&", "&amp;");


				edit[i-1] = edit[i-1].replaceAll("%2D", "-");

				edit[i-1] = edit[i-1].replaceAll("%2C", ",");
				edit[i-1] = edit[i-1].replaceAll("%20", " ");
				edit[i-1] = edit[i-1].replaceAll("%2F", "/");



				if(set_data.equals("")){
					set_data += rsMdSelect.getColumnName(i)+"='"+edit[i-1]+"'";
				}else{
					set_data += ","+rsMdSelect.getColumnName(i)+"='"+edit[i-1]+"'";
				}

				if(edit[i-1]!=null && edit[i-1].length()>0){
					if(set_data_add.equals("")){
						if(rsMdSelect.getColumnName(i).equals("password")){
							set_data_add += rsMdSelect.getColumnName(i)+"=MD5('"+edit[i-1]+"')";
						}else{
							set_data_add += rsMdSelect.getColumnName(i)+"='"+edit[i-1]+"'";
						}
					}else{
						if(rsMdSelect.getColumnName(i).equals("password")){
							set_data_add += ","+rsMdSelect.getColumnName(i)+"=MD5('"+edit[i-1]+"')";
						}else{
							set_data_add += ","+rsMdSelect.getColumnName(i)+"='"+edit[i-1]+"'";
						}
					}
				}

				if(edit_table.equals("tbl_main_category") && rsMdSelect.getColumnName(i).equals("main_category")){
					up_location += "/"+edit[i-1];
				}else if(edit_table.equals("tbl_category") && rsMdSelect.getColumnName(i).equals("category")){
					ResultSet rsUpLoc = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" WHERE category='"+edit[i-1]+"'");
					if(rsUpLoc.next())
						up_location += "/"+rsUpLoc.getString("category");
				}else if(edit_table.equals("tbl_item") && rsMdSelect.getColumnName(i).equals("category")){
					ResultSet rsUpLoc = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" WHERE category='"+edit[i-1]+"'");
					if(rsUpLoc.next())
						up_location += "/"+rsUpLoc.getString("category");
				}

				if(where_empty){
					for(int j=0; j<pk_count; j++){
						if(primary_key[j].equals(rsMdSelect.getColumnName(i))){
							if(where_clause.equals("")){
								where_clause += rsMdSelect.getColumnName(i)+"='"+edit[i-1]+"'";
							}else{
								where_clause += " AND "+rsMdSelect.getColumnName(i)+"='"+edit[i-1]+"'";
							}
						}
					}
				}
			}
		}
	}%>

	<body style="background: #fff;">

		<div id="blank">

			<%if(action.equals("Edit")){%>

				<%ResultSet rsEdit = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" WHERE "+where_clause+"");
				ResultSetMetaData rsMdEdit = rsEdit.getMetaData();%>

				<h4>Edit Record</h4>

				<%if(rsEdit.next()){%>
					<form class="alt" name="Edit" Action="adminSearch" Method="Post">
					<table>
						<%for(int i=1; i<=rsMdEdit.getColumnCount(); i++){%>
							<tr>
								<th class="alt">
									<%boolean printed = false;
									boolean primary = false;
									for(int j=0; j<pk_count; j++){
										if(primary_key[j].equals(rsMdEdit.getColumnName(i)) && !printed){%>
											<%=rsMdEdit.getColumnName(i)+"*"%>
											<%printed = true;%>
											<%primary = true;%>
										<%}else if(j==pk_count-1 && !printed){%>
											<%=rsMdEdit.getColumnName(i)%>
											<%printed=true;%>
										<%}%>
									<%}%>
								</th>

								<td class="alt">

									<%if(edit_table.equals("tbl_examiner_exam") && rsMdEdit.getColumnName(i).equals("userId")){%>
										<select name="Edit_<%=i%>">
											<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT userId, last_name, first_name FROM tbl_user WHERE type='Examiner' AND hide=0 ORDER BY last_name, first_name");
												while(rsFkC.next()){%>
													<option value="<%=rsFkC.getString(1)%>" <%=(rsEdit.getString(i).equals(rsFkC.getString(1))?"SELECTED":"")%>><%=rsFkC.getString(2)+", "+rsFkC.getString(3)%></option>
												<%}%>
										</select>

									<%}else if(edit_table.equals("tbl_examiner_exam") && rsMdEdit.getColumnName(i).equals("examId")){%>
										<select name="Edit_<%=i%>">
											<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT examId, name FROM tbl_exam_type WHERE hide=0 ORDER BY name");
												while(rsFkC.next()){%>
													<option value="<%=rsFkC.getString(1)%>" <%=(rsEdit.getString(i).equals(rsFkC.getString(1))?"SELECTED":"")%>><%=rsFkC.getString(2)%></option>
												<%}%>
										</select>

									<%}else if(edit_table.equals("tbl_agent_code") && rsMdEdit.getColumnName(i).equals("userId")){%>
										<select name="Edit_<%=i%>">
											<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT userId, last_name, first_name FROM tbl_user WHERE type='Agent' AND hide=0 ORDER BY last_name, first_name");
											while(rsFkC.next()){%>
												<option value="<%=rsFkC.getString(1)%>" <%=(rsEdit.getString(i).equals(rsFkC.getString(1))?"SELECTED":"")%>><%=rsFkC.getString(2)+", "+rsFkC.getString(3)%></option>
											<%}%>
										</select>

									<%}else if(edit_table.equals("tbl_insurance_co") && rsMdEdit.getColumnName(i).equals("billingId")){%>
										<select name="Edit_<%=i%>">
											<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT billingId, name, city, state FROM tbl_billing_co WHERE hide=0 ORDER BY name");
											while(rsFkC.next()){%>
												<option value="<%=rsFkC.getString(1)%>" <%=(rsEdit.getString(i)!=null&&rsEdit.getString(i).equals(rsFkC.getString(1))?"SELECTED":"")%>><%=rsFkC.getString(2)+" - "+rsFkC.getString(3)+", "+rsFkC.getString(4)%></option>
											<%}%>
										</select>


									<%}else if(edit_table.equals("tbl_insurance_co") && rsMdEdit.getColumnName(i).equals("labId")){%>
										<select name="Edit_<%=i%>">
											<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT labId, name, city, state FROM tbl_lab_co WHERE hide=0 ORDER BY name");
											while(rsFkC.next()){%>
												<option value="<%=rsFkC.getString(1)%>" <%=(rsEdit.getString(i)!=null&&rsEdit.getString(i).equals(rsFkC.getString(1))?"SELECTED":"")%>><%=rsFkC.getString(2)+" - "+rsFkC.getString(3)+", "+rsFkC.getString(4)%></option>
											<%}%>
										</select>

									<%}else if(primary){%>
										<textarea style="background-color: #CCC; color: #000;" disabled="true" name="x_<%=i%>" cols="50" rows="<%=(rsEdit.getString(i)!=null&&rsEdit.getString(i).length()!=0?(Math.round(rsEdit.getString(i).length()/50)+1):1)%>"><%=(rsEdit.getString(i)!=null&&rsEdit.getString(i).length()!=0?rsEdit.getString(i):"")%></textarea>
										<input name="Edit_<%=i%>" type="hidden" value="<%=rsEdit.getString(i)%>" />

									<%}else if(rsMdEdit.getColumnTypeName(i).equals("DATETIME") || rsMdEdit.getColumnTypeName(i).equals("DATE")){%>

										<input name="Edit_<%=i%>" type="text" size="60" value="<%=(rsEdit.getString(i)==null||rsEdit.getString(i).length()==0?"":rsEdit.getString(i))%>"/>
										<a href="javascript:cal<%=i%>.popup();"><img src="img/cal.gif" /></a>

										<script type="text/javascript">
											<!--
											// create calendar object(s) just after form tag closed
											// specify form element as the only parameter (document.forms['formname'].elements['inputname']);
											// note: you can have as many calendar objects as you need for your application
											var cal<%=i%> = new calendar3(document.forms['Edit'].elements['Edit_<%=i%>']);
											cal<%=i%>.year_scroll = true;
											<%if(rsMdEdit.getColumnTypeName(i).equals("DATETIME")){%>
												cal<%=i%>.time_comp = true;
											<%}else{%>
												cal<%=i%>.time_comp = false;
											<%}%>
											//-->
										</script>

									<%}else if((rsMdEdit.getColumnTypeName(i).startsWith("TINY") || rsMdEdit.getColumnTypeName(i).startsWith("tiny") ) && rsMdEdit.getColumnDisplaySize(i)==1){%>
										<select name="Edit_<%=i%>">
											<option value="1" <%=((rsEdit.getString(i).equals("1"))?"SELECTED":"")%>>Yes</option>
											<option value="0" <%=((rsEdit.getString(i).equals("0"))?"SELECTED":"")%>>No</option>
										</select>


									<%}else if(rsMdEdit.getColumnName(i).equals("type")){%>
										<select name="Edit_<%=i%>">
											<%ResultSet rsEnum = con.createStatement().executeQuery("SHOW COLUMNS FROM "+edit_table+" LIKE '"+rsMdEdit.getColumnName(i)+"'");
											if(rsEnum.next()){
												String enumString = rsEnum.getString("Type").replaceAll("'","");
												enumString = enumString.substring(enumString.indexOf("(")+1,enumString.indexOf(")"));

												String[] enumArray = enumString.split(",");
												for (int x=0; x<enumArray.length; x++){%>
													<option value="<%=enumArray[x]%>" <%=((enumArray[x].equals(rsEdit.getString(i)))?"SELECTED":"")%>><%=enumArray[x]%></option>
												<%}%>
											<%}%>
										</select>

									<%}else if(rsMdEdit.getColumnName(i).equals("picture")){%>

										<input name="Edit_<%=i%>" type="text" size="60" value="<%=(rsEdit.getString(i)==null||rsEdit.getString(i).length()==0?"":rsEdit.getString(i))%>"/>
										<br/><input name="Upload" type="checkbox" value="picture" onClick="showHide('Edit_<%=i%>')" /> Upload Different Image from Disk
										<%if(rsEdit.getString(i)!=null && rsEdit.getString(i).length()!=0){%>
											<br/>OR <a href="<%=rsEdit.getString(i)%>" target="_blank">Preview Existing Image</a>
										<%}%>

									<%}else if(rsMdEdit.getColumnName(i).equals("alt_picture")){%>

										<input name="Edit_<%=i%>" type="text" size="60" value="<%=(rsEdit.getString(i)==null||rsEdit.getString(i).length()==0?"":rsEdit.getString(i))%>"/>
										<br/><input name="Upload2" type="checkbox" value="alt_picture" onClick="showHide('Edit_<%=i%>')" /> Upload Different Image from Disk										<%if(rsEdit.getString(i)!=null && rsEdit.getString(i).length()!=0){%>
											<br/>OR <a href="<%=rsEdit.getString(i)%>" target="_blank">Preview Existing Image</a>
										<%}%>

									<%}else if(rsMdEdit.getColumnName(i).equals("last_update")){%>
										<input style="background-color: #CCC; color: #000;" name="Edit_<%=i%>" type="text" size="60" value="<%=(rsEdit.getString(i)==null||rsEdit.getString(i).length()==0?"":rsEdit.getString(i))%>" disabled="true"/>

									<%}else if(rsMdEdit.getColumnName(i).equals("password")){%>
										<input style="background-color: #CCC; color: #000;" name="Edit_<%=i%>" type="text" size="40" value="<%=(rsEdit.getString(i)==null||rsEdit.getString(i).length()==0?"":rsEdit.getString(i))%>" disabled="true"/>
										<input name="Edit_<%=i%>" type="hidden" value="<%=(rsEdit.getString(i)==null||rsEdit.getString(i).length()==0?"":rsEdit.getString(i))%>"/>
										<a href="webadmin?Action=Password" target="_parent">Change Password</a>

									<%}else{%>
										<textarea name="Edit_<%=i%>" cols="50" rows="<%=(rsEdit.getString(i)!=null&&rsEdit.getString(i).length()!=0?(Math.round(rsEdit.getString(i).length()/50)+1):2)%>"><%=(rsEdit.getString(i)!=null&&rsEdit.getString(i).length()!=0?rsEdit.getString(i):"")%></textarea>
									<%}%>
								</td>
								<td class="alt">(<%=rsMdEdit.getColumnTypeName(i)%> - <%=rsMdEdit.getColumnDisplaySize(i)%>)</td>
							<tr>
						<%}%>
						<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
						<input name="Where_Clause" type="hidden" value="<%=where_clause%>" />
						<input name="Edit_Count" type="hidden" value="<%=rsMdEdit.getColumnCount()%>" />
						<input name="reURL" type="hidden" value="<%=reURL%>" />

						<tr><td><input name="ASAction" type="submit" value="Update" /></td></tr>
					</table>
					</form>
				<%}%>

			<%}else if(action.equals("Delete")){

				ResultSet rsDelete = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" WHERE "+where_clause+" ");
				ResultSetMetaData rsMdDelete = rsDelete.getMetaData();%>

				<h4>Are your sure you want to delete this record?</h4>

				<%if(rsDelete.next()){%>
					<form class="alt" name="Delete" Action="adminSearch" Method="Post">
					<table>
						<%for(int i=1; i<=rsMdDelete.getColumnCount(); i++){%>
							<tr>
								<th>
									<% boolean printed=false;
									for(int j=0; j<pk_count; j++){
										if(primary_key[j].equals(rsMdDelete.getColumnName(i))){%>
											<%=rsMdDelete.getColumnName(i)+"*"%>
											<%printed = true;
										}else if(j==pk_count-1 && !printed){%>
											<%=rsMdDelete.getColumnName(i)%>
											<%printed = true;
										}%>
									<%}%>
								</th>

								<td class="alt"><%=rsDelete.getString(i)%></td>
								<td class="alt">(<%=rsMdDelete.getColumnTypeName(i)%> - <%=rsMdDelete.getColumnDisplaySize(i)%>)</td>
							<tr>
						<%}%>
						<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
						<input name="Where_Clause" type="hidden" value="<%=where_clause%>" />

						<tr><td><input name="ASAction" type="submit" value="Confirm" /></td></tr>
					</table>
					</form>
				<%}%>

			<%}else if(action.equals("Create")){

				DatabaseMetaData dbmdc = con.getMetaData();
				ResultSet rsCount = dbmdc.getColumns("","",edit_table,"");
				int colCount=0;
				while(rsCount.next()){
					colCount++;
				}

				ResultSet rsC = dbmdc.getColumns("","",edit_table,"");
				ResultSetMetaData rsMdC = rsC.getMetaData();


				ResultSet rsAdd = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" LIMIT 1 ");
				ResultSetMetaData rsMdAdd = rsAdd.getMetaData();
				%>

				<h4>Add a Record</h4>

				<form class="alt" name="Create" action="adminSearch" method="post">
				<table>
				<%int k=1;
				while(rsC.next()){%>
						<tr>
							<th class="alt">
								<%boolean printed=false;
								for(int j=0; j<pk_count; j++){
									if(primary_key[j].equals(rsC.getString("COLUMN_NAME"))){%>
										<%=rsC.getString("COLUMN_NAME")+"*"%>
										<%printed = true;
									}else if(j==pk_count-1 && !printed){%>
										<%=rsC.getString("COLUMN_NAME")%>
										<%printed = true;
									}%>
								<%}%>
							</th>

							<td class="alt">

							<%
							String add_extra = "";
							String add_default = "";
							ResultSet rsColumnAdd = con.createStatement().executeQuery("SHOW COLUMNS FROM "+edit_table+" LIKE '"+rsMdAdd.getColumnName(k)+"'");
							if(rsColumnAdd.next()){
								add_extra = rsColumnAdd.getString("Extra");
								add_default = rsColumnAdd.getString("Default");
							}%>


								<%if(edit_table.equals("tbl_examiner_exam") && rsMdAdd.getColumnName(k).equals("userId")){%>
									<select name="Edit_<%=k%>">
										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT userId, last_name, first_name FROM tbl_user WHERE type='Examiner' AND hide=0 ORDER BY last_name, first_name");
											while(rsFkC.next()){%>
												<option value="<%=rsFkC.getString(1)%>"><%=rsFkC.getString(2)+", "+rsFkC.getString(3)%></option>
											<%}%>
									</select>

								<%}else if(edit_table.equals("tbl_examiner_exam") && rsMdAdd.getColumnName(k).equals("examId")){%>
									<select name="Edit_<%=k%>">
										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT examId, name FROM tbl_exam_type WHERE hide=0 ORDER BY name");
											while(rsFkC.next()){%>
												<option value="<%=rsFkC.getString(1)%>"><%=rsFkC.getString(2)%></option>
											<%}%>
									</select>

								<%}else if(edit_table.equals("tbl_agent_code") && rsMdAdd.getColumnName(k).equals("userId")){%>
									<select name="Edit_<%=k%>">
										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT userId, last_name, first_name FROM tbl_user WHERE type='Agent' AND hide=0 ORDER BY last_name, first_name");
											while(rsFkC.next()){%>
												<option value="<%=rsFkC.getString(1)%>"><%=rsFkC.getString(2)+", "+rsFkC.getString(3)%></option>
											<%}%>
									</select>

								<%}else if(edit_table.equals("tbl_insurance_co") && rsMdAdd.getColumnName(k).equals("billingId")){%>
									<select name="Edit_<%=k%>">
										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT billingId, name, city, state FROM tbl_billing_co WHERE hide=0 ORDER BY name");
										while(rsFkC.next()){%>
											<option value="<%=rsFkC.getString(1)%>"><%=rsFkC.getString(2)+" - "+rsFkC.getString(3)+", "+rsFkC.getString(4)%></option>
										<%}%>
									</select>


								<%}else if(edit_table.equals("tbl_insurance_co") && rsMdAdd.getColumnName(k).equals("labId")){%>
									<select name="Edit_<%=k%>">
										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT labId, name, city, state FROM tbl_lab_co WHERE hide=0 ORDER BY name");
										while(rsFkC.next()){%>
											<option value="<%=rsFkC.getString(1)%>"><%=rsFkC.getString(2)+" - "+rsFkC.getString(3)+", "+rsFkC.getString(4)%></option>
										<%}%>
									</select>

								<%}else if(rsMdAdd.getColumnTypeName(k).equals("DATETIME") || rsMdAdd.getColumnTypeName(k).equals("DATE")){%>

										<input name="Edit_<%=k%>" type="text" size="60" value=""/>
										<a href="javascript:cal<%=k%>.popup();"><img src="img/cal.gif" /></a>

										<script type="text/javascript">
											<!--
											// create calendar object(s) just after form tag closed
											// specify form element as the only parameter (document.forms['formname'].elements['inputname']);
											// note: you can have as many calendar objects as you need for your application
											var cal<%=k%> = new calendar3(document.forms['Create'].elements['Edit_<%=k%>']);
											cal<%=k%>.year_scroll = true;
											<%if(rsMdAdd.getColumnTypeName(k).equals("DATETIME")){%>
												cal<%=k%>.time_comp = true;
											<%}else{%>
												cal<%=k%>.time_comp = false;
											<%}%>
											//-->
										</script>

								<%}else if((rsMdAdd.getColumnTypeName(k).startsWith("TINY") || rsMdAdd.getColumnTypeName(k).startsWith("tiny"))&& rsMdAdd.getColumnDisplaySize(k)==1){%>
									<select name="Edit_<%=k%>">
										<option value="1" <%=(rsC.getString("COLUMN_DEF")!=null && rsC.getString("COLUMN_DEF").equals("1")?"selected":"")%>>Yes</option>
										<option value="0" <%=(rsC.getString("COLUMN_DEF")!=null && rsC.getString("COLUMN_DEF").equals("0")?"selected":"")%>>No</option>
									</select>

								<%}else if(rsMdAdd.getColumnName(k).equals("type")){%>
									<select name="Edit_<%=k%>">
										<%ResultSet rsEnum = con.createStatement().executeQuery("SHOW COLUMNS FROM "+edit_table+" LIKE '"+rsMdAdd.getColumnName(k)+"'");
										if(rsEnum.next()){
											String enumString = rsEnum.getString("Type").replaceAll("'","");
											enumString = enumString.substring(enumString.indexOf("(")+1,enumString.indexOf(")"));

											String[] enumArray = enumString.split(",");
											for (int x=0; x<enumArray.length; x++){%>
												<option value="<%=enumArray[x]%>" <%=((enumArray[x].equals(rsEnum.getString("Default")))?"SELECTED":"")%>><%=enumArray[x]%></option>
											<%}%>
										<%}%>
									</select>


								<%}else if(rsMdAdd.getColumnName(k).equals("picture")){%>

									<input name="Edit_<%=k%>" type="text" size="60" value=""/>

									<br/><input name="Upload" type="checkbox" value="picture" onClick="showHide('Edit_<%=k%>')" /> Upload Image from Disk

								<%}else if(rsMdAdd.getColumnName(k).equals("alt_picture")){%>

									<input name="Edit_<%=k%>" type="text" size="60" value=""/>

									<br/><input name="Upload2" type="checkbox" value="alt_picture" onClick="showHide('Edit_<%=k%>')" /> Upload Image from Disk

								<%}else if(rsMdAdd.getColumnName(k).equals("last_update")){%>
									<input style="background-color: #CCC;" name="Edit_<%=k%>" type="text" size="60" value="" disabled="true">

								<%}else if(rsMdAdd.getColumnName(k).equals("password")){%>
									<input name="Edit_<%=k%>" type="text" size="60" value="temp123">

								<%}else if(add_extra.equals("auto_increment")){%>
									<input style="background-color: #CCC;" name="Edit_<%=k%>" type="text" size="60" value="auto number" disabled="true">
								<%}else{%>
									<textarea name="Edit_<%=k%>" cols="50" rows="2"><%=(rsC.getString("COLUMN_DEF")!=null?rsC.getString("COLUMN_DEF"):"")%></textarea>
								<%}%>
							</td>
							<td class="alt">(<%=rsMdAdd.getColumnTypeName(k)%> - <%=rsMdAdd.getColumnDisplaySize(k)%>)

							</td>
						<tr>
				<%k++;
				}%>
				<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
				<input name="Where_Clause" type="hidden" value="<%=where_clause%>" />
				<input name="Edit_Count" type="hidden" value="<%=colCount%>" />
				<input name="reURL" type="hidden" value="<%=reURL%>" />

				<tr><td><input name="ASAction" type="submit" value="Add" /></td></tr>
				</table>
				</form>

			<%}else if(action.equals("Update")){%>

				<%if(upload.equals("picture") || upload2.equals("alt_picture")){%>
					<%con.createStatement().executeUpdate("UPDATE "+edit_table+" SET "+set_data+" WHERE "+where_clause+" ");%>

					<h3>Select an Image to upload. (Update)</h3>
					<p style="text-align:left;">
						Table: <%=edit_table%><br/>
						Where: <%=where_clause%><br/>
						Location: <%=up_location%>
					</p>
					<form name="Upload_File" action="upload" method="post" enctype="multipart/form-data">
						<%if(upload.equals("picture")){%>
							<br/>Picture: <input name="FILE1" type="file" size="50" value="" />
						<%usession.setAttribute("Upload", upload);
						}%>

						<%if(upload2.equals("alt_picture")){%>
							<br/>Alt Picture: <input name="FILE2" type="file" size="50" value="" />
						<%usession.setAttribute("Upload2", upload2);
						}%>

						<%
						usession.setAttribute("Edit_Table", edit_table);
						usession.setAttribute("Where_Clause", where_clause);
						usession.setAttribute("Up_Location", up_location);
						%>
						<br/>
						<p>*Note: Uploaded images must be less than 2Mb.</p>
						<input name="Action" type="submit" value="Update" />
					</form>

				<%}else if(upload.equals("2")){%>

					<div style="text-align:left"><br/>Upload completed successfully</div>
					<form name="Done" Action="<%=reURL%>" Method="Post" target="_parent">
						<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
						<input name="ASAction" type="hidden" value="" />
						<input name="Button" type="submit" value="Complete" />
					</form>


				<%}else{%>
					<%con.createStatement().executeUpdate("UPDATE "+edit_table+" SET "+set_data+" WHERE "+where_clause+" ");%>
					<div style="text-align:left;">
						<br/>&nbsp;&nbsp;Update completed successfully
						<form name="Done" Action="<%=reURL%>" Method="Post" target="_parent">
							<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
							<input name="ASAction" type="hidden" value="" />
							&nbsp;&nbsp;<input name="Button" type="submit" value="Complete" />
						</form>
					</div>

				<%}%>

			<%}else if(action.equals("Confirm")){
				con.createStatement().executeUpdate("DELETE FROM "+edit_table+" WHERE "+where_clause+"");%>
				<div style="text-align:left"><br/>Deletion completed successfully</div>
				<form name="Done" Action="<%=reURL%>" Method="Post" target="_parent">
						<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
						<input name="ASAction" type="hidden" value="" />
						<input name="Button" type="submit" value="Complete" />
				</form>

			<%}else if(action.equals("Add")){%>

				<%if(upload.equals("picture") || upload2.equals("alt_picture")){%>
					<%con.createStatement().executeUpdate("INSERT "+edit_table+" SET "+set_data_add+"");%>

					<h3>Select an Image to upload. (Add)</h3>
					<p style="text-align:left;">
						Table: <%=edit_table%><br/>
						Where: <%=where_clause%><br/>
						Location: <%=up_location%>
					</p>
					<form class="alt" name="Upload_File" action="upload" method="post" enctype="multipart/form-data">
						<%if(upload.equals("picture")){%>
							<br/>Picture: <input name="FILE1" type="file" size="50" value="" />
						<%usession.setAttribute("Upload", upload);
						}%>

						<%if(upload2.equals("alt_picture")){%>
							<br/>Alt Picture: <input name="FILE2" type="file" size="50" value="" />
						<%usession.setAttribute("Upload2", upload2);
						}%>

						<%
						usession.setAttribute("Edit_Table", edit_table);
						usession.setAttribute("Where_Clause", where_clause);
						usession.setAttribute("Up_Location", up_location);
						%>
						<br/>
						<p>*Note: Uploaded images must be less than 2Mb.</p>
						<input name="Action" type="submit" value="Update" />
					</form>
				<%}else{%>

					<%con.createStatement().executeUpdate("INSERT "+edit_table+" SET "+set_data_add+" ");%>

					<div style="text-align:left"><br/>Addition completed successfully</div>
					<form name="Done" Action="<%=reURL%>" Method="Post" target="_parent">
							<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
							<input name="ASAction" type="hidden" value="" />
							<input name="Button" type="submit" value="Complete" />
					</form>
				<%}%>
			<%}else{

				int totalCount = 0;

				ResultSet rsMainCount;

				ResultSet rsSearch = null;

				if(searchColumn.equals("") || searchColumn == null){
					rsSearch = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" LIMIT "+searchLimit+" OFFSET "+offset+" ");

					rsMainCount = con.createStatement().executeQuery("SELECT COUNT(*) FROM "+edit_table);
					if(rsMainCount.next()){
						totalCount =rsMainCount.getInt(1);
					}
				}else{
					rsSearch = con.createStatement().executeQuery("SELECT * FROM "+edit_table+" WHERE "+searchColumn+" "+filter+" '"+(filter.equals("like")||filter.equals("not like")?"%":"")+searchText+(filter.equals("like")||filter.equals("not like")?"%":"")+"' LIMIT "+searchLimit+" OFFSET "+offset+"");

					rsMainCount = con.createStatement().executeQuery("SELECT COUNT(*) FROM "+edit_table+" WHERE "+searchColumn+" like '%"+searchText+"%'");
					if(rsMainCount.next()){
						totalCount =rsMainCount.getInt(1);
					}
				}

				ResultSetMetaData rsMdSearch = rsSearch.getMetaData();%>


				<table>

					<tr>
						<th align="left" colspan="<%=rsMdSearch.getColumnCount()%>">
							<%for (int i=0; i<totalCount; i=i+searchLimit){
								if(i == offset){
									if(i+searchLimit > totalCount){%>
										&nbsp;<a><%=i+1%> - <%=totalCount%></a>
									<%}else{%>
										&nbsp;<a><%=i+1%> - <%=i+searchLimit%></a>
									<%}%>
								<%}else{
									if(i+searchLimit > totalCount){%>
										&nbsp;<a href="adminSearch?Edit_Table=<%=edit_table%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>"><%=i+1%> - <%=totalCount%></a>
									<%}else{%>
										&nbsp;<a href="adminSearch?Edit_Table=<%=edit_table%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>"><%=i+1%> - <%=i+searchLimit%></a>&nbsp;
									<%}%>
								<%}%>
							<%}%>
						</th>
					</tr>

					<tr>
						<th style="text-align:left;background:#ccc;" colspan="<%=rsMdSearch.getColumnCount()+2%>"><a href="adminSearch?ASAction=Create&Edit_Table=<%=edit_table%>">Add a Record</a></th>
					</tr>
					<tr>
						<th class="alt">Edit</th>
						<th class="alt">Delete</th>
						<%for(int i=1; i<=rsMdSearch.getColumnCount(); i++){
							boolean printed = false;
							for(int j=0; j<pk_count; j++){
								if(primary_key[j].equals(rsMdSearch.getColumnName(i)) && !printed ){%>
									<th class="alt">&nbsp;<%=rsMdSearch.getColumnName(i)+"*"%>&nbsp;</th>
									<%printed = true;
								}else if(j==pk_count-1 && !printed){%>
									<th class="alt">&nbsp;<%=rsMdSearch.getColumnName(i)%>&nbsp;</th>
									<%printed = true;
								}
							}
						}%>
					</tr>

					<%
					while(rsSearch.next()){
						where_clause = "("; //reset where_clause for each record
						for(int i=1; i<=rsMdSearch.getColumnCount(); i++){
							boolean printed = false;
							for(int j=0; j<pk_count; j++){
								if(primary_key[j].equals(rsMdSearch.getColumnName(i)) && !printed ){
									if(where_clause.equals("(")){
										where_clause += edit_table+"."+primary_key[j]+"&#61;'"+rsSearch.getString(i)+"'";
									}else{
										where_clause += " AND "+edit_table+"."+primary_key[j]+"&#61;'"+rsSearch.getString(i)+"'";
									}
									printed = true;
								}else if(j==pk_count-1 && !printed){
									printed = true;
								}
							}
						}
						where_clause += ")";
						%>
						<tr>
							<td class="alt"><a href="adminSearch?ASAction=Edit&Edit_Table=<%=edit_table%>&Where_Clause=<%=where_clause%>"><img src="img/editIco.gif" alt="Click to edit record"></a></td>
							<td class="alt"><a href="adminSearch?ASAction=Delete&Edit_Table=<%=edit_table%>&Where_Clause=<%=where_clause%>"><img src="img/deleteIco.gif" alt="Click to delete record"></a></td>
							<%for(int i=1; i<=rsMdSearch.getColumnCount(); i++){%>

								<%if(edit_table.equals("tbl_examiner_exam") && rsMdSearch.getColumnName(i).equals("userId")){%>

									<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT last_name, first_name FROM tbl_user WHERE userId="+rsSearch.getString(i));
										if(rsFkC.next()){%>
											<td class="alt" nowrap><%=rsFkC.getString(1)+", "+rsFkC.getString(2)%> (<%=rsSearch.getString(i)%>)</td>
										<%}else{%>
											<td class="alt" nowrap>N/A (<%=rsSearch.getString(i)%>)</td>
										<%}%>


								<%}else if(edit_table.equals("tbl_examiner_exam") && rsMdSearch.getColumnName(i).equals("examId")){%>

										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT name FROM tbl_exam_type WHERE examId="+rsSearch.getString(i));
											if(rsFkC.next()){%>
												<td class="alt" nowrap><%=rsFkC.getString(1)%> (<%=rsSearch.getString(i)%>)</td>
											<%}else{%>
												<td class="alt" nowrap>N/A (<%=rsSearch.getString(i)%>)</td>
											<%}%>


								<%}else if(edit_table.equals("tbl_agent_code") && rsMdSearch.getColumnName(i).equals("userId")){%>

										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT last_name, first_name FROM tbl_user WHERE userId="+rsSearch.getString(i));
										if(rsFkC.next()){%>
											<td class="alt" nowrap><%=rsFkC.getString(1)+", "+rsFkC.getString(2)%> (<%=rsSearch.getString(i)%>)</td>
										<%}else{%>
											<td class="alt" nowrap>N/A (<%=rsSearch.getString(i)%>)</td>
										<%}%>
								<%}else if(edit_table.equals("tbl_insurance_co") && rsMdSearch.getColumnName(i).equals("billingId")){%>

										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT name, city, state FROM tbl_billing_co WHERE billingId="+rsSearch.getString(i));
										if(rsFkC.next()){%>
											<td class="alt" nowrap><%=rsFkC.getString(1)+" - "+rsFkC.getString(2)+", "+rsFkC.getString(2)%> (<%=rsSearch.getString(i)%>)</td>
										<%}else{%>
											<td class="alt" nowrap>N/A (<%=rsSearch.getString(i)%>)</td>
										<%}%>
								<%}else if(edit_table.equals("tbl_insurance_co") && rsMdSearch.getColumnName(i).equals("labId")){%>

										<%ResultSet rsFkC = con.createStatement().executeQuery("SELECT name, city, state FROM tbl_lab_co WHERE labId="+rsSearch.getString(i));
										if(rsFkC.next()){%>
											<td class="alt" nowrap><%=rsFkC.getString(1)+" - "+rsFkC.getString(2)+", "+rsFkC.getString(2)%> (<%=rsSearch.getString(i)%>)</td>
										<%}else{%>
											<td class="alt" nowrap>N/A (<%=rsSearch.getString(i)%>)</td>
										<%}%>
								<%}else{%>
									<td class="alt" nowrap><%=(rsSearch.getString(i)!=null && rsSearch.getString(i).length()>50?rsSearch.getString(i).substring(0,50).trim()+"...":rsSearch.getString(i))%></td>
								<%}%>
							<%}%>
						</tr>
					<%}%>
					<tr>
						<th style="text-align:left;background:#ccc;" colspan="<%=rsMdSearch.getColumnCount()+2%>"><a href="adminSearch?ASAction=Create&Edit_Table=<%=edit_table%>">Add a Record</a></th>
					</tr>
					<tr>
						<th align="left" colspan="<%=rsMdSearch.getColumnCount()%>">
							<%for (int i=0; i<totalCount; i=i+searchLimit){
								if(i == offset){
									if(i+searchLimit > totalCount){%>
										&nbsp;<a><%=i+1%> - <%=totalCount%></a>
									<%}else{%>
										&nbsp;<a><%=i+1%> - <%=i+searchLimit%></a>
									<%}%>
								<%}else{
									if(i+searchLimit > totalCount){%>
										&nbsp;<a href="adminSearch?Edit_Table=<%=edit_table%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>"><%=i+1%> - <%=totalCount%></a>
									<%}else{%>
										&nbsp;<a href="adminSearch?Edit_Table=<%=edit_table%>&amp;SearchLimit=<%=searchLimit%>&amp;Offset=<%=i%>"><%=i+1%> - <%=i+searchLimit%></a>&nbsp;
									<%}%>
								<%}%>
							<%}%>
						</th>
					</tr>

				</table>
			<%}%>


		</div>
	<%} catch(SQLException sqle) {

		if(sqle.getErrorCode() == 1062){%>
			<p class="error">ERROR: The record you are try to add or update already exist, duplicate entries not allowed.</p>
		<%}%>
		<p class="error">SQL:<%=sqle.getErrorCode()%> <%=sqle.getMessage()%></p>

	<%} catch(Exception e) { %>
		<p class="error">JAVA:<%=e.getMessage()%></p>
	<%}finally{
		if(con != null)
			con.close();
	}%>
	</body>
</html>

