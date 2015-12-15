package xap.lui.core.comps;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.render.ILuiRender;
public class ViewElement extends LuiElement {
	private static final long serialVersionUID = 6347664694295884243L;
	private ViewPartMeta widget;
	public ViewElement(String id) {
		super(id);
	}
	public ViewElement() {
		super();
	}
	public ViewPartMeta getWidget() {
		return widget;
	}
	public void setWidget(ViewPartMeta widget) {
		this.widget = widget;
	}
	@Override
	public String getWidgetName() {
		return null;
	}
	public ILuiRender getRender(){
		return null;
	}
}
