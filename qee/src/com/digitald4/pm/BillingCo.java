package com.digitald4.pm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import com.digitald4.util.Address;
import com.digitald4.util.DBConnector;
import com.digitald4.util.DD4Object;
public class BillingCo extends DD4Object{
	public final static String TABLE="tbl_billing_co";
	public final static String WHERE="WHERE billingId=?";
	public final static String SELECT="SELECT *";
	private static Hashtable<String,BillingCo> companies = new Hashtable<String,BillingCo>();
	private String name="";
	private String website="";
	private String contact="";
	private String email="";
	private String phoneNo="";
	private String faxNo="";
	protected Address address = new Address();

	public static BillingCo getBillingCo(int id)throws Exception{
		BillingCo company=null;
		if(companies.containsKey(""+id)){
			company = companies.get(""+id);
			company.refresh();
		}else{
			company = new BillingCo(id);
			if(!company.refresh())
				return null;
			companies.put(""+id,company);
		}
		return company;
	}
	public BillingCo(){
	}
	private BillingCo(int id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public Address getAddress(){
		return address;
	}
	public String getWebsite(){
		return website;
	}
	public String getContact(){
		return contact;
	}
	public String getEmail(){
		return email;
	}
	public String getPhoneNo(){
		return phoneNo;
	}
	public String getFaxNo(){
		return faxNo;
	}
	public void setWebsite(String website)throws Exception{
		setProp("website",website);
		this.website = website;
	}
	public void setContact(String contact)throws Exception{
		setProp("contact",contact);
		this.contact = contact;
	}
	public void setEmail(String email)throws Exception{
		setProp("email",email);
		this.email = email;
	}
	public void setPhoneNo(String phoneNo)throws Exception{
		setProp("phone_no",phoneNo);
		this.phoneNo = phoneNo;
	}
	public void setFaxNo(String faxNo)throws Exception{
		setProp("fax_no",faxNo);
		this.faxNo = faxNo;
	}
	public void setAddress(Address address)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET address1=?, address2=?, city=?, state=?, zip_code=? WHERE UserId=?");
			ps.setString(1,address.getStreet1());
			ps.setString(2,address.getStreet2());
			ps.setString(3,address.getCity());
			ps.setString(4,address.getState());
			ps.setString(5,address.getZip());
			ps.setInt(6,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
		this.address=address;
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
		email = rs.getString("email");
		faxNo = rs.getString("fax_no");
		contact = rs.getString("contact");
		phoneNo = rs.getString("phone_no");
		address = new Address(rs.getString("address1"),rs.getString("address2"),rs.getString("city"),rs.getString("state"),rs.getString("zip_code"));
		name = rs.getString("name");
		website = rs.getString("website");
	}
	public void setProp(String col, String value)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? WHERE UserId=?");
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
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? WHERE UserId=?");
			ps.setInt(1,value);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public void setProp(String col, boolean value)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=? WHERE UserId=?");
			ps.setBoolean(1,value);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public boolean setField(String field, String value)throws Exception{
		return false;
	}
	public String toString(){
		return name;
	}
}