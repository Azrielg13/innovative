<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.digitald4.iis.model.*"%>
<%Appointment appointment = (Appointment)request.getAttribute("appointment");%>
<article class="container_12">
	<div  id="cal_sec"></div>
	<input type="text" id="test" name="test" onchange="asyncUpdate(this, 'com.digitald4.iis.model.Patient', 3, 'name')"/>
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
		var target = "http://192.168.1.19:8080/iis/update";//document.location.href.match(/^([^#]+)/)[1];
		// Send
		$.ajax({
			url: target,
			dataType: 'json',
			type: 'POST',
			data: data,
			success: function(data, textStatus, XMLHttpRequest) {
				if (data.valid) {
					document.all.cal_sec.innerHTML = 'ok';
				} else {
					document.all.cal_sec.innerHTML = data.error || 'An unexpected error occured, please try again';
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				document.all.cal_sec.innerHTML = 'Error while contacting server, please try again';
			}
		});
	}
</script>