package com.digitald4.util;
import java.util.Hashtable;
import java.sql.*;
public class Company{
	private static Company company;
	public static Company getCompany()throws Exception{
		if(company != null)
			return company;
		else{
			Connection con = DBConnector.getInstance().getConnection();
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM tbl_company");
			if(rs.next()){
				company = new Company(rs.getString("company"));
				//company.setName(rs.getString("company"));
				company.setWebsite(rs.getString("Website"));
				company.setSlogan(rs.getString("Slogan"));
				company.setDescription(rs.getString("ip_Address"));
				company.setEmail(rs.getString("Email"));
				company.setPaypal(rs.getString("paypal"));
				company.setStatCounterID(rs.getString("StatCounter_id"));
				//company.setStatCounterPart(rs.getString("StatCounter_part"));
				company.setContainer(rs.getString("Container"));
				company.setAddress(rs.getString("Address"));
				company.setPhone(rs.getString("Phone"));
				company.setFax(rs.getString("fax"));
				company.setReportFooter(rs.getString("report_footer"));
			}
			rs.close();
			con.close();
		}
		return company;
	}
	private String name="";
	private String website="";
	private String slogan="";
	private String description="";
	private String ipAddress="";
	private String email="";
	private String paypal="";
	private String statCounterID="";
	private String statCounterPart="";
	private String container="";
	private String address="";
	private String phone="";
	private String fax="";
	private String reportFooter;
	protected Company(String name){
		System.out.println("New company: "+name);
		setName(name);
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setWebsite(String website){
		this.website = website;
	}
	public String getWebsite(){
		return website;
	}
	public void setSlogan(String slogan){
		this.slogan=slogan;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	public void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}
	public String getIpAddress(){
		return ipAddress;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public String getAddress(){
		return address;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setFax(String fax){
		this.fax = fax;
	}
	public String getFax(){
		return fax;
	}
	public void setPaypal(String paypal){
		this.paypal = paypal;
	}
	public String getPaypal(){
		return paypal;
	}
	public void setContainer(String container){
		this.container = container;
	}
	public String getContainer(){
		return container;
	}
	public void setStatCounterID(String statCounterID){
		this.statCounterID=statCounterID;
	}
	public String getStatCounterID(){
		return statCounterID;
	}
	public void setStatCounterPart(String statCounterPart){
		this.statCounterPart=statCounterPart;
	}
	public String getStatCounterPart(){
		return statCounterPart;
	}
	public String getReportFooter(){
		return reportFooter;
	}
	public void setReportFooter(String reportFooter){
		this.reportFooter = reportFooter;
	}
}
