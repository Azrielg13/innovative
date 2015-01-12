<%@page import="com.digitald4.common.model.Company"%>
<%@page import="com.digitald4.common.util.FormatText"%>
<%@page import="com.digitald4.iis.model.Appointment"%>
<%@page import="com.digitald4.iis.servlet.AppConfirmationServlet" %>
<%Company company = Company.get();%>
<!doctype html>
	<!--[if lt IE 8 ]><html lang="en" class="no-js ie ie7"><![endif]-->
	<!--[if IE 8 ]><html lang="en" class="no-js ie"><![endif]-->
	<!--[if (gt IE 8)|!(IE)]><!--><html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		
		<title><%=company.getName()%></title>
		<meta name="description" content="">
		<meta name="author" content="">
		
		<!-- Global stylesheets -->
		<link href="css/reset.css" rel="stylesheet" type="text/css">
		<link href="css/common.css" rel="stylesheet" type="text/css">
		<link href="css/form.css" rel="stylesheet" type="text/css">
		<link href="css/standard.css" rel="stylesheet" type="text/css">
		<link href="css/special-pages.css" rel="stylesheet" type="text/css">
		
		<!-- Favicon -->
		<link rel="shortcut icon" type="image/x-icon" href="favicon.ico">
		<link rel="icon" type="image/png" href="favicon-large.png">
		
		<!-- Modernizr for support detection, all javascript libs are moved right above </body> for better performance -->
		<script src="js/libs/modernizr.custom.min.js"></script>
		
		<link href="http://addtocalendar.com/atc/1.5/atc-style-glow-orange.css" rel="stylesheet" type="text/css">
		
	</head>
	
	<!-- the 'special-page' class is only an identifier for scripts -->
	<body class="special-page login-bg dark">
	
		<% String message = (String)request.getAttribute("message");
		if (message != null) {%>
		<section id="message">
			<div class="block-border"><div class="block-content no-title dark-bg">
				<p class="mini-infos"><%=message%></p>
			</div></div>
		</section>
		<%}%>
		
		<section id="login-block">
			<div class="block-border"><div class="block-content">
				
				<!--
				IE7 compatibility: if you want to remove the <h1>,
				add style="zoom:1" to the above .block-content div
				-->
				<h1><%=company.getName()%></h1>
				<div class="block-header">Appointment Confirmation</div>
				
				
			  <% AppConfirmationServlet.ErrorCode error = (AppConfirmationServlet.ErrorCode) request.getAttribute("error");
			  String action = request.getParameter("action");
			  if (error != null) {%>
			   	<p class="message error no-margin">Unable to locate your appointment, either it has been reassigned or cancelled, contact the office for more details</p>
			  <%} else {
			  	  Appointment appointment = (Appointment)request.getAttribute("appointment");%>
			  	  <div class="columns">
			  	  	<div class="colx2-left">
			  	  		<p><%=FormatText.formatDate(appointment.getStart(), FormatText.USER_DATETIME)%></p>
			  	  		<p><%=appointment.getPatient()%></p>
			  	  	</div>
			  	  	<div class="colx2-right">
			  	  		<%=appointment.getPatient().getServiceAddress()%>
			  	  	</div>
			  	  </div>
			  	  <hr>
				  <%if ("confirm".equals(action)){%>
				  	<script type="text/javascript">(function () {
			            if (window.addtocalendar)if(typeof window.addtocalendar.start == "function")return;
			            if (window.ifaddtocalendar == undefined) { window.ifaddtocalendar = 1;
			                var d = document, s = d.createElement('script'), g = 'getElementsByTagName';
			                s.type = 'text/javascript';s.charset = 'UTF-8';s.async = true;
			                s.src = ('https:' == window.location.protocol ? 'https' : 'http')+'://addtocalendar.com/atc/1.5/atc.min.js';
			                var h = d[g]('body')[0];h.appendChild(s); }})();
    				</script>
				  	<p>Thank you, you are confirmed</p>
				  	<span class="addtocalendar atc-style-glow-orange">
				        <var class="atc_event">
				            <var class="atc_date_start"><%=FormatText.formatDate(appointment.getStart(), FormatText.MYSQL_DATETIME)%></var>
				            <var class="atc_date_end"><%=FormatText.formatDate(appointment.getEnd(), FormatText.MYSQL_DATETIME)%></var>
				            <var class="atc_timezone">America/Los_Angeles</var>
				            <var class="atc_title">IIS - <%=appointment.getPatient()%></var>
				            <var class="atc_description">Infusion appointment with <%=appointment.getPatient()%></var>
				            <var class="atc_location"><%=appointment.getPatient().getServiceAddress()%></var>
				            <var class="atc_organizer">Innovation Infusion Solutions, Inc</var>
				            <var class="atc_organizer_email">nicole@innovativeinfusionsolutions.com</var>
				        </var>
				    </span>
				  <%} else if ("decline".equals(action)){%>
				  	The office has been notified you can not make this appointment.
				  <%} else {%>
				  	<div class="columns">
				  		<div class="colx2-left">
			  				<a href="app_confirm?action=confirm&obf_id=<%=request.getParameter("obf_id")%>&nurse_oid=<%=request.getParameter("nurse_oid")%>">
			  					<button>Confirm</button>
			  				</a>
			  			</div>
			  			<div class="colx2-right">
			  				<form action="app_confirm?action=decline&obf_id=<%=request.getParameter("obf_id")%>&nurse_oid=<%=request.getParameter("nurse_oid")%>" method="POST">
			  					<label>Reason</label>
			  					<textarea name="confirm_notes" rows="5" cols="25"></textarea>
			  					<button type="submit">Decline</button>
			  				</form>
						</div>
					</div>
				  <%}%>
			  <%}%>
			</div></div>
		</section>
		
		<!--
		
		Updated as v1.5:
		Libs are moved here to improve performance
		
		-->
		
		<!-- Generic libs -->
		<script src="js/libs/jquery-1.10.2.min.js"></script>
		
		<!-- Template libs -->
		<script src="js/common.js"></script>
		<script src="js/standard.js"></script>
		<script src="js/jquery.tip.js"></script>
		
	</body>
</html>
