<%@ taglib uri="../tld/c.tld" prefix="c"%>
<%@ taglib uri="../tld/dd4.tld" prefix="dd4"%>
<%@ page import="com.digitald4.iis.model.*"%>
<% Patient patient = (Patient)request.getAttribute("patient"); %>
<article class="container_12">	
		<section class="grid_8">
			<div class="block-border"><form class="block-content form" id="complex_form" method="post" action="">
				<h1>Patient Assessment</h1>
				
				<div class="block-controls">
					<!-- Top tabs -->
					<ul class="controls-tabs js-tabs">
						<li class="current"><a href="#" title="Charts"><img src="images/icons/web-app/24/Bar-Chart.png" width="24" height="24"></a></li>
						<li><a href="#" title="Comments"><img src="images/icons/web-app/24/Comment.png" width="24" height="24"></a></li>
						<li><a href="#" title="Medias"><img src="images/icons/web-app/24/Picture.png" width="24" height="24"></a></li>
						<li><a href="#" title="Users"><img src="images/icons/web-app/24/Profile.png" width="24" height="24"></a></li>
						<li><a href="#" title="Informations"><img src="images/icons/web-app/24/Info.png" width="24" height="24"></a></li>
					</ul>
					
				</div>
				
<!-- 				<ul class="message warning no-margin">
					<li>This is a <strong>warning message</strong>, inside a box</li>
					<li class="close-bt"></li>
				</ul> -->
				
				<div class="columns">
					<div class="col200pxL-left">
						
						<h2>Status</h2>
						
						<ul class="side-tabs js-tabs same-height">
							<li><a href="#tab-behavioral" title="Behavioral Status">Behavioral Status</a></li>
							<li><a href="#tab-hearing" title="Hearing">Hearing</a></li>
							<li><a href="#tab-cardio" title="Cardiovascular">Cardiovascular</a></li>
							<li><a href="#tab-respiratory" title="Respiratory">Respiratory</a></li>
							<li><a href="#tab-gi" title="G.I.">G.I.</a></li>
							<li><a href="#tab-endocrine" title="Endocrine">Endocrine</a></li>
							<li><a href="#tab-gu" title="GU">GU</a></li>
							<li><a href="#tab-neuromuscular" title="Neuromuscular">Neuromuscular</a></li>
							<li><a href="#tab-integumetary" title="Integumetary">Integumentary</a></li>
							<li><a href="#tab-pain" title="Pain">Pain</a></li>
							<li><a href="#tab-teaching" title="Teaching">Teaching</a></li>
							
						</ul>
						
					</div>
					<div class="col200pxL-right">
						
						<div id="tab-behavioral" class="tabs-content">

									<div class="infos">
										<h3>*NPA</h3>
										<p>No Problem Assesed</p>
									</div>
									Behavorial
														<div class="columns">
						<div class="colx2-left">
							<dd4:input type="TEXT" object="<%=patient%>" prop="rx" label="Behavorial Status" />
						</div>
						<p class="colx2-right">
							<label for="patient.referral_source">Referral Source:</label> <input
							type="text" name="patient.referral_source" id="patient.referral_source" value=""
							class="full-width">
						</p>
					</div>
									<!-- INSERT FORM HERE -->
	
									</div>

					
						
						<div id="tab-hearing" class="tabs-content">
						<div class="infos">
										<h3>*NPA</h3>
										<p>No Problem Assesed</p>
									</div>
						
		

						</div>
						
						<div id="tab-cardio" class="tabs-content">
							Cardio
						</div>
						
						<div id="tab-respiratory" class="tabs-content">
							Respiratory
						</div>
						
						<div id="tab-gi" class="tabs-gi">
							GI
						</div>
						
						<div id="tab-endocrine" class="tabs-endocrine">
							Endocrine
						</div>
						
						<div id="tab-gu" class="tabs-gu">
							gu
						</div>
						
						<div id="tab-neuromuscular" class="tabs-neuromuscular">
							Neuromuscular
						</div>
						
						<div id="tab-integumetary" class="tabs-integumetary">
							Integumetary
						</div>
						
						<div id="tab-pain" class="tabs-pain">
							Pain
						</div>
						
						<div id="tab-teaching" class="tabs-teaching">
							Pain
						</div>
							</div>
					</div>
				
			</form></div>
		</section>
		
	<div class="clear"></div>


	<div class="clear"></div>

</article>