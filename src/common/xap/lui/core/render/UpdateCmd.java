package xap.lui.core.render;

import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.TreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

public class UpdateCmd extends AbstractRaCommand {

	public UpdateCmd(RaParameter rp) {
		super(rp);
	}

	@Override
	public void execute() {
		this.doUpdate(rp.getUiMeta(), rp.getPageMeta());
	}
	private void doUpdate(UIPartMeta uiMeta, PagePartMeta pageMeta) {
		UpdateParameter param = rp.getParam();
		String compId = param.getCompId();
		String viewId = param.getViewId();
		String compType = param.getCompType();
		UIElement uiEle = null;
		LuiElement webEle = null;
		if(compId.indexOf(".") != -1){
			String[] ids = compId.split("\\.");
			if("gridpanel".equals(compType)){
				uiEle = findGridCell(uiMeta, uiEle, ids);
			}else if("menubar_menuitem".equals(compType)){
				webEle = UIElementFinder.findMenuItemById(pageMeta, viewId, ids[0], ids[1]);
			}else{
				uiEle = UIElementFinder.findElementById(uiMeta, ids[0], ids[1]);
				if(uiEle == null)
					uiEle = UIElementFinder.findElementById(uiMeta, ids[1]);
				webEle = UIElementFinder.findWebElementById(pageMeta, viewId, ids[0], ids[1]);
			}
		}else if (rp.getSubEleId() != null){
			uiEle = UIElementFinder.findElementById(uiMeta, rp.getUiId());
			webEle = UIElementFinder.findWebElementById(pageMeta, viewId, rp.getEleId(), rp.getSubEleId());
		}
		else{
			uiEle = UIElementFinder.findElementById(uiMeta, compId);
			webEle = UIElementFinder.findWebElementById(pageMeta, viewId, compId);
			//增加判断，根据FormComp查找webelement
			if(webEle==null){
				ViewPartMeta widget = pageMeta.getWidget(viewId);
				if(widget != null) {
					WebComp[] components = widget.getViewComponents().getComps();
					for(WebComp comp : components){
						if(comp instanceof FormComp){
							webEle = ((FormComp)comp).getElementById(compId);
							break;
						}
					}
					//处理dataset的同步
					webEle = widget.getViewModels().getDataset(compId);
				}
			}
		}
	
		if (uiEle == null && webEle == null)
			return;
		
		if (webEle != null) {
			if(("validateFormula".equals(param.getAttr()) || "editFormular".equals(param.getAttr()) || "precision".equals(param.getAttr()) ) &&  ("form_element".equals(param.getCompType())||"grid_header".equals(param.getCompType()))){
				Field feild = null;
				if("form_element".equals(param.getCompType())){
					FormElement gc = (FormElement) webEle;
					ViewPartMeta widget = rp.getPageMeta().getWidget(gc.getParent().getWidget().getId());
					Dataset ds = widget.getViewModels().getDataset(gc.getParent().getDataset());
					feild = ds.getField(gc.getField());
				}
				if("grid_header".equals(param.getCompType())){
					GridColumn gc = (GridColumn) webEle;
					ViewPartMeta widget = rp.getPageMeta().getWidget(gc.getGridComp().getWidget().getId());
					Dataset ds = widget.getViewModels().getDataset(gc.getGridComp().getDataset());
					feild = ds.getField(gc.getField());
				}
				if(feild != null){
					if("validateFormula".equals(param.getAttr())){
						feild.setValidateFormula(param.getNewValue());
					}
					if("editFormular".equals(param.getAttr())){
						feild.setEditFormular(param.getNewValue());
					}
					if("precision".equals(param.getAttr())){
						feild.setPrecision(param.getNewValue());
					}
				}
			} else if("treeLevel1".equals(param.getAttr())) {
				TreeViewComp tree = (TreeViewComp)webEle;
				tree.setTopLevel(tree.getTopLevel());
			} else if("treeLevel2".equals(param.getAttr())) {
				TreeViewComp tree = (TreeViewComp)webEle;
				TreeLevel topLevel = tree.getTopLevel();
				topLevel.setChildTreeLevel(topLevel.getChildTreeLevel());
			} else {
				try {
					setContext(webEle, param);
				} 
				catch (Exception e) {
					try {
						setContext(uiEle, param);
					} 
					catch (Exception e1) {
						LuiLogger.error(e1.getMessage(), e1);
					}
				}
			}
			
		} 
		else{
			try {
				setContext(uiEle, param);
			} 
			catch (Exception e1) {
				throw new LuiRuntimeException(e1.getMessage(), e1);
			}
		}
	}

	
	 
}
