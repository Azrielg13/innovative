package com.digitald4.order;
import java.text.*;
public class Item{
	private String itemNo="";
	private int cartIndex=0;
	private String title="";
	private String picURL="img/checkout.gif";
	private double price=2.99;
	private int qty=1;
	private String color="";
	private String size="";
	private boolean shipReq=true;
	private boolean taxable=true;
	public Item(String itemNo){
		setItemNo(itemNo);
	}
	public Item(String itemNo, int cartIndex){
		setItemNo(itemNo);
		setCartIndex(cartIndex);
	}
	public Item(String itemNo, int cartIndex, int qty){
		setItemNo(itemNo);
		setCartIndex(cartIndex);
		setQty(qty);
	}
	public Item(String itemNo, int cartIndex, int qty, String color, String size){
		setItemNo(itemNo);
		setCartIndex(cartIndex);
		setQty(qty);
		setColor(color);
		setSize(size);
	}
	public Item(String itemNo, String title, String picture, double price, boolean shipReq, boolean taxable){
		setItemNo(itemNo);
		setTitle(title);
		setPicURL(picture);
		setPrice(price);
		setShipping(shipReq);
		setTaxable(taxable);
	}
	public void setCartIndex(int cartIndex){
		this.cartIndex=cartIndex;
	}
	public int getCartIndex(){
		return cartIndex;
	}
	public void setItemNo(String itemNo){
		this.itemNo = itemNo;
	}
	public String getItemNo(){
		return itemNo;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	public void setPicURL(String picURL){
		this.picURL = picURL;
	}
	public String getPicURL(){
		return picURL;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public double getPrice(){
		return price;
	}
	public String getUnitCost(){
		return NumberFormat.getCurrencyInstance().format(price);
	}
	public String getTotalCost(){
		return NumberFormat.getCurrencyInstance().format(price*qty);
	}
	public void setTaxable(boolean taxable){
		this.taxable = taxable;
	}
	public boolean isTaxable(){
		return taxable;
	}	
	public void setQty(int qty){
		this.qty = qty;
	}
	public int getQty(){
		return qty;
	}
	public void setColor(String color){
		if(color == null)
			this.color = "";
		else
			this.color = color;
	}
	public String getColor(){
		return color;
	}
	public void setSize(String size){
		if(size == null)
			this.size="";
		else
			this.size = size;
	}
	public String getSize(){
		return size;
	}
	public void setShipping(boolean required){
		this.shipReq = required;
	}
	public boolean requiresShipping(){
		return shipReq;
	}
	public String toString(){
		return getItemNo();
	}
	public boolean equals(Object cmp){
		if(cmp instanceof Item){
			Item cmpItem = (Item)cmp;
			return (getItemNo().equals(cmpItem.getItemNo()) && getColor().equals(cmpItem.getColor()) && getSize().equals(cmpItem.getSize()));
		}
		return false;
	}
}
