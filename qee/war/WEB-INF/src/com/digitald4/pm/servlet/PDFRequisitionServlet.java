/***************************************************************************

$name: Invoice Report
$version: 2.0
$date_modified: 07242006
$description:
$owner: Brian Stonerock
Copyright (c) 2006 BSto Productions. All Rights Reserved.

****************************************************************************/
package com.digitald4.pm.servlet;
import com.digitald4.pm.*;
import com.digitald4.util.*;

import java.io.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.sql.*;
import java.lang.Math.*;
import java.awt.Color;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

import java.text.*;


public class PDFRequisitionServlet extends ParentServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		doWeb(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doWeb(request, response);
	}

	public void doWeb(HttpServletRequest request, HttpServletResponse response){

		try {
			HttpSession session = request.getSession();

			if(!checkLogin(request,response))
				return;

			String clientIds = request.getParameter("clientId");
			if(clientIds == null || clientIds.length() == 0)
				return;
			int clientId = Integer.parseInt(clientIds);
			Client client = Client.getClient(clientId);

			User user = (User)request.getSession().getAttribute("user");

			if(!isAuthorized(request,response,client))
				return;

			//verify who is looking at each requisition
			if(user.getType()<=User.STAFF){

			}else if(user.getType()==User.AGENT){
				if(client.getAgent().getId()!=user.getId())
					return;
			}else if(user.getType()==User.EXAMINER){
				if(client.getExaminer().getId()!=user.getId())
					return;
			}else{
				return;
			}
			response.setContentType( "application/pdf" );

			ServletOutputStream out = response.getOutputStream();

			Connection con =  DBConnector.getInstance().getConnection();

			Document document = new Document(PageSize.A4, 25, 25, 25, 25);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter.getInstance( document, buffer );
			document.addAuthor("BSto Productions");
			document.addSubject("Order");
			document.addTitle("Order");

			String filename = "Order_"+clientId+".pdf";

			document.open();
			createPDF(con,document,clientId);
			document.close();
			con.close();

			DataOutput output = new DataOutputStream( response.getOutputStream() );
			byte[] bytes = buffer.toByteArray();
			response.setContentLength(bytes.length);
			for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }



		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			//con.close();
    	}

 	}

	 public static void createPDF(Connection con, Document document, int clientId) {

		try {
			document.resetHeader();

			HeaderFooter footer = new HeaderFooter(new Phrase("",FontFactory.getFont(FontFactory.HELVETICA, 8)), false);
			footer.setBorder(Rectangle.NO_BORDER);
			document.setFooter(footer);
			document.setPageSize(PageSize.A4);
			document.setMargins(25,25,25,25);
			document.newPage();

			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			String DATE_FORMAT = "MM/dd/yyyy";
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
			sdf.setTimeZone(TimeZone.getDefault());

			String website = "";

			//Need to Edit to use company object




			String reportFooter = "";
			ResultSet rsCompany = con.createStatement().executeQuery("SELECT * FROM tbl_company WHERE hide=0");
			if(rsCompany.next()){

				reportFooter = rsCompany.getString("report_footer");

				Paragraph companyPara = new Paragraph("");

// TODO: Need to add padding on the left

///////////////////////////////////////////////////////////////////

				//Image gif = Image.getInstance("http://"+rsCompany.getString("website")+"/img/LogoPic.gif");

				try{
					if(rsCompany.getString("website")!=null&&rsCompany.getString("website").length()!=0){
						Image gif = Image.getInstance("http://"+rsCompany.getString("website")+"/img/LogoPic.gif");
						gif.setAlignment(Image.LEFT);
						//gif.setAbsolutePosition(10, 10);
						gif.setAlignment(Image.LEFT | Image.UNDERLYING);
						gif.scaleAbsolute(100, 100);
						//document.add(gif);
						companyPara.add(gif);
					}
				}catch(Exception fuckthisshit){

				}


				companyPara.setAlignment(Element.ALIGN_CENTER);

				//companyPara.add(new Chunk("ORDER # "+clientId+"\n\n", new Font(Font.HELVETICA, 18, Font.BOLD)));

				if(rsCompany.getString("company")!=null && rsCompany.getString("company").length()>0)
					companyPara.add(new Chunk(""+rsCompany.getString("company"), new Font(Font.HELVETICA, 20, Font.BOLD)));

				if(rsCompany.getString("slogan")!=null && rsCompany.getString("slogan").length()>0)
					companyPara.add(new Chunk("\n"+rsCompany.getString("slogan"), new Font(Font.HELVETICA, 10, Font.ITALIC)));

				if(rsCompany.getString("address")!=null && rsCompany.getString("address").length()>0)
					companyPara.add(new Chunk("\n"+rsCompany.getString("address"), new Font(Font.HELVETICA, 10)));

				if(rsCompany.getString("phone")!=null && rsCompany.getString("phone").length()>0)
					companyPara.add(new Chunk("\n Office "+rsCompany.getString("phone"), new Font(Font.HELVETICA, 10)));

				if(rsCompany.getString("fax")!=null && rsCompany.getString("fax").length()>0)
					companyPara.add(new Chunk("\n Fax "+rsCompany.getString("fax"), new Font(Font.HELVETICA, 10)));

				if(rsCompany.getString("website")!=null && rsCompany.getString("website").length()>0){
					website = rsCompany.getString("website");
					website = "www.QualityExpressExaminers.com";
					companyPara.add(new Chunk("\n"+website, new Font(Font.HELVETICA, 10, Font.BOLD)));
				}

				document.add(companyPara);
			}

			Client client = Client.getClient(clientId);
			Address clientAdd = client.getAddress();
			Address clientAdd2 = client.getAddress2();
			if(clientAdd2 != null && (clientAdd2.getStreet1() == null || clientAdd2.getStreet1().length() == 0))
				clientAdd2 = null;

			InsuranceCo ins = (InsuranceCo)client.getIns();
			Address insAdd = ins.getAddress();

			Address billAdd = null;
			if(client.getBillingCo()!=null)
				billAdd = client.getBillingCo().getAddress();

			Table datatable = new Table(2);
			datatable.setBorderWidth(0);
			datatable.setBorderColor(new Color(0, 0, 0));
			datatable.setDefaultCellBorder(0);
			datatable.setAlignment(0);
			datatable.setPadding(1);
			datatable.setSpacing(1);
			int headerwidths[] = {50,50};
			datatable.setWidths(headerwidths);
			datatable.setWidth(100);

			datatable.setDefaultHorizontalAlignment(0);
			datatable.setDefaultColspan(1);
			datatable.endHeaders();


			Cell cell = new Cell();

			datatable.addCell(new Phrase("Order #: "+client.getId(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
			datatable.addCell(new Phrase("Received Date: "+client.getRecDate(), FontFactory.getFont(FontFactory.HELVETICA, 12)));


			cell = new Cell("Agent (Agent Code): "+(client.getAgent()==null?"":client.getAgent())+(client.getAgentCode()==null||client.getAgentCode().length()==0?"":" ("+client.getAgentCode()+")")+"\t\t\t\t\t\t\t\t\t\t"+"Agency (Agency Code): "+(client.getAgency()==null?"":client.getAgency())+(client.getAgencyCode()==null||client.getAgencyCode().length()==0?"":" ("+client.getAgencyCode()+")"));
			cell.setBorderColor(new Color(0, 0, 0));
			cell.setBorder(Rectangle.TOP);
			cell.setBorderWidth(2);
			cell.setColspan(2);
            datatable.addCell(cell);


			//datatable.addCell(new Phrase("Agent (Agent Code): "+client.getAgent()+" ("+client.getAgentCode()+")", FontFactory.getFont(FontFactory.HELVETICA, 10)));
			//datatable.addCell(new Phrase("Agency (Agency Code): "+client.getAgency()+" ("+client.getAgencyCode()+")", FontFactory.getFont(FontFactory.HELVETICA, 10)));

			//cell = new Cell("Agent (Agent Code): "+(client.getAgent()==null?"":client.getAgent())+(client.getAgentCode()==null||client.getAgentCode().length()==0?"":" ("+client.getAgentCode()+")"));
			//cell.setBorderColor(new Color(0, 0, 0));
			//cell.setBorder(Rectangle.TOP);
			//cell.setBorderWidth(2);
			//datatable.addCell(cell);

			//cell = new Cell("Agency (Agency Code): "+(client.getAgency()==null?"":client.getAgency())+(client.getAgencyCode()==null||client.getAgencyCode().length()==0?"":" ("+client.getAgencyCode()+")"));
			//cell.setBorderColor(new Color(0, 0, 0));
			//cell.setBorder(Rectangle.TOP);
			//cell.setBorderWidth(2);
			//datatable.addCell(cell);


			datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));



			datatable.setDefaultColspan(2);
			datatable.addCell(new Phrase("Insurance Company: "+(client.getIns()==null?"":client.getIns()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
			datatable.setDefaultColspan(1);



			cell = new Cell("Insurance Type: "+(client.getInsType()==null?"":client.getInsType())+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+"Insurance Amount: "+(client.getInsAmount()==null?"":client.getInsAmount()));
			cell.setBorderColor(new Color(0, 0, 0));
			cell.setBorder(Rectangle.TOP);
			cell.setBorderWidth(2);
			cell.setColspan(2);
            datatable.addCell(cell);

			//cell = new Cell("Insurance Type: "+(client.getInsType()==null?"":client.getInsType()));
			//cell.setBorderColor(new Color(0, 0, 0));
			//cell.setBorder(Rectangle.TOP);
			//cell.setBorderWidth(2);
            //datatable.addCell(cell);

			//cell = new Cell("Insurance Amount: "+(client.getInsAmount()==null?"":client.getInsAmount()));
			//cell.setBorderColor(new Color(0, 0, 0));
			//cell.setBorder(Rectangle.TOP);
			//cell.setBorderWidth(2);
            //datatable.addCell(cell);

			//datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));
			//datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));

			cell = new Cell("");
			cell.setBorderColor(new Color(0, 0, 0));
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderWidth(2);
			cell.setColspan(2);
            datatable.addCell(cell);


			datatable.addCell(new Phrase("Proposed Insured: "+client, FontFactory.getFont(FontFactory.HELVETICA, 12)));
			datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));



			cell = new Cell("DOB (Age): "+(client.getDOB()==null||client.getDOB().length()==0?"":client.getDOB())+" "+(client.getAge()==0?"":"("+client.getAge()+")   Sex: "+(client.getSex()==null?"":client.getSex())));
			//cell.setBorderColor(new Color(0, 0, 0));
			//cell.setBorder(Rectangle.TOP);
			//cell.setBorderWidth(2);
			datatable.addCell(cell);

			cell = new Cell("SSN: "+(client.getSSN()==null||client.getSSN().length()==0?"":client.getSSN()));
			//cell.setBorderColor(new Color(0, 0, 0));
			//cell.setBorder(Rectangle.TOP);
			//cell.setBorderWidth(2);
			datatable.addCell(cell);

			//datatable.addCell(new Phrase("DOB (Age): "+client.getDOB()+" ("+client.getAge()+")", FontFactory.getFont(FontFactory.HELVETICA, 10)));
			//datatable.addCell(new Phrase("SSN: "+client.getSSN(), FontFactory.getFont(FontFactory.HELVETICA, 10)));



			datatable.setDefaultColspan(clientAdd2==null?2:1);
			datatable.addCell(new Phrase("Exam Address: "+(clientAdd==null?"":clientAdd), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			if(clientAdd2 != null)
				datatable.addCell(new Phrase("Alt Address: "+(client.getAddress2()==null?"":client.getAddress2()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.setDefaultColspan(2);
			datatable.addCell(new Phrase("Home Phone No: "+(client.getPhoneNo()==null?"":client.getPhoneNo()) +"       Alt Phone No 1: "+(client.getAltPhone1()==null||client.getAltPhone1().length()==0?"":client.getAltPhone1()) +"       Alt Phone No 2: "+(client.getAltPhone2()==null||client.getAltPhone2().length()==0?"":client.getAltPhone2()), FontFactory.getFont(FontFactory.HELVETICA, 10)));


			cell = new Cell("");
			cell.setBorderColor(new Color(0, 0, 0));
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderWidth(2);
			cell.setColspan(2);
            datatable.addCell(cell);

			datatable.setDefaultColspan(1);
			datatable.addCell(new Phrase("Primary Language: "+client.getPrimaryLang(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.addCell(new Phrase("Lab: "+(client.getLabCo()==null?"":client.getLabCo()), FontFactory.getFont(FontFactory.HELVETICA, 10)));

			String exams = ""+(client.getLabCode()==null||client.getLabCode().length()==0?"":"Lab Code: "+client.getLabCode())+"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
			for(int a=0; a<client.getExams().size();a++){
				Exam exam = (Exam)client.getExams().get(a);
				if(a!=0)
					exams+="\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
				exams+=""+(a+1)+". "+exam.getName()+(exam.getNote()!=null?" - "+exam.getNote():"");
			}


			datatable.setDefaultColspan(2);
			datatable.addCell(new Phrase("Procedures: "+(exams==null||exams.length()==0?"":exams), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.addCell(new Phrase("Examiner: "+(client.getExaminer()==null?"":client.getExaminer()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));



			datatable.setDefaultColspan(1);
			datatable.addCell(new Phrase("Please Make Sure Billing Company is:", FontFactory.getFont(FontFactory.HELVETICA, 12)));
			datatable.addCell(new Phrase(""+(client.getBillingCo()==null?"":client.getBillingCo()) + "\n"+ (billAdd==null?"":billAdd), FontFactory.getFont(FontFactory.HELVETICA, 12)));

			datatable.addCell(new Phrase("Schedule Date: "+(client.getStartDate()==null||client.getStartDate().length()==0?"":client.getStartDate())+" "+(client.getStartTime()==null||client.getStartTime().length()==0?"":"Preset Time: "+client.getStartTime()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 10)));


			datatable.setDefaultColspan(2);
			datatable.addCell(new Phrase("Mailing Instructions: "+(client.getMailIns()==null||client.getMailIns().length()==0?"":client.getMailIns()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
			datatable.addCell(new Phrase("Special Notes: "+(client.getNote()==null||client.getNote().length()==0?"":client.getNote()), FontFactory.getFont(FontFactory.HELVETICA, 10)));


			document.add(datatable);

			Paragraph body = new Paragraph("");
			body.setAlignment(Element.ALIGN_LEFT);

			body.add(new Chunk("Note to All Examiners: ", new Font(Font.HELVETICA, 8, Font.BOLD)));
			body.add(new Chunk(""+reportFooter, new Font(Font.HELVETICA, 8)));


			document.add(body);


		}

		catch(DocumentException de) {
			System.err.println(de.getMessage());
		}

		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
