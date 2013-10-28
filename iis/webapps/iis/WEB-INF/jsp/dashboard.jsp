<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.common.dao.*"%>
<%@ page import="com.digitald4.common.component.Column"%>
<%@ page import="com.digitald4.iis.model.*"%>
<%
	int year = (Integer)request.getAttribute("year");
	int month = (Integer)request.getAttribute("month");
	Collection<Appointment> appointments = Appointment.getAllActive();
%>
<article class="container_8">
	<dd4:table title="Pending Intakes" columns="<%=(Collection<Column>)request.getAttribute(\"pintake_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"patients\")%>"/>
	<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"penass_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"penass_data\")%>"/>
	<dd4:table title="Awaiting Review" columns="<%=(Collection<Column>)request.getAttribute(\"reviewable_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"reviewables\")%>"/>
	<dd4:table title="Payable" columns="<%=(Collection<Column>)request.getAttribute(\"payable_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"payables\")%>"/>
</article>
<article class="container_12">
	<div id="cal_sec" class="grid_8">
		<%=request.getAttribute("calendar")%>
	</div>
</article>
