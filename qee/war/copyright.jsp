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
		<b><c:out value="${company.name}"/> Copyright</b><br/><br/>
		1. ESSENTIAL INFORMATION PLEASE READ THE FOLLOWING TERMS AND CONDITIONS. YOUR ACCEPTANCE OF THESE TERMS IS AN ABSOLUTE CONDITION OF YOUR USE OF THIS PRODUCT.<br/><br/>

		2. LICENSE<br/><br/>

		(a) Application:<br/>
		This agreement applies to any file on <c:out value="${company.website}"/> whether a photograph, illustration, video or audio file or code snipett ("Data File") which is marked as a Royalty Free Data File.  Any other Data File shall be subject to the Standard Licence Agreement.<br/><br/>

		(b) Geographic limits:  <br/>
		This License Agreement governs your use of <c:out value="${company.name}"/> Royalty Free Data Files ("the Data File(s)"), provided by <c:out value="${company.name}"/> ("<c:out value="${company.website}"/>").  <c:out value="${company.name}"/> licenses to you the use of the Data File(s) only as provided below on a non-exclusive and non-transferable basis.  All other rights to the Data File(s) and accompanying materials (if applicable), including without limitation, copyright and all other rights, are retained by <c:out value="${company.name}"/> and/or its members.<br/><br/>

		(c) Number of Users:<br/>
		THIS IS A ONE (1) PERSON LICENSE AGREEMENT ONLY.  You are permitted to use the Data File.  Any additional persons who wish to use the Data File must download it from <c:out value="${company.name}"/>.<br/><br/>

		(d) Permitted Uses. You may:<br/><br/>

		(i) Back up, copy, or archive one copy of the Data File(s) as necessary for internal use, and only as necessary for that use. Any copy or archive you make must include the Data File's copyright information.<br/><br/>

		(ii) Use the Data File(s) in any electronic or print media, including advertising and editorial use, and consumer merchandise.<br/><br/>

		(iii) Use the Data File(s) for any items for resale, including book covers and consumer merchandise, provided these products are not intended to allow the re-distribution or re-use of the Data File(s).<br/><br/>

		(iv) Modify or alter the Data File(s) as necessary for your use, as allowed for elsewhere in these terms and conditions, provided that the rights to any derivative work shall belong to <c:out value="${company.name}"/>. You may use such derivative work only as permitted in accordance with these terms and conditions.  All copyrights with respect to such derivative work are assigned to <c:out value="${company.name}"/> or its members, which assignment the parties hereby agree is valid under the Canadian (and other International) Copyright Law. If requested by <c:out value="${company.name}"/>, you agree to execute a written assignment of any such copyrights with respect to such derivative work.<br/><br/>

		(v) In the normal course of workflow, convey to a third party (such as a printer) temporary copies of the Data File(s) that are integral to your work product and without which the product could not be completed.<br/><br/>

		(e) Prohibited Uses: You may not:<br/><br/>

		(i) Transfer the rights to the Data File(s) or accompanying materials (if applicable), except as specifically provided for elsewhere in this Agreement. All other rights are reserved by <c:out value="${company.name}"/> and/or its members.<br/><br/>

		(ii) Reverse engineer, decompile, or disassemble any part of the Data File(s) or accompanying materials (if applicable), subject to applicable law.<br/><br/>

		(iii) Copy or reproduce the Data File(s) or accompanying materials (if applicable), except as specifically provided for in paragraph 2(c).<br/><br/>

		(iv) Remove any copyright or trademark from any place where it appears on the Data File(s) or its accompanying materials.<br/><br/>

		(v) USE THE DATA FILE(S) IN ANY WAY THAT COULD BE CONSIDERED DEFAMATORY, LIBELOUS, PORNOGRAPHIC, OBSCENE, IMMORAL, OR FRAUDULENT.<br/><br/>

		(vi) Use the Data File(s) or any part of it as part of a trademark, service mark, or logo. <c:out value="${company.name}"/> or its members retains the full rights to the Data File(s), and therefore you cannot establish your own rights over any part of it.<br/><br/>

		(vii) Re-sell, distribute or sub-license the Data File(s), accompanying material, (if applicable), or the rights to use the Data File(s) to anyone for any purpose, except as specifically provided for elsewhere in this Agreement. <c:out value="${company.name}"/> is in the business of licensing Data Files to its Members and prohibits using the Data File(s) to enter, either directly or indirectly, a similar or competing business. It is also prohibited to use the Data File(s) in a product whereby the purchaser or licensee of that product can then use the Data File(s) for its own purposes.<br/>
		<br/>

		3. ADDITIONAL ROYALTY<br/><br/>

		Notwithstanding anything to the contrary herein, you agree that in the event that you or a Related Party (as defined in the Income Tax Act) either individually or in combination reproduce a Data File, or an element in a Data File, in excess of 500,000 times, you shall be required to pay a royalty fee equal to US $0.01 for each reproduction which is in excess of 100,000 reproductions.  You further agree to notify <c:out value="${company.name}"/> in the event that you (or a combination of you and Related Parties) reproduce a Data File in excess of 500,000 times.  Such a disclosure notice must be sent to <c:out value="${company.name}"/> each and every month in which a Data File which you have reproduced in excess of 500,000 times is reproduced.  Each such notice must contain the number of reproductions made in any particular month, provided however where you are sending the first such notice to <c:out value="${company.name}"/> you will only be required to disclose those reproduction made in combination by you and related parties which are in excess of 500,000. Shortly after receiving your notice <c:out value="${company.name}"/> shall invoice you for such use and you agree to pay such invoice within 30 days of receipt.   In the event that you breach any of the provisions of this Article 3, you agree that you shall be liable for all damages suffered by <c:out value="${company.name}"/>uding all costs incurred by <c:out value="${company.name}"/> in enforcing its rights hereunder.  In addition you agree that you shall be liable for additional punitive damages equal to 5 times the damages suffered by <c:out value="${company.name}"/> resulting from your breach of the provisions of this Article 3.<br/><br/>

		4. TERMS<br/>

		This License is effective until it is terminated. You can terminate this license by destroying the Data File(s), along with any copies or archives of it or accompanying materials (if applicable), and ceasing to use the Data File(s) for any purpose. The license also terminates  if at any time you fail to comply with the terms of this Agreement. If this happens, you hereby agree to destroy all copies and archives of the Data File(s), to cease using the Data File(s) for any purpose, and to confirm to <c:out value="${company.name}"/> in writing that you have complied with these requirements.<br/><br/>

		5. DISCLAIMER REGARDING MEMBER UPLOADED DATA FILES<br/>
		<c:out value="${company.name}"/> makes no representation or warranty that Data files provided by its members are Royalty Free.  Neither <c:out value="${company.name}"/>.com nor any of its directors, officers, employees, partners, or agents shall be liable for any damages, whether direct, indirect, consequential or incidental, arising out of the use of, or the inability to use, any Data Files downloaded from this web site.  Full rights and ownership of Data Files is provided as a reference and questions regarding the usability for any purpose or proposed use should be directed to the member who uploaded the file.<br/><br/>

		6. LIMITED WARRANTY<br/>
		<c:out value="${company.name}"/> represents that it has the right to enter into this Agreement and has the right to grant this license under these terms. <c:out value="${company.name}"/> warrants that the Data File(s) and/or accompanying material (if applicable) will be free from defects in materials and workmanship under normal use for a period of 30 days from the date you download or receive it. EXCEPT AS PROVIDED ABOVE, THE DATA FILE(S) AND ACCOMPANYING MATERIALS (IF APPLICABLE) ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. <c:out value="${company.name}"/> DOES NOT WARRANT THAT THE DATA FILE(S) WILL MEET YOUR REQUIREMENTS OR THAT ITS USE WILL BE UNINTERRUPTED OR ERROR FREE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE DATA FILE(S) IS WITH YOU. SHOULD THE DATA FILE(S) PROVE DEFECTIVE, YOU (AND NOT <c:out value="${company.name}"/> OR AN AUTHORIZED PERSONNEL OR OTHER COMPUTER DEALER) ASSUME THE ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION. Certain jurisdictions do not allow the exclusion of implied warranties, so the above exclusion may not apply to you. You have specific rights under this warranty, but you may have others, which vary from state to state.<br/><br/>

		7. LIMITATION OF REMEDIES <br/><br/>

		(a) Replacement or Refund: <br/>
		<c:out value="${company.name}"/>'s entire liability and your exclusive remedy, with respect to any claims arising out of your use of the Data File(s) or accompanying material (if applicable), or out of your actions in downloading the Data File(s), shall be as follows:  Downloaded Data Files: You may be permitted to download the Data File(s) again, at a location <c:out value="${company.name}"/> will provide for you.  If you continue to be unable to download the Data File(s), <c:out value="${company.name}"/> will refund your credits, provided <c:out value="${company.name}"/> determines in its sole and absolute discretion that you have been unable to download the Data File(s) successfully.<br/><br/>

		(b) No Money Damages:<br/>
		UNDER NO CIRCUMSTANCES WHATSOEVER WILL <c:out value="${company.name}"/> BE LIABLE TO YOU FOR ANY DAMAGES, INCLUDING ANY LOST PROFITS, LOST SAVINGS, OR OTHER INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR INABILITY TO USE THE DATA FILE(S) EVEN IF I STOCK PHOTO OR A DISTRIBUTOR AUTHORIZED BY I STOCK PHOTO HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. SOME STATES DO NOT ALLOW THE LIMITATIONS OR EXCLUSION OF LIABILITY FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE ABOVE LIMITATION OR EXCLUSION MAY NOT APPLY TO YOU.<br/><br/>

		8. INDEMNIFICATION<br/>
		You agree to indemnify and hold <c:out value="${company.name}"/> harmless against all claims or liability asserted against <c:out value="${company.name}"/> arising out of or in connection with any breach of any of the terms of this Agreement.<br/><br/>

		9. GENERAL<br/><br/>

		(a) Governing Law: <br/>
		This Agreement will not be governed by the United Nations Convention on Contracts for the International Sale of Goods, the application of which is expressly excluded.  This Agreement will be governed under the laws of california and the federal laws of the United States applicable therein.  Each of the parties hereto do expressly submit and attorn to the jurisdiction at the Courts of California.<br/><br/>

		(b) Enforceability:<br/>
		If any provision of this Agreement is held to be not enforceable, such provision shall be reformed only to the extent to make it enforceable.

		(c) Taxes and other:<br/>
		You agree to pay and be responsible for any and all sales taxes, use taxes, value added taxes and duties imposed by any jurisdiction as a result of license granted to you, or of your use of the Data File(s), pursuant to this Agreement.<br/><br/>

		(d) Discontinuance:<br/>
		<c:out value="${company.name}"/> reserves the right to elect at a later date to replace the Data File(s) with an alternative for any reason.  Upon notice of such replacement, the license for the replaced Data File(s) terminates for any products that do not already exist, and this license automatically applies to the replacement Data File(s).  You agree not to use the replaced Data File(s) for future products and to take all reasonable steps to discontinue use of the replaced Data File(s) in products that already exist.<br/><br/>

		10. ARBITRATION <br/>
		A material part of this contract is the Agreement to arbitrate.  Any and all disputes arising out of, under or in connection with this Agreement, with the exception of copyright claims, including without limitation, its validity, interpretation, performance and breach, shall be submitted to arbitration in California, pursuant to the rules of the Arbitration Act (California) in effect at the time arbitration is demanded.  Judgment upon any award rendered may be entered in the highest court of the forum having jurisdiction.  This Agreement, its validity and effect, shall be interpreted under, and governed by, the laws of California, and you agree that the arbitrators shall award all costs of arbitration, including legal fees, plus legal rate-of-interest to the successful party.  Copyright infringement claims shall be brought in the Courts of California.  You consent to service of any required notice or process upon you by registered mail or overnight courier with proof of delivery. <br/><br/>

		11. LEGAL FEES AND JURISDICTION<br/>
		If <c:out value="${company.name}"/> is obligated to go to court, rather than arbitration, to enforce any of its rights, or to collect any fees, you agree to reimburse <c:out value="${company.name}"/> for its legal fees, costs and disbursements if <c:out value="${company.name}"/> is successful.  You agree that the Courts of California are the agreed and appropriate forums for any such suit, and consent to service of process by registered mail or overnight courier with proof of delivery. <br/><br/>

		12. ENTIRE CONTRACT<br/>
		YOU ACKNOWLEDGE THAT YOU HAVE READ THIS AGREEMENT, UNDERSTAND IT, AND AGREE TO BE BOUND BY ITS TERMS AND CONDITIONS.  YOU FURTHER AGREE THAT IT IS THE COMPLETE AND EXCLUSIVE STATEMENT OF THE AGREEMENT BETWEEN YOU AND <c:out value="${company.name}"/>, WHICH SUPERSEDES ANY PROPOSAL OR PRIOR AGREEMENT, ORAL OR WRITTEN, AND ANY OTHER COMMUNICATION BETWEEN YOU AND <c:out value="${company.name}"/> RELATING TO THE SUBJECT OF THIS AGREEMENT.  <br/><br/>
		</div>

	</body>

</html>