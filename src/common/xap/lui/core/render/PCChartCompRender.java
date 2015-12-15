package xap.lui.core.render;

import xap.lui.core.comps.ChartBaseComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIChartComp;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 图标渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCChartCompRender extends UINormalComponentRender<UIChartComp, ChartBaseComp> {

	public PCChartCompRender(ChartBaseComp webEle) {
		super(webEle);
	}

	public String createBody() {

		StringBuilder buf = new StringBuilder();
		ChartBaseComp chart = this.getWebElement();
		UIComponent uiComp = this.getUiElement();

		//ChartConfig config = chart.getConfig();

		String chartId = getVarId();

		// ChartComp(parent, name, left, top, width, height, chartconfig,
		// position, className)

		//buf.append(config.GenCreateScript(chartId));

		buf.append("var ").append(chartId).append(" = new ChartComp(document.getElementById('").append(getDivId()).append("'),'").append(chart.getId()).append("','0','0','100%','100%',config_")
				.append(chartId);

		buf.append(",'relative','").append(uiComp.getClassName()).append("');\n");

		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent(" + chartId + ");\n");

		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var " + widget + " = pageUI.getViewPart('" + this.getCurrWidget().getId() + "') \n");
		buf.append(chartId + ".viewpart=" + widget + ";\n");

		//buf.append(chartId + ".setDataset(" + widget + ".getDataset('" + chart.getChartModel().getDataset() + "'));\n");
		if (chart.isVisible() == false)
			buf.append(chartId + ".hide();\n");
		/*
		 * String captionfunc = config.getCaptionFunction(); }
		 */
		return buf.toString();
	}



	protected String getSourceType(IEventSupport ele) {

		return LuiPageContext.SOURCE_TYPE_CHART;
	}

}
