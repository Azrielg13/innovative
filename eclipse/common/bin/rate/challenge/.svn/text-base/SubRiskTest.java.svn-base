//package rate.challenge;
//
//
//import java.sql.SQLException;
//
//import com.sce.esp.log.EspLogger;
//import com.sce.esp.object.ESPTestCase;
//import com.sce.esp.object.model.SubYear;
//import com.sce.esp.util.db.PDBConnection;
//
//
//public class SubRiskTest extends ESPTestCase{
//	private static final int PLANYEAR = 2012;
//	private static final int SUB_ID = 330;
//
//	public void setUp(){
//		PDBConnection.getInstance().enable();
//
//	}
//	
//	public void testSubRisk() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk());
//	}
//	
//	//getLoadAtRisk()
//	public void testGetLoadAtRisk() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getLoadAtRisk());
//		EspLogger.notice(this, sy.getSubRisk().getLoadAtRisk());
//	}
//	
//	//getAreaLoadAtRisk
//	public void testGetAreaLoadAtRisk() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getAreaLoadAtRisk());
//		EspLogger.notice(this, sy.getSubRisk().getAreaLoadAtRisk());
//	}
//	
//	public void testGetHottestDuct() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getHottestDuct());
//		EspLogger.notice(this, sy.getSubRisk().getHottestDuct());
//	}
//	
//	//getHottestDuctTemp
//	public void testGetHottestDuctTemp() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getHottestDuctTemp());
//		EspLogger.notice(this, sy.getSubRisk().getHottestDuctTemp());
//	}
//	
//	//getN1LoadAtRisk
//	public void testGetN1LoadAtRisk() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getN1LoadAtRisk());
//		EspLogger.notice(this, sy.getSubRisk().getN1LoadAtRisk());
//	}
//	
//	//getLoadAtRiskWeighted
//	public void testGetLoadAtRiskWeighted() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getLoadAtRiskWeighted());
//		EspLogger.notice(this, sy.getSubRisk().getLoadAtRiskWeighted());
//	}
//	
//	//getN1LoadAtRiskWeighted
//	public void testGetN1LoadAtRiskWeighted() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getN1LoadAtRiskWeighted());
//		EspLogger.notice(this, sy.getSubRisk().getN1LoadAtRiskWeighted());
//	}
//	
//	//getAreaLoadAtRiskWeighted
//	public void tetAreaLoadAtRiskWeighted() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getAreaLoadAtRiskWeighted());
//		EspLogger.notice(this, sy.getSubRisk().getAreaLoadAtRiskWeighted());
//	}
//	
//	//getLoadAtRiskDuration
//	public void testGetLoadAtRiskDuration() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getHottestDuct());
//		EspLogger.notice(this, sy.getSubRisk().getLoadAtRiskDuration());
//	}
//	
//	//getAreaLoadAtRiskDuration
//	public void testGetAreaLoadAtRiskDuration() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getAreaLoadAtRiskDuration());
//		EspLogger.notice(this, sy.getSubRisk().getAreaLoadAtRiskDuration());
//	}
//	
//	//getLoadAtRiskValue
//	public void testGetLoadAtRiskValue() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getLoadAtRiskValue());
//		EspLogger.notice(this, sy.getSubRisk().getLoadAtRiskValue());
//	}
//	
//	//getAreaLoadAtRiskValue
//	public void testGetAreaLoadAtRiskValue() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getAreaLoadAtRiskValue());
//		EspLogger.notice(this, sy.getSubRisk().getAreaLoadAtRiskValue());
//	}
//	
//	//getTotalLoadAtRiskWeighted
//	public void testGetTotalLoadAtRiskWeighted() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getTotalLoadAtRiskWeighted());
//		EspLogger.notice(this, sy.getSubRisk().getTotalLoadAtRiskWeighted());
//	}
//	
//	public void testGetAreaReserveCktLimited() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getAreaReserveCktLimited());
//		EspLogger.notice(this, sy.getSubRisk().getAreaReserveCktLimited());
//	}
//	
//	public void testGetAreaReserveCktLimited2ndLevel() throws SQLException{
//		SubYear sy = SubYear.getInstance(PLANYEAR, 0, SUB_ID, 2014);
//		assertNotNull(sy.getSubRisk().getAreaReserveCktLimited2ndLevel());
//		EspLogger.notice(this, sy.getSubRisk().getAreaReserveCktLimited2ndLevel());
//	}
//
//
////	case 45: return Math.round(sub.getYear(simId,year).getSubRisk().getAreaReserveCktLimited() * sub.getMVAFactor()); // Area Reserve							
////	case 46: return Math.round(sub.getYear(simId,year).getSubRisk().getAreaReserveHCCktLimited() * sub.getMVAFactor()); // Area Reserve HC							
////	case 47: return Math.round(sub.getYear(simId,year).getSubRisk().getAreaReserveLCCktLimited() * sub.getMVAFactor()); // Area Reserve LC
////	case 48: return Math.round(sub.getYear(simId,year).getSubRisk().getAreaReserveCktLimited2ndLevel() * sub.getMVAFactor()); // Area Reserve							
////	case 49: return Math.round(sub.getYear(simId,year).getSubRisk().getAreaReserveHCCktLimited2ndLevel() * sub.getMVAFactor()); // Area Reserve HC							
////	case 50: return Math.round(sub.getYear(simId,year).getSubRisk().getAreaReserveLCCktLimited2ndLevel() * sub.getMVAFactor()); // Area Reserve LC
//
//	
//}
//
////CREATE TABLE "MDI"."MDI252_CKT_TIE"
////(
////  "PLANYEAR"    NUMBER(4,0) NOT NULL ENABLE,
////  "ID" NUMBER(9,0) NOT NULL ENABLE,
////  "EFF_DATE" DATE,
////  "FROM_CKT_ID" NUMBER(9,0),
////  "TO_CKT_ID" NUMBER(9,0),
////  "CKT_SWT_TYPE_ID" NUMBER(9,0),
////  "SWT_NUM" VARCHAR2(162),
////  "ENABLED" NUMBER(1,0),
////  "MAX_XFRS" FLOAT(24),
////  "LIMITING_FACTOR"  VARCHAR2(128),
////  "CABLE"  VARCHAR2(32),
////  "NOTE"  VARCHAR2(128),
////  "INSERT_TS" DATE,
////  "INSERT_USER_ID" NUMBER(9,0),
////  "MODIFIED_TS" DATE,
////  "MODIFIED_USER_ID" NUMBER(9,0),
////  "DELETED_TS" DATE,
////  "DELETED_USER_ID" NUMBER(9,0),
////  CONSTRAINT "MDI252_PK" PRIMARY KEY ("PLANYEAR", "ID") ,
////  CONSTRAINT "MDI252_FK2" FOREIGN KEY ("PLANYEAR","FROM_CKT_ID") REFERENCES "MDI"."MDI220_CKT" ("PLANYEAR","CKT_ID") ENABLE,
////  CONSTRAINT "MDI252_FK3" FOREIGN KEY ("PLANYEAR","TO_CKT_ID") REFERENCES "MDI"."MDI220_CKT" ("PLANYEAR","CKT_ID") ENABLE,
////  CONSTRAINT "MDI252_FK4" FOREIGN KEY ("INSERT_USER_ID") REFERENCES "MDI"."MDI000_USER" ("USERNAME_ID") ENABLE,
////  CONSTRAINT "MDI252_FK5" FOREIGN KEY ("MODIFIED_USER_ID") REFERENCES "MDI"."MDI000_USER" ("USERNAME_ID") ENABLE,
////  CONSTRAINT "MDI252_FK6" FOREIGN KEY ("DELETED_USER_ID") REFERENCES "MDI"."MDI000_USER" ("USERNAME_ID") ENABLE,
////  CONSTRAINT "MDI252_FKI" FOREIGN KEY ("PLANYEAR") REFERENCES "MDI"."MDI010_DEPARTMENT" ("PLANYEAR") ON
////DELETE CASCADE ENABLE
////);
////grant select on MDI252_CKT_TIE to mdi_r_inquiry;
////grant insert,update,delete on MDI252_CKT_TIE to mdi_r_user;
//
//
