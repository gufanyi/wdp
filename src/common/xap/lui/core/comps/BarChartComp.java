package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.GenericChartData;
import xap.lui.core.dataset.MarkLineData;
import xap.lui.core.dataset.MarkLineData.MarkLineDataType;
import xap.lui.core.dataset.MarkPointData;
import xap.lui.core.dataset.SeriesDataOnlyData;
import xap.lui.core.echar.Axis;
import xap.lui.core.echar.Legend;
import xap.lui.core.echar.LineSeries;
import xap.lui.core.echar.MarkLine;
import xap.lui.core.echar.MarkPoint;
import xap.lui.core.echar.Option;
import xap.lui.core.echar.Title;
import xap.lui.core.echar.Tooltip;
import xap.lui.core.render.PCBarChartCompRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;

@XmlRootElement(name = "BarChart")
@XmlAccessorType(XmlAccessType.NONE)
public class BarChartComp extends ChartBaseComp {
	private static final long serialVersionUID = 8689895704016937272L;

	@JSONField(serialize = false)
	private PCBarChartCompRender render = null;

	public PCBarChartCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCBarChartCompRender(this));
		}
		return render;

	}

	@Override
	public void updateOption() {
		this.getRender().updateProp();
	}

	public Option getDemoOption0() {
		GenericChartData chartData = new GenericChartData();
		chartData.setLegend_data(new String[] { "蒸发量", "降水量" });
		chartData.setxAxis_data(new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" });
		SeriesDataOnlyData seriesData0 = new SeriesDataOnlyData();
		{

			seriesData0.setSeries_data(new String[] { "2.0", "4.9", "7.0", "23.2", "25.6", "76.7", "135.6", "162.2", "32.6", "20.0", "6.4", "3.3" });
			MarkPointData martPointData0 = new MarkPointData();
			martPointData0.setType("max");
			martPointData0.setName("最大值");
			MarkPointData martPointData1 = new MarkPointData();
			martPointData1.setType("min");
			martPointData1.setName("最小值");
			seriesData0.setMarkpoint_data(new MarkPointData[] { martPointData0, martPointData1 });
			MarkLineData markLineData = new MarkLineData();
			markLineData.setType(MarkLineDataType.average);
			markLineData.setName("平均值");
			seriesData0.setMarkline_data(new MarkLineData[] { markLineData });
		}

		SeriesDataOnlyData seriesData1 = new SeriesDataOnlyData();
		{
			seriesData1.setSeries_data(new String[] { "2.6", "5.9", "9.0", "26.4", "28.7", "70.7", "175.6", "182.2", "48.7", "18.8", "6.0", "2.3" });
			MarkPointData martPointData0 = new MarkPointData();
			martPointData0.setName("年最高");
			martPointData0.setValue("182.2");
			martPointData0.setxAxis("7");
			martPointData0.setyAxis("183");

			MarkPointData martPointData1 = new MarkPointData();
			martPointData1.setName("年最低");
			martPointData1.setValue("2.3");
			martPointData1.setxAxis("11");
			martPointData1.setyAxis("3");

			seriesData1.setMarkpoint_data(new MarkPointData[] { martPointData0, martPointData1 });
			MarkLineData markLineData = new MarkLineData();
			markLineData.setType(MarkLineDataType.average);
			markLineData.setName("平均值");
			seriesData1.setMarkline_data(new MarkLineData[] { markLineData });
		}

		Option option = new Option();
		Title title = new Title();
		title.setText("某地区蒸发量和降水量");
		title.setSubtext("纯属虚构");
		option.setTitle(title);
		Tooltip toolTip = new Tooltip();
		toolTip.setTrigger("axis");
		option.setTooltip(toolTip);
		Legend legend = new Legend();
		legend.setData(chartData.getLegend_data());
		option.setLegend(legend);
		option.setCalculable(true);
		{
			{
				Axis xAxis = new Axis();
				xAxis.setType("category");
				xAxis.setBoundaryGap0(false);
				xAxis.setData(chartData.getxAxis_data());
				option.setXAxis(new Axis[] { xAxis });
			}
			{
				Axis yAxis = new Axis();
				yAxis.setType("value");
				option.setYAxis(new Axis[] { yAxis });
			}

		}

		LineSeries series0 = new LineSeries();
		{
			series0.setName("蒸发量");
			series0.setType("bar");
			series0.setData(seriesData0.getSeries_data());
			MarkPoint markPoint = new MarkPoint();
			markPoint.setData(seriesData0.getMarkpoint_data());
			series0.setMarkPoint(markPoint);
			MarkLine markLine = new MarkLine();
			markLine.setData(seriesData0.getMarkline_data());
			series0.setMarkLine(markLine);
		}
		LineSeries series1 = new LineSeries();
		{
			series1.setName("降水量");
			series1.setType("bar");
			series1.setData(seriesData1.getSeries_data());
			MarkPoint markPoint = new MarkPoint();
			markPoint.setData(seriesData1.getMarkpoint_data());
			series1.setMarkPoint(markPoint);
			MarkLine markLine = new MarkLine();
			markLine.setData(seriesData1.getMarkline_data());
			series1.setMarkLine(markLine);
		}
		option.setSeries(new LineSeries[] { series0, series1 });
		return option;
	}
}
