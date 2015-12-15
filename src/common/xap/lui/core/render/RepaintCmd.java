package xap.lui.core.render;

import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;

/**
 * 重画进行修改的前台cmd类
 * @author wupeng1
 *
 */
public class RepaintCmd extends AbstractRaCommand {

	public RepaintCmd(RaParameter rp) {
		super(rp);
	}

	@Override
	public void execute() {
		
		String id = rp.getEleId();
		UIComponent child = (UIComponent) UIElementFinder.findElementById(rp.getUiMeta(), id);
		UIElement pPanel = UIElementFinder.findParent(rp.getUiMeta(), child);
		//1:删除所选的控件
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();		
		//将ajax的状态置为nullstatus
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		//后台删除formcomp操作
		deleteUIElement(rp.getUiMeta(),rp.getPageMeta(),child);
		RequestLifeCycleContext.get().setPhase(phase);		
		
		//2:将新的控件添加到拖放的位置中，状态已经置回，回触发相应的notifyChange()
		if(child  instanceof UIGridComp){
			((UIGridComp) child).setAutoExpand(UIConstant.FALSE); 
		}
		
		this.addUIElement(pPanel, child);
		
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = rp.getWidgetId();
		paramObj.uiId = child.getId();
		
		paramObj.eleId = child.getId();
		paramObj.type = rp.getType();
		callServer(paramObj, RaParameter.ADD);
	
	}
	
	//产出UI界面，并触发前台脚本
	public void deleteUIElement(UIPartMeta uiMeta, PagePartMeta pageMeta,UIElement uiEle){
		if (rp.getEleId() == null && rp.getUiId() == null && rp.getSubEleId() == null && rp.getSubuiId() == null)
			return;
		UIElement parent = null;
		parent = UIElementFinder.findParent((UIPartMeta) uiMeta, uiEle);
		removeUIElement(parent, uiEle);
		
		//处理导航
		ParamObject param = new ParamObject();
		if(uiEle != null)
			param.subuiId = uiEle.getId();
		if(parent != null)	
			param.uiId = parent.getId();
		param.type = rp.getType();
		param.widgetId = rp.getWidgetId();
		callServer(param, "delete");		
	}

}
