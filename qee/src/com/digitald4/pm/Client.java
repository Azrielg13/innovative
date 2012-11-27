package com.digitald4.pm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.digitald4.util.Address;
import com.digitald4.util.DBConnector;
import com.digitald4.util.DD4Object;
public class Client extends User{
	public final static String TABLE="tbl_client";
	public final static String WHERE="WHERE clientId=?";
	public final static String DATE_FORMAT="%m/%d/%Y";
	public final static SimpleDateFormat USER_DATE_TIME = new SimpleDateFormat("MM/dd/yy HH:mm");
	public final static SimpleDateFormat MYSQL_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat USER_DATE = new SimpleDateFormat("MM/dd/yyyy");
	public final static SimpleDateFormat MYSQL_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public final static String SELECT="SELECT status+0, AES_DECRYPT(ssn,?) AS ssn, DATE_FORMAT(dob,'"+DATE_FORMAT+"') dob, DATE_FORMAT(received_date,'"+DATE_FORMAT+"') rec, DATE_FORMAT(start_date,'"+DATE_FORMAT+"') start_date, DATE_FORMAT(end_date,'"+DATE_FORMAT+"') end_date, DATE_FORMAT(last_update,'"+DATE_FORMAT+"') last_update, "+TABLE+".*";
	private User creator;
	private User agent;
	private String agentCode="";
	private String agency="";
	private String agencyCode="";
	private User examiner;
	private InsuranceCo ins;
	private InsuranceType insType;
	private String dob="";
	private String recDate;
	private String startDate="";
	private String startTime="";
	private String targetDate;
	private String endDate;
	private String primaryLang="English";
	private String note="";
	private BillingCo billingCo;
	private LabCo labCo;
	private String labCode="";
	private String mailIns="";
	private int status;
	private String insAmount="";
	private Vector<Message> messages = new Vector<Message>();
	private Vector<Exam> exams = new Vector<Exam>();
	private static Hashtable<String,Client> clients = new Hashtable<String,Client>();
	private String lastUpdate;
	private Address address2=new Address();
	private static Vector<String> statusNames;

	public static Client getClient(int id) throws Exception{
		if(statusNames == null)
			refreshStatusNames();
		Client client=null;
		if(clients.containsKey(""+id)){
			client = clients.get(""+id);
			client.refresh();
		}else{
			client = new Client(id);
			if(!client.refresh())
				return null;
			clients.put(""+id,client);
		}
		return client;
	}
	public Client(){
		super(0,CLIENT);
	}
	protected Client(int id){
		super(id,CLIENT);
	}
	protected Client(int id, Connection con, ResultSet rs)throws Exception{
		super(id,CLIENT);
		refresh(con,rs);
	}
	public int getStatus(){
		return status;
	}
	public void setStatus(int status)throws Exception{
		setProp("status",status);
		this.status = status;
	}
	public void setStatus(String statusName)throws Exception{
		for(int s=0; s<statusNames.size(); s++){
			if(statusNames.get(s).equals(statusName)){
				setStatus(s);
				return;
			}
		}
	}
	public BillingCo getBillingCo(){
		return billingCo;
	}
	public LabCo getLabCo(){
		return labCo;
	}
	public String getLabCode(){
		return labCode;
	}
	public void setLabCode(String labCode)throws Exception{
		setProp("lab_code",labCode);
		this.labCode = labCode;
	}
	public String getMailIns(){
		return mailIns;
	}
	public void setMailIns(String mailIns)throws Exception{
		setProp("instructions",mailIns);
		this.mailIns = mailIns;
	}
	public String getStatusName(){
		if(status >= 0 || status < statusNames.size())
			return statusNames.get(status);
		return "Unknown";
	}
	public String getTargetDate(){
		return targetDate;
	}
	public static final Vector<String> getStatusNames()throws Exception{
		if(statusNames == null)
			refreshStatusNames();
		return statusNames;
	}
	public static void refreshStatusNames()throws Exception{
		if(statusNames == null)
			statusNames = new Vector<String>();
		statusNames.removeAllElements();
		statusNames.add("Not Set");
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("show columns from tbl_client");
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			if(rs.getString(1).equalsIgnoreCase("status")){
				String type = rs.getString("type");
				if(type.startsWith("enum(")){
					StringTokenizer st = new StringTokenizer(type.substring(5,type.length()-1),",");
					while(st.hasMoreTokens())
						statusNames.add(st.nextToken().replace("'",""));

				}
			}
		}
		rs.close();
		ps.close();
		con.close();
	}
	public boolean hasExam(int id){
		for(Exam exam:exams)
			if(exam.getId() == id)
				return true;
		return false;
	}
	public Exam getExam(int id){
		for(Exam exam:exams)
			if(exam.getId() == id)
				return exam;
		return null;
	}
	public Vector<Exam> getExams(){
		return exams;
	}
	public boolean refresh()throws Exception{
		boolean ret=true;
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(SELECT+" FROM "+TABLE+" "+WHERE);
		ps.setString(1,User.KEY);
		ps.setInt(2,id);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			refresh(con,rs);
		else
			ret=false;
		rs.close();
		ps.close();
		con.close();
		return ret;
	}
	public void setBillingCo(BillingCo billingCo)throws Exception{
		setProp("billingId",billingCo);
		this.billingCo = billingCo;
	}
	public void setLabCo(LabCo labCo)throws Exception{
		setProp("labId",labCo);
		this.labCo = labCo;
	}

	public void refresh(Connection con, ResultSet rs)throws Exception{
		type = User.CLIENT;

		faxNo = rs.getString("fax_no");
		firstName = rs.getString("first_name");
		lastName = rs.getString("last_name");
		phoneNo = rs.getString("phone_no");
		ssn = rs.getString("ssn");
		address = new Address(rs.getString("address1"),"",rs.getString("city1"),rs.getString("state1"),rs.getString("zip_code1"));
		address2 = new Address(rs.getString("address2"),"",rs.getString("city2"),rs.getString("state2"),rs.getString("zip_code2"));

		examiner = User.getUser(rs.getInt("eUserId"));
		altPhone1 = rs.getString("alt_phone_no1");
		altPhone2 = rs.getString("alt_phone_no2");
		creator = User.getUser(rs.getInt("cUserId"));
		agent = User.getUser(rs.getInt("aUserId"));
		agentCode = rs.getString("agentCode");
		agency = rs.getString("agency");
		agencyCode = rs.getString("agencyCode");
		note = rs.getString("note");
		//lab = rs.getString("lab");
		dob = rs.getString("dob");
		primaryLang = rs.getString("Language");
		recDate = rs.getString("rec");
		startDate = rs.getString("start_date");
		startTime = rs.getString("start_time");
		endDate = rs.getString("end_date");
		//targetDate = rs.getString("target");
		ins = InsuranceCo.getInsuranceCo(rs.getInt("insuranceId"));
		billingCo = BillingCo.getBillingCo(rs.getInt("billingId"));
		labCo = LabCo.getLabCo(rs.getInt("labId"));
		labCode = rs.getString("lab_code");
		mailIns = rs.getString("instructions");
		insAmount = rs.getString("insAmount");

		exams.removeAllElements();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM tbl_client_exam "+WHERE);
		ps.setInt(1,id);
		ResultSet rsE = ps.executeQuery();
		while(rsE.next())
			exams.add(new Exam(this,rsE.getInt("examId"),rsE));
		messages.removeAllElements();
		ps = con.prepareStatement(Message.SELECT+" FROM "+Message.TABLE+" "+WHERE+" ORDER BY last_update DESC");
		ps.setInt(1,id);
		ResultSet rsM = ps.executeQuery();
		while(rsM.next())
			messages.add(new Message(this,rsM.getInt("messageId"),rsM));
		insType = InsuranceType.getInsuranceType(rs.getInt("insTypeId"));
		lastUpdate = rs.getString("last_update");
		hide = rs.getBoolean("hide");
	}
	public boolean setField(String field, String value)throws Exception{
		switch(field.charAt(0)){
			case 'a': if(field.charAt(1) == 'g'){
						setAgent(User.getUser(Integer.parseInt(value))); return true;
					}else if(field.charAt(1) == 'l'){
						setAltPhone1(value); return true;
					}
					break;
			case 'c': setCreator(User.getUser(Integer.parseInt(value))); return true;
			case 'd': setDOB(value); return true;
			case 'e': if(field.charAt(1) == 'x'){
						setExaminer(User.getUser(Integer.parseInt(value))); return true;
					  }else if(field.charAt(1) == 'n'){
						setEndDate(value); return true;
					}break;
			case 'i': if(field.charAt(3) == 'a'){
							setInsAmount(value);
						}else{
							setIns(InsuranceCo.getInsuranceCo(Integer.parseInt(value)));
					} return true;
			case 'n': setNote(value); return true;
			//case 'l': setLab(value); return true;
			case 'p': if(field.charAt(1) == 'r'){
						setPrimaryLang(value); return true;
					}
					break;
			case 'r': setRecDate(value); return true;
			case 's': if(field.charAt(3) == 'r'){
						setStartDate(value); return true;
					 }else if(field.charAt(3) == 't'){
					 	setStatus(Integer.parseInt(value));
					}
					break;
			//case 't': setTargetDate(value); return true;
		}
		return super.setField(field,value);
	}
	public String getLastUpdate(){
		return lastUpdate;
	}
	public void setLastUpdate()throws Exception{
		String lastUpdate = MYSQL_DATE_TIME.format(Calendar.getInstance().getTimeInMillis());
		setProp("last_update",lastUpdate);
		this.lastUpdate = lastUpdate;
	}
	public String getAgentCode(){
		return agentCode;
	}
	public String getAgency(){
		return agency;
	}
	public String getAgencyCode(){
		return agencyCode;
	}
	public void setAgentCode(String agentCode)throws Exception{
		setProp("agentCode",agentCode);
		this.agentCode = agentCode;
	}
	public void setAgency(String agency)throws Exception{
		setProp("agency",agency);
		this.agency = agency;
	}
	public void setAgencyCode(String agencyCode)throws Exception{
		setProp("agencyCode",agencyCode);
		this.agencyCode = agencyCode;
	}
	public void setAddress(Address address)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET address2=?, city2=?, state2=?, zip_code2=? WHERE UserId=?");
			ps.setString(1,address2.getStreet1());
			ps.setString(2,address2.getCity());
			ps.setString(3,address2.getState());
			ps.setInt(4,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
		this.address=address;
	}
	public void setAddress2(Address address2)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET address2=?, city2=?, state2=?, zip_code2=? WHERE UserId=?");
			ps.setString(1,address2.getStreet1());
			ps.setString(2,address2.getCity());
			ps.setString(3,address2.getState());
			ps.setInt(4,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
		this.address2=address2;
	}
	public Address getAddress2(){
		return address2;
	}
	public void setExams(Vector<Exam> exams)throws Exception{
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement psD = con.prepareStatement("DELETE FROM "+Exam.TABLE+" "+WHERE);
		psD.setInt(1,id);
		psD.executeUpdate();
		PreparedStatement ps = con.prepareStatement("INSERT INTO "+Exam.TABLE+"(clientid,examid,name,description,status,note,finish_date) VALUES(?,?,?,?,?,?,?)");
		for(Exam exam:exams){
			ps.setInt(1,id);
			ps.setInt(2,exam.getId());
			ps.setString(3,exam.getName());
			ps.setString(4,exam.getDesc());
			ps.setInt(5,exam.getStatus());
			ps.setString(6,exam.getNote());
			ps.setString(7,exam.getFinishDate());
			ps.executeUpdate();
		}
		ps.close();
		con.close();
		setLastUpdate();
		this.exams = exams;
	}
	public void addMessage(String text, User user)throws Exception{
		int msgId=1;
		if(messages.size() > 0)
			msgId = messages.get(messages.size()-1).getId()+1;
		Connection con = DBConnector.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO "+Message.TABLE+"(clientid,messageid,message,userid) VALUES(?,?,?,?)");
		ps.setInt(1,id);
		ps.setInt(2,msgId);
		ps.setString(3,text);
		ps.setInt(4,user.getId());
		ps.executeUpdate();
		ps.close();
		con.close();
		setLastUpdate();
		Message msg = new Message(this,msgId,text,user);
		msg.refresh();
		messages.add(msg);
	}
	public String getInsAmount(){
		return insAmount;
	}
	public Message getMessage(int msgId){
		for(Message msg:messages)
			if(msg.getId() == msgId)
				return msg;
		return null;
	}
	public int getMessageCount(){
		return messages.size();
	}
	public Vector<Message> getMessages(){
		return messages;
	}
	public void setMessages(Vector<Message> messages)throws Exception{
		this.messages = messages;
	}
	public void setIns(InsuranceCo ins)throws Exception{
		if(ins != null)
			setProp("insuranceId",ins.getId());
		else
			setProp("insuranceId",0);
		this.ins=ins;
	}
	public InsuranceType getInsType(){
		return (insType!=null)?insType:new InsuranceType();
	}
	public void setInsType(InsuranceType insType)throws Exception{
		if(insType != null)
			setProp("insTypeId",insType.getId());
		else
			setProp("insTypeId",0);
		this.insType=insType;
	}
	public void setCreator(User user)throws Exception{
		if(user != null)
			setProp("cUserId",user.getId());
		else
			setProp("cUserId",0);
		this.creator=user;
	}
	public void setExaminer(User examiner)throws Exception{
		if(examiner != null)
			setProp("eUserId",examiner.getId());
		else
			setProp("eUserId",0);
		this.examiner=examiner;
	}
	public void setInsAmount(String insAmount)throws Exception{
		setProp("insAmount",insAmount);
		this.insAmount=insAmount;
	}
	public void setAgent(User agent)throws Exception{
		if(agent != null)
			setProp("aUserId",agent.getId());
		else
			setProp("aUserId",0);
		this.agent=agent;
	}
	public void setDOB(String dob)throws Exception{
		setDate("dob",dob);
		this.dob=dob;
	}
	public void setRecDate(String recDate)throws Exception{
		setDate("received_date",recDate);
		this.recDate = recDate;
	}
	public void setStartDate(String startDate)throws Exception{
		setDate("start_date",startDate);
		this.startDate = startDate;
	}
	public String getStartTime(){
		return startTime;
	}
	public void setStartTime(String startTime)throws Exception{
		setProp("start_time",startTime);
		this.startTime = startTime;
	}
	/*public void setTargetDate(String targetDate)throws Exception{
		setDate("target_date",targetDate);
		this.targetDate = targetDate;
	}*/
	public void setEndDate(String endDate)throws Exception{
		setDate("end_date",endDate);
		this.endDate = endDate;
	}
	public void setPrimaryLang(String primaryLang)throws Exception{
		setProp("language",primaryLang);
		this.primaryLang = primaryLang;
	}
	public void setNote(String note)throws Exception{
		setProp("note",note);
		this.note = note;
	}
	/*
	public void setLab(String lab)throws Exception{
		setProp("lab",lab);
		this.lab = lab;
	}
	*/
	public User getCreator(){
		return creator;
	}
	public User getExaminer(){
		return (examiner!=null)?examiner:new User();
	}
	public User getAgent(){
		return (agent!=null)?agent:new User();
	}
	public InsuranceCo getIns(){
		return (ins!=null)?ins:new InsuranceCo();
	}
	public String getDOB(){
		return dob;
	}
	public int getAge(){
		if(dob == null || dob.length() == 0)
			return 0;
		Calendar today = Calendar.getInstance();
		Calendar birth = Calendar.getInstance();
		birth.set(Calendar.YEAR,Integer.parseInt(dob.substring(dob.lastIndexOf('/')+1)));
		birth.set(Calendar.MONTH,Integer.parseInt(dob.substring(0,dob.indexOf('/')))-1);
		birth.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dob.substring(dob.indexOf('/')+1,dob.lastIndexOf('/'))));
		return (int)Math.round((today.getTimeInMillis()-birth.getTimeInMillis())/(1000*60*60*24*365.25));
	}
	public String getRecDate(){
		return recDate;
	}
	public String getStartDate(){
		return startDate;
	}
	//public String getTargetDate(){
	//	return targetDate;
	//}
	public String getEndDate(){
		return endDate;
	}
	public String getPrimaryLang(){
		return primaryLang;
	}
	public String getNote(){
		return note;
	}
	/*
	public String getLab(){
		return lab;
	}
	*/
	public boolean isGood(){
		return (isGood(agent) && isGood(insAmount) && isGood(firstName) && isGood(lastName) && isGood(phoneNo) && isGood(address.getCity()) && isGood(address.getState()));
	}
	public static boolean isGood(String value){
		return (value != null && value.length() > 0);
	}
	public static boolean isGood(Object value){
		return (value != null);
	}
	public static boolean isGood(int number){
		return (number > 0);
	}
	public void setProp(String col, DD4Object value)throws Exception{
		if(value == null)
			setProp(col,(String)null);
		else
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
	public void setDate(String col, String date)throws Exception{
		if(id > 0){
			Connection con = DBConnector.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE "+TABLE+" SET "+col+"=STR_TO_DATE(?,?) "+WHERE);
			ps.setString(1,date);
			ps.setString(2,DATE_FORMAT);
			ps.setInt(3,id);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
	}
}


