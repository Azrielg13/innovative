<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.iis.model.*"%>
<%Appointment appointment = (Appointment)request.getAttribute("appointment");%>
<article class="container_12">
	<dd4:asstab title="Patient Assessment" appointment="<%=appointment%>"/>
</article>