<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.digitald4.common.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%User user = (User)request.getAttribute("user");
String error = (String) request.getAttribute("error");%>
<article class="container_12">
	<section class="grid_8">
		<%if (error != null) {%>
			<p class="message error no-margin"><%=error%></p>
	   	<%}%>
		<div class="block-content form">
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="first_name" label="First Name" async="true"/>
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="last_name" label="Last Name" async="true" />
			<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="email" label="Email Address" async="true" />
			<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=user%>" prop="type_id" label="Account Type"
					options="<%=GenData.UserType.get(user.getEntityManager()).getGeneralDatas()%>" async="true"/>
			<dd4:input type="<%=InputTag.Type.CHECK%>" object="<%=user%>" prop="disabled" label="Disabled" async="true" />
			<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=user%>" prop="notes" label="Notes" async="true" />
		</div>
		<div class="block-border">
			<%if (error != null) {%>
				<p class="message error no-margin"><%=error%></p>
		   	<%}%>
			<form class="block-content form" id="simple_form" method="POST" action="user">
				<h1>Change Password</h1>
				<input type="hidden" name="id" id="id" value="<%=user.getId()%>"/>
				<label>Password</label>
				<input type="password" name="password" class="full-width"/>
				<label>Confirm Password</label>
				<input type="password" name="passConf" class="full-width"/>
				<button type="submit">Change</button>
			</form>
		</div>
	</section>
</article>