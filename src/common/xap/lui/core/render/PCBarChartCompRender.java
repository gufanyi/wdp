package xap.lui.core.render;

import xap.lui.core.comps.BarChartComp;
import xap.lui.core.comps.LineChartComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIBarChartComp;

@SuppressWarnings("unchecked")
public class PCBarChartCompRender extends UINormalComponentRender<UIBarChartComp, BarChartComp> {

	public PCBarChartCompRender(BarChartComp webEle) {
		super(webEle);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return "bar";
	}

	@Override
	public String createBody() {
		BarChartComp chart = (BarChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		return buf.toString();
	}
	
	public void updateProp(){
		BarChartComp chart = (BarChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		this.addDynamicScript(buf.toString());
	}

}
