
<br/>
<a href="conditions_of_use.jsp" onclick="window.open('conditions_of_use.jsp'); return false">Conditions</a>
| <a href="privacy.jsp" onclick="window.open('privacy.jsp'); return false">Privacy</a>
| <a href="copyright.jsp" onclick="window.open('copyright.jsp'); return false">Copyright</a>
<br/>


Copyright <c:out value='${company.website}'/>, 2006. All rights reserved.
<br/>
All trademarks property of their owners.

<%//Valid <a href="http://validator.w3.org/check/referer">XHTML</a> &amp; <a href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a>.%>
<br/>
<%if(request.getRequestURL().toString().charAt(4)==':' && company.getStatCounterID()!=null && company.getStatCounterID().length()!=0){%>

	<div class="statcounter">
		<a class="statcounter" href="http://www.statcounter.com/">
			<img class="statcounter" src="<%=company.getStatCounterID()%>" alt="website statistics" />
		</a>
	</div>

<%}%>


<%// Search Engine Code %>
<a href="index"><img class="spacer" src="img/spacer.gif" alt=""/></a>
<a href="item"><img class="spacer" src="img/spacer.gif" alt=""/></a>
<a href="category"><img class="spacer" src="img/spacer.gif" alt=""/></a>


<a href="news"><img class="spacer" src="img/spacer.gif" alt=""/></a>
<a href="faq"><img class="spacer" src="img/spacer.gif" alt=""/></a>
<a href="contact"><img class="spacer" src="img/spacer.gif" alt=""/></a>
<a href="testimonial"><img class="spacer" src="img/spacer.gif" alt=""/></a>
<a href="link"><img class="spacer" src="img/spacer.gif" alt=""/></a>
