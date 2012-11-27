package com.digitald4.util;
public class Address{
	private String street1="";
	private String street2="";
	private String city="";
	private String state="CA";
	private String zip="";
	public Address(){
	}
	public Address(String street1, String city, String state, String zip){
		setStreet1(street1);
		setCity(city);
		setState(state);
		setZip(zip);
	}
	public Address(String street1, String street2, String city, String state, String zip){
		setStreet1(street1);
		setStreet2(street2);
		setCity(city);
		setState(state);
		setZip(zip);
	}
	private void setStreet1(String street1){
		this.street1=street1;
	}
	private void setStreet2(String street2){
		this.street2=street2;
	}
	private void setCity(String city){
		this.city=city;
	}
	private void setState(String state){
		this.state=state;
	}
	private void setZip(String zip){
		this.zip=zip;
	}
	public String getStreet1(){
		return street1;
	}
	public String getStreet2(){
		return street2;
	}
	public String getCity(){
		return city;
	}
	public String getState(){
		return state;
	}
	public String getZip(){
		return zip;
	}
	public String toString(){
		return street1+" "+street2+" "+city+", "+state+" "+zip;
	}
	public boolean isGood(){
		return (street1 != null && street1.length() > 0 && city != null && city.length() > 0 && state != null && state.length() > 0 && zip != null && zip.length() > 0);
	}
}
