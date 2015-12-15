package xap.lui.core.dataset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.render.PCViewLayOutRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 聚合类型.实现类只需在调用getAllCombItems时返回正确的数据即可
 * 
 * @author dengjt
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ComboData extends ViewElement {

	private static final long serialVersionUID = 6716603920828925202L;

	@XmlAttribute
	private String caption;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public abstract DataItem[] getAllDataItems();

	@JSONField(serialize=false)
	private PCViewLayOutRender render = null;

	@JSONField(serialize=false)
	public PCViewLayOutRender getRender() {
		if (render == null) {
			ViewPartMeta  webElement=  this.getWidget();
	        UIPartMeta  uiPartMeta=LuiRenderContext.current().getUiPartMeta();
	        UIViewPart uiEle = UIElementFinder.findUIWidget(uiPartMeta, webElement.getId());
			render = RenderProxy.getRender(new PCViewLayOutRender(uiEle));
		}
		return render;
	}

	public String matchText(String value) {
		DataItem[] items = getAllDataItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				DataItem item = items[i];
				if (item.getValue().equals(value))
					return item.getText();
			}
		}
		return null;
	}

	public void removeDataItem(String itemId) {
		// DataChangeScript();
		// notifyComboChange();
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setDataList(this);
		}
	}

	public void removeAllDataItems() {
		// notifyComboChange();
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setDataList(this);
		}
	}

	public void addDataItem(DataItem item) {
		// 每次调用替换会有问题，导致只剩最后一项。
		// notifyComboChange();
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setDataList(this);
		}
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	
	// protected void notifyComboChange(){
	// if(LifeCyclePhase.ajax.equals(getPhase())){
	// Map<String,Object> map = new HashMap<String,Object>();
	// if(this.getWidget() != null){
	// String widgetId = this.getWidget().getId();
	// map.put("widgetId", widgetId);
	// map.put("comboDataId", this.getId());
	// map.put("type", "comboChange");
	// this.getWidget().notifyChange(UIElement.UPDATE, map);
	// }
	// }
	// }
}
