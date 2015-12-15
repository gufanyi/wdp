package xap.lui.core.pluginout;

import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.ViewPartContext;

public interface IPlugoutType {
	/*数据集选中行(单行)*/
	public static final String TYPE_DATASET_SEL_ROW = "TYPE_DATASET_SEL_ROW";
	/*数据集所有选中行*/
	public static final String TYPE_DATASET_MUTL_SEL_ROW = "TYPE_DATASET_MUTL_SEL_ROW";
	/*数据集所有行*/
	public static final String TYPE_DATASET_ALL_ROW = "TYPE_DATASET_ALL_ROW";
	
	/*控件值*/
	public static final String TYPE_COMPONENT_VALUE = "TYPE_COMPONENT_VALUE";
	
	public Object fetchContent(PipeOutItem item, ViewPartContext viewCtx);
	
	public void buildSourceWidgetRule(WidgetRule widgetRule, String source);
}
