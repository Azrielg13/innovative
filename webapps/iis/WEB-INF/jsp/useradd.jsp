<%@ taglib uri="../tld/dd4.tld" prefix="dd4" %>
<%@ page import="com.digitald4.common.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<% User user = (User) request.getAttribute("user");%>
<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<% String error = (String) request.getAttribute("error");
				if (error != null) {%>
					<p class="message error no-margin"><%=error%></p>
		   	<%}%>
			<form class="block-content form" id="simple_form" method="post" action="useradd">
				<h1>User Registration</h1>
				<fieldset class="white-bg required">
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="first_name" label="First Name" />
						</div>
						<div class="colx3-center">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="last_name" label="Last Name" />
						</div>
						<div class="colx3-right">
							<dd4:input type="<%=InputTag.Type.TEXT%>" object="<%=user%>" prop="email" label="Email Address" />
						</div>
					</div>
					<div class="columns">
						<div class="colx3-left">
							<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=user%>" prop="type_id" label="Account Type"
									options="<%=GenData.UserType.get(user.getEntityManager()).getGeneralDatas()%>"/>
						</div>
						<p class="colx3-center">
							<label>Password</label>
							<input type="password" name="password" class="full-width"/>
						</p>
						<p class="colx3-right">
							<label>Confirm Password</label>
							<input type="password" name="passConf" class="full-width"/>
						</p>
					</div>
					<dd4:input type="<%=InputTag.Type.TEXTAREA%>" object="<%=user%>" prop="notes" label="Notes" />
				</fieldset>

				<fieldset class="grey-bg no-margin">
					<button style="float:right" type="submit">Submit</button>
				</fieldset>

			</form>
		</div>
	</section>
	<div class="clear"></div>
	<div class="clear"></div>
</article>
