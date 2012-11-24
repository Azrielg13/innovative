package com.digitald4.pm;
import java.sql.*;
import java.util.*;
import com.digitald4.util.*;
public class InsuranceCo extends BillingCo{
	public final static String TABLE="tbl_insurance_co";
	public final static String WHERE="WHERE insuranceId=?";
	public final static String SELECT="SELECT "+TABLE+".*, "+User.INSURANCE_CO+" AS typeid";
	private static Hashtable<String,InsuranceCo> companies = new Hashtable<String,InsuranceCo>();
	private BillingCo billingCo;
	private LabCo labCo;
	private String labCode="";
	private String mailIns="";

	public static InsuranceCo getInsuranceCo(int id)throws Exception{
		InsuranceCo company=null;
		if(companies.containsKey(""+id)){
			company = companies.get(""+id);
			company.refresh();
		}else{
			company = new InsuranceCo(id);
			if(!company.refresh())
				return null;
			companies.put(""+id,company);
		}
		return company;
	}
	public InsuranceCo(){
	}
	private InsuranceCo(int id){
		this.id = id;
	}
	public BillingCo getBillingCo(){
		if(billingCo == null)
			return new BillingCo();
		return billingCo;
	}
	public void setBillingCo(BillingCo billingCo)throws Exception{
		setProp("billingId",billingCo);
		this.billingCo = billingCo;
	}
	public LabCo getLabCo(){
		if(labCo == null)
			return new LabCo();
		return labCo;
	}
	public void setLabCo(LabCo labCo)throws Exception{
		setProp("labId",labCo);
		this.labCo = labCo;
	}
	public String getLabCode(){
		return labCode;
	}
	public void setLabCode(String labCode)throws Exception{
		setProp("Lab_Code",labCode);
		this.labCode = labCode;
	}
	public String getMailIns(){
		return mailIns;
	}
	public void setMailIns(String mailIns)throws Exception{
		setProp("insructions",mailIns);
		this.mailIns = mailIns;
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
	public void refresh(ResultSet rs)throws Exception{
		super.refresh(rs);
		billingCo = BillingCo.getBillingCo(rs.getInt("billingId"));
		labCo = LabCo.getLabCo(rs.getInt("labId"));
		labCode = rs.getString("Lab_Code");
		mailIns = rs.getString("instructions");
	}
	public String toString(){
		return getName()+" - "+address.getCity()+", "+address.getState();
	}
	public void setProp(String col, DD4Object value)throws Exception{
		if(value == null)
			setProp(col,(String)null);
		setProp(col,value.getId());
	}
	public void setProp(String col, String value)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setString(1,value);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public void setProp(String col, int value)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setInt(1,value);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public void setProp(String col, double value)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setDouble(1,value);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public void setProp(String col, boolean value)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? "+WHERE);
			ps.setBoolean(1,value);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
}