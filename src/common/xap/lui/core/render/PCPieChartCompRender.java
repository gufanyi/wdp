package xap.lui.core.render;

import xap.lui.core.comps.BarChartComp;
import xap.lui.core.comps.PieChartComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIPieChartComp;

public class PCPieChartCompRender extends UINormalComponentRender<UIPieChartComp, PieChartComp> {

	public PCPieChartCompRender(PieChartComp webEle) {
		super(webEle);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return "pie";
	}

	@Override
	public String createBody() {
		PieChartComp chart = (PieChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		return buf.toString();
	}
	
	
	public void updateProp(){
		PieChartComp chart = (PieChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		this.addDynamicScript(buf.toString());
	}

}
