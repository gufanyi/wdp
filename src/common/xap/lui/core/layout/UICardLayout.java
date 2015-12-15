package xap.lui.core.layout;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCCardLayoutRender;
import xap.lui.core.render.notify.RenderProxy;



public class UICardLayout extends UILayout {
	private static final long serialVersionUID = -7255360718452324802L;
	public static final String CURRENT_ITEM = "currentItem";
	
	public static final String WIDGET_NAME = "cardlayout";

	private String id;
	private String currentItem;
	private String widgetId;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id=value;
	}

	public String getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(String value) {
		this.currentItem=value;
		//UpdatePair pair = new UpdatePair(CURRENT_ITEM, value);
		//notifyChange(UPDATE,pair);
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setCardPage(Integer.parseInt(value));
		}
	}
	
	public String getViewId() {
		return widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId = widgetId;
	}

	@Override
	public PCCardLayoutRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCCardLayoutRender(this));
		}
		return (PCCardLayoutRender)render;
	}
	
	
}
