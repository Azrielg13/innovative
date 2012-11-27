package com.digitald4.pm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.digitald4.util.DBConnector;
import com.digitald4.util.DD4Object;
public class Message extends DD4Object{
	public final static String TABLE="tbl_message";
	public final static String SELECT="SELECT DATE_FORMAT(last_update,'"+Client.DATE_FORMAT+"') last_update, "+TABLE+".*";
	public final static String WHERE="WHERE clientId=? AND messageId=?";
	private Client client;
	private String text;
	private User creator;
	private String lastUpdate;

	public Message(Client client, int id){
		this.client = client;
		this.id = id;
	}
	public Message(Client client, int id, ResultSet rs)throws Exception{
		this.client = client;
		this.id = id;
		refresh(rs);
	}
	public Message(Client client, int id, String text, User creator){
		this.client=client;
		this.id=id;
		this.text=text;
		this.creator=creator;
	}
	public boolean refresh()throws Exception{
		boolean ret=true;
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(SELECT+" FROM "+TABLE+" "+WHERE);
		ps.setInt(1,client.getId());
		ps.setInt(2,id);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			refresh(rs);
		else
			ret = false;
		rs.close();
		ps.close();
		con.close();
		return ret;
	}
	public void refresh(ResultSet rs)throws Exception{
		creator = User.getUser(rs.getInt("userId"));
		text = rs.getString("message");
		lastUpdate = rs.getString("last_update");
	}
	public boolean setField(String field, String value)throws Exception{
		switch(field.charAt(0)){
			case 'c': setCreator(User.getUser(Integer.parseInt(value))); return true;
			case 't': setText(value); return true;
		}
		return false;
	}
	public String getLastUpdate(){
		return lastUpdate;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public String getText(){
		return text;
	}
	public User getCreator(){
		return creator;
	}

	public void setText(String text)throws Exception{
		setProp("message",text);
		this.text=text;
	}
	public void setCreator(User creator)throws Exception{
		if(creator != null)
			setProp("user",creator.getId());
		else
			setProp("user",0);
		this.creator=creator;
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
}
