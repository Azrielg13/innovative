<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="com.digitald4.common.component.Column"%>
<%@ page import="com.digitald4.common.util.FormatText"%>
<%@ page import="com.digitald4.iis.model.*" %>
<%@ page import="com.digitald4.iis.report.PayrollReport"%>
<%@ page import="java.util.Collection"%>
<%@ page import="org.joda.time.DateTime"%>

<article class="container_12">
	<section class="grid_8">
		<div class="block-border">
			<div class="error-block"></div>
			<form class="block-content form" id="simple_form" method="get" action="create_payroll_report" target="_blank">
				<h1>Payroll Reports</h1>
				<div class="columns">
					<div class="colx2-left">
						<label>End Date</label>
						<input type="text" name="end_date" id="dOB" value="<%=FormatText.formatDate(DateTime.now())%>" class="weekpicker">
						<img src="images/icons/fugue/calendar-month.png" width="16" height="16">
					</div>
					<div class="colx2-right">
						<button type="submit" name="type" value="<%=PayrollReport.REPORT_TYPE.WEEKLY%>">Generate Weekly Report</button>
					</div>
				</div>
				<div class="columns">
					<div class="colx2-left">
						<label>Year</label>
						<input id="spinner" name="year" class="ui-spinner-input" value="<%=DateTime.now().getYear()%>" autocomplete="off" role="spinbutton" aria-valuenow="5">
					</div>
					<div class="colx2-right">
						<button type="submit" name="type" value="<%=PayrollReport.REPORT_TYPE.YEARLY%>">Generate Yearly Report</button>
					</div>
				</div>
				<div class="columns">
					<div class="colx2-left">
						<label>Month</label>
						<select name="month">
							<%int month = DateTime.now().getMonthOfYear();%>
							<option value="1" <%=month == 1 ? "selected" : ""%>>January</option>
							<option value="2" <%=month == 2 ? "selected" : ""%>>February</option>
							<option value="3" <%=month == 3 ? "selected" : ""%>>March</option>
							<option value="4" <%=month == 4 ? "selected" : ""%>>April</option>
							<option value="5" <%=month == 5 ? "selected" : ""%>>May</option>
							<option value="6" <%=month == 6 ? "selected" : ""%>>June</option>
							<option value="7" <%=month == 7 ? "selected" : ""%>>July</option>
							<option value="8" <%=month == 8 ? "selected" : ""%>>August</option>
							<option value="9" <%=month == 9 ? "selected" : ""%>>September</option>
							<option value="10" <%=month == 10 ? "selected" : ""%>>October</option>
							<option value="11" <%=month == 11 ? "selected" : ""%>>November</option>
							<option value="12" <%=month == 12 ? "selected" : ""%>>December</option>
						</select>
					</div>
					<div class="colx2-right">
						<button type="submit" name="type" value="<%=PayrollReport.REPORT_TYPE.MONTHLY%>">Generate Monthly Report</button>
					</div>
				</div>
			</form>
		</div>
	</section>
</article>
<script>
  $(function() {
    var spinner = $( "#spinner" ).spinner();
 
    $( "#disable" ).click(function() {
      if ( spinner.spinner( "option", "disabled" ) ) {
        spinner.spinner( "enable" );
      } else {
        spinner.spinner( "disable" );
      }
    });
    $( "#destroy" ).click(function() {
      if ( spinner.spinner( "instance" ) ) {
        spinner.spinner( "destroy" );
      } else {
        spinner.spinner();
      }
    });
    $( "#getvalue" ).click(function() {
      alert( spinner.spinner( "value" ) );
    });
    $( "#setvalue" ).click(function() {
      spinner.spinner( "value", 5 );
    });
 
    $( "button" ).button();
  });
</script>
