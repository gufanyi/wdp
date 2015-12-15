package xap.lui.core.plugins;

import xap.lui.core.event.IEventSupport;
import xap.lui.core.render.UINormalComponentRender;

@SuppressWarnings("rawtypes")
public class PCGaugeCompRender extends UINormalComponentRender {

	@SuppressWarnings("unchecked")
	public PCGaugeCompRender(GaugeChartComp webEle) {
		super(webEle);
	}
	
	
	

	@Override
	protected String getSourceType(IEventSupport ele) {
		return "gauge";
	}

	@Override
	public String createBody() {
		GaugeChartComp chart = (GaugeChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		buf.append("try{clearInterval(timeTicket);}catch(error){}\n");
		buf.append("timeTicket = setInterval(function (){\n");
		buf.append("option.series[0].data[0].value = (Math.random()*100).toFixed(2) - 0;\n");
		buf.append("myChart.setOption(option, true);\n");
		buf.append("},2000);\n");
		return buf.toString();
	}
	
	
	public void updateProp(){
		GaugeChartComp chart = (GaugeChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		
		
		this.addDynamicScript(buf.toString());
	}

}
