package com.digitald4.medical;

public class Admin extends Person{
	private String type="Staff";
	public Admin(int id, String type){
		super(id);
		setType(type);
	}
	public Admin(int id, String type, String email){
		super(id,email);
		setType(type);
	}
	public Admin(int id, String type, String email, String firstName){
		super(id,email,firstName);
		setType(type);
	}
	public Admin(int id, String type, String email, String firstName, String lastName){
		super(id,email,firstName,lastName);
		setType(type);
	}
	public Admin(int id, String type, String email, String firstName, String lastName, String phoneNo){
		super(id,email,firstName,lastName,phoneNo);
		setType(type);
	}
	public Admin(int id, String type, String email, String firstName, String lastName, String phoneNo, Address address){
		super(id,email,firstName,lastName,phoneNo,address);
		setType(type);
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
}
