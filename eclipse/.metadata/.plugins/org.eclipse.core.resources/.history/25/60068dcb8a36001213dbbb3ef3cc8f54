package com.digitald4.order;
public class ShipMethod{
	private String title="";
	private int maxCount=1;
	private double price=0;
	public ShipMethod(){
	}
	public ShipMethod(String title, int maxCount, double price){
		setTitle(title);
		setMaxCount(maxCount);
		setPrice(price);
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	public void setMaxCount(int maxCount){
		this.maxCount=maxCount;
	}
	public int getMaxCount(){
		return maxCount;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public double getPrice(){
		return price;
	}
	public double getQuote(Cart cart){
		return Math.ceil(cart.getShipCount()/(getMaxCount()*1.0))*getPrice();
	}
	public String getSelected(){
		return "SELECTED";
	}
	public boolean isGood(){
		return (title != null && title.length() > 0);
	}
}
