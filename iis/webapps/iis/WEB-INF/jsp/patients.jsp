<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.common.dao.*"%>
<%@ page import="com.digitald4.common.component.Column"%>
<%@ page import="com.digitald4.iis.model.*"%>

<article class="container_12">
	<dd4:table title="Patients" columns="<%=(Collection<Column>)request.getAttribute(\"patients_cols\")%>" data="<%=(Collection<? extends DataAccessObject>)request.getAttribute(\"patients\")%>"/>
</article>