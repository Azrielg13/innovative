package com.digitald4.order;
public class CreditCard{
	//private String name="";
	private String type="";
	private String number="";
	private String backCode="";
	private int month=1;
	private int year=06;

	public CreditCard(){
	}
	public CreditCard(String type, String number, String backCode, int month, int year){
		//setName(name);
		setType(type);
		setNumber(number);
		setBackCode(backCode);
		setMonth(month);
		setYear(year);
	}
	//public void setName(String name){
	//	this.name = name;
	//}
	//public String getName(){
	//	return name;
	//}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
	public void setNumber(String number){
		this.number = number;
	}
	public String getNumber(){
		return number;
	}
	public void setBackCode(String backCode){
		this.backCode = backCode;
	}
	public String getExpDate(){
		return ""+year+"-"+month+"-1";
	}
	public void setMonth(int month){
		this.month = month;
	}
	public void setMonth(String month){
		if(month == null)
			this.month=0;
		else
			this.month = Integer.parseInt(month);
	}
	public String getMonth(){
		return ""+month;
	}
	public void setYear(int year){
		this.year = year;
	}
	public void setYear(String year){
		if(year == null)
			this.year=0;
		else
			this.year = Integer.parseInt(year);
	}
	public String getYear(){
		return ""+year;
	}
	public String getBackCode(){
		return backCode;
	}
	public boolean isGood(){
		return (type != null && type.length() > 0 && number != null && number.length() > 0 && backCode != null && backCode.length() > 0 && month > 0 && year > 0);
	}
}
