package com.digitald4.order;
public class Address{
	private String name="";
	private String street1="";
	private String street2="";
	private String city="";
	private String state="CA";
	private String zip="";
	private String phoneNo="";
	private double taxRate=0.00;
	public Address(){
	}
	public Address(String street1){
		setStreet1(street1);
	}
	public Address(String name, String street1, String city, String state, String zip){
		setName(name);
		setStreet1(street1);
		setCity(city);
		setState(state);
		setZip(zip);
	}
	public Address(String name, String street1, String street2, String city, String state, String zip){
		setName(name);
		setStreet1(street1);
		setStreet2(street2);
		setCity(city);
		setState(state);
		setZip(zip);
	}
	public void clear(){
		name="";
		street1="";
		street2="";
		city="";
		state="";
		zip="";
		phoneNo="";
		taxRate=0.00;
	}
	public void copy(Address address){
		setName(address.getName());
		setStreet1(address.getStreet1());
		setStreet2(address.getStreet2());
		setCity(address.getCity());
		setState(address.getState());
		setZip(address.getZip());
		setPhoneNo(address.getPhoneNo());
	}
	public void setName(String name){
		if(name == null)
			name="";
		this.name = name;
	}
	public String getName(){
		return name;
	}	
	public void setStreet1(String street1){
		if(street1 == null)
			street1="";
		this.street1 = street1;
	}
	public String getStreet1(){
		return street1;
	}
	public void setStreet2(String street2){
		if(street2 == null)
			street2="";
		this.street2 = street2;
	}
	public String getStreet2(){
		return street2;
	}
	public void setCity(String city){
		if(city == null)
			city="";
		this.city = city;
	}
	public String getCity(){
		return city;
	}
	public void setState(String state){
		if(state == null)
			state="";
		this.state = state;	
		setTaxRate(7.75);
	}
	public String getState(){
		return state;
	}
	public void setTaxRate(double taxRate){
		this.taxRate = taxRate;
	}
	public double getTaxRate(){
		return taxRate;
	}
	public void setZip(String zip){
		if(zip == null)
			zip="";
		this.zip = zip;
	}
	public String getZip(){
		return zip;
	}
	public void setPhoneNo(String phoneNo){
		if(phoneNo == null)
			phoneNo="";
		this.phoneNo = phoneNo;
	}
	public String getPhoneNo(){
		return phoneNo;
	}	
	public String toString(){
		String toStr=street1+" "+street2+" "+city+", "+state+" "+zip;
		return toStr;
	}
	public boolean isGood(){
		return (name != null && name.length() > 0 && street1 != null && street1.length() > 0 && city != null && city.length() > 0 && state != null && state.length() > 0 && zip != null && zip.length() > 0 && phoneNo != null && phoneNo.length() > 0);
	}
}
