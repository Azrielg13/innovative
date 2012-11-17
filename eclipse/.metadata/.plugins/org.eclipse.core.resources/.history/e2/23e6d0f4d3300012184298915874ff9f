package reports;

import java.awt.BasicStroke;
import java.awt.Color;

import net.sf.jasperreports.components.charts.AbstractChartCustomizer;
import net.sf.jasperreports.components.charts.ChartComponent;

import org.jfree.chart.JFreeChart;

public class SpiderChartCustomizer extends AbstractChartCustomizer
{

	public void customize(JFreeChart chart, ChartComponent chartComponent)
	{
		chart.getPlot().setOutlineVisible(true);
		chart.getPlot().setOutlinePaint(new Color(0,0,255));
		chart.getPlot().setOutlineStroke(new BasicStroke(1f));
	}
}