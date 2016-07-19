<%@ taglib uri="../tld/iis.tld" prefix="iis"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.common.model.*"%>
<%@ page import="com.digitald4.iis.model.*"%>
<%Appointment appointment = (Appointment) request.getAttribute("appointment");
User user = (User) session.getAttribute("user");%>
<script src="js/multicheck.js"></script>
<article class="container_12">
	<a href="report.pdf?type=ass&id=<%=appointment.getId()%>">PDF Report</a>
	<iis:asstab title="Patient Assessment" appointment="<%=appointment%>" admin="<%=user.isAdmin()%>"/>
</article>
