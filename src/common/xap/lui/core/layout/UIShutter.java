package xap.lui.core.layout;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCOutlookbarCompRender;
import xap.lui.core.render.notify.RenderProxy;




public class UIShutter extends UILayout {
	private static final long serialVersionUID = -4232393027776208793L;
	public static final String CURRENT_ITEM = "currentItem";
	
	public static final String CLASSNAME = "className";
	private String className;
	private Integer currentItem;
	private String id;
	private String widgetId;
	public static final String WIDGET_NAME = "outlookbar";
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
	this.className=className;
	}

	
	public Integer getCurrentItem() {
		return currentItem;
	}
	
	public void setCurrentItem(Integer value){
		this.currentItem=value;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setActiveItem(value);
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id=id;
	}

	
	public String getViewId() {
		return widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId=widgetId;
	}

	@Override
	public PCOutlookbarCompRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCOutlookbarCompRender(this));
		}
		return (PCOutlookbarCompRender)render;
	}
	
	
}
