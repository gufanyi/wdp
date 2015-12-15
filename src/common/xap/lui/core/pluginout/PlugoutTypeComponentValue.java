package xap.lui.core.pluginout;

import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.ViewPartContext;

public class PlugoutTypeComponentValue implements IPlugoutType {

	@Override
	public void buildSourceWidgetRule(WidgetRule widgetRule, String source) {
		//默认就会提交控件Context，不需要设置
	}

	@Override
	public Object fetchContent(PipeOutItem item, ViewPartContext viewCtx) {
		String compId = item.getSource();
		WebComp comp = (WebComp)viewCtx.getView().getViewComponents().getComponent(compId);
		if (comp instanceof TextComp) {
			return ((TextComp)comp).getValue();
		}else{
			return null;
		}
	}

}
