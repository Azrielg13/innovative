<%
Company company = Company.getInstance();
request.setAttribute("company",company);
%>

<title><c:out value='${company.name}'/></title>

<link rel="shortcut icon" href="img/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="css/formatting.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/design.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/print.css" media="print"/>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="title" content="<c:out value='${company.name}'/>" />
<meta name="description" content="" />
<meta name="author" content="BSto Productions" />
<meta name="copyright" content="<c:out value='${company.website}'/>"/>
<meta name="audience" content="all"/>
<meta name="page-topic" content="<c:out value='${company.description}'/>"/>
<meta name="page-type" content="directory"/>
<meta name="description" content=""/>
<meta name="keywords" content="<c:out value='${company.description}'/>"/>
<meta name="robots" content="index, follow" />
<meta name="revisit-after" content="30 days" />
<meta name="expires" content="0" />

<script type="text/javascript" src="scripts/swapImage.js"></script>
<script type="text/javascript" src="scripts/calendar2.js"></script>

<script type="text/javascript" src="scripts/fat.js"></script>
<script type="text/javascript" src="scripts/ajax.js"></script>
<script type="text/javascript" src="scripts/cookie.js"></script>
<script type="text/javascript" src="scripts/ypSlideOutMenusC.js"></script>
<script type="text/javascript" src="scripts/comboBox.js"></script>
<script type="text/javascript" src="scripts/formatNumber.js"></script>
<script type="text/javascript" src="scripts/calendar3.js"></script>

<!--

<script type="text/javascript" src="scripts/floatImage.js"></script>
<script type="text/javascript" src="scripts/floatCursor.js"></script>
//-->

<script type="text/javascript">
	<!--
	function showHideTable(theTable,theImg){
		if (document.getElementById(theTable).style.display == 'none'){
			document.getElementById(theTable).style.display = 'block';
			document.getElementById(theImg).src='img/collapse.gif';
		}else{
			document.getElementById(theTable).style.display = 'none';
			document.getElementById(theImg).src='img/expand.gif';
		}
	}
	//-->
</script>

<script type="text/javascript">
	<!--
	function showTable(theTable,Show){
		if (Show == 'Show'){
			document.getElementById(theTable).style.display = 'block';
		}else{
			document.getElementById(theTable).style.display = 'none';
		}
	}
	//-->
</script>

<script type="text/javascript">
	<!--
	function showHide(theTable){
		if (document.getElementById(theTable).style.display == 'none'){
			document.getElementById(theTable).style.display = 'block';
		}else{
			document.getElementById(theTable).style.display = 'none';
		}
	}
	//-->
</script>

<script type="text/javascript">
	<!--
	function no_error(){
		return true;
	}
	window.onerror=no_error;
	//-->
</script>
