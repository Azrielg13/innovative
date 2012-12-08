<%

	String search_data2 = request.getParameter("SearchData");
	if(search_data2 == null || search_data2.length() == 0)
		search_data2="";

	search_data2 = search_data2.replaceAll("'","");

	User user = (User)session.getAttribute("user");
	%>

	<div id="header">
		<form id="search" action="search" method="post">
			<table cellspacing="0px" cellpadding="0px">
				<tr>
					<td rowspan="3">
						<img src="img/LogoPicTan.gif" width="194" height="197" alt=""></td>
					<td>
						<img src="img/Cardigan_02.jpg" width="2" height="42" alt=""></td>
					<td>
						<a href="home"><img src="img/YourSite.jpg" width="173" height="42" alt=""></a></td>
					<td>
						<img src="img/Cardigan_04.jpg" width="2" height="42" alt=""></td>
					<td valign="center" align="center" style="background-image:url(img/Search.jpg);width:300px;height:42px;"><input style="margin:0px 0px 3px 0px;" name="SearchData" type="text" size ="30" maxlength="50" value="<%=search_data2%>"/>&nbsp;<input style="margin:13px 0px 0px 0px;" type="image" src="img/SearchPic.gif" /></td>
					<td>
						<img src="img/Cardigan_06.jpg" width="1" height="42" alt=""></td>
					<td rowspan="2">
						<a href="account"><img src="img/menu.gif" width="83" height="52" alt=""></a></td>
					<td rowspan="2">
						<a href="contact"><img src="img/menu2.gif" width="45" height="52" alt=""></td>
				</tr>
				<tr>
					<td rowspan="2">
						<img src="img/Cardigan_10.jpg" width="2" height="155" alt=""></td>
					<td rowspan="2" valign="top" style="background-image:url(img/Menu.jpg);width:173px;height:155px;">
						<div class="menu"><a href="#">Testimonials</a></div>
						<div class="menu"><a href="#">Reports</a></div>
						<div class="menu"><a href="faq">FAQ</a></div>
						<div class="menu"><a href="news">News</a></div>
						<div class="menu" style="border-bottom:0px"><a href="link">Links</a></div></td>
					<td rowspan="2">
						<img src="img/Cardigan_12.jpg" width="2" height="155" alt=""></td>
					<td>
						<img src="img/Cardigan_13.jpg" width="300" height="10" alt=""></td>
					<td>
						<img src="img/Cardigan_14.jpg" width="1" height="10" alt=""></td>
				</tr>
				<tr>
					<td valign="top" style="background-image:url(img/TopPane1.jpg);width:300px;height:145px;">
						<iframe name="neighborhood" width="300px" height="145px" src="iframe" frameborder="no" scrolling="yes">
						Sorry, you need inline frames to fully see this page.
						</iframe>
					</td>
					<td>
						<img src="img/Cardigan_16.jpg" width="1" height="145" alt=""></td>
					<td colspan="3" valign="top" style="background-image:url(img/TopRightPane.jpg);width:128px;height:145px;">
						<div id="white" style="margin:2px 0px 0px 2px;">
							<div style="font-size:120%;font-weight:bold;">Quicklinks</div>

							<%if(user!=null && user.getId() != null){%>

							<%}else{%>
								<a href="login">Not Logged In</a>
							<%}%>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<img src="img/Date.jpg" width="194" height="21" alt=""></td>
					<td>
						<img src="img/Cardigan_19.jpg" width="2" height="21" alt=""></td>
					<td>
						<img src="img/Cardigan_20.jpg" width="173" height="21" alt=""></td>
					<td>
						<img src="img/Cardigan_21.jpg" width="2" height="21" alt=""></td>
					<td valign="center" colspan="5" style="background-image:url(img/Breadcrumb.jpg);width:429px;height:21px;">
						<div id="allwhite">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<%if(user!=null && user.getId() != null){%>
								Welcome,&nbsp;<%=user.getFirstName()%>&nbsp;<%=user.getLastName()%>
								&nbsp;<a href="logout">[Logout]</a>
							<%}else{%>
								<a href="login">Not Logged In</a>
							<%}%>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div> <%//header%>