package com.digitald4.medical;

public class Agent extends Person{
	public Agent(int id, String email){
		super(id,email);
	}
	public Agent(int id, String email, String firstName){
		super(id,email,firstName);
	}
	public Agent(int id, String email, String firstName, String lastName){
		super(id,email,firstName,lastName);
	}
	public Agent(int id, String email, String firstName, String lastName, String phoneNo){
		super(id,email,firstName,lastName,phoneNo);
	}
	public Agent(int id, String email, String firstName, String lastName, String phoneNo, Address address){
		super(id,email,firstName,lastName,phoneNo,address);
	}
}
