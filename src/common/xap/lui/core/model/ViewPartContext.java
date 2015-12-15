package xap.lui.core.model;
import com.alibaba.fastjson.annotation.JSONField;
import xap.lui.core.layout.UIPartMeta;
public class ViewPartContext implements IUIContext {
	private String id;
	private ViewPartMeta viewPart;
	private UIPartMeta uiMeta;
	@JSONField(serialize = false)
	public UIPartMeta getUIMeta() {
		return uiMeta;
	}
	public void setUIMeta(UIPartMeta um) {
		this.uiMeta = um;
	}
	public void setId(String widgetId) {
		this.id = widgetId;
	}
	@JSONField(serialize = false)
	public String getId() {
		return id;
	}
	@JSONField(deserialize = true)
	public void setView(ViewPartMeta widget) {
		this.viewPart = widget;
	}
	@JSONField(serialize = true)
	public ViewPartMeta getView() {
		return this.viewPart;
	}
	public void reset() {}
}
