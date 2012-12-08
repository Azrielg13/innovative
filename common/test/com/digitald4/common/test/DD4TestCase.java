package com.digitald4.common.test;

import org.junit.Test;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.log.EspLogger;


public class DD4TestCase{
	static{
		try {
			EspLogger.init(false, EspLogger.LEVEL.MESSAGE);
			EntityManagerHelper.init("driver", "url", "mayfiee", "edison");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void PrintSQLStats(){
		EspLogger.message(this, "******************************** SQL Stats *******************************************");
//		Connection con=null;
//		try {
//			con = PDBConnection.getInstance().getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if(con instanceof StatsConnection)
//			for(StatsSQLImp sql:((StatsConnection)con).getTimedSQLStatements())
//				EspLogger.message(this, sql.getReportOutput());
	}
}
