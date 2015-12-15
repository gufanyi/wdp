/**
 * 
 */
package xap.lui.core.render;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;

/**
 * @author chouhl
 *
 */
public class UpdateIdCmd extends AbstractRaCommand {

	/**
	 * @param rp
	 */
	public UpdateIdCmd(RaParameter rp) {
		super(rp);
	}

	@Override
	public void execute() {
		this.doUpdate(rp.getUiMeta(), rp.getPageMeta(), rp.getParam());
	}
	
	private void doUpdate(UIPartMeta uiMeta, PagePartMeta pageMeta, UpdateParameter param) {
		String compId = param.getCompId();
		String viewId = param.getViewId();
		
		UIElement uiEle = null;
		LuiElement webEle = null;
		
		if(compId.indexOf(".") != -1){
			String[] ids = compId.split("\\.");
			if("gridpanel".equals(param.getCompType())){
				uiEle = findGridCell(uiMeta, uiEle, ids);
			}
			else{
				uiEle = UIElementFinder.findElementById(uiMeta, ids[0], ids[1]);
				webEle = UIElementFinder.findWebElementById(pageMeta, viewId, ids[0], ids[1]);
			}
		}
		else{
			uiEle = UIElementFinder.findElementById(uiMeta, compId);
			webEle = UIElementFinder.findWebElementById(pageMeta, viewId, compId);
		}
		
		if(webEle == null){
			throw new LuiRuntimeException("只有UI控件允许修改ID!");
		}
	
		if (uiEle == null && webEle == null)
			return;
		
		ILuiRender render =uiEle.getRender();
		String renderType = ((UIRender)render).getRenderType(webEle);
		String newValue = param.getNewValue();
		if(isNewIdExist(pageMeta.getWidget(viewId), newValue)){
			throw new LuiRuntimeException("新ID："+newValue+" 在已有控件中已存在!");
		}
		
		if(isNewIdExist(uiMeta, newValue)){
			throw new LuiRuntimeException("新ID："+newValue+"在已有布局中已存在!");
		}
		UILayoutPanel panel = (UILayoutPanel) UIElementFinder.findParent(uiMeta, uiEle);
		
		try {
			String key = webEle.getId();
//			setContext(webEle, param);
			WebComp newEle = (WebComp)webEle.clone();
			newEle.setId(newValue);
			rp.setUiId(key);
			CmdInvoker.invoke(new DeleteCmd(rp));
			
			pageMeta.getWidget(viewId).getViewComponents().removeComponent(key);
			pageMeta.getWidget(viewId).getViewComponents().addComponent(newEle);
			UIElement newUIEle = (UIElement) uiEle.doClone();
			LifeCyclePhase ajax = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			newUIEle.setId(newValue);
			RequestLifeCycleContext.get().setPhase(ajax);
			panel.setElement(newUIEle);

			ParamObject paramObj = new ParamObject();
			paramObj.widgetId = rp.getWidgetId();
			paramObj.uiId = newValue;
			paramObj.eleId = newValue;
			
			if(newUIEle instanceof UIFormElement)
				paramObj.type = LuiPageContext.SOURCE_FORMELEMENT;
			else
				paramObj.type = renderType;
			callServer(paramObj, RaParameter.ADD);
		} 
		catch (Exception e) {
			throw new LuiRuntimeException("控件 "+webEle.getId()+"修改成新ID"+newValue+"失败!");
		}
	}
	/**
	 * 检验newID在widget控件中是否存在
	 * @param widget
	 * @param newId
	 * @return
	 */
	private boolean isNewIdExist(ViewPartMeta widget, String newId){
		boolean isExist = false;
		while(widget != null){
			WebComp comp = widget.getViewComponents().getComponent(newId);
			if(comp != null){
				isExist = true;
				break;
			}
			comp = widget.getViewMenus().getMenuBar(newId);
			if(comp != null){
				isExist = true;
				break;
			}
			comp = widget.getViewMenus().getContextMenu(newId);
			if(comp != null){
				isExist = true;
				break;
			}
			widget = widget.getWidget();
		}
		return isExist;
	}
	/**
	 * 检验newID在uimeta布局中是否存在
	 * @param uiMeta
	 * @param newId
	 * @return
	 */
	private boolean isNewIdExist(UIPartMeta uiMeta, String newId){
		boolean isExist = false;
		UIElement uiEle = UIElementFinder.findElementById(uiMeta, newId);
		if(uiEle != null){
			isExist = true;
		}
		return isExist;
	}
	
}
