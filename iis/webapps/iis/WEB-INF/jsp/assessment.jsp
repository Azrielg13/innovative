<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.iis.model.*"%>
<%Appointment appointment = (Appointment)request.getAttribute("appointment");%>
<script src="js/assessment/module.js"></script>
<script src="js/assessment/services.js"></script>
<script src="js/assessment/controllers.js"></script>
<script src="js/assessment/directives.js"></script>
<article class="container_12">
	<dd4:asstab title="Patient Assessment" appointment="<%=appointment%>"/>
</article>
<script>
	function asyncUpdate(comp, classname, id, attribute) {
		document.all.cal_sec.innerHTML = comp.value;
		// Request
		var data = {
			classname: classname,
			id: id,
			attribute: attribute,
			value: comp.value
		};
		var target = "update";
		// Send
		$.ajax({
			url: target,
			dataType: 'json',
			type: 'GET',
			data: data,
			success: function(data, textStatus, XMLHttpRequest) {
				if (data.valid) {
					notify('Change Saved');
				} else {
					notify(data.error || 'An unexpected error occured, please try again');
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				notify('Error while contacting server, please try again');
			}
		});
	}
</script>