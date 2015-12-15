package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 布局中的panel 渲染器
 */
@SuppressWarnings("unchecked")
public class PCGridRowPanelRender extends UILayoutPanelRender<UIGridRowPanel, LuiElement> {

	private String tableId = null;

	public PCGridRowPanelRender(UIGridRowPanel uiEle) {
		super(uiEle);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_GRIDROWPANEL;
	}

	public String place() {
		StringBuffer buf = new StringBuffer();
		// buf.append(this.generalHeadHtmlDynamic());
		UILayoutPanel panel = this.getUiElement();

		// 渲染子节点
		ILuiRender render = panel.getElement().getRender();
		if (render != null) {
			if (render instanceof PCGridRowLayoutRender) {
				((PCGridRowLayoutRender) render).setTableId(getTableId());
			}
			buf.append(render.place());
		}

		this.divId = render.getDivId();
		return buf.toString();
	}

	public String create() {
		StringBuffer buf = new StringBuffer();

		// 渲染子节点
		UILayoutPanel panel = this.getUiElement();
		ILuiRender render = panel.getElement().getRender();
		if (render != null) {
			buf.append(render.create());
		}

		return buf.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}

	/**
	 * Object obj 父节点
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		UIGridRowLayout row = (UIGridRowLayout) this.getUiElement().getElement();
		row.getRender().destroy();
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	@Override
	public String placeSelf() {
		// TODO Auto-generated method stub
		return "";
	}

}
