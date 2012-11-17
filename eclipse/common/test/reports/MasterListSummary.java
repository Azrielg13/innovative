package reports;

import com.dd4.common.reports.AbstractReportMain;



public class MasterListSummary extends AbstractReportMain<MasterListSummaryDesign, MasterListSummaryData> {
	
	@Override
	protected MasterListSummaryDesign getReportDesign() {
		return new MasterListSummaryDesign();
	}
	
	@Override
	protected MasterListSummaryData getReportData() {
		return new MasterListSummaryData();
	}
	
	public static void main(String[] args) {
		new MasterListSummary();
	}
}
