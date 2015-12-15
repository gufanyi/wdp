package xap.lui.core.render;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.GridComp;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabRightPanel;
import xap.lui.core.model.PagePartMeta;
/**
 * 删除命令
 * 
 * @author licza
 * 
 */
public class DeleteCmd extends AbstractRaCommand {
	public DeleteCmd(RaParameter rp) {
		super(rp);
	}
	@Override
	public void execute() {
		delete(rp.getUiMeta(), rp.getPageMeta());
	}
	private void delete(UIPartMeta uiMeta, PagePartMeta pageMeta) {
		if (rp.getEleId() == null && rp.getUiId() == null && rp.getSubEleId() == null && rp.getSubuiId() == null)
			return;
		ParamObject param = new ParamObject();
		UIElement uiEle = null;
		UIElement parent = null;
		String childId = "";
		// 表格数据
		if (rp.getRowIndex() != null) {
			uiEle = getGridElement(rp);
			if (uiEle instanceof UIGridPanel && ((UIGridPanel) uiEle).getElement() != null) {
				UIGridPanel cell = (UIGridPanel) uiEle;
				UIElement ele = cell.getElement();
				parent = cell;
				childId = ele.getId();
				removeUIElement(cell, ele);
			} else {
				UIGridPanel gridPanel = (UIGridPanel) uiEle;
				parent = gridPanel.getParent();
				removeUIElement(parent, gridPanel);
			}
		} else {
			if (rp.getSubuiId() != null && !rp.getSubuiId().equals("")) {
				uiEle = UIElementFinder.findElementById(uiMeta, rp.getUiId(), rp.getSubuiId());
				if (uiEle == null)
					uiEle = UIElementFinder.findElementById(uiMeta, rp.getSubuiId());
			} else {
				if (rp.getSubEleId() != null && !rp.getSubEleId().equals("")) {
					if ("form_element".equals(rp.getType())) {
						if(rp.getUiId() == null) {
							FormComp form = (FormComp) pageMeta.getWidget(rp.getWidgetId()).getViewComponents().getComponent(rp.getEleId());
							param.subuiId = rp.getSubEleId();
							form.removeElementById(rp.getSubEleId());
						} else {
							uiEle = UIElementFinder.findElementById(uiMeta, rp.getUiId());
						}
					}
					if ("grid_header".equals(rp.getType()) && rp.getUiId() == null) {
						GridComp grid = (GridComp) pageMeta.getWidget(rp.getWidgetId()).getViewComponents().getComponent(rp.getEleId());
						param.subuiId = rp.getSubEleId();
						grid.removeColumnByField(rp.getSubEleId());
					}
				} else {
					uiEle = UIElementFinder.findElementById(uiMeta, rp.getUiId());
				}
			}
			
			if(uiEle instanceof UIPartMeta) {
				uiEle = UIElementFinder.findParent((UIPartMeta) uiMeta, uiEle);
			}
			
			parent = UIElementFinder.findParent((UIPartMeta) uiMeta, uiEle);
			removeUIElement(parent, uiEle);
			
			if(uiEle instanceof UITabRightPanel) {
				UITabComp tabComp = (UITabComp)((UITabRightPanel) uiEle).getLayout();
				tabComp.removeRightPanel(uiEle.getId(), true);
			}
		}
		if (uiEle != null)
			param.subuiId = uiEle.getId();
		if (uiEle instanceof UIGridPanel && ((UIGridPanel) uiEle).getElement() != null) {
			param.subuiId = childId;
		}
		if (parent != null)
			param.uiId = parent.getId();
		param.type = rp.getType();
		param.widgetId = rp.getWidgetId();
		//callServer(param, "delete");
	}
}
