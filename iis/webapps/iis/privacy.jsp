<%@page import="com.digitald4.util.*" %>
<%@page import="java.text.*" %>
<%@taglib uri="WEB-INF/tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="WEB-INF/tld/c.tld" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<%
	Company company = Company.getCompany();
	request.setAttribute("company",company);
	%>

	<body>
		<div id="legal" style="text-align: left;">
		<b><c:out value="${company.name}"/> Privacy Policy</b><br/><br/>
		<c:out value="${company.name}"/>  and its divisions, affiliates and subsidiaries respects the privacy and the integrity of any information that you provide us as a user of this Site. Under the Personal Information Protection and Electronic Documents Act, 2000 c.5. (the &quot;Privacy Act&quot;) we have created the following Privacy Policy. <br/>
		<br/>
		<b>1. APPLICATION OF THIS POLICY <br/>
		</b><br/>
		This Privacy Policy applies to our iStock only. It will tell you how and why we gather personal and non-personal information about you. <br/>
		<br/>
		This Site is connected by &quot;hyperlinks&quot; to other websites. We are not responsible in any way for the privacy practices on other websites and suggest that you review the privacy policies on those linked websites before using them. <br/>
		<br/>
		<b>2. WE ARE ACCOUNTABLE TO YOU <br/>
		</b><br/>
		We have designated an officer of the company to be accountable for our policies and practices with respect to the management of personal information. Any questions, comments or complaints that you might have about our privacy practices should be forwarded in writing to: <br/>
		<br/>
		<c:out value="${company.email}"/>
		<br/>
		<br/>
		<b>3. COLLECTION OF INFORMATION <br/>
		</b><br/>
		Personally identifiable information or personal information is defined as information that identifies you, as an individual, and includes your name, home address, home telephone number, and e-mail address. <br/>
		<br/>
		We only collect personally identifiable information about you after you have specifically and knowingly provided such information to us. By voluntarily providing this information to us at various points when using our Site, including, sending us a comment or suggestion, entering a chatroom or bulletin board, or entering a contest that is posted on our Site, you will be consenting to the collection and use of your personal information by us for that online activity. You are under no obligation to provide us with this information and can access many aspects of the Site without providing us any personal information. <br/>
		<br/>
		<b>4. THE PURPOSE OF OUR COLLECTING INFORMATION FROM YOU <br/>
		</b><br/>
		If you participate in an online activity, your personal information will only be used for the purposes of that online activity, unless you otherwise provide us with your consent.  For example, we will collect and use personal information for the following purposes: <br/>
		<br/>
		&#149;        To run contests, select entrants, and choose prize winners. <br/>
		&#149;        To respond to your questions, or comments you forward to us. <br/>
		&#149;        To run chatrooms, bulletin boards or other areas of interaction with other members.<br/>
		&#149;        To keep aggregate information on Site use. <br/>
		<br/>
		<b>5.        DISCLOSURE OF YOUR PERSONAL INFORMATION <br/>
		</b><br/>
		We will not sell or rent your personal information to any other party without your consent.  However, we may disclose your personal information in the circumstances set out below. <br/>
		<br/>
		If you have entered into and won one of our contests, your name and city of residence may be published by us or one of the sponsors.  Please check the specific contest rules for details. <br/>
		<br/>
		We may also be required by law to disclose personal information without your consent in the event of emergency situations or when required by government or other legal authority. <br/>
		<br/>
		We may also disclose personal information to third parties in connection with a corporate re-organization, merger or amalgamation, or the sale of all or substantially all of our assets, provided that the personal information disclosed continues to be used for the purposes permitted by this Privacy Policy by the entity acquiring this information. <br/>
		<br/>
		Please be advised that when you voluntarily disclose information on the bulletin boards or in the forum or chat areas of our Site, your personal and other information disclosed in your communication shall become public information and can be collected and used by other parties.  We cannot control what third parties in the bulletin board or chatroom do with your personal information. <br/>
		<br/>
		<b>6.        NON PERSONAL INFORMATION <br/>
		</b><br/>
		In addition, we collect non-personally identifiable information, which is information that does not identify you personally, but becomes available to us as a result of your visit to our Site. This information includes; your IP address, a number automatically assigned your computer when you visit the Site; your Internet Service Provider; and your Web Browser. <br/>
		<br/>
		One way we use this non-personal information is as a diagnostic and administrative tool for identifying system problems and administering the Site. From time to time we use cookies on this Site. Cookies are small text files that your web browser leaves on your hard drive to recognize you as a repeat user of our Site. If you do not want cookies to be used, you should access the features of your web browser for the procedure to disable the cookies. <br/>
		<br/>
		We collect personal and non-personal information in a non-identifying, anonymous and aggregate form to enhance our Site design, to share with advertisers and for statistical purposes.  These statistics and reports will contain only aggregated and no personally identifiable information. <br/>
		<br/>
		<b>7.        SAFEGUARDS WE HAVE IN PLACE <br/>
		</b><br/>
		We employ reasonable managerial and technical measures to ensure that your information is secure.  For example, all personally identifiable information is maintained behind a firewall. <br/>
		<br/>
		<b>8.        CHILDREN <br/>
		</b><br/>
		Minors in the country, province or state in which they live, can only use this Site with the approval of a parent or guardian. <br/>
		<br/>
		<b>9.        CHANGES TO THIS PRIVACY POLICY <br/>
		</b><br/>
		We may change this policy as new services are added or old ones are changed.  Such changes will be effective when a notice of the change is posted on the Site.  Please check this Privacy Policy from time to time for updates by checking the date of the &#147;Last Update&#148; at the top of this document.<br/>
			<br/>

		</div>

	</body>

</html>