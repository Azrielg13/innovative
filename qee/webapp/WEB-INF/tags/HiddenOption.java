package com.digitald4.ctags;

public class HiddenOption{
	private String hidden=null;
	private String display=null;
	
	public HiddenOption(String hidden, String display){
		setHidden(hidden);
		setDisplay(display);
	}
	public void setHidden(String hidden){
		this.hidden=hidden;
	}
	public String getHidden(){
		return hidden;
	}
	public void setDisplay(String display){
		this.display=display;
	}
	public String getDisplay(){
		return display;
	}
	public String toString(){
		return getDisplay();
	}
}
