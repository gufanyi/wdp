package xap.lui.core.render;

import xap.lui.core.comps.LineChartComp;
import xap.lui.core.echar.Option;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UILineCharComp;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("unchecked")
public class PCLineChartCompRender extends UINormalComponentRender<UILineCharComp, LineChartComp> {

	public PCLineChartCompRender(LineChartComp webEle) {
		super(webEle);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return "line";
	}

	public String createBody() {
		LineChartComp chart = (LineChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();		
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		return buf.toString();
	}
	
	public void updateProp(){
		LineChartComp chart = (LineChartComp) this.getWebElement();
		StringBuilder buf = new StringBuilder();
		String str1=chart.getProp();
		buf.append("$('#"+getDivId()+"').remove();\n");
		buf.append("var myChart = echarts.init($ge('" + getDivId() + "'));\n");
		buf.append("var option = eval("+str1+");\n");
		buf.append("myChart.setOption(option);");
		this.addDynamicScript(buf.toString());
	}

}
