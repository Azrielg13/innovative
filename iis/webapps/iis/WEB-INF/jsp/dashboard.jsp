<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.common.dao.*"%>
<%@ page import="com.digitald4.common.component.Column"%>
<%@ page import="com.digitald4.iis.model.*"%>
<%
	int year = (Integer)request.getAttribute("year");
	int month = (Integer)request.getAttribute("month");
%>
<script src="js/large-cal.js"></script>
<article class="container_12">
	<div class="columns">
		<div class="colx2-left">
			<dd4:table title="Pending Intakes" columns="<%=(Collection<Column>)request.getAttribute(\"pintake_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"patients\")%>"/>
		</div>
		<div class="colx2-left">
			<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"penass_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"penass_data\")%>"/>
		</div>
	</div>
	<div class="columns">
		<div class="colx2-left">
			<dd4:table title="Awaiting Review" columns="<%=(Collection<Column>)request.getAttribute(\"reviewable_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"reviewables\")%>"/>
		</div>
		<div class="colx2-left">
			<dd4:table title="License Expiration" columns="<%=(Collection<Column>)request.getAttribute(\"alarming_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"alarming\")%>"/>
		</div>
	</div>
</article>
<article class="container_12">
	<div id="cal_sec" class="grid_12">
		<%=request.getAttribute("calendar")%>
	</div>
</article>
<article class="container_12">
	<div class="columns">
		<div class="colx2-left">
			<dd4:table title="Payable" columns="<%=(Collection<Column>)request.getAttribute(\"payable_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"payables\")%>"/>
		</div>
		<div class="colx2-left">
			<dd4:table title="Billable" columns="<%=(Collection<Column>)request.getAttribute(\"billable_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"billables\")%>"/>
		</div>
	</div>
</article>

