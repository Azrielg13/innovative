<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@page import="com.sce.mdi.*" %>
<%@page import="java.io.*"%>
<%@page import="java.lang.Math.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>

<%Connection conU = null;
try {

	conU =  DBConnector.getInstance().getConnection();

	User user = (User)session.getAttribute("user");

	%>
	<div id="white" style="text-align:left;padding:0px;margin:0px;background-image:url(img/TopPane1.jpg);width:278px;height:145px;">
		<div style="width:278px;height:145px;margin:2px 0px 0px 5px;">
				<%
				String whereClause = "";
				if(user!=null && user.getId() > 0){

					if(user.getType()==User.AGENT){
						whereClause = "AND aUserId="+user.getId();
					}else if(user.getType()==User.EXAMINER){
						whereClause = "AND eUserId="+user.getId();
					}%>
						<div style="font-size:120%;font-weight:bold;">Insurance Co by City (Active Clients)</div>

						<%ResultSet rsCity = conU.createStatement().executeQuery("SELECT COUNT(tbl_client.clientId) AS clientCount, tbl_client.city2 FROM tbl_client WHERE tbl_client.hide=0 "+whereClause+" GROUP BY tbl_client.city2 ORDER BY tbl_client.city2");
						while(rsCity.next()){%>
							&nbsp;<%=rsCity.getString("city2")+" ("+rsCity.getInt(1)+")"%><br/>
							<%ResultSet rsHood = conU.createStatement().executeQuery("SELECT COUNT(tbl_client.clientId), tbl_insurance_co.insuranceId, tbl_insurance_co.name FROM tbl_insurance_co LEFT JOIN tbl_client USING(insuranceId) WHERE tbl_insurance_co.hide=0 AND tbl_client.hide=0 AND tbl_client.city2='"+rsCity.getString("city2")+"' "+whereClause+" GROUP BY tbl_insurance_co.name ORDER BY tbl_insurance_co.name");
							while(rsHood.next()){%>
								&nbsp;&nbsp;->&nbsp;<a href="search?SearchData=<%=rsHood.getInt("insuranceId")%>" target="_parent"><%=rsHood.getString("name")+" ("+rsHood.getInt(1)+")"%></a><br/>
							<%}%>
							<br/>
						<%}%>

				<%}else{%>
					<a href="login" target="_parent">Not Logged In</a>

				<%}%>

		</div>
	</div>

<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(conU != null)
		conU.close();
}%>