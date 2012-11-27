package com.digitald4.common.tools;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.TreeSet;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class UMLClass implements Comparable<UMLClass>{
	private static Hashtable<String,UMLClass> classes = new Hashtable<String,UMLClass>();
	private String name;
	private String superClass;
	private String tablePrefix;
	private String desc;
	private String selectRole;
	private String insertRole;
	private String updateRole;
	private String deleteRole;
	private ArrayList<UMLAttribute> attributes = new ArrayList<UMLAttribute>();
	private ArrayList<UMLReference> references = new ArrayList<UMLReference>();
	private boolean processed;
	public final static String STANDARD_COLUMNS=""
			+"\tINSERT_TS DATE,\n"
			+"\tINSERT_USER_ID NUMBER(9,0),\n"
			+"\tMODIFIED_TS DATE,\n"
			+"\tMODIFIED_USER_ID NUMBER(9,0),\n"
			+"\tDELETED_TS DATE,\n"
			+"\tDELETED_USER_ID NUMBER(9,0)";
	public final static String STANDARD_CONSTRAINTS=""
			+"\tCONSTRAINT @TablePrefix_FK10 FOREIGN KEY (INSERT_USER_ID) REFERENCES MDI.MDI000_USER(USERNAME_ID) ON DELETE SET NULL,\n"
			+"\tCONSTRAINT @TablePrefix_FK11 FOREIGN KEY (MODIFIED_USER_ID) REFERENCES MDI.MDI000_USER(USERNAME_ID) ON DELETE SET NULL,\n"
			+"\tCONSTRAINT @TablePrefix_FK12 FOREIGN KEY (DELETED_USER_ID) REFERENCES MDI.MDI000_USER(USERNAME_ID) ON DELETE SET NULL";
	public final static String AUDIT_TRIGGER="CREATE OR REPLACE TRIGGER @PREFIX_T_DUI AFTER INSERT OR UPDATE OR DELETE ON MDI.@TABLE\n" 
			+"FOR EACH ROW\n"
			+"BEGIN\n"
			+"\tIF(sys_context('USERENV','CLIENT_IDENTIFIER') IS NULL OR sys_context('USERENV','CLIENT_IDENTIFIER') != 'NON LOGGER') THEN\n"
			+"\t\tIF INSERTING THEN\n"
			+"\t\t\tINSERT INTO MDI.MDI005_AUDIT(transaction_id,trans_timestamp,session_id,table_name,\n"
			+"\t\t\t\trow_key,\n"
			+"\t\t\t\ttrans_type,username,host)\n"
			+"\t\t\t\tVALUES(MDI.MDI005_SEQ.NEXTVAL,sysdate,TO_NUMBER(sys_context('userenv','sessionid')),'@TABLE',\n"
			+"\t\t\t\t@NEWID,\n"
			+"\t\t\t\t1,user,sys_context('userenv','host'));\n"
			+"\t\tELSIF UPDATING THEN\n"
			+"\t\t\tINSERT INTO MDI.MDI005_AUDIT(transaction_id,trans_timestamp,session_id,table_name,\n"
			+"\t\t\t\trow_key,\n"
			+"\t\t\t\ttrans_type,username,host)\n"
			+"\t\t\t\tVALUES(MDI.MDI005_SEQ.NEXTVAL,sysdate,TO_NUMBER(sys_context('userenv','sessionid')),'@TABLE',\n"
			+"\t\t\t\t@NEWID,\n"
			+"\t\t\t\t2,user,sys_context('userenv','host'));\n"
			+"\t\tELSIF DELETING THEN\n"
			+"\t\t\tINSERT INTO MDI.MDI005_AUDIT(transaction_id,trans_timestamp,session_id,table_name,\n"
			+"\t\t\t\trow_key,\n"
			+"\t\t\t\ttrans_type,username,host)\n"
			+"\t\t\t\tVALUES(MDI.MDI005_SEQ.NEXTVAL,sysdate,TO_NUMBER(sys_context('userenv','sessionid')),'@TABLE',\n"
			+"\t\t\t\t@OLDID,\n"
			+"\t\t\t\t3,user,sys_context('userenv','host'));\n"
			+"\t\tEND IF;\n"
			+"\tEND IF;\n"
			+"END;\n"
			+"/\n";
	public UMLClass(String name){
		setName(name);
		addClass(this);
	}
	public UMLClass(Element e){
		setName(e.getAttributeValue("name"));
		setSuperClass(e.getAttributeValue("extends"));
		setTablePrefix(e.getAttributeValue("tableprefix"));
		setSelectRole(e.getAttributeValue("selectrole"));
		setInsertRole(e.getAttributeValue("insertrole"));
		setUpdateRole(e.getAttributeValue("updaterole"));
		setDeleteRole(e.getAttributeValue("deleterole"));
		setDesc(e.getText());
		for(Object o:e.getChildren("ATTRIBUTE"))
			addAttribute(new UMLAttribute(this,(Element)o));
		for(Object o:e.getChildren("REFERENCE"))
			addReference(new UMLReference(this,(Element)o));
		if((getSuperClass()==null || !getSuperClass().equals("Sub")) && !getName().equalsIgnoreCase("Org")){
			addReference(new UMLReference(this,"User","_INSERTUSERID",false,"INSERT_USER_ID","USERNAME_ID"));
			addReference(new UMLReference(this,"User","_MODIFIEDUSERID",false,"MODIFIED_USER_ID","USERNAME_ID"));
			addReference(new UMLReference(this,"User","_DELETEDUSERID",false,"DELETED_USER_ID","USERNAME_ID"));
		}
		addClass(this);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDBName() {
		return getName().toUpperCase().replaceAll(" ", "_");
	}
	public String getSuperClass() {
		return superClass;
	}
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public String getSelectRole() {
		if(selectRole==null)
			return "MDI_R_INQUIRY";
		return selectRole;
	}
	public void setSelectRole(String selectRole) {
		this.selectRole = selectRole;
	}
	public String getInsertRole() {
		if(insertRole==null)
			return "MDI_R_USER";
		return insertRole;
	}
	public void setInsertRole(String insertRole) {
		this.insertRole = insertRole;
	}
	public String getUpdateRole() {
		if(updateRole==null)
			return "MDI_R_USER";
		return updateRole;
	}
	public void setUpdateRole(String updateRole) {
		this.updateRole = updateRole;
	}
	public String getDeleteRole() {
		if(deleteRole==null)
			return "MDI_R_USER";
		return deleteRole;
	}
	public void setDeleteRole(String deleteRole) {
		this.deleteRole = deleteRole;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDBTable(){
		return (getTablePrefix()!=null?getTablePrefix()+"_":"")+getDBName();
	}
	public ArrayList<UMLAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<UMLAttribute> attributes) {
		this.attributes = attributes;
	}
	public Collection<UMLReference> getParentReferences() {
		return references;
	}
	public void setReferences(ArrayList<UMLReference> references) {
		this.references = references;
	}
	public void addAttribute(UMLAttribute umlAttribute) {
		getAttributes().add(umlAttribute);
	}
	public void addReference(UMLReference umlReference) {
		getParentReferences().add(umlReference);
	}
	public String toString(){
		return getName();
	}
	public static void addClass(UMLClass c){
		classes.put(c.getName().toUpperCase(), c);
	}
	public static Collection<UMLClass> getClasses(){
		return new TreeSet<UMLClass>(classes.values());
	}
	public static UMLClass findClass(String name){
		return classes.get(name.toUpperCase());
	}
	public Collection<UMLReference> getChildReferences(){
		ArrayList<UMLReference> refs = new ArrayList<UMLReference>();
		for(UMLClass c:getClasses())
			for(UMLReference ref:c.getParentReferences())
				if(ref.getRefClass().equalsIgnoreCase(getName()))
					refs.add(ref);
		return refs;
	}
	private static String undo = "";
	public static String getUndo(){
		return "/* Undo Commands\n"+undo+"*/";
	}
	private String getDBCreation(String schema) {
		StringBuffer ta = new StringBuffer();
		String seq="";
		String columns="";
		String pk="";
		String triggerId="";
		for(UMLAttribute attr:getAttributes()){
			if(attr.getSequence()!=null){
				undo="DROP SEQUENCE "+schema+"."+getTablePrefix()+"_"+attr.getSequence()+";\n"+undo;
				seq+="CREATE SEQUENCE "+schema+"."+getTablePrefix()+"_"+attr.getSequence()+" MINVALUE 1 MAXVALUE 999999999 CYCLE START WITH 1 INCREMENT BY 1 NOCACHE;\n";
				seq+="GRANT SELECT ON "+schema+"."+getTablePrefix()+"_"+attr.getSequence()+" TO "+getInsertRole()+";\n";
			}
			if(columns.length()>0)
				columns +=",\n";
			columns += "\t"+attr.getDBCreation();
			if(attr.isId()){
				if(pk.length()>0){
					pk += ",";
					triggerId+="||'-'||";
				}
				pk+=attr.getDBName();
				triggerId+="TO_CHAR(:NEW."+attr.getDBName()+")";
			}
		}
		ta.append(seq);
		undo="DROP TABLE "+schema+"."+getDBTable()+";\n"+undo;
		ta.append("CREATE TABLE "+schema+"."+getDBTable()+"(\n");
		ta.append(columns+",\n");
		ta.append(STANDARD_COLUMNS);
		if(pk.length()>0){
			ta.append(",\n\tCONSTRAINT "+getTablePrefix()+"_PK PRIMARY KEY ("+pk+")");
		}
		String fks="";
		String indexes="";
		for(UMLReference ref:getParentReferences())
			fks+=",\n\t"+ref.getDBCreation();
		for(UMLIndex index:getIndexes())
			indexes += index.getDBCreation()+"\n";
		ta.append(fks);
		//ta.append(",\n"+STANDARD_CONSTRAINTS.replaceAll("@TablePrefix", getTablePrefix()));
		ta.append("\n);\n");
		ta.append(indexes);
		ta.append("GRANT SELECT ON "+schema+"."+getDBTable()+" TO "+getSelectRole()+";\n");
		ta.append("GRANT INSERT ON "+schema+"."+getDBTable()+" TO "+getInsertRole()+";\n");
		ta.append("GRANT UPDATE ON "+schema+"."+getDBTable()+" TO "+getUpdateRole()+";\n");
		ta.append("GRANT DELETE ON "+schema+"."+getDBTable()+" TO "+getDeleteRole()+";\n");
		ta.append("/\n");
		ta.append(AUDIT_TRIGGER.replaceAll("@PREFIX", schema+"."+getTablePrefix()).replaceAll("@TABLE", getDBTable()).replaceAll("@NEWID", triggerId).replaceAll("@OLDID", triggerId.replaceAll(":NEW", ":OLD")));
		return ta.toString();
	}
	public static void save(String file) throws Exception{
		//SAXBuilder builder = new SAXBuilder();
		//Document document = builder.build(file);
		Element root = new Element("DOMAIN");
		root.setAttribute("name", "esp suite");
		for(UMLClass uc:getClasses())
			root.addContent(uc.getXMLElement());
		Document doc = new Document(root);
		FileOutputStream fos = new FileOutputStream(file);
		new XMLOutputter(Format.getPrettyFormat()).output(doc,fos);
		fos.close();
	}
	public Element getXMLElement() {
		Element e = new Element("CLASS");
		e.setAttribute("name", getName());
		e.setAttribute("extends",getSuperClass());
		e.setAttribute("tableprefix",getTablePrefix());
		e.setAttribute("selectrole",getSelectRole());
		e.setAttribute("insertrole",getInsertRole());
		e.setAttribute("updaterole",getUpdateRole());
		e.setAttribute("deleterole",getDeleteRole());
		e.setText(getDesc());
		for(UMLAttribute attr:getAttributes())
			e.addContent(attr.getXMLElement());
		for(UMLReference ref:getParentReferences())
			e.addContent(ref.getXMLElement());
		return e;
	}
	public boolean isProcessed(){
		return processed;
	}
	public Collection<UMLClass> getPreds(){
		TreeSet<UMLClass> preds = new TreeSet<UMLClass>();
		for(UMLReference ref:getParentReferences())
			if(ref.getRefUMLClass()!=this)
				preds.add(ref.getRefUMLClass());
		return preds;
	}
	public void getDBChange(DatabaseMetaData dbmd, String schema, PrintStream ps, boolean outputRelated) throws SQLException {
		if(isProcessed())return;
		for(UMLClass pred:getPreds())
			if(!pred.isProcessed())
				pred.getDBChange(dbmd, schema, ps, outputRelated);
		processed=true;
		String out="";
		ResultSet rs = dbmd.getTables(null, schema, getDBTable(), new String[]{"TABLE"});
		if(rs.next()){
			for(UMLAttribute attr:getAttributes())
				out += attr.getDBChange(dbmd,schema);
			//Drop Columns that don't exist in the XML any longer
			ResultSet rs2 = dbmd.getColumns(null, null, getDBTable(), null);
			while(rs2.next()){
				String column = rs2.getString("COLUMN_NAME");
				boolean found=true;
				if(!STANDARD_COLUMNS.contains(column)){
					found=false;
					for(UMLAttribute attr:getAttributes()){
						if(attr.getDBName().equalsIgnoreCase(column) || attr.getFormerName()!=null && attr.getDBFormerName().equalsIgnoreCase(column)){
							found=true;
							break;
						}
					}
				}
				if(!found)
					out += "--VERIFY ALTER TABLE "+schema+"."+getDBTable()+" DROP COLUMN "+column+";\n";
			}
			rs2.close();
			//Drop constraints that don't exist in the xml any longer
			for(DBForiegnKey dbfk:getDBReferences(dbmd).values()){
				boolean found=true;
				if(!dbfk.getName().endsWith("PK")){
					found=false;
					for(UMLReference ref:getParentReferences()){
						if(ref.getDBFKName().equalsIgnoreCase(dbfk.getName())){
							found=true;
							break;
						}
					}
				}
				if(!found){
					if(dbfk.getRefTable().equals("MDI000_USER"))
						out += "ALTER TABLE "+schema+"."+getDBTable()+" DROP CONSTRAINT "+dbfk.getName()+";\n";
					else
						out += "--VERIFY ALTER TABLE "+schema+"."+getDBTable()+" DROP CONSTRAINT "+dbfk.getName()+";\n";
				}
			}
			for(UMLReference ref:getParentReferences())
				out += ref.getDBChange(dbmd,schema);
			TreeSet<String> indexes = getDBIndexes(dbmd);
			for(String index:indexes){
				if(!index.contains("_U")){ //Leave Unique Constraints
					boolean found=false;
					for(UMLIndex ui:getIndexes()){
						if(ui.getDBName().equalsIgnoreCase(index)){
							found=true;
							break;
						}
					}
					if(!found)
						out += "DROP INDEX "+schema+"."+index+";\n";
				}
			}
			for(UMLIndex ui:getIndexes()){
				if(!ui.isSameAsPK())
					out += ui.getDBChange(dbmd)+"\n";
			}
		}
		else
			out += getDBCreation(schema);
		rs.close();
		out = out.trim();
		if(out.length()>0/*&& out.contains("--VERIFY")*/)
			ps.println("--"+this+"\n"+out);
		/*for(UMLReference child:getChildReferences())
			if(!child.getUmlClass().isProcessed())
				child.getUmlClass().getDBChange(dbmd, schema, ps, outputRelated);*/
	}
	public Collection<UMLAttribute> getPKAttributes() {
		ArrayList<UMLAttribute> pks = new ArrayList<UMLAttribute>();
		for(UMLAttribute att:getAttributes())
			if(att.isId())
				pks.add(att);
		return pks;
	}
	public Collection<UMLIndex> getIndexes(){
		TreeSet<UMLIndex> indexes = new TreeSet<UMLIndex>();
		for(UMLReference ref:getParentReferences()){
			if(ref.isIndexed())
				indexes.add(ref.getUMLIndex());
		}
		return indexes;
	}
	@Override
	public int compareTo(UMLClass uc) {
		if(this==uc)return 0;
		int ret = getDBTable().compareTo(uc.getDBTable());
		return ret;
	}
	private Hashtable<String,DBForiegnKey> dbFks;
	public Hashtable<String,DBForiegnKey> getDBReferences(DatabaseMetaData dbmd) throws SQLException{
		if(dbFks==null){
			dbFks = new Hashtable<String,DBForiegnKey>();
			ResultSet rs = dbmd.getCrossReference(null, null, null, null, null, getDBTable());
			while(rs.next()){
				DBForiegnKey key = dbFks.get(rs.getString("FK_NAME"));
				if(key == null){
					key = new DBForiegnKey(rs.getString("FKTABLE_NAME"),rs.getString("FK_NAME"),rs.getString("PKTABLE_NAME"));
					dbFks.put(key.getName(), key);
				}
				key.setColumn(rs.getInt("KEY_SEQ"),rs.getString("FKCOLUMN_NAME"));
				key.setRefCol(rs.getInt("KEY_SEQ"),rs.getString("PKCOLUMN_NAME"));
				key.setDeleteRule(rs.getInt("DELETE_RULE"));
			}
			rs.close();
		}
		return dbFks;
	}
	public DBForiegnKey getDBReference(DatabaseMetaData dbmd, String dbName) throws SQLException {
		return getDBReferences(dbmd).get(dbName);
	}
	private TreeSet<String> dbIndexes;
	public TreeSet<String> getDBIndexes(DatabaseMetaData dbmd) throws SQLException{
		if(dbIndexes==null){
			dbIndexes = new TreeSet<String>();
			ResultSet rs = dbmd.getIndexInfo(null, null, getDBTable(), false, true);
			while(rs.next()){
				if(rs.getString("INDEX_NAME")!=null && !rs.getString("INDEX_NAME").endsWith("PK"))
					dbIndexes.add(rs.getString("INDEX_NAME"));
			}
			rs.close();
		}
		return dbIndexes;
	}
}

