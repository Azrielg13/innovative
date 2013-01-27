
<%
	Connection con = null;
try {

	con =  DBConnector.get().getConnection();
	//Class.forName("org.gjt.mm.mysql.Driver");
	//con = DriverManager.getConnection(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));
%>

	<div id="left_panel">

		<div>
			<h3> Products </h3>
			<%ResultSet rsMainSide = con.createStatement().executeQuery("SELECT tbl_category.category FROM tbl_category WHERE tbl_category.hide = 0 ORDER BY tbl_category.entry_no");%>
			<ul>
				<%while(rsMainSide.next()){%>
					<li style="font-size:8pt;"><a href="item?Category=<%=rsMainSide.getString(1)%>"><%=rsMainSide.getString(1)%></a></li>
				<%}%>
			</ul>
		</div>

		<br/>

		<div>

			<%Customer cust = (Customer)session.getAttribute("cust");

			if(cust!=null && cust.getCustID()>0){%>

				<div>
					<h3>Welcome, <%=cust.getFName()%></h3>
					&nbsp;<a href="account">My Account</a>
					<br/>&nbsp;<a href="logout">Logout</a>

				<%if(cust.getType().equals("admin")){%>
					<br/><br/>
					<h3>Admin User</h3>
					&nbsp;<a href="admin">Admin Page</a>
				<%}%>

				<%if(cust.getType().equals("merchant")){%>
					<br/><br/>
					<h3>Merchant Account User</h3>
					&nbsp;<a style="color:green;">Merchant Price Discount Active</a>
				<%}%>

				</div>

			<%}else{%>

				<form id="login" action="login" method="post">
						<div>
							<h3>Customer Login</h3>

							&nbsp;<input name="u" type="text" size ="15" maxlength="100"  value="<%=(session.getAttribute("cust")==null?"":session.getAttribute("cust"))%>"/><br/>
							&nbsp;Username<br/>
							&nbsp;<input name="key" type="password" size ="15" maxlength="32"  value=""/>

							<input type="image" src="img/b_go.gif" /><br/>
							&nbsp;Password
							<br/>
							<br/>
							&nbsp;Or&nbsp;<a href="register">Register Now</a>
						</div>
				</form>
			<%}%>

		</div>

		<br/>

		<%Cart cart = (Cart)session.getAttribute("cart");

		if(cart!=null && cart.getItemCount()>0){
			String custType="default";
			if(cust != null)
				custType=cust.getType();%>

			<div>
				<a href="cart"><h3>Your Cart</h3></a>

				&nbsp;<%=cart.getItemCount()%> Item<%=(cart.getItemCount()==1?"":"s")%>
				<br/>&nbsp;Total:&nbsp;<%=java.text.NumberFormat.getCurrencyInstance().format(cart.getGrandTotal(custType))%>

				<%if(cust!=null && cust.getCustID()>0 && cust.getType().equals("merchant")){%>
					<br/>&nbsp;<a style="color:green;">Merchant Discount: <%=java.text.NumberFormat.getCurrencyInstance().format(cart.getGrandTotal("default")-cart.getGrandTotal(custType))%> (<%=java.text.NumberFormat.getPercentInstance().format((cart.getGrandTotal("default")-cart.getGrandTotal(custType))/cart.getGrandTotal("default"))%>)</a>
				<%}%>

			</div>


		<%}%>

		<br/><br/><br/><br/><br/><br/><br/><br/>



	</div> <%//left panel%>
<%} catch(Exception e) { %>
	<%=e.getMessage()%>
<%}finally{
	if(con != null)
		con.close();
}%>
