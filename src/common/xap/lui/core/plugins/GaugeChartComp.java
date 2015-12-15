package xap.lui.core.plugins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.comps.ChartBaseComp;
import xap.lui.core.dataset.GenericChartData;
import xap.lui.core.dataset.SeriesData;
import xap.lui.core.dataset.SeriesDataWithName;
import xap.lui.core.echar.Option;
import xap.lui.core.echar.Tooltip;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "GaugeChart")
@XmlAccessorType(XmlAccessType.NONE)
public class GaugeChartComp extends ChartBaseComp {

	private static final long serialVersionUID = 262012915856475540L;

	@JSONField(serialize = false)
	private PCGaugeCompRender render = null;

	public PCGaugeCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCGaugeCompRender(this));
		}
		return render;

	}

	@Override
	public void updateOption() {
		this.getRender().updateProp();
	}

	public Option getDemoOption0() {
		GenericChartData chartData = new GenericChartData();

		SeriesDataWithName seriesData0 = new SeriesDataWithName();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append("{value:50, name:\"完成率\"}");
		builder.append("]");
		seriesData0.setSeries_data(builder.toString());
		chartData.setSeries_data(new SeriesData[] { seriesData0 });

		Option option = new Option();

		Tooltip toolTip = new Tooltip();
		toolTip.setFormatter("{a}<br/>{b}:{c}%");
		option.setTooltip(toolTip);

		GaugeSeries series0 = new GaugeSeries();
		{
			series0.setName("业务指标");
			series0.setType("gauge");
			series0.setData(seriesData0.getSeries_data());
			Detail detail = new Detail();
			detail.setFormatter("{value}%");
			series0.setDetail(detail);
		}

		option.setSeries(new GaugeSeries[] { series0 });
		return option;
	}

}
