<%@ taglib uri="../tld/c.tld" prefix="c"%>
<%@page import="com.digitald4.util.*" %>
<%@page import="com.digitald4.pm.*" %>
<%@page import="com.digitald4.pm.servlet.*" %>
<%@ page import="java.sql.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<title><c:out value='${company.name}'/> - Admin</title>
		<%@ include file="/WEB-INF/jsp/include/head.jsp" %>
	</head>

	<body style="background:#fff">
		<div>
			<div>

				<c:import url="${adminBody}" />

			</div>
		</div> <%//container%>
	</body>
</html>

