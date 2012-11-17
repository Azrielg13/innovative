package reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.math.BigDecimal;
import java.util.Date;

import com.dd4.common.reports.ReportDesign;
import com.dd4.common.reports.Templates;

import net.sf.dynamicreports.report.builder.ReportBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.chart.TimeSeriesChartBuilder;
import net.sf.dynamicreports.report.builder.column.PercentageColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.Orientation;
import net.sf.dynamicreports.report.exception.DRException;

public class MasterListSummaryDesign implements ReportDesign<MasterListSummaryData> {

	public void configureReport(ReportBuilder<?> rb, MasterListSummaryData invoiceData) throws DRException {
		//init styles
		FontBuilder  boldFont = stl.fontArialBold();
		//init columns
		TextColumnBuilder<String>     titleColumn     = col.column("Title", "Title", type.stringType()).setMinWidth(200);
		TextColumnBuilder<String>     programColumn      = col.column("Program", "Program", type.stringType()).setPrintRepeatedDetailValues(false);
		TextColumnBuilder<Date>       opDateColumn = col.column("OD", "OD", type.dateType()).setHorizontalAlignment(HorizontalAlignment.CENTER);
		TextColumnBuilder<String>    pinColumn  = col.column("PIN", "PIN", type.stringType()).setFixedWidth(40).setHorizontalAlignment(HorizontalAlignment.CENTER);
		TextColumnBuilder<BigDecimal> budgetColumn = col.column("Budget", "Budget", Templates.currencyType);
		//
		TextColumnBuilder<BigDecimal> appPriceColumn     = budgetColumn.multiply(1000).setTitle("Approved Cost").setDataType(Templates.currencyType);
		PercentageColumnBuilder       pricePercColumn = col.percentageColumn("% Allocation", appPriceColumn);
		//init groups
		ColumnGroupBuilder stateGroup = grp.group(programColumn);
		//init subtotals
		AggregationSubtotalBuilder<Number> priceAvg         = sbt.avg(appPriceColumn).setValueFormatter(Templates.createCurrencyValueFormatter("avg = "));
		AggregationSubtotalBuilder<BigDecimal> unitPriceSum = sbt.sum(budgetColumn).setLabel("Total:").setLabelStyle(Templates.boldStyle);
		AggregationSubtotalBuilder<BigDecimal> priceSum     = sbt.sum(appPriceColumn).setLabel("").setLabelStyle(Templates.boldStyle);
		//init charts
		Bar3DChartBuilder      itemChart  = cht.bar3DChart()
		                                       .setTitle("Approved Costs by Program")
		                                       .setTitleFont(boldFont)
		                                       .setOrientation(Orientation.HORIZONTAL)
		                                       .setCategory(programColumn)
		                                       .addSerie(cht.serie(appPriceColumn));
		TimeSeriesChartBuilder dateChart  = cht.timeSeriesChart()
		                                       .setTitle("3 Yr. Outlook")
		                                       .setTitleFont(boldFont)
		                                       .setFixedHeight(150)
		                                       .setTimePeriod(opDateColumn)
		                                       .addSerie(
		                                        	cht.serie(appPriceColumn));
		PieChartBuilder        stateChart = cht.pieChart()
		                                       .setTitle("Costs by Program")
		                                       .setTitleFont(boldFont)
		                                       .setFixedHeight(100)
		                                       .setShowLegend(false)
		                                       .setKey(programColumn)
		                                       .addSerie(
		                                        	cht.serie(appPriceColumn));

		//configure report
		rb.setTemplate(Templates.reportTemplate)
		  //columns
		  .columns(
				  pinColumn, titleColumn, programColumn, opDateColumn,  appPriceColumn, pricePercColumn)
		  //groups
		  .groupBy(stateGroup)
		  //subtotals
		  .subtotalsAtFirstGroupFooter(
		  	sbt.sum(budgetColumn), sbt.sum(appPriceColumn))
		  .subtotalsOfPercentageAtGroupFooter(stateGroup,
		  	sbt.percentage(appPriceColumn).setShowInColumn(pricePercColumn))
		  .subtotalsAtSummary(
		  	unitPriceSum, priceSum, priceAvg)
		  //band components
		  .title(
		  	Templates.createTitleComponent("TSPOC Project Summary","TSPOC Master List","Approved Projects"),
		  	cmp.horizontalList(
		  		itemChart, cmp.verticalList(dateChart, stateChart)),
		  	cmp.verticalGap(10))
		  .pageFooter(
		  	Templates.footerComponent);
	}
}