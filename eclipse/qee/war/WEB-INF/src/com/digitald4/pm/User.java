package com.digitald4.pm;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.digitald4.util.*;
public class User extends DD4Object implements Comparable{
	public final static String SELECT="SELECT *, type+0 AS typeId, AES_DECRYPT(ssn,?) AS ssn";
	public final static String TABLE="tbl_user";
	public final static String WHERE="WHERE userid=?";
	public final static int ADMIN=1;
	public final static int STAFF=2;
	public final static int AGENT=3;
	public final static int EXAMINER=4;
	public final static int CLIENT=5;
	public final static int INSURANCE_CO=6;
	public final static String KEY="e4wqiwjrukjdsgfs";
	protected int id;
	protected String email="";
	protected String firstName="";
	protected String lastName="";
	protected String phoneNo="";
	protected String altPhone1="";
	protected String altPhone2="";
	protected String faxNo="";
	protected String ssn="";
	protected Address address = new Address();
	protected String picture="";
	protected int type=-1;
	protected boolean hide;
	private Vector<Exam> exams;
	protected static Hashtable<String,User> users = new Hashtable<String,User>();

	public static User getUser(int id) throws Exception{
		User user=null;
		if(users.containsKey(""+id)){
			user = users.get(""+id);
			user.refresh();
		}else{
			user = new User(id);
			if(!user.refresh()){
				System.out.println("Could not refresh user");
				return null;
			}
			users.put(""+id,user);
		}
		return user;
	}
	public User(){
	}
	protected User(int id){
		this.id = id;
	}
	protected User(int id, int type){
		this.id = id;
		this.type = type;
	}
	public boolean isHidden(){
		return hide;
	}
	public void setHidden(boolean hide)throws Exception{
		setProp("hide",hide);
		this.hide = hide;
	}
	public String[] getAgentInfo(InsuranceCo ins)throws Exception{
		String[] ac = new String[3];
		if(ins != null){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT agentCode, agency, agencyCode FROM tbl_client WHERE aUserId=? AND insuranceId=? AND agentCode IS NOT NULL AND agentCode != '' ORDER BY last_update DESC LIMIT 1");
			ps.setInt(1,id);
			ps.setInt(2,ins.getId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ac[0] = rs.getString(1);
				ac[1] = rs.getString(2);
				ac[2] = rs.getString(3);
			}
			rs.close();
			ps.close();
			con.close();
		}
		return ac;
	}
	public boolean refresh()throws Exception{
		boolean ret=true;
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(SELECT+" FROM "+TABLE+" "+WHERE);
		ps.setString(1,KEY);
		ps.setInt(2,id);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			refresh(rs);
		else{
			System.out.println("No next");
			ret = false;
		}
		rs.close();
		ps.close();
		con.close();
		return ret;
	}
	public void refresh(ResultSet rs)throws Exception{
		type = rs.getInt("typeid");
		email = rs.getString("email");
		faxNo = rs.getString("fax_no");
		firstName = rs.getString("first_name");
		lastName = rs.getString("last_name");
		phoneNo = rs.getString("phone_no");
		altPhone1 = rs.getString("alt_phone_no1");
		altPhone2 = rs.getString("alt_phone_no2");
		ssn = rs.getString("ssn");
		picture = rs.getString("picture");
		address = new Address(rs.getString("address1"),rs.getString("address2"),rs.getString("city"),rs.getString("state"),rs.getString("zip_code"));
		hide = rs.getBoolean("hide");
	}
	public boolean setField(String field, String value)throws Exception{
		switch(field.charAt(0)){
			case 'e': setEmail(value); return true;
			case 'f': if(field.charAt(1) == 'a'){
						setFaxNo(value); return true;
					}else if(field.charAt(1) == 'i'){
						setFirstName(value); return true;
					}
					break;
			case 'h': setHidden(value.charAt(0) != 'f' && value.charAt(0) != '0'); return true;
			case 'l': setLastName(value); return true;
			case 'p': if(field.charAt(1) == 'h'){
						setPhoneNo(value); return true;
					}else if(field.charAt(1) == 'i'){
						setPicture(value); return true;
					}
					break;
			case 't': setType(Integer.parseInt(value)); return true;
		}
		return false;
	}
	public void setId(int id){
		this.id = id;
	}
	public void setPassword(String pass)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET password=MD5(?) WHERE UserId=?");
			ps.setString(1,pass);
			ps.setInt(2,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public void setAltPhone1(String altPhone1)throws Exception{
		setProp("alt_phone_no1",altPhone1);
		this.altPhone1 = altPhone1;
	}
	public String getAltPhone1(){
		return altPhone1;
	}
	public void setAltPhone2(String altPhone2)throws Exception{
		setProp("alt_phone_no2",altPhone2);
		this.altPhone2 = altPhone2;
	}
	public String getAltPhone2(){
		return altPhone2;
	}
	public void setSSN(String ssn)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET ssn=AES_ENCRYPT(?,?) WHERE UserId=?");
			ps.setString(1,ssn);
			ps.setString(2,KEY);
			ps.setInt(3,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
		this.ssn = ssn;
	}
	public String getSSN(){
		return ssn;
	}
	public void setEmail(String email)throws Exception{
		setProp("email",email);
		this.email = email;
	}
	public void setFirstName(String firstName)throws Exception{
		setProp("first_name",firstName);
		this.firstName = firstName;
	}
	public void setLastName(String lastName)throws Exception{
		setProp("last_name",lastName);
		this.lastName = lastName;
	}
	public void setPhoneNo(String phoneNo)throws Exception{
		setProp("phone_no",phoneNo);
		this.phoneNo = phoneNo;
	}
	public void setFaxNo(String faxNo)throws Exception{
		setProp("fax_no",faxNo);
		this.faxNo = faxNo;
	}
	public void setPicture(String picture)throws Exception{
		setProp("picture",picture);
		this.picture=picture;
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
	public void setType(int type)throws Exception{
		setProp("type",type);
		this.type=type;
	}
	public Vector<Exam> getExams()throws Exception{
		if(exams == null)
			refreshExams();
		return exams;
	}
	public void refreshExams()throws Exception{
		if(exams == null)
			exams = new Vector<Exam>();
		exams.removeAllElements();
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT tbl_exam_type.*, tbl_examiner_exam.*, null finish_date, null note, 0 status FROM tbl_examiner_exam INNER JOIN tbl_exam_type USING(examId) WHERE userId=? && tbl_exam_type.hide=0 && tbl_examiner_exam.hide=0");
		ps.setInt(1,id);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
			exams.add(new Exam(rs.getInt("examId"),rs));
		rs.close();
		ps.close();
		con.close();
	}
	public int getId(){
		return id;
	}
	public String getEmail(){
		return email;
	}
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getPhoneNo(){
		return phoneNo;
	}
	public String getFaxNo(){
		return faxNo;
	}
	public String getPicture(){
		return picture;
	}
	public Address getAddress(){
		return address;
	}
	public int getType(){
		return type;
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
	public void setDate(String col, String format, String date)throws Exception{
		if(id > 0){
			if(date == null || date.length() == 0){
				setProp(col,(String)null);
				return;
			}
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=STR_TO_DATE(?,?) WHERE UserId=?");
			ps.setString(1,date);
			ps.setString(2,format);
			ps.setInt(3,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
	public String toString(){
		return lastName+((firstName==null || firstName.length()==0)?"":", "+firstName);
	}
	public boolean isGood(){
		return (email != null && email.length() > 0 && firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0 && type > -1 && phoneNo != null && phoneNo.length() > 0);
	}
	public int compareTo(Object o){
		int ret=0;
		if(o instanceof User){
			User user = (User)o;
			ret = toString().compareTo(user.toString());
			if(ret == 0)
				ret = (getType() < user.getType())?-1:(getType() > user.getType())?1:0;
			if(ret == 0)
				ret = (getId() < user.getId())?-1:(getId() > user.getId())?1:0;
		}
		return ret;
	}
}
