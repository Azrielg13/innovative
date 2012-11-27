package com.digitald4.pm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import com.digitald4.util.DBConnector;
public class InsuranceType{
	public final static String TABLE="tbl_insurance_type";
	public final static String WHERE="WHERE insTypeId=?";
	private int id;
	private String name="";
	private boolean hide;
	private static Hashtable<Integer,InsuranceType> types = new Hashtable<Integer,InsuranceType>();

	public static InsuranceType getInsuranceType(int id)throws Exception{
		InsuranceType it=null;
		if(types.containsKey(id)){
			it = types.get(id);
			it.refresh();
		}else{
			it = new InsuranceType(id);
			if(!it.refresh())
				return null;
			types.put(id,it);
		}
		return it;
	}
	public InsuranceType(){
	}
	public InsuranceType(int id){
		this.id = id;
	}
	public InsuranceType(int id, String name, boolean hide){
		this.id = id;
		this.name = name;
		this.hide = hide;
	}
	public boolean refresh()throws Exception{
		boolean ret=true;
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM "+TABLE+" "+WHERE);
		ps.setInt(1,id);
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
		name = rs.getString("insType");
		hide = rs.getBoolean("hide");
	}
	public int getId(){
		return id;
	}
	//Eddie this throws an error if the name is null
	public String getName(){
		return name;
	}
	public boolean ishidden(){
		return hide;
	}
	public String toString(){
		return getName();
	}
}