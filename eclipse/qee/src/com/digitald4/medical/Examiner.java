package com.digitald4.medical;

public class Examiner extends Person{
	private String ssn="";
	private String faxNo="";
	private String cellNo="";
	private String homeNo="";
	public Examiner(int id, String email){
		super(id,email);
	}
	public Examiner(int id, String email, String firstName){
		super(id,email,firstName);
	}
	public Examiner(int id, String email, String firstName, String lastName){
		super(id,email,firstName,lastName);
	}
	public Examiner(int id, String email, String firstName, String lastName, String phoneNo){
		super(id,email,firstName,lastName,phoneNo);
	}
	public Examiner(int id, String email, String firstName, String lastName, String phoneNo, Address address){
		super(id,email,firstName,lastName,phoneNo,address);
	}
	public Examiner(int id, String email, String firstName, String lastName, String phoneNo, Address address, String ssn, String faxNo, String cellNo, String homeNo){
		super(id,email,firstName,lastName,phoneNo,address);
		setSSN(ssn);
		setFaxNo(faxNo);
		setCellNo(cellNo);
		setHomeNo(homeNo);
	}
	public String getSSN(){
		return ssn;
	}
	public void setSSN(String ssn){
		this.ssn = ssn;
	}
	public void setFaxNo(String faxNo){
		this.faxNo = faxNo;
	}
	public void setCellNo(String cellNo){
		this.cellNo = cellNo;
	}
	public void setHomeNo(String homeNo){
		this.homeNo = homeNo;
	}
	public String getFaxNo(){
		return faxNo;
	}
	public String getCellNo(){
		return cellNo;
	}
	public String getHomeNo(){
		return homeNo;
	}
}
