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
import xap.lui.core.echar.AxisLabel;
import xap.lui.core.echar.Legend;
import xap.lui.core.echar.LineSeries;
import xap.lui.core.echar.MarkLine;
import xap.lui.core.echar.MarkPoint;
import xap.lui.core.echar.Option;
import xap.lui.core.echar.Title;
import xap.lui.core.echar.Tooltip;
import xap.lui.core.render.PCLineChartCompRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;

@XmlRootElement(name = "LineChart")
@XmlAccessorType(XmlAccessType.NONE)
public class LineChartComp extends ChartBaseComp {
	private static final long serialVersionUID = -333800625881978264L;
	@JSONField(serialize = false)
	private PCLineChartCompRender render = null;

	public PCLineChartCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCLineChartCompRender(this));
		}
		return render;
	}

	@Override
	public void updateOption() {
		this.getRender().updateProp();
	}


	public Option getDemoOption0() {
		GenericChartData chartData = new GenericChartData();
		chartData.setLegend_data(new String[] { "最高气温", "最低气温" });
		chartData.setxAxis_data(new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" });
		SeriesDataOnlyData seriesData0 = new SeriesDataOnlyData();
		{

			seriesData0.setSeries_data(new String[] { "1", "-2", "2", "5", "3", "2", "0" });
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
			seriesData1.setSeries_data(new String[] { "1", "-2", "2", "5", "3", "2", "0" });
			MarkPointData martPointData0 = new MarkPointData();
			martPointData0.setName("周最低");
			martPointData0.setValue("-2");
			martPointData0.setxAxis("1");
			martPointData0.setyAxis("-1.5");
			seriesData1.setMarkpoint_data(new MarkPointData[] { martPointData0 });
			MarkLineData markLineData = new MarkLineData();
			markLineData.setType(MarkLineDataType.average);
			markLineData.setName("平均值");
			seriesData1.setMarkline_data(new MarkLineData[] { markLineData });
		}

		Option option = new Option();
		Title title = new Title();
		title.setText("未来一周气温变化");
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
				AxisLabel label = new AxisLabel();
				label.setFormatter("{value}°C");
				yAxis.setAxisLabel(label);
				option.setYAxis(new Axis[] { yAxis });
			}

		}

		LineSeries series0 = new LineSeries();
		{
			series0.setName("最高气温");
			series0.setType("line");
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
			series1.setName("最低气温");
			series1.setType("line");
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
