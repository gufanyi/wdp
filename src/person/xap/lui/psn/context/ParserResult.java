package xap.lui.psn.context;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;

public class ParserResult {
	private ViewPartMeta widget = null;
	private UIPartMeta uiMeta = null;
	
	public ParserResult() {
	}
	
	public ParserResult(ViewPartMeta widget, UIPartMeta uiMeta) {
		super();
		this.widget = widget;
		this.uiMeta = uiMeta;
	}
	
	public ViewPartMeta getWidget() {
		return widget;
	}
	
	public void setWidget(ViewPartMeta widget) {
		this.widget = widget;
	}
	
	public UIPartMeta getUiMeta() {
		return uiMeta;
	}
	
	public void setUiMeta(UIPartMeta uiMeta) {
		this.uiMeta = uiMeta;
	}
}
