<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>

<%@page import="com.jspsmart.upload.*"%>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />



<%Connection con = null;
	try {

		HttpSession usession = request.getSession(true);

		con =  DBConnector.getInstance().getConnection();
		//Class.forName("org.gjt.mm.mysql.Driver");
		//con = DriverManager.getConnection(getServletContext().getInitParameter("dburl"), "adminuser", "admin");

		String edit_table = "";
		if(usession.getAttribute("Edit_Table") == null){
			edit_table = request.getParameter("Edit_Table");
			if(edit_table == null || edit_table.length()==0){
				edit_table = "0";
			}else{
				usession.setAttribute("Edit_Table", edit_table);
			}
		}else{
			edit_table = usession.getAttribute("Edit_Table").toString();
		}

		String upload = "";
		if(usession.getAttribute("Upload") == null){
			upload = request.getParameter("Upload");
			if(upload == null || upload.length()==0){
				upload = "";
			}else{
				usession.setAttribute("Upload", upload);
			}
		}else{
			upload = usession.getAttribute("Upload").toString();
		}

		String upload2 = "";
		if(usession.getAttribute("Upload2") == null){
			upload2 = request.getParameter("Upload2");
			if(upload2 == null || upload2.length()==0){
				upload2 = "";
			}else{
				usession.setAttribute("Upload2", upload2);
			}
		}else{
			upload2 = usession.getAttribute("Upload2").toString();
		}

		String where_clause = "";
		if(usession.getAttribute("Where_Clause") == null){
			where_clause = request.getParameter("Where_Clause");
			if(where_clause == null || where_clause.length()==0){
				where_clause = "0";
			}else{
				usession.setAttribute("Where_Clause", where_clause);
			}
		}else{
			where_clause = usession.getAttribute("Where_Clause").toString();
		}

		String up_location = "";
		if(usession.getAttribute("Up_Location") == null){
			up_location = request.getParameter("Up_Location");
			if(up_location == null || where_clause.length()==0){
				up_location = "img/";
			}else{
				usession.setAttribute("Up_Location", up_location);
			}
		}else{
			up_location = usession.getAttribute("Up_Location").toString();
		}

		String up_path = up_location.replaceAll("\\\\\\\\","/");
		%>

		<div>

			<%
			//Check the file
				// Is it an image (.jpg, .bmp, .gif, .pdf?, etc)
				// Size less than 2Mb
				// Does the file exist
			//Check the server space availablitiy
			//Add to item to the cart

			// Initialization
			mySmartUpload.initialize(pageContext);

			mySmartUpload.setMaxFileSize(2000000);

			mySmartUpload.upload();
			%>

			<form name="Continue" action="adminSearch" method="post">
			<input type="hidden" name="body" value="adminSearch"/>
			<div>
				<h3>Upload File (v 2.1)</h3>
				<h4>Info About the File</h4>

				<table>
					<%
					int up_count=0;
					for (int i=0;i<mySmartUpload.getFiles().getCount();i++){
						com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);

						if (!myFile.isMissing()) {

							//Does the directory exist
							String exists = "";
							File cFile = new File("/home/bsto/public_html/bstoproductions.com/cust3/ROOT/"+up_path);
							if(cFile.exists()){
								exists = "Directory Exists";
							}else{
								if(cFile.mkdirs())
									exists = "New Directory";
							}
							myFile.saveAs(up_path+ "/" + myFile.getFileName());
							//myFile.saveAs(up_path+ "/" + myFile.getFileName(), mySmartUpload.SAVE_VIRTUAL);
							if(upload.equals("picture") && i==0)
								con.createStatement().executeUpdate("UPDATE "+edit_table+" SET picture='"+up_location+"/"+myFile.getFileName()+"' WHERE "+where_clause+"");
							else if(upload2.equals("alt_picture") && i==0)
								con.createStatement().executeUpdate("UPDATE "+edit_table+" SET alt_picture='"+up_location+"/"+myFile.getFileName()+"' WHERE "+where_clause+"");
							else if(i==1)
								con.createStatement().executeUpdate("UPDATE "+edit_table+" SET alt_picture='"+up_location+"/"+myFile.getFileName()+"' WHERE "+where_clause+"");
							%>
							<tr><th>File Name (<%=i+1%>):</th><td><%=myFile.getFileName()%></td></tr>
							<tr><th>File Extension (<%=i+1%>):</th><td><%=myFile.getFileExt()%></td></tr>
							<tr><th>File Size (<%=i+1%>):</th><td><%=myFile.getSize()%> Bytes</td></tr>
							<tr><th>File Content Type (<%=i+1%>):</th><td><%=myFile.getContentType()%></td></tr>
							<tr><th>File Path (<%=i+1%>):</th><td><%=up_path%> (<%=exists%>)</td></tr>
							<tr><td>&nbsp;</td><td>&nbsp;</td></tr>

						<%}%>
					<%up_count++;%>
					<%}%>

					<%if(up_count==0){%>
						<tr><td class="error">File could not be uploaded. The file may be corrupt or invalid.</td></tr>
						<tr><td><a href="adminSearch?Edit_Table=<%=edit_table%>&Action=Update&amp;Upload=1"><img src="img/continue.gif"/></a></td></tr>
					<%}else{%>
						<input name="Edit_Table" type="hidden" value="<%=edit_table%>" />
						<input name="Where_Clause" type="hidden" value="<%=where_clause%>" />
						<input name="Upload" type="hidden" value="2" />
						<input name="Action" type="hidden" value="Update" />
						<tr><th><%=up_count%> Total Files Uploaded</th><td></td></tr>
						<tr><th>Click to Continue:</th><td><input type="image" src="img/continue.gif"></td></tr>
					<%}%>
					<%
					usession.removeAttribute("Upload");
					usession.removeAttribute("Upload2");
					%>

				</table>
			</div>
			</form>

		</div>


	<%} catch(Exception e) { %>
		<%=e.getMessage()%>
		<div class="error">File could not be uploaded. The file may be corrupt or invalid.</div>
		<div><a href="adminSearch"><img src="img/continue.gif"/></a></div>
	<%}finally{
		if(con != null)
			con.close();
	}%>


