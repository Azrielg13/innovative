package com.digitald4.order;
public class Customer{
	private int custID=0;
	private String email="";
	private String fName="";
	private String lName="";
	private String type="";
	private Address address = new Address();

	public Customer(){
	}
	public Customer(int custID){
		setCustID(custID);
	}
	public Customer(int custID, String email, String fName){
		setCustID(custID);
		setEmail(email);
		setFName(fName);
	}
	public Customer(int custID, String email, String fName, String lName){
		setCustID(custID);
		setEmail(email);
		setFName(fName);
		setLName(lName);
	}
	public void setCustID(int custID){
		this.custID=custID;
	}
	public int getCustID(){
		return custID;
	}
	public void setType(String type){
		this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	public void setFName(String fName){
		this.fName = fName;
	}
	public String getFName(){
		return fName;
	}
	public void setLName(String lName){
		this.lName = lName;
	}
	public String getLName(){
		return lName;
	}
	public Address getAddress(){
		return address;
	}
	public String toString(){
		return email;
	}
	public boolean isGood(){
		return (email != null && email.length() > 0 && fName != null && fName.length() > 0 && lName != null && lName.length() > 0 && address.isGood());
	}
}
