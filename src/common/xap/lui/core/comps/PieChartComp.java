package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.GenericChartData;
import xap.lui.core.dataset.SeriesDataWithName;
import xap.lui.core.echar.Axis;
import xap.lui.core.echar.Legend;
import xap.lui.core.echar.Option;
import xap.lui.core.echar.PieSeries;
import xap.lui.core.echar.Title;
import xap.lui.core.echar.Tooltip;
import xap.lui.core.render.PCPieChartCompRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;

@XmlRootElement(name = "PieChart")
@XmlAccessorType(XmlAccessType.NONE)
public class PieChartComp extends ChartBaseComp {
	private static final long serialVersionUID = 1066086511507304739L;

	@JSONField(serialize = false)
	private PCPieChartCompRender render = null;

	public PCPieChartCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCPieChartCompRender(this));
		}
		return render;

	}
	

	@Override
	public void updateOption() {
		this.getRender().updateProp();
	}

	
	
	public Option getDemoOption0() {
		GenericChartData chartData = new GenericChartData();
		chartData.setLegend_data(new String[] { "直接访问","邮件营销","联盟广告","视频广告","搜索引擎"});
		chartData.setxAxis_data(new String[] {});
		SeriesDataWithName seriesData0 = new SeriesDataWithName();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append("{value:335, name:\"直接访问\"},");
		builder.append("{value:310, name:\"邮件营销\"},");
		builder.append("{value:234, name:\"联盟广告\"},");
		builder.append("{value:1548,name:\"视频广告\"},");
		builder.append("{value:15, name:\"搜索引擎\"},");
		builder.append("]");
		seriesData0.setSeries_data(builder.toString());

		SeriesDataWithName seriesData1 = new SeriesDataWithName();
		seriesData1.setSeries_data(builder.toString());

		Option option = new Option();
		Title title = new Title();
		title.setText("某站点用户访问来源");
		title.setSubtext("纯属虚构");
		title.setX("center");
		option.setTitle(title);
		Tooltip toolTip = new Tooltip();
		toolTip.setTrigger("item");
		toolTip.setFormatter("{a}<br/>{b}:{c}({d}%)");
		option.setTooltip(toolTip);
		Legend legend = new Legend();
		legend.setOrient("vertical");
		legend.setX("left");
		legend.setData(chartData.getLegend_data());
		option.setLegend(legend);
		option.setCalculable(true);
		

		PieSeries series0 = new PieSeries();
		{
			series0.setName("访问来源");
			series0.setType("pie");
			series0.setRadius(new String[] { "55%" });
			series0.setCenter(new String[] { "50%", "60%" });
			series0.setRoseType("radius");
			series0.setData(seriesData0.getSeries_data());
		}
		
		option.setSeries(new PieSeries[] { series0 });
		return option;
	}
	public Option getDemoOption1() {
		GenericChartData chartData = new GenericChartData();
		chartData.setLegend_data(new String[] { "rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8" });
		chartData.setxAxis_data(new String[] {});
		SeriesDataWithName seriesData0 = new SeriesDataWithName();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append("{value:10, name:\"rose1\"},");
		builder.append(" {value:5, name:\"rose2\"},");
		builder.append("{value:15, name:\"rose3\"},");
		builder.append("{value:25, name:\"rose4\"},");
		builder.append("{value:20, name:\"rose5\"},");
		builder.append("{value:35, name:\"rose6\"},");
		builder.append("{value:30, name:\"rose7\"},");
		builder.append("{value:40, name:\"rose8\"}");
		builder.append("]");
		seriesData0.setSeries_data(builder.toString());

		SeriesDataWithName seriesData1 = new SeriesDataWithName();
		seriesData1.setSeries_data(builder.toString());

		Option option = new Option();
		Title title = new Title();
		title.setText("南丁格尔玫瑰图");
		title.setSubtext("纯属虚构");
		title.setX("center");
		option.setTitle(title);
		Tooltip toolTip = new Tooltip();
		toolTip.setTrigger("item");
		toolTip.setFormatter("{a}<br/>{b}:{c}({d}%)");
		option.setTooltip(toolTip);
		Legend legend = new Legend();
		legend.setX("center");
		legend.setY("bottom");
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

		PieSeries series0 = new PieSeries();
		{
			series0.setName("半径模式");
			series0.setType("pie");
			series0.setRadius(new String[] { "20", "110" });
			series0.setCenter(new String[] { "25%", "200" });
			series0.setRoseType("radius");
			series0.setWidth("40%");
			series0.setMax(40);
			series0.setData(seriesData0.getSeries_data());
		}
		PieSeries series1 = new PieSeries();
		{
			series1.setName("面积模式");
			series1.setType("pie");
			series1.setRadius(new String[] { "30", "100" });
			series1.setCenter(new String[] { "75%", "200" });
			series1.setRoseType("area");
			series1.setMax(40);
			series1.setSort("ascending");
			series1.setX("50%");
			series1.setData(seriesData1.getSeries_data());
		}
		option.setSeries(new PieSeries[] { series0, series1 });
		return option;
	}
}
