package com.digitald4.pm;
import java.sql.*;
import java.util.*;
import com.digitald4.util.*;
public class LabCo extends BillingCo{
	public final static String TABLE="tbl_lab_co";
	public final static String WHERE="WHERE LabId=?";
	public final static String SELECT="SELECT *";
	private static Hashtable<String,LabCo> companies = new Hashtable<String,LabCo>();

	public static LabCo getLabCo(int id)throws Exception{
		LabCo company=null;
		if(companies.containsKey(""+id)){
			company = companies.get(""+id);
			company.refresh();
		}else{
			company = new LabCo(id);
			if(!company.refresh())
				return null;
			companies.put(""+id,company);
		}
		return company;
	}
	public LabCo(){
	}
	private LabCo(int id){
		this.id = id;
	}
	public boolean refresh()throws Exception{
		boolean ret=true;
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(SELECT+" FROM "+TABLE+" "+WHERE);
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
}