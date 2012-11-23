<%@page import="com.digitald4.order.*" %>
<%@page import="java.text.*" %>
<%@taglib uri="WEB-INF/tld/dd4.tld" prefix="dd4"%>
<%@taglib uri="WEB-INF/tld/c.tld" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<%
	Company company = Company.getCompany(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));
	request.setAttribute("company",company);
	%>

	<body>
		<div id="legal" style="text-align: left;">
		<b><c:out value="${company.name}"/> CONDITIONS OF USE AGREEMENT</b><br/><br/>

		<b>General </b><br/>

		This Agreement governs your membership in the <c:out value="${company.name}"/> community, allowing you full access to the <c:out value="${company.name}"/> web site (the "Site") and to upload and download (subject to having the appropriate number of credits) photographs, illustrations, audio and video files, code snippets and other Material ("Data Files") therefrom. The Site is operated by <c:out value="${company.name}"/> ("<c:out value="${company.website}"/>"). <br/><br/>

		<c:out value="${company.name}"/> reserves the right, in its discretion, to change or modify all or any part of this Agreement at any time, effective immediately upon notice published on the Site. Your continued use of the membership constitutes your binding acceptance of these terms and conditions in this Agreement, including any changes or modifications made by <c:out value="${company.name}"/> as permitted above. If at any time the terms and conditions of this Agreement are no longer acceptable to you, you should immediately cease use of the membership and the Site. <br/><br/>

		<b>Use of Content </b><br/>

		You acknowledge that the Site contains or may contain information, software, Data Files and other material (collectively the "Content") which is protected by copyright, trademark or other proprietary rights of <c:out value="${company.name}"/> or other third parties (including other members). <br/><br/>

		You may not modify, publish, transmit, transfer or sell, reproduce, create derivative works from, distribute, perform, display, or in any way exploit any of the Content, in whole or in part, except as otherwise expressly permitted in the this Agreement and the Royalty Free Licence Agreement. Content consisting of downloadable software may not be reverse engineered unless specifically authorized by the owner of the software's patent and or copyright. Subject to the provisions of the Upload Agreement, you may post on the Site any Data Files owned wholly by you. <br/><br/>



		You may download Data Files only in accordance with the terms of the Royalty Free Licence Agreement. <br/><br/>

		<b>Rules of Conduct </b><br/>

		You shall not post on the Site any Data File which: (i) is libelous, defamatory, obscene, pornographic, abusive, harassing or threatening; (ii) contains viruses or other contaminating or destructive features; (iii) violates the rights of others, including but not limited to Data Files which infringe any copyright, trademark, patent, trade secret or violate any right of privacy or publicity; or (iv) otherwise violates any applicable law. <br/><br/>

		<b>Managing Data Files </b><br/>

		<c:out value="${company.name}"/> does not and cannot review all Data Files uploaded by Members on the Site and is not responsible for the content of such Data File. Notwithstanding the foregoing, <c:out value="${company.name}"/> reserves the right to delete, move or edit any Data File that it may determine, in its sole discretion, violates or may violate this Agreement or is otherwise unacceptable. You shall remain solely responsible for all Data Files uploaded under your Member's name. <c:out value="${company.name}"/> shall have the right but not the obligation, to correct any errors or omissions in any Data Files, as it may determine in its sole discretion. <br/><br/>


		<b>No Endorsement </b><br/>

		<c:out value="${company.name}"/> does not represent the reliability of any Data File which you may download and you acknowledge that any reliance upon such Data File shall be at your sole risk. <br/><br/>

		The Site may contain links to sites on the Internet which are owned and operated by third parties (the "External Sites"). You acknowledge that <c:out value="${company.name}"/> is not responsible for the availability of, or the content located on or through any External Site. You should contact the site administrator or webmaster for those External Sites if you have any concerns regarding such links or the content located on such External Sites. <br/><br/>

		<b>Indemnity </b><br/>

		You agree to indemnify, defend and hold <c:out value="${company.name}"/> and its affiliates, and their respective officers, directors, owners, agents, information providers and licensors (collectively, the "<c:out value="${company.name}"/> Parties") harmless from and against any and all claims, liability, losses, costs and expenses (including attorneys' fees) incurred by any <c:out value="${company.name}"/> Party in connection with: (i) any use or alleged use of the <c:out value="${company.name}"/> web site under your member name by any person, whether or not authorized by you; (ii) or resulting from any Data File uploaded under your member name; or (iii) any breach of the your warranties which are contained in this Agreement. <c:out value="${company.name}"/> reserves the right, at your expense, to assume the exclusive defense and control of any matter otherwise subject to indemnification by you, and in such case, you agree to cooperate with <c:out value="${company.name}"/>'s defense of such claim. <br/><br/>

		<b>Termination of Service </b><br/>

		<c:out value="${company.name}"/> reserves the right, in its sole discretion, to restrict, suspend or terminate your Membership at any time for any reason without prior notice or liability. <c:out value="${company.name}"/> may change, suspend or discontinue all or any aspect of the Site at any time, including the availability of any feature, database, or Content, without prior notice or liability. <br/><br/>

		<b>DISCLAIMER OF WARRANTIES AND LIMITATION OF LIABILITY </b><br/>

		NEITHER <c:out value="${company.name}"/> NOR ANY PROVIDER OF DATA FILES OR THEIR RESPECTIVE AGENTS WARRANT THAT THE SITE WILL BE UNINTERRUPTED OR ERROR FREE; NOR DOES <c:out value="${company.name}"/>, ANY THRID PARTY PROVIDER OF DATA FILES OR THEIR RESPECTIVE AGENTS MAKE ANY WARRANTY AS TO THE RESULTS TO BE OBTAINED FROM USE OF THE SITE OR THE DATA FILES. THE SITE AND THE DATA FILES ARE DISTRIBUTED ON AN "AS IS, WHERE IS, AS AVAILABLE" BASIS. NEITHER <c:out value="${company.name}"/>, THIRD PARTY DATA FILE PROVIDERS, NOR THEIR RESPECTIVE AGENTS MAKE ANY WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING WITHOUT LIMITATION, WARRANTIES OF TITLE OR IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE, WITH RESPECT TO THE SITE OR ANY DATA FILES DOWNLOADED THROUGH THE SITE. NEITHER <c:out value="${company.name}"/> NOR ANY THIRD PARTY DATA FILE PROVIDER WARRANTS THAT ANY DATA FILES AVAILABLE FOR DOWNLOADING THROUGH THE SITE WILL BE FREE OF VIRUSES OR SIMILAR CONTAMINATION OR DESTRUCTIVE FEATURES. YOU EXPRESSLY AGREE THAT THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE SITE AND ANY DATA FILES DOWNLOADED THEREFROM BY YOU IS ASSUMED SOLELY BY YOU. <br/><br/>

		NEITHER <c:out value="${company.name}"/>, ANY THIRD PARTY DATA FILE PROVIDER NOR THEIR AGENTS SHALL BE LIABLE FOR ANY ACT, DIRECT OR INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OF OR INABILITY TO USE THE SITE, EVEN IF SUCH PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. <br/><br/>

		SOME JURISDICTIONS DO NOT ALLOW EXCLUSION OF IMPLIED WARRANTIES OR LIMITATION OF LIABILITY FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE ABOVE LIMITATIONS OR EXCLUSIONS MAY NOT APPLY TO YOU. IN SUCH JURISDICTIONS, THE LIABILITY OF <c:out value="${company.name}"/>, PROVIDERS OF DATA FILES AND THEIR RESPECTIVE AGENTS SHALL BE LIMITED TO THE GREATEST EXTENT PERMITTED BY LAW. <br/><br/>

		<b>Miscellaneous </b><br/>

		You specifically agree and acknowledge that you have, in addition to the terms of this Agreement, reviewed the terms of the Royalty Free Licence Agreement, the Upload Agreement and any other agreements which may be incorporated by reference therein. You further acknowledge that you agree to be bound by the terms of such agreements. <br/><br/>

		This Agreement will be governed under the laws of the state of California and the federal laws of the United States of America applicable therein. Each of the parties hereto do expressly submit and attorn to the jurisdiction at the Courts. If any provision of this Agreement is held invalid, the remainder of this Agreement shall continue in full force and effect. <br/>
		<br/><br/>
		2005 <c:out value="${company.name}"/> All Rights Reserved.<br/><br/>

		</div>

	</body>
</html>