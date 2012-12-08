package com.digitald4.common.model;

public class Company{
	private static Company company;
	public static Company getInstance(){
		if(company == null) {
			company = new Company();
			company.setName("Innovative Infusion Solutions");
			company.setWebsite("www.iis.com");
			company.setSlogan("we are iis");
			company.setDescription("infusion");
			company.setEmail("kenya@iis.com");
			company.setPaypal("");
			company.setStatCounterID(null);
			company.setStatCounterPart(null);
			company.setContainer(null);
			company.setAddress(null);
			company.setPhone(null);
			company.setFax(null);
		}
		return company;
	}
	public static Company getCompany(){
		return getInstance();
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
	private Company(){
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
	public String getSlogan(){
		return slogan;
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
}
