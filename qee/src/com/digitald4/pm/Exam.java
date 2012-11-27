package com.digitald4.pm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.digitald4.util.DBConnector;
import com.digitald4.util.DD4Object;
public class Exam extends DD4Object{
	public final static int OPEN=0;
	public final static int CLOSED=1;
	public final static int CANCELLED=2;
	public final static String TABLE="tbl_client_exam";
	public final static String WHERE="WHERE ClientId=? AND ExamId=?";
	private Client client;
	private String name;
	private String desc;
	private int status;
	private String note;
	private String finishDate;
	private String lastUpdate;
	private String price;
	private boolean hide=false;

	public Exam(int id){
		this.id=id;
	}
	public Exam(int id, ResultSet rs)throws SQLException{
		this(null,id,rs);
	}
	public Exam(Client client, int id, ResultSet rs)throws SQLException{
		this.id = id;
		this.client = client;
		refresh(rs);
	}
	public void refresh(ResultSet rs)throws SQLException{
		desc = rs.getString("description");
		finishDate = rs.getString("finish_date");
		lastUpdate = rs.getString("last_update");
		name = rs.getString("name");
		note = rs.getString("note");
		price = rs.getString("price");
		status = rs.getInt("status");
		hide = rs.getBoolean("hide");
	}
	public boolean setField(String field, String value)throws Exception{
		switch(field.charAt(0)){
			case 'd': setDesc(value); return true;
			case 'f': setFinishDate(value); return true;
			//case 'l': setLastUpdate(value); break;
			case 'n': if(field.charAt(1)=='a'){
						setName(value); return true;
					}else if(field.charAt(1) == 'o'){
						setNote(value); return true;
					}
			case 's': setStatus(Integer.parseInt(value)); return true;
		}
		return false;
	}
	public String getLastUpdate(){
		return lastUpdate;
	}
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getDesc(){
		return desc;
	}
	public int getStatus(){
		return status;
	}
	public String getNote(){
		return note;
	}
	public String getPrice(){
		return price;
	}
	public String getFinishDate(){
		return finishDate;
	}
	public boolean isHidden(){
		return hide;
	}
	public void setHide(boolean hide)throws Exception{
		setProp("hide",hide);
		this.hide=hide;
	}
	public void setClient(Client client){
		this.client = client;
	}
	public void setName(String name)throws Exception{
		setProp("name",name);
		this.name=name;
	}
	public void setDesc(String desc)throws Exception{
		setProp("description",desc);
		this.desc = desc;
	}
	public void setStatus(int status)throws Exception{
		setProp("status",status);
		this.status = status;
	}
	public void setNote(String note)throws Exception{
		setProp("note",note);
		this.note = note;
	}
	public void setPrice(String price)throws Exception{
		setProp("price",price);
		this.price = price;
	}
	public void setFinishDate(String finishDate)throws Exception{
		setDate("finish_date",finishDate);
		this.finishDate = finishDate;
	}
	public void setProp(String col, String value)throws Exception{
		if(id > 0 && client != null){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setString(1,value);
			ps.setInt(2,client.getId());
			ps.setInt(3,id);
			ps.executeUpdate();
			ps.close();
			con.close();
			client.setLastUpdate();
		}
	}
	public void setProp(String col, int value)throws Exception{
		if(id > 0 && client != null){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setInt(1,value);
			ps.setInt(2,client.getId());
			ps.setInt(3,id);
			ps.executeUpdate();
			ps.close();
			con.close();
			client.setLastUpdate();
		}
	}
	public void setProp(String col, boolean value)throws Exception{
		if(id > 0 && client != null){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setBoolean(1,value);
			ps.setInt(2,client.getId());
			ps.setInt(3,id);
			ps.executeUpdate();
			ps.close();
			con.close();
			client.setLastUpdate();
		}
	}
	public void setDate(String col, String date)throws Exception{
		if(id > 0 && client != null){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=STR_TO_DATE(?,?) "+WHERE);
			ps.setString(1,date);
			ps.setString(2,Client.DATE_FORMAT);
			ps.setInt(3,client.getId());
			ps.setInt(4,id);
			ps.executeUpdate();
			ps.close();
			con.close();
			client.setLastUpdate();
		}
	}
	public String toString(){
		return getName();
	}
}
