package xap.lui.core.render;


import xap.lui.core.command.CmdInvoker;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;


/**
 * 移动控件命令，由Editable.js中dragEnd()方法触发
 * 
 * @author liujmc
 */
public class MoveCmd extends AbstractRaCommand {

	String oper;
	
	UIElement uiEle;
	
	public MoveCmd(RaParameter rp ,String oper, UIElement uiEle) {
		super(rp);
		this.oper = oper;
		this.uiEle = uiEle;
	}

	@Override
	public void execute() {
		String compId = rp.getCurrentDropObj();
		if(compId == null){
			rp.getUiId();
		}
		UIElement child = UIElementFinder.findElementById(rp.getUiMeta(),compId);
		
		//1:删除所选的控件
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();		
		//将ajax的状态置为nullstatus
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		//后台删除
		deleteUIElement(rp.getUiMeta(),rp.getPageMeta(),child);
		RequestLifeCycleContext.get().setPhase(phase);		
		
		//2:添加之前删除目标位置容器中的内容
		UILayoutPanel lp = (UILayoutPanel)uiEle;
		if(lp.getElement()!=null){
			rp.setUiId(lp.getId());
			rp.setSubuiId(lp.getElement().getId());
			CmdInvoker.invoke(new DeleteCmd(rp));
		}
		
		//3:将新的控件添加到拖放的位置中，状态已经置回，回触发相应的notifyChange()
		if(child  instanceof UIGridComp){
		    ((UIGridComp) child).setAutoExpand(UIConstant.FALSE); 
		}
		this.addUIElement(uiEle, child);
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = rp.getWidgetId();
		paramObj.uiId = child.getId();
		
		if(child instanceof UIComponent){
			paramObj.eleId = child.getId();
			paramObj.type = rp.getCurrentDropObjType() == null ? rp.getCurrentDropObj() : rp.getCurrentDropObjType();
			//callServer(paramObj, RaParameter.ADD);
		}
	}
	
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
