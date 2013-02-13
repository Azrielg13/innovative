<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.common.tld.*" %>
<%@ page import="com.digitald4.common.component.Column"%>

<%Patient patient = (Patient)request.getAttribute("patient"); %>
<article class="container_12">
	<section class="grid_4">
		<dd4:midcal year="2013" month="02" title="Patient Calendar"/>
	</section>
	<section class="grid_4">
		<form class="block-content form" id="simple_form" method="post" action="patient">
			<input type="hidden" name="id" id="id" value="<%=patient.getId()%>"/>
			<dd4:input type="<%=InputTag.Type.COMBO%>" object="<%=patient%>" prop="referral_resolution_id" label="Patient State" options="<%=GenData.PATIENT_STATE.get().getGeneralDatas()%>"/>
			<button type="submit">Save</button>
		</form>
	</section>
</article>