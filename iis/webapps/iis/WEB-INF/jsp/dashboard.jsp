<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.common.dao.*"%>
<%@ page import="com.digitald4.common.component.Column"%>

<article class="container_8">
	<dd4:table title="Pending Intakes" columns="<%=(Collection<Column>)request.getAttribute(\"pintake_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"patients\")%>"/>
	<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"penass_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"appointments\")%>"/>
	<dd4:table title="Pending Assessment" columns="<%=(Collection<Column>)request.getAttribute(\"payable_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"payables\")%>"/>
</article>