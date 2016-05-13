<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="com.digitald4.common.component.Column"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="java.util.Collection"%>

<article class="container_12">
	<dd4:table title="Unpaid Invoices" columns="<%=(Collection<Column>)request.getAttribute(\"invoicecols\")%>" data="<%=Invoice.getUnpaidInvoices()%>"/>
</article>
